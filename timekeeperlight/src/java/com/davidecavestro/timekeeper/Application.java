/*
 * Application.java
 *
 * Created on 26 novembre 2005, 14.55
 */

package com.davidecavestro.timekeeper;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.gui.HungAwtExit;
import com.davidecavestro.common.gui.persistence.PersistenceStorage;
import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.common.help.HelpManager;
import com.davidecavestro.common.help.HelpResourcesResolver;
import com.davidecavestro.common.log.CompositeLogger;
import com.davidecavestro.common.log.ConsoleLogger;
import com.davidecavestro.common.log.Logger;
import com.davidecavestro.common.log.LoggerAdapter;
import com.davidecavestro.common.log.PlainTextLogger;
import com.davidecavestro.common.undo.RBUndoManager;
import com.davidecavestro.common.util.*;
import com.davidecavestro.timekeeper.conf.ApplicationEnvironment;
import com.davidecavestro.timekeeper.conf.CommandLineApplicationEnvironment;
import com.davidecavestro.timekeeper.conf.UserResources;
import com.davidecavestro.timekeeper.conf.UserSettings;
import com.davidecavestro.timekeeper.tray.SystemTraySupport;
import com.davidecavestro.timekeeper.gui.WindowManager;
import com.davidecavestro.timekeeper.actions.ActionManager;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import com.davidecavestro.timekeeper.conf.DefaultSettings;
import com.davidecavestro.timekeeper.gui.MainWindow;
import com.davidecavestro.timekeeper.gui.Splash;
import com.davidecavestro.timekeeper.model.PersistentTaskTreeModel;
import com.davidecavestro.timekeeper.model.PersistentWorkSpaceModel;
import com.davidecavestro.timekeeper.model.TaskTreeModelExceptionHandler;
import com.davidecavestro.timekeeper.model.WorkSpace;
import com.davidecavestro.timekeeper.model.WorkSpaceModel;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelEvent;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelListener;
import com.davidecavestro.timekeeper.persistence.PersistenceNode;
import com.davidecavestro.timekeeper.persistence.PersistenceNodeException;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.text.DefaultStyledDocument;

/**
 * Il gestore centrale dell'intera applicazione.
 *
 * @author  davide
 */
public class Application {
	private CompositeLogger _logger;
	private final ApplicationEnvironment _env;
	private final ApplicationContext _context;
	private final PersistenceNode _persistenceNode;
	
	/**
	 * Costruttore.
	 */
	public Application (final CommandLineApplicationEnvironment args) {
		_env = args;
		
		
		
		final RBUndoManager undoManager = new RBUndoManager ();
		
		final Properties releaseProps = new Properties ();
		try {
			/*
			 * carica dati di configurazione.
			 */
			releaseProps.load (getClass ().getResourceAsStream ("release.properties"));
		} catch (final Exception e) {
			System.err.println (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Cannot_load_release_properties"));
			/*@todo mostrare stacktrace finito lo sviluppo*/
//			e.printStackTrace (System.err);
		}
		
		final ApplicationData applicationData = new ApplicationData (releaseProps);
		final UserResources userResources = new UserResources (applicationData);
		final UserSettings userSettings = new UserSettings (this, userResources);
		
		final ApplicationOptions applicationOptions = new ApplicationOptions (userSettings, new ApplicationOptions (new DefaultSettings (args, userResources), null));
		
		/**
		 * Percorso del file di configuraizone/mappatura, relativo alla directory di
		 * installazione dell'applicazione.
		 */
		final Properties p = new Properties ();
		try {
			p.load (new FileInputStream (new File (_env.getApplicationDirPath (), "helpmap.properties")));
		} catch (IOException ioe){
//			System.out.println (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Missing_help_resources_mapping_file"));
		}
		
		
		try {
			final File plainTextLogFile = new File (applicationOptions.getLogDirPath (), CalendarUtils.getTS (Calendar.getInstance ().getTime (), CalendarUtils.FILENAME_TIMESTAMP_FORMAT)+".log");
			
//			logDocument.setParser (new javax.swing.text.html.parser.ParserDelegator ());
			
			_logger = new CompositeLogger (new PlainTextLogger (plainTextLogFile, true, 10), null);
			
		} catch (IOException ioe){
			System.out.println (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Logging_disabled._CAUSE:_")+ExceptionUtils.getStackTrace (ioe));
			_logger = new CompositeLogger (new LoggerAdapter (), null);
		}
		
		final TaskTreeModelExceptionHandler peh = new TaskTreeModelExceptionHandler () {};
		_persistenceNode = new PersistenceNode (applicationOptions, _logger);
		/*
		 * salva le informazioni relative al database nelle impostazioni personali
		 */
		userSettings.setJDOStorageDirPath (applicationOptions.getJDOStorageDirPath ());
		userSettings.setJDOStorageName (applicationOptions.getJDOStorageName ());
		userSettings.setJDOUserName (applicationOptions.getJDOUserName ());
		
		final ProgressItem pi = new ProgressItem (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"));
		final Project prj = new Project (pi.getName (), pi);
		final PersistentTaskTreeModel model = new PersistentTaskTreeModel (_persistenceNode, undoManager, applicationOptions, _logger, peh, prj);
		model.addTaskTreeModelListener (new TaskTreeModelListener () {
			public void treeNodesChanged (TaskTreeModelEvent e) {
			}
			public void treeNodesInserted (TaskTreeModelEvent e) {
			}
			public void treeNodesRemoved (TaskTreeModelEvent e) {
			}
			public void treeStructureChanged (TaskTreeModelEvent e) {
			}
			public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
				/*
				 * La variazione del progetto fa rimuovere la coda di undo
				 */
				undoManager.discardAllEdits ();
			}
		});
		final PersistentWorkSpaceModel wsModel = new PersistentWorkSpaceModel (_persistenceNode, applicationOptions, _logger);
		try {
			_persistenceNode.init ();
		} catch (final PersistenceNodeException pne) {
			System.err.print ("Cannot initialize the persistence subsystem. ");
			System.err.println ("Please check to have permissions to write to "+applicationOptions.getJDOStorageDirPath ());
			System.err.println ("If you have the correct permissions, please wait a couple of minutes, so that the current lock become obsolete");
			pne.printStackTrace (System.err);
			exit ();
			/*
			 * Exit dovrebbe termiare la JVM, se non fosse, comunque l'applicazione non deve partire
			 */
			throw new RuntimeException (pne);
		}
		wsModel.init () ;
		
		
		_context = new ApplicationContext (
			_env,
			applicationOptions,
			new WindowManager (),
			new UIPersister (new UserUIStorage (userSettings)),
			_logger,
			userSettings,
			applicationData,
			model,
			wsModel,
			undoManager,
			new ActionManager (),
			new HelpManager (new HelpResourcesResolver (p), "help-contents/JTTSlite.hs"),
			peh,
			_persistenceNode
			);
		
		model.addUndoableEditListener (undoManager);
		
		/**
		 * Assicura la persistenza delle informazioni di configurazione del DB
		 */
		userSettings.setJDOStorageDirPath (applicationOptions.getJDOStorageDirPath ());
		userSettings.setJDOStorageName (applicationOptions.getJDOStorageName ());
		userSettings.setJDOUserName (applicationOptions.getJDOUserName ());
	}
	
	
	/**
	 * Fa partire l'applicazione.
	 */
	public void start (){
		_context.getLogger ().info (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("starting_UI"));
		final WindowManager wm = _context.getWindowManager ();
		
		wm.init (_context);
		wm.setLookAndFeel (_context.getApplicationOptions ().getLookAndFeel (), false);
		
		final Splash splash = wm.getSplashWindow (_context.getApplicationData ());
		splash.show ();
		try {
			splash.showInfo (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Initializing_context..."));
			splash.showInfo (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Initializing_log_console..."));
			final ConsoleLogger cl = new ConsoleLogger (new DefaultStyledDocument (), true);
			
			_context.getWindowManager ().getLogConsole ().init (cl.getDocument ());
			
			_logger.setSuccessor (cl);
			splash.showInfo (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Preparing_main_window..."));
			wm.getMainWindow ().addWindowListener (
				new java.awt.event.WindowAdapter () {
				public void windowClosing (java.awt.event.WindowEvent evt) {
					exit ();
				}
			});
			
			_context.getModel ().setWorkSpace (prepareWorkSpace ());
		
		} finally {
			splash.setVisible (false);
			splash.dispose ();
		}
		
		wm.getMainWindow ().show ();
		_context.getLogger ().info (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("UI_successfully_started"));
	}
	
	public Logger getLogger (){
		return _logger;
	}
	
	/**
	 * Operazioni da effettuare prima dell'uscita dall'applicazione, tra le quali:
	 *	<UL>
	 *		<LI>Salvataggio impostazioni utente.
	 *	</UL>
	 */
	public void beforeExit (){
		_persistenceNode.flushData ();
		
		
		_context.getUIPersisteer ().makePersistentAll ();
		
		
		/* Garantisce la chiusura del logger. */
		_logger.close ();
		
		/* Salva le preferenze utente */
		_context.getUserSettings ().storeProperties ();
	}
	
	private WorkSpace prepareWorkSpace () {
		
		
		
		final WorkSpace lastWorkSpace = findWorkSpace (_context.getApplicationOptions ().getLastProjectName ());
		if (lastWorkSpace!=null) {
			/*
			 * ritorna l'ultimo usato
			 */
			return lastWorkSpace;
		}
		
		final WorkSpaceModel wsm = _context.getWorkSpaceModel ();
		if (wsm.getSize ()>0) {
			/*
			 * ritorna il primo workspace disponibile
			 */
			return wsm.getElementAt (0);
		}
		
		/*
		 * Crea unnuovoworkspace
		 */
		_context.getLogger ().info (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Creating_a_new_empty_workspace..."));
		final WorkSpace newWS = createNewWorkSpace ();
		_context.getLogger ().info (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Empty_workspace_created."));
		
		return newWS;
	}
	
	private WorkSpace createNewWorkSpace () {
		/*
		 * Crea nuovo progetto.
		 */
		final ProgressItem pi = new ProgressItem (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"));
		pi.insert (new ProgressItem (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_task")));
		final WorkSpace ws = new Project (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"), pi);
		_context.getWorkSpaceModel ().addElement (ws);
		
		return ws;
	}
	
	private WorkSpace findWorkSpace (String name) {
		if (name!=null) {
			for (final Iterator<WorkSpace> it =_context.getWorkSpaceModel ().iterator (); it.hasNext ();) {
				final WorkSpace p = it.next ();
				if (name.equals (p.getName ())) {
					return p;
				}
			}
		}
		return null;
	}
	
	/**
	 * Termina l'applicazione.
	 */
	public final void exit (){
		beforeExit ();
		_context.getWindowManager ().disposeAllFrames ();
		HungAwtExit.forceOtherFramesDispose (_context.getWindowManager ().getMainWindow ());
		System.out.println ("Closing application...");
	}
	
	private final class UserUIStorage implements PersistenceStorage {
		private final UserSettings _userSettings;
		public UserUIStorage (final UserSettings userSettings){
			_userSettings = userSettings;
		}
		
		public java.util.Properties getRegistry () {
			return _userSettings.getProperties ();
		}
		
	}
	
	
	/**
	 * Porta la finestra principale dell'applicazione inprimo piano.
	 */
	public void bringToFront () {
		_context.getWindowManager ().getMainWindow ().bringToFront ();		
	}
	
	
}

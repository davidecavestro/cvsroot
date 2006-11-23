/*
 * Application.java
 *
 * Created on 26 novembre 2005, 14.55
 */

package com.davidecavestro.timekeeper;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.gui.persistence.PersistenceStorage;
import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.common.help.HelpManager;
import com.davidecavestro.common.help.HelpResourcesResolver;
import com.davidecavestro.common.log.CompositeLogger;
import com.davidecavestro.common.log.ConsoleLogger;
import com.davidecavestro.common.log.Logger;
import com.davidecavestro.common.log.LoggerAdapter;
import com.davidecavestro.common.log.PlainTextLogger;
import com.davidecavestro.common.util.*;
import com.davidecavestro.timekeeper.conf.ApplicationEnvironment;
import com.davidecavestro.timekeeper.conf.CommandLineApplicationEnvironment;
import com.davidecavestro.timekeeper.conf.UserResources;
import com.davidecavestro.timekeeper.conf.UserSettings;
import com.davidecavestro.timekeeper.gui.WindowManager;
import com.davidecavestro.timekeeper.actions.ActionManager;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import com.davidecavestro.timekeeper.conf.DefaultSettings;
import com.davidecavestro.timekeeper.model.TaskTreeModelImpl;
import com.davidecavestro.timekeeper.model.undo.RBUndoManager;
import com.davidecavestro.timekeeper.model.TaskTreeModelExceptionHandler;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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
	
	/** 
	 * Costruttore.
	 */
	public Application (final CommandLineApplicationEnvironment args) {
		this._env = args;


		
		RBUndoManager undoManager = new RBUndoManager ();

		final Properties releaseProps = new Properties ();
		try {
			/*
			 * carica dati di configurazione.
			 */
			releaseProps.load(getClass().getResourceAsStream ("release.properties"));
		} catch (final Exception e) {
			System.err.println ("Cannot load release properties");
			/*@todo mostrare stacktrace finito lo sviluppo*/
//			e.printStackTrace (System.err);
		}

		final ApplicationData applicationData = new ApplicationData (releaseProps);
		final UserSettings userSettings = new UserSettings (this, new UserResources (applicationData));
		
		final ApplicationOptions applicationOptions = new ApplicationOptions (userSettings, new ApplicationOptions (new DefaultSettings (args), null));
		
		/**
		 * Percorso del file di configuraizone/mappatura, relativo alla directory di 
		 * installazione dell'applicazione.
		 */
		final Properties p = new Properties ();
		try {
			p.load (new FileInputStream (_env.getApplicationDirPath ()+"/helpmap.properties"));
		} catch (IOException ioe){
			System.out.println ("Missing help resources mapping file");
		}
		
		
		try {
			final StringBuffer sb = new StringBuffer ();
			sb.append (applicationOptions.getLogDirPath () ).append ("/")
			.append (CalendarUtils.getTS (Calendar.getInstance ().getTime (), CalendarUtils.FILENAME_TIMESTAMP_FORMAT))
			.append (".log");
			
			final File plainTextLogFile = new File (sb.toString ());
			
//			logDocument.setParser (new javax.swing.text.html.parser.ParserDelegator ());
			
			_logger = new CompositeLogger (new PlainTextLogger (plainTextLogFile, true, 10), null);
			
		} catch (IOException ioe){
			System.out.println ("Logging disabled. CAUSE: "+ExceptionUtils.getStackTrace (ioe));
			this._logger = new CompositeLogger (new LoggerAdapter (), null);
		}
		
		final TaskTreeModelExceptionHandler peh = new TaskTreeModelExceptionHandler () {};

		final ProgressItem pi = new ProgressItem ("foo");
		pi.insert (new ProgressItem ("foo child"));
		pi.addProgress (new Progress (new Date (), new Date (), pi));
		final Project prj = new Project ("foo", pi);
		final TaskTreeModelImpl model = new TaskTreeModelImpl (applicationOptions, peh, prj);
		
		this._context = new ApplicationContext (
			_env,
			applicationOptions,
			new WindowManager (),
			new UIPersister (new UserUIStorage (userSettings)),
			_logger,
			userSettings,
			applicationData,
			model,
			undoManager,
			new ActionManager (),
			new HelpManager (new HelpResourcesResolver (p), "help/MainTimekeeperHelp.hs"),
			peh
			);
		
		model.addUndoableEditListener(undoManager);
		
	}
	
	
	/**
	 * Fa partire l'applicazione.
	 */
	public void start (){
		_context.getLogger().info ("starting UI");
		final WindowManager wm = this._context.getWindowManager ();
		wm.getSplashWindow (this._context.getApplicationData ()).show ();
		try {
			wm.getSplashWindow (this._context.getApplicationData ()).showInfo ("Initializing context...");
			wm.init (this._context);
			final ConsoleLogger cl = new ConsoleLogger (new DefaultStyledDocument(), true);

			_context.getWindowManager ().getLogConsole ().init (cl.getDocument ());

			this._logger.setSuccessor (cl);
			wm.getSplashWindow (this._context.getApplicationData ()).showInfo ("Preparing main window...");
			wm.getMainWindow ().addWindowListener (
				new java.awt.event.WindowAdapter () {
					public void windowClosing (java.awt.event.WindowEvent evt) {
						exit ();
					}
				});
		} finally {
			wm.getSplashWindow (this._context.getApplicationData ()).hide ();
		}
		wm.getMainWindow ().show ();
		_context.getLogger().info ("UI successfully started");
	}
	
	public Logger getLogger (){
		return this._logger;
	}
	
	/**
	 * Operazioni da effettuare prima dell'uscita dall'applicazione, tra le quali:
	 *	<UL>
	 *		<LI>Salvataggio impostazioni utente.
	 *	</UL>
	 */
	public void beforeExit (){
		this._context.getUIPersisteer ().makePersistentAll ();
		
		
		/* Garantisce la chiusura del logger. */
		this._logger.close ();
		
		/* Salva le preferenze utente */
		this._context.getUserSettings ().storeProperties ();
	}

	/**
	 * Termina l'applicazione.
	 */
	public final void exit (){
		beforeExit ();
		System.exit (0);
	}
	
	private final class UserUIStorage implements PersistenceStorage {
		private final UserSettings _userSettings;
		public UserUIStorage (final UserSettings userSettings){
			this._userSettings = userSettings;
		}

		public java.util.Properties getRegistry () {
			return this._userSettings.getProperties ();
		}
		
	}
	
}

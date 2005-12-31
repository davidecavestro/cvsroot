/*
 * Application.java
 *
 * Created on 26 novembre 2005, 14.55
 */

package com.davidecavestro.rbe;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.gui.persistence.PersistenceStorage;
import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.common.log.CompositeLogger;
import com.davidecavestro.common.log.Logger;
import com.davidecavestro.common.log.LoggerAdapter;
import com.davidecavestro.rbe.conf.ApplicationEnvironment;
import com.davidecavestro.rbe.conf.CommandLineApplicationEnvironment;
import com.davidecavestro.rbe.conf.UserResources;
import com.davidecavestro.rbe.conf.UserSettings;
import com.davidecavestro.rbe.gui.WindowManager;
import com.davidecavestro.rbe.gui.actions.ActionManager;
import com.davidecavestro.rbe.model.DefaultResourceBundleModel;
import com.davidecavestro.rbe.model.LocalizationProperties;
import com.davidecavestro.rbe.model.ResourceBundleModel;
import com.davidecavestro.rbe.model.undo.RBUndoManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * Il gestore centrale dell'intera applicazione.
 *
 * @author  davide
 */
public class Application {
	private final Logger _logger;
	private final ApplicationEnvironment _env;
	private final ApplicationContext _context;
	
	/** 
	 * Costruttore.
	 */
	public Application (final CommandLineApplicationEnvironment args) {
		this._env = args;

//		final Locale fooLocale = Locale.ITALIAN;
//		final Properties fooProperties = new Properties ();
//		
//		try {
//			fooProperties.load (new FileInputStream ("/tmp/foo_it.properties"));
//		} catch (FileNotFoundException fnfe){
//			fnfe.printStackTrace (System.err);
//		}catch (IOException ioe){
//			ioe.printStackTrace (System.err);
//		}
//		
//		final Locale fooLocale1 = new Locale ("");
//		final Properties fooProperties1 = new Properties ();
//		
//		try {
//			fooProperties1.load (new FileInputStream ("/tmp/foo.properties"));
//		} catch (FileNotFoundException fnfe){
//			fnfe.printStackTrace (System.err);
//		}catch (IOException ioe){
//			ioe.printStackTrace (System.err);
//		}
		
		DefaultResourceBundleModel model = new DefaultResourceBundleModel ("blank", new LocalizationProperties [] {new LocalizationProperties (LocalizationProperties.DEFAULT, new Properties ())});
		RBUndoManager undoManager = new RBUndoManager ();

		final ApplicationData applicationData = new ApplicationData ();
		final UserSettings userSettings = new UserSettings (this, new UserResources (applicationData));
		this._context = new ApplicationContext (
		new WindowManager (),
		new UIPersister (new UserUIStorage (userSettings)),
		userSettings,
		applicationData,
		model,
		undoManager,
		new ActionManager ()
		);
		model.addUndoableEditListener(undoManager);
		
		this._logger = new CompositeLogger (new LoggerAdapter (), new LoggerAdapter ());
	}
	
	
	/**
	 * Fa partire l'applicazione.
	 */
	public void start (){
		final WindowManager wm = this._context.getWindowManager ();
		wm.init (this._context);
		{
			wm.getMainWindow ().addWindowListener (
			new java.awt.event.WindowAdapter () {
				public void windowClosing (java.awt.event.WindowEvent evt) {
					Application.this.exit ();
				}
			});
		}
		wm.getMainWindow ().show ();
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
//		synchronized (this){
//			setChanged ();
//			notifyObservers (ObserverCodes.APPLICATIONEXITING);
//		}
		this._context.getUIPersisteer ().makePersistentAll ();
		
//		closeActiveStoreData ();
		
		/* Forza chiusura logger. */
		this._logger.close ();
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

/*
 * Application.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.jdo.*;
import javax.swing.*;

import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.log.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.persistence.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Il controller centrale dell'applicazione.
 *
 * @author  davide
 */
public class Application extends Observable{
	
	/**
	 * La finestra principale della GUI.
	 */
	private MainForm mainForm;
	
	/**
	 * Il registro degli eventi dell'applicazione.
	 */
	private static Logger _logger;
	
	/**
	 * Le impostazioni di configurazione dell'applicazione.
	 */
	private static ApplicationOptions _applicationOptions;
	
	/**
	 * L'ambiente di lancio e inizializzazione.
	 */
	private static CommandLineApplicationEnvironment _applicationEnvironment;
	
	/**
	 * Ambiente di configurazione della persistenza.
	 */
	private DataStoreEnvironment _dataStoreEnvironment;
	
	/** 
	 * Costruttore privato. 
	 */
	private Application(String[] args) {
		
		//inizializza il supporto per la persistenza dei dati
		initPersistence ();
		
		final ActionPool actionPool = ActionPool.getInstance ();
		//registra le azioni su Application
		this.addObserver(actionPool.getNodeCreateAction ());
		this.addObserver(actionPool.getNodeDeleteAction ());
		this.addObserver(actionPool.getProgressStartAction ());
		this.addObserver(actionPool.getProgressStopAction ());
		this.addObserver(actionPool.getProjectCreateAction ());
		this.addObserver(actionPool.getProjectDeleteAction ());
		this.addObserver(actionPool.getProjectCloseAction ());
		this.addObserver(actionPool.getProjectOpenAction ());
		this.addObserver(actionPool.getProjectSaveAction ());
		this.addObserver(actionPool.getProjectXMLExportAction ());
		this.addObserver(actionPool.getProjectXMLImportAction ());
		this.addObserver(actionPool.getProgressItemUpdateAction ());
	}
	
	/**
	 * L'istanza di questa applicazione (singleton).
	 */
	private static Application instance = null;
	/**
	 * Il timer lù per la notifica degli avanzamenti.
	 */
	private javax.swing.Timer timer;
	
	/** 
	 * Ritorna l'istanza di questa applicazione.
	 * @return l'istanza di questa applicazione.
	 */
	public static Application getInstance(){
		return getInstance (null);
	}
	
	/**
	 *
	 * Ritorna l'istanza di questa applicazione.
	 * @return l'istanza di questa applicazione.
	 * @param args i parametri di inizializzazione.
	 */
	private final static Application getInstance(String[] args){
		if (instance == null){
			//istanziazione lazy
			instance = new Application(args);
			
			//istanzia la finestra principale dell'interfaccia
			instance.mainForm = new MainForm(instance);
			
			//registra la finestra principale su Application per le notoifiche
			instance.addObserver (instance.mainForm);
			
			//prepara il timer per la notifica del progressing dell'avanzamento
			ActionListener timerActionPerformer = new ActionListener (){
				/**
				 * Gestisce evento timer.
				 */
				public void actionPerformed (ActionEvent ae){
					instance.setChanged ();
					//notifica l'avanzamento
					instance.notifyObservers (ObserverCodes.ITEMPROGRESSINGPERIODCHANGE);
				}
			};
			instance.timer = new javax.swing.Timer (1000, timerActionPerformer);
			instance.timer.stop();
		}
		return instance; 
	}
	
	/** 
	 * Ritorna la finestra principale della GUI per questa applicazione.
	 * @return la finestra principale della GUI per questa applicazione.
	 */
	public MainForm getMainForm(){
		return this.mainForm;
	}
	
//	/**
//	 * Ritorna il pool di azioni.
//	 * @return il pool di azioni.
//	 */	
//	public ActionPool getActionPool (){
//		return this.actionPool;
//	}
	
	/**
	 * Metodo di lancio.
	 * @param args gli argomenti della linea di comando.
	 */
	public static void main(String args[]) {
		_applicationEnvironment = 
			new CommandLineApplicationEnvironment (args);
		
		_applicationOptions = 
			new ApplicationOptions (UserSettings.getInstance (), 
				new ApplicationOptions (SystemSettings.getInstance (), 
					new ApplicationOptions (DefaultSettings.getInstance (), null)));
		
		
		try {
			final StringBuffer sb = new StringBuffer ();
			sb.append (_applicationOptions.getLogDirPath () ).append ("/")
			.append (CalendarUtils.getTS (Calendar.getInstance ().getTime (), CalendarUtils.FILENAME_TIMESTAMP_FORMAT))
			.append (".txt");
			
			final File plainTextLogFile = new File (sb.toString ());
			_logger = 
				new CompositeLogger (
					new PlainTextLogger (
						plainTextLogFile, true), null);
		} catch (IOException ioe){
			System.out.println ("Logging disabled. CAUSE: "+ExceptionUtils.getStackTrace (ioe));
		}
		Application a = null;
		try {
			SplashScreen.getInstance ().startSplash ();
			try {
				a = getInstance(args);

		//		a.getProjectCreateAction ().execute ("Void project");
				ActionPool.getInstance ().getProjectCloseAction ().execute ();
				Application.getInstance ().addObserver (UserSettings.getInstance ());
				try{
		//			a.getMainForm().setBounds(0, 0, 800, 600);
					a.getMainForm().show();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(a.getMainForm(),
					ex.toString(), "Warning",
					JOptionPane.WARNING_MESSAGE);
				}
			} finally {
				SplashScreen.getInstance ().stopSplash ();
			}
					/* testa persistenza*/
			if (!a.testPersistenceManager (a.getPersistenceManager ())){
				final UserSettingsFrame usf = UserSettingsFrame.getInstance ();
				usf.showDataSettings ();
				usf.show ();
			}

		} catch (Exception e){
			e.printStackTrace();
			System.exit (1);
		}
	}
	
	/**
	 * Il progetto corrente.
	 */
	private Project project;
	
	/**
	 * Imposta il progetto corrente.
	 * @param project il progetto corrente.
	 */	
	public void setProject(Project project){
		this.project = project;
		if (project!=null){
			this.setSelectedItem (project.getRoot());
		} else {
			this.setSelectedItem (null);
		}
		//notifica cambiamento di progetto
		this.setChanged();
		this.notifyObservers(ObserverCodes.PROJECTCHANGE);
	}
	
	
	/**
	 * Ritorna il progetto corrente.
	 * @return il progetto corrente.
	 */	
	public Project getProject(){
		return this.project;
	}
	
	/**
	 * L'elemento corrente.
	 */
	private ProgressItem currentItem;
	
	/**
	 * Imposta l'elemento corrente.
	 * @param current l'elemento corrente.
	 */	
	public void setCurrentItem(ProgressItem current){
		this.currentItem = current;
		//notifica cambiamento elemento corrente
		this.setChanged();
		this.notifyObservers(ObserverCodes.CURRENTITEMCHANGE);
		
		/*
		 * Gestione timer notifica avanzamento.
		 */
		if (current!=null){
			//avvia timer
			instance.timer.start();
		} else {
			//arresta timer
			instance.timer.stop();
		}
	}
	
	/**
	 * Ritorna l'elemento corrente.
	 * @return l'elemento corrente.
	 */	
	public ProgressItem getCurrentItem(){
		return this.currentItem;
	}
	
	/**
	 * L'elemento selezionato.
	 */
	private ProgressItem selectedItem;
	
	/**
	 * Imposta l'elemento selezionato.
	 * @param selected l'elemento selezionato.
	 */	
	public void setSelectedItem(ProgressItem selected){
		this.selectedItem = selected;
		//notifica variazione elemento selezionato
		this.setChanged();
		this.notifyObservers(ObserverCodes.SELECTEDITEMCHANGE);
	}
	
	/**
	 * Ritorna l'elemento selezionato.
	 * @return l'elemento selezionato.
	 */	
	public ProgressItem getSelectedItem(){
		return this.selectedItem;
	}
	
	/**
	 * L'avanzamento selezionato.
	 */
	private Period selectedProgress;
	
	/**
	 * Imposta l'avanzamento selezionato.
	 * @param selected l'avanzamento selezionato.
	 */	
	public void setSelectedProgress(Period selected){
		this.selectedProgress = selected;
		//notifica variazione selezione elemento
		this.setChanged();
		this.notifyObservers(ObserverCodes.SELECTEDPROGRESSCHANGE);
	}
	
	/**
	 * Ritorna l'acanzamento selezionato.
	 * @return l'acanzamento selezionato.
	 */	
	public Period getSelectedProgress(){
		return this.selectedProgress;
	}

	/**
	 * Il gestore della persistenza deidati.
	 */
	private PersistenceManager pm;
	
	private final void setDatastoreProperties (){
		if (this._dataStoreEnvironment==null){
			this._dataStoreEnvironment = new DataStoreEnvironment (null);
		}
		final Properties properties = this._dataStoreEnvironment.getDataStoreProperties ();
		properties.put ("javax.jdo.PersistenceManagerFactoryClass", "com.sun.jdori.fostore.FOStorePMF");
		final StringBuffer connectionURL = new StringBuffer ();
		connectionURL.append ("fostore:");
		final String storageDirPath = this._applicationOptions.getJDOStorageDirPath ();
		if (storageDirPath!=null && storageDirPath.length ()>0){
			connectionURL.append (storageDirPath);
			if (!storageDirPath.endsWith ("/")){
				connectionURL.append ("/");
			}
		}
		connectionURL.append (this._applicationOptions.getJDOStorageName ());
		properties.put ("javax.jdo.option.ConnectionURL", connectionURL.toString ());
		properties.put ("javax.jdo.option.ConnectionUserName", this._applicationOptions.getJDOUserName ());
	}
	
	/**
	 * Inizializza la gestione della persistenza dei dati.
	 */
	private void initPersistence (){
		setDatastoreProperties ();
		final Properties properties = this._dataStoreEnvironment.getDataStoreProperties ();
		final PersistenceManagerFactory pmf =
					  JDOHelper.getPersistenceManagerFactory(properties);
		
		System.out.println ("Setting persistent manager from "+properties);
		
		this.pm = pmf.getPersistenceManager();
		
		if (this.pm.currentTransaction().isActive ()){
			this.pm.currentTransaction().commit();
		}
		this.pm.currentTransaction().begin();
	}
	
	/**
	 * Rilascia le risorse per la risorsea gestione della persistenza dei dati.
	 */
	private void closePersistence (){
		if (this.pm==null){
			return;
		}
		if (this.pm.currentTransaction().isActive ()){
			this.pm.currentTransaction().commit();
		}
		if (!this.pm.isClosed ()){
			this.pm.close ();
		}
	}
	
	/**
	 * specifica se il manager della persistenza è utilizzabile allo stato attuale.
	 */
	private boolean _usablePersistenceManager = false;
	
	/**
	 * Ritorna <TT>true</TT> se il persistence manager specificato è utilizzabile.
	 *
	 * @param pm il manager della persistenza.
	 *@todo un test decente di validità della connessione allo storage.
	 */	
	private boolean testPersistenceManager (final PersistenceManager pm){
		try {
			final Iterator it = pm.getExtent(Project.class, true).iterator();
		} catch (Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * Inizializza il repository dei dati persistenti con le impostazioni correnti.
	 */
	public void createDataStore (){
		if (_usablePersistenceManager){
			closePersistence ();
		}
		setDatastoreProperties ();
		this.pm = DataStoreUtil.createDataStore (getDataStoreEnvironment (), true);
		_usablePersistenceManager = true;
	}
	
	/**
	 * Ritorna il gestore della persistenza dei dati.
	 * @return il gestore della persistenza dei dati.
	 */	
	public final PersistenceManager getPersistenceManager (){
		System.out.println ("returning persistent manager for properties "+	this._dataStoreEnvironment.getDataStoreProperties ());
		return this.pm;
	}
	
	/**
	 * Forza la persistenza delle modifiche.
	 */
	public void flushData (){
		Transaction tx = this.pm.currentTransaction();
		try {
			tx.commit();
		} catch (Exception e){
			Application.getLogger ().error ("Error flushing data for persistence. ", e);
			e.printStackTrace();
			tx.rollback();
		}
		tx.begin();
	}
	
	/**
	 * Ritorna i progetti disponibili.
	 * @return i progetti disponibili.
	 */	
	public List getAvailableProjects (){
		List retValue = new ArrayList ();
		this.setProcessing (true);
		try {
			for (Iterator it = getPersistenceManager().getExtent(Project.class, true).iterator();it.hasNext ();){
				retValue.add (it.next());
			}
		} finally {
			this.setProcessing (false);
		}
		return retValue;
	}
	
    /**
     * Marca questo <tt>Observable</tt> come modificato; il metodo
     * <tt>hasChanged</tt> ora ritornerà <tt>true</tt>.
     */
    public final synchronized void setChanged() {
		super.setChanged ();
    }
	
	/**
	 * Stato elaborazione.
	 */
	private int _processing = 0;
	
	/**
	 * Imposta lo stato di elaborazione dell'applicazione.
	 * @param processing lo stato di elaborazione dell'applicazione.
	 */	
	public void setProcessing (boolean processing){
		if (processing){
			this._processing++;
		} else {
			this._processing--;
			if (this._processing==0){
				if (Application.getOptions ().beepOnEvents ()){
					java.awt.Toolkit.getDefaultToolkit().beep();
				}
			}
		}
		this.setChanged ();
		this.notifyObservers (ObserverCodes.PROCESSINGCHANGE);
	}
	
	/**
	 * Ritorna lo stato di elaborazione dell'applicazione.
	 * @return  lo stato di elaborazione dell'applicazione.
	 */	
	public boolean isProcessing (){
		return this._processing>0;
	}
	
	/**
	 * Ritorna le opzioni di configurazione per questa applicazione.
	 *
	 * @return le opzioni di configurazione.
	 */	
	public static ApplicationOptions getOptions (){
		return _applicationOptions;
	}
	
	/**
	 * Ritorna l'ambiente di lancio per questa applicazione.
	 *
	 * @return l'ambinete di lancio.
	 */	
	public static ApplicationEnvironment getEnvironment (){
		return _applicationEnvironment;
	}
	
	/**
	 * Ritorna l'ambiente di configurazione del repository.
	 *
	 * @return l'ambinete di persistenza.
	 */	
	public DataStoreEnvironment getDataStoreEnvironment (){
		return _dataStoreEnvironment;
	}
	
	/**
	 * Ritorna il logger.
	 *
	 * @return il logger.
	 */	
	public static Logger getLogger (){
		return _logger;
	}
	
	/**
	 * Operazioni da effettuare prima dell'uscita dall'applicazione, tra le quali:
	 *	<UL>
	 *		<LI>Salvataggio impostazioni utente.
	 *	</UL>
	 */
	public void beforeExit (){
		setChanged ();
		notifyObservers (ObserverCodes.APPLICATIONEXITING);
		UserSettings.getInstance ().storeProperties ();
		final ProgressItem currentItem = this.getCurrentItem ();
		if (currentItem!=null){
			currentItem.stopPeriod ();
		}
		ActionPool.getInstance ().getProjectSaveAction ().execute ();
		/* Forza chiusura logger. */
		_logger.close ();
	}

	/**
	 * Termina l'applicazione.
	 */
	public final void exit (){
		beforeExit ();
		System.exit (0);
	}

}
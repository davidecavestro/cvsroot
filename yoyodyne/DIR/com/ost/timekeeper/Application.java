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
	 * <TT>true</TT> se l'applicazione è in fase di inizializzazione.
	 */
	private boolean _onAppInit=false;
	/** 
	 * Costruttore privato. 
	 */
	private Application(String[] args) {
		
		this._onAppInit = true;
		try {
			//inizializza il supporto per la persistenza dei dati
			initPersistence ();
		} finally {
			this._onAppInit = false;
		}
		
		final ActionPool actionPool = ActionPool.getInstance ();
		//registra le azioni su Application
		this.addObserver(actionPool.getNodeCreateAction ());
		this.addObserver(actionPool.getNodeDeleteAction ());
		this.addObserver(actionPool.getProgressStartAction ());
		this.addObserver(actionPool.getProgressStopAction ());
		this.addObserver(actionPool.getProgressUpdateAction ());
		this.addObserver(actionPool.getProgressDeleteAction ());
		this.addObserver(actionPool.getProjectCreateAction ());
		this.addObserver(actionPool.getProjectDeleteAction ());
		this.addObserver(actionPool.getProjectCloseAction ());
		this.addObserver(actionPool.getProjectOpenAction ());
		this.addObserver(actionPool.getProjectSaveAction ());
		this.addObserver(actionPool.getProjectXMLExportAction ());
		this.addObserver(actionPool.getProjectXMLImportAction ());
		this.addObserver(actionPool.getNodeUpdateAction ());
		this.addObserver(actionPool.getStartNodeEdit ());
		this.addObserver(actionPool.getStartProgressEdit ());
		this.addObserver(actionPool.getStartNewProgressCreation ());
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
				
				ActionPool.getInstance ().getShowDataStoreWIzardDialog ().execute ();
//				final UserSettingsFrame usf = UserSettingsFrame.getInstance ();
//				usf.showDataSettings ();
//				usf.show ();
			} else {
				
				a.setProcessing (true, ResourceSupplier.getString (ResourceClass.UI, "controls", "loading.project"));
				try {
					
					final String lastProjectName = _applicationOptions.getLastProjectName ();
					try {
						for (final Iterator it = a.getPersistenceManager ().getExtent (Project.class, true).iterator ();it.hasNext ();){
							final Project candidate = (Project)it.next ();
							if (candidate.getName ().equals (lastProjectName)){
								a.setProject (candidate);
								break;
							}
						}
					} catch (final Throwable t){
						a.getLogger ().error ("Cannot load last project", t);
						a.setProject (null);
					}
					
				} finally {
					a.setProcessing (false);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			System.exit (1);
		}
		
		try {
			
			final JPopupMenu popup = new JPopupMenu ();
			popup.add (new JMenuItem (ActionPool.getInstance ().getProgressStartAction ()));
			popup.add (new JMenuItem (ActionPool.getInstance ().getProgressStopAction ()));
			org.jdesktop.jdic.tray.TrayIcon trayIcon = new org.jdesktop.jdic.tray.TrayIcon(
			ResourceSupplier.getImageIcon (ResourceClass.UI, "application.png"),
			ApplicationData.getInstance ().getApplicationExternalName (),
			popup
			);
			org.jdesktop.jdic.tray.SystemTray systemTray = org.jdesktop.jdic.tray.SystemTray.getDefaultSystemTray();
			systemTray.addTrayIcon(trayIcon);

		} catch (Exception e){
			System.out.println (com.ost.timekeeper.util.ExceptionUtils.getStackTrace (e));
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
			for (final Iterator it = project.getRoot().getProgresses ().iterator ();it.hasNext ();){
				new com.ost.timekeeper.actions.commands.DeleteProgress ((Progress)it.next ()).execute ();
			}
		}
		if (project!=null){
			this.setSelectedItem (project.getRoot());
		} else {
			this.setSelectedItem (null);
		}
		//notifica cambiamento di progetto
		synchronized (this){
			this.setChanged();
			this.notifyObservers(ObserverCodes.PROJECTCHANGE);
		}
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
		synchronized (this){
			this.setChanged();
			this.notifyObservers(ObserverCodes.CURRENTITEMCHANGE);
		}		
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
		
		synchronized (this){
			this.setChanged();
			this.notifyObservers(ObserverCodes.SELECTEDITEMCHANGE);
		}
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
	private Progress selectedProgress;
	
	/**
	 * Imposta l'avanzamento selezionato.
	 * @param selected l'avanzamento selezionato.
	 */	
	public void setSelectedProgress(Progress selected){
		this.selectedProgress = selected;
		//notifica variazione selezione elemento
		
		synchronized (this){
			this.setChanged();
			this.notifyObservers(ObserverCodes.SELECTEDPROGRESSCHANGE);
		}
	}
	
	/**
	 * Ritorna l'acanzamento selezionato.
	 * @return l'acanzamento selezionato.
	 */	
	public Progress getSelectedProgress(){
		return this.selectedProgress;
	}

	/**
	 * Il gestore della persistenza dei dati.
	 */
	private PersistenceManager pm;
	
	public final void setDatastoreProperties (){
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
	public void initPersistence (){
		if (!this._onAppInit){
			/*
			 * Evita stackoverflow
			 */
			closeActiveStoreData ();
		}
		setDatastoreProperties ();
		final Properties properties = this._dataStoreEnvironment.getDataStoreProperties ();
		final PersistenceManagerFactory pmf =
					  JDOHelper.getPersistenceManagerFactory(properties);
		
//		System.out.println ("Setting persistent manager from "+properties);
		
		this.pm = pmf.getPersistenceManager();
		
		if (this.pm.currentTransaction().isActive ()){
			this.pm.currentTransaction().commit();
		}
//		this.pm.currentTransaction().begin();
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
	 * Chiude tutte le eventuali azioni attualmente in corso che utilizzano il datastore.
	 */
	private void closeActiveStoreData (){
		/* assicura chiusura avanzamento incorso */
		final ProgressItem currentItem = this.getCurrentItem ();
		if (currentItem!=null && currentItem.isProgressing ()){
			ActionPool.getInstance ().getProgressStopAction ().execute ();
		}
		ActionPool.getInstance ().getProjectCloseAction ().execute ();
	}
	
	/**
	 * Inizializza il repository dei dati persistenti con le impostazioni correnti.
	 */
	public void createDataStore (){
		closeActiveStoreData ();
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
//		System.out.println ("returning persistent manager for properties "+	this._dataStoreEnvironment.getDataStoreProperties ());
		return this.pm;
	}
	
	/**
	 * Forza la persistenza delle modifiche.
	 */
	public void flushData (){
		Transaction tx = this.pm.currentTransaction();
		try {
			if (this.pm.currentTransaction().isActive ()){
				this.pm.currentTransaction().commit();
			}
		} catch (Exception e){
			Application.getLogger ().error ("Error flushing data for persistence. ", e);
			e.printStackTrace();
			tx.rollback();
		}
//		tx.begin();
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
		this.setProcessing (processing, ResourceSupplier.getString (ResourceClass.UI, "controls", "processing"));
	}
	
	private final Stack _processingMessages = new Stack ();
	/**
	 * Imposta lo stato di elaborazione dell'applicazione.
	 * @param message messaggio.
	 * @param processing lo stato di elaborazione dell'applicazione.
	 */	
	public void setProcessing (boolean processing, final String message){
		if (processing){
			this._processing++;
			this._processingMessages.push (message);
		} else {
			this._processing--;
			this._processingMessages.pop ();
			if (this._processing==0){
				if (Application.getOptions ().beepOnEvents ()){
					java.awt.Toolkit.getDefaultToolkit().beep();
				}
			}
		}
		
		synchronized (this){
			this.setChanged ();
			this.notifyObservers (ObserverCodes.PROCESSINGCHANGE);
		}
	}
	
	/**
	 * Ritorna il messaggio di elaborazione corrente.
	 *
	 * @return il messaggio di elaborazione corrente.
	 */	
	public String getProcessingMessage (){
		if (!this._processingMessages.isEmpty ()){
			return (String)this._processingMessages.peek ();
		} else {
			return null;
		}
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
		synchronized (this){
			setChanged ();
			notifyObservers (ObserverCodes.APPLICATIONEXITING);
		}
		UserSettings.getInstance ().storeProperties ();
		
		closeActiveStoreData ();
		
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
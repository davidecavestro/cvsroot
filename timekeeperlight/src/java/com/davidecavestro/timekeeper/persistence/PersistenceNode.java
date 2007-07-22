/*
 * PersistenceNode.java
 *
 * Created on December 15, 2006, 12:03 AM
 *
 */

package com.davidecavestro.timekeeper.persistence;

import com.davidecavestro.common.log.Logger;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import com.davidecavestro.timekeeper.model.WorkSpace;
import com.ost.timekeeper.model.Project;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

/**
 * Gestisce l'inizializzazione e l'accesso allo storage per la persistenza dei dati tramite JDPa
 *
 * @author Davide Cavestro
 */
public class PersistenceNode {
	
	/**
	 * Le impostazioni applicative di interesse per l'accesso allo storage..
	 */
	final ApplicationOptions _ao;
	
	/**
	 * Il logger.
	 */
	final Logger _logger;
	
	public PersistenceNode (final ApplicationOptions ao, final Logger logger) {
		_ao = ao;
		_logger = logger;
	}
	
	/**
	 * Il gestore della persistenza dei dati.
	 */
	private PersistenceManager _pm;
	private Properties _props;
	
	/**
	 * Imposta le properties necessarie all'inizializzazione dello storage.
	 */
	private final void setDatastoreProperties (){
		if (_props==null){
			_props = new Properties (null);
		}
		_props.put ("javax.jdo.PersistenceManagerFactoryClass", "com.sun.jdori.fostore.FOStorePMF");
		final StringBuffer connectionURL = new StringBuffer ();
		connectionURL.append ("fostore:");
		final String storageDirPath = _ao.getJDOStorageDirPath ();
		if (storageDirPath!=null && storageDirPath.length ()>0){
			connectionURL.append (storageDirPath);
			if (!storageDirPath.endsWith ("/")){
				connectionURL.append ("/");
			}
		}
		/* 
		 * crea il percorsose necessario/possibile
		 */
		new File (_ao.getJDOStorageDirPath ()).mkdirs ();
		
		connectionURL.append (_ao.getJDOStorageName ());
		_props.put ("javax.jdo.option.ConnectionURL", connectionURL.toString ());
		_props.put ("javax.jdo.option.ConnectionUserName", _ao.getJDOUserName ());
	}
	
	
	private File _datastoreLock;
	/**
	 * Ritorna il file di lock del datastore.
	 * Il file viene sempre ritornato, ma potrebe anche non essere valido (non esiste il file sottostante).
	 */
	private File getDatastoreLock () {
		if (_datastoreLock==null) {
			_datastoreLock = new File (new File (_ao.getJDOStorageDirPath ()), _ao.getJDOStorageName ()+".lock");
		}
		return _datastoreLock;
	}
	
	
	/**
	 * Identificatore dell'istanza di applicazione al fine di accedere al db.
	 */
	private final String _dbAccessID = Double.toString (Math.random ());
	
	private final static long MAX_DELAY = 1000*90;
	/**
	 * Acquisisce il lock sul datastore.
	 */
	private void lockDatastore () throws IOException {
		final File lockFile = getDatastoreLock ();
		if (lockFile.exists () && System.currentTimeMillis ()  - lockFile.lastModified () < MAX_DELAY) {
			throw new IOException ("Cannot create a new datastore lock file. The existing one is too recent.");
		}
		final PrintWriter fw = new PrintWriter (getDatastoreLock ());
		try {
			fw.println (_dbAccessID);
		} finally {
			fw.flush ();
			fw.close ();
		}
		getDatastoreLock ().deleteOnExit ();
		final Timer t = new Timer ("datastore-lock-refresher", true);
		t.schedule (
			new TimerTask () {
				public void run () {
					lockFile.setLastModified (System.currentTimeMillis ());
				}
			}, 1000);
	}
	
	/**
	 * Ritorna <CODE>true</CODE> se il datastore &egrave; acquisito 
	 * dall'istanza corrente dell'applicazione.
	 */
	private boolean isDatastoreAvailable () {
		final BufferedReader br;
		try {
			br = new BufferedReader (new FileReader (getDatastoreLock()));
			try {
				return _dbAccessID.equals (br.readLine ());
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					br.close ();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			return false;
		} catch (FileNotFoundException ex) {
			return false;
		}
	}
	
	/**
	 * Libera il lock acquisito sul datastore-
	 */
	private void unlockDatastore () {
		getDatastoreLock ().delete ();
	}
	
	/**
	 * Inizializza la gestione della persistenza dei dati.
	 * Imposta il campo interno <CODE>_pm</CODE>.
	 */
	private void initPersistence (){
		setDatastoreProperties ();
		final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory (_props);
		
		_pm = pmf.getPersistenceManager ();
		
		if (_pm.currentTransaction ().isActive ()){
			_pm.currentTransaction ().commit ();
		}
		_pm.currentTransaction ().setOptimistic (false);
	}
	
	/**
	 * Rilascia le risorse per la risorsea gestione della persistenza dei dati.
	 */
	private void closePersistence (){
		if (_pm==null){
			return;
		}
		if (_pm.currentTransaction ().isActive ()){
			_pm.currentTransaction ().commit ();
		}
		if (!_pm.isClosed ()){
			_pm.close ();
		}
	}
	
	/**
	 * Inizializza il repository dei dati persistenti con le impostazioni correnti.
	 * Imposta il campo interno <CODE>_pm</CODE>.
	 */
	private void createDataStore (){
		if (_pm!=null){
			closePersistence ();
		}
		setDatastoreProperties ();
		_pm = createDataStore (_props);
	}
	
	/**
	 * Ritorna il gestore della persistenza dei dati.
	 * Se non &egrave; stato ancora inizializzato ritorna <CODE>null</CODE>.
	 * 
	 *
	 * @return il gestore della persistenza dei dati, oppure <CODE>null</CODE>. se non &egrave; disponibile.
	 * @see #init()
	 */
	public final PersistenceManager getPersistenceManager (){
		return _pm;
	}
	
	/**
	 * Forza la persistenza delle modifiche.
	 */
	public void flushData (){
		if (_pm==null) {
			return;
		}
		final Transaction tx = _pm.currentTransaction ();
		try {
			if (_pm.currentTransaction ().isActive ()){
				_pm.currentTransaction ().commit ();
			}
		} catch (Exception e){
			e.printStackTrace (System.err);
			tx.rollback ();
		}
	}
	
	/**
	 * Crea un nuovo repository di dati persistenti.
	 * @todo siccome l'INFAME che ha fatto la classe JDOHelper chiama direttamente i metodi
	 * di Hashtable, non posso usare le properties di default.
	 *
	 * @param env l'ambiente di configurazione del data store.
	 * @return il gestore della persistenza per il datastore creato.
	 */
	public static PersistenceManager createDataStore (final Properties props) {
		final Properties properties = (Properties)props.clone ();
		properties.put ("com.sun.jdori.option.ConnectionCreate", "true");
		final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory (properties);
		final PersistenceManager pm = pmf.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		
		tx.begin ();
		tx.commit ();
		
		return pm;
	}
	
	public List<WorkSpace> getAvailableWorkSpaces () {
		final List<WorkSpace> data = new ArrayList<WorkSpace> ();
		final PersistenceManager pm = getPersistenceManager ();
		for (final Iterator it = pm.getExtent(Project.class, true).iterator();it.hasNext ();){
			data.add ((Project)it.next());
		}
		return data;
	}
	
	/**
	 * Inizializza il gestore della persistenza dei dati.
	 * Nel caso in cui il sistema non sia configurato correttamente &egrave; possibile che il PersistenceManager non possa essere istanziato.
	 * Se il campo interno <CODE>_pm</CODE> ha un valore diverso da <CODE>null</CODE>, il metodo ritorna subito.
	 */
	public void init () throws PersistenceNodeException {
		if (_pm==null) {
			try {
				try {
					/*
					 * prova ad usare il datastore correntemente configurato.
					 */
					initPersistence ();
					_pm.currentTransaction ().begin ();
					_pm.currentTransaction ().rollback ();
				} catch (final Exception e) {
					/*
					 * prova a creare il datastore alvolo, con le impostazioni correnti.
					 */
					createDataStore ();
				}
			
				lockDatastore ();
				if (!isDatastoreAvailable ()) {
					throw new PersistenceNodeException (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Cannot_initialize_data_storage."));
				}
				
			} catch (final Exception ex) {
				throw new PersistenceNodeException (ex);
			}
		}
	}

}

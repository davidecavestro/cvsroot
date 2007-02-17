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
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
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
	
	/**
	 * Inizializza la gestione della persistenza dei dati.
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
	 */
	private void createDataStore (){
		if (_pm!=null){
			closePersistence ();
		}
		setDatastoreProperties ();
		_pm = createDataStore (_props);
	}
	
	/**
	 * Inizializza e ritorna il gestore della persistenza dei dati.
	 * Se non &egrave; possibile inizializzarlo ritorna <CODE>null</CODE>.
	 * Nel caso in cui il sistema non sia configurato correttamente &egrave; possibile che il PersistenceManager non possa essere istanziato.
	 *
	 * @return il gestore della persistenza dei dati, oppure <CODE>null</CODE>. se non &egrave; disponibile.
	 */
	public final PersistenceManager getPersistenceManager (){
		if (_pm==null) {
			try {
				/*
				 * prova ad usare il datastore correntemente configurato.
				 */
				initPersistence ();
				_pm.currentTransaction ().begin ();
				_pm.currentTransaction ().rollback ();
			} catch (final Exception e) {
				try {
					/*
					 * prova a creare il datastore alvolo, con le impostazioni correnti.
					 */
					createDataStore ();
				} catch (final Exception ie) {
					_logger.error ("Cannot initialize data storage", e);
					_logger.error ("Cannot create data storage", ie);
					return null;
				}
			}
		}
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
	
	public List<WorkSpace> getAvailableWorkSpaces () throws PersistenceNodeException {
		final List<WorkSpace> data = new ArrayList<WorkSpace> ();
		final PersistenceManager pm = getPersistenceManager ();
		if (pm == null) {
			throw new PersistenceNodeException ("Cannot initialize data storage.");
		} else {
			for (final Iterator it = pm.getExtent(Project.class, true).iterator();it.hasNext ();){
				data.add ((Project)it.next());
			}
		}
		return data;
	}
}

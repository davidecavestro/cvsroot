/*
 * DataStoreUtil.java
 *
 * Created on 14 maggio 2004, 20.14
 */

package com.ost.timekeeper.persistence;

import java.io.*;
import java.util.*;
import javax.jdo.*;

/**
 * Classe di utilit� per la gestione della persistenza e del repository.
 * E' configurabile tramite il file <TT>datastore.properties</TT>.
 *
 * @todo siccome l'INFAME che ha fatto la classe JDOHelper chiama direttamente i metodi
 * di Hashtable, non posso usare le properties di default.
 *
 * @author  davide
 */
public final class DataStoreUtil {
	
	/** Costruttore vuoto. */
	private DataStoreUtil() {
	}

	/**
	 * Propriet� .
	 */
	private static Properties _datastoreProperties = new Properties();
	
	/**
	 * Propriet� configurabili.
	 */
//	private final static Properties customizableDatastoreProperties = new Properties();
	
	/**
	 * Inizializzatore.
	 */
	static {
		try {
			/*
			 * carica dati di configurazione.
			 */
			_datastoreProperties.load(DataStoreUtil.class.getResourceAsStream("datastore.properties"));
		} catch (IOException ioe) {
			throw new RuntimeException (ioe);
		}
	}
	/**
	 * Ritorna le propriet� di configurazione.
	 *
	 * @return le propriet� di configurazione.
	 */	
	public static Properties getDataStoreProperties (){
//		if (_datastoreProperties == null){
//			_datastoreProperties = new Properties (customizableDatastoreProperties);
//		}
		return _datastoreProperties;
	}
	
	/**
	 * Crea un nuovo repository didati persistenti.
	 */
	public static void createDataStore() {
		try {
			Properties properties = getDataStoreProperties ();
			properties.put("com.sun.jdori.option.ConnectionCreate", "true");
			PersistenceManagerFactory pmf =
			JDOHelper.getPersistenceManagerFactory(properties);
			PersistenceManager pm = pmf.getPersistenceManager();
			Transaction tx = pm.currentTransaction();
			tx.begin();
			tx.commit();
		} catch (Exception e) {
			System.out.println("Problem creating database");
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo di accesso per l'uso stand-alone 
	 * (generazione datastore da linea di comando).
	 *
	 * @param args i parametri di lancio (non usati).
	 */	
	public static void main (String[] args){
		DataStoreUtil.createDataStore();
	}
}

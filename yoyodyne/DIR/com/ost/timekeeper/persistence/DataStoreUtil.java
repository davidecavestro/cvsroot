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
 * Classe di utilità per la gestione della persistenza e del repository.
 * E' configurabile tramite il file <TT>datastore.properties</TT>.
 *
 * @author  davide
 */
public final class DataStoreUtil {
	
	/** Costruttore vuoto. */
	private DataStoreUtil() {
	}

	/**
	 * Proprietà configurabili.
	 */
	private final static Properties datastoreProperties = new Properties();
	
	/**
	 * Inizializzatore.
	 */
	static {
		try {
			/*
			 * carica dati di configurazione.
			 */
			datastoreProperties.load(DataStoreUtil.class.getResourceAsStream("datastore.properties"));
		} catch (IOException ioe) {
			throw new RuntimeException (ioe);
		}
	}
	/**
	 * Ritorna le proprietà di configurazione.
	 *
	 * @return le proprietà di configurazione.
	 */	
	public static Properties getDataStoreProperties (){
		return datastoreProperties;
	}
	
	/**
	 * Crea un nuovo repository didati persistenti.
	 */
	public static void createDataStore() {
		try {
			Properties properties = datastoreProperties;
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

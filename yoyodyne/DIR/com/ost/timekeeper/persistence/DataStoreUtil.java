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
	 * Crea un nuovo repository didati persistenti.
	 */
	public static void createDataStore(final DataStoreEnvironment env) {
		try {
			final Properties properties = env.getDataStoreProperties ();
			properties.put("com.sun.jdori.option.ConnectionCreate", "true");
			PersistenceManagerFactory pmf =
			JDOHelper.getPersistenceManagerFactory(properties);
			PersistenceManager pm = pmf.getPersistenceManager();
			Transaction tx = pm.currentTransaction();
//			if (!pm.isClosed ()){
//				tz.commit ();
//			}
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
		DataStoreUtil.createDataStore(new DataStoreEnvironment (args[0]));
	}
}

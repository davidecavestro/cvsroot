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
	 * Crea un nuovo repository di dati persistenti.
	 *
	 * @param env l'ambiente di configurazione del data store.
	 * @param leaveOpen <TT>true</TT> se la transazone deve rimanere attiva.
	 * @return il gestore della persistenza per il datastore creato.
	 */
	public static PersistenceManager createDataStore(final DataStoreEnvironment env, final boolean leaveOpen) {
		PersistenceManager pm = null;
		try {
			final Properties properties = (Properties)env.getDataStoreProperties ().clone ();
			properties.put("com.sun.jdori.option.ConnectionCreate", "true");
			final PersistenceManagerFactory pmf =
			JDOHelper.getPersistenceManagerFactory(properties);
			pm = pmf.getPersistenceManager();
			final Transaction tx = pm.currentTransaction();

			tx.begin();
			tx.commit ();
			
//			if (leaveOpen ){
//				tx.begin();
//			}

		} catch (final Exception e) {
			System.out.println("Problem creating database");
			e.printStackTrace();
		}
		return pm;
	}
	
	/**
	 * Metodo di accesso per l'uso stand-alone 
	 * (generazione datastore da linea di comando).
	 *
	 * @param args i parametri di lancio (non usati).
	 */	
	public static void main (String[] args){
		DataStoreUtil.createDataStore(new DataStoreEnvironment (args[0]), false);
	}
}

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
 *
 * @author  davide
 */
public class DataStoreUtil {
	
	/** Creates a new instance of DataStoreUtil */
	private DataStoreUtil() {
	}

	private final static Properties datastoreProperties = new Properties();
	static {
		try {
			datastoreProperties.load(DataStoreUtil.class.getResourceAsStream("datastore.properties"));
		} catch (IOException ioe) {
			throw new RuntimeException (ioe);
		}
	}
	public static Properties getDataStoreProperties (){
		return datastoreProperties;
	}
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
	
	public static void main (String[] args){
		DataStoreUtil.createDataStore();
	}
}

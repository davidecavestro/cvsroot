/*
 * SettingsSupport.java
 *
 * Created on 13 settembre 2004, 23.01
 */

package com.ost.timekeeper.conf;

import java.util.*;

/**
 * Gestisce la persistenza delle impostazioni personalizzate a livello globale ed utente.
 *
 * @author  davide
 */
public class SettingsSupport {
	
	private Properties properties;
	/** 
	 * Costruttore con risorsa di configurazione.
	 * @param properties la risorsa di configurazione.
	 */
	public SettingsSupport(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * Ritorna il valore di una proprietà di tipo booleano.
	 * @param propertyName il nome della proprietà.
	 * @return il valore di una proprietà di tipo booleano.
	 */	
	public Boolean getBooleanProperty (String propertyName){
		String propertyValue = this.properties.getProperty (propertyName);
		if (propertyValue!=null){
			return new Boolean (propertyValue);
		} else {
			return null;
		}
	}
	
	/**
	 * Ritorna il valore di una proprietà di tipo stringa.
	 * @param propertyName il nome della proprietà.
	 * @return il valore di una proprietà di tipo stringa.
	 */	
	public String getStringProperty (String propertyName){
		return this.properties.getProperty (propertyName);
	}
	
	/**
	 * Ritorna il valore di una proprietà di tipo data.
	 * @param propertyName il nome della proprietà.
	 * @return il valore di una proprietà di tipo data.
	 */	
	public Calendar getCalendarProperty (String propertyName){
//		String propertyValue = this.properties.getProperty (propertyName);
//		java.sql.Timestamp ts = new java.sql.Timestamp (year
//		if (propertyValue!=null){
//			return new Boolean (propertyValue);
//		} else {@@@
			return null;
//		}
	}
	
	/**
	 * Ritorna il valore di una proprietà di tipo intero.
	 * @param propertyName il nome della proprietà.
	 * @return il valore di una proprietà di tipo intero.
	 */	
	public Integer getIntegerProperty (String propertyName){
		String propertyValue = this.properties.getProperty (propertyName);
		if (propertyValue!=null){
			return new Integer (propertyValue);
		} else {
			return null;
		}
	}
	
}

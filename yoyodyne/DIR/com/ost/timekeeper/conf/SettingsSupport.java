/*
 * SettingsSupport.java
 *
 * Created on 13 settembre 2004, 23.01
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.util.*;
import java.util.*;

/**
 * Gestisce la persistenza delle impostazioni personalizzate.
 *
 * @author  davide
 */
public final class SettingsSupport {
	
	/** 
	 * Costruttore privato.
	 */
	private SettingsSupport() {
	}
	
	/**
	 * Ritorna il valore di una proprietà di tipo booleano.
	 *
	 * @param properties la risorsa di configurazione.
	 * @param propertyName il nome della proprietà.
	 * @return il valore di una proprietà di tipo booleano.
	 */	
	public static Boolean getBooleanProperty (Properties properties, String propertyName){
		String propertyValue = properties.getProperty (propertyName);
		if (propertyValue!=null){
			return new Boolean (propertyValue);
		} else {
			return null;
		}
	}
	
	/**
	 * Ritorna il valore di una proprietà di tipo stringa.
	 *
	 * @param properties la risorsa di configurazione.
	 * @param propertyName il nome della proprietà.
	 * @return il valore di una proprietà di tipo stringa.
	 */	
	public static String getStringProperty (Properties properties, String propertyName){
		return properties.getProperty (propertyName);
	}
	
	/**
	 * Ritorna il valore di una proprietà di tipo data.
	 *
	 * @param properties la risorsa di configurazione.
	 * @param propertyName il nome della proprietà.
	 * @return il valore di una proprietà di tipo data.
	 */	
	public static Calendar getCalendarProperty (Properties properties, String propertyName){
		String propertyValue = properties.getProperty (propertyName);
		if (propertyValue!=null){
			return CalendarUtils.getCalendar (propertyValue, CalendarUtils.TIMESTAMP_FORMAT);
		} else {
			return null;
		}
	}
	
	/**
	 * Ritorna il valore di una proprietà di tipo intero.
	 *
	 * @param properties la risorsa di configurazione.
	 * @param propertyName il nome della proprietà.
	 * @return il valore di una proprietà di tipo intero.
	 */	
	public static Integer getIntegerProperty (Properties properties, String propertyName){
		String propertyValue = properties.getProperty (propertyName);
		if (propertyValue!=null){
			return new Integer (propertyValue);
		} else {
			return null;
		}
	}
	
}

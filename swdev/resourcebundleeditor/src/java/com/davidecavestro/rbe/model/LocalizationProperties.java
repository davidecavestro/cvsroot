/*
 * LocalizationProperties.java
 *
 * Created on 3 dicembre 2005, 15.00
 */

package com.davidecavestro.rbe.model;

import java.util.Locale;
import java.util.Properties;

/**
 * Risorse di localizzazione.
 *
 * @author  davide
 */
public class LocalizationProperties {
	
	private final Locale locale;
	private final Properties properties;
	
	/**
	 * Costruttore.
	 * @param locale il locale.
	 * @param props le proeprties;
	 */
	public LocalizationProperties (Locale locale, Properties props) {
		this.locale = locale;
		this.properties = props;
	}
	
	/**
	 * Ritorna il locale.
	 *
	 * @return il locale.
	 */	
	public Locale getLocale (){
		return this.locale;
	}
	
	/**
	 * Ritorna la mappa delle risorse.
	 *
	 * @return la amppa delle risorse.
	 */	
	public Properties getProperties (){
		return this.properties;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di queste LocalizationProperties.
	 *
	 * @return una stringa che rappresenta queste LocalizationProperties.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("locale: ").append (this.locale);
		sb.append (" proeprties: ").append (this.properties);
		return sb.toString ();
	}
}

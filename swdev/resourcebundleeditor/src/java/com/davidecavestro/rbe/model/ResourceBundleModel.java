/*
 * ResourceBundleModel.java
 *
 * Created on 1 dicembre 2005, 23.49
 */

package com.davidecavestro.rbe.model;

import java.util.Locale;
import java.util.Set;

/**
 * Rappresenta i dati contenuti in un <CODE>ResourceBundle</CODE>.
 *
 * @author  davide
 */
public interface ResourceBundleModel extends ResourceBundleModelNotifier {
	
	/**
	 * Ritorna i <CODE>Locale</CODE> nel modello.
	 *
	 * @return i <CODE>Locale</CODE> nel modello.
	 */	
	Locale[] getLocales ();
	
	/**
	 * Ritorna le chiavi del modello.
	 *
	 * @return le chiavi del modello.
	 */	
	Set getKeySet ();
	
	/**
	 * Ritorna il valore per la chiave ed il locale specificati.
	 *
	 * @return il valore per la chiave ed il locale specificati.
	 * @param locale il locale.
	 * @param key la chiave.
	 */	
	String getValue (Locale locale, String key );
	
	/**
	 * Imposta il valore per la chiave ed il locale specificati.
	 *
	 * @param value il valore.
	 * @param locale il locale.
	 * @param key la chiave.
	 */	
	void setValue (Locale locale, String key, String value );
	
}

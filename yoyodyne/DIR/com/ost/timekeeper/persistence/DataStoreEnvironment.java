/*
 * DataStoreEnvironment.java
 *
 * Created on 3 gennaio 2005, 13.51
 */

package com.ost.timekeeper.persistence;

import java.io.*;
import java.util.*;

/**
 * Ambiente di configurazione per il repository dei dati.
 *
 * @author  davide
 */
public final class DataStoreEnvironment {
	
	/**
	 * Il percorso del file di configurazione.
	 */
	private String _propsPath;
	
	/**
	 * Le proprietà di configurazione.
	 */
	private Properties _datastoreProperties = new Properties();
	
	/**
	 * Lo stato di inizializzazione della configurazione.
	 */
	private boolean _datastorePropertiesLoaded = false;
	
	/**
	 * Costruttore.
	 * @param propertiesPath il percorso del file di configurazione. Nel caso in 
	 * cui sia <TT>null</TT> la configurazione verrà inizializzata vuota.
	 */
	public DataStoreEnvironment (final String propertiesPath) {
		this._propsPath = propertiesPath;
	}
	
	/**
	 * Carica le proprietà.
	 */
	private void loadProps (final File propSource){
		try {
			/*
			 * carica dati di configurazione.
			 */
			_datastoreProperties.load(new FileInputStream (propSource));
		} catch (final IOException ioe) {
			throw new RuntimeException (ioe);
		}
		_datastorePropertiesLoaded = true;
	}
	
	/**
	 * Ritorna le proprietà di configurazione.
	 *
	 * @return le proprietà di configurazione.
	 */	
	public Properties getDataStoreProperties (){
		if (!_datastorePropertiesLoaded && this._propsPath!=null){
			loadProps (new File (this._propsPath));
		}
		return _datastoreProperties;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo ambiente.
	 *
	 * @return una stringa che rappresenta questo ambiente.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("propsPath: ").append (this._propsPath);
		return sb.toString ();
	}
}

/*
 * ApplicationData.java
 *
 * Created on 19 dicembre 2004, 19.32
 */

package com.ost.timekeeper;

import com.ost.timekeeper.conf.*;
import java.io.*;
import java.util.*;

/**
 * Contiene i dati descrittivi dell'applicazione quali, ad esempio,il numero di versione...
 *
 * @author  davide
 */
public final class ApplicationData {

	/**
	 * Nome della proprietà contenente il nome dell'applicazione per uso interno.
	 */
	public final static String PROPNAME_INTERNALAPPLICATIONNAME = "internal.name";

	/**
	 * Nome della proprietà contenente il numero di versione più significativo.
	 */
	public final static String PROPNAME_MAJORVERSIONNUMBER = "major.version.number";

	/**
	 * Nome della proprietà contenente il numero di versione meno significativo.
	 */
	public final static String PROPNAME_MINORVERSIONNUMBER = "minor.version.number";

	/**
	 * Nome della proprietà contenente il numero progressivo di build dell'applicazione.
	 */
	public final static String PROPNAME_BUILDNUMBER = "build.number";

	/**
	 * Nome della proprietà contenente la data di rilascio dell'applicazione.
	 */
	public final static String PROPNAME_RELEASE_DATE = "release.date";

	/**
	 * L'istanza del singleton.
	 */
	private static ApplicationData _instance;
	
	/**
	 * Proprietà di configurazione di questo descrittore.
	 */
	private final static Properties _applicationProperties = new Properties ();
	
	/**
	 * Inizializzatore.
	 */
	static {
		try {
			/*
			 * carica dati di configurazione.
			 */
			_applicationProperties.load(ApplicationData.class.getResourceAsStream("application.properties"));
		} catch (IOException ioe) {
			//throw new RuntimeException (ioe);
			Application.getLogger ().warning ("Missing application properties file.");
		}
	}
	
	
	/** Costruttore privato. */
	private ApplicationData () {
	}
	
	public static ApplicationData getInstance (){
		if (_instance==null){
			_instance = new ApplicationData ();
		}
		return _instance;
	}
	
	/**
	 * Ritorna il nome interno dell'applicazione. Questo nome è inteso ad uso 
	 * dei processi di identificazione e configurazione dell'applicazione.
	 *
	 * @return il nome interno dell'applicazione.
	 */
	public static String getApplicationInternalName (){
		return SettingsSupport.getStringProperty (_applicationProperties, PROPNAME_INTERNALAPPLICATIONNAME);
	}
	
}

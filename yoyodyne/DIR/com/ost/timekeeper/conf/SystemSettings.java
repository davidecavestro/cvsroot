/*
 * UserSettings.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.*;
import java.awt.*;

/**
 * impostazioni configurabili di sistema.
 *
 * @author  davide
 */
public final class SystemSettings extends AbstractSettings {
	
	/**
	 * Percorso file impostazioni.
	 */
	public final static String PROPERTIES_PATH = ".timekeeper/systemconf.properties";
	
	/**
	 * header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = "SYSTEM SETTINGS";
	
	/**
	 * L'istanza diquesto singleton.
	 */
	private static UserSettings _instance;
	/** 
	 * Ritorna l'istanza delle impostazioni utente.
	 */
	public static UserSettings getInstance () {
		if (_instance==null){
			_instance = new UserSettings ();
		}
		return _instance;
	}
	

	
	public String getPropertiesFileName () {
		final StringBuffer sb = new StringBuffer ();
		sb.append (getSystemApplicationSettingsPath ()).append ("/").append (PROPERTIES_PATH);
		return sb.toString ();
	}

	public String getPropertiesHeader () {
		return PROPERTIES_HEADER;
	}
	
	/**
	 * Ritorna il percorso della directory contenente la configurazione dell'applicazione.
	 *
	 * @return il percorso della directory contenente la configurazione dell'applicazione.
	 */	
	public static String getSystemApplicationSettingsPath (){
		/**
		 * Prova a cercare nell'ambiente.
		 */
		final String envArg = Application.getEnvironment ().getApplicationSettingsPath ();
		if (envArg!=null){
			return envArg;
		}
		/**
		 * Prende variabile di sistema.
		 */
		return System.getProperty ("user.dir");
	}
}

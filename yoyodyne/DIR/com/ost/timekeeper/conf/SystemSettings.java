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
		sb.append (Application.getEnvironment ().getApplicationSettingsPath ()).append ("/").append (ResourceNames.SYSTEM_SETTINGSFILE_NAME);
		return sb.toString ();
	}

	public String getPropertiesHeader () {
		return PROPERTIES_HEADER;
	}
	

}

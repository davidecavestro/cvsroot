/*
 * SystemResources.java
 *
 * Created on 19 dicembre 2004, 21.08
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.*;

/**
 * Risorse applicative condivise tra gli utenti.
 *
 * @author  davide
 */
public class SystemResources {
	
	/** Costruttore. */
	private SystemResources () {
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
		 * Prende directory corrente.
		 */
		return System.getProperty ("user.dir");
	}	
	/**
	 * Ritorna il percorso della directory privata dell'utente di supporto all'applicazione .
	 *
	 * @return il percorso della directory privata dell'utente di supporto all'applicazione .
	 */	
	public static String getSystemApplicationDirPath (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (getUserHomeDirPath ());
		sb.append ("/").append (ResourceNames.USER_APPLICATIONREPOSITORYDIR_NAME);
		return sb.toString ();
	}
	
	/**
	 * Ritorna il percorso della directory privata dell'utente contenente la configurazione dell'applicazione .
	 *
	 * @return il percorso della directory privata dell'utente contenente la configurazione dell'applicazione .
	 */	
	public static String getUserApplicationSettingsDirPath (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (getUserApplicationDirPath ());
		sb.append ("/").append (ResourceNames.USER_SETTINGSDIR_NAME);
		return sb.toString ();
	}
	
	/**
	 * Ritorna il percorso della directory privata dell'utente contenente i dati dell'applicazione .
	 *
	 * @return il percorso della directory privata dell'utente contenente i dati dell'applicazione .
	 */	
	public static String getUserApplicationDataDirPath (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (getUserApplicationDirPath ());
		sb.append ("/").append (ResourceNames.USER_DATADIR_NAME);
		return sb.toString ();
	}
	

}

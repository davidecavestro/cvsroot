/*
 * UserResources.java
 *
 * Created on 19 dicembre 2004, 20.57
 */

package com.ost.timekeeper.conf;

/**
 * Risorse applicative legate all'utente.
 *
 * @author  davide
 */
public class UserResources {
	
	/** Costruttore. */
	private UserResources () {
	}
	
	/**
	 * Ritorna il percorso della HOME directory dell'utente.
	 *
	 * @return il percorso della HOME directory dell'utente.
	 */	
	public static String getUserHomeDirPath (){
		return System.getProperty (ResourceNames.USER_HOMEDIR_PATH);
	}
	
	/**
	 * Ritorna il nome dell'acount dell'utente.
	 *
	 * @return il nome dell'acount dell'utente.
	 */	
	public static String getUserAccount (){
		return System.getProperty (ResourceNames.USER_ACCOUNT);
	}
	
	/**
	 * Ritorna il percorso della directory privata dell'utente di supporto all'applicazione .
	 *
	 * @return il percorso della directory privata dell'utente di supporto all'applicazione .
	 */	
	public static String getUserApplicationDirPath (){
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

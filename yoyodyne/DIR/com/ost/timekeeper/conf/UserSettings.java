/*
 * UserSettings.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.*;
import com.ost.timekeeper.ui.*;
import java.awt.*;
import java.util.*;

/**
 * Le impostazioni personalizzate dell'utente.
 *
 * @author  davide
 */
public final class UserSettings extends AbstractSettings implements Observer{
	
	/**
	 * Percorso file impostazioni.
	 */
	public final static String PROPERTIES_PATH = ".timekeeper/conf.properties";
	
	/**
	 * header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = " *** USER SETTINGS *** ";

	/**
	 * Nome proprietà di sistema contenente il percordo della home directory utente.
	 */
	public final static String USER_HOMEDIR_PATH = "user.home";
	
	/**
	 * L'istanza del singleton.
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
	

	
	/**
	 * Ritorna il percorso del file di properties.
	 *
	 * @return il percorso del file di properties.
	 */	
	public String getPropertiesFileName () {
		final StringBuffer sb = new StringBuffer ();
		sb.append (getUserHomeDirPath ()).append ("/").append (PROPERTIES_PATH);
		return sb.toString ();
	}

	public String getPropertiesHeader () {
		return PROPERTIES_HEADER;
	}
	
	/**
	 * Ritorna il percorso della HOME directory dell'utente.
	 *
	 * @return il percorso della HOME directory dell'utente.
	 */	
	public static String getUserHomeDirPath (){
		return System.getProperty (USER_HOMEDIR_PATH);
	}
	
	/**
	 * Imposta il colore del desktop.
	 */
	public void setDesktopColor (Color color) {
		super.setDesktopColor (color);
		notifyChanges ();
	}
	
	/**
	 * Imposta la posizione della finestra principale dell'applicazione.
	 * @param r la posizione.
	 */
	public void setMainFormBounds (Rectangle r) {
		super.setMainFormBounds (r);
//		notifyChanges ();
	}
	
	/**
	 * Notifica le modifiche avvenute.
	 */
	private void notifyChanges (){
		final UserSettingsNotifier notifier = UserSettingsNotifier.getInstance ();
		notifier.setChanged ();
		System.out.println ("notifying user changes");
		
		notifier.notifyObservers (ObserverCodes.USERSETTINGSCHANGE);
	}
	
	public void update (Observable o, Object arg) {
		if (arg!=null && arg.equals (ObserverCodes.APPLICATIONEXITING)){
			/* Applicazione in fase di chiusura. */
			/* Salva ultima posizione finestra principale. */
			this.setMainFormBounds (Application.getInstance ().getMainForm ().getBounds ());
			/* Salva ultima posizione finestre desktop. */
			this.setProgressItemInspectorBounds (ProgressItemInspectorFrame.getInstance ().getBounds ());
			this.setProgressPeriodInspectorBounds (ProgressInspectorFrame.getInstance ().getBounds ());
			this.setProgressListFrameBounds (ProgressListFrame.getInstance ().getBounds ());
		}
	}
	
}

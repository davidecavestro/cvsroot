/*
 * UserSettings.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.ui.chart.ChartFrame;
import java.awt.*;
import java.util.*;

/**
 * Le impostazioni personalizzate dell'utente.
 *
 * @author  davide
 */
public final class UserSettings extends AbstractSettings implements Observer{
	
	/**
	 * header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = " *** USER SETTINGS *** ";

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
		sb.append (
			UserResources.getUserApplicationSettingsDirPath ())
			.append ("/").append (ResourceNames.USER_SETTINGSFILE_NAME);
		return sb.toString ();
	}

	public String getPropertiesHeader () {
		return PROPERTIES_HEADER;
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
			this.setChartFrameBounds (ChartFrame.getInstance ().getBounds ());
			/* Salva larghezza albero */
			this.setProgressItemTreeWidth (new Integer (Application.getInstance ().getMainForm ().getProgressItemTreeWidth ()));
			/* Salva tipo lista avanzamenti. */
			this.setProgressListType (ProgressListFrame.getInstance ().getListType ());
		}
	}
	
}

/*
 * UserSettings.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper.conf;

import java.awt.*;

/**
 *
 * @author  davide
 */
public final class UserSettings extends AbstractSettings {
	
	/**
	 * Percorso file impostazioni.
	 */
	public final static String PROPERTIES_PATH = ".timekeeper/conf.properties";
	
	/**
	 * header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = "USER SETTINGS";
	
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
	 * Ritorna la posizione della finestra principale dell'applicazione.
	 *
	 * @return la posizione della finestra principale dell'applicazione.
	 */	
	public Rectangle getMainFormBounds (){
		final Integer xPos = SettingsSupport.getIntegerProperty (this.getProperties (), PROPNAME_MAINFORM_XPOS);
		if (xPos==null){
			return null;
		}
		final Integer yPos = SettingsSupport.getIntegerProperty (this.getProperties (), PROPNAME_MAINFORM_YPOS);
		if (yPos==null){
			return null;
		}
		final Integer width = SettingsSupport.getIntegerProperty (this.getProperties (), PROPNAME_MAINFORM_WIDTH);
		if (width==null){
			return null;
		}
		final Integer height = SettingsSupport.getIntegerProperty (this.getProperties (), PROPNAME_MAINFORM_HEIGHT);
		if (height==null){
			return null;
		}
		return new Rectangle (xPos.intValue (), yPos.intValue (), width.intValue (), height.intValue ());
	}
	
	/** Imposta la posizione della finestra principale dell'applicazione.
	 * @param r la posizione.
	 */	
	public void setMainFormBounds (Rectangle r){
		this.getProperties ().setProperty (PROPNAME_MAINFORM_XPOS, Double.toString (r.getX ()));
		this.getProperties ().setProperty (PROPNAME_MAINFORM_YPOS, Double.toString (r.getY ()));
		this.getProperties ().setProperty (PROPNAME_MAINFORM_WIDTH, Double.toString (r.getWidth ()));
		this.getProperties ().setProperty (PROPNAME_MAINFORM_HEIGHT, Double.toString (r.getHeight ()));
	}
	
	public String getPropertiesFileName () {
		return PROPERTIES_PATH;
	}

	public String getPropertiesHeader () {
		return PROPERTIES_HEADER;
	}
	
}

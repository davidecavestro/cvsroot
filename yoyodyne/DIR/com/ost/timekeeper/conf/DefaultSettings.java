/*
 * DefaultSettings.java
 *
 * Created on 13 settembre 2004, 22.50
 */

package com.ost.timekeeper.conf;

import java.awt.*;

/**
 * Impostazioni di predefinite.
 *
 * @author  davide
 */
public final class DefaultSettings extends AbstractSettings {
	
	/**
	 * header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = "DEFAULT SETTINGS";
	
	private static DefaultSettings _instance;
	/** 
	 * Ritorna l'istanza delle impostazioni predefinite.
	 */
	public static DefaultSettings getInstance () {
		if (_instance==null){
			_instance = new DefaultSettings ();
		}
		return _instance;
	}
	
	private final int MAINFORM_INSET = 60;
	/**
	 * Ritorna la posizione della finestra principale dell'applicazione.
	 * La posizione risulta indentata 50 pixels da ogni bordo dello schermo.
	 *
	 * @return la posizione della finestra principale dell'applicazione.
	 */	
	public Rectangle getMainFormBounds (){
		final Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		return new Rectangle (MAINFORM_INSET, MAINFORM_INSET,
		screenSize.width  - MAINFORM_INSET*2,
		screenSize.height - MAINFORM_INSET*2);
	}
	
	public String getPropertiesFileName () {
		return null;
	}
	
	public String getPropertiesHeader () {
		return PROPERTIES_HEADER;
	}
	
}

/*
 * ApplicationSettings.java
 *
 * Created on 4 dicembre 2004, 14.15
 */

package com.ost.timekeeper.conf;

import java.awt.*;

/**
 * Impostazioni configurazbili.
 *
 * @author  davide
 */
public interface ApplicationSettings {
	/**
	 * Ritorna la posizione iniziale della finestra principale dell'applicazione.
	 *
	 * @return la posizione iniziale della finestra principale dell'applicazione.
	 */	
	public Rectangle getMainFormBounds ();
	/**
	 * Ritorna la posizione iniziale dello splash screen.
	 *
	 * @return la posizione iniziale dello splash screen.
	 */	
	public Rectangle getSplashScreenBounds ();
	
}

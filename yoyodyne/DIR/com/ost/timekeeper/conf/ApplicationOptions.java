/*
 * ApplicationOptions.java
 *
 * Created on 4 dicembre 2004, 14.12
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.conf.*;
import java.awt.*;

/**
 * Opzioni di configurazione dell'applicazione.
 *
 * @author  davide
 */
public final class ApplicationOptions {
	
	/**
	 * L'istanza di questo singleton.
	 */
	private static ApplicationOptions _instance;
	
	private ApplicationSettings _settings;
	/** 
	 * Costruttore privato, evita istanzazione dall'esterno. 
	 */
	private ApplicationOptions (ApplicationSettings settings) {
		this._settings = settings;
	}
	
	public static ApplicationOptions getInstance (){
		if (_instance==null){
			_instance = new ApplicationOptions (DefaultSettings.getInstance ());
		}
		return _instance;
	}
	
	public Rectangle getMainFormBounds (){
		return this._settings.getMainFormBounds ();
	}
	public Rectangle getSplashScreenBounds (){
		return this._settings.getSplashScreenBounds ();
	}
	
}

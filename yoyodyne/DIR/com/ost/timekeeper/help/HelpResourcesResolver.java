/*
 * HelpResourcesResolver.java
 *
 * Created on 27 dicembre 2004, 13.47
 */

package com.ost.timekeeper.help;

import com.ost.timekeeper.*;
import java.io.*;
import java.util.*;

/**
 * Risolve gli identificatori delle risorse di help con dei target validi.
 * Si appoggia ai file di configurazione.
 *
 * @author  davide
 */
public class HelpResourcesResolver {
	/**
	 * La configurazione.
	 */
	private Properties _properties;
	
	/**
	 * L'istanza del singleton.
	 */
	private static HelpResourcesResolver _instance;
	
	/**
	 * Percorso del file di configuraizone/mappatura, relativo alla directory di 
	 * installazione dell'applicazione.
	 */
	public final String PROPERTIES_PATH = "/helpmap.properties";
	
	/** Costruttore.*/
	private HelpResourcesResolver () {
		this._properties = new Properties ();
		try {
			this._properties.load (new FileInputStream (Application.getEnvironment ().getApplicationDirPath ()+PROPERTIES_PATH));
		} catch (IOException ioe){
			System.out.println ("Missing help resources mapping file");
		}
	}
	
	public static HelpResourcesResolver getInstance (){
		if (_instance==null){
			_instance = new HelpResourcesResolver ();
		}
		return _instance;
	}
	
	/**
	 * Risolve il valore della risorsa di help specificata.
	 * Cerca nel file di mappatura, altrimenti ritorna direttamente il valore della risorsa. 
	 *
	 * @param helpResource la risorsa.
	 * @return il valore della risorsa di help specificata..
	 */	
	public String resolveHelpID (HelpResource helpResource){
		final String resourceValue = helpResource.getValue ();
		/* cerca nel file di mappatura, altrimenti ritornadirettamente il valore della risorsa. */
		return this._properties.getProperty (resourceValue, resourceValue);
	}
	
}

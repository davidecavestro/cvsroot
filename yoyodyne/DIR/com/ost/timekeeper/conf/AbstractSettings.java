/*
 * AbstractSettings.java
 *
 * Created on 4 dicembre 2004, 14.16
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.util.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Implementazione di base delle impostazioni applicative.
 * Il retrieving delle impostazioni avvviene tramite una catena di responsabilità.
 *
 * @author  davide
 */
public abstract class AbstractSettings implements ApplicationSettings {
	
	/**
	 * Posizione sull'asse X della finestra principale dellapplicazione.
	 */
	public final static String PROPNAME_MAINFORM_XPOS = "mainformxpos";
	
	/**
	 * Posizione sull'asse Y della finestra principale dellapplicazione.
	 */
	public final static String PROPNAME_MAINFORM_YPOS = "mainformypos";
	
	/**
	 * Larghezza della finestra principale dellapplicazione.
	 */
	public final static String PROPNAME_MAINFORM_WIDTH = "mainformwidth";
	
	/**
	 * ALtezza della finestra principale dellapplicazione.
	 */
	public final static String PROPNAME_MAINFORM_HEIGHT = "mainformheight";
	
	/**
	 * Costruttore.
	 */
	protected AbstractSettings () {
	}

	/**
	 * Ritorna il nome del file di preferenze associato a queste impostazioni.
	 * @return il nome del file di preferenze associato a queste impostazioni.
	 */	
	public abstract String getPropertiesFileName ();
	
	/**
	 * Carica e ritorna le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws NestedRuntimeException in caso di errori nellapertura del file di risorse.
	 * @return le properties caricate.
	 */	
	public final Properties loadProperties () throws NestedRuntimeException{
		final Properties properties = new Properties();
		try {
			final String persistentFileName = this.getPropertiesFileName ();
			if (persistentFileName!=null){
				final FileInputStream in = new FileInputStream(persistentFileName);
				properties.load(in);
				in.close();
			}
		} catch (FileNotFoundException fnfe) {
			throw new NestedRuntimeException (fnfe);
		} catch (IOException ioe) {
			throw new NestedRuntimeException (ioe);
		}
		return properties;
	}
	
	/**
	 * Salva le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws NestedRuntimeException in caso di errori nell'apertura del file di risorse.
	 */	
	public final void storeProperties () throws NestedRuntimeException{
		final Properties properties = this.getProperties ();
		try {
			final String persistentFileName = this.getPropertiesFileName ();
			if (persistentFileName!=null){
				final FileOutputStream out = new FileOutputStream(persistentFileName);
				properties.store(out, this.getPropertiesHeader ());
				out.flush ();
				out.close();
			}
		} catch (FileNotFoundException fnfe) {
			throw new NestedRuntimeException (fnfe);
		} catch (IOException ioe) {
			throw new NestedRuntimeException (ioe);
		}
	}
	
	/**
	 * Ritorna lo header del file di properties di queste impostazioni.
	 * @return lo header del file di properties di queste impostazioni.
	 */	
	public abstract String getPropertiesHeader ();
	
	/**
	 * Il file contenente le impostazioni applicative persistenti.
	 */
	private Properties _properties;
	/**
	 * Ritorna * il file contenente le impostazioni applicative persistenti.
	 *
	 * @return il file contenente le impostazioni applicative persistenti.
	 */	
	public final Properties getProperties (){
		if (this._properties==null){
			this._properties = this.loadProperties ();
		}
		return this._properties;
	}
	
	private final int SPLAHSCREEN_INSET = 60;
	/**
	 * Ritorna la posizione iniziale dello splash screen.
	 * La posizione risulta indentata 50 pixels da ogni bordo dello schermo.
	 *
	 * @return la posizione iniziale dello splash screen.
	 */	
	public Rectangle getSplashScreenBounds (){
		final Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		return new Rectangle (SPLAHSCREEN_INSET, SPLAHSCREEN_INSET,
		screenSize.width  - SPLAHSCREEN_INSET*2,
		screenSize.height - SPLAHSCREEN_INSET*2);
	}
		
}

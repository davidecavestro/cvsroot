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
public abstract class AbstractSettings implements CustomizableSettings {
	
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
			System.out.println ("File not found "+fnfe.getMessage ());
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
				/*
				 * Gerantisce presenza file.
				 */
				final File persistentFile = new File (persistentFileName);
				FileUtils.makeFilePath (persistentFile);
				final FileOutputStream out = new FileOutputStream(persistentFile);
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
	
	/**
	 * Ritorna la posizione della finestra principale dell'applicazione.
	 *
	 * @return la posizione della finestra principale dell'applicazione.
	 */	
	public Rectangle getMainFormBounds (){
		return SettingsSupport.getRectangle (
			this.getProperties (), 
			PROPNAME_MAINFORM_XPOS, 
			PROPNAME_MAINFORM_YPOS, 
			PROPNAME_MAINFORM_WIDTH, 
			PROPNAME_MAINFORM_HEIGHT);
	}
	
	/**
	 * Ritorna la posizione della finestra di dettaglio nodo di avanzamento.
	 *
	 * @return la posizione della finestra di dettaglio nodo di avanzamento.
	 */	
	public Rectangle getProgressItemInspectorBounds (){
		return SettingsSupport.getRectangle (
			this.getProperties (), 
			PROPNAME_PROGRESSITEMINSPECTOR_XPOS, 
			PROPNAME_PROGRESSITEMINSPECTOR_YPOS, 
			PROPNAME_PROGRESSITEMINSPECTOR_WIDTH, 
			PROPNAME_PROGRESSITEMINSPECTOR_HEIGHT);
	}
	
	/**
	 * Ritorna la posizione della finestra di dettaglio periodo di avanzamento.
	 *
	 * @return la posizione della finestra di dettaglio periodo di avanzamento.
	 */	
	public Rectangle getProgressPeriodInspectorBounds (){
		return SettingsSupport.getRectangle (
			this.getProperties (), 
			PROPNAME_PROGRESSPERIODINSPECTOR_XPOS, 
			PROPNAME_PROGRESSPERIODINSPECTOR_YPOS, 
			PROPNAME_PROGRESSPERIODINSPECTOR_WIDTH, 
			PROPNAME_PROGRESSPERIODINSPECTOR_HEIGHT);
	}
	
	/**
	 * Ritorna la posizione della finestra di elenco avanzamenti.
	 *
	 * @return la posizione della finestra di elenco avanzamenti.
	 */	
	public Rectangle getProgressListFrameBounds (){
		return SettingsSupport.getRectangle (
			this.getProperties (), 
			PROPNAME_PROGRESSLISTFRAME_XPOS, 
			PROPNAME_PROGRESSLISTFRAME_YPOS, 
			PROPNAME_PROGRESSLISTFRAME_WIDTH, 
			PROPNAME_PROGRESSLISTFRAME_HEIGHT);
	}
	
	/** Imposta la posizione della finestra principale dell'applicazione.
	 * @param r la posizione.
	 */	
	public void setMainFormBounds (Rectangle r){
		SettingsSupport.setRectangle (
			this.getProperties (), 
			r, 
			PROPNAME_MAINFORM_XPOS, 
			PROPNAME_MAINFORM_YPOS, 
			PROPNAME_MAINFORM_WIDTH, 
			PROPNAME_MAINFORM_HEIGHT);
	}
	
	/** Imposta la posizione della finestra di dettaglio nodo di avanzamento.
	 * @param r la posizione.
	 */	
	public void setProgressItemInspectorBounds (Rectangle r){
		SettingsSupport.setRectangle (
			this.getProperties (), 
			r, 
			PROPNAME_PROGRESSITEMINSPECTOR_XPOS, 
			PROPNAME_PROGRESSITEMINSPECTOR_YPOS, 
			PROPNAME_PROGRESSITEMINSPECTOR_WIDTH, 
			PROPNAME_PROGRESSITEMINSPECTOR_HEIGHT);
	}	
	
	/** Imposta la posizione della finestra di elkenco avanzamenti.
	 * @param r la posizione.
	 */	
	public void setProgressListFrameBounds (Rectangle r) {
		SettingsSupport.setRectangle (
			this.getProperties (), 
			r, 
			PROPNAME_PROGRESSLISTFRAME_XPOS, 
			PROPNAME_PROGRESSLISTFRAME_YPOS, 
			PROPNAME_PROGRESSLISTFRAME_WIDTH, 
			PROPNAME_PROGRESSLISTFRAME_HEIGHT);
	}
	
	/** Imposta la posizione della finestra di dettaglio periodo di avanzamento.
	 * @param r la posizione.
	 */	
	public void setProgressPeriodInspectorBounds (Rectangle r) {
		SettingsSupport.setRectangle (
			this.getProperties (), 
			r, 
			PROPNAME_PROGRESSPERIODINSPECTOR_XPOS, 
			PROPNAME_PROGRESSPERIODINSPECTOR_YPOS, 
			PROPNAME_PROGRESSPERIODINSPECTOR_WIDTH, 
			PROPNAME_PROGRESSPERIODINSPECTOR_HEIGHT);
	}
	
	public String getLogDirPath (){
		return SettingsSupport.getStringProperty (this.getProperties (), PROPNAME_LOGDIRPATH);		
	}
	
	/**
	 * Ritorna il colore del desktop.
	 *
	 * @return il colore del desktop.
	 */
	public Color getDesktopColor () {
		return SettingsSupport.getColorProperty (this.getProperties (), PROPNAME_DESKTOPCOLOR);
	}
	
	public void setDesktopColor (Color color) {
		SettingsSupport.setColorProperty (this.getProperties (), PROPNAME_DESKTOPCOLOR, color);
	}
	
}

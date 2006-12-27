/*
 * AbstractSettings.java
 *
 * Created on 4 dicembre 2004, 14.16
 */

package com.davidecavestro.timekeeper.conf;

import com.davidecavestro.common.util.ExceptionUtils;
import com.davidecavestro.common.util.NestedRuntimeException;
import com.davidecavestro.common.util.file.FileUtils;
import com.davidecavestro.common.util.settings.SettingsSupport;
import com.davidecavestro.timekeeper.Application;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * Implementazione di base delle impostazioni applicative.
 * Il retrieving delle impostazioni avvviene tramite una catena di responsabilita'.
 *
 * @author  davide
 */
public abstract class AbstractSettings implements CustomizableSettings {
	
	private final Application _application;
	
	/**
	 * Costruttore.
	 * @param application l'applicazione.
	 */
	protected AbstractSettings (final Application application) {
		this._application = application;
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
			try {
			this._application.getLogger ().warning ( "Error loading propoerties. ", fnfe);
			} catch (Exception e){
				/* evita eccezioni dovute a dipendenze inizializzazione*/
				System.out.println (ExceptionUtils.getStackTrace (e));
				System.out.println ("Above error IS NOT a bad thing if you are running this application for the first time.");
			}
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
	
	public String getLogDirPath (){
		return SettingsSupport.getStringProperty (this.getProperties (), PROPNAME_LOGDIRPATH);		
	}
	
	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public Integer getPlainTextLogBufferSize (){
		return SettingsSupport.getIntegerProperty (this.getProperties (), PLAINTEXTLOG_BUFFERSIZE);
	}
	/**
	 * Imposta la dimensione del buffer per il logger di testo semplice.
	 */
	public void setPlainTextLogBufferSize (final Integer size){
		SettingsSupport.setIntegerProperty (this.getProperties (), PLAINTEXTLOG_BUFFERSIZE, size);
	}
	
	/**
	 * Ritorna il LookAndFeel.
	 *
	 * @return il LookAndFeel.
	 */	
	public String getLookAndFeel (){
		return SettingsSupport.getStringProperty (this.getProperties (), PROPNAME_LOOKANDFEEL);		
	}	

	/**
	 * Imposta il LookAndFeel.
	 *
	 * @param lookAndFeel il Look And Feel
	 */	
	public void setLookAndFeel (final String lookAndFeel) {
		SettingsSupport.setStringProperty (this.getProperties (), PROPNAME_LOOKANDFEEL, lookAndFeel);
	}

	public String getJDOUserName () {
		return SettingsSupport.getStringProperty (this.getProperties (), PROPNAME_JDOSTORAGEUSER_NAME);
	}

	public void setJDOUserName (String s) {
		SettingsSupport.setStringProperty (this.getProperties (), PROPNAME_JDOSTORAGEUSER_NAME, s);
	}

	public String getJDOStorageName () {
		return SettingsSupport.getStringProperty (this.getProperties (), PROPNAME_JDOSTORAGE_NAME);
	}

	public void setJDOStorageName (String s) {
		SettingsSupport.setStringProperty (this.getProperties (), PROPNAME_JDOSTORAGE_NAME, s);
	}

	public String getJDOStorageDirPath () {
		return SettingsSupport.getStringProperty (this.getProperties (), PROPNAME_JDOSTORAGEDIR_PATH);
	}

	public void setJDOStorageDirPath (String s) {
		SettingsSupport.setStringProperty (this.getProperties (), PROPNAME_JDOSTORAGEDIR_PATH, s);
	}

	public void setLastProjectName (String s) {
		SettingsSupport.setStringProperty (this.getProperties (), PROPNAME_LASTPROJECTNAME, s);
	}

	
}

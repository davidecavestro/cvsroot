/*
 * AbstractSettings.java
 *
 * Created on 4 dicembre 2004, 14.16
 */

package com.davidecavestro.timekeeper.conf;

import com.davidecavestro.common.log.NotificationUtils;
import com.davidecavestro.common.util.ExceptionUtils;
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

	protected Application getApplication () {
		return _application;
	}
	
	/**
	 * Ritorna il nome del file di preferenze associato a queste impostazioni.
	 * @return il nome del file di preferenze associato a queste impostazioni.
	 */	
	public abstract String getPropertiesFileName ();
	
	/**
	 * Carica e ritorna le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws RuntimeException in caso di errori nellapertura del file di risorse.
	 * @return le properties caricate.
	 */	
	public final Properties loadProperties () throws RuntimeException{
		final Properties properties = new Properties();
		try {
			final String persistentFileName = getPropertiesFileName ();
			if (persistentFileName!=null){
				final FileInputStream in = new FileInputStream(persistentFileName);
				properties.load(in);
				in.close();
			}
		} catch (FileNotFoundException fnfe) {
			try {
			this._application.getLogger ().warning ( fnfe, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Error_loading_properties._"));
			} catch (Exception e){
				/* evita eccezioni dovute a dipendenze inizializzazione*/
				new NotificationUtils ().error (e, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Above_error_IS_NOT_a_bad_thing_if_you_are_running_this_application_for_the_first_time."));
			}
		} catch (IOException ioe) {
			throw new RuntimeException (ioe);
		}
		return properties;
	}
	
	/**
	 * Salva le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws RuntimeException in caso di errori nell'apertura del file di risorse.
	 */	
	public final void storeProperties () throws RuntimeException{
		final Properties properties = getProperties ();
		try {
			final String persistentFileName = getPropertiesFileName ();
			if (persistentFileName!=null){
				/*
				 * Gerantisce presenza file.
				 */
				final File persistentFile = new File (persistentFileName);
				FileUtils.makeFilePath (persistentFile);
				final FileOutputStream out = new FileOutputStream(persistentFile);
				properties.store(out, getPropertiesHeader ());
				out.flush ();
				out.close();
			}
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException (fnfe);
		} catch (IOException ioe) {
			throw new RuntimeException (ioe);
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
		return SettingsSupport.getStringProperty (getProperties (), PROPNAME_LOGDIRPATH);		
	}
	
	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public Integer getPlainTextLogBufferSize (){
		return SettingsSupport.getIntegerProperty (getProperties (), PLAINTEXTLOG_BUFFERSIZE);
	}
	/**
	 * Imposta la dimensione del buffer per il logger di testo semplice.
	 */
	public void setPlainTextLogBufferSize (final Integer size){
		SettingsSupport.setIntegerProperty (getProperties (), PLAINTEXTLOG_BUFFERSIZE, size);
	}
	
	/**
	 * Ritorna il LookAndFeel.
	 *
	 * @return il LookAndFeel.
	 */	
	public String getLookAndFeel (){
		return SettingsSupport.getStringProperty (getProperties (), PROPNAME_LOOKANDFEEL);		
	}	

	/**
	 * Imposta il LookAndFeel.
	 *
	 * @param lookAndFeel il Look And Feel
	 */	
	public void setLookAndFeel (final String lookAndFeel) {
		SettingsSupport.setStringProperty (getProperties (), PROPNAME_LOOKANDFEEL, lookAndFeel);
	}

	public String getJDOUserName () {
		return SettingsSupport.getStringProperty (getProperties (), PROPNAME_JDOSTORAGEUSER_NAME);
	}

	public void setJDOUserName (final String s) {
		SettingsSupport.setStringProperty (getProperties (), PROPNAME_JDOSTORAGEUSER_NAME, s);
	}

	public String getJDOStorageName () {
		return SettingsSupport.getStringProperty (getProperties (), PROPNAME_JDOSTORAGE_NAME);
	}

	public void setJDOStorageName (final String s) {
		SettingsSupport.setStringProperty (getProperties (), PROPNAME_JDOSTORAGE_NAME, s);
	}

	public String getJDOStorageDirPath () {
		return SettingsSupport.getStringProperty (getProperties (), PROPNAME_JDOSTORAGEDIR_PATH);
	}

	public void setJDOStorageDirPath (final String s) {
		SettingsSupport.setStringProperty (getProperties (), PROPNAME_JDOSTORAGEDIR_PATH, s);
	}

	public void setLastProjectName (final String s) {
		SettingsSupport.setStringProperty (getProperties (), PROPNAME_LASTPROJECTNAME, s);
	}

	
	public void setChartGhostEnabled (final Boolean b) {
		SettingsSupport.setBooleanProperty (getProperties (), PROPNAME_CHARTGHOSTENABLED, b);
	}
	
	public Boolean getChartGhostEnabled () {
		return SettingsSupport.getBooleanProperty (getProperties (), PROPNAME_CHARTGHOSTENABLED);
	}

	public Integer getChartDepth () {
		return SettingsSupport.getIntegerProperty (getProperties (), PROPNAME_CHARTDEPTH);
	}

	public void setCharDepth (final Integer i) {
		SettingsSupport.setIntegerProperty (getProperties (), PROPNAME_CHARTDEPTH, i);
	}
	
}

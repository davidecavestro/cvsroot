/*
 * CustomizableSettings.java
 *
 * Created on 11 dicembre 2004, 15.02
 */

package com.davidecavestro.timekeeper.conf;

import java.awt.*;
import java.util.*;

/**
 * Impostazione applicative personalizzabili.
 *
 * @author  davide
 */
public interface CustomizableSettings extends ApplicationSettings{
	
	
	/** Dimensione del buffer per il log di testo semplice. */
	public final static String PLAINTEXTLOG_BUFFERSIZE = "palintextlogbuffersize";
	
	/**
	 * Percorso della directory contenente i file di log.
	 */
	public final static String PROPNAME_LOGDIRPATH = "logdirpath";
	
	/**
	 * Il LookAndFeel.
	 */
	public final static String PROPNAME_LOOKANDFEEL = "lookandfeel";
		
	
	/**
	 * Parametro che definisce il nome dell'utente da usare per l'0accesso allo storage JDO.
	 */
	public final static String PROPNAME_JDOSTORAGEUSER_NAME = "jdousername";

	/**
	 * Parametro che definisce il nome dello storage JDO per la persistenza.
	 */
	public final static String PROPNAME_JDOSTORAGE_NAME = "jdostoragename";

	/**
	 * Parametro che definisce il percorso della directory contenente lo storage JDO per la persistenza.
	 */
	public final static String PROPNAME_JDOSTORAGEDIR_PATH = "jdostoragedirpath";
	
	/**
	 * Parametro che definisce ilnome dell'ultimo progetto utilizzato.
	 */
	public final static String PROPNAME_LASTPROJECTNAME = "lastprojectname";
	
		
	/**
	 * Ritorna il nome del file di preferenze associato a queste impostazioni.
	 * @return il nome del file di preferenze associato a queste impostazioni.
	 */	
	public String getPropertiesFileName ();
	
	/**
	 * Carica e ritorna le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws RuntimeException in caso di errori nellapertura del file di risorse.
	 * @return le properties caricate.
	 */	
	public Properties loadProperties () throws RuntimeException;
	
	/**
	 * Salva le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws RuntimeException in caso di errori nell'apertura del file di risorse.
	 */	
	public void storeProperties () throws RuntimeException;
	
	/**
	 * Ritorna lo header del file di properties di queste impostazioni.
	 * @return lo header del file di properties di queste impostazioni.
	 */	
	public String getPropertiesHeader ();
	
	/**
	 * Ritorna il file contenente le impostazioni applicative persistenti.
	 *
	 * @return il file contenente le impostazioni applicative persistenti.
	 */	
	public Properties getProperties ();
	
	
	/**
	 * Impostala dimensione del buffer per il logger di testo semplice.
	 */
	public void setPlainTextLogBufferSize (final Integer size);
	
	/**
	 * Imposta il LookAndFeel.
	 */
	void setLookAndFeel (final String lookAndFeel);
	
	/**
	 * Imposta il nome utente da utilizare per accedere allo storage JDo.
	 *
	 * @param s il nuovo valore.
	 */
	void setJDOUserName (String s);

	/**
	 * Imposta il nome dello storage JDo.
	 *
	 * @param s il nuovo valore.
	 */
	void setJDOStorageName (String s);

	/**
	 * Imposta il percorso della directory da usare per lo storage JDO.
	 * 
	 * @param s il nuovo valore.
	 */
	void setJDOStorageDirPath (String s);	

	/**
	 * Imposta il nome dell'ultimo project caricato.
	 * 
	 * @param s il nome dell'ultimo project caricato.
	 */
	void setLastProjectName (String s);	
	
}

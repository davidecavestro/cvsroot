/*
 * ApplicationSettings.java
 *
 * Created on 4 dicembre 2004, 14.15
 */

package com.davidecavestro.timekeeper.conf;

import java.awt.*;

/**
 * Impostazioni configurazbili.
 *
 * @author  davide
 */
public interface ApplicationSettings {
	/**
	 * Ritorna il percorso della directory contenente i file di log.
	 *
	 * @return il percorso della directory contenente i file di log.
	 */
	public String getLogDirPath ();
	
	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public Integer getPlainTextLogBufferSize ();
	
	/**
	 * Ritorna il LookAndFeel.
	 *
	 * @return il LookAndFeel.
	 */	
	String getLookAndFeel ();
	
	/**
	 * Ritorna il nome dell'ultimo progetto.
	 * @return il nome dell'ultimo progetto.
	 */
	String getLastProjectName ();
	
	/**
	 * Ritorna il nome utente da utilizare per accedere allo storage JDo.
	 * @return il nome utente da utilizare per accedere allo storage JDo.
	 */
	String getJDOUserName ();

	/**
	 * Ritorna il nome dello storage JDo.
	 * 
	 * @return il nome dello storage JDo.
	 */
	String getJDOStorageName ();

	/**
	 * Ritorna il percorso della directory da usare per lo storage JDO.
	 * 
	 * @return il percorso della directory da usare per lo storage JDO.
	 */
	String getJDOStorageDirPath ();
}

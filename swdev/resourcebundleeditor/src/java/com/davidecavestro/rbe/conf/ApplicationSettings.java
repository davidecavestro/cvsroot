/*
 * ApplicationSettings.java
 *
 * Created on 4 dicembre 2004, 14.15
 */

package com.davidecavestro.rbe.conf;

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
	
}

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
	 * Ritorna la posizione iniziale della finestra di dettaglion odo di avanzamento.
	 * @return la posizione iniziale della finestra di dettaglion odo di avanzamento.
	 */	
	public Rectangle getProgressItemInspectorBounds ();
		
	/** 
	 * Ritorna la posizione iniziale della finestra di dettaglio periodo di avanzamento.
	 * @return la posizione iniziale della finestra di dettaglio periodo di avanzamento.
	 */	
	public Rectangle getProgressPeriodInspectorBounds ();
		
	/** 
	 * Ritorna la posizione iniziale della finestra di elenco avanzamenti.
	 * @return la posizione iniziale della finestra di elenco avanzamenti.
	 */	
	public Rectangle getProgressListFrameBounds ();
	
	/**
	 * Ritorna il percorso della directory contenente i file di log.
	 *
	 * @return il percorso della directory contenente i file di log.
	 */
	public String getLogDirPath ();
	
	
	/**
	 * Ritorna il colore del desktop.
	 *
	 * @return il colore del desktop.
	 */	
	public Color getDesktopColor ();
}

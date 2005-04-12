/*
 * ApplicationSettings.java
 *
 * Created on 4 dicembre 2004, 14.15
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.view.*;
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
	 * Ritorna la posizione iniziale della finestra di dettaglio nodo di avanzamento.
	 * @return la posizione iniziale della finestra di dettaglio nodo di avanzamento.
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
	
	/**
	 * Ritorna lo stato di abilitazione della notifica sonora in presenza di eventi.
	 * @return lo stato di abilitazione della notifica sonora in presenza di eventi.
	 */
	public Boolean beepOnEvents ();
	
	/**
	 * Ritorna lo stato di abilitazione dell'autocaricamento grafico ad anello.
	 * @return lo stato di abilitazione dell'autocaricamento grafico ad anello.
	 */
	public Boolean ringChartAutoload ();
	
	/**
	 * Ritorna lo stato di abilitazione dell'autocaricamento grafico a barre.
	 * @return lo stato di abilitazione dell'autocaricamento grafico a barre.
	 */
	public Boolean barChartAutoload ();
	
	/**
	 * Ritorna il tipo di lista degli avanzamenti.
	 *
	 * @return il tipo di lista degli avanzamenti.
	 */
	public ProgressListType getProgressListType ();
	
	/**
	 * Ritorna il percorso della directory contenente i dati persistenti (JDO).
	 *
	 * @return il percorso della directory contenente i dati persistenti (JDO).
	 */
	public String getJDOStorageDirPath ();
	
	/**
	 * Ritorna il nome dello storage JDO (i file contenenti i dati persistenti e gli indici).
	 *
	 * @return il nome dello storage JDO (i file contenenti i dati persistenti e gli indici).
	 */
	public String getJDOStorageName ();
	
	/**
	 * Ritorna il nome dell'utente JDO.
	 *
	 * @return il nome dell'utente JDO.
	 */
	public String getJDOUserName ();
	
	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public Integer getPlainTextLogBufferSize ();
	
	/**
	 * Ritorna l'ampiezza dell'albero dei nodi di avanzamento.
	 *
	 * @return l'ampiezza dell'albero dei nodi di avanzamento..
	 */
	public Integer getProgressItemTreeWidth ();
	
	/**
	 * Ritorna la posizione iniziale della finestra di gestione grafici.
	 * @return la posizione iniziale della finestra di gestione grafici.
	 */
	public Rectangle getChartFrameBounds ();
	
	/**
	 * Ritorna il numero di livelli visibili per il grafico ad anello.
	 * @return il numero di livelli visibili per il grafico ad anello.
	 */
	Integer getRingChartVisibleLevels ();
	
	/**
	 * Ritorna il LookAndFeel.
	 *
	 * @return il LookAndFeel.
	 */	
	String getLookAndFeel ();
	
	/**
	 * Ritorna il nome dell'ultimo progetto caricato.
	 *
	 * @return il nome dell'ultimo progetto caricato.
	 */	
	String getLastProjectName ();
}

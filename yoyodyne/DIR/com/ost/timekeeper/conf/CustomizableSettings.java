/*
 * CustomizableSettings.java
 *
 * Created on 11 dicembre 2004, 15.02
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;
import java.awt.*;
import java.util.*;

/**
 * Impostazione applicative personalizzabili.
 *
 * @author  davide
 */
public interface CustomizableSettings extends ApplicationSettings{
	
	/** Posizione sull'asse X della finestra principale dell'applicazione. */
	public final static String PROPNAME_MAINFORM_XPOS = "mainformxpos";
	/** Posizione sull'asse Y della finestra principale dell'applicazione. */
	public final static String PROPNAME_MAINFORM_YPOS = "mainformypos";
	/** Larghezza della finestra principale dell'applicazione. */
	public final static String PROPNAME_MAINFORM_WIDTH = "mainformwidth";
	/** Altezza della finestra principale dell'applicazione. */
	public final static String PROPNAME_MAINFORM_HEIGHT = "mainformheight";
	
	/** Posizione sull'asse X della finestra di dettaglio nodo di avanzamento. */
	public final static String PROPNAME_PROGRESSITEMINSPECTOR_XPOS = "progressiteminspectorxpos";
	/** Posizione sull'asse Y della finestra di dettaglio nodo di avanzamento. */
	public final static String PROPNAME_PROGRESSITEMINSPECTOR_YPOS = "progressiteminspectorypos";
	/** Larghezza della finestra di dettaglio nodo di avanzamento. */
	public final static String PROPNAME_PROGRESSITEMINSPECTOR_WIDTH = "progressiteminspectorwidth";
	/** Altezza della finestra di dettaglio nodo di avanzamento. */
	public final static String PROPNAME_PROGRESSITEMINSPECTOR_HEIGHT = "progressiteminspectorheight";
	
	/** Posizione sull'asse X della finestra di dettaglio periodo di avanzamento. */
	public final static String PROPNAME_PROGRESSPERIODINSPECTOR_XPOS = "progressperiodinspectorxpos";
	/** Posizione sull'asse Y della finestra di dettaglio periodo di avanzamento. */
	public final static String PROPNAME_PROGRESSPERIODINSPECTOR_YPOS = "progressperiodinspectorypos";
	/** Larghezza della finestra di dettaglio periodo di avanzamento. */
	public final static String PROPNAME_PROGRESSPERIODINSPECTOR_WIDTH = "progressperiodinspectorwidth";
	/** Altezza della finestra di dettaglio periodo di avanzamento. */
	public final static String PROPNAME_PROGRESSPERIODINSPECTOR_HEIGHT = "progressperiodinspectorheight";
	
	/** Posizione sull'asse X della finestra di elenco avanzamenti. */
	public final static String PROPNAME_PROGRESSLISTFRAME_XPOS = "progresslistframexpos";
	/** Posizione sull'asse Y della finestra di elenco avanzamenti. */
	public final static String PROPNAME_PROGRESSLISTFRAME_YPOS = "progresslistframeypos";
	/** Larghezza della finestra di elenco avanzamenti. */
	public final static String PROPNAME_PROGRESSLISTFRAME_WIDTH = "progresslistframewidth";
	/** Altezza della finestra di elenco avanzamenti. */
	public final static String PROPNAME_PROGRESSLISTFRAME_HEIGHT = "progresslistframeheight";
	
	/** Posizione sull'asse X della finestra di gestione grafici. */
	public final static String PROPNAME_CHARTFRAME_XPOS = "chartframexpos";
	/** Posizione sull'asse Y della finestra di gestione grafici. */
	public final static String PROPNAME_CHARTFRAME_YPOS = "chartframeypos";
	/** Larghezza della finestra di gestione grafici. */
	public final static String PROPNAME_CHARTFRAME_WIDTH = "chartframewidth";
	/** Altezza della finestra di gestione grafici. */
	public final static String PROPNAME_CHARTFRAME_HEIGHT = "chartframeheight";
	
	/** Larghezza dell'albero dei nodi di avanzamento. */
	public final static String PROPNAME_PROGRESSITEMTREE_WIDTH = "progressitemtreewidth";
	
	
	/** Beep in presenza di eventi. */
	public final static String PROPNAME_BEEPONEVENTS = "beeponevents";
	
	/** Tipo di lista avanzamenti. */
	public final static String PROPNAME_PROGRESSLISTTYPE = "progresslisttype";
	
	/** Dimensione del buffer per il log di testo semplice. */
	public final static String PLAINTEXTLOG_BUFFERSIZE = "palintextlogbuffersize";
	
	/**
	 * Percorso della directory contenente i file di log.
	 */
	public final static String PROPNAME_LOGDIRPATH = "logdirpath";
	
	/**
	 * Colore desktop.
	 */
	public final static String PROPNAME_DESKTOPCOLOR = "desktopcolor";
	
	/**
	 * Percorso della directory contenente i dati persistenti JDO.
	 */
	public final static String PROPNAME_JDOSTORAGEDIRPATH = "jdostoragedirpath";
	
	/**
	 * Nome dei file contenenti di datipersistenti.
	 */
	public final static String PROPNAME_JDOSTORAGENAME = "jdostoragename";
	
	/**
	 * Nome dell'utente JDO.
	 */
	public final static String PROPNAME_JDOUSERNAME = "jdousername";
	
	/**
	 * Numero livelli visibili nel grafico ad anello.
	 */
	public final static String PROPNAME_RINGCHART_VISIBLELEVELS = "ringchartvisiblelevels";
	
	
	/**
	 * Ritorna il nome del file di preferenze associato a queste impostazioni.
	 * @return il nome del file di preferenze associato a queste impostazioni.
	 */	
	public String getPropertiesFileName ();
	
	/**
	 * Carica e ritorna le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws NestedRuntimeException in caso di errori nellapertura del file di risorse.
	 * @return le properties caricate.
	 */	
	public Properties loadProperties () throws NestedRuntimeException;
	
	/**
	 * Salva le properties a partire dal nome del file associato a queste impostazioni.
	 *
	 * @throws NestedRuntimeException in caso di errori nell'apertura del file di risorse.
	 */	
	public void storeProperties () throws NestedRuntimeException;
	
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
	 * Imposta il colore del desktop.
	 *
	 */
	public void setDesktopColor (Color color);
	
	/** Imposta la posizione della finestra principale dell'applicazione.
	 * @param r la posizione.
	 */	
	public void setMainFormBounds (Rectangle r);
	
	/** Imposta la posizione della finestra di dettaglion odo di avanzamento.
	 * @param r la posizione.
	 */	
	public void setProgressItemInspectorBounds (Rectangle r);
		
	/** Imposta la posizione della finestra di dettaglio periodo di avanzamento.
	 * @param r la posizione.
	 */	
	public void setProgressPeriodInspectorBounds (Rectangle r);
		
	/** Imposta la posizione della finestra di elenco avanzamenti.
	 * @param r la posizione.
	 */	
	public void setProgressListFrameBounds (Rectangle r);
	
	/** Imposta la posizione della finestra di gestione grafici.
	 * @param r la posizione.
	 */	
	public void setChartFrameBounds (Rectangle r);
	
	/**
	 * Imposta lo stato di abilitazione della notifica sonora in presenza di eventi.
	 * @param beep lo stato di abilitazione.
	 */
	public void setBeepOnEvents (Boolean beep);
	
	/**
	 * Imposta il tipo di lista degli avanzamenti.
	 */
	public void setProgressListType (ProgressListType type);
	
	/**
	 * Ritorna il percorso della directory contenente i dati persistenti (JDO).
	 *
	 * @return il percorso della directory contenente i dati persistenti (JDO).
	 */
	public void setJDOStorageDirPath (String path);
	
	/**
	 * Ritorna il nome dello storage JDO (i file contenenti i dati persistenti e gli indici).
	 *
	 * @return il nome dello storage JDO (i file contenenti i dati persistenti e gli indici).
	 */
	public void setJDOStorageName (final String name);
	
	/**
	 * Imposta il nome dell'utente JDO.
	 *
	 * @param name il nome dell'utente JDO.
	 */
	public void setJDOUserName (final String name);
	
	/**
	 * Impostala dimensione del buffer per il logger di testo semplice.
	 */
	public void setPlainTextLogBufferSize (final Integer size);
	
	/**
	 * Imposta la larghezza dell'albero dei nodi di avanzamento.
	 */
	public void setProgressItemTreeWidth (final Integer width);
	
	/**
	 * Imposta il numero di livelli visibili per il grafico ad anello.
	 */
	void setRingChartVisibleLevels (final Integer depth);
}

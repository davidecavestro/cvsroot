/*
 * CustomizableSettings.java
 *
 * Created on 11 dicembre 2004, 15.02
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.util.*;
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
	
	/**
	 * Percorso della directory contenente i file di log.
	 */
	public final static String PROPNAME_LOGDIRPATH = "logdirpath";
	
	/**
	 * Colore desktop.
	 */
	public final static String PROPNAME_DESKTOPCOLOR = "desktopcolor";
	
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
}

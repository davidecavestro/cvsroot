/*
 * DefaultSettings.java
 *
 * Created on 13 settembre 2004, 22.50
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.view.*;
import java.awt.*;
import javax.swing.*;

/**
 * Impostazioni di predefinite.
 *
 * @author  davide
 */
public final class DefaultSettings implements ApplicationSettings {
	
	/**
	 * Header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = "DEFAULT SETTINGS";

	/**
	 * Dimensione predefinita per il buffer del log di testo semplice
	 */
	public final static Integer DEFAULT_PLAINTEXTLOG_BUFFERSIZE = new Integer (8192);
	
	/**
	 * Il tipo di lista di avanzamento predefinito.
	 */
	private ProgressListType DEFAULT_PROGRESSLISTTYPE = ProgressListType.SUBTREE;
	
	private static DefaultSettings _instance;
	
	/** Costruttore privato. */
	private DefaultSettings (){}
	
	/**
	 *
	 * Ritorna l'istanza delle impostazioni predefinite.
	 *
	 * @return l'istanza delle impostazioni predefinite.
	 */
	public static DefaultSettings getInstance () {
		if (_instance==null){
			_instance = new DefaultSettings ();
		}
		return _instance;
	}
	
	/**
	 * La dimensione della distanza dal bordo per la finestra principale.
	 */
	private final int MAINFORM_INSET = 60;
	/**
	 * Ritorna la posizione della finestra principale dell'applicazione.
	 * La posizione risulta indentata 50 pixels da ogni bordo dello schermo.
	 *
	 * @return la posizione della finestra principale dell'applicazione.
	 */	
	public Rectangle getMainFormBounds (){
		final Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		return new Rectangle (MAINFORM_INSET, MAINFORM_INSET,
		screenSize.width  - MAINFORM_INSET*2,
		screenSize.height - MAINFORM_INSET*2);
	}
	
	/**
	 * Ritorna il percorso della directory contenente i file di log.
	 * Il valore di ritorno risulta composto nel seguente modo:
	 * <CODE>Application.getEnvironment ().getApplicationDirPath ()+"/logs/";</CODE>
	 *
	 * @return il percorso della directory contenente i file di log.
	 */
	public String getLogDirPath () {
		final StringBuffer sb = new StringBuffer ();
		final String applicationDirPath = Application.getEnvironment ().getApplicationDirPath ();
		if (applicationDirPath!=null){
			sb.append (applicationDirPath);
		} else {
			sb.append (System.getProperty (ResourceNames.USER_WORKINGDIR_PATH));
		}
		sb.append ("/logs");
		return sb.toString ();
	}
	
	/**
	 * Ritorna il colore del desktop.
	 * Per questa implementazione vale sempre <TT>BLACK</TT>.
	 *
	 * @return ilcolore del desktop.
	 */	
	public Color getDesktopColor () {
		return Color.BLACK;
	}

	/**
	 * Offset orizzontale per frame desktop.
	 */
	private final static int DESKTOP_FRAMES_OFFSETX = 30;
	/**
	 * Offset verticale per frame desktop.
	 */
	private final static int DESKTOP_FRAMES_OFFSETY = 30;
	
	/** Il livello della finestra di dettaglio nodo di avanzamento sul desktop. */
	protected final int PROGRESSITEMINSPECTOR_LEVEL = 1;
	/** 
	 * Ritorna la posizione iniziale della finestra di dettaglio nodo di avanzamento.
	 * Per questa implementazione il valore è quello che permette di posizionare 
	 * la finestra al livello {@link #PROGRESSITEMINSPECTOR_PROSITION}.
	 *
	 * @return la posizione iniziale della finestra di dettaglio nodo di avanzamento.
	 */	
	public Rectangle getProgressItemInspectorBounds () {
		final ProgressItemInspectorFrame frame = ProgressItemInspectorFrame.getInstance ();
		return new Rectangle (
			DESKTOP_FRAMES_OFFSETX * PROGRESSITEMINSPECTOR_LEVEL, 
			DESKTOP_FRAMES_OFFSETY * PROGRESSITEMINSPECTOR_LEVEL, 
			frame.getWidth (), 
			frame.getHeight () 
		);
	}
	
	/** Il livello della finestra di elenco avanzamenti sul desktop. */
	protected final int PROGRESSLIST_LEVEL = 2;
	/** 
	 * Ritorna la posizione iniziale della finestra di elenco avanzamenti.
	 * Per questa implementazione il valore è quello che permette di posizionare 
	 * la finestra al livello {@link #PROGRESSLIST_PROSITION}.
	 *
	 * @return la posizione iniziale della finestra di elenco avanzamenti.
	 */	
	public Rectangle getProgressListFrameBounds () {
		final ProgressListFrame frame = ProgressListFrame.getInstance ();
		return new Rectangle (
			DESKTOP_FRAMES_OFFSETX * PROGRESSLIST_LEVEL, 
			DESKTOP_FRAMES_OFFSETY * PROGRESSLIST_LEVEL, 
			frame.getWidth (), 
			frame.getHeight () 
		);
	}
	
	/** Il livello della finestra di dettaglio periodo sul desktop. */
	protected final int PROGRESSINSPECTOR_LEVEL = 0;
	/** 
	 * Ritorna la posizione iniziale della finestra di dettaglio periodo di avanzamento.
	 * Per questa implementazione il valore è quello che permette di 
	 * posizionare la finestra al livello {@link #PROGRESSINSPECTOR_PROSITION}.
	 * 
	 * @return la posizione iniziale della finestra di dettaglio periodo di avanzamento.
	 */	
	public Rectangle getProgressPeriodInspectorBounds () {
		final ProgressInspectorFrame frame = ProgressInspectorFrame.getInstance ();
		return new Rectangle (
			DESKTOP_FRAMES_OFFSETX * PROGRESSINSPECTOR_LEVEL, 
			DESKTOP_FRAMES_OFFSETY * PROGRESSINSPECTOR_LEVEL, 
			frame.getWidth (), 
			frame.getHeight () 
		);
	}
	
	/**
	 * Notifica sonora in presenza di eventi. 
	 * Per questa implementazione il valore è sempre <TT>TRUE</TT>.
	 * @return lo stato di abilitazione della notifica sonora in presenza di eventi.
	 */
	public Boolean beepOnEvents () {
		return Boolean.TRUE;
	}
	
	/**
	 * Ritorna il tipo di lista degli avanzamenti ().
	 * Per questa implementazione il valore è sempre {@link #DEFAULT_PROGRESSLISTTYPE}.
	 *
	 * @return il tipo di lista degli avanzamenti.
	 */
	public com.ost.timekeeper.view.ProgressListType getProgressListType () {
		return DEFAULT_PROGRESSLISTTYPE;
	}
	
	/**
	 * Ritorna il percorso della directory contenente i dati persistenti (JDO).
	 *
	 * @return il percorso della directory contenente i dati persistenti (JDO).
	 */
	public String getJDOStorageDirPath (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (UserResources.getUserHomeDirPath ()).append ("/").append (ApplicationData.getInstance ().getApplicationInternalName ());
		return sb.toString ();
	}
	
	/**
	 * Ritorna il nome dello storage JDO (i file contenenti i dati persistenti e gli indici).
	 *
	 * @return il nome dello storage JDO (i file contenenti i dati persistenti e gli indici).
	 */
	public String getJDOStorageName (){
		return ApplicationData.getInstance ().getApplicationInternalName ();
	}
	
	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public Integer getPlainTextLogBufferSize (){
		return DEFAULT_PLAINTEXTLOG_BUFFERSIZE;
	}
	
	/**
	 * Ritorna il nome dell'utente JDO. E' il nome dell'account di sistema per utente corrente.
	 *
	 * @return il nome dell'utente JDO.
	 */
	public String getJDOUserName (){
		return UserResources.getUserAccount ();
	}
	
	/**
	 * Ritorna l'ampiezza dell'albero dei nodi di avanzamento.
	 *
	 * @return l'ampiezza dell'albero dei nodi di avanzamento..
	 */
	public Integer getProgressItemTreeWidth (){
		return new Integer (-1);
	}
}

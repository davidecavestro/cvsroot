/*
 * DefaultSettings.java
 *
 * Created on 13 settembre 2004, 22.50
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.*;
import com.ost.timekeeper.ui.*;
import java.awt.*;
import javax.swing.*;

/**
 * Impostazioni di predefinite.
 *
 * @author  davide
 */
public final class DefaultSettings implements ApplicationSettings {
	
	/**
	 * header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = "DEFAULT SETTINGS";
	
	private static DefaultSettings _instance;
	/** 
	 * Ritorna l'istanza delle impostazioni predefinite.
	 */
	public static DefaultSettings getInstance () {
		if (_instance==null){
			_instance = new DefaultSettings ();
		}
		return _instance;
	}
	
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
	
	public String getLogDirPath () {
		return Application.getEnvironment ().getApplicationDirPath ()+"/logs/";
	}
	
	/**
	 * Ritorna il colore del desktop.
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
	
	public Rectangle getProgressItemInspectorBounds () {
		final int PROGRESSITEMINSPECTOR_PROSITION = 1;
		final ProgressItemInspectorFrame frame = ProgressItemInspectorFrame.getInstance ();
		return new Rectangle (
			DESKTOP_FRAMES_OFFSETX * PROGRESSITEMINSPECTOR_PROSITION, 
			DESKTOP_FRAMES_OFFSETY * PROGRESSITEMINSPECTOR_PROSITION, 
			frame.getWidth (), 
			frame.getHeight () 
		);
	}
	
	public Rectangle getProgressListFrameBounds () {
		final int PROGRESSLIST_PROSITION = 2;
		final ProgressListFrame frame = ProgressListFrame.getInstance ();
		return new Rectangle (
			DESKTOP_FRAMES_OFFSETX * PROGRESSLIST_PROSITION, 
			DESKTOP_FRAMES_OFFSETY * PROGRESSLIST_PROSITION, 
			frame.getWidth (), 
			frame.getHeight () 
		);
	}
	
	public Rectangle getProgressPeriodInspectorBounds () {
		final int PROGRESSINSPECTOR_PROSITION = 0;
		final ProgressInspectorFrame frame = ProgressInspectorFrame.getInstance ();
		return new Rectangle (
			DESKTOP_FRAMES_OFFSETX * PROGRESSINSPECTOR_PROSITION, 
			DESKTOP_FRAMES_OFFSETY * PROGRESSINSPECTOR_PROSITION, 
			frame.getWidth (), 
			frame.getHeight () 
		);
	}
	
}

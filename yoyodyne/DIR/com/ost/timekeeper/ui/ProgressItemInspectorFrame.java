/*
 * ProgressItemInspectorFrame.java
 *
 * Created on 4 dicembre 2004, 10.13
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.util.*;
import com.tomtessier.scrollabledesktop.*;
import java.awt.*;

/**
 * Il frame per la gestione del dettaglio nodo di avanzamento.
 *
 * @author  davide
 */
public final class ProgressItemInspectorFrame extends BaseInternalFrame {
	
	/**
	 * Istanza singleton.
	 */
	private static ProgressItemInspectorFrame _instance;
	
	/**
	 * Pannello di editazione dettaglio.
	 */
	private ProgressItemEditPanel progressItemdEditPanel;
	
	/** 
	 * Costruttore. 
	 */
	private ProgressItemInspectorFrame () {
		super (ResourceSupplier.getString (ResourceClass.UI, "controls", "progressitem.inspector"),
			true, //resizable
			false, //closable
			true, //maximizable
			true);//iconifiable
		initComponents ();
		
				
	}
	
	/**
	 * Ritorna l'istanza di questo inspector.
	 *
	 * @return l'istanza di questo inspector.
	 */	
	public static ProgressItemInspectorFrame getInstance (){
		if (_instance==null){
			_instance = new ProgressItemInspectorFrame();
		}
		return _instance;
	}
	
	/** 
	 * Inizializzazione delle componenti di questo frame.
	 */
	private void initComponents () {
		final Application app = Application.getInstance ();
		
		this.progressItemdEditPanel = new ProgressItemEditPanel ();
		app.addObserver (this.progressItemdEditPanel);
		
		this.setContentPane (progressItemdEditPanel);
		
		/*
		 * Imposta dimensione minima.
		 */
		this.setMinimumSize (new Dimension (250, 150));
		
		this.setFrameIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "progressitem-inspector-frame.gif"));
		
		pack ();
	}
	
	
	
}

/*
 * ProgressInspectorFrame.java
 *
 * Created on 4 dicembre 2004, 10.13
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.util.*;
import com.tomtessier.scrollabledesktop.*;
import java.awt.*;

/**
 * Il frame per la gestione del dettaglio avanzamento.
 *
 * @author  davide
 */
public final class ProgressInspectorFrame extends BaseInternalFrame {
	
	/**
	 * Istanza singleton.
	 */
	private static ProgressInspectorFrame _instance;
	
	/**
	 * Pannello di editazione dettaglio.
	 */
	private PeriodEditPanel periodEditPanel;
	
	/** 
	 * Costruttore. 
	 */
	private ProgressInspectorFrame () {
		super (ResourceSupplier.getString (ResourceClass.UI, "controls", "progress.inspector"),
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
	public static ProgressInspectorFrame getInstance (){
		if (_instance==null){
			_instance = new ProgressInspectorFrame();
		}
		return _instance;
	}
	
	/** 
	 * Inizializzazione delle componenti di questo frame.
	 */
	private void initComponents () {
		final Application app = Application.getInstance ();
		
		this.periodEditPanel = new PeriodEditPanel ();
		app.addObserver (periodEditPanel);
		
		this.setContentPane (periodEditPanel);
		
		/*
		 * Imposta dimensione minima.
		 */
		this.setMinimumSize (new Dimension (250, 150));
		
		this.setFrameIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "progress-inspector-frame.png"));
		
		pack ();
	}
	
	
	
}

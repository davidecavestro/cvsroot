/*
 * ProgressInspectorFrame.java
 *
 * Created on 4 dicembre 2004, 10.13
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.util.*;
//import com.tomtessier.scrollabledesktop.*;
import java.awt.*;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Il frame per la gestione del dettaglio avanzamento.
 *
 * @author  davide
 */
public final class ProgressInspectorFrame extends BaseInternalPanel {
	
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
		initComponents ();
		
				
	}
	
	/** Inizializza il frame con la posizione.
	 * 
	 */
	public final void init (final Desktop desktop, final int x, final int y){
		super.init (
			desktop,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "progress.inspector"), 
			ResourceSupplier.getImageIcon (ResourceClass.UI, "progress-inspector-frame.png"),
			false, 
			x, 
			y);

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
		
		this.setLayout (new GridBagLayout ());
		final GridBagConstraints c = new GridBagConstraints ();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);
		c.weightx=1.0;
		c.weighty=1.0;
		c.gridx=0;
		c.gridy=0;
		this.add (periodEditPanel, c);
		
		/*
		 * Imposta dimensione minima.
		 */
		this.setMinimumSize (new Dimension (250, 150));
		
//		this.setFrameIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "progress-inspector-frame.png"));
		
//		pack ();
	}
	
	
	
}

/*
 * ChartFrame.java
 *
 * Created on 4 marzo 2005, 8.23
 */

package com.ost.timekeeper.ui.chart;

import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import com.tomtessier.scrollabledesktop.BaseInternalFrame;
import java.util.Observer;
import javax.swing.JFrame;

/**
 * Finestra di gestione grafici interattivi.
 *
 * @author  davide
 */
public class ChartFrame extends BaseInternalFrame implements Observer{
	
	private static ChartFrame _instance;
	
	/**
	 * Ritorna listanza del singleton.
	 *
	 * @return l'unica istanza di questo tipo di finestra.
	 */	
	public static ChartFrame getInstance (){
		if (_instance==null){
			_instance = new ChartFrame ();
		}
		return _instance;
	}
	
	/** Costruttore. */
	private ChartFrame () {
		super (ResourceSupplier.getString (ResourceClass.UI, "controls", "chart.frame"),
			true, //resizable
			false, //closable
			true, //maximizable
			true);//iconifiable
		
		initComponents ();
		this.setFrameIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "chart-frame.gif"));
		
		pack ();
	}
	
	/**
	 * Inizializza i componentidi questo frame.
	 */
	private void initComponents (){
		this.getContentPane ().add (new RingChartPanel (ResourceSupplier.getString (ResourceClass.UI, "controls", "subtree.work.ring.chart")));
	}
	
	public void update (java.util.Observable o, Object arg) {
	}
	
}

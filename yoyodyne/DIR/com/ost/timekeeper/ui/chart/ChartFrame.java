/*
 * ChartFrame.java
 *
 * Created on 4 marzo 2005, 8.23
 */

package com.ost.timekeeper.ui.chart;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.ObserverCodes;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import com.tomtessier.scrollabledesktop.BaseInternalFrame;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Finestra di gestione grafici interattivi.
 *
 * @author  davide
 */
public class ChartFrame extends BaseInternalFrame implements Observer{
	
	private RingChartPanel _ringChartPanel;
	private BarGraphPanel _barGraphPanel;
	
	
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
		this.setFrameIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "charts-frame.png"));
		
		pack ();
		Application.getInstance ().addObserver (this);
	}
	
	/**
	 * Inizializza i componentidi questo frame.
	 */
	private void initComponents (){
		final JTabbedPane tabbedPane = new JTabbedPane (JTabbedPane.TOP);
		
		this._ringChartPanel = new RingChartPanel (ResourceSupplier.getString (ResourceClass.UI, "controls", "subtree.work.ring.chart"));
		tabbedPane.addTab (ResourceSupplier.getString (ResourceClass.UI, "controls", "ring.chart"), this._ringChartPanel);
		
		this._barGraphPanel = new BarGraphPanel (ResourceSupplier.getString (ResourceClass.UI, "controls", "subtree.work.bar.graph"));
		tabbedPane.addTab (ResourceSupplier.getString (ResourceClass.UI, "controls", "bar.graph"), this._barGraphPanel);
		
		this.getContentPane ().add (tabbedPane);
	}
	
	public void update (java.util.Observable o, Object arg) {
		if (arg!=null && arg.equals (ObserverCodes.PROJECTCHANGE)){
			if (Application.getOptions ().ringChartAutoload ()){
				final Project  project = Application.getInstance ().getProject ();
				ProgressItem root = null;
				if (project!=null){
					root = project.getRoot ();
				}
				this.reloadRingChart (root);
			}
			if (Application.getOptions ().barChartAutoload ()){
				final Project  project = Application.getInstance ().getProject ();
				ProgressItem root = null;
				if (project!=null){
					root = project.getRoot ();
				}
				this.reloadBarChart (root);
			}

		}
	}
	/**
	 * Reimposta la radice del grafico ad anello, e lo aggiorna.
	 * @param root la nuova radice.
	 */
	public final void reloadRingChart (final ProgressItem root) {
		this._ringChartPanel.reloadChart (root);
	}
	/**
	 * Reimposta la radice del grafico a barre, e lo aggiorna.
	 * @param root la nuova radice.
	 */
	public final void reloadBarChart (final ProgressItem root) {
		this._barGraphPanel.reloadChart (root);
	}
	
}

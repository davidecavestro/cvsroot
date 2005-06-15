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
import com.ost.timekeeper.ui.BaseInternalPanel;
import com.ost.timekeeper.ui.Desktop;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
//import com.tomtessier.scrollabledesktop.BaseInternalFrame;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;

/**
 * Finestra di gestione grafici interattivi.
 *
 * @author  davide
 */
public class ChartFrame extends BaseInternalPanel implements Observer{
	
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
		
		initComponents ();
		
		Application.getInstance ().addObserver (this);
	}
	
	/** Inizializza il frame con la posizione.
	 * 
	 */
	public final void init (final Desktop desktop, final int x, final int y){
		super.init (
			desktop,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "chart.frame"), 
			ResourceSupplier.getImageIcon (ResourceClass.UI, "charts-frame.png"),
			false, 
			x, 
			y);

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
		
		this.setLayout (new GridBagLayout ());
		final GridBagConstraints c = new GridBagConstraints ();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);
		c.weightx=1.0;
		c.weighty=1.0;
		c.gridx=0;
		c.gridy=0;
		this.add (tabbedPane, c);
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

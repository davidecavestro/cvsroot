/*
 * BaseInternalPanel.java
 *
 * Created on June 15, 2005, 11:39 AM
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.ui.Desktop;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Pannello interno al desktop.
 *
 * @author  davide
 */
public abstract class BaseInternalPanel extends JPanel {
	
	private JInternalFrame _frame;
	
	/**
	 * Costruttore.
	 */
	public BaseInternalPanel () {
	}
	
	/** Inizializza il frame con la posizione.
	 * 
	 */
	public final void init (final Desktop desktop, final String title, final ImageIcon icon, final boolean closeable, final int x, final int y){
		_frame = desktop.add (
			title, 
			icon,
			this, 
			false, x, y);
		_frame.pack ();
	}
	
	public JInternalFrame getFrame (){
		return _frame;
	}
	
}

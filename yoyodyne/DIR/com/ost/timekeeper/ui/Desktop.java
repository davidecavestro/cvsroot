/*
 * Desktop.java
 *
 * Created on 8 dicembre 2004, 11.42
 */

package com.ost.timekeeper.ui;

import com.tomtessier.scrollabledesktop.*;
import javax.swing.*;

/**
 * Il desktop dell'UI.
 *
 * @author  davide
 */
public final class Desktop extends JScrollableDesktopPane{
	
	/**
	 * L'istanza del desktop.
	 */
	private static Desktop _instance;
	
	/**
	 * Costruttore vuoto.
	 */
	private Desktop () {
	}
	
	/**
	 * Ritorna l'istanza del desktop.
	 * @return l'istanza del desktop.
	 */
	public static Desktop getInstance (){
		if (_instance==null){
			_instance=new Desktop ();
		}
		return _instance;
	}
	
	/**
	 * Attiva il frame specififcato.
	 * @param child il frame interno.
	 */	
	public void bringToTop (JInternalFrame child){
			try {
				child.setIcon (false);
			} catch (java.beans.PropertyVetoException pve) {}

//					inspector.getDesktopPane ().getDesktopManager().deiconifyFrame (inspector);
			child.getDesktopPane ().getDesktopManager().activateFrame (child);
//					Desktop.getInstance ().setSelectedFrame (inspector);
	}
}

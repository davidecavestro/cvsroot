/*
 * ProgressStopAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.util.*;

/**
 * Termina un avanzamento in esecuzione.
 *
 * @author  davide
 */
public final class ProgressStopAction extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressStopAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.stop"), ResourceSupplier.getImageIcon (ResourceClass.UI, "stop.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.stop.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		Application app = Application.getInstance ();
		ActionPool.getInstance ().getProgressStartAction ().setEnabled (true);
		app.getCurrentItem ().stopPeriod ();
		app.setCurrentItem (null);
		app.setChanged ();
		app.notifyObservers (ObserverCodes.ITEMPROGRESSINGPERIODCHANGE);
		app.setChanged ();
		app.notifyObservers (ObserverCodes.SELECTEDITEMCHANGE);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.CURRENTITEMCHANGE)){
				this.setEnabled (((Application)o).getCurrentItem ()!=null);
			}
		}
	}
}

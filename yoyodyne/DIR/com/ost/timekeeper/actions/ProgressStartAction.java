/*
 * ProgressStartAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.util.*;

/**
 * Avvia un nuovo avanzamento.
 *
 * @author  davide
 */
public final class ProgressStartAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressStartAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.start"), ResourceSupplier.getImageIcon (ResourceClass.UI, "start.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.start.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		Application app = Application.getInstance ();
		ProgressItem selectedItem = app.getSelectedItem ();
		selectedItem.startPeriod ();
		app.setCurrentItem (selectedItem);
		app.setChanged ();
		app.notifyObservers (ObserverCodes.ITEMPROGRESSINGPERIODCHANGE);
		app.setChanged ();
		app.notifyObservers (ObserverCodes.SELECTEDITEM);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && (arg.equals (ObserverCodes.CURRENTITEM)) || arg.equals (ObserverCodes.SELECTEDITEM)){
				Application app = (Application)o;
				this.setEnabled (app.getCurrentItem ()==null && app.getSelectedItem ()!=null);
			}
		}
	}
}

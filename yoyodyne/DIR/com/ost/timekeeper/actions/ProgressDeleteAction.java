/*
 * ProgressDeleteAction.java
 *
 * Created on 04 luglio 2004, 18.05
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Rimuove di un avanzamento {@link
 * com.ost.timekeeper.mode.Period} dai dati persistenti.
 *
 * @author  davide
 */
public final class ProgressDeleteAction extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressDeleteAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.deleteprogress"), ResourceSupplier.getImageIcon (ResourceClass.UI, "deleteprogress.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.deleteprogress.tooltip"));
		//		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		Application app = Application.getInstance ();
		Period selectedProgress = app.getSelectedProgress ();
		app.getPersistenceManager ().deletePersistent (selectedProgress);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.SELECTEDITEM)){
				this.setEnabled (((Application)o).getSelectedItem ()!=null);
			}
		}
	}
}

/*
 * StartProjectEdit.java
 *
 * Created on 30 maggio 2005, 23.28
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Attiva l'editor di progetto.
 *
 * @author  davide
 */
public final class StartProjectEdit extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public StartProjectEdit () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.editproject"), ResourceSupplier.getImageIcon (ResourceClass.UI, "editproject.png"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.editproject.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		ProjectInspectorFrame.getInstance ().show ();
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && (
				arg.equals (ObserverCodes.PROJECTCHANGE)
				)
			){
				this.setEnabled (((Application)o).getProject ()!=null);
			}
		}
	}
}

/*
 * NodeEditAction.java
 *
 * Created on 15 novembre 2004, 22.44
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Rende persistenti le modifiche ad un nodo di avanzamento.
 *
 * @author  davide
 */
public final class NodeEditAction extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public NodeEditAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.editnode"), ResourceSupplier.getImageIcon (ResourceClass.UI, "editnode.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.editnode.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		Application app = Application.getInstance ();
		app.flushData ();
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.SELECTEDITEMCHANGE)){
				this.setEnabled (((Application)o).getSelectedItem ()!=null);
			}
		}
	}
}

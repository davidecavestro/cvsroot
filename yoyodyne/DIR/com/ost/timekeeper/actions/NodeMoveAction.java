/*
 * NodeMoveAction.java
 *
 * Created on 30 dicembre 2004, 10.20
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Sposta un nodo di avanzamento.
 *
 * @author  davide
 * @todo implementazione parziale.
 */
public final class NodeMoveAction extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public NodeMoveAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.movenode"), ResourceSupplier.getImageIcon (ResourceClass.UI, "movenode.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.movenode.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		final Application app = Application.getInstance ();
		final ProgressItem movingNode = app.getSelectedItem ();
//		new MoveNode (movingNode, ).execute ();
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.SELECTEDITEMCHANGE)){
				this.setEnabled (((Application)o).getSelectedItem ()!=null);
			}
		}
	}
}

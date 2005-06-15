/*
 * StartNodeEdit.java
 *
 * Created on 06 marzo 2005, 9.41
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Attiva l'editor di nodo di avanzamento.
 *
 * @author  davide
 */
public final class StartNodeEdit extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public StartNodeEdit () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.editnode"), ResourceSupplier.getImageIcon (ResourceClass.UI, "editnode.png"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.editnode.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		Desktop.getInstance ().bringToTop (ProgressItemInspectorFrame.getInstance ().getFrame ());
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && (
				arg.equals (ObserverCodes.SELECTEDITEMCHANGE)
				|| arg.equals (ObserverCodes.PROJECTCHANGE)
				|| arg.equals (ObserverCodes.CURRENTITEMCHANGE)
				)
			){
				this.setEnabled (((Application)o).getSelectedItem ()!=null);
			}
		}
	}
}

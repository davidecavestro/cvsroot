/*
 * NodeCreateAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.help.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Crea un nuovo nodo della gerarchia di {@link
 * com.ost.timekeeper.model.ProgressItem}.
 *
 * @author davide
 */
public final class NodeCreateAction extends javax.swing.AbstractAction implements java.util.Observer {
	
	/**
	 * Costruttore vuoto.
	 */
	public NodeCreateAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.createnode"), ResourceSupplier.getImageIcon (ResourceClass.UI, "newnode.png"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.createnode.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		final String newNodeName = askForName ();
		if (newNodeName==null){
			/*
			 * Scelta non valida.
			 */
			return;
		}
		final Application app = Application.getInstance ();
		final ProgressItem parent = app.getSelectedItem ();
		new CreateNode (parent, new Attribute[]{new StringAttribute (CreateNode.NEWNODENAME, newNodeName)}, -1).execute ();
		//              selectedItem.insert(newNode, selectedItem.getChildCount());
	}
	
	/**
	 * Richiede all'utente il nome del nuovo nodo e lo ritorna.
	 *
	 * @return il nome del nuovo nodo.
	 */
	public String askForName (){
		return StringInputDialog.supplyString (Application.getInstance ().getMainForm (),
		ResourceSupplier.getString (ResourceClass.UI, "controls", "new_node"),
		ResourceSupplier.getString (ResourceClass.UI, "controls", "new_node.enter_name"),
		true,
		HelpResource.NEWNODEDIALOG);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.SELECTEDITEMCHANGE)){
				this.setEnabled (((Application)o).getSelectedItem ()!=null);
			}
		}
	}
}

/*
 * NodeUpdateAction.java
 *
 * Created on 21 novembre 2004, 11.54
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Implementa la logica di abilitazione del flusso che rende persistenti le 
 * modifiche allo stato del nodo di avanzamento selezionato.
 *
 * @author  davide
 */
public final class NodeUpdateAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public NodeUpdateAction () {
		super(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.saveprogressitem"));
		this.putValue(SHORT_DESCRIPTION, ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.saveprogressitem.tooltip"));
//		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	/**
	 * Porta in primo piano l'editor.
	 *
	 * @param e
	 */	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		Desktop.getInstance ().bringToTop (ProgressItemInspectorFrame.getInstance ());
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && 
				arg.equals (ObserverCodes.SELECTEDITEMCHANGE)
				|| arg.equals (ObserverCodes.PROJECTCHANGE)
				|| arg.equals (ObserverCodes.CURRENTITEMCHANGE)){
				/*
				 * Abilitato solo se c'è un nodo di avanzamento corrente.
				 */
				this.setEnabled(((Application)o).getSelectedItem()!=null);
			}
		}
	}
}

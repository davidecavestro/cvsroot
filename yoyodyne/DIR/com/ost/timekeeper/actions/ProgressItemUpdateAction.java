/*
 * ProgressItemUpdateAction.java
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
 * Rende persistente lo stato di un nodo di avanzamento.
 *
 * @author  davide
 */
public final class ProgressItemUpdateAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressItemUpdateAction () {
		super(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.saveprogressitem"));
		this.putValue(SHORT_DESCRIPTION, ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.saveprogressitem.tooltip"));
//		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		Application app = Application.getInstance();
		app.flushData();
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals(ObserverCodes.SELECTEDITEMCHANGE)){
				/*
				 * Abilitato solo se c'è un nodo di avanzamento corrente.
				 */
				this.setEnabled(((Application)o).getSelectedItem()!=null);
			}
		}
	}
}

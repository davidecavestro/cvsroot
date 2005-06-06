/*
 * ProjectUpdateAction.java
 *
 * Created on 30 maggio 2005, 23.36
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
 * modifiche allo stato del progettocorrente.
 *
 * @author  davide
 */
public final class ProjectUpdateAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProjectUpdateAction () {
		super(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.saveproject"));
		this.putValue(SHORT_DESCRIPTION, ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.saveproject.tooltip"));
//		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	/**
	 * Implementazione vuota.
	 *
	 * @param e
	 */	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		Desktop.getInstance ().bringToTop (ProgressItemInspectorFrame.getInstance ());
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && 
				arg.equals (ObserverCodes.PROJECTCHANGE)
				){
				/*
				 * Abilitato solo se c'è un progetto corrente.
				 */
				this.setEnabled(((Application)o).getProject()!=null);
			}
		}
	}
}

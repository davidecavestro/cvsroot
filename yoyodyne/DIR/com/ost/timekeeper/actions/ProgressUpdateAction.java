/*
 * ProgressUpdateAction.java
 *
 * Created on 06 marzo 2005, 10.39
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
 * modifiche allo stato del periodo di avanzamento selezionato.
 *
 * @author  davide
 */
public final class ProgressUpdateAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressUpdateAction () {
		super(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.saveprogress"));
		this.putValue(SHORT_DESCRIPTION, ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.saveprogress.tooltip"));
//		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	/**
	 * Implementazione vuota.
	 *
	 * @param e
	 */	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		Application app = Application.getInstance();
//		app.flushData();
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && 
				arg.equals (ObserverCodes.SELECTEDPROGRESSCHANGE)
				|| arg.equals (ObserverCodes.PROJECTCHANGE)
				|| arg.equals (ObserverCodes.CURRENTITEMCHANGE)){
				/*
				 * Abilitato solo se c'è un periodo di avanzamento selezionato.
				 */
				this.setEnabled(((Application)o).getSelectedProgress ()!=null);
			}
		}
	}
}

/*
 * ProgressDeleteAction.java
 *
 * Created on 04 luglio 2004, 18.05
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.DeleteProgress;
import com.ost.timekeeper.actions.commands.UpdateNode;
import com.ost.timekeeper.actions.commands.attributes.Attribute;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;
import javax.swing.JOptionPane;

/**
 * Rimuove di un avanzamento {@link
 * com.ost.timekeeper.model.Progress} dai dati persistenti.
 *
 * @author  davide
 */
public final class ProgressDeleteAction extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressDeleteAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.deleteprogress"), ResourceSupplier.getImageIcon (ResourceClass.UI, "deleteprogress.png"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.deleteprogress.tooltip"));
		//		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		final Application app = Application.getInstance ();
		if (
		JOptionPane.showConfirmDialog (
		app.getMainForm (), ResourceSupplier.getString (ResourceClass.UI, "controls", "delete.progress.confirm"))!=JOptionPane.OK_OPTION){
			return;
		}
		
		final ProgressItem node = app.getSelectedItem ();
		final Progress deleting = app.getSelectedProgress ();
		
//		node.deleteProgress (deleting);
//		new UpdateNode (node,  new Attribute [0]).execute ();
		new DeleteProgress (deleting).execute ();
//		app.setChanged ();
//		app.notifyObservers (ObserverCodes.SELECTEDPROGRESSCHANGE);
		app.setChanged ();
		app.notifyObservers (ObserverCodes.SELECTEDITEMCHANGE);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && (
				arg.equals (ObserverCodes.SELECTEDPROGRESSCHANGE)
				|| arg.equals (ObserverCodes.PROJECTCHANGE)
				|| arg.equals (ObserverCodes.CURRENTITEMCHANGE)
				)
			){
				final Progress selectedProgress = ((Application)o).getSelectedProgress ();
				final boolean enabled = selectedProgress!=null && !selectedProgress.isEndOpened ();
				
				this.setEnabled (enabled);
			}
		}
	}
}

/*
 * ProgressStartAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.AttributeMap;
import com.ost.timekeeper.actions.commands.attributes.StringAttribute;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.ProgressStartDialog;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.util.*;
import java.awt.event.ActionEvent;
import sun.security.krb5.internal.i;

/**
 * Avvia un nuovo avanzamento.
 *
 * @author  davide
 */
public final class ProgressStartAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressStartAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.start"), ResourceSupplier.getImageIcon (ResourceClass.UI, "ok.png"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.start.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		AttributeMap attributes = null;
		if (0!=(e.getModifiers() & ActionEvent.SHIFT_MASK)){
			/* pressione tasto SHIFT*/
			attributes = ProgressStartDialog.getData ();
			if (attributes == null){
				return;
			}
		}
		Application app = Application.getInstance ();
		ProgressItem selectedItem = app.getSelectedItem ();
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager ();
		final javax.jdo.Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			if (attributes!=null){
				final Progress progress = selectedItem.startPeriod (attributes.getAttribute (ProgressStartDialog.FROM).getValue ());
				final StringAttribute descr = attributes.getAttribute (ProgressStartDialog.DESCRIPTION);
				if (descr!=null){
					progress.setDescription (descr.getValue ());
				}
				final StringAttribute notes = attributes.getAttribute (ProgressStartDialog.NOTES);
				if (notes!=null){
					progress.setNotes (notes.getValue ());
				}
			} else {
				selectedItem.startPeriod ();
			}
			
			tx.commit ();
		} catch (final Throwable t){
			tx.rollback ();
			throw new NestedRuntimeException (t);
		}
		app.setCurrentItem (selectedItem);
		app.setChanged ();
		app.notifyObservers (ObserverCodes.ITEMPROGRESSINGPERIODCHANGE);
		app.setChanged ();
		app.notifyObservers (ObserverCodes.SELECTEDITEMCHANGE);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && (arg.equals (ObserverCodes.CURRENTITEMCHANGE)) || arg.equals (ObserverCodes.SELECTEDITEMCHANGE)){
				Application app = (Application)o;
				this.setEnabled (app.getCurrentItem ()==null && app.getSelectedItem ()!=null);
			}
		}
	}
}

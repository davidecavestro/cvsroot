/*
 * StartNewProgressCreation.java
 *
 * Created on 17 aprile 2005, 11.28
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.NewProgressDialog;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.util.*;

/**
 * Attiva l'editor per la crezione di un nuovo avanzamento.
 *
 * @author  davide
 */
public final class StartNewProgressCreation extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public StartNewProgressCreation () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.new.progress.terminated"), ResourceSupplier.getImageIcon (ResourceClass.UI, "ok.png"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.new.progress.terminated.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		Application app = Application.getInstance ();
		ProgressItem selectedItem = app.getSelectedItem ();

		NewProgressDialog.getInstance ().show ();
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

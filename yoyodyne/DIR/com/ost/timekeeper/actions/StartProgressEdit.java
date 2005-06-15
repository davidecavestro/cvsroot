/*
 * StartProgressEdit.java
 *
 * Created on 06 marzo 2005, 10.46
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Attiva l'editor di periodo di avanzamento.
 *
 * @author  davide
 */
public final class StartProgressEdit extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public StartProgressEdit () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.editprogress"), ResourceSupplier.getImageIcon (ResourceClass.UI, "editprogress.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.editprogress.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		Desktop.getInstance ().bringToTop (ProgressInspectorFrame.getInstance ().getFrame ());
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && (
				arg.equals (ObserverCodes.SELECTEDPROGRESSCHANGE)
				|| arg.equals (ObserverCodes.PROJECTCHANGE)
				|| arg.equals (ObserverCodes.CURRENTITEMCHANGE)
				)
			){
				this.setEnabled (((Application)o).getSelectedProgress ()!=null);
			}
		}
	}
}

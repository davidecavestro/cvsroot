/*
 * ShowDataStoreWIzardDialog.java
 *
 * Created on 27 aprile 2005, 00.19
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.report.ReportsFrame;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.wizard.datastore.DataStoreDirector;
import com.ost.timekeeper.util.*;

/**
 * Attiva la finestra di procedura guidata di configurazione del datastore.
 *
 * @author  davide
 */
public final class ShowDataStoreWIzardDialog extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ShowDataStoreWIzardDialog () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.datastore.wizard"), ResourceSupplier.getImageIcon (ResourceClass.UI, "wizard.png"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.datastore.wizard.tooltip"));
//		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (true);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		execute ();
	}
	
	public void execute (){
		new com.ost.timekeeper.wizard.Dialog (new DataStoreDirector (), ResourceSupplier.getString (ResourceClass.UI, "controls", "Datastore.configuration.wizard")).show ();
	}
	
	public void update (Observable o, Object arg) {
	}
}

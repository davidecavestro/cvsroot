/*
 * ShowReportsDialog.java
 *
 * Created on 08 aprile 2005, 00.54
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.report.ReportsFrame;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Attiva la finestra di lancio report.
 *
 * @author  davide
 */
public final class ShowReportsDialog extends javax.swing.AbstractAction implements java.util.Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ShowReportsDialog () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.reports"), ResourceSupplier.getImageIcon (ResourceClass.UI, "printer1.png"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.reports.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (true);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		ReportsFrame.getInstance ().show ();
	}
	
	public void update (Observable o, Object arg) {
	}
}

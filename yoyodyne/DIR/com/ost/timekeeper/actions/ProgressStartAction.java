/*
 * ProgressStartAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public class ProgressStartAction extends javax.swing.AbstractAction {
	
	/** Creates a new instance of ProgressStartAction */
	public ProgressStartAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.start"), ResourceSupplier.getImageIcon (ResourceClass.UI, "start.gif"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		this.setEnabled(false);
		Application app = Application.getInstance();
		ProgressItemNode selectedItem = app.getSelectedItem ();
		selectedItem.getProgressItem().startPeriod();
		Application.getInstance().setCurrentItem(selectedItem);
	}
	
}

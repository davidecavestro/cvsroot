/*
 * NodeCreateAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public class NodeCreateAction extends javax.swing.AbstractAction {
	
	/** Creates a new instance of NodeCreateAction */
	public NodeCreateAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.createnode"), ResourceSupplier.getImageIcon (ResourceClass.UI, "newnode.gif"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		ProgressItem newNode = new ProgressItem (askForName ());
		Application app = Application.getInstance();
		ProgressItem selectedItem = app.getSelectedItem ();
		app.getMainForm().getProgressTreeModel().insertNodeInto(newNode, selectedItem, selectedItem.childCount());
//		selectedItem.insert(newNode, selectedItem.getChildCount());
	}
	
	public String askForName (){
		return StringInputDialog.createDialog(Application.getInstance().getMainForm (), "Ask user", "Enter new node name", true);
	}
}

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

/**
 *
 * @author  davide
 */
public class NodeCreateAction extends javax.swing.AbstractAction {
	
	/** Creates a new instance of NodeCreateAction */
	public NodeCreateAction() {
		super (java.util.ResourceBundle.getBundle("com/ost/timekeeper/ui/bundle/menu").getString("actions.createnode"), new javax.swing.ImageIcon(ProgressStartAction.class.getResource("/com/ost/timekeeper/ui/images/newnode.gif")));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		ProgressItemNode newNode = new ProgressItemNode(new ProgressItem (askForName ()));
		Application app = Application.getInstance();
		ProgressItemNode selectedItem = app.getSelectedItem ();
		selectedItem.insert(newNode, selectedItem.getChildCount());
	}
	
	public String askForName (){
		return StringInputDialog.createDialog(Application.getInstance().getMainForm (), "Ask user", "Enter new node name", true);
	}
}

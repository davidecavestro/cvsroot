/*
 * NodeDeleteAction.java
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
public class NodeDeleteAction extends javax.swing.AbstractAction {
	
	/** Creates a new instance of NodeDeleteAction */
	public NodeDeleteAction() {
		super (java.util.ResourceBundle.getBundle("com/ost/timekeeper/ui/bundle/menu").getString("actions.deletenode"), new javax.swing.ImageIcon(ProgressStartAction.class.getResource("/com/ost/timekeeper/ui/images/deletenode.gif")));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		Application app = Application.getInstance();
		ProgressItemNode selectedItem = app.getSelectedItem ();
		ProgressItemNode parent = (ProgressItemNode)selectedItem.getParent();
		int pos = parent.getIndex(selectedItem);
		parent.remove(selectedItem);
		ProgressItemNode newSelection = null;
		if (parent.getChildCount()!=0){
			if (parent.getChildCount()>pos){
				newSelection = (ProgressItemNode)parent.getChildAt(pos);
			} else {
				newSelection = (ProgressItemNode)parent.getChildAt(parent.getChildCount()-1);
			}
		} else {
			newSelection = parent;
		}
		app.setSelectedItem(newSelection);
	}
	
	public String askForName (){
		return StringInputDialog.createDialog(Application.getInstance().getMainForm (), "Ask user", "Enter new node name", true);
	}
}

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
import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public class NodeDeleteAction extends javax.swing.AbstractAction {
	
	/** Creates a new instance of NodeDeleteAction */
	public NodeDeleteAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.deletenode"), ResourceSupplier.getImageIcon (ResourceClass.UI, "deletenode.gif"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		Application app = Application.getInstance();
		ProgressItemNode selectedItem = app.getSelectedItem ();
		ProgressItemNode parent = (ProgressItemNode)selectedItem.getParent();
		if (parent==null){
			//non si rimuove la radice;
			return;
		}
		app.getMainForm().getProgressTreeModel ().removeNodeFromParent(selectedItem);
		int pos = parent.getIndex(selectedItem);
//		parent.remove(selectedItem);
		
		//determina nuova selezione
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

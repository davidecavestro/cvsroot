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
 * Implementa la creazione di un nuovo nodo della gerarchia di {@link
 * com.ost.timekeeper.mode.ProgressItem}.
 * 
 * @author davide
 */
public class NodeCreateAction extends javax.swing.AbstractAction implements java.util.Observer {
	
	/**
	 * Costruttore vuoto.
	 */
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
//              selectedItem.insert(newNode, selectedItem.getChildCount());
	}
	
	public String askForName (){
		return StringInputDialog.createDialog(Application.getInstance().getMainForm (), "Ask user", "Enter new node name", true);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals ("selecteditem")){
				this.setEnabled(((Application)o).getSelectedItem()!=null);
			}
		}
	}
}

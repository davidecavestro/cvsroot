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
public class ProgressStartAction extends javax.swing.AbstractAction implements Observer{
	
	/** Creates a new instance of ProgressStartAction */
	public ProgressStartAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.start"), ResourceSupplier.getImageIcon (ResourceClass.UI, "start.gif"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		this.setEnabled(false);
		Application app = Application.getInstance();
		ProgressItem selectedItem = app.getSelectedItem ();
		selectedItem.startPeriod();
		Application.getInstance().setCurrentItem(selectedItem);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && (arg.equals ("currentitem")) || arg.equals ("selecteditem")){
				Application app = (Application)o;
				this.setEnabled (app.getCurrentItem()==null && app.getSelectedItem()!=null);
			}
		}
	}
}

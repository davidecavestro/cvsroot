/*
 * ProgressStopAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public class ProgressStopAction extends javax.swing.AbstractAction implements java.util.Observer{
	
	/** Creates a new instance of ProgressStopAction */
	public ProgressStopAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.stop"), ResourceSupplier.getImageIcon (ResourceClass.UI, "stop.gif"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		this.setEnabled(false);
		Application.getInstance().getProgressStartAction ().setEnabled (true);
		Application.getInstance().getCurrentItem ().stopPeriod();
		Application.getInstance().setCurrentItem (null);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals ("currentitem")){
				this.setEnabled(((Application)o).getCurrentItem()!=null);
			}
		}
	}
}

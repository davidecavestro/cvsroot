/*
 * ProgressStopAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import com.ost.timekeeper.*;

/**
 *
 * @author  davide
 */
public class ProgressStopAction extends javax.swing.AbstractAction {
	
	/** Creates a new instance of ProgressStopAction */
	public ProgressStopAction() {
		super (java.util.ResourceBundle.getBundle("com/ost/timekeeper/ui/bundle/menu").getString("actions.stop"), new javax.swing.ImageIcon(ProgressStartAction.class.getResource("/com/ost/timekeeper/ui/images/stop.gif")));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		this.setEnabled(false);
		Application.getInstance().getProgressStartAction ().setEnabled (true);
		Application.getInstance().getCurrentItem ().getProgressItem().stopPeriod();
		Application.getInstance().setCurrentItem (null);
	}
	
}

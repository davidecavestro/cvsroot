/*
 * ProjectOpenAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.util.*;

import javax.jdo.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Azione di apertura di un progetto (con scelta utente).
 *
 * @author  davide
 */
public final class ProjectOpenAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProjectOpenAction() {
		super(ResourceSupplier.getString(ResourceClass.UI, "menu", "file.open"), ResourceSupplier.getImageIcon(ResourceClass.UI, "openproject.gif"));
		this.putValue(SHORT_DESCRIPTION, ResourceSupplier.getString(ResourceClass.UI, "menu", "file.open.tooltip"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		Application app = Application.getInstance();
		app.setProject(
		ProjectSelectDialog.chooseProject(app.getMainForm(),
		ResourceSupplier.getString(ResourceClass.UI, "controls", "openproject"),
		ResourceSupplier.getString(ResourceClass.UI, "controls", "selectprojecttoopen"),
		true));
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals(ObserverCodes.PROJECT)){
				this.setEnabled(true);
			}
		}
	}
}

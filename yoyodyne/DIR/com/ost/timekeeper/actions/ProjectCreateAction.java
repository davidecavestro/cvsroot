/*
 * ProjectCreateAction.java
 *
 * Created on 15 maggio2004, 9.18
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Azione di creazione di un nuovo progetto
 * @author  davide
 */
public class ProjectCreateAction extends javax.swing.AbstractAction implements Observer{
	
	/** Crea una nuova istanza di ProjectCreateAction */
	public ProjectCreateAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "file.new"), ResourceSupplier.getImageIcon (ResourceClass.UI, "createproject.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "file.new.tooltip"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		String projectName = askUserForProjectName ();
		execute (projectName);
	}
	
	public void execute (String projectName){
		Application app = Application.getInstance();
		app.setProject (new Project (projectName, new ProgressItem (projectName)));
		app.getPersistenceManager().makePersistent(app.getProject());
//		app.getMainForm().getProgressTreeModel()
	}
	
	public String askUserForProjectName (){
		return StringInputDialog.createDialog(Application.getInstance().getMainForm (), "Ask user", "Enter new project name", true);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals ("project")){
				this.setEnabled(true);
			}
		}
	}
}

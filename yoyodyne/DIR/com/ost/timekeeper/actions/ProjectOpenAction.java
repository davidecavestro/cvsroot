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
 *
 * @author  davide
 */
public class ProjectOpenAction extends javax.swing.AbstractAction implements Observer{
	
	/** Creates a new instance of ProjectOpenAction */
	public ProjectOpenAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "file.open"), ResourceSupplier.getImageIcon (ResourceClass.UI, "openproject.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "file.open.tooltip"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		String projectName = askForName ();
		Application app = Application.getInstance();
		app.setProject (
			ProjectSelectDialog.createDialog(app.getMainForm(), 
				ResourceSupplier.getString (ResourceClass.UI, "controls", "openproject"), 
				ResourceSupplier.getString (ResourceClass.UI, "controls", "selectprojecttoopen"), 
				true));
//		Extent extent = app.getPersistenceManager().getExtent(Project.class, true);
//		Project currentProject = null;
//		for (Iterator it = extent.iterator();it.hasNext();){
//			Project project = (Project)it.next ();
//			if (project.getName().equals(projectName)){
//				currentProject = project;
//				break;
//			}
//		}
//		app.setProject (currentProject);

	}
	
	public String askForName (){
		return StringInputDialog.createDialog(Application.getInstance().getMainForm (), "Ask user", "Enter project name", true);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.PROJECT)){
				this.setEnabled(true);
			}
		}
	}
}

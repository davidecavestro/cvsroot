/*
 * ProjectDeleteAction.java
 *
 * Created on 15 maggio 2004, 9.30
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Azione di rimozione di un progetto
 * @author  davide
 */
public class ProjectDeleteAction extends javax.swing.AbstractAction implements Observer{
	
	/** Crea una nuova istanza di ProjectDeleteAction */
	public ProjectDeleteAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.deleteproject"), ResourceSupplier.getImageIcon (ResourceClass.UI, "deleteproject.gif"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		Application app = Application.getInstance();
		Collection toDelete = new ArrayList ();
		
		Project project = app.getProject();
		if (project.jdoIsPersistent()){
			toDelete.add (project);
			ProgressItem root = project.getRoot();
			toDelete.add (root);
			for (Iterator it=root.getSubtreeProgresses().iterator();it.hasNext();){
				Period progress = (Period)it.next ();
				if (progress.jdoIsPersistent()){
					toDelete.add (progress);
				}
			}

			for (Iterator it=root.getDescendants().iterator();it.hasNext();){
				ProgressItem node = (ProgressItem)it.next ();
				if (node.jdoIsPersistent()){
					toDelete.add (node);
				}
			}
		}
		app.setProject (null);
		app.getPersistenceManager().deletePersistentAll(toDelete);
	}
	
	public String askForName (){
		return StringInputDialog.createDialog(Application.getInstance().getMainForm (), "Ask user", "Enter new node name", true);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals ("project")){
				this.setEnabled(((Application)o).getProject()!=null);
			}
		}
	}
}

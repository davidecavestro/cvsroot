/*
 * ProjectCreateAction.java
 *
 * Created on 15 maggio2004, 9.18
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.help.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;
import javax.jdo.*;

/**
 * Crea un nuovo progetto.
 *
 * @author  davide
 */
public final class ProjectCreateAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProjectCreateAction() {
		super(ResourceSupplier.getString(ResourceClass.UI, "menu", "project.new"), ResourceSupplier.getImageIcon(ResourceClass.UI, "createproject.png"));
		this.putValue(SHORT_DESCRIPTION, ResourceSupplier.getString(ResourceClass.UI, "menu", "project.new.tooltip"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		final String projectName = askUserForProjectName();
		
		if (projectName!=null){
			/*
			 * Scelta valida.
			 */
			execute(projectName);
		}
	}
	
	public void execute(String projectName){
		if (projectName==null || projectName.trim().length()==0){
			//progetto senza nome non ammesso
			return;
		}
		Application app = Application.getInstance();
		app.setProject(new Project(projectName, new ProgressItem(projectName)));
		Project newProject = app.getProject();
		if (newProject!=null){
			//nuovo progetto
			//rende persistente nuovo progetto
			final PersistenceManager pm = app.getPersistenceManager();
			final Transaction tx = pm.currentTransaction();
			tx.begin();
			try {
				pm.makePersistent(newProject);
				tx.commit();			
			} catch (final Throwable t){
				tx.rollback ();
				throw new NestedRuntimeException (t);
			}
		}
	}
	
	/**
	 * Richiede all'utente il nome del nuovo progetto e lo ritorna.
	 *
	 * @return il nome del nuovo progetto.
	 */
	public static String askUserForProjectName(){
		return StringInputDialog.supplyString(Application.getInstance().getMainForm(),
		ResourceSupplier.getString(ResourceClass.UI, "controls", "new_project"),
		ResourceSupplier.getString(ResourceClass.UI, "controls", "new_project.enter_name"),
		true,
		HelpResource.NEWPROJECTDIALOG);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals(ObserverCodes.PROJECTCHANGE)){
				this.setEnabled(true);
			}
		}
	}
}

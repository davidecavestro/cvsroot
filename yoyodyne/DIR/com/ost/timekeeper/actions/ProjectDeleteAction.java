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
 * Azione di rimozione di un progetto.
 *
 * @author  davide
 */
public final class ProjectDeleteAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProjectDeleteAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.deleteproject"), ResourceSupplier.getImageIcon (ResourceClass.UI, "deleteproject.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "file.deleteproject.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		final Application app = Application.getInstance ();
		Collection toDelete = new ArrayList ();
		
		Project project = app.getProject ();
		if (project.jdoIsPersistent ()){
			/*
			 * Progetto persistente.
			 */
			toDelete.add (project);
			ProgressItem root = project.getRoot ();
			toDelete.add (root);
			for (Iterator it=root.getSubtreeProgresses ().iterator ();it.hasNext ();){
				Period progress = (Period)it.next ();
				if (progress.jdoIsPersistent ()){
					/*
					 * Avanzamento persistente.
					 */
					toDelete.add (progress);
				}
			}
			
			for (Iterator it=root.getDescendants ().iterator ();it.hasNext ();){
				ProgressItem node = (ProgressItem)it.next ();
				if (node.jdoIsPersistent ()){
					/*
					 * Nodo persistente.
					 */
					toDelete.add (node);
				}
			}
		}
		app.setProject (null);
		/*
		 * Rimuove persistenza oggetti determinati.
		 */
		app.getPersistenceManager ().deletePersistentAll (toDelete);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.PROJECTCHANGE)){
				this.setEnabled (((Application)o).getProject ()!=null);
			}
		}
	}
}

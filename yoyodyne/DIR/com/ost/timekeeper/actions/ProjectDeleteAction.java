/*
 * ProjectDeleteAction.java
 *
 * Created on 15 maggio 2004, 9.30
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.DeleteProject;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;
import javax.swing.JOptionPane;

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
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "project.delete")/*, ResourceSupplier.getImageIcon (ResourceClass.UI, "deleteproject.gif")*/);
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "project.delete.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		final Application app = Application.getInstance ();
		if (
		JOptionPane.showConfirmDialog (
		app.getMainForm (), ResourceSupplier.getString (ResourceClass.UI, "controls", "delete.project.confirm"))!=JOptionPane.OK_OPTION){
			return;
		}
		new DeleteProject (app.getProject ()).execute ();
		app.setProject (null);
		
		Application.getLogger ().debug ("Project erased");
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.PROJECTCHANGE)){
				this.setEnabled (((Application)o).getProject ()!=null);
			}
		}
	}
}

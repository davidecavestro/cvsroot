/*
 * ProjectXMLImportAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.io.*;
import java.util.*;
import javax.swing.*;

import org.exolab.castor.mapping.*;
import org.exolab.castor.xml.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public class ProjectXMLImportAction extends javax.swing.AbstractAction implements Observer{
	
	private final JFileChooser chooser = new JFileChooser();
	
	/** Creates a new instance of ProjectXMLImportAction */
	public ProjectXMLImportAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.xmlimportproject"), ResourceSupplier.getImageIcon (ResourceClass.UI, "xmlimportproject.gif"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
//		ProgressItem newNode = new ProgressItem (askForName ());
		Application app = Application.getInstance();
		// Load Mapping
		Mapping mapping = new Mapping();
		try{
			mapping.loadMapping("modelmapping.xml");
			
			int returnVal = chooser.showOpenDialog(app.getMainForm());
			if(returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			// Create a Reader to the file to unmarshal from
			Reader reader = new FileReader(chooser.getSelectedFile().getName());
			
			// Create a new Unmarshaller
			Unmarshaller unmarshaller = new Unmarshaller(Project.class);
			unmarshaller.setMapping(mapping);
			// Unmarshal the project object
			Project project = (Project)unmarshaller.unmarshal(reader);
			System.out.println ("progetto importato: "+project);
			app.setProject (project);
		} catch (Exception ex) {
			throw new NestedRuntimeException(ex);
		}
	}
	
//	public String askForName (){
//		return StringInputDialog.createDialog(Application.getInstance().getMainForm (), "Ask user", "Enter new node name", true);
//	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals ("project")){
				this.setEnabled(((Application)o).getProject()!=null);
			}
		}
	}
}

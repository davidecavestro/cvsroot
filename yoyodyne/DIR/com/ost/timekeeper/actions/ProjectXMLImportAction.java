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
 * Importa un progetto da una fonte XML.
 *
 * @author  davide
 */
public final class ProjectXMLImportAction extends javax.swing.AbstractAction implements Observer{
	
	private final JFileChooser chooser = new JFileChooser();
	
	/** 
	 * Costruttore vuoto. 
	 */
	public ProjectXMLImportAction() {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.xmlimportproject"), ResourceSupplier.getImageIcon (ResourceClass.UI, "xmlimportproject.gif"));
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "file.xmlimportproject.tooltip"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		final Application app = Application.getInstance();
		// Load Mapping
		final Mapping mapping = new Mapping();
		try{
			mapping.loadMapping("modelmapping.xml");
			
			final int returnVal = chooser.showOpenDialog(app.getMainForm());
			
			try {
				final SwingWorker worker = new SwingWorker() {
					public Object construct() {
						app.setProcessing (true);
						try {
							if(returnVal != JFileChooser.APPROVE_OPTION) {
								return null;
							}
							try {
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
						} finally {
							app.setProcessing (false);
						}
						JOptionPane.showMessageDialog(Application.getInstance ().getMainForm (), ResourceSupplier.getString(ResourceClass.UI, "controls", "xmlimport.successful"));
						return null;
							
					}
				};
				worker.start();
			} finally {
//				app.setProcessing (false);
			}
			
			
		} catch (Exception ex) {
			throw new NestedRuntimeException(ex);
		}
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.PROJECTCHANGE)){
				//indipendente dal progetto corrente
				this.setEnabled(true);
			}
		}
	}
}

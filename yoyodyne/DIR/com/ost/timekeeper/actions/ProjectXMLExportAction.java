/*
 * ProjectSaveAction.java
 *
 * Created on 24 aprile 2004, 12.06
 */

package com.ost.timekeeper.actions;

import java.io.*;
import java.util.*;
import javax.swing.*;

//import org.exolab.castor.mapping.*;
//import org.exolab.castor.xml.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.ExportProjectToXML;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.ui.support.CustomFileFilter;
import com.ost.timekeeper.util.*;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.xml.sax.*;

/**
 * Esporta i dati relativi ad un progetto in formato XML.
 * Utilitzza il file <TT>modelmapping.xml</TT> per la definizione della
 * mappatura da utilizzare durante la creazione del XML.
 *
 * @author  davide
 */
public final class ProjectXMLExportAction extends javax.swing.AbstractAction implements Observer {
	
	/**
	 * Componente grafica per la selezione del file di esportazione.
	 */
	private final JFileChooser chooser = new JFileChooser();
	
	/**
	 * Costruttore vuoto.
	 */
	public ProjectXMLExportAction() {
		super(ResourceSupplier.getString(ResourceClass.UI, "menu", "project.xmlexportproject"), ResourceSupplier.getImageIcon(ResourceClass.UI, "export.png"));
		this.putValue(SHORT_DESCRIPTION, ResourceSupplier.getString(ResourceClass.UI, "menu", "project.xmlexportproject.tooltip"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
		final CustomFileFilter filter = new CustomFileFilter (
			new String []{FileUtils.xml},
			new String []{ResourceSupplier.getString (ResourceClass.UI, "controls", "file.type.xml")}
		);
		chooser.addChoosableFileFilter (filter);
		
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		final Application app = Application.getInstance();
		

	
	
	
		
		// Load Mapping
//		final Mapping mapping = new Mapping();
		try{
//			mapping.loadMapping(this.getClass ().getResource ("dataiomap.xml"));
			
			final int returnVal = chooser.showSaveDialog(app.getMainForm());
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					app.setProcessing (true);
					try{
						if(returnVal != JFileChooser.APPROVE_OPTION) {
							return null;
						}
						try {
							final org.jdom.Document data = new org.jdom.Document ();
							new ExportProjectToXML (app.getProject (), data).execute ();
							
							final XMLOutputter xo = new XMLOutputter ();
							xo.output (data, new FileOutputStream (chooser.getSelectedFile()));
							
//							// Create a Reader to the file to unmarshal from
//							Writer writer = new FileWriter(chooser.getSelectedFile());
//
//							// Create a new Marshaller
//							Marshaller marshaller = new Marshaller(writer);
//							marshaller.setMapping(mapping);
//							// Marshal the project object
//							marshaller.marshal(app.getProject(), writer);
						} catch (Exception ex) {
							System.out.println (ExceptionUtils.getStackTrace (ex));
							throw new NestedRuntimeException(ex);
						}
					} finally {
						app.setProcessing (false);
					}
					
					JOptionPane.showMessageDialog(Application.getInstance ().getMainForm (), ResourceSupplier.getString(ResourceClass.UI, "controls", "xmlexport.successful"));
					return null;
				}
			};
			worker.start();
				
		} catch (Exception ex) {
			Application.getLogger ().error ( "Error exporting project. ", ex);
			throw new NestedRuntimeException(ex);
		}
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals(ObserverCodes.PROJECTCHANGE)){
				this.setEnabled(((Application)o).getProject()!=null);
			}
		}
	}
}

/*
 * ProjectSaveAction.java
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
 * Esporta i dati relativi ad un progetto in formato XML.
 * Utilitzza il file <TT>modelmapping.xml</TT> per la definizione della
 * mappatura da utilizzare durante la creazione del XML.
 *
 * @author  davide
 */
public final class ProjectXMLExportAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Componente grafica per la selezione del file di esportazione.
	 */
	private final JFileChooser chooser = new JFileChooser();
	
	/**
	 * Costruttore vuoto.
	 */
	public ProjectXMLExportAction() {
		super(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.xmlexportproject"), ResourceSupplier.getImageIcon(ResourceClass.UI, "xmlexportproject.gif"));
		this.putValue(SHORT_DESCRIPTION, ResourceSupplier.getString(ResourceClass.UI, "menu", "file.xmlexportproject.tooltip"));
		this.putValue(ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled(false);
	}
	
	public void actionPerformed(java.awt.event.ActionEvent e) {
		Application app = Application.getInstance();
		// Load Mapping
		Mapping mapping = new Mapping();
		try{
			mapping.loadMapping("modelmapping.xml");
			
			int returnVal = chooser.showSaveDialog(app.getMainForm());
			app.setProcessing (true);
			try{
				if(returnVal != JFileChooser.APPROVE_OPTION) {
					return;
				}
				// Create a Reader to the file to unmarshal from
				Writer writer = new FileWriter(chooser.getSelectedFile().getName());

				// Create a new Marshaller
				Marshaller marshaller = new Marshaller(writer);
				marshaller.setMapping(mapping);
				// Marshal the project object
				marshaller.marshal(app.getProject(), writer);
			} finally {
				app.setProcessing (false);
			}
		} catch (Exception ex) {
			System.out.println(ExceptionUtils.getStackStrace(ex));
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

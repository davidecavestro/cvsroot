/*
 * ProjectSelectDialog.java
 *
 * Created on 06 giugno 2004, 20.15
 */

package com.ost.timekeeper.ui;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.help.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.*;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Finestra di selezione di un progetto.
 *
 * @author  davide
 */
public final class ProjectSelectDialog extends javax.swing.JDialog {
	
	private String titleText;
	private String labelText;
	private List projects;
	/**
	 * Conferma della selezione effettuata.
	 */
	private boolean confirmed;
	
	/**
	 * Costruttore con parametri.
	 *
	 * @param parent la finestra padre.
	 * @param title il titolo di questa finestra
	 * @param label l'etichetta contenete la richiesta di selezione.
	 * @param modal stato modale.
	 * @param projects l'insieme di progetti da presentare per la scelta.
	 */
	public ProjectSelectDialog (java.awt.Frame parent, String title, String label, boolean modal, List projects) {
		super (parent, modal);
		this.projects=projects;
		this.titleText = title;
		this.labelText = label;
		initComponents ();
		/* inizializza help contestuale */
		javax.help.CSH.setHelpIDString (this, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PROJECTSELECTDIALOG ));
		
		/*
		 * centra
		 */
		this.setLocationRelativeTo (null);
	}
	
	/**
	 * Inizializzazione componenti.
	 */
	private void initComponents () {
		infoLabel = new TopBorderPane ();
		projectList = new javax.swing.JList ();
		buttonPanel = new javax.swing.JPanel ();
		confirmButton = new javax.swing.JButton ();
		cancelButton = new javax.swing.JButton ();
		directHelpButton = new DirectHelpButton ();
		
		this.setTitle (this.titleText);
		
		final JPanel mainPanel = new JPanel (new java.awt.GridBagLayout ());
		
		addWindowListener (new java.awt.event.WindowAdapter () {
			public void windowClosing (java.awt.event.WindowEvent evt) {
				confirmed=false;
				closeDialog (evt);
			}
		});
		
		this.infoLabel.setText (this.labelText);
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
		
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		mainPanel.add (infoLabel, c);
		
		c.gridwidth = 1;
		//Garantisce che il campo abbia sempre il focus iniziale
		addComponentListener (new ComponentAdapter () {
			public void componentShown (ComponentEvent ce) {
				projectList.requestFocusInWindow ();
			}
		});
		
		projectList.setModel (new ProjectListModel (this.projects));
		projectList.addMouseListener (new MouseAdapter (){
			public void mouseClicked (MouseEvent e) {
				if (e.getClickCount ()>1){
					confirmButtonActionPerformed ( null );
				}
			}
		});
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		mainPanel.add(new JScrollPane (projectList), c);
		
		confirmButton.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "controls.button.confirm"));
		confirmButton.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				confirmButtonActionPerformed (evt);
			}
		});
		
		buttonPanel.add (confirmButton);
		
		cancelButton.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "controls.button.cancel"));
		final Action cancelAction = new javax.swing.AbstractAction ("cancel"){
			public void actionPerformed(ActionEvent e) {
				onCancel ();
			}
		};
		cancelButton.setAction (cancelAction);
		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		cancelButton.getActionMap().put("cancel", cancelAction);
		buttonPanel.add (cancelButton);
		
		buttonPanel.add (directHelpButton);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		mainPanel.add (buttonPanel, c);
		
		mainPanel.setPreferredSize (new java.awt.Dimension (340, 220));
		
		getContentPane ().add (mainPanel);
		
		getRootPane ().setDefaultButton (confirmButton);
		pack ();
		
	}
	
	private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt) {
		onCancel ();
	}
	
	private void onCancel () {
		this.projectList.setSelectedValue (null, true);
		this.hide ();
	}
	
	private void confirmButtonActionPerformed (java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		confirmed=true;
		this.hide ();
	}
	
	/**
	 * Chiude questa finestra.
	 */
	private void closeDialog (java.awt.event.WindowEvent evt) {
		setVisible (false);
		dispose ();
	}
	
	// Variables declaration
	private javax.swing.JButton confirmButton;
	private javax.swing.JButton cancelButton;
	private TopBorderPane infoLabel;
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JList projectList;
	private DirectHelpButton directHelpButton;
	
	// End of variables declaration
	
	/**
	 * Richiede all'utente di scegliere un progetto tra quelli disponibili, e lo ritorna.
	 *
	 * @param parent la finestra padre.
	 * @param title il titolo.
	 * @param label l'etichetta di scelta.
	 * @param modal stato modale.
	 * @return ritorna il progetto scelto dall'utente.
	 */
	public static Project chooseProject (java.awt.Frame parent, String title, String label, boolean modal){
		ProjectSelectDialog dialog = new ProjectSelectDialog (parent, title, label, modal, Application.getInstance ().getAvailableProjects ());
		dialog.show ();
		return dialog.confirmed?(Project)dialog.projectList.getSelectedValue ():null;
	}
	
	/**
	 * Modello dati per la lista di selezione.
	 */
	private final class ProjectListModel implements javax.swing.ListModel{
		
		private List projects;
		
		public ProjectListModel (List projects){
			this.projects = projects;
		}
		public void addListDataListener (javax.swing.event.ListDataListener l) {
		}
		
		public Object getElementAt (int index) {
			return this.projects.get (index);
		}
		
		public int getSize () {
			return this.projects.size ();
		}
		
		public void removeListDataListener (javax.swing.event.ListDataListener l) {
		}
		
	}
}

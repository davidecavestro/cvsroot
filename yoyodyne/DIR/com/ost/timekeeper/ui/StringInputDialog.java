/*
 * StringInputDialog.java
 *
 * Created on 18 aprile 2004, 14.33
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.help.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Richiede all'utente l'immissione di una gererica stringa.
 *
 * @author  davide
 */
public final class StringInputDialog extends javax.swing.JDialog {
	
	private String titleText;
	private String labelText;
	
	/**
	 * Stato conferma scelta.
	 */
	private boolean confirmed = false;
	
	/**
	 * Costruttore con parametri.
	 */
	public StringInputDialog (java.awt.Frame parent, String title, String label, boolean modal, HelpResource helpResource) {
		super (parent, modal);
		/* inizializza help contestuale */
		javax.help.CSH.setHelpIDString (this, HelpResourcesResolver.getInstance ().resolveHelpID (helpResource ));
		
		this.titleText = title;
		this.labelText = label;
		initComponents ();
		postInitComponents ();
		
		/*
		 * Centra sullo schermo.
		 */
		this.setLocationRelativeTo (null);
	}
	
	/**
	 * Inizializza le componenti di questa finestra..
	 */
	private void initComponents () {
		final JPanel mainPanel = new javax.swing.JPanel (new java.awt.GridBagLayout ());
		dataEditor = new javax.swing.JTextField ();
		buttonPanel = new javax.swing.JPanel ();
		confirmButon = new javax.swing.JButton ();
		cancelButton = new javax.swing.JButton ();
		
		getContentPane ().setLayout (new BorderLayout ());
		
		addWindowListener (new java.awt.event.WindowAdapter () {
			public void windowClosing (java.awt.event.WindowEvent evt) {
				closeDialog (evt);
			}
		});
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
		
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		mainPanel.add (new TopBorderPane (this.labelText), c);
		
		dataEditor.setText ("");
		
		//Garantisce che il campo abbia sempre il focus iniziale
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                dataEditor.requestFocusInWindow();
            }
        });		
		
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		mainPanel.add (dataEditor, c);
		
		/* etichetta filler */
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		mainPanel.add (new JLabel (), c);
		
		getContentPane ().add (mainPanel, java.awt.BorderLayout.CENTER);
		
		confirmButon.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "controls.button.confirm"));
		confirmButon.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				confirmButtonActionPerformed (evt);
			}
		});
		
		buttonPanel.add (confirmButon);
		
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
		
		final DirectHelpButton dhb = new DirectHelpButton ();
		dhb.setRolloverEnabled (true);
		buttonPanel.add (dhb);
		
		getContentPane ().add (buttonPanel, java.awt.BorderLayout.SOUTH);
		
		mainPanel.setFocusCycleRoot (true);
		
		getRootPane ().setDefaultButton (confirmButon);
		this.setTitle (this.titleText);
		pack ();
	}
	
	/**
	 * Post inizializzazione componenti.
	 */
	private void postInitComponents () {
	}
	
	private void jButtonCancelActionPerformed (java.awt.event.ActionEvent evt) {
		onCancel ();
	}
	
	private void onCancel () {
		this.hide ();
	}
	
	private void confirmButtonActionPerformed (java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		this.confirmed = true;
		this.hide ();
	}
	
	/**
	 * Chiude questa finestra.
	 */
	private void closeDialog (java.awt.event.WindowEvent evt) {
		setVisible (false);
		dispose ();
	}
	
	/**
	 * Ritorna lo stato di conferma della scelta da parte dell'utente.
	 *
	 * @return lo stato di conferma della scelta da parte dell'utente.
	 */
	public boolean isConfirmed (){
		return this.confirmed;
	}
	
	private javax.swing.JButton confirmButon;
	private javax.swing.JButton cancelButton;
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JTextField dataEditor;
	
	/**
	 * Ritorna una stringa dopo averne richiesto l'immissione all'utente.
	 *
	 * @param parent la finestra padre.
	 * @param title il titolo della finestra.
	 * @param label la richiesta.
	 * @param modal stato modale.
	 * @return  una stringa dopo averne immessa all'utente.
	 */
	public static String supplyString (java.awt.Frame parent, String title, String label, boolean modal, HelpResource helpResource){
		final StringInputDialog dialog = new StringInputDialog (parent, title, label, modal, helpResource);
		dialog.pack ();
		dialog.show ();
		return dialog.isConfirmed ()?dialog.dataEditor.getText ():null;
	}
}

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
		descriptionPanel = new javax.swing.JPanel ();
		inputPanel = new javax.swing.JPanel ();
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
		
		descriptionPanel.setLayout (new java.awt.BorderLayout ());
		descriptionPanel.add (new JLabel (this.labelText), BorderLayout.WEST);
		final DirectHelpButton dhb = new DirectHelpButton ();
		dhb.setRolloverEnabled (true);
		descriptionPanel.add (dhb, BorderLayout.EAST);
		
		descriptionPanel.setBorder (new EmptyBorder (3,5,3,5));
		getContentPane ().add (descriptionPanel, java.awt.BorderLayout.NORTH);
		
		inputPanel.setLayout (new java.awt.BorderLayout ());
		
		dataEditor.setText ("");
		
		
		//Garantisce che il campo abbia sempre il focus iniziale
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                dataEditor.requestFocusInWindow();
            }
        });		
		
		inputPanel.setBorder (new EmptyBorder (3,5,3,5));
		inputPanel.add (dataEditor, java.awt.BorderLayout.CENTER);
		getContentPane ().add (inputPanel, java.awt.BorderLayout.CENTER);
		
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
		
		getContentPane ().add (buttonPanel, java.awt.BorderLayout.SOUTH);
		
		inputPanel.setFocusCycleRoot (true);
		
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
	private javax.swing.JPanel descriptionPanel;
	private javax.swing.JPanel inputPanel;
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

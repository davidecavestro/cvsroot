/*
 * StringInputDialog.java
 *
 * Created on 18 aprile 2004, 14.33
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.util.*;

/**
 * Richiede all'utente l'immissione di una gererica stringa.
 *
 * @author  davide
 */
public final class StringInputDialog extends javax.swing.JDialog {
	
	private String titleText;
	private String labelText;
	
	/**
	 * Costruttore con parametri.
	 */
	public StringInputDialog(java.awt.Frame parent, String title, String label, boolean modal) {
		super(parent, modal);
		initComponents();
		this.titleText = title;
		this.labelText = label;
		postInitComponents();
	}
	
	/**
	 * Inizializza le componenti di questa finestra..
	 */
	private void initComponents() {
		jPanelDescription = new javax.swing.JPanel();
		jTextFieldInput = new javax.swing.JTextField();
		jPanelData = new javax.swing.JPanel();
		jButtonConfirm = new javax.swing.JButton();
		jButtonCancel = new javax.swing.JButton();
		
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});
		
		jPanelDescription.setLayout(new java.awt.BorderLayout());
		
		jTextFieldInput.setText("");
		jPanelDescription.add(jTextFieldInput, java.awt.BorderLayout.CENTER);
		
		getContentPane().add(jPanelDescription, java.awt.BorderLayout.CENTER);
		
		jButtonConfirm.setText(ResourceSupplier.getString(ResourceClass.UI, "global", "controls.button.confirm"));
		jButtonConfirm.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonConfirmActionPerformed(evt);
			}
		});
		
		jPanelData.add(jButtonConfirm);
		
		jButtonCancel.setText(ResourceSupplier.getString(ResourceClass.UI, "global", "controls.button.cancel"));
		jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCancelActionPerformed(evt);
			}
		});
		
		jPanelData.add(jButtonCancel);
		
		getContentPane().add(jPanelData, java.awt.BorderLayout.SOUTH);
		
		pack();
	}
	
	/**
	 * Post inizializzazione componenti.
	 */
	private void postInitComponents() {
		jPanelDescription.setBorder(new javax.swing.border.TitledBorder(this.labelText));
		this.setTitle(this.titleText);
		this.pack();
	}
	
	private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		this.hide();
	}
	
	private void jButtonConfirmActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		this.hide();
	}
	
	/** 
	 * Chiude questa finestra.
	 */
	private void closeDialog(java.awt.event.WindowEvent evt) {
		setVisible(false);
		dispose();
	}
	
	// Variables declaration - do not modify
	private javax.swing.JButton jButtonConfirm;
	private javax.swing.JButton jButtonCancel;
	private javax.swing.JPanel jPanelDescription;
	private javax.swing.JPanel jPanelData;
	private javax.swing.JTextField jTextFieldInput;
	// End of variables declaration
	
	/**
	 * Ritorna una stringa dopo averne richiesto l'immissione all'utente.
	 *
	 * @param parent la finestra padre.
	 * @param title il titolo della finestra.
	 * @param label la richiesta.
	 * @param modal stato modale.
	 * @return  una stringa dopo averne immessa all'utente.
	 */	
	public static String supplyString(java.awt.Frame parent, String title, String label, boolean modal){
		StringInputDialog dialog = new StringInputDialog(parent, title, label, modal);
		dialog.pack();
		dialog.show();
		return dialog.jTextFieldInput.getText();
	}
}

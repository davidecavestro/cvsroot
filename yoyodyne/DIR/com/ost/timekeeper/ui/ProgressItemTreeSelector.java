/*
 * ProgressItemTreeSelector.java
 *
 * Created on 04 marzo 2005, 8.37
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.help.*;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 * Richiede all'utente la selezione di un nodo dell'albero.
 *
 * @author  davide
 */
public final class ProgressItemTreeSelector extends javax.swing.JDialog {
	
	private String titleText;
	private String labelText;
	
	private TopBorderPane _labelPane;
	
	/**
	 * Stato conferma scelta.
	 */
	private boolean confirmed = false;
	
	/**
	 * la selezione corrente.
	 */
	private ProgressItem _selection;
	/**
	 * Costruttore con parametri.
	 */
	public ProgressItemTreeSelector (java.awt.Frame parent, String title, String label, boolean modal, HelpResource helpResource) {
		super (parent, modal);
		
		setHelpResource (helpResource);
		
		this.titleText = title;
		this.labelText = label;
		initComponents ();
		postInitComponents ();
		
		/*
		 * Centra sullo schermo.
		 */
		this.setLocationRelativeTo (null);
	}

	private void setHelpResource (final HelpResource helpResource){
		/* inizializza help contestuale */
		javax.help.CSH.setHelpIDString (this, HelpResourcesResolver.getInstance ().resolveHelpID (helpResource ));		
	}
	
	private void setLabelText (final String label){
		this.labelText = label;
		this._labelPane = new TopBorderPane (this.labelText);
	}

	public void setTitle (final String title){
		this.titleText = title;
		super.setTitle (title);
	}
	
	/**
	 * Inizializza le componenti di questa finestra..
	 */
	private void initComponents () {
		final JPanel mainPanel = new javax.swing.JPanel (new BorderLayout ());
		tree = new javax.swing.JTree (Application.getInstance ().getMainForm ().getProgressTreeModel ());
		
		tree.addTreeSelectionListener (new TreeSelectionListener (){
			public void valueChanged (TreeSelectionEvent e){
				if (e.getNewLeadSelectionPath () == null) {
					//gestione caso cancellazione elemento selezionato
					return;
				}
				TreePath thePath = e.getPath ();
				_selection = (ProgressItem)thePath.getLastPathComponent ();
			}
		});
		
		buttonPanel = new javax.swing.JPanel ();
		confirmButton = new javax.swing.JButton ();
		cancelButton = new javax.swing.JButton ();
		
		getContentPane ().setLayout (new BorderLayout ());
		
		addWindowListener (new java.awt.event.WindowAdapter () {
			public void windowClosing (java.awt.event.WindowEvent evt) {
				closeDialog (evt);
			}
		});
		
//		final GridBagConstraints c = new GridBagConstraints();
//		c.fill = GridBagConstraints.BOTH;
//		c.anchor = GridBagConstraints.FIRST_LINE_START;
//		c.insets = new Insets (3, 10, 3, 10);
//		
//		c.weightx = 1.0;
//		c.gridx = 0;
//		c.gridy = 0;
//		c.gridwidth = 1;
		this._labelPane = new TopBorderPane (this.labelText);
		mainPanel.add (_labelPane, BorderLayout.NORTH);
		
//		dataEditor.setText ("");
		
		//Garantisce che il campo abbia sempre il focus iniziale
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                tree.requestFocusInWindow();
            }
        });		
		
//		c.weightx = 1.0;
//		c.gridx = 0;
//		c.gridy = 1;
//		c.gridwidth = 1;
		mainPanel.add (new JScrollPane (tree), BorderLayout.CENTER);
		
		getContentPane ().add (mainPanel, java.awt.BorderLayout.CENTER);
		
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
		
		final DirectHelpButton dhb = new DirectHelpButton ();
		dhb.setRolloverEnabled (true);
		buttonPanel.add (dhb);
		
		getContentPane ().add (buttonPanel, java.awt.BorderLayout.SOUTH);
		
		mainPanel.setFocusCycleRoot (true);
		
		getRootPane ().setDefaultButton (confirmButton);
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
	
	private javax.swing.JButton confirmButton;
	private javax.swing.JButton cancelButton;
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JTree tree;
	
	
	private static ProgressItemTreeSelector _dialogInstance;
	
	/**
	 * Ritorna una stringa dopo averne richiesto l'immissione all'utente.
	 *
	 * @param parent la finestra padre.
	 * @param title il titolo della finestra.
	 * @param label la richiesta.
	 * @param modal stato modale.
	 * @return  una stringa dopo averne immessa all'utente.
	 */
	public static synchronized UserChoice supplyProgressItem (String title, String label, boolean modal, HelpResource helpResource){
		if (_dialogInstance==null){
			_dialogInstance = new ProgressItemTreeSelector (Application.getInstance ().getMainForm (), title, label, modal, helpResource);
		}
		_dialogInstance.confirmed = false;
		_dialogInstance.setTitle (title);
		_dialogInstance.setLabelText (label);
		_dialogInstance.setModal (modal);
		_dialogInstance.setHelpResource (helpResource);
		
		_dialogInstance.pack ();
		_dialogInstance.show ();
		return new UserChoice (_dialogInstance.isConfirmed (), _dialogInstance._selection);
	}
	
	public final static class UserChoice {
		private boolean _confirmed;
		private ProgressItem _selection;
		
		public UserChoice (final boolean confirmed, final ProgressItem selection){
			this._confirmed=confirmed;
			this._selection=selection;
		}
		public boolean isConfirmed (){return this._confirmed;}
		public ProgressItem getChoice (){return this._selection;}
	}
}

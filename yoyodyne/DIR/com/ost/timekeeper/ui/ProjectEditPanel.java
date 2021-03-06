/*
 * ProjectEditPanel.java
 *
 * Created on 30 maggio 2005, 23.15
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.actions.commands.UpdateProject;
import com.ost.timekeeper.actions.commands.attributes.Attribute;
import com.ost.timekeeper.actions.commands.attributes.StringAttribute;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 * Pannello di modifica progetto.
 *
 * @author  davide
 * @todo fix abilitazione pulsante conferma (ora � abilitato di default, si riabilita solo alla pressione dei tasti sui campi).
 */
public final class ProjectEditPanel extends javax.swing.JPanel implements Observer, KeyListener {
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Pannello di editazione.
	 */
	final javax.swing.JPanel editPanel = new javax.swing.JPanel ();
	
	/**
	 * Etichetta supporto editazione NOME.
	 */
	final javax.swing.JLabel nameLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione DESCRIZIONE.
	 */
	final javax.swing.JLabel descriptionLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione NOTE.
	 */
	final javax.swing.JLabel notesLabel = new javax.swing.JLabel ();
	
	/**
	 * Componente editazione NOME.
	 */
	final javax.swing.JTextField nameEditor = new javax.swing.JTextField ();
	
	/**
	 * COmponente editazione DESCRIZIONE.
	 */
	final javax.swing.JTextArea descriptionEditor = new javax.swing.JTextArea (2, 20);
	
	/**
	 * Componente editazione NOTE.
	 */
	final javax.swing.JTextArea notesEditor = new javax.swing.JTextArea (3, 20);
	
	/**
	 * Pulsantiera.
	 */
	final javax.swing.JPanel buttonPanel = new javax.swing.JPanel ();
	/**
	 * Pulsante di conferma.
	 */
	final javax.swing.JButton confirmButton = new javax.swing.JButton ();
	
	/**
	 * Pulsante di annullamento modifiche.
	 */
	final javax.swing.JButton resetButton = new javax.swing.JButton ();
	/**********************************************
	 * FINE dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Stato modifica ai dati trattati da questo pannello.
	 */
	private boolean _dataChanged = false;
	
	/**
	 * Costruttore.
	 *
	 */
	public ProjectEditPanel () {
		/*
		 * Inizializzazione componenti grafiche.
		 */
		initComponents ();
		resynch ();
	}
	
	/**
	 * Inizializzaizone componenti.
	 */
	private void initComponents () {
		
		this.setLayout (new java.awt.BorderLayout ());
		
		/*
		 * Configurazione pulsante CONFERMA.
		 */
		confirmButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent e){
				pushData ();
			}
		});
//		confirmButton.setAction (ActionPool.getInstance ().getProjectUpdateAction ());
		confirmButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "confirm"));
		
		/*
		 * Configurazione pulsante ANNULLA.
		 */
		resetButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "reset"));
		resetButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent e){
				resynch ();
			}
		});
		
		/*
		 * Configurazione pulsantiera.
		 */
		buttonPanel.setLayout (new java.awt.FlowLayout ());
		
		
		/*
		 * Inserimento pulsante CONFERMA.
		 */
		buttonPanel.add (confirmButton);
		
		/*
		 * Inserimento pulsante ANNULLA.
		 */
		buttonPanel.add (resetButton);
		
		
		/*
		 * Configurazione pannello editazione.
		 */
		editPanel.setLayout (new java.awt.GridBagLayout ());
		
		/*
		 * Configurazione editazione NOME.
		 */
		nameLabel.setLabelFor (nameEditor);
		nameLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "name"));
		nameEditor.setMinimumSize (new Dimension (120, 20));
		nameEditor.addKeyListener (this);
		
		/*
		 * Configurazione editazione DESCRIZIONE.
		 */
		descriptionLabel.setLabelFor (descriptionEditor);
		descriptionLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "description"));
//		descriptionEditor.setMinimumSize (new Dimension (120, 20));
		descriptionEditor.addKeyListener (this);
		
		/*
		 * Configurazione editazione NOTE.
		 */
		notesLabel.setLabelFor (notesEditor);
		notesLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "notes"));
//		notesEditor.setMinimumSize (new Dimension (120, 20));
		notesEditor.addKeyListener (this);
		
		final GridBagConstraints c = new GridBagConstraints ();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		editPanel.add (new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "controls", "main")), c);
		
		c.gridwidth = 1;
		
		/*
		 * Inserimento editazione NOME.
		 */
		c.gridx = 0;
		c.gridy = 1;
		editPanel.add (nameLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		editPanel.add (nameEditor, c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		editPanel.add (new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "controls", "secondary")), c);
		
		c.gridwidth = 1;
		
		/*
		 * Inserimento editazione DESCRIZIONE.
		 */
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		editPanel.add (descriptionLabel, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 3;
		editPanel.add (new JScrollPane (descriptionEditor,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), c);
		
		/*
		 * Inserimento editazione NOTE.
		 */
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 4;
		editPanel.add (notesLabel, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 4;
		editPanel.add (new JScrollPane (notesEditor,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), c);
		
		
		//		SpringUtilities.makeCompactGrid(editPanel,
		//                                4, 2, //rows, cols
		//                                6, 6,        //initX, initY
		//                                6, 6);       //xPad, yPad
		
		{
			final JPanel controlsPanel = new JPanel (new BorderLayout ());
			controlsPanel .add (new JScrollPane (editPanel), BorderLayout.CENTER);
			controlsPanel.setBorder (new BevelBorder (BevelBorder.RAISED));

			/*
			 * Inserimento pannello editazione.
			 */
			this.add (controlsPanel, java.awt.BorderLayout.CENTER);
		}
		
		/*
		 * Inserimento pulsantiera.
		 */
		this.add (buttonPanel, java.awt.BorderLayout.SOUTH);
		
		/*
		 * Inizializza flag a "NON MODIFICATO"
		 */
		this.setDataChanged (false);
	}
	
	/**
	 * Risincronizza il pannello di editazione con i dati del modello.
	 */
	private final void resynch (){
		/*
		 * resetta flag modifica
		 */
		this.setDataChanged (false);
		final Project editSubject = Application.getInstance ().getProject ();
		final boolean validItem = editSubject!=null;
		final boolean editingEnabled = validItem;
		
		this.editPanel.setEnabled (validItem);
		
		String code = "";
		String name = "";
		String description = "";
		String notes = "";
		if (validItem){
			/*
			 * C'� un progetto corrente.
			 * Inizializzazione campi editazione a valori progetto.
			 */
			name = editSubject.getName ();
			description = editSubject.getDescription ();
			notes = editSubject.getNotes ();
		}
		this.nameEditor.setText (name);
		this.descriptionEditor.setText (description);
		this.notesEditor.setText (notes);
	}
	
	/**
	 * Aggiorna il progetto con i dati delle componenti di editazione.
	 */
	private final void pushData (){
		final Project editSubject = Application.getInstance ().getProject ();
		if (editSubject!=null){
			/*
			 * C'� un progetto corrente.
			 * Inizializzazione campi editazione a valori progetto.
			 */
			
			new UpdateProject (editSubject, 
				new Attribute[]{
					new StringAttribute (UpdateProject.NAME, this.nameEditor.getText ()),
					new StringAttribute (UpdateProject.DESCRIPTION, this.descriptionEditor.getText ()),
					new StringAttribute (UpdateProject.NOTES, this.notesEditor.getText ())
				}).execute ();
				
//			final Application app = Application.getInstance ();
//			final javax.jdo.PersistenceManager pm = app.getPersistenceManager ();
//			final javax.jdo.Transaction tx = pm.currentTransaction ();
//			tx.begin ();
//			try {
//				editSubject.setCode (this.codeEditor.getText ());
//				editSubject.setName (this.nameEditor.getText ());
//				editSubject.setDescription (this.descriptionEditor.getText () );
//				editSubject.setNotes (this.notesEditor.getText () );
//				tx.commit ();
//			} catch (final Throwable t){
//				tx.rollback ();
//				throw new com.ost.timekeeper.util.NestedRuntimeException (t);
//			}
			
			this.setDataChanged (false);
		}
	}
	
	/**
	 * Aggiorna questo pannello a seguito di una notifica da parte di un oggetto sosservato.
	 * Nello specifico la notifica di interesse � quella relativa al cambiamento del progetto
	 * di avanzamento correntemente selezionata.
	 * A seguito di tale notifica i campi di editaszione cvengono aggiornati.
	 *
	 * @param o la sorgente della notifica.
	 * @param arg argomento della notifica.
	 */
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.PROJECTCHANGE)){
				this.resynch ();
			}
		}
	}
	
	/**
	 * Imposta il flag di "modifica avvenuta" per questo pannello.
	 */
	private void setDataChanged (boolean changed){
		this._dataChanged = changed;
		this.confirmButton.setEnabled (this._dataChanged && ActionPool.getInstance ().getProjectUpdateAction ().isEnabled ());
		this.resetButton.setEnabled (this._dataChanged);
	}
	
	/**
	 * Implementazione vuota.
	 */
	public void keyPressed (KeyEvent e) {
	}
	
	/**
	 * Implementazione vuota.
	 */
	public void keyReleased (KeyEvent e) {
	}
	
	/**
	 * Notifica modifica.
	 */
	public void keyTyped (KeyEvent e) {
		/*
		 * Notifica modifica dati.
		 */
		setDataChanged (true);
	}
	
}

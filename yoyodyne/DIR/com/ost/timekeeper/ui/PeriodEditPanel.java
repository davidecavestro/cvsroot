/*
 * ProgressItemEditPanel.java
 *
 * Created on 06 dicembre 2004, 21.59
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Pannello di modifica periodo di avanzamento.
 *
 * @author  davide
 * @todo fix abilitazione pulsante conferma (ora � abilitato di default, si riabilita solo alla pressione dei tasti sui campi).
 */
public final class PeriodEditPanel extends javax.swing.JPanel implements Observer, KeyListener {
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Pannello di editazione.
	 */
	private final javax.swing.JPanel editPanel = new javax.swing.JPanel ();
	
	/**
	 * Etichetta supporto editazione data/ora INIZIO.
	 */
	private final javax.swing.JLabel fromLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione data/ora FINE.
	 */
	private final javax.swing.JLabel toLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione DESCRIZIONE.
	 */
	private final javax.swing.JLabel descriptionLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione NOTE.
	 */
	private final javax.swing.JLabel notesLabel = new javax.swing.JLabel ();
	
	/**
	 * Componente editazione INIZIO.
	 */
	private final javax.swing.JFormattedTextField  fromEditor = new javax.swing.JFormattedTextField  (DateFormat.getDateTimeInstance (DateFormat.DATE_FIELD, DateFormat.MEDIUM));
	
	/**
	 * Componente editazione FINE.
	 */
	private final javax.swing.JFormattedTextField toEditor = new javax.swing.JFormattedTextField  (DateFormat.getDateTimeInstance (DateFormat.DATE_FIELD, DateFormat.MEDIUM));
	
	/**
	 * COmponente editazione DESCRIZIONE.
	 */
	private final javax.swing.JTextArea descriptionEditor = new javax.swing.JTextArea (3, 20);
	
	/**
	 * Componente editazione NOTE.
	 */
	private final javax.swing.JTextArea notesEditor = new javax.swing.JTextArea (5, 20);
	
	/**
	 * Pulsantiera.
	 */
	private final javax.swing.JPanel buttonPanel = new javax.swing.JPanel ();
	/**
	 * Pulsante di conferma.
	 */
	private final javax.swing.JButton confirmButton = new javax.swing.JButton ();
	
	/**
	 * Pulsante di annullamento modifiche.
	 */
	private final javax.swing.JButton resetButton = new javax.swing.JButton ();
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
	public PeriodEditPanel () {
		/*
		 * Inizializzazione componenti grafiche.
		 */
		initComponents ();
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
		confirmButton.setAction (ActionPool.getInstance ().getProgressItemUpdateAction ());
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
		 * Configurazione editazione campo INIZIO.
		 */
		fromLabel.setLabelFor (fromEditor);
		fromLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "from"));
//		fromEditor.setMinimumSize (new Dimension (120, 20));
		fromEditor.addKeyListener (this);
		
		/*
		 * Configurazione editazione campo FINE.
		 */
		toLabel.setLabelFor (toEditor);
		toLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "to"));
//		toEditor.setMinimumSize (new Dimension (120, 20));
		toEditor.addKeyListener (this);
		
		/*
		 * Configurazione editazione DESCRIZIONE.
		 */
		descriptionLabel.setLabelFor (descriptionEditor);
		descriptionLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "description"));
		descriptionEditor.setMinimumSize (new Dimension (120, 20));
		descriptionEditor.addKeyListener (this);
		
		/*
		 * Configurazione editazione NOTE.
		 */
		notesLabel.setLabelFor (notesEditor);
		notesLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "notes"));
		notesEditor.setMinimumSize (new Dimension (120, 20));
		notesEditor.addKeyListener (this);
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);
			
		/*
		 * Inserimento editazione INIZIO.
		 */
		c.gridx = 0;
		c.gridy = 0;
		editPanel.add (fromLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		editPanel.add (fromEditor, c);
		
		/*
		 * Inserimento editazione FINE.
		 */
		c.gridx = 0;
		c.gridy = 1;
		editPanel.add (toLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		editPanel.add (toEditor, c);
		
		/*
		 * Inserimento editazione DESCRIZIONE.
		 */
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		editPanel.add (descriptionLabel, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 2;
		editPanel.add (new JScrollPane (descriptionEditor,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), c);
		
		/*
		 * Inserimento editazione NOTE.
		 */
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		editPanel.add (notesLabel, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 3;
		editPanel.add (new JScrollPane (notesEditor,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), c);
		
		
		
//		SpringUtilities.makeCompactGrid(editPanel,
//                                4, 2, //rows, cols
//                                6, 6,        //initX, initY
//                                6, 6);       //xPad, yPad
		
		/*
		 * Inserimento pannello editazione.
		 */
		this.add (new JScrollPane (editPanel), java.awt.BorderLayout.CENTER);
		
		/*
		 * Inserimento pulsantiera.
		 */
		this.add (buttonPanel, java.awt.BorderLayout.SOUTH);
		
		/*
		 * Imposta dimensione minima.
		 */
		this.setMinimumSize (new Dimension (250, 150));
	}
	
	/**
	 * Risincronizza il pannello di editazione con i dati del modello.
	 */
	private final void resynch (){
		final Application app = Application.getInstance ();
		
		final Period editSubject = app.getSelectedProgress ();
		final boolean validItem = editSubject!=null;
		final boolean editingEnabled = validItem;
		
		this.editPanel.setEnabled (validItem);
		
		Date from = null;
		Date to = null;
		String description = "";
		String notes = "";
		if (validItem){
			/* 
			 * C'� un nodo selezionato.
			 * Inizializzazione campi editazione a valori nodo di avanzamento selezionato.
			 */
			from = editSubject.getFrom ();
			to = editSubject.getTo ();
			description = editSubject.getDescription ();
			notes = editSubject.getNotes ();
		}
		this.fromEditor.setValue (from);
		this.toEditor.setValue (to);
		this.descriptionEditor.setText (description);
		this.notesEditor.setText (notes);
		
		/*
		 * resetta flag modifica
		 */
		this.setDataChanged (false);
		
	}
	
	/**
	 * Aggiorna il nodo di avanzamento selezionato con i dati delle componenti di editazione.
	 */
	private final void pushData (){
		final Application app = Application.getInstance ();
		
		final Period editSubject = app.getSelectedProgress ();
		if (editSubject!=null){
			/* 
			 * C'� un nodo selezionato.
			 * Inizializzazione campi editazione a valori nodo di avanzamento selezionato.
			 */
			editSubject.setFrom ((Date)this.fromEditor.getValue ());
			editSubject.setTo ((Date)this.toEditor.getValue());
			editSubject.setDescription (this.descriptionEditor.getText () );
			editSubject.setNotes (this.notesEditor.getText () );
			
			this.setDataChanged (false);
		}
	}
	
	/**
	 * Aggiorna questo pannello a seguito di una notifica da parte di un oggetto sosservato.
	 * Nello specifico la notifica di interesse � quella relativa al cambiamento del nodo
	 * di avanzamento correntemente selezionata.
	 * A seguito di tale notifica i campi di editaszione cvengono aggiornati.
	 *
	 * @param o la sorgente della notifica.
	 * @param arg argomento della notifica.
	 */	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals(ObserverCodes.SELECTEDPROGRESSCHANGE)){
				this.resynch ();
			}
		}
	}
	
	/**
	 * Imposta il flag di "modifica avvenuta" per questo pannello.
	 */
	private void setDataChanged (boolean changed){
		this._dataChanged = changed;
		this.confirmButton.setEnabled (this._dataChanged);
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
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

/**
 * Pannello di modifica periodo di avanzamento.
 *
 * @author  davide
 */
public final class PeriodEditPanel extends javax.swing.JPanel implements Observer {
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Pannello di editazione.
	 */
	final javax.swing.JPanel editPanel = new javax.swing.JPanel ();
	
	/**
	 * Etichetta supporto editazione data/ora INIZIO.
	 */
	final javax.swing.JLabel fromLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione data/ora FINE.
	 */
	final javax.swing.JLabel toLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione DESCRIZIONE.
	 */
	final javax.swing.JLabel descriptionLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione NOTE.
	 */
	final javax.swing.JLabel notesLabel = new javax.swing.JLabel ();
	
	/**
	 * Componente editazione INIZIO.
	 */
	final javax.swing.JFormattedTextField  fromEditor = new javax.swing.JFormattedTextField  (DateFormat.getDateTimeInstance (DateFormat.DATE_FIELD, DateFormat.MEDIUM));
	
	/**
	 * Componente editazione FINE.
	 */
	final javax.swing.JFormattedTextField toEditor = new javax.swing.JFormattedTextField  (DateFormat.getDateTimeInstance (DateFormat.DATE_FIELD, DateFormat.MEDIUM));
	
	/**
	 * COmponente editazione DESCRIZIONE.
	 */
	final javax.swing.JTextArea descriptionEditor = new javax.swing.JTextArea (3, 20);
	
	/**
	 * Componente editazione NOTE.
	 */
	final javax.swing.JTextArea notesEditor = new javax.swing.JTextArea (5, 20);
	
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
		
		//		JScrollPane scrollPane = new JScrollPane (jTextAreaDescription,
		//		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		//		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		/*
		 * Configurazione pannello editazione.
		 */
		editPanel.setLayout (new javax.swing.SpringLayout ());
		
		/*
		 * Configurazione editazione campo INIZIO.
		 */
		fromLabel.setLabelFor (fromEditor);
		fromLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "from"));
//		fromEditor.setMinimumSize (new Dimension (120, 20));
		
		/*
		 * Configurazione editazione campo FINE.
		 */
		toLabel.setLabelFor (toEditor);
		toLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "to"));
//		toEditor.setMinimumSize (new Dimension (120, 20));
		
		/*
		 * Configurazione editazione DESCRIZIONE.
		 */
		descriptionLabel.setLabelFor (descriptionEditor);
		descriptionLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "description"));
		descriptionEditor.setMinimumSize (new Dimension (120, 20));
		
		/*
		 * Configurazione editazione NOTE.
		 */
		notesLabel.setLabelFor (notesEditor);
		notesLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "notes"));
		notesEditor.setMinimumSize (new Dimension (120, 20));
		
		/*
		 * Inserimento editazione INIZIO.
		 */
		editPanel.add (fromLabel);
		editPanel.add (fromEditor);
		
		/*
		 * Inserimento editazione FINE.
		 */
		editPanel.add (toLabel);
		editPanel.add (toEditor);
		
		/*
		 * Inserimento editazione DESCRIZIONE.
		 */
		editPanel.add (descriptionLabel);
		editPanel.add (new JScrollPane (descriptionEditor,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		/*
		 * Inserimento editazione NOTE.
		 */
		editPanel.add (notesLabel);
		editPanel.add (new JScrollPane (notesEditor,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		
		
		
		SpringUtilities.makeCompactGrid(editPanel,
                                4, 2, //rows, cols
                                6, 6,        //initX, initY
                                6, 6);       //xPad, yPad
		
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
			 * C'è un nodo selezionato.
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
	}
	
	/**
	 * Aggiorna il nodo di avanzamento selezionato con i dati delle componenti di editazione.
	 */
	private final void pushData (){
		final Application app = Application.getInstance ();
		
		final Period editSubject = app.getSelectedProgress ();
		if (editSubject!=null){
			/* 
			 * C'è un nodo selezionato.
			 * Inizializzazione campi editazione a valori nodo di avanzamento selezionato.
			 */
			editSubject.setFrom ((Date)this.fromEditor.getValue ());
			editSubject.setTo ((Date)this.toEditor.getValue());
			editSubject.setDescription (this.descriptionEditor.getText () );
			editSubject.setNotes (this.notesEditor.getText () );
		}
	}
	
	/**
	 * Aggiorna questo pannello a seguito di una notifica da parte di un oggetto sosservato.
	 * Nello specifico la notifica di interesse è quella relativa al cambiamento del nodo
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
	
}

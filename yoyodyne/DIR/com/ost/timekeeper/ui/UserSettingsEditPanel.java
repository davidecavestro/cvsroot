/*
 * UserSettingsEditPanel.java
 *
 * Created on 12 dicembre2004, 11.51
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * Pannello di modifica impostazioni utente.
 *
 * @author  davide
 */
public final class UserSettingsEditPanel extends javax.swing.JPanel implements Observer {
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Pannello di editazione.
	 */
	final javax.swing.JPanel editPanel = new javax.swing.JPanel ();
	
	/**
	 * Etichetta per componente selezione colore desktop.
	 */
	final javax.swing.JLabel desktopColorLabel = new javax.swing.JLabel ();
	
	/**
	 * Componente selezione colore desktop.
	 */
	final ColorChooser desktopColorChooser = new ColorChooser ();
	
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
	public UserSettingsEditPanel () {
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
		editPanel.setLayout (new javax.swing.SpringLayout ());
		

		/*
		 * Configurazione editazione CODICE.
		 */
		desktopColorLabel.setLabelFor (desktopColorChooser);
		desktopColorLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "desktop.color"));
		desktopColorChooser.setMinimumSize (new Dimension (16, 16));
		desktopColorChooser.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				final Color newColor = JColorChooser.showDialog(
						 UserSettingsEditPanel.this,
						 ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.desktop.color"),
						 UserSettings.getInstance ().getDesktopColor ());
				desktopColorChooser.setColor (newColor);
				setDataChanged (true);
			}
		});
			
		
		/*
		 * Inserimento editazione CODICE.
		 */
		editPanel.add (desktopColorLabel);
		editPanel.add (desktopColorChooser);
		

		SpringUtilities.makeCompactGrid(editPanel,
                                1, 2, //rows, cols
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
		
		this.desktopColorChooser.setColor (UserSettings.getInstance ().getDesktopColor ());
	}
	
	/**
	 * Aggiorna il nodo di avanzamento selezionato con i dati delle componenti di editazione.
	 */
	private final void pushData (){
		System.out.println ("psuhing data");
		UserSettings.getInstance ().setDesktopColor (this.desktopColorChooser.getColor ());

		this.setDataChanged (false);
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
		if (arg!=null && arg.equals(ObserverCodes.USERSETTINGSCHANGE)){
			this.resynch ();
		}
	}
	
	/**
	 * Imposta il flag di "modifica avvenuta" per questo pannello.
	 */
	private void setDataChanged (boolean changed){
		this._dataChanged = changed;
		this.confirmButton.setEnabled (this._dataChanged);
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

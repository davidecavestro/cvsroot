/*
 * UserSettingsEditPanel.java
 *
 * Created on 12 dicembre 2004, 11.51
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
import javax.swing.border.*;

/**
 * Pannello di modifica impostazioni utente.
 *
 * @author  davide
 * @todo correggere gestione ablitazione pulsanti, sputtanata dalla creazione dei pannelli secondari.
 */
public final class UserSettingsEditPanel extends javax.swing.JPanel implements Observer {
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Pannello di editazione impostazioni dati.
	 */
	private final UserDataSettingsEditPanel dataEditPanel = new UserDataSettingsEditPanel ();
	
	/**
	 * Pannello di editazione impostazioni grafiche.
	 */
	private final UserGUISettingsEditPanel uiEditPanel = new UserGUISettingsEditPanel ();
	
	/**
	 * Pannello di editazione impostazioni grafiche.
	 */
	private final UserOtherSettingsEditPanel otherEditPanel = new UserOtherSettingsEditPanel ();
	
	
	/**
	 * Pulsantiera.
	 */
	private final javax.swing.JPanel buttonPanel = new javax.swing.JPanel ();
	/**
	 * Pulsante di conferma.
	 */
	private final javax.swing.JButton confirmButton = new javax.swing.JButton ();
	/**
	 * Pulsante di "applica".
	 */
	private final javax.swing.JButton applyButton = new javax.swing.JButton ();
	/**
	 * Pulsante di annullamento modifiche.
	 */
	private final javax.swing.JButton resetButton = new javax.swing.JButton ();
	
	/**********************************************
	 * FINE dichiarazione componenti UI interne.
	 **********************************************/
	
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
				/* @@@ nasconde finestra */
				getRootPane ().getParent ().setVisible (false);
			}
		});
		confirmButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "confirm"));
		
		/*
		 * Configurazione pulsante APPLICA.
		 */
		applyButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent e){
				pushData ();
			}
		});
		applyButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "apply"));
		
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
		 * Inserimento pulsante APPLICA.
		 */
		buttonPanel.add (applyButton);
		
		/*
		 * Inserimento pulsante ANNULLA.
		 */
		buttonPanel.add (resetButton);
		
		
		final JTabbedPane tabbedPane = new JTabbedPane ();
		tabbedPane.setTabLayoutPolicy (JTabbedPane.SCROLL_TAB_LAYOUT);
		
		/*
		 * Inserimento pannello editazione.
		 */
		this.add (tabbedPane, java.awt.BorderLayout.CENTER);
		
		/*
		 * Inserimento pannello editazione.
		 */
		tabbedPane.addTab (ResourceSupplier.getString (ResourceClass.UI, "controls", "ui.prefs"), new JScrollPane (uiEditPanel));

		
		/*
		 * Inserimento pannello editazione.
		 */
		tabbedPane.addTab (ResourceSupplier.getString (ResourceClass.UI, "controls", "data.prefs"), new JScrollPane (dataEditPanel));
		
		/*
		 * Inserimento pannello editazione.
		 */
		tabbedPane.addTab (ResourceSupplier.getString (ResourceClass.UI, "controls", "other.prefs"), new JScrollPane (otherEditPanel));
		
		
		/*
		 * Inserimento pulsantiera.
		 */
		this.add (buttonPanel, java.awt.BorderLayout.SOUTH);
		
		/*
		 * Inizializza flag a "NON MODIFICATO"
		 */
		this.resynch ();
		
		//getRootPane().setDefaultButton (confirmButton);
	}
	
	/**
	 * Risincronizza il pannello di editazione con i dati del modello.
	 */
	private final void resynch (){
		uiEditPanel.resynch ();
		dataEditPanel.resynch ();
		otherEditPanel.resynch ();
	}
	
	/**
	 * Aggiorna il nodo di avanzamento selezionato con i dati delle componenti di editazione.
	 */
	private final void pushData (){
		uiEditPanel.pushData ();
		dataEditPanel.pushData ();
		otherEditPanel.pushData ();
		onDataChanges ();
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
	public void update (Observable o, Object arg) {
		if (arg!=null && arg.equals (ObserverCodes.USERSETTINGSCHANGE)){
			this.resynch ();
		}
	}
	
	/**
	 * Imposta il flag di "modifica avvenuta" per questo pannello.
	 */
	private void onDataChanges (){
		final boolean dataChanged = 
			uiEditPanel.getDataChanged () 
			|| dataEditPanel.getDataChanged ()
			|| otherEditPanel.getDataChanged ()
			;

		this.confirmButton.setEnabled (dataChanged);
		this.applyButton.setEnabled (dataChanged);
		this.resetButton.setEnabled (dataChanged);
	}
	
}

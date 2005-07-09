/*
 * UserOtherSettingsEditPanel.java
 *
 * Created on 28 dicembre 2004, 19.14
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
import javax.swing.event.*;

/**
 * Pannello di modifica impostazioni utente non classificabili.
 *
 * @author  davide
 */
public final class UserOtherSettingsEditPanel extends ObservablePanel implements Observer {
	
	public final static String USEROTHERSETTINGSCHANGE = "userothersettingschange";
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Etichetta per componente editazione dimesnione del buffer per il log di testo piano.
	 */
	private final javax.swing.JLabel plainTextLogBufferSizeLabel = new javax.swing.JLabel ();
	
	/**
	 * Componente editazione del percorso della directory dello storage.
	 */
	private final javax.swing.JSpinner plainTextLogBufferSizeEditor = new javax.swing.JSpinner ();
	
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
	public UserOtherSettingsEditPanel () {
		/*
		 * Inizializzazione componenti grafiche.
		 */
		initComponents ();
	}
	
	/**
	 * Inizializzaizone componenti.
	 */
	private void initComponents () {
		
		setLayout (new java.awt.GridBagLayout ());
		
		/*
		 * Configurazione editazione del percorso della directory dello storage.
		 */
		plainTextLogBufferSizeLabel.setLabelFor (plainTextLogBufferSizeEditor);
		plainTextLogBufferSizeLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "plaintextlog.buffersize"));
		plainTextLogBufferSizeEditor.setMinimumSize (new Dimension (50, 16));
		
		/*
		 * Inizializzazione.
		 */
		plainTextLogBufferSizeEditor.addChangeListener (new ChangeListener (){
			public void stateChanged(ChangeEvent e){
					setDataChanged (true);
			}});
			
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
			
		/*
		 * Inserimento componenti editazione dimensione del buffer per il log.
		 */
		c.gridx = 0;
		c.gridy = 0;
		add (plainTextLogBufferSizeLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		add (plainTextLogBufferSizeEditor, c);
		
		/* etichetta vuota per riempire lo spazio rimanente */
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add (new JLabel (""), c);
//		SpringUtilities.makeCompactGrid (this,
//		1, 2, //rows, cols
//		6, 6,        //initX, initY
//		6, 6);       //xPad, yPad
		
		this.setMinimumSize (new Dimension (320, 240));
	}
	
	/**
	 * Risincronizza il pannello di editazione con i dati del modello.
	 */
	protected final void resynch (){
		/*
		 * resetta flag modifica
		 */
		this.setDataChanged (false);
		
		final Integer currentValue = UserSettings.getInstance ().getPlainTextLogBufferSize ();
		
		/*
		 * Inizializzazione.
		 */
		if (currentValue!=null) {
			this.plainTextLogBufferSizeEditor.setValue (currentValue);
		} else {
			this.plainTextLogBufferSizeEditor.setValue (new Integer (8192));			
		}
	}
	
	/**
	 * Aggiorna il nodo di avanzamento selezionato con i dati delle componenti di editazione.
	 */
	protected final void pushData (){
		Application.getLogger ().debug ("Pushing other user preferences.");
		UserSettings.getInstance ().setPlainTextLogBufferSize ((Integer)this.plainTextLogBufferSizeEditor.getValue ());
		
		this.setDataChanged (false);
	}
	
	/**
	 * Aggiorna questo pannello a seguito di una notifica da parte di un oggetto osservato.
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
	private void setDataChanged (boolean changed){
		this._dataChanged = changed;
		if (changed){
			this.setChanged ();
			//notifica la modifica
			this.notifyObservers (USEROTHERSETTINGSCHANGE);
		}
	}
	
	/**
	 * Ritorna il flag di "modifica avvenuta" per questo pannello.
	 */
	protected boolean getDataChanged (){
		return this._dataChanged;
	}
	
	
}

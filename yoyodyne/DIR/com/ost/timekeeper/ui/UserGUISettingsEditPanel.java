/*
 * UserGUISettingsEditPanel.java
 *
 * Created on 26 dicembre 2004, 12.09
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
 * Pannello di modifica impostazioni utente relative all'interfaccia grafica.
 *
 * @author  davide
 */
public final class UserGUISettingsEditPanel extends javax.swing.JPanel implements Observer {
	
	public final static String USERGUISETTINGSCHANGE = "userguisettingschange";
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Etichetta per componente selezione colore desktop.
	 */
	private final javax.swing.JLabel desktopColorLabel = new javax.swing.JLabel ();
	/**
	 * Componente selezione colore desktop.
	 */
	private final ColorChooser desktopColorChooser = new ColorChooser ();
	
	/**
	 * Etichetta per componente abilitazione notifica sonora.
	 */
	private final javax.swing.JLabel beepOnEventsLabel = new javax.swing.JLabel ();
	/**
	 * Componente abilitazione notifica sonora.
	 */
	private final JCheckBox beepOnEventsBox = new JCheckBox ();
	
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
	public UserGUISettingsEditPanel () {
		/*
		 * Inizializzazione componenti grafiche.
		 */
		initComponents ();
	}
	
	/**
	 * Inizializzaizone componenti.
	 */
	private void initComponents () {
		
		setLayout (new javax.swing.SpringLayout ());
		
		/*
		 * Configurazione scelta colore desktop.
		 */
		desktopColorLabel.setLabelFor (desktopColorChooser);
		desktopColorLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "desktop.color"));
		desktopColorChooser.setMinimumSize (new Dimension (16, 16));
		/*
		 * Inizializzazione.
		 */
		desktopColorChooser.setSize (50, 50);
		//		desktopColorChooser.setBorder (new EtchedBorder (EtchedBorder.RAISED));
		desktopColorChooser.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				final Color newColor = JColorChooser.showDialog (
				UserGUISettingsEditPanel.this,
				ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.desktop.color"),
				UserSettings.getInstance ().getDesktopColor ());
				desktopColorChooser.setColor (newColor);
				setDataChanged (true);
			}
		});
		
		/*
		 * Configurazione abilitazione notifica sonora.
		 */
		beepOnEventsLabel.setLabelFor (beepOnEventsBox);
		beepOnEventsLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "beeponevents"));
		
		beepOnEventsBox.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				setDataChanged (true);
			}
		});
		
		/*
		 * Inserimento componenti scelta colore desktop.
		 */
		add (desktopColorLabel);
		add (desktopColorChooser);
		
		/*
		 * Inserimento componenti abilitazione notifica sonora.
		 */
		add (beepOnEventsLabel);
		add (beepOnEventsBox);
		
		
		SpringUtilities.makeCompactGrid (this,
		2, 2, //rows, cols
		6, 6,        //initX, initY
		6, 6);       //xPad, yPad
		
		//getRootPane().setDefaultButton (confirmButton);
	}
	
	/**
	 * Risincronizza il pannello di editazione con i dati del modello.
	 */
	protected final void resynch (){
		/*
		 * resetta flag modifica
		 */
		this.setDataChanged (false);
		
		this.desktopColorChooser.setColor (UserSettings.getInstance ().getDesktopColor ());
		/*
		 * Inizializzazione.
		 */
		final Boolean currentUserValue = UserSettings.getInstance ().beepOnEvents ();
		if (currentUserValue!=null){
			beepOnEventsBox.setSelected (currentUserValue.booleanValue ());
		} else {
			/* inizializza con valore opzioni */
			final boolean currentValue = Application.getOptions ().beepOnEvents ();
			beepOnEventsBox.setSelected (currentValue);
		}
	}
	
	/**
	 * Aggiorna il nodo di avanzamento selezionato con i dati delle componenti di editazione.
	 */
	protected final void pushData (){
		Application.getLogger ().info ("Pushing user GUI preferences data.");
		UserSettings.getInstance ().setDesktopColor (this.desktopColorChooser.getColor ());
		UserSettings.getInstance ().setBeepOnEvents (BooleanUtils.getBoolean (this.beepOnEventsBox.isSelected ()));
		
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
	}
	
	/**
	 * Ritorna il flag di "modifica avvenuta" per questo pannello.
	 */
	protected boolean getDataChanged (){
		return this._dataChanged;
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

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
public final class UserGUISettingsEditPanel extends ObservablePanel implements Observer {
	
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
		
		setLayout (new java.awt.GridBagLayout ());
		
		/*
		 * Configurazione scelta colore desktop.
		 */
		desktopColorLabel.setLabelFor (desktopColorChooser);
		desktopColorLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "desktop.color"));
		desktopColorChooser.setMinimumSize (new Dimension (16, 16));
		/*
		 * Inizializzazione.
		 */
		desktopColorChooser.setPreferredSize (new Dimension (16, 16));
		desktopColorChooser.setBorder (new CompoundBorder (new BevelBorder (BevelBorder.RAISED), (new CompoundBorder (new EmptyBorder (2,2,2,2), new BevelBorder (BevelBorder.LOWERED)))));
		//		desktopColorChooser.setBorder (new EtchedBorder (EtchedBorder.RAISED));
		desktopColorChooser.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				final Color newColor = JColorChooser.showDialog (
				UserGUISettingsEditPanel.this,
				ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.desktop.color"),
				UserSettings.getInstance ().getDesktopColor ());
				if (newColor!=null){
					desktopColorChooser.setColor (newColor);
					setDataChanged (true);
				}
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
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);
		
		/*
		 * Inserimento componenti scelta colore desktop.
		 */
		c.gridx = 0;
		c.gridy = 0;
		add (desktopColorLabel, c);
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 1;
		c.gridy = 0;
		add (desktopColorChooser, c);
		
		/*
		 * Inserimento componenti abilitazione notifica sonora.
		 */
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		add (beepOnEventsLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		add (beepOnEventsBox, c);
		
		/* etichetta vuota per riempire lo spazio rimanente */
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		add (new JLabel (""), c);
		
//		SpringUtilities.makeCompactGrid (this,
//		2, 2, //rows, cols
//		6, 6,        //initX, initY
//		6, 6);       //xPad, yPad
		
		//getRootPane().setDefaultButton (confirmButton);
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
	 * Nello specifico la notifica di interesse � quella relativa al cambiamento del nodo
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
		if (changed){
			this.setChanged ();
			//notifica la modifica
			this.notifyObservers (USERGUISETTINGSCHANGE);
		}
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
/*
 * UserDataSettingsEditPanel.java
 *
 * Created on 24 dicembre 2004, 19.14
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.persistence.*;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * Pannello di modifica impostazioni utente relative alla gestione dei dati.
 *
 * @author  davide
 */
public final class UserDataSettingsEditPanel extends ObservablePanel implements Observer {
	
	public final static String USERDATASETTINGSCHANGE = "userdatasettingschange";
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Etichetta per componente editazione del percorso della directory dello storage.
	 */
	private final javax.swing.JLabel jdoStorageDirPathLabel = new javax.swing.JLabel ();
	
	/**
	 * Componente editazione del percorso della directory dello storage.
	 */
	private final javax.swing.JTextField jdoStorageDirPathEditor = new javax.swing.JTextField ();
	
	/**
	 * Componente selezione del percorso della directory dello storage.
	 */
	private final JFileChooser jdoStorageDirPathChooser = new JFileChooser();
	
	/**
	 * Etichetta per componente editazione del nome dello storage.
	 */
	private final javax.swing.JLabel jdoStorageNameLabel = new javax.swing.JLabel ();
	
	/**
	 * Componente editazione del nome dello storage.
	 */
	private final javax.swing.JTextField jdoStorageNameEditor = new javax.swing.JTextField ();

	/**
	 * Pulsante di selezione percorso.
	 */
	private final javax.swing.JButton jdoStorageDirPathChoice = new javax.swing.JButton ();
	
	/**
	 * Pulsante di inizializzazione storage.
	 */
	private final javax.swing.JButton jdoStorageInitButton = new javax.swing.JButton ();
	
	
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
	public UserDataSettingsEditPanel () {
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
		
		setLayout (new GridBagLayout ());
		
		/*
		 * Configurazione editazione del percorso della directory dello storage.
		 */
		jdoStorageDirPathLabel.setLabelFor (jdoStorageDirPathEditor);
		jdoStorageDirPathLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "jdostoragedir.path"));
		jdoStorageDirPathChoice.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "choose"));
		jdoStorageDirPathEditor.setMinimumSize (new Dimension (200, 16));
		
		/*
		 * Inizializzazione.
		 */
		jdoStorageDirPathChooser.setFileSelectionMode (jdoStorageDirPathChooser.DIRECTORIES_ONLY);
		jdoStorageDirPathChooser.setFileHidingEnabled (false);
		//		desktopColorChooser.setBorder (new EtchedBorder (EtchedBorder.RAISED));
		jdoStorageDirPathChoice.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				final int returnVal = jdoStorageDirPathChooser.showDialog (
					Application.getInstance ().getMainForm(), ResourceSupplier.getString(ResourceClass.UI, "controls", "jdostoragedir.path"));
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					jdoStorageDirPathEditor.setText (jdoStorageDirPathChooser.getSelectedFile().getPath ());
					setDataChanged (true);
				}
			}});
			
		jdoStorageDirPathEditor.getDocument ().addDocumentListener (new DocumentListener(){
		    public void insertUpdate(DocumentEvent e){somethingChanged (e);}
		    public void removeUpdate(DocumentEvent e){somethingChanged (e);}
			public void changedUpdate(DocumentEvent e){somethingChanged (e);}
	
			private void somethingChanged (DocumentEvent e){
				setDataChanged (true);
			}
		});
		
		jdoStorageInitButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "datastorage.init"));
		jdoStorageInitButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				if (
				JOptionPane.showConfirmDialog (
				UserDataSettingsEditPanel.this, ResourceSupplier.getString (ResourceClass.UI, "controls", "datastorage.init.confirm"))==JOptionPane.OK_OPTION){
					Application.getInstance ().createDataStore ();
				}
			}});
			
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
		
		/*
		 * Inserimento componenti editazione percorso directory storage notifica sonora.
		 */
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		add (jdoStorageDirPathLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		add (jdoStorageDirPathEditor, c);
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.5;
		add (jdoStorageDirPathChoice, c);
		
		/*
		 * Configurazione editazione del nome dello storage.
		 */
		jdoStorageNameLabel.setLabelFor (jdoStorageNameEditor);
		jdoStorageNameLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "jdostorage.name"));
		jdoStorageNameEditor.setMinimumSize (new Dimension (150, 16));
		jdoStorageNameEditor.getDocument ().addDocumentListener (new DocumentListener(){
		    public void insertUpdate(DocumentEvent e){somethingChanged (e);}
		    public void removeUpdate(DocumentEvent e){somethingChanged (e);}
			public void changedUpdate(DocumentEvent e){somethingChanged (e);}
	
			private void somethingChanged (DocumentEvent e){
				setDataChanged (true);
			}
		});
		
		
//		c.weightx = 1;
		/*
		 * Inserimento componenti editazione nome dello storage.
		 */
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.5;
		add (jdoStorageNameLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		add (jdoStorageNameEditor, c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		add (jdoStorageInitButton, c);
		
		/* etichetta vuota per riempire lo spazio rimanente */
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		add (new JLabel (""), c);
		
//		SpringUtilities.makeCompactGrid (this,
//		2, 3, //rows, cols
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
		
		/*
		 * Inizializzazione.
		 */
		this.jdoStorageDirPathEditor.setText (UserSettings.getInstance ().getJDOStorageDirPath ());
		this.jdoStorageNameEditor.setText (UserSettings.getInstance ().getJDOStorageName ());
		
	}
	
	/**
	 * Aggiorna il nodo di avanzamento selezionato con i dati delle componenti di editazione.
	 */
	protected final void pushData (){
		Application.getLogger ().debug ("Pushing user data preferences.");
		UserSettings.getInstance ().setJDOStorageDirPath (this.jdoStorageDirPathEditor.getText ());
		UserSettings.getInstance ().setJDOStorageName (this.jdoStorageNameEditor.getText ());
		
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
		/* Abilita il pulsante di inizializzazione archivio solo dopo aver applicato le modifiche. */
		this.jdoStorageInitButton.setEnabled (!changed);
		if (changed){
			this.setChanged ();
			//notifica la modifica
			this.notifyObservers (USERDATASETTINGSCHANGE);
		}
	}
	
	/**
	 * Ritorna il flag di "modifica avvenuta" per questo pannello.
	 */
	protected boolean getDataChanged (){
		return this._dataChanged;
	}
	
	
}

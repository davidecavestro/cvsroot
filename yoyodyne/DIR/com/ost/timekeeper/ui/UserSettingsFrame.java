/*
 * UserSettingsFrame.java
 *
 * Created on 13 dicembre 2004, 22.41
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.util.*;
import java.awt.*;

/**
 * Il frame per la gestione delle preferenze utente.
 *
 * @author  davide
 */
public final class UserSettingsFrame extends javax.swing.JDialog {
	
	/**
	 * Istanza singleton.
	 */
	private static UserSettingsFrame _instance;
	
	/**
	 * Pannello di editazione dettaglio.
	 */
	private UserSettingsEditPanel editPanel;
	
	/** 
	 * Costruttore. 
	 */
	private UserSettingsFrame () {
//		super (ResourceSupplier.getString (ResourceClass.UI, "controls", "user.settings"),
//			true, //resizable
//			false, //closable
//			true, //maximizable
//			true);//iconifiable
		setTitle (ResourceSupplier.getString (ResourceClass.UI, "controls", "user.settings"));
		initComponents ();
		
//		this.setIconImage (ResourceSupplier.getImageIcon (ResourceClass.UI, "user-settings-frame.gif").getImage ());
		
		/*
		 * centra
		 */
		this.setLocationRelativeTo (null);
	}
	
	/**
	 * Ritorna l'istanza di questo inspector.
	 *
	 * @return l'istanza di questo inspector.
	 */	
	public static UserSettingsFrame getInstance (){
		if (_instance==null){
			_instance = new UserSettingsFrame();
		}
		return _instance;
	}
	
	/** 
	 * Inizializzazione delle componenti di questo frame.
	 */
	private void initComponents () {
		final Application app = Application.getInstance ();
		
		this.editPanel = new UserSettingsEditPanel ();
		app.addObserver (editPanel);
		
		this.setContentPane (editPanel);
		
		/*
		 * Imposta dimensione minima.
		 */
//		this.setMinimumSize (new Dimension (250, 150));
		pack ();
	}

	/**
	 * Mostra il pannello di editazione della configurazione relativa ai dati.
	 */
	public void showDataSettings (){
		this.editPanel.showDataSettings ();
	}
	
}

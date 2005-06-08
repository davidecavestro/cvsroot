/*
 * UserSettingsFrame.java
 *
 * Created on 13 dicembre 2004, 22.41
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.ui.persistence.PersistenceStorage;
import com.ost.timekeeper.ui.persistence.PersistenceUtils;
import com.ost.timekeeper.ui.persistence.PersistentComponent;
import com.ost.timekeeper.ui.persistence.UIPersister;
import com.ost.timekeeper.util.*;
//import com.tomtessier.scrollabledesktop.BaseInternalFrame;
import java.awt.*;
import javax.swing.JFrame;

/**
 * Il frame per la gestione delle preferenze utente.
 *
 * @author  davide
 */
public final class UserSettingsFrame extends /*javax.swing.JDialog*/ JFrame implements PersistentComponent{
	
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
		
		this.setIconImage (ResourceSupplier.getImageIcon (ResourceClass.UI, "user-settings-frame.png").getImage ());
		
		final boolean inited = UIPersister.getInstance ().register (this, true);
		
		if (!inited ){
			/*
			 * centra
			 */
			this.setLocationRelativeTo (null);
		}
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
//		getRootPane ().setPreferredSize (new java.awt.Dimension (340, 220));
		pack ();
	}

	/**
	 * Mostra il pannello di editazione della configurazione relativa ai dati.
	 */
	public void showDataSettings (){
		this.editPanel.showDataSettings ();
	}
	
	/**
	 * Ritorna la chiave usata per la persistenza degli attributi di questo componente.
	 *
	 * @return la chiave usata per la persistenza degli attributi di questo componente.
	 */	
	public String getPersistenceKey (){return "usersettingsframe";}
	
	/**
	 * Rende persistente lo statodi questo componente.
	 * @param props lo storage delle impostazioni persistenti.
	 */
	public void makePersistent (final PersistenceStorage props){
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);
	}
	
	/**
	 * Ripristina lo stato persistente, qualora esista.
	 * @param props lo storage delle impostazioni persistenti.
	 * @return <TT>true</TT> se sono stati ripristinati i dati persistenti.
	 */
	public boolean restorePersistent (final PersistenceStorage props){
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}
		
}

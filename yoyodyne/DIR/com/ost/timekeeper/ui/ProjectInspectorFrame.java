/*
 * ProjectInspectorFrame.java
 *
 * Created on 30 maggio 2005, 23.31
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.ui.persistence.PersistenceStorage;
import com.ost.timekeeper.ui.persistence.PersistenceUtils;
import com.ost.timekeeper.ui.persistence.PersistentComponent;
import com.ost.timekeeper.ui.persistence.UIPersister;
import com.ost.timekeeper.util.*;
//import com.tomtessier.scrollabledesktop.*;
import java.awt.*;
import javax.swing.JFrame;

/**
 * Il frame per la gestione del dettaglio progetto.
 *
 * @author  davide
 */
public final class ProjectInspectorFrame extends JFrame implements PersistentComponent{
	
	/**
	 * Istanza singleton.
	 */
	private static ProjectInspectorFrame _instance;
	
	/**
	 * Pannello di editazione dettaglio.
	 */
	private ProjectEditPanel _projectEditPanel;
	
	/** 
	 * Costruttore. 
	 */
	private ProjectInspectorFrame () {
		super (ResourceSupplier.getString (ResourceClass.UI, "controls", "project.inspector"));
		initComponents ();
		
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
	public static ProjectInspectorFrame getInstance (){
		if (_instance==null){
			_instance = new ProjectInspectorFrame();
		}
		return _instance;
	}
	
	/** 
	 * Inizializzazione delle componenti di questo frame.
	 */
	private void initComponents () {
		final Application app = Application.getInstance ();
		
		this._projectEditPanel = new ProjectEditPanel ();
		app.addObserver (this._projectEditPanel);
		
		this.setContentPane (_projectEditPanel);
		
		/*
		 * Imposta dimensione minima.
		 */
//		this.setMinimumSize (new Dimension (250, 150));
		
		this.setIconImage (ResourceSupplier.getImageIcon (ResourceClass.UI, "project-inspector-frame.png").getImage ());
		
		pack ();
	}
	
	/**
	 * Ritorna la chiave usata per la persistenza degli attributi di questo componente.
	 *
	 * @return la chiave usata per la persistenza degli attributi di questo componente.
	 */	
	public String getPersistenceKey (){return "projecteditframe";}
	
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

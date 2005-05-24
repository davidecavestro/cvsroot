/*
 * LogFrame.java
 *
 * Created on 11 maggio 2005, 21.03
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.ui.persistence.PersistenceStorage;
import com.ost.timekeeper.ui.persistence.PersistenceUtils;
import com.ost.timekeeper.ui.persistence.PersistentComponent;
import com.ost.timekeeper.ui.persistence.UIPersister;
import com.ost.timekeeper.util.*;
import com.tomtessier.scrollabledesktop.BaseInternalFrame;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;

/**
 * Il frame di visualizzazione del log.
 *
 * @author  davide
 */
public final class LogFrame extends JFrame implements PersistentComponent{
	
	/**
	 * Istanza singleton.
	 */
	private static LogFrame _instance;
	
	private final JTextPane _logPanel = new JTextPane ();

	/** 
	 * Costruttore. 
	 */
	private LogFrame () {
//		super (ResourceSupplier.getString (ResourceClass.UI, "controls", "user.settings"),
//			true, //resizable
//			false, //closable
//			true, //maximizable
//			true);//iconifiable
		setTitle (ResourceSupplier.getString (ResourceClass.UI, "controls", "Log_frame"));
		initComponents ();
		
		final boolean inited = UIPersister.getInstance ().register (this, true);
		
		this.setIconImage (ResourceSupplier.getImageIcon (ResourceClass.UI, "log-frame.png").getImage ());
		
		if (!inited ){
			/*
			 * centra
			 */
			this.setLocationRelativeTo (null);
		}
		
	}
	
	/**
	 * Ritorna l'istanza di questa finestra.
	 *
	 * @return l'istanza di questa finestra.
	 */	
	public static LogFrame getInstance (){
		if (_instance==null){
			_instance = new LogFrame();
		}
		return _instance;
	}
	
	/**
	 * Inizializza la finestra sul docuemnto specificato.
	 *
	 * @param logDocument il docuemnto contentnente le informazioni di log.
	 */	
	public static void init (final Document logDocument){
		_instance._logPanel.setDocument (logDocument);
	}
	
	/** 
	 * Inizializzazione delle componenti di questo frame.
	 */
	private void initComponents () {
		
		final JPanel panel = new JPanel (new BorderLayout ());
		
		final JToolBar toolbar = new JToolBar ();
		
		
		_logPanel.setEditable (false);
//		_logPanel.setBackground (Color.BLACK);
//		_logPanel.setForeground (Color.WHITE);

		panel.add (toolbar, BorderLayout.NORTH);
		panel.add (new JScrollPane (_logPanel), BorderLayout.CENTER);
		
		this.getContentPane ().add (panel);
		
		/*
		 * Imposta dimensione minima.
		 */
//		this.setMinimumSize (new Dimension (250, 150));
		this.setBounds (0,0,300, 300);
//		pack ();
	}

	/**
	 * Ritorna la chiave usata per la persistenza degli attributi di questo componente.
	 *
	 * @return la chiave usata per la persistenza degli attributi di questo componente.
	 */	
	public String getPersistenceKey (){return "logframe";}
	
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

/*
 * SplashScreen.java
 *
 * Created on 8 dicembre 2004, 9.25
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Finestra informativa sull'applicazione.
 *
 * @author  davide
 */
public final class AboutBox extends JDialog {
	
	private static AboutBox _instance;
	
	/*
	 * Componenti.
	 */
	
	/**
	 * Il pannello principale.
	 */
	private JPanel mainPanel;
	
	/**
	 * Il pannello dei pulsanti.
	 */
	private JPanel buttonPanel;
	
	/**
	 * Il pannello contenente i loghi.
	 */
	private JPanel logoPanel;
	
	/**
	 * Il pannello contenente le informazioni.
	 */
	private JPanel infoPane;
	
	/**
	 * Le informazioni.
	 */
	private JEditorPane infoEditor;
	
	/**
	 * Etichetta logo aziendale.
	 */
	private JLabel companyLogoLabel;
	
	/**
	 * Immagine logo aziendale.
	 */
	private ImageIcon companyLogoImage;
	
	/**
	 * Etichetta nome prodotto.
	 */
	private JLabel productNameLabel;
	
	/**
	 * Immagine nome prodotto.
	 */
	private ImageIcon productNameImage;
	
	/**
	 * Etichetta logo prodotto.
	 */
	private JLabel productLogoLabel;
	
	/**
	 * Immagine logo prodotto.
	 */
	private ImageIcon productLogoImage;
	
	/** 
	 * Costruttore. 
	 */
	private AboutBox () {
		initComponents ();
	}
	
	/**
	 * Implementa il singleton.
	 *
	 * @return lìistanza di questo singleton.
	 */	
	public static AboutBox getInstance (){
		if (_instance==null){
			_instance = new AboutBox ();
		}
		return _instance;
	}
	
	/**
	 * Inizializzazione componenti di questa finestra.
	 */
	public void initComponents (){
		mainPanel = new JPanel (new BorderLayout ());
		
		logoPanel = new JPanel (new BorderLayout ());
		
		this.productNameLabel = new JLabel ();
//		this.productNameLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "productname"));
		this.productNameLabel.setText ("");
		this.productNameImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "productname.jpg");
		this.productNameLabel.setIcon (this.productNameImage);
		this.logoPanel.add (this.productNameLabel, BorderLayout.NORTH);
		
		this.infoPane = new JPanel (new BorderLayout ());
		this.infoEditor = new JEditorPane ("text/html", ResourceSupplier.getString (ResourceClass.UI, "about", "applicationinfo"));
		this.infoEditor.setEditable (false);
		this.infoPane.add (this.infoEditor, BorderLayout.NORTH);
		this.logoPanel.add (this.infoPane, BorderLayout.SOUTH);
		
		this.companyLogoLabel = new JLabel ();
//		this.companyLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "companylogo"));
		this.companyLogoLabel.setText ("");
		this.companyLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "companylogosmall.jpg");
		this.companyLogoLabel.setIcon (this.companyLogoImage);
		this.mainPanel.add (this.companyLogoLabel, BorderLayout.CENTER);
		
		this.productLogoLabel = new JLabel ();
//		this.productLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "productlogo"));
		this.productLogoLabel.setText ("");
		this.productLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "productlogosmall.jpg");
		this.productLogoLabel.setIcon (this.productLogoImage);
		this.mainPanel.add (this.productLogoLabel, BorderLayout.CENTER);
		
		
		this.mainPanel.add (this.logoPanel, BorderLayout.NORTH);
		
		this.buttonPanel = new JPanel ();
		final JButton confirmButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "ok"));
		confirmButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				_instance.hide ();
			}
		});
		this.buttonPanel.add (confirmButton);
		this.mainPanel.add (this.buttonPanel, BorderLayout.SOUTH);
		
		/*
		 * Aggiunge pannello principale.
		 */
		this.getContentPane ().add (mainPanel);
		
		this.getRootPane().setDefaultButton (confirmButton);
		
		this.pack ();
		/*
		 * Centra sullo schermo.
		 */
		this.setLocationRelativeTo (null);
	}
}

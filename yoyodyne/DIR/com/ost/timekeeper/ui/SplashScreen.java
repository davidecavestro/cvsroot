/*
 * SplashScreen.java
 *
 * Created on 8 dicembre 2004, 9.25
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * Finestra senza barra del titolo , ne bordi, visibile tra il momento del 
 * lancio dell'applicazione e l'appariziione della finestra principale.
 *
 * @author  davide
 */
public final class SplashScreen extends JWindow {
	
	private static SplashScreen _instance;
	
	/*
	 * Componenti.
	 */
	
	/**
	 * Il pannello principale.
	 */
	private JPanel mainPanel;
	
	/**
	 * Il pannello contenente i loghi.
	 */
	private JPanel logoPanel;
	
	/**
	 * La barra di avanzamento.
	 */
	private JProgressBar progressBar;
	
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
	private SplashScreen () {
		initComponents ();
	}
	
	/**
	 * Implementa il singleton.
	 *
	 * @return lìistanza di questo singleton.
	 */	
	public static SplashScreen getInstance (){
		if (_instance==null){
			_instance = new SplashScreen ();
		}
		return _instance;
	}
	
	/**
	 * Fa partire lo spash screen.
	 */
	public void startSplash (){
		progressBar.setIndeterminate (true);
		progressBar.setValue (50);
		this.show ();
	}
	
	/**
	 * Ferma lo spash screen.
	 */
	public void stopSplash (){
		this.hide ();
		progressBar.setIndeterminate (false);
		progressBar.setValue (0);
	}
	
	/**
	 * Inizializzazione componenti di questa finestra.
	 */
	public void initComponents (){
		setBounds (ApplicationOptions.getInstance ().getSplashScreenBounds ());
		mainPanel = new JPanel (new BorderLayout ());
		
		logoPanel = new JPanel (new BorderLayout ());
		
		this.companyLogoLabel = new JLabel ();
//		this.companyLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "companylogo"));
		this.companyLogoLabel.setText ("");
		this.companyLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "companylogo.jpg");
		this.companyLogoLabel.setIcon (this.companyLogoImage);
		this.logoPanel.add (this.companyLogoLabel, BorderLayout.NORTH);
		
		this.productNameLabel = new JLabel ();
//		this.productNameLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "productname"));
		this.productNameLabel.setText ("");
		this.productNameImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "productname.jpg");
		this.productNameLabel.setIcon (this.productNameImage);
		this.logoPanel.add (this.productNameLabel, BorderLayout.CENTER);
		
		this.productLogoLabel = new JLabel ();
//		this.productLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "productlogo"));
		this.productLogoLabel.setText ("");
		this.productLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "productlogo.jpg");
		this.productLogoLabel.setIcon (this.productLogoImage);
		this.logoPanel.add (this.productLogoLabel, BorderLayout.SOUTH);
		
		
		this.mainPanel.add (this.logoPanel, BorderLayout.CENTER);
		
		progressBar = new JProgressBar ();
		progressBar.setBorder (null);
		progressBar.setMinimumSize (new Dimension (50, 20));
	
		mainPanel.add (progressBar, BorderLayout.SOUTH);
		this.mainPanel.setBorder (BorderFactory.createLineBorder (Color.BLACK));
		/*
		 * Aggiunge pannello principale.
		 */
		this.getContentPane ().add (mainPanel);
		this.pack ();
		/*
		 * Centra sullo schermo.
		 */
		this.setLocationRelativeTo (null);
	}
}

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
        final JScrollPane infoScrollPane = new JScrollPane(this.infoEditor);
        infoScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        infoScrollPane.setPreferredSize (new Dimension (this.productNameImage.getIconWidth (), this.productNameImage.getIconHeight ()));
        infoScrollPane.setMinimumSize(new Dimension(10, 10));
		
//		this.infoPane.add (new JScrollPane (this.infoEditor), BorderLayout.NORTH);
//		this.infoPane.setPreferredSize (new Dimension (this.productNameImage.getIconWidth (), this.productNameImage.getIconHeight ()));
		this.logoPanel.add (infoScrollPane, BorderLayout.CENTER);
		
		mainPanel = new JPanel (new BorderLayout ());
		
		this.mainPanel.add (this.logoPanel, BorderLayout.NORTH);
		
		final JPanel otherLogosPanel = new JPanel ();

		this.companyLogoLabel = new JLabel ();
//		this.companyLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "companylogo"));
		this.companyLogoLabel.setText ("");
		this.companyLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "companylogosmall.jpg");
		this.companyLogoLabel.setIcon (this.companyLogoImage);
		otherLogosPanel.add (this.companyLogoLabel, BorderLayout.NORTH);
		
		this.productLogoLabel = new JLabel ();
//		this.productLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "productlogo"));
		this.productLogoLabel.setText ("");
		this.productLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "productlogosmall.jpg");
		this.productLogoLabel.setIcon (this.productLogoImage);
		otherLogosPanel.add (this.productLogoLabel, BorderLayout.SOUTH);
		
		this.mainPanel.add (otherLogosPanel, BorderLayout.CENTER);		
		
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
		this.getContentPane ().add (new JScrollPane (mainPanel));
		
		this.getRootPane().setDefaultButton (confirmButton);
		
		this.pack ();
		/*
		 * Centra sullo schermo.
		 */
		this.setLocationRelativeTo (null);
		
		/*
		 * Impedisce ridimensionamento.
		 */
		this.setResizable (false);
	}
}

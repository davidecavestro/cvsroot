/*
 * SplashScreen.java
 *
 * Created on 8 dicembre 2004, 9.25
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
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
	private JPanel aboutPanel;
	
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
		
		aboutPanel = new JPanel (new BorderLayout ());
		
		this.aboutPanel.add (this.logoPanel, BorderLayout.NORTH);
		
		final JPanel otherLogosPanel = new JPanel ();

		this.companyLogoLabel = new JLabel ();
//		this.companyLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "companylogo"));
		this.companyLogoLabel.setText ("");
		this.companyLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "companylogosmall.jpg");
		this.companyLogoLabel.setIcon (this.companyLogoImage);
		otherLogosPanel.add (this.companyLogoLabel, BorderLayout.NORTH);
		
		this.aboutPanel.add (otherLogosPanel, BorderLayout.CENTER);		
		
		this.buttonPanel = new JPanel ();
		final JButton confirmButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "ok"));
		confirmButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				_instance.hide ();
			}
		});
		this.buttonPanel.add (confirmButton);
		
		final JTabbedPane tabbedPanel = new JTabbedPane ();
		tabbedPanel.addTab ("About", new JScrollPane (aboutPanel));
		
		final JPanel detailPanel = new JPanel ();
		
		detailPanel.setLayout (new GridBagLayout ());
		
		final ApplicationData appData = ApplicationData.getInstance ();
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);
		
		this.productLogoLabel = new JLabel ();
//		this.productLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "productlogo"));
		this.productLogoLabel.setText ("");
		this.productLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "productlogosmall.jpg");
		this.productLogoLabel.setIcon (this.productLogoImage);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		detailPanel.add (this.productLogoLabel, c);
		
		{
			final JLabel detailTitle = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "about", "detail.title"));
			detailTitle.setHorizontalAlignment (JLabel.CENTER);
			detailTitle.setFont (new java.awt.Font ("Default", Font.BOLD, 12));

			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 1;
			c.gridy = 0;
			c.gridwidth = 2;
			detailPanel.add (detailTitle, c);
		}
		
		{
			final JLabel productVersionLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "about", "product.version")+": ");
			productVersionLabel.setHorizontalAlignment (JLabel.RIGHT);
			
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
			detailPanel.add (productVersionLabel, c);
		}
		{
			final JLabel productVersionValue = new JLabel (appData.getVersionNumber ());

			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 2;
			c.gridy = 1;
			c.gridwidth = 1;
			detailPanel.add (productVersionValue, c);
		}
		
		{
			final JLabel productBuildLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "about", "product.build")+": ");
			productBuildLabel.setHorizontalAlignment (JLabel.RIGHT);

			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 1;
			detailPanel.add (productBuildLabel, c);
		}
		{
			final JLabel productBuildValue = new JLabel (appData.getBuildNumber ());

			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 2;
			c.gridy = 2;
			c.gridwidth = 1;
			detailPanel.add (productBuildValue, c);
		}
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 10;
		detailPanel.add (new JLabel (), c);
		
		tabbedPanel.addTab ("Details", new JScrollPane (detailPanel));
		
		final JPanel mainPanel = new JPanel ();
		mainPanel.setLayout (new BorderLayout ());
		
		mainPanel.add (tabbedPanel, BorderLayout.CENTER);
		
		mainPanel.add (buttonPanel, BorderLayout.SOUTH);
		
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
		
		/*
		 * Impedisce ridimensionamento.
		 */
		this.setResizable (false);
	}
}

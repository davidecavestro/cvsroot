/*
 * SplashScreen.java
 *
 * Created on 8 dicembre 2004, 9.25
 */

package com.ost.timekeeper.ui;

import com.jgoodies.animation.*;
import com.ost.timekeeper.*;
import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.ui.support.GradientPanel;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
	
//	/**
//	 * Etichetta logo prodotto.
//	 */
//	private JLabel productLogoLabel;
	
//	/**
//	 * Immagine logo prodotto.
//	 */
//	private ImageIcon productLogoImage;
	
	/**
	 * La pagina di intro
	 */
    private IntroPage introPage;
	
	/**
	 * La velocità dell'intro.
	 */
    private static final int DEFAULT_FRAME_RATE = 30;
	
	/**
	 * Il pannello contenente l'animazione di intro.
	 */
	private JPanel introPanel;
	
	/**
	 * Il pannello contenente la licenza.
	 */
	private JPanel licensePanel;
	
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
//		this.productNameLabel.setIcon (this.productNameImage);
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
		
		this.aboutPanel.add (this.logoPanel, BorderLayout.CENTER);
		
//		final JPanel otherLogosPanel = new JPanel ();

//		this.companyLogoLabel = new JLabel ();
////		this.companyLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "companylogo"));
//		this.companyLogoLabel.setText ("");
//		this.companyLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "companylogosmall.jpg");
//		this.companyLogoLabel.setIcon (this.companyLogoImage);
//		otherLogosPanel.add (this.companyLogoLabel, BorderLayout.NORTH);
		
//		this.aboutPanel.add (otherLogosPanel, BorderLayout.CENTER);		
		
		this.licensePanel = new JPanel (new BorderLayout ());
		
		try {
			final JEditorPane licenseEditor = new JEditorPane (this.getClass ().getResource ("license.html"));
			this.infoEditor.setEditable (false);
			this.licensePanel.add (new JScrollPane (licenseEditor), BorderLayout.CENTER);
		} catch (final IOException ioe){
			throw new NestedRuntimeException (ioe);
		}
		
		
		this.buttonPanel = new JPanel ();
		final JButton confirmButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "ok"));
		confirmButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				_instance.hide ();
			}
		});
		this.buttonPanel.add (confirmButton);
		
		final JTabbedPane tabbedPanel = new JTabbedPane ();
		
		this.introPage = new IntroPage();
		
		introPanel = introPage.build ();
		introPanel.setMaximumSize (new Dimension (350, 100));
		introPanel.addComponentListener (new ComponentAdapter (){
			public void componentShown(ComponentEvent e){
				AboutBox.this._animator = new Animator(AboutBox.this.introPage.animation(), DEFAULT_FRAME_RATE);
				AboutBox.this._animator.start ();
			}
			public void componentHidden(ComponentEvent e){
				if (AboutBox.this._animator !=null){
					AboutBox.this._animator.stop ();
				}
			}
		});

		tabbedPanel.addTab (
			ResourceSupplier.getString (ResourceClass.UI, "about", "intro.title"),
			introPanel);

		tabbedPanel.addTab (
			ResourceSupplier.getString (ResourceClass.UI, "about", "about.title"),
			new JScrollPane (aboutPanel));
		
		tabbedPanel.addTab (
			ResourceSupplier.getString (ResourceClass.UI, "about", "about.license"),
			new JScrollPane (licensePanel));
		
		
		tabbedPanel.addTab (
			ResourceSupplier.getString (ResourceClass.UI, "about", "details.title"),
			new JScrollPane (createDetailPanel ())
			);
		
//		final JPanel systemPanel = new JPanel (new BorderLayout ());
		final JTable systemTable = new JTable (
		new Object[][]{
			System.getProperties ().keySet ().toArray (),
			System.getProperties ().values ().toArray ()
			}, new Object[]{
				ResourceSupplier.getString (ResourceClass.UI, "about", "property"),
				ResourceSupplier.getString (ResourceClass.UI, "about", "value")
			});
		systemTable.setAutoscrolls (true);
//		systemPanel.add (new JScrollPane (systemTable), BorderLayout.CENTER);
		tabbedPanel.addTab (
			ResourceSupplier.getString (ResourceClass.UI, "about", "system.title"), 
			new JScrollPane (systemTable));
		systemTable.setMaximumSize (new Dimension (100, 50));
		final JPanel mainPanel = new JPanel ();
		mainPanel.setLayout (new BorderLayout ());
		
		final GradientPanel gradientPanel = new GradientPanel (Color.LIGHT_GRAY, GradientPanel.OBLIQUE);
		gradientPanel.setPreferredSize (new Dimension (30, 100));
		mainPanel.add (gradientPanel, java.awt.BorderLayout.WEST);
		
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
		
//		this.addWindowListener (new AboutWindowListener());
	}
	
	private Animator _animator=null;
	
	/**
	 * Mostra la finestra.
	 */
//    private class AboutWindowListener extends WindowAdapter {
//		public void windowActivated(WindowEvent e){
//			/* primo show */
//			AboutBox.this._animator = new Animator(AboutBox.this.introPage.animation(), DEFAULT_FRAME_RATE);
//
////				final Animator animator = _animator;
////				Runnable runnable = new Runnable() {
////					public void run() {
////						if (AboutBox.this.isVisible ()){
////							animator.start();
////						}
////					}
////				};
////
////				AnimationUtils.invokeOnStop(introPage.animation(), runnable);
//
//		AboutBox.this._animator.start();
//		}

//		public void windowDeactivated(WindowEvent e){
//			if(AboutBox.this._animator!=null){
//				AboutBox.this._animator.stop ();
//			}
//		}
//	}
	
	public void show (){
		super.show ();
		this.introPanel.invalidate ();
	}
	
	private JPanel createDetailPanel (){
		
		final Font valueFont = new Font("Default",Font.BOLD,12);

		final JPanel container = new JPanel (new BorderLayout ());
		
		final JPanel detailPanel = new JPanel (new GridBagLayout ());
		
		final ApplicationData appData = ApplicationData.getInstance ();
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 5, 3, 5);
		
//		this.productLogoLabel = new JLabel ();
////		this.productLogoLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "global", "productlogo"));
//		this.productLogoLabel.setText ("");
//		this.productLogoImage = ResourceSupplier.getImageIcon (ResourceClass.UI, "productlogosmall.jpg");
//		this.productLogoLabel.setIcon (this.productLogoImage);
		
//		c.weightx = 0.0;
//		c.weighty = 0.0;
//		c.gridx = 0;
//		c.gridy = 0;
//		c.gridwidth = 1;
//		c.anchor = 0;
//		detailPanel.add (this.productLogoLabel, c);
//		container.add (this.productLogoLabel, BorderLayout.WEST);
		{
//			final JLabel detailTitle = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "about", "detail.title"));
//			detailTitle.setHorizontalAlignment (JLabel.CENTER);
//			detailTitle.setFont (new java.awt.Font ("Default", Font.BOLD, 12));

			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 2;
			
			final TopBorderPane detailTitle = new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "about", "detail.title"));
			detailPanel.add (detailTitle, c);
		}
		
		{
			final JLabel productVersionLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "about", "product.version")+": ");
			productVersionLabel.setHorizontalAlignment (JLabel.RIGHT);
			
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			detailPanel.add (productVersionLabel, c);
		}
		{
			final JLabel productVersionValue = new JLabel (appData.getVersionNumber ());
			productVersionValue.setFont (valueFont);

			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
			detailPanel.add (productVersionValue, c);
		}
		
		{
			final JLabel productReleaseDateLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "about", "product.releasedate")+": ");
			productReleaseDateLabel.setHorizontalAlignment (JLabel.RIGHT);

			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 1;
			detailPanel.add (productReleaseDateLabel, c);
		}
		{
			final JLabel productReleaaseDateValue = new JLabel (CalendarUtils.toTSString (appData.getReleaseDate ().getTime ()));
			productReleaaseDateValue.setFont (valueFont);

			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 1;
			c.fill = GridBagConstraints.BOTH;
			detailPanel.add (productReleaaseDateValue, c);
		}
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		detailPanel.add (new JLabel (), c);
		
		{
			final JLabel productBuildLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "about", "product.build")+": ");
			productBuildLabel.setHorizontalAlignment (JLabel.RIGHT);

			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridx = 0;
			c.gridy = 4;
			c.gridwidth = 1;
			detailPanel.add (productBuildLabel, c);
		}
		{
			final JLabel productBuildValue = new JLabel (appData.getBuildNumber ());
			productBuildValue.setFont (valueFont);

			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 1;
			c.gridy = 4;
			c.gridwidth = 1;
			detailPanel.add (productBuildValue, c);
		}
		
		{
			final JLabel productBuildDateLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "about", "product.builddate")+": ");
			productBuildDateLabel.setHorizontalAlignment (JLabel.RIGHT);

			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridx = 0;
			c.gridy = 5;
			c.gridwidth = 1;
			detailPanel.add (productBuildDateLabel, c);
		}
		{
			final JLabel productBuildDateValue = new JLabel (CalendarUtils.toTSString (appData.getBuildDate ().getTime ()));
			productBuildDateValue.setFont (valueFont);

			c.weightx = 1.0;
			c.weighty = 0.0;
			c.gridx = 1;
			c.gridy = 5;
			c.gridwidth = 1;
			c.fill = GridBagConstraints.BOTH;
			detailPanel.add (productBuildDateValue, c);
		}
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 6;
		detailPanel.add (new JLabel (), c);
		
		container.add (detailPanel, BorderLayout.CENTER);
		return container;
	}
}

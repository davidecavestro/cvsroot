/*
 * ReportsFrame.java
 *
 * Created on 29 gennaio 2005, 20.57
 */

package com.ost.timekeeper.report;

import com.ost.timekeeper.help.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * FOrnisce l'interfaccia utente per la generazione dei report.
 *
 * @author  davide
 */
public class ReportsFrame extends JFrame {
	
	/**
	 * Il formato di output del report generato.
	 */
	private String _reportOutputType;
	
	/**
	 * Il tipo di report generato.
	 */
	private String _reportFlavor;
	
	/**
	 * L'istanza del singleton.
	 */
	private static ReportsFrame _instance;
	
	/**
	 * Tipo di report che mappa {@link com.ost.timekeeper.report.flavors.SimpleProgresses}.
	 */
	private final static String SIMPLE_PROGRESSES = "simple_progresses";
	
	/** Costruttore. */
	public ReportsFrame () {
		initComponents ();
	}
	
	/**
	 * Ritorna l'istanza di questo frame, dopo averlo istanziato, se necessario.
	 *
	 * @return l'istanza di questo frame.
	 */	
	public static ReportsFrame getInstance (){
		if (_instance==null){
			_instance= new ReportsFrame ();
		}
		return _instance;
	}
	
	/**
	 * Inizializza le componenti di questa finestra.
	 */
	private void initComponents (){
		this.setTitle (ResourceSupplier.getString (ResourceClass.UI,"controls", "reports"));
		final JPanel mainPanel = new JPanel ();
		mainPanel.setLayout (new BorderLayout ());
		
		/* inizializza help contestuale */
		javax.help.CSH.setHelpIDString (this, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.REPORTGENERATIONDIALOG ));
		
		final JLabel bannerLabel = new JLabel ("");
		final ImageIcon banner = ResourceSupplier.getImageIcon (ResourceClass.UI, "report-banner.gif", true);
		
		bannerLabel.setIcon (banner);
		mainPanel.add (bannerLabel, BorderLayout.WEST);
		
		final JTabbedPane tabbedPanel = new JTabbedPane (JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		
		final JPanel reportFlavorPanel = createFlavorPanel ();
		tabbedPanel.addTab (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.flavor"),
			ResourceSupplier.getImageIcon (ResourceClass.UI, "report-flavor.gif"),
			reportFlavorPanel,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.flavor.hint")			
			);
		
		final JPanel filterPanel = createFilterPanel ();
		tabbedPanel.addTab (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "filters"),
			ResourceSupplier.getImageIcon (ResourceClass.UI, "report-filters.gif"),
			filterPanel,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.filters.hint")			
			);
		
		final JPanel reportTypePanel = createReportTypePanel ();
		tabbedPanel.addTab (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "output"),
			ResourceSupplier.getImageIcon (ResourceClass.UI, "report-type.gif"),
			reportTypePanel,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.hint")			
			);
		
		mainPanel.add (tabbedPanel, BorderLayout.CENTER);
		
		mainPanel.add (createButtonPanel (), BorderLayout.SOUTH);
		
		this.getContentPane ().add (mainPanel);
		this.pack ();
		
		this.setIconImage (ResourceSupplier.getImageIcon (ResourceClass.UI, "reports-frame.gif").getImage ());
		
		/*
		 * Centra sullo schermo.
		 */
		this.setLocationRelativeTo (null);
	}
	
	/**
	 * Istanzia, inizializza e ritorna il pannello contenente i controlli per 
	 * la gestione della tipologia di report.
	 *
	 * @return il pannello per la gestione della tipologia del report.
	 */	
	private JPanel createFlavorPanel (){
		final JPanel thePanel = new JPanel ();
		thePanel.setLayout (new GridBagLayout ());
		
		thePanel.setBorder (new TitledBorder (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.flavor.choose")));
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);

		final ButtonGroup buttonGroup = new ButtonGroup();
		
		{
			/* scelta default */
			final JRadioButton simpleProgressesChoice = new JRadioButton (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "report.flavor.simpleprogresses"), true
				);
			
			simpleProgressesChoice.setMnemonic (KeyEvent.VK_S);
			
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			thePanel.add (simpleProgressesChoice, c);
			
			buttonGroup.add (simpleProgressesChoice);
			
			//@@@ temporaneo @@@
			simpleProgressesChoice.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					_reportFlavor = SIMPLE_PROGRESSES;
				}
			});
			/* inizializza campo interno */
			_reportFlavor = SIMPLE_PROGRESSES;
			
		}
		
		{
			final JRadioButton treeProgressesChoice = new JRadioButton (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "report.flavor.treeprogresses")
				);
			
			treeProgressesChoice.setMnemonic (KeyEvent.VK_T);
			
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			thePanel.add (treeProgressesChoice, c);
			
			buttonGroup.add (treeProgressesChoice);
		}
				
		return thePanel;
	}
	
	/**
	 * Istanzia, inizializza e ritorna il pannello contenente i controlli per la gestione dei filtri.
	 *
	 * @return il pannello per la gestione dei filtri.
	 */	
	private JPanel createFilterPanel (){
		final JPanel thePanel = new JPanel ();
		thePanel.setLayout (new GridBagLayout ());
		
		thePanel.setBorder (new TitledBorder (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.filter.choose")));
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);

//		final JLabel descriptionLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.filter.choose"));
//		c.gridx = 0;
//		c.gridy = 0;
//		c.gridwidth = 2;
//		thePanel.add (descriptionLabel, c);
		
		return thePanel;
	}
	
	/**
	 * Istanzia, inizializza e ritorna il pannello contenente i controlli per 
	 * la gestione del tipo di report.
	 *
	 * @return il pannello per la gestione del tipo di report.
	 */	
	private JPanel createReportTypePanel (){
		
		final ActionListener al = new ActionListener (){
			public void actionPerformed (ActionEvent e) {
				ReportsFrame.this._reportOutputType = e.getActionCommand ();
			}
			
		};
		final JPanel thePanel = new JPanel ();
		thePanel.setLayout (new GridBagLayout ());
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);

//		final JLabel descriptionLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.choose"));
//		c.gridx = 0;
//		c.gridy = 0;
//		c.gridwidth = 2;
//		thePanel.add (descriptionLabel, c);
		
		thePanel.setBorder (new TitledBorder (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.choose")));
		
		final ButtonGroup buttonGroup = new ButtonGroup();
		
		{
			/* scelta default */
			final JRadioButton choice = new JRadioButton (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.view")/*,
				ResourceSupplier.getImageIcon (ResourceClass.UI, "report-type-pdf.gif")*/, true
				);
			
			choice.setMnemonic (KeyEvent.VK_V);
			choice.setActionCommand (JRBindings.TASK_VIEW);
			choice.addActionListener (al);
			
			/* inizializza campo interno */
			_reportOutputType = JRBindings.TASK_VIEW;
			
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 1;
			thePanel.add (choice, c);
			
			buttonGroup.add (choice);
		}
		
		{
			final JRadioButton choice = new JRadioButton (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.pdf")/*,
				ResourceSupplier.getImageIcon (ResourceClass.UI, "report-type-pdf.gif")*/
				);
			
			choice.setMnemonic (KeyEvent.VK_P);
			choice.setActionCommand (JRBindings.TASK_PDF);
			choice.addActionListener (al);
			
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			thePanel.add (choice, c);
			
			buttonGroup.add (choice);
		}
		
		{
			final JRadioButton choice = new JRadioButton (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.html")/*,
				ResourceSupplier.getImageIcon (ResourceClass.UI, "report-type-pdf.gif")*/
				);
			
			choice.setMnemonic (KeyEvent.VK_H);
			choice.setActionCommand (JRBindings.TASK_HTML);
			choice.addActionListener (al);
			
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 1;
			thePanel.add (choice, c);
			
			buttonGroup.add (choice);
		}
		
		{
			final JRadioButton choice = new JRadioButton (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.xml")/*,
				ResourceSupplier.getImageIcon (ResourceClass.UI, "report-type-pdf.gif")*/
				);
			
			choice.setMnemonic (KeyEvent.VK_X);
			choice.setActionCommand (JRBindings.TASK_XML);
			choice.addActionListener (al);
			
			
			c.gridx = 0;
			c.gridy = 3;
			c.gridwidth = 1;
			thePanel.add (choice, c);
			
			buttonGroup.add (choice);
		}
		
		{
			final JRadioButton choice = new JRadioButton (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.csv")/*,
				ResourceSupplier.getImageIcon (ResourceClass.UI, "report-type-pdf.gif")*/
				);
			
			choice.setMnemonic (KeyEvent.VK_C);
			choice.setActionCommand (JRBindings.TASK_CSV);
			choice.addActionListener (al);
			
			
			c.gridx = 0;
			c.gridy = 4;
			c.gridwidth = 1;
			thePanel.add (choice, c);
			
			buttonGroup.add (choice);
		}
		
		{
			final JRadioButton choice = new JRadioButton (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.xls")/*,
				ResourceSupplier.getImageIcon (ResourceClass.UI, "report-type-pdf.gif")*/
				);
			
			choice.setMnemonic (KeyEvent.VK_L);
			choice.setActionCommand (JRBindings.TASK_XLS);
			choice.addActionListener (al);
			
			
			c.gridx = 0;
			c.gridy = 5;
			c.gridwidth = 1;
			thePanel.add (choice, c);
			
			buttonGroup.add (choice);
		}
		
		return thePanel;
	}

	/**
	 * Istanzia, inizializza e ritorna il pannello contenente i pulsanti di lancio e chiusura.
	 *
	 * @return il pannello contenente i pulsanti.
	 */	
	private JPanel createButtonPanel (){
		final JPanel buttonPanel = new JPanel ();
		buttonPanel.setLayout (new java.awt.FlowLayout ());
		
		{	
			final JButton launchButton = new JButton ();
			/*
			 * Configurazione pulsante LANCIA.
			 */
			launchButton.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent e){
					launchReport ();
				}
			});
			launchButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "launch"));
			buttonPanel.add (launchButton);
		}		
		{
			final JButton closeButton = new JButton ();
			/*
			 * Configurazione pulsante CHIUDI.
			 */
			closeButton.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent e){
					ReportsFrame.this.hide ();
				}
			});
			closeButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "close"));
			
			buttonPanel.add (closeButton);
		}
		
		{
			final DirectHelpButton directHelpButton = new DirectHelpButton ();

			buttonPanel.add (directHelpButton);
		}
		
		return buttonPanel;
	}
	
	private void launchReport (){
		if (this._reportFlavor!=null){
			if (this._reportFlavor.equals(SIMPLE_PROGRESSES)){
				final com.ost.timekeeper.Application app = com.ost.timekeeper.Application.getInstance ();

				final com.ost.timekeeper.report.DataExtractor extractor = new com.ost.timekeeper.report.flavors.SimpleProgresses (com.ost.timekeeper.Application.getInstance ().getSelectedItem (), new com.ost.timekeeper.report.filter.TargetedFilterContainer[]{});
				final com.ost.timekeeper.report.ReportPreferences prefs = new com.ost.timekeeper.report.ReportPreferences (null);
				final JRBindings jrb = new JRBindings ("/usr/local/share/devel/jtimekeeper/src/report/plainprogresses.jasper", "/plainprogresses/progress", ReportsFrame.this._reportOutputType);
				new com.ost.timekeeper.report.ReportDataGenerator().generate (extractor, prefs, jrb);
			}
		}
	}
}

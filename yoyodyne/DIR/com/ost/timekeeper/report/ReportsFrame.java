/*
 * ReportsFrame.java
 *
 * Created on 29 gennaio 2005, 20.57
 */

package com.ost.timekeeper.report;

import com.ost.timekeeper.*;
import com.ost.timekeeper.help.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.report.filter.*;
import com.ost.timekeeper.report.filter.flavors.date.*;
import com.ost.timekeeper.report.flavors.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.ui.support.GradientPanel;
import com.ost.timekeeper.util.*;
import com.toedter.calendar.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.EventListenerList;

/**
 * FOrnisce l'interfaccia utente per la generazione dei report.
 *
 * @author  davide
 */
public class ReportsFrame extends JFrame implements Observer, FlavorModel, FlavorListener{
	
	/**
	 * Il formato di output del report generato.
	 */
	private String _reportOutputType;
	
	/**
	 * L'istanza del singleton.
	 */
	private static ReportsFrame _instance;
	
	private ReportManager _reportManager;
	private JTabbedPane _tabbedPanel;
	
	private final ReportManager _plainProgressesReportManager = new PlainProgressesReportManager ();
	private final ReportManager _cumulateProgressesReportManager = new CumulateProgressesReportManager ();
	private final ReportManager _cumulateLocalProgressesReportManager = new CumulateLocalProgressesReportManager ();
	
	/** Costruttore. */
	public ReportsFrame () {
		initComponents ();
		this.addFlavorListener (this);
		/* inizializza campo interno */
		this._flavorModelImpl.setFlavor (SIMPLE_PROGRESSES);
		pack ();
	}
	
	/**
	 * Ritorna l'istanza di questo frame, dopo averlo istanziato, se necessario.
	 *
	 * @return l'istanza di questo frame.
	 */
	public static ReportsFrame getInstance (){
		if (_instance==null){
			_instance= new ReportsFrame ();
			Application.getInstance ().addObserver (_instance);
		}
		return _instance;
	}
	
	/**
	 * Inizializza le componenti di questa finestra.
	 */
	private void initComponents (){
		this.setTitle (ResourceSupplier.getString (ResourceClass.UI,"controls", "reports"));
		final JPanel mainPanel = new JPanel (new BorderLayout ());
		
		/* inizializza help contestuale */
		javax.help.CSH.setHelpIDString (this, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.REPORTGENERATIONDIALOG ));
		
		ImageIcon banner = null;
		try {
			banner = ResourceSupplier.getImageIcon (ResourceClass.UI, "report-banner.gif", false);
		} catch (MissingResourceException mre){}
		if (banner!=null){
			/* risorsa banner disponibile */
			final JLabel bannerLabel = new JLabel ("");
			
			bannerLabel.setIcon (banner);
			mainPanel.add (bannerLabel, BorderLayout.WEST);
		} else {
			/* risorsa banner non disponibile */
			final GradientPanel gradientPanel = new GradientPanel (Color.LIGHT_GRAY, GradientPanel.OBLIQUE);
			gradientPanel.setPreferredSize (new Dimension (30, 120));
			mainPanel.add (gradientPanel, BorderLayout.WEST);
		}
		
		this._tabbedPanel = new JTabbedPane (JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		final JPanel reportFlavorPanel = createFlavorPanel ();
		_tabbedPanel.addTab (
		ResourceSupplier.getString (ResourceClass.UI, "controls", "report.flavor"),
		ResourceSupplier.getImageIcon (ResourceClass.UI, "report-flavor.png"),
		reportFlavorPanel,
		ResourceSupplier.getString (ResourceClass.UI, "controls", "report.flavor.hint")
		);
		
		//		final JPanel filterPanel = createFilterPanel ();
		_tabbedPanel.addTab ("", new JPanel ());
		//		ResourceSupplier.getString (ResourceClass.UI, "controls", "filters"),
		//		ResourceSupplier.getImageIcon (ResourceClass.UI, "reports-filter.png"),
		//		filterPanel,
		//		ResourceSupplier.getString (ResourceClass.UI, "controls", "report.filters.hint")
		//		);
		
		final JPanel reportTypePanel = createReportTypePanel ();
		_tabbedPanel.addTab (
		ResourceSupplier.getString (ResourceClass.UI, "controls", "output"),
		ResourceSupplier.getImageIcon (ResourceClass.UI, "mime.png"),
		reportTypePanel,
		ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.hint")
		);
		
		mainPanel.add (_tabbedPanel, BorderLayout.CENTER);
		
		mainPanel.add (createButtonPanel (), BorderLayout.SOUTH);
		
		this.getContentPane ().add (mainPanel);
		this.pack ();
		//		mainPanel.setMinimumSize (new Dimension (400, 150));
		
		this.setIconImage (ResourceSupplier.getImageIcon (ResourceClass.UI, "reports-frame.png").getImage ());
		
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
		
		final GridBagConstraints c = new GridBagConstraints ();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
		
		final ButtonGroup buttonGroup = new ButtonGroup ();
		
		{
			/* scelta default */
			final JRadioButton simpleProgressesChoice = new JRadioButton (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "Job.progresses"), true
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
					ReportsFrame.this._flavorModelImpl.setFlavor (SIMPLE_PROGRESSES);
				}
			});
			
		}
		
		//		{
		//			final JRadioButton treeProgressesChoice = new JRadioButton (
		//			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.flavor.treeprogresses")
		//			);
		//
		//			treeProgressesChoice.setMnemonic (KeyEvent.VK_T);
		//
		//			c.gridx = 0;
		//			c.gridy = 1;
		//			c.gridwidth = 1;
		//			thePanel.add (treeProgressesChoice, c);
		//			/*@todo: abilitare radio scelta report*/
		//			treeProgressesChoice.setEnabled (false);
		//
		//			buttonGroup.add (treeProgressesChoice);
		//		}
		
		{
			final JRadioButton cumulateProgressesChoice = new JRadioButton (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "Tree.cumulated.effort.in.period")
			);
			
			cumulateProgressesChoice.setMnemonic (KeyEvent.VK_C);
			
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			thePanel.add (cumulateProgressesChoice, c);
			
			buttonGroup.add (cumulateProgressesChoice);
			cumulateProgressesChoice.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					ReportsFrame.this._flavorModelImpl.setFlavor (CUMULATE_PROGRESSES);
				}
			});
		}
		
		{
			final JRadioButton cumulateLocalProgressesChoice = new JRadioButton (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "Job.effort.in.period")
			);
			
			cumulateLocalProgressesChoice.setMnemonic (KeyEvent.VK_L);
			
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 1;
			thePanel.add (cumulateLocalProgressesChoice, c);
			
			buttonGroup.add (cumulateLocalProgressesChoice);
			cumulateLocalProgressesChoice.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					ReportsFrame.this._flavorModelImpl.setFlavor (CUMULATE_LOCAL_PROGRESSES);
				}
			});
		}
		
		/* etichetta vuota per riemire spazio */
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		thePanel.add (new JLabel (), c);
		
		return thePanel;
	}
	
	/**
	 * Istanzia, inizializza e ritorna il pannello contenente i controlli per la gestione dei filtri.
	 *
	 * @return il pannello per la gestione dei filtri.
	 */
	//	private JPanel createFilterPanel (){
	//		final JPanel thePanel = new JPanel ();
	//		thePanel.setLayout (new GridBagLayout ());
	//
	//		final GridBagConstraints c = new GridBagConstraints ();
	//		c.fill = GridBagConstraints.BOTH;
	//		c.anchor = GridBagConstraints.FIRST_LINE_START;
	//		c.insets = new Insets (3, 10, 3, 10);
	//
	//		{
	//			c.gridx = 0;
	//			c.gridy = 0;
	//			c.gridwidth = 3;
	//			final TopBorderPane borderPanel = new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.filter.structure"));
	//			//			borderPanel.setFont(new java.awt.Font("Dialog", 3, 12));
	//			//			borderPanel.setForeground(new java.awt.Color(110, 88, 195));
	//			thePanel.add (borderPanel, c);
	//		}
	//
	//		reportRootLabel.setEnabled (false);
	//		{
	//			final JCheckBox rootCheckBox = new JCheckBox (ResourceSupplier.getString (ResourceClass.UI, "controls", "filter.root.enable"));
	//			c.gridx = 0;
	//			c.gridy = 1;
	//			c.gridwidth = 1;
	//			thePanel.add (rootCheckBox, c);
	//
	//			final JLabel rootDescriptionLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.root.item")+": ");
	//			rootDescriptionLabel.setEnabled (false);
	//
	//			rootCheckBox.addActionListener (new ActionListener (){
	//				public void actionPerformed (ActionEvent ae){
	//					final boolean isSelected = rootCheckBox.getModel ().isSelected ();
	//					ReportsFrame.this.rootFilterEnabled = isSelected;
	//					reportRootLabel.setEnabled (isSelected);
	//					rootDescriptionLabel.setEnabled (isSelected);
	//				}
	//			});
	//
	//			final JButton rootSelectionButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.root"));
	//			rootSelectionButton.addActionListener (new java.awt.event.ActionListener () {
	//				public void actionPerformed (java.awt.event.ActionEvent evt) {
	//					final ProgressItemTreeSelector.UserChoice choice = ProgressItemTreeSelector.supplyProgressItem (
	//					ResourceSupplier.getString (ResourceClass.UI, "controls", "progressitem.choice"),
	//					ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.report.root"), true, HelpResource.PLAINPROGRESSREPORT_ROOTSELECTION_DIALOG);
	//					if (choice.isConfirmed ()){
	//						ReportsFrame.this._root = choice.getChoice ();
	//						ReportsFrame.this.reportRootLabel.setText (ReportsFrame.this._root!=null?ReportsFrame.this._root.getName ():"");
	//					}
	//				}
	//			});
	//			thePanel.add (rootSelectionButton, c);
	//
	//			c.gridx = 0;
	//			c.gridy = 2;
	//			c.gridwidth = 1;
	//			thePanel.add (rootDescriptionLabel, c);
	//			c.gridx = 1;
	//			c.gridy = 2;
	//			c.gridwidth = 1;
	//			thePanel.add (reportRootLabel, c);
	//			c.gridx = 2;
	//			c.gridy = 2;
	//			c.gridwidth = 1;
	//			thePanel.add (rootSelectionButton, c);
	//		}
	//
	//		{
	//			c.gridx = 0;
	//			c.gridy = 3;
	//			c.gridwidth = 3;
	//			final TopBorderPane borderPanel = new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.filter.time"));
	//			//			borderPanel.setFont(new java.awt.Font("Dialog", 3, 12));
	//			//			borderPanel.setForeground(new java.awt.Color(110, 88, 195));
	//			thePanel.add (borderPanel, c);
	//		}
	//
	//		{
	//			final JDateChooser fromDateChooser = new JDateChooser ();
	//
	//			c.weightx = 1.0;
	//			//			c.weighty = 1.0;
	//
	//			final JCheckBox fromCheckBox = new JCheckBox (ResourceSupplier.getString (ResourceClass.UI, "controls", "filter.from.enable"));
	//			c.gridx = 0;
	//			c.gridy = 4;
	//			c.gridwidth = 1;
	//			thePanel.add (fromCheckBox, c);
	//
	//			/* disabilita pannello contenente editor date */
	//			fromCheckBox.addActionListener (new ActionListener (){
	//				public void actionPerformed (ActionEvent ae){
	//					final boolean isSelected = fromCheckBox.getModel ().isSelected ();
	//					fromDateChooser.setEnabled (isSelected);
	//					ReportsFrame.this.fromFilterEnabled = isSelected;
	//				}
	//			});
	//
	//			fromDateChooser.addPropertyChangeListener (new DateChooserHelper ());
	//
	//			c.gridx = 0;
	//			c.gridy = 5;
	//			c.gridwidth = 1;
	//
	//			//			fromFilterPanel.add (fromDateChooser);
	//			thePanel.add (fromDateChooser, c);
	//
	//			fromDateChooser.setEnabled (false);
	//			fromDateChooser.addPropertyChangeListener ("date", new PropertyChangeListener (){
	//				public void propertyChange (PropertyChangeEvent evt){
	//					ReportsFrame.this.fromFilter = new Date (((Date)evt.getNewValue ()).getTime ());
	//					ReportsFrame.this.fromFilter.setHours (0);
	//					ReportsFrame.this.fromFilter.setMinutes (0);
	//					ReportsFrame.this.fromFilter.setSeconds (0);
	//				}
	//			});
	//		}
	//
	//		{
	//			final JDateChooser toDateChooser = new JDateChooser ();
	//
	//			final JCheckBox toCheckBox = new JCheckBox (ResourceSupplier.getString (ResourceClass.UI, "controls", "filter.to.enable"));
	//			c.gridx = 1;
	//			c.gridy = 4;
	//			c.gridwidth = 1;
	//			thePanel.add (toCheckBox, c);
	//
	//			/* disabilita pannello contenente editor date */
	//			toCheckBox.addActionListener (new ActionListener (){
	//				public void actionPerformed (ActionEvent ae){
	//					final boolean isSelected = toCheckBox.getModel ().isSelected ();
	//					toDateChooser.setEnabled (isSelected);
	//					ReportsFrame.this.toFilterEnabled = isSelected;
	//				}
	//			});
	//
	//			toDateChooser.addPropertyChangeListener (new DateChooserHelper ());
	//
	//			c.gridx = 1;
	//			c.gridy = 5;
	//			c.gridwidth = 1;
	//
	//			//			toFilterPanel.add (toDateChooser);
	//			thePanel.add (toDateChooser, c);
	//
	//			toDateChooser.setEnabled (false);
	//			toDateChooser.addPropertyChangeListener ("date", new PropertyChangeListener (){
	//				public void propertyChange (PropertyChangeEvent evt){
	//					ReportsFrame.this.toFilter = new Date (((Date)evt.getNewValue ()).getTime ());
	//					ReportsFrame.this.toFilter.setHours (0);
	//					ReportsFrame.this.toFilter.setMinutes (0);
	//					ReportsFrame.this.toFilter.setSeconds (0);
	//				}
	//			});
	//		}
	//
	//		/* etichetta vuota per riemire spazio */
	//		c.weightx = 1.0;
	//		c.weighty = 1.0;
	//		c.gridx = 0;
	//		c.gridy = 6;
	//		c.gridwidth = 3;
	//		thePanel.add (new JLabel (), c);
	//
	//		return thePanel;
	//	}
	
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
		
		final GridBagConstraints c = new GridBagConstraints ();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
		
		//		final JLabel descriptionLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.choose"));
		//		c.gridx = 0;
		//		c.gridy = 0;
		//		c.gridwidth = 2;
		//		thePanel.add (descriptionLabel, c);
		
		thePanel.setBorder (new TitledBorder (ResourceSupplier.getString (ResourceClass.UI, "controls", "report.type.choose")));
		
		final ButtonGroup buttonGroup = new ButtonGroup ();
		
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
		
		/* etichetta vuota per riemire spazio */
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		thePanel.add (new JLabel (), c);
		
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
		} {
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
		final com.ost.timekeeper.Application app = com.ost.timekeeper.Application.getInstance ();
		if (app.getProject ()==null){
			JOptionPane.showMessageDialog (this, ResourceSupplier.getString (ResourceClass.UI,"controls", "application.no.project.loaded"));
			return;
		}
		
		final DataExtractor extractor = this._reportManager.initExtractor ();
		
		
		if (this.getFlavor ().equals (SIMPLE_PROGRESSES)){
			
			final com.ost.timekeeper.report.ReportPreferences prefs = new com.ost.timekeeper.report.ReportPreferences (null);
			final JRBindings jrb = new JRBindings (this.getClass ().getResourceAsStream ("plainprogresses.jasper"), "/plainprogresses/progress", ReportsFrame.this._reportOutputType);
			
			new com.ost.timekeeper.report.ReportDataGenerator ().generate (extractor, prefs, jrb);
		} else if (this.getFlavor ().equals (CUMULATE_PROGRESSES)){
			
			//				final java.util.List filters = new ArrayList ();
			//				if (this.fromFilterEnabled && this.fromFilter!=null){
			//					filters.add (new com.ost.timekeeper.report.filter.TargetedFilterContainer (SimpleProgresses.PROGRESS_FROM, new NegateFilter (new BeforeDateFilter (this.fromFilter))));
			//				}
			//				if (this.toFilterEnabled && this.toFilter!=null){
			//					filters.add (new com.ost.timekeeper.report.filter.TargetedFilterContainer (SimpleProgresses.PROGRESS_FROM, new NegateFilter (new AfterDateFilter (this.toFilter))));
			//				}
			//
			//				final com.ost.timekeeper.report.DataExtractor extractor = new com.ost.timekeeper.report.flavors.CumulateProgresses (
			//				this.rootFilterEnabled && this._root!=null?this._root:app.getProject ().getRoot (),
			//				(com.ost.timekeeper.report.filter.TargetedFilterContainer[])filters.toArray (targetedFilterArray),
			//				new Date (), 1, 7);
			final com.ost.timekeeper.report.ReportPreferences prefs = new com.ost.timekeeper.report.ReportPreferences (null);
			final JRBindings jrb = new JRBindings (this.getClass ().getResourceAsStream ("cumulateprogresses.jasper"), "/cumulateprogresses/period", ReportsFrame.this._reportOutputType);
			
			new com.ost.timekeeper.report.ReportDataGenerator ().generate (extractor, prefs, jrb);
		} else if (this.getFlavor ().equals (CUMULATE_LOCAL_PROGRESSES)){
			final com.ost.timekeeper.report.ReportPreferences prefs = new com.ost.timekeeper.report.ReportPreferences (null);
			final JRBindings jrb = new JRBindings (this.getClass ().getResourceAsStream ("cumulatelocalprogresses.jasper"), "/cumulatelocalprogresses/period/progressitem", ReportsFrame.this._reportOutputType);
			
			new com.ost.timekeeper.report.ReportDataGenerator ().generate (extractor, prefs, jrb);
		}
	}
	
	public void update (Observable o, Object arg) {
		//		if (o instanceof Application){
		//			if (arg!=null && arg.equals (ObserverCodes.SELECTEDITEMCHANGE)){
		//				final ProgressItem selectedItem =  Application.getInstance ().getSelectedItem ();
		//				this.reportRootLabel.setText (selectedItem!=null?selectedItem.getName ():"");
		//			}
		//		}
	}
	
	private final FlavorModelImpl _flavorModelImpl = new FlavorModelImpl ();
	
	private final class FlavorModelImpl extends AbstractFlavorModel {
		
		/**
		 * Il tipo di report generato.
		 */
		private String _reportFlavor;
		
		public String getFlavor () {
			return this._reportFlavor;
		}
		
		public void setFlavor (final String flavor) {
			final String oldValue = this._reportFlavor;
			this._reportFlavor = flavor;
			if (oldValue!=flavor && (oldValue==null || (oldValue!=null && flavor!=null && !oldValue.equals (flavor)))){
				this.fireFlavorChanged ();
			}
		}
		public void flavorChanged (com.ost.timekeeper.report.flavors.FlavorEvent flavorEvent) {
			final String flavor = flavorEvent.getFlavor ();
			
			if (flavor.equals (SIMPLE_PROGRESSES)){
				ReportsFrame.this._reportManager = _plainProgressesReportManager;
			} else if (flavor.equals (CUMULATE_PROGRESSES)){
				ReportsFrame.this._reportManager = _cumulateProgressesReportManager;
			} else if (flavor.equals (CUMULATE_LOCAL_PROGRESSES)){
				ReportsFrame.this._reportManager = _cumulateLocalProgressesReportManager;
			}
			
			ReportsFrame.this._tabbedPanel.removeTabAt (1);
			
			ReportsFrame.this._tabbedPanel.insertTab (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "filters"),
			ResourceSupplier.getImageIcon (ResourceClass.UI, "reports-filter.png"),
			ReportsFrame.this._reportManager.getConfigurationPanel (),
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.filters.hint"),
			1
			);
			ReportsFrame.this.pack ();
		}
	}
	
	
	/**
	 * Aggiunge un listener che deve essere notificato ad ogni modifica di flavor.
	 *
	 * @param	l		il listener
	 */
	public void addFlavorListener (FlavorListener l){
		this._flavorModelImpl.addFlavorListener (l);
	}
	
	/**
	 * Rimuove un listener registrato du questo modello.
	 *
	 * @param l il listenenr da rimuovere.
	 */
	public void removeFlavorListener (FlavorListener l) {
		this._flavorModelImpl.removeFlavorListener (l);
	}
	
	public String getFlavor () {
		return this._flavorModelImpl.getFlavor ();
	}
	
	public void flavorChanged (com.ost.timekeeper.report.flavors.FlavorEvent flavorEvent) {
		this._flavorModelImpl.flavorChanged (flavorEvent);
	}
	
	
}

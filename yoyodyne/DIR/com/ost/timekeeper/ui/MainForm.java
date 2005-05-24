/*
 * MainForm.java
 *
 * Created on 22 febbraio 2004, 10.56
 */

package com.ost.timekeeper.ui;

import com.jgoodies.plaf.Options;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.windows.ExtWindowsLookAndFeel;
import com.jgoodies.uif_lite.panel.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.help.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.report.*;
import com.ost.timekeeper.ui.chart.ChartFrame;
import com.ost.timekeeper.ui.support.JToolButton;
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;
import com.toedter.components.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.*;
import javax.help.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

/**
 * Finestra principale dell'applicazione.
 *
 * @author  davide
 * @todo implementare UNDO/REDO
 */
public final class MainForm extends javax.swing.JFrame implements Observer {
	
	private Application application;
	/**
	 * La lista degli elementi di menu che gestiscono la scelta del LookAndFeel
	 */
	private final java.util.List lookAndFeelMenuItems = new ArrayList ();
	
	/**
	 * Costruttore.
	 *
	 * @param app il dispatcher principale.
	 */
	public MainForm (Application app) {
		this.application = app;
		this.progressTreeModel = new ProgressTreeModel (application.getProject ());
		this.application.addObserver (progressTreeModel);
		
		ApplicationOptionsNotifier.getInstance ().addObserver (this);
		
		// Set the JGoodies Plastic 3D look and feel
		loadCustomLookAndFeels ();
		
		initComponents ();
		//inizializza table model su dati applicazione
		ProgressListFrame.getInstance ().getProgressTable ().getProgressTableModel ().load (application.getCurrentItem ());
		postInitComponents ();
		
		setBounds (Application.getInstance ().getOptions ().getMainFormBounds ());
		JFrame.setDefaultLookAndFeelDecorated (true);
		
		updateLookAndFeel (Application.getOptions ().getLookAndFeel ());

	}
	
	/**
	 * Installs the Kunststoff and Plastic Look And Feels if available in classpath.
	 */
	public final void loadCustomLookAndFeels () {
		// if in classpath thry to load JGoodies Plastic Look & Feel
		try {
			UIManager.installLookAndFeel ("JGoodies Plastic 3D",
			"com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
			//            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
			//			SwingUtilities.updateComponentTreeUI(MainForm.this);
		} catch (Throwable t) {
//			try {
//				UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
//				
//				//				UIManager.setLookAndFeel(info.getClassName());
//				
//				// update the complete application's
//				// look & feel
//				
//			}  catch (Exception e) {
//				e.printStackTrace ();
//			}
		}
	}
	
	/**
	 * Inizializzaizone componenti.
	 */
	private void initComponents () {
		
		progressTreePopup = new javax.swing.JPopupMenu ();
		mainPanel = new javax.swing.JPanel ();
		mainToolbar = new javax.swing.JToolBar ();
		nodeCreateButton = new JToolButton (ActionPool.getInstance ().getNodeCreateAction ());
		nodeEditButton = new JToolButton (ActionPool.getInstance ().getStartNodeEdit ());
		nodeDeleteButton = new JToolButton (ActionPool.getInstance ().getNodeDeleteAction ());
		startButton = new JToolButton (ActionPool.getInstance ().getProgressStartAction ());
		stopButton = new JToolButton (ActionPool.getInstance ().getProgressStopAction ());
		statusBar = new javax.swing.JPanel ();
		statusLabel = new javax.swing.JLabel ();
		currentDurationLabel = new javax.swing.JLabel ();
		jobProgress = new javax.swing.JProgressBar ();
		//		jLabel3 = new javax.swing.JLabel ();
		jPanelTree = new javax.swing.JPanel ();
		jSplit_Tree_Data = new javax.swing.JSplitPane ();
		progressItemTree = new ProgressItemTree (this.progressTreeModel);
		this.application.addObserver (progressItemTree);
		
		//		dataTabbedPane = new javax.swing.JTabbedPane ();
		desktop = Desktop.getInstance ();
		Color desktopColor = Application.getOptions ().getDesktopColor ();
		if (desktopColor==null) {
			desktopColor = Color.BLACK;
		}
		desktop.setBackground (desktopColor);
		//		desktop.setDragMode (JDesktopPane.LIVE_DRAG_MODE);
		
		
		this.application.addObserver (ProgressListFrame.getInstance ().getProgressTable ());
		menuBar = new javax.swing.JMenuBar ();
		jMenuFile = new javax.swing.JMenu ();
		jMenuProject = new javax.swing.JMenu ();
		jMenuItemNewProject = new javax.swing.JMenuItem ();
		jMenuItemOpen = new javax.swing.JMenuItem ();
		jMenuItemDeleteProject = new javax.swing.JMenuItem ();
		jMenuItemSaveProject = new javax.swing.JMenuItem ();
		jMenuExportProject = new javax.swing.JMenu ();
		jMenuItemXMLProjectExport = new javax.swing.JMenuItem ();
		jMenuImportProject = new javax.swing.JMenu ();
		jMenuItemXMLProjectImport = new javax.swing.JMenuItem ();
		jMenuItemCloseProject = new javax.swing.JMenuItem ();
		jMenuItemFinish = new javax.swing.JMenuItem ();
		jMenuEdit = new javax.swing.JMenu ();
		jMenuItemCut = new javax.swing.JMenuItem ();
		jMenuItemCopy = new javax.swing.JMenuItem ();
		jMenuItemPaste = new javax.swing.JMenuItem ();
		jMenuActions = new javax.swing.JMenu ();
		jMenuItemStart = new javax.swing.JMenuItem ();
		jMenuItemCreateProgress = new javax.swing.JMenuItem ();
		jMenuItemStop = new javax.swing.JMenuItem ();
		jMenuItemCreateNode = new javax.swing.JMenuItem ();
		jMenuItemStartNodeEdit = new javax.swing.JMenuItem ();
		jMenuItemDeleteNode = new javax.swing.JMenuItem ();
		jMenuItemTreeExpandCollapse = new javax.swing.JMenuItem ();
		jMenuTools = new javax.swing.JMenu ();
		jMenuItemOptions = new javax.swing.JMenuItem ();
		jMenuItemReports = new javax.swing.JMenuItem ();
		jMenuItemDataStoreWizard = new javax.swing.JMenuItem ();
		jMenuHelp = new javax.swing.JMenu ();
		jMenuItemAbout = new javax.swing.JMenuItem ();
		jMenuItemHelp = new javax.swing.JMenuItem ();
		jMenuItemContextualHelp = new javax.swing.JMenuItem ();
		
		setDefaultCloseOperation (javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		final ApplicationData appData = ApplicationData.getInstance ();
		setTitle (appData.getApplicationExternalName ());
		setBackground (new java.awt.Color (204, 204, 255));
		setFont (new java.awt.Font ("Default", 0, 10));
		setIconImage (ResourceSupplier.getImageIcon (ResourceClass.UI, "application.png").getImage ());
		setName ("MainWindow");
		addWindowListener (new java.awt.event.WindowAdapter () {
			public void windowClosing (java.awt.event.WindowEvent evt) {
				Application.getInstance ().exit ();
			}
		});
		
		mainPanel.setLayout (new java.awt.BorderLayout ());
		
		//		mainToolbar.setOrientation (JToolBar.VERTICAL);
		mainToolbar.setLayout (new java.awt.FlowLayout ( java.awt.FlowLayout.LEADING));
		//		mainToolbar.setBorder (new javax.swing.border.EtchedBorder ());
		mainToolbar.setRollover (true);
		mainToolbar.setAutoscrolls (true);
		//		mainToolbar.setLayout(new BoxLayout(mainToolbar, BoxLayout.Y_AXIS));
		//		mainToolbar.setAlignmentY(mainToolbar.TOP_ALIGNMENT);
		mainToolbar.putClientProperty ("jgoodies.headerStyle", "Both");
		
        mainToolbar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        // Swing
        mainToolbar.putClientProperty(
            Options.HEADER_STYLE_KEY,
            Boolean.TRUE);
        mainToolbar.putClientProperty(
            PlasticLookAndFeel.BORDER_STYLE_KEY,
            null);
        mainToolbar.putClientProperty(
            ExtWindowsLookAndFeel.BORDER_STYLE_KEY,
            null);
        mainToolbar.putClientProperty(
            PlasticLookAndFeel.IS_3D_KEY,
            Boolean.TRUE);
		
		
		mainToolbar.add (nodeCreateButton);
		
		mainToolbar.add (nodeEditButton);
		
		mainToolbar.add (nodeDeleteButton);
		
		mainToolbar.add (new javax.swing.JSeparator ());
		
		mainToolbar.add (startButton);
		
		mainToolbar.add (stopButton);
		
		mainToolbar.add (new javax.swing.JSeparator ());
		
		
		{
			final JButton reportButton = new JToolButton (ActionPool.getInstance ().getShowReportsDialog ());

			mainToolbar.add (reportButton);
		}
		
		mainToolbar.add (new javax.swing.JSeparator ());
		
		{
			final DirectHelpButton directHelpButton = new DirectHelpButton ();
			JToolButton.makeToolButton (directHelpButton);
			mainToolbar.add (directHelpButton);
		}
		javax.help.CSH.setHelpIDString (mainToolbar, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.MAINTOOLBAR ));
		
		mainPanel.add (mainToolbar, java.awt.BorderLayout.NORTH);
		
		statusBar.setLayout (new javax.swing.BoxLayout (statusBar, javax.swing.BoxLayout.X_AXIS));
		
		//		statusBar.setMinimumSize (new java.awt.Dimension (10, 30));
		statusLabel.setFont (new java.awt.Font ("Default", 0, 12));
		statusLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "statusbar", "statuslabel.idle_UC"));
		statusLabel.setPreferredSize (new Dimension (80, 30));
		
		//		statusLabel.setBorder (new javax.swing.border.LineBorder (new java.awt.Color (0, 0, 0)));
		statusLabel.setBorder (new javax.swing.border.BevelBorder (javax.swing.border.BevelBorder.LOWERED));
		statusLabel.setToolTipText (ResourceSupplier.getString (ResourceClass.UI, "controls", "application.progress.status"));
		statusBar.add (statusLabel);
		
		currentDurationLabel.setFont (new java.awt.Font ("Default", 0, 12));
		currentDurationLabel.setText ("00:00:00");
		currentDurationLabel.setBorder (new javax.swing.border.LineBorder (new java.awt.Color (0, 0, 0)));
		currentDurationLabel.setBorder (new javax.swing.border.BevelBorder (javax.swing.border.BevelBorder.LOWERED));
		currentDurationLabel.setToolTipText (ResourceSupplier.getString (ResourceClass.UI, "controls", "progressing.period.duration"));
		currentDurationLabel.setPreferredSize (new Dimension (70, 30));
		currentDurationLabel.setHorizontalAlignment (SwingConstants.RIGHT);
		statusBar.add (currentDurationLabel);
		
		//		jobProgress.setBorder (new EmptyBorder (0,0,0,0));
		jobProgress.setIndeterminate (false);
		jobProgress.setValue (0);
		//		jobProgress.setPreferredSize (new Dimension (200, 50));
		jobProgress.setVisible (false);
		statusBar.add (jobProgress);
		
		javax.help.CSH.setHelpIDString (statusBar, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.MAINSTATUSBAR ));
		
		mainPanel.add (statusBar, java.awt.BorderLayout.SOUTH);
		
		jPanelTree.setLayout (new java.awt.BorderLayout ());
		
		final JPanel treePanel = new JPanel (new BorderLayout ());
		
		jSplit_Tree_Data.setToolTipText (ResourceSupplier.getString (ResourceClass.UI, "global", "controls.splitter.tooltip"));
		//		progressItemTree.setAutoscrolls (true);
		//		progressItemTree.setPreferredSize (new java.awt.Dimension (100, 200));
/*
		treePanel.add (new SimpleInternalFrame (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "jobs.tree"),
			null, null
			), BorderLayout.NORTH);
 */
		
		final JScrollPane treeScroller = new JScrollPane (progressItemTree);
		treePanel.add (treeScroller, BorderLayout.CENTER);
		
		treeScroller.getViewport ().setBackground (progressItemTree.getBackground ());
		
		/*
		 * mantiene il background allineato con l'albero
		 */
			application.addObserver (new Observer (){
			public void update(Observable o, Object arg){
				if (o instanceof Application){
					if (arg!=null && arg.equals (ObserverCodes.LOOKANDFEEL_CHANGING)){
						treeScroller.getViewport ().setBackground (progressItemTree.getBackground ());
					}
				}				
			}
		});
		
		treePanel.setPreferredSize (new Dimension (180, 200));
		jSplit_Tree_Data.setLeftComponent (treePanel);
		javax.help.CSH.setHelpIDString (progressItemTree, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PROGRESSITEMTREE ));
		
		/*
		 * Gestione doppio click su albero.
		 */
		progressItemTree.addMouseListener (new MouseAdapter (){
			public void mouseClicked (MouseEvent e) {
				if (e.getClickCount ()>1){
					/*
					 * Almeno doppio click.
					 */
					Desktop.getInstance ().bringToTop (ProgressItemInspectorFrame.getInstance ());
				}
			}
		});
		
		final ApplicationOptions options = Application.getInstance ().getOptions ();
		
		/*
		 * Frame lista avanzamenti.
		 */
		final ProgressListFrame progressListFrame = ProgressListFrame.getInstance (); {
			//Set the window's location.
			final Rectangle bounds = options.getProgressListFrameBounds ();
			progressListFrame.setBounds (bounds);
			progressListFrame.setVisible (true); //necessary as of 1.3
			desktop.add (progressListFrame, (int)bounds.getX (), (int)bounds.getY ());
		}
		try {
			progressListFrame.setSelected (true);
		} catch (java.beans.PropertyVetoException e) {}
		javax.help.CSH.setHelpIDString (progressListFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PROGRESSLIST ));
		
		/*
		 * Dettaglio nodo di avanzamento.
		 */
		final ProgressItemInspectorFrame progressItemInspectorFrame = ProgressItemInspectorFrame.getInstance (); {
			//Set the window's location.
			final Rectangle bounds = options.getProgressItemInspectorBounds ();
			progressItemInspectorFrame.setBounds (bounds);
			progressItemInspectorFrame.setVisible (true); //necessary as of 1.3
			desktop.add (progressItemInspectorFrame, (int)bounds.getX (), (int)bounds.getY ());
		}
		try {
			progressItemInspectorFrame.setSelected (true);
		} catch (java.beans.PropertyVetoException e) {}
		javax.help.CSH.setHelpIDString (progressItemInspectorFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PROGRESSITEMINSPECTORFRAME ));
		
		//		dataTabbedPane.addTab (ResourceSupplier.getString (ResourceClass.UI, "controls", "detail")
		//		, new JScrollPane (progressItemEditPanel));
		
		/*
		 * Dettaglio avanzamento.
		 */
		final ProgressInspectorFrame periodInspectorFrame = ProgressInspectorFrame.getInstance ();
		//Set the window's location.
		{
			final Rectangle bounds = options.getProgressPeriodInspectorBounds ();
			periodInspectorFrame.setBounds (bounds);
			periodInspectorFrame.setVisible (true); //necessary as of 1.3
			desktop.add (periodInspectorFrame, (int)bounds.getX (), (int)bounds.getY ());
		}
		try {
			periodInspectorFrame.setSelected (true);
		} catch (java.beans.PropertyVetoException e) {}
		javax.help.CSH.setHelpIDString (periodInspectorFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PERIODINSPECTORFRAME ));
		
		/*
		 * Dettaglio avanzamento.
		 */
		final ChartFrame chartFrame = ChartFrame.getInstance ();
		//Set the window's location.
		{
			final Rectangle bounds = options.getChartFrameBounds ();
			chartFrame.setBounds (bounds);
			chartFrame.setVisible (true); //necessary as of 1.3
			desktop.add (chartFrame, (int)bounds.getX (), (int)bounds.getY ());
		}
		try {
			chartFrame.setSelected (true);
		} catch (java.beans.PropertyVetoException e) {}
		javax.help.CSH.setHelpIDString (chartFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.CHARTFRAME ));
		
		//		dataTabbedPane.addTab (ResourceSupplier.getString (ResourceClass.UI, "controls", "detail")
		//		, new JScrollPane (progressItemEditPanel));
		
		
		//		final JPanel rightPanel = new JPanel (new BorderLayout ());
		
		//		jSplit_Tree_Data.setRightComponent (rightPanel);
		
		//		rightPanel.add (desktop, BorderLayout.CENTER);
		
		//		final JScrollPane scrollPane = new JScrollPane();
		//
		//		final JViewport viewport = new JViewport();
		
		//		viewport.setView(desktop);
		
		//		scrollPane.setViewport(viewport);
		
		//		desktop.setPreferredSize(new Dimension(1600,1200)); //very important
		
		//		rightPanel.add (scrollPane, BorderLayout.CENTER);
		
		jSplit_Tree_Data.setRightComponent (desktop);
		
		
		jSplit_Tree_Data.setOneTouchExpandable (true);
		
		final int treewidth = Application.getOptions ().getProgressItemTreeWidth ();
		jSplit_Tree_Data.setDividerLocation (treewidth>0?treewidth:180);
		
		jPanelTree.add (jSplit_Tree_Data, java.awt.BorderLayout.CENTER);
		
		mainPanel.add (jPanelTree, java.awt.BorderLayout.CENTER);
		
		getContentPane ().add (mainPanel, java.awt.BorderLayout.CENTER);
		
		jMenuFile.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "file"));
		
		//separatore
		jMenuFile.add (new javax.swing.JSeparator ());
		
		jMenuProject.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "project"));
		//create project
		jMenuItemNewProject.setAction (ActionPool.getInstance ().getProjectCreateAction ());
		jMenuProject.add (jMenuItemNewProject);
		
		//open project
		jMenuItemOpen.setAction (ActionPool.getInstance ().getProjectOpenAction ());
		jMenuProject.add (jMenuItemOpen);
		
		jMenuItemCloseProject.setAction (ActionPool.getInstance ().getProjectCloseAction ());
		jMenuProject.add (jMenuItemCloseProject);
		
		//delete project
		jMenuItemDeleteProject.setAction (ActionPool.getInstance ().getProjectDeleteAction ());
		jMenuProject.add (jMenuItemDeleteProject);
		
		//separatore
		jMenuProject.add (new javax.swing.JSeparator ());
		
		//save project
		jMenuItemSaveProject.setAction (ActionPool.getInstance ().getProjectSaveAction ());
		/* l'utente non saprebbe che farsene */
		//		jMenuProject.add (jMenuItemSaveProject);
		
		//separatore
		jMenuProject.add (new javax.swing.JSeparator ());
		
		//export project
		jMenuExportProject.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "project.export"));
		jMenuProject.add (jMenuExportProject);
		
		//export project XML
		jMenuItemXMLProjectExport.setAction (ActionPool.getInstance ().getProjectXMLExportAction ());
		jMenuExportProject.add (jMenuItemXMLProjectExport);
		
		//import project
		jMenuImportProject.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "project.import"));
		jMenuProject.add (jMenuImportProject);
		
		//import project XML
		jMenuItemXMLProjectImport.setAction (ActionPool.getInstance ().getProjectXMLImportAction ());
		jMenuImportProject.add (jMenuItemXMLProjectImport);
		
		//separatore
		//evitiamo uscite involontarie, almeno finchè non si implementa l'alert
		//@todo implementare alert richiesta uscita applicazione
		//jMenuItemFinish.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemFinish.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "file.exit"));
		jMenuItemFinish.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "exit.png", true));
		jMenuItemFinish.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				//chiusura
				if (
				JOptionPane.showConfirmDialog (
				Application.getInstance ().getMainForm (), ResourceSupplier.getString (ResourceClass.UI, "controls", "application.exit.confirm"))!=JOptionPane.OK_OPTION){
					return;
				}
				Application.getInstance ().exit ();
			}
		});
		jMenuFile.add (jMenuItemFinish);
		
		menuBar.add (jMenuFile);
		menuBar.add (jMenuProject);
		
		jMenuEdit.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "edit"));
		
		final TransferActionListener actionListener = new TransferActionListener ();
		jMenuItemCut.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "editcut.png"));
		jMenuItemCut.setActionCommand ((String)TransferHandler.getCutAction ().
		getValue (Action.NAME));
		jMenuItemCut.addActionListener (actionListener);
		
		jMenuItemCut.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemCut.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "edit.cut"));
		jMenuEdit.add (jMenuItemCut);
		
		jMenuItemCopy.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "editcopy.png"));
		jMenuItemCopy.setActionCommand ((String)TransferHandler.getCopyAction ().
		getValue (Action.NAME));
		jMenuItemCopy.addActionListener (actionListener);
		jMenuItemCopy.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemCopy.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "edit.copy"));
		/** @todo: implementare logica abilitazione*/
		jMenuItemCopy.setEnabled (false);
		jMenuEdit.add (jMenuItemCopy);
		
		jMenuItemPaste.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "editpaste.png"));
		jMenuItemPaste.setActionCommand ((String)TransferHandler.getPasteAction ().
		getValue (Action.NAME));
		jMenuItemPaste.addActionListener (actionListener);
		jMenuItemPaste.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemPaste.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "edit.paste"));
		jMenuEdit.add (jMenuItemPaste);
		
		menuBar.add (jMenuEdit);
		
		jMenuActions.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions"));
		
		jMenuItemCreateNode.setAction (ActionPool.getInstance ().getNodeCreateAction ());
		jMenuActions.add (jMenuItemCreateNode);
		
		jMenuItemStartNodeEdit.setAction (ActionPool.getInstance ().getStartNodeEdit ());
		jMenuActions.add (jMenuItemStartNodeEdit);
		
		jMenuItemDeleteNode.setAction (ActionPool.getInstance ().getNodeDeleteAction ());
		jMenuActions.add (jMenuItemDeleteNode);
		
		jMenuActions.addSeparator ();
		

		//import project
		final JMenu startProgress = new JMenu (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.new.progress"));
		jMenuActions.add (startProgress);
		
		jMenuItemStart.setAction (ActionPool.getInstance ().getProgressStartAction ());
		startProgress.add (jMenuItemStart);
		
		jMenuItemCreateProgress.setAction (ActionPool.getInstance ().getStartNewProgressCreation ());
		startProgress.add (jMenuItemCreateProgress);
		
		jMenuItemStop.setAction (ActionPool.getInstance ().getProgressStopAction ());
		jMenuActions.add (jMenuItemStop);
		
		jMenuActions.addSeparator ();
		jMenuItemTreeExpandCollapse.setAction (treeExpandCollapseAction);
		jMenuActions.add (jMenuItemTreeExpandCollapse);
		
		menuBar.add (jMenuActions);
		
		jMenuItemReports.setAction (ActionPool.getInstance ().getShowReportsDialog ());
		jMenuTools.add (jMenuItemReports);
		
		jMenuTools.add (new javax.swing.JSeparator ());
		
		jMenuItemDataStoreWizard.setAction (ActionPool.getInstance ().getShowDataStoreWIzardDialog ());
		jMenuTools.add (jMenuItemDataStoreWizard);
		
		jMenuTools.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "tool"));
		jMenuItemOptions.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "configure.png"));
		jMenuItemOptions.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.options"));
		jMenuItemOptions.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent e){
				UserSettingsFrame.getInstance ().show ();
			}
		});
		
		jMenuTools.add (jMenuItemOptions);
		
		final JMenuItem logMenuItem = new JMenuItem ();
		logMenuItem.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_L, 0));
		logMenuItem.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "log-frame.png"));
		logMenuItem.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.log_console"));
		logMenuItem.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent e){
				LogFrame.getInstance ().show ();
			}
		});
		
		jMenuTools.add (logMenuItem);
		
		jMenuHelp.add (jMenuTools);
		
		menuBar.add (jMenuTools);
		
		
		// Menu for the look and feels (lnfs).
		UIManager.LookAndFeelInfo[] lnfs = UIManager.getInstalledLookAndFeels ();
		
		ButtonGroup lnfGroup = new ButtonGroup ();
		JMenu lnfMenu = new JMenu ("Look&Feel");
		lnfMenu.setMnemonic ('L');
		
		menuBar.add (lnfMenu);
		
		
		
		for (int i = 0; i < lnfs.length; i++) {
			if (!lnfs[i].getName ().equals ("CDE/Motif")) {
				JRadioButtonMenuItem rbmi = new JRadioButtonMenuItem (lnfs[i].getName ());
				lnfMenu.add (rbmi);
				
				lookAndFeelMenuItems.add (rbmi);
				
				// preselect the current Look & feel
				rbmi.setSelected (UIManager.getLookAndFeel ().getName ().equals (lnfs[i].getName ()));
				
				// store lool & feel info as client property
				rbmi.putClientProperty ("lnf name", lnfs[i]);
				
				// create and add the item listener
				rbmi.addItemListener (
				// inlining
				new ItemListener () {
					public void itemStateChanged (ItemEvent ie) {
						JRadioButtonMenuItem rbmi2 = (JRadioButtonMenuItem) ie.getSource ();
						
						if (rbmi2.isSelected ()) {
							// get the stored look & feel info
							UIManager.LookAndFeelInfo info = (UIManager.LookAndFeelInfo) rbmi2.getClientProperty (
							"lnf name");
							updateLookAndFeel (info.getClassName ());
						}
					}
				});
				lnfGroup.add (rbmi);
			}
		}
		
		
		
		jMenuHelp.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "help"));
		jMenuItemAbout.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_QUOTE, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemAbout.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "help.about"));
		jMenuItemAbout.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "info.png"));
		jMenuItemAbout.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent e){
				AboutBox.getInstance ().show ();
			}
		});
		
		jMenuHelp.add (jMenuItemAbout);
		
		jMenuItemHelp.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_F1, 0));
		jMenuItemHelp.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "contents.png"));
		jMenuItemHelp.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "help.help"));
		//		jMenuItemHelp.setActionCommand ("Help");
		HelpManager.getInstance ().initialize (jMenuItemHelp);
		
		jMenuHelp.add (jMenuItemHelp);
		
		jMenuItemContextualHelp.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "help.contextualhelp"));
		jMenuItemContextualHelp.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "contexthelp.png"));
		jMenuItemContextualHelp.addActionListener (new CSH.DisplayHelpAfterTracking (HelpManager.getInstance ().getMainHelpBroker ()));
		
		jMenuHelp.add (jMenuItemContextualHelp);
		
		/* il menu dell'help va aggiunto in coda */
		menuBar.add (jMenuHelp);
		
//		menuBar.setBorderPainted (false);
		setJMenuBar (menuBar);
		
		progressTreePopup.add (treeExpandCollapseAction);
		progressTreePopup.addSeparator ();
		progressTreePopup.add (ActionPool.getInstance ().getNodeCreateAction ());
		progressTreePopup.add (ActionPool.getInstance ().getNodeDeleteAction ());
		progressTreePopup.addSeparator ();
		progressTreePopup.add (ActionPool.getInstance ().getProgressStartAction ());
		progressTreePopup.add (ActionPool.getInstance ().getProgressStopAction ());
		progressTreePopup.addSeparator ();
		
		progressItemTree.add (progressTreePopup);
		progressItemTree.addMouseListener (new PopupTrigger ());
		
		// register the menu bar with the scrollable desktop
		desktop.registerMenuBar (menuBar);
		
		menuBar.add (jMenuHelp);
		
		// register the default internal frame icon
		//		desktop.registerDefaultFrameIcon(new ImageIcon("images/frmeicon.gif"));
		
		
		pack ();
	}
	
	private void jMenuItemCloseActionPerformed (java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}
	
	private void jMenuItemOpenActionPerformed (java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}
	
	
	
	// Variables declaration - do not modify
	private javax.swing.JLabel statusLabel;
	private javax.swing.JLabel currentDurationLabel;
	private javax.swing.JLayeredPane jLayeredPane1;
	private javax.swing.JMenu jMenuActions;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenu jMenuEdit;
	private javax.swing.JMenu jMenuExportProject;
	private javax.swing.JMenu jMenuImportProject;
	private javax.swing.JMenuItem jMenuItemXMLProjectExport;
	private javax.swing.JMenuItem jMenuItemXMLProjectImport;
	private javax.swing.JMenu jMenuFile;
	private javax.swing.JMenu jMenuProject;
	private javax.swing.JMenu jMenuHelp;
	private javax.swing.JMenuItem jMenuItemAbout;
	private javax.swing.JMenuItem jMenuItemCloseProject;
	private javax.swing.JMenuItem jMenuItemCopy;
	private javax.swing.JMenuItem jMenuItemCut;
	private javax.swing.JMenuItem jMenuItemDeleteNode;
	private javax.swing.JMenuItem jMenuItemTreeExpandCollapse;
	private javax.swing.JMenuItem jMenuItemFinish;
	private javax.swing.JMenuItem jMenuItemHelp;
	private javax.swing.JMenuItem jMenuItemContextualHelp;
	private javax.swing.JMenuItem jMenuItemNewProject;
	private javax.swing.JMenuItem jMenuItemOpen;
	private javax.swing.JMenuItem jMenuItemDeleteProject;
	private javax.swing.JMenuItem jMenuItemSaveProject;
	private javax.swing.JMenuItem jMenuItemCreateNode;
	private javax.swing.JMenuItem jMenuItemStartNodeEdit;
	private javax.swing.JMenuItem jMenuItemOptions;
	private javax.swing.JMenuItem jMenuItemReports;
	private javax.swing.JMenuItem jMenuItemDataStoreWizard;
	private javax.swing.JMenuItem jMenuItemPaste;
	private javax.swing.JMenuItem jMenuItemStart;
	private javax.swing.JMenuItem jMenuItemCreateProgress;
	private javax.swing.JMenuItem jMenuItemStop;
	private javax.swing.JMenu jMenuTools;
	private javax.swing.JPanel jPanelTree;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JPopupMenu progressTreePopup;
	private javax.swing.JSplitPane jSplit_Tree_Data;
	private Desktop desktop;
	private javax.swing.JToolBar mainToolbar;
	private javax.swing.JProgressBar jobProgress;
	private ProgressItemTree progressItemTree;
	private javax.swing.JButton nodeCreateButton;
	private javax.swing.JButton nodeEditButton;
	private javax.swing.JButton nodeDeleteButton;
	private javax.swing.JButton startButton;
	private javax.swing.JPanel statusBar;
	private javax.swing.JButton stopButton;
	// End of variables declaration
	
	private void postInitComponents () {
		this.progressItemTree.getTree ().addTreeSelectionListener (new TreeSelectionListener (){
			public void valueChanged (TreeSelectionEvent e){
				if (e.getNewLeadSelectionPath () == null) {
					//gestione caso cancellazione elemento selezionato
					return;
				}
				TreePath thePath = e.getPath ();
				application.setSelectedItem ((ProgressItem)thePath.getLastPathComponent ());
				treeExpandCollapseAction.makeForPath (thePath);
			}
		});
		this.progressItemTree.getTree ().setSelectionRow (0);
		
		this.progressItemTree.getTree ().addTreeSelectionListener (ProgressListFrame.getInstance ().getProgressTable ());
//		this.progressItemTree.getTree ().setEditable (true);
//		this.progressItemTree.getTree ().setInvokesStopCellEditing (true);
	}
	
	private final void initTreeModelListeners (){
		//		this.progressItemTree.getTree ().getModel ().addTreeModelListener (new TreeModelListener (){
		//			public void treeNodesChanged (TreeModelEvent e){repaint ();}
		//			public void treeNodesInserted (TreeModelEvent e){repaint ();}
		//			public void treeNodesRemoved (TreeModelEvent e){repaint ();}
		//			public void treeStructureChanged (TreeModelEvent e){repaint ();}
		//		});
		//		this.progressItemTree.getTree ().getModel ().addTreeModelListener (ProgressListFrame.getInstance ().getProgressTable ());
	}
	
	/**
	 * Inizializzaizone listener modello tabella avanzamenti.
	 */
	private final void initTableModelListeners (){
	}
	
	/**
	 * Modello albero nodi di avanzamento.
	 */
	private ProgressTreeModel progressTreeModel;
	
	/**
	 * Ritorna il modello dell'albero dei nodi di avanzamento.
	 *
	 * @return modello dell'albero.
	 */
	public ProgressTreeModel getProgressTreeModel (){
		return this.progressTreeModel;
	}
	
	public void update (Observable o, Object arg) {
		if (arg!=null && arg.equals (ObserverCodes.PROJECTCHANGE)){
			this.progressTreeModel.load (application.getProject ());
			
			ProgressListFrame.getInstance ().getProgressTable ().getProgressTableModel ().load (application.getSelectedItem ());
			
			initTreeModelListeners ();
			this.progressItemTree.invalidate ();
			initTableModelListeners ();
			
			/* aggiorna titolo */
			
			final ApplicationData appData = ApplicationData.getInstance ();
			final StringBuffer title = new StringBuffer ();
			title.append (appData.getApplicationExternalName ())
			.append (" v.")
			.append (appData.getVersionNumber ());
			final Project currentProject = Application.getInstance ().getProject ();
			if (currentProject!=null){
				title.append (" - ")
				.append (ResourceSupplier.getString (ResourceClass.UI, "controls","project"))
				.append (" ")
				.append (currentProject.getName ());
			}
			setTitle (title.toString ());
		} else if (arg!=null && ( arg.equals (ObserverCodes.ITEMPROGRESSINGPERIODCHANGE) || arg.equals (ObserverCodes.CURRENTITEMCHANGE))){
			final Application app = Application.getInstance ();
			statusLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "statusbar", app.getCurrentItem ()!=null?"statuslabel.working_UC":"statuslabel.idle_UC"));
			
			Duration duration=null; {
				final ProgressItem currentProgressItem = application.getCurrentItem ();
				if (currentProgressItem!=null){
					final Progress currentProgress = currentProgressItem.getCurrentProgress ();
					if (currentProgress!=null){
						duration = currentProgress.getDuration ();
					}
				}
			}
			if (duration==null){
				duration = emptyDuration;
			}
			final StringBuffer sb = new StringBuffer ();
			/*
			sb.append (durationNumberFormatter.format(duration.getDays()))
			.append (":")
			 */
			sb.append (durationNumberFormatter.format (duration.getHours ()))
			.append (":")
			.append (durationNumberFormatter.format (duration.getMinutes ()))
			.append (":")
			.append (durationNumberFormatter.format (duration.getSeconds ()));
			currentDurationLabel.setText (sb.toString ());
			
			currentDurationLabel.revalidate ();
//			Application.getLogger ().debug ("updating duration label");
			
		} else if (arg!=null && (arg.equals (ObserverCodes.PROCESSINGCHANGE))){
			Application app = Application.getInstance ();
			jobProgress.setIndeterminate (app.isProcessing ());
			jobProgress.setValue (app.isProcessing ()?99:0);
			jobProgress.setVisible (app.isProcessing ());
			
			final String processingMessage = app.getProcessingMessage ();
			final boolean stringPainted = processingMessage!=null;
			jobProgress.setStringPainted (stringPainted);
			if (stringPainted){
				jobProgress.setString (processingMessage);
			} else {
				jobProgress.setString (null);
			}
			
		} else if (arg!=null && arg.equals (ObserverCodes.APPLICATIONOPTIONSCHANGE)){
			this.desktop.setBackground (Application.getOptions ().getDesktopColor ());
			this.desktop.revalidate ();
			this.desktop.repaint ();
		}
	}
	
	private final static Date fooFixedDate = new Date ();
	private final static Duration emptyDuration = new Duration (fooFixedDate, fooFixedDate);
	
	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	
	private TreePath lastExpandCollapsePath;
	private TreeExpandCollapseAction treeExpandCollapseAction = new TreeExpandCollapseAction ();
	
	/**
	 * Azione espansione/collassamento albero.
	 */
	private final class TreeExpandCollapseAction extends javax.swing.AbstractAction {
		
		/**
		 * Costruttore vuoto.
		 */
		public TreeExpandCollapseAction () {
			super ();
			this.setEnabled (true);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			if (lastExpandCollapsePath==null){
				return;
			}
			if (progressItemTree.getTree ().isExpanded (lastExpandCollapsePath)){
				progressItemTree.getTree ().collapsePath (lastExpandCollapsePath);
			} else {
				progressItemTree.getTree ().expandPath (lastExpandCollapsePath);
			}
			makeForPath (lastExpandCollapsePath);
		}
		
		public void makeForPath (TreePath path){
			if (path != null) {
				if (progressItemTree.getTree ().isExpanded (path)){
					treeExpandCollapseAction.putValue (Action.NAME, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.collapse"));
				} else {
					treeExpandCollapseAction.putValue (Action.NAME, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.expand"));
				}
				lastExpandCollapsePath = path;
			}
		}
	}
	
	/**
	 * Gestione popup.
	 */
	class PopupTrigger extends MouseAdapter {
		public void mouseReleased (MouseEvent e) {
			handlePopUp (e);
		}
		public void mousePressed (MouseEvent e) {
			handlePopUp (e);
		}
		public void handlePopUp (MouseEvent e){
			if (e.isPopupTrigger ()) {
				int x = e.getX ();
				int y = e.getY ();
				final TreePath path = progressItemTree.getTree ().getPathForLocation (x, y);
				/* varia selezione corrente */
				progressItemTree.getTree ().setSelectionPath (path);
				treeExpandCollapseAction.makeForPath (path);
				progressTreePopup.show (progressItemTree, x, y);
			}
		}
	}
	
	/**
	 * Ritorna l'ampiezza dell'albero.  L'implementazione attuale ritorna in realtà la posizione dello splitter.
	 *
	 * @return l'ampiezza dell'albero.
	 */
	public int getProgressItemTreeWidth (){
		return jSplit_Tree_Data.getDividerLocation ();
	}
	
	public class TransferActionListener implements ActionListener,
	PropertyChangeListener {
		private JComponent focusOwner = null;
		
		public TransferActionListener () {
			KeyboardFocusManager manager = KeyboardFocusManager.
			getCurrentKeyboardFocusManager ();
			manager.addPropertyChangeListener ("permanentFocusOwner", this);
		}
		
		public void propertyChange (PropertyChangeEvent e) {
			Object o = e.getNewValue ();
			if (o instanceof JComponent) {
				focusOwner = (JComponent)o;
			} else {
				focusOwner = null;
			}
		}
		
		public void actionPerformed (ActionEvent e) {
			if (focusOwner == null)
				return;
			String action = (String)e.getActionCommand ();
			Action a = focusOwner.getActionMap ().get (action);
			if (a != null) {
				a.actionPerformed (new ActionEvent (focusOwner,
				ActionEvent.ACTION_PERFORMED,
				null));
			}
		}
	}
	
	/**
	 * Modifica il Look And Feel corrente.
	 *
	 * @param laf il nome del nuovo Look And Feel.
	 */
	public void updateLookAndFeel (final String laf){
		try {
			UIManager.setLookAndFeel (laf);
			SwingUtilities.updateComponentTreeUI (MainForm.this);
		} catch (Exception e) {
			e.printStackTrace ();
			System.err.println ("Unable to set UI " + e.getMessage ());
		}
		updateLookAndFeelMenuItemsState ();
		final Application application = Application.getInstance ();
		application.setChanged ();
		application.notifyObservers (ObserverCodes.LOOKANDFEEL_CHANGING);
	}
	
	/**
	 * Aggiorna lo stato degli elementi di menu che gestiscono il Look And Feel.
	 */
	private void updateLookAndFeelMenuItemsState (){
		for (final Iterator it = lookAndFeelMenuItems.iterator ();it.hasNext ();){
			final JRadioButtonMenuItem rbmi = (JRadioButtonMenuItem)it.next ();
				rbmi.setSelected (UIManager.getLookAndFeel ().getName ().equals (((UIManager.LookAndFeelInfo)rbmi.getClientProperty ("lnf name")).getName ()));
		}
	}
}

/*
 * MainForm.java
 *
 * Created on 22 febbraio 2004, 10.56
 */

package com.ost.timekeeper.ui;

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
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;
import com.toedter.components.*;
import java.awt.*;
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
	 * Costruttore.
	 *
	 * @param app il dispatcher principale.
	 */
	public MainForm (Application app) {
		this.application = app;
		this.progressTreeModel = new ProgressTreeModel (application.getProject ());
		this.application.addObserver (progressTreeModel);
		
		ApplicationOptionsNotifier.getInstance ().addObserver (this);
		
		initComponents ();
		//inizializza table model su dati applicazione
		ProgressListFrame.getInstance ().getProgressTable ().getProgressTableModel ().load (application.getCurrentItem ());
		postInitComponents ();
		
		setBounds (Application.getInstance ().getOptions ().getMainFormBounds ());
		JFrame.setDefaultLookAndFeelDecorated (true);
	}
	
    /**
     * Installs the Kunststoff and Plastic Look And Feels if available in classpath.
     */
    public final void initializeLookAndFeels() {
    	// if in classpath thry to load JGoodies Plastic Look & Feel
        try {
            UIManager.installLookAndFeel("JGoodies Plastic 3D",
                "com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");
        } catch (Throwable t) {
        	try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}  catch (Exception e) {
				e.printStackTrace();
			}
        }
    }

	/**
	 * Inizializzaizone componenti.
	 */
	private void initComponents () {
        // Set the JGoodies Plastic 3D look and feel
        initializeLookAndFeels();
		
		progressTreePopup = new javax.swing.JPopupMenu ();
		mainPanel = new javax.swing.JPanel ();
		mainToolbar = new javax.swing.JToolBar ();
		nodeCreateButton = new javax.swing.JButton ();
		nodeEditButton = new javax.swing.JButton ();
		nodeDeleteButton = new javax.swing.JButton ();
		startButton = new javax.swing.JButton ();
		stopButton = new javax.swing.JButton ();
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
		jMenuItemNew = new javax.swing.JMenuItem ();
		jMenuItemOpen = new javax.swing.JMenuItem ();
		jMenuItemSave = new javax.swing.JMenuItem ();
		jMenuExport = new javax.swing.JMenu ();
		jMenuItemXMLProjectExport = new javax.swing.JMenuItem ();
		jMenuImport = new javax.swing.JMenu ();
		jMenuItemXMLProjectImport = new javax.swing.JMenuItem ();
		jMenuItemClose = new javax.swing.JMenuItem ();
		jMenuItemFinish = new javax.swing.JMenuItem ();
		jMenuEdit = new javax.swing.JMenu ();
		jMenuItemCut = new javax.swing.JMenuItem ();
		jMenuItemCopy = new javax.swing.JMenuItem ();
		jMenuItemPaste = new javax.swing.JMenuItem ();
		jMenuActions = new javax.swing.JMenu ();
		jMenuItemStart = new javax.swing.JMenuItem ();
		jMenuItemStop = new javax.swing.JMenuItem ();
		jMenuItemCreateNode = new javax.swing.JMenuItem ();
		jMenuItemStartNodeEdit = new javax.swing.JMenuItem ();
		jMenuItemDeleteNode = new javax.swing.JMenuItem ();
		jMenuItemTreeExpandCollapse = new javax.swing.JMenuItem ();
		jMenuTools = new javax.swing.JMenu ();
		jMenuItemOptions = new javax.swing.JMenuItem ();
		jMenuItemReports = new javax.swing.JMenuItem ();
		jMenuHelp = new javax.swing.JMenu ();
		jMenuItemAbout = new javax.swing.JMenuItem ();
		jMenuItemHelp = new javax.swing.JMenuItem ();
		jMenuItemContextualHelp = new javax.swing.JMenuItem ();
		
		setDefaultCloseOperation (javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle (ResourceSupplier.getString (ResourceClass.UI, "global", "app.title"));
		setBackground (new java.awt.Color (204, 204, 255));
		setFont (new java.awt.Font ("Default", 0, 10));
		setIconImage (getIconImage ());
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
		mainToolbar.putClientProperty("jgoodies.headerStyle", "Both");

		nodeCreateButton.setAction (ActionPool.getInstance ().getNodeCreateAction ());
		nodeCreateButton.setText ("");
		mainToolbar.add (nodeCreateButton);
		
		nodeEditButton.setAction (ActionPool.getInstance ().getStartNodeEdit ());
		nodeEditButton.setText ("");
		mainToolbar.add (nodeEditButton);
		
		nodeDeleteButton.setAction (ActionPool.getInstance ().getNodeDeleteAction ());
		nodeDeleteButton.setText ("");
		mainToolbar.add (nodeDeleteButton);
		
		mainToolbar.add (new javax.swing.JSeparator ());
		
		startButton.setAction (ActionPool.getInstance ().getProgressStartAction ());
		startButton.setText ("");
		mainToolbar.add (startButton);
		
		stopButton.setAction (ActionPool.getInstance ().getProgressStopAction ());
		stopButton.setText ("");
		mainToolbar.add (stopButton);
		
		mainToolbar.add (new javax.swing.JSeparator ());
		
		{
			final DirectHelpButton directHelpButton = new DirectHelpButton ();
			directHelpButton.setRolloverEnabled (true);
			directHelpButton.setBorderPainted (true);
			mainToolbar.add (directHelpButton);
		}
		javax.help.CSH.setHelpIDString(mainToolbar, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.MAINTOOLBAR ));
		
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
		
		mainPanel.add (statusBar, java.awt.BorderLayout.SOUTH);
		
		jPanelTree.setLayout (new java.awt.BorderLayout ());
		
		final JPanel treePanel = new JPanel (new BorderLayout ());
		
		jSplit_Tree_Data.setToolTipText (ResourceSupplier.getString (ResourceClass.UI, "global", "controls.splitter.tooltip"));
//		progressItemTree.setAutoscrolls (true);
//		progressItemTree.setPreferredSize (new java.awt.Dimension (100, 200));
		treePanel.add (new SimpleInternalFrame (
			ResourceSupplier.getString (ResourceClass.UI, "controls", "jobs.tree"),
			null, new JScrollPane (progressItemTree)
			));
//		treePanel.add (new JScrollPane (progressItemTree), BorderLayout.CENTER);
		
		treePanel.setBackground (progressItemTree.getBackground ());
		
		treePanel.setPreferredSize (new Dimension (180, 200));
		jSplit_Tree_Data.setLeftComponent (treePanel);
		javax.help.CSH.setHelpIDString(progressItemTree, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PROGRESSITEMTREE ));
		
		/*
		 * Gestione doppio click su albero.
		 */
		progressItemTree.addMouseListener (new MouseAdapter (){
			public void mouseClicked(MouseEvent e) {
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
		final ProgressListFrame progressListFrame = ProgressListFrame.getInstance ();
        {
			//Set the window's location.
			final Rectangle bounds = options.getProgressListFrameBounds ();
			progressListFrame.setBounds (bounds);
			progressListFrame.setVisible (true); //necessary as of 1.3
			desktop.add (progressListFrame, (int)bounds.getX (), (int)bounds.getY ());
		}
		try {
			progressListFrame.setSelected (true);
		} catch (java.beans.PropertyVetoException e) {}
		javax.help.CSH.setHelpIDString(progressListFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PROGRESSLIST ));

		/*
		 * Dettaglio nodo di avanzamento.
		 */
		final ProgressItemInspectorFrame progressItemInspectorFrame = ProgressItemInspectorFrame.getInstance ();
		{
			//Set the window's location.
			final Rectangle bounds = options.getProgressItemInspectorBounds ();
			progressItemInspectorFrame.setBounds (bounds);
			progressItemInspectorFrame.setVisible (true); //necessary as of 1.3
			desktop.add (progressItemInspectorFrame, (int)bounds.getX (), (int)bounds.getY ());
		}
		try {
			progressItemInspectorFrame.setSelected (true);
		} catch (java.beans.PropertyVetoException e) {}
		javax.help.CSH.setHelpIDString(progressItemInspectorFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PROGRESSITEMINSPECTORFRAME ));
		
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
		javax.help.CSH.setHelpIDString(periodInspectorFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PERIODINSPECTORFRAME ));
		
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
		javax.help.CSH.setHelpIDString(chartFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.CHARTFRAME ));

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
		//create project
		jMenuItemNew.setAction (ActionPool.getInstance ().getProjectCreateAction ());
		jMenuFile.add (jMenuItemNew);
		
		//open project
		jMenuItemOpen.setAction (ActionPool.getInstance ().getProjectOpenAction ());
		jMenuFile.add (jMenuItemOpen);
		
		jMenuItemClose.setAction (ActionPool.getInstance ().getProjectCloseAction ());
		jMenuFile.add (jMenuItemClose);
		
		//separatore
		jMenuFile.add (new javax.swing.JSeparator ());
		
		//save project
		jMenuItemSave.setAction (ActionPool.getInstance ().getProjectSaveAction ());
		/* l'utente non saprebbe che farsene */
//		jMenuFile.add (jMenuItemSave);
		
		//separatore
		jMenuFile.add (new javax.swing.JSeparator ());
		
		//export project
		jMenuExport.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "file.export"));
		jMenuFile.add (jMenuExport);
		
		//export project XML
		jMenuItemXMLProjectExport.setAction (ActionPool.getInstance ().getProjectXMLExportAction ());
		jMenuExport.add (jMenuItemXMLProjectExport);
		
		//import project
		jMenuImport.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "file.import"));
		jMenuFile.add (jMenuImport);
		
		//import project XML
		jMenuItemXMLProjectImport.setAction (ActionPool.getInstance ().getProjectXMLImportAction ());
		jMenuImport.add (jMenuItemXMLProjectImport);
		
		//separatore
		jMenuFile.add (new javax.swing.JSeparator ());
		
		//separatore
		//evitiamo uscite involontarie, almeno finchè non si implementa l'alert
		//@todo implementare alert richiesta uscita applicazione
		//jMenuItemFinish.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemFinish.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "file.exit"));
		jMenuItemFinish.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				//chiusura
				Application.getInstance ().exit ();
			}
		});
		jMenuFile.add (jMenuItemFinish);
		
		menuBar.add (jMenuFile);
		
		jMenuEdit.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "edit"));
		jMenuItemCut.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemCut.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "edit.cut"));
		jMenuEdit.add (jMenuItemCut);
		
		jMenuItemCopy.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemCopy.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "edit.copy"));
		jMenuEdit.add (jMenuItemCopy);
		
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
		
		jMenuItemStart.setAction (ActionPool.getInstance ().getProgressStartAction ());
		jMenuActions.add (jMenuItemStart);
		
		jMenuItemStop.setAction (ActionPool.getInstance ().getProgressStopAction ());
		jMenuActions.add (jMenuItemStop);
		
		jMenuActions.addSeparator ();
		jMenuItemTreeExpandCollapse.setAction (treeExpandCollapseAction);
		jMenuActions.add (jMenuItemTreeExpandCollapse);
		
		menuBar.add (jMenuActions);
		
		jMenuTools.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "tool"));
		jMenuItemOptions.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.options"));
		jMenuItemOptions.addActionListener (new ActionListener (){
			public void actionPerformed(ActionEvent e){
				UserSettingsFrame.getInstance ().show ();
			}
		});

		jMenuTools.add (jMenuItemOptions);
		
		jMenuItemReports.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.reports"));
		jMenuItemReports.addActionListener (new ActionListener (){
			public void actionPerformed(ActionEvent e){
				ReportsFrame.getInstance ().show ();
			}
		});

		jMenuTools.add (new javax.swing.JSeparator ());
		
		jMenuTools.add (jMenuItemReports);

		menuBar.add (jMenuTools);

		
        // Menu for the look and feels (lnfs).
        UIManager.LookAndFeelInfo[] lnfs = UIManager.getInstalledLookAndFeels();

        ButtonGroup lnfGroup = new ButtonGroup();
        JMenu lnfMenu = new JMenu("Look&Feel");
        lnfMenu.setMnemonic('L');

        menuBar.add(lnfMenu);

        for (int i = 0; i < lnfs.length; i++) {
            if (!lnfs[i].getName().equals("CDE/Motif")) {
                JRadioButtonMenuItem rbmi = new JRadioButtonMenuItem(lnfs[i].getName());
                lnfMenu.add(rbmi);

                // preselect the current Look & feel
                rbmi.setSelected(UIManager.getLookAndFeel().getName().equals(lnfs[i].getName()));

                // store lool & feel info as client property
                rbmi.putClientProperty("lnf name", lnfs[i]);

                // create and add the item listener
                rbmi.addItemListener(
                // inlining
                new ItemListener() {
                        public void itemStateChanged(ItemEvent ie) {
                            JRadioButtonMenuItem rbmi2 = (JRadioButtonMenuItem) ie.getSource();

                            if (rbmi2.isSelected()) {
                                // get the stored look & feel info
                                UIManager.LookAndFeelInfo info = (UIManager.LookAndFeelInfo) rbmi2.getClientProperty(
                                        "lnf name");

                                try {
                                    UIManager.setLookAndFeel(info.getClassName());

                                    // update the complete application's
                                    // look & feel
                                    SwingUtilities.updateComponentTreeUI(MainForm.this);

                                    // set the split pane devider border to
                                    // null
//                                    BasicSplitPaneDivider divider = ((BasicSplitPaneUI) splitPane.getUI()).getDivider();
//
//                                    if (divider != null) {
//                                        divider.setBorder(null);
//                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();

                                    System.err.println("Unable to set UI " + e.getMessage());
                                }
                            }
                        }
                    });
                lnfGroup.add(rbmi);
            }
        }
		
		
		
		jMenuHelp.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "help"));
		jMenuItemAbout.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_QUOTE, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemAbout.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "help.about"));
		jMenuItemAbout.addActionListener (new ActionListener (){
			public void actionPerformed(ActionEvent e){
				AboutBox.getInstance ().show ();
			}
		});
		
		jMenuHelp.add (jMenuItemAbout);
		
		jMenuItemHelp.setAccelerator (javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_F1, 0));
		jMenuItemHelp.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "help.help"));
//		jMenuItemHelp.setActionCommand ("Help");
		HelpManager.getInstance ().initialize (jMenuItemHelp);
		
		jMenuHelp.add (jMenuItemHelp);
		
		jMenuItemContextualHelp.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "help.contextualhelp"));
		jMenuItemContextualHelp.setIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "directhelp.gif"));
		jMenuItemContextualHelp.addActionListener(new CSH.DisplayHelpAfterTracking(HelpManager.getInstance ().getMainHelpBroker ()));

		jMenuHelp.add (jMenuItemContextualHelp);
		
		/* il menu dell'help va aggiunto in coda */
		menuBar.add (jMenuHelp);
		
		menuBar.setBorderPainted (false);
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
		desktop.registerMenuBar(menuBar);
		
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
	private javax.swing.JMenu jMenuExport;
	private javax.swing.JMenu jMenuImport;
	private javax.swing.JMenuItem jMenuItemXMLProjectExport;
	private javax.swing.JMenuItem jMenuItemXMLProjectImport;
	private javax.swing.JMenu jMenuFile;
	private javax.swing.JMenu jMenuHelp;
	private javax.swing.JMenuItem jMenuItemAbout;
	private javax.swing.JMenuItem jMenuItemClose;
	private javax.swing.JMenuItem jMenuItemCopy;
	private javax.swing.JMenuItem jMenuItemCut;
	private javax.swing.JMenuItem jMenuItemDeleteNode;
	private javax.swing.JMenuItem jMenuItemTreeExpandCollapse;
	private javax.swing.JMenuItem jMenuItemFinish;
	private javax.swing.JMenuItem jMenuItemHelp;
	private javax.swing.JMenuItem jMenuItemContextualHelp;
	private javax.swing.JMenuItem jMenuItemNew;
	private javax.swing.JMenuItem jMenuItemOpen;
	private javax.swing.JMenuItem jMenuItemSave;
	private javax.swing.JMenuItem jMenuItemCreateNode;
	private javax.swing.JMenuItem jMenuItemStartNodeEdit;
	private javax.swing.JMenuItem jMenuItemOptions;
	private javax.swing.JMenuItem jMenuItemReports;
	private javax.swing.JMenuItem jMenuItemPaste;
	private javax.swing.JMenuItem jMenuItemStart;
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
		this.progressItemTree.getTree ().setEditable (true);
		this.progressItemTree.getTree ().setInvokesStopCellEditing (true);
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
		} else if (arg!=null && ( arg.equals (ObserverCodes.ITEMPROGRESSINGPERIODCHANGE) || arg.equals (ObserverCodes.CURRENTITEMCHANGE))){
			final Application app = Application.getInstance ();
			statusLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "statusbar", app.getCurrentItem ()!=null?"statuslabel.working_UC":"statuslabel.idle_UC"));

			Duration duration=null;
			{
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
			Application.getLogger ().debug ("updating duration label");
			
		} else if (arg!=null && (arg.equals (ObserverCodes.PROCESSINGCHANGE))){
			Application app = Application.getInstance ();
			jobProgress.setIndeterminate (app.isProcessing ());
			jobProgress.setValue (app.isProcessing ()?99:0);
			jobProgress.setVisible (app.isProcessing ());
		} else if (arg!=null && arg.equals(ObserverCodes.APPLICATIONOPTIONSCHANGE)){
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
}

/*
 * MainForm.java
 *
 * Created on 22 febbraio 2004, 10.56
 */

package com.ost.timekeeper.ui;

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
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;
import java.awt.*;
import java.text.*;
import javax.help.*;

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
	 * Inizializzaizone componenti.
	 */
	private void initComponents () {
		progressTreePopup = new javax.swing.JPopupMenu ();
		jPanelMain = new javax.swing.JPanel ();
		mainToolbar = new javax.swing.JToolBar ();
		nodeCreateButton = new javax.swing.JButton ();
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
		
		//		dataTabbedPane = new javax.swing.JTabbedPane ();
		desktop = Desktop.getInstance ();
		Color desktopColor = Application.getOptions ().getDesktopColor ();
		if (desktopColor==null) {
			desktopColor = Color.BLACK;
		}
		desktop.setBackground (desktopColor);
		desktop.setDragMode (JDesktopPane.LIVE_DRAG_MODE);
		

		this.application.addObserver (ProgressListFrame.getInstance ().getProgressTable ());
		jMenuBarMain = new javax.swing.JMenuBar ();
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
		jMenuItemDeleteNode = new javax.swing.JMenuItem ();
		jMenuItemTreeExpandCollapse = new javax.swing.JMenuItem ();
		jMenuTools = new javax.swing.JMenu ();
		jMenuItemOptions = new javax.swing.JMenuItem ();
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
		
		jPanelMain.setLayout (new java.awt.BorderLayout ());
		
		mainToolbar.setLayout (new java.awt.FlowLayout ( java.awt.FlowLayout.LEFT));
		mainToolbar.setBorder (new javax.swing.border.EtchedBorder ());
		mainToolbar.setRollover (true);
		mainToolbar.setAutoscrolls (true);
		nodeCreateButton.setAction (ActionPool.getInstance ().getNodeCreateAction ());
		nodeCreateButton.setText ("");
		mainToolbar.add (nodeCreateButton);
		
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
		
		jPanelMain.add (mainToolbar, java.awt.BorderLayout.NORTH);
		
		statusBar.setLayout (new javax.swing.BoxLayout (statusBar, javax.swing.BoxLayout.X_AXIS));
		
		statusBar.setMinimumSize (new java.awt.Dimension (10, 30));
		statusLabel.setFont (new java.awt.Font ("Default", 0, 12));
		statusLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "statusbar", "statuslabel.idle_UC"));
		statusLabel.setBorder (new javax.swing.border.LineBorder (new java.awt.Color (0, 0, 0)));
		statusBar.add (statusLabel);
		
		currentDurationLabel.setFont (new java.awt.Font ("Default", 0, 12));
		currentDurationLabel.setText ("currentDurationLabel");
		currentDurationLabel.setBorder (new javax.swing.border.LineBorder (new java.awt.Color (0, 0, 0)));
		statusBar.add (currentDurationLabel);
		
		jobProgress.setBorder (null);
		jobProgress.setIndeterminate (false);
		jobProgress.setValue (0);
		statusBar.add (jobProgress);
		
		jPanelMain.add (statusBar, java.awt.BorderLayout.SOUTH);
		
		jPanelTree.setLayout (new java.awt.BorderLayout ());
		
		jSplit_Tree_Data.setToolTipText (ResourceSupplier.getString (ResourceClass.UI, "global", "controls.splitter.tooltip"));
		progressItemTree.setAutoscrolls (true);
		progressItemTree.setPreferredSize (new java.awt.Dimension (150, 200));
		jSplit_Tree_Data.setLeftComponent (new JScrollPane (progressItemTree));
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
        //Set the window's location.
        progressListFrame.setBounds (options.getProgressListFrameBounds ());
		progressListFrame.setVisible (true); //necessary as of 1.3
		desktop.add (progressListFrame);
		try {
			progressListFrame.setSelected (true);
		} catch (java.beans.PropertyVetoException e) {}
		javax.help.CSH.setHelpIDString(progressListFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PROGRESSLIST ));

		/*
		 * Dettaglio nodo di avanzamento.
		 */
		final ProgressItemInspectorFrame progressItemInspectorFrame = ProgressItemInspectorFrame.getInstance ();
        //Set the window's location.
        progressItemInspectorFrame.setBounds (options.getProgressItemInspectorBounds ());
		progressItemInspectorFrame.setVisible (true); //necessary as of 1.3
		desktop.add (progressItemInspectorFrame);
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
        periodInspectorFrame.setBounds (options.getProgressPeriodInspectorBounds ());
		periodInspectorFrame.setVisible (true); //necessary as of 1.3
		desktop.add (periodInspectorFrame);
		try {
			periodInspectorFrame.setSelected (true);
		} catch (java.beans.PropertyVetoException e) {}
		javax.help.CSH.setHelpIDString(periodInspectorFrame, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.PERIODINSPECTORFRAME ));
		

		//		dataTabbedPane.addTab (ResourceSupplier.getString (ResourceClass.UI, "controls", "detail")
		//		, new JScrollPane (progressItemEditPanel));
		
		jSplit_Tree_Data.setRightComponent (desktop);
		jSplit_Tree_Data.setOneTouchExpandable (true);
		
		jPanelTree.add (jSplit_Tree_Data, java.awt.BorderLayout.CENTER);
		
		jPanelMain.add (jPanelTree, java.awt.BorderLayout.CENTER);
		
		getContentPane ().add (jPanelMain, java.awt.BorderLayout.CENTER);
		
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
		jMenuFile.add (jMenuItemSave);
		
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
		//evitiamo uscite involontarie, almeno finch� non si implementa l'alert
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
		
		jMenuBarMain.add (jMenuFile);
		
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
		
		jMenuBarMain.add (jMenuEdit);
		
		jMenuActions.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "actions"));
		
		jMenuItemCreateNode.setAction (ActionPool.getInstance ().getNodeCreateAction ());
		jMenuActions.add (jMenuItemCreateNode);
		
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
		
		jMenuBarMain.add (jMenuActions);
		
		jMenuTools.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "tool"));
		jMenuItemOptions.setText (ResourceSupplier.getString (ResourceClass.UI, "menu", "tools.options"));
		jMenuItemOptions.addActionListener (new ActionListener (){
			public void actionPerformed(ActionEvent e){
				UserSettingsFrame.getInstance ().show ();
			}
		});

		jMenuTools.add (jMenuItemOptions);
		
		jMenuBarMain.add (jMenuTools);
		
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
		jMenuItemContextualHelp.addActionListener(new CSH.DisplayHelpAfterTracking(HelpManager.getInstance ().getMainHelpBroker ()));
		
		jMenuHelp.add (jMenuItemContextualHelp);
		
		jMenuBarMain.add (jMenuHelp);
		
		setJMenuBar (jMenuBarMain);
		
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
	private javax.swing.JMenuBar jMenuBarMain;
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
	private javax.swing.JMenuItem jMenuItemOptions;
	private javax.swing.JMenuItem jMenuItemPaste;
	private javax.swing.JMenuItem jMenuItemStart;
	private javax.swing.JMenuItem jMenuItemStop;
	private javax.swing.JMenu jMenuTools;
	private javax.swing.JPanel jPanelTree;
	private javax.swing.JPanel jPanelMain;
	private javax.swing.JPopupMenu progressTreePopup;
	private javax.swing.JSplitPane jSplit_Tree_Data;
	private Desktop desktop;
	private javax.swing.JToolBar mainToolbar;
	private javax.swing.JProgressBar jobProgress;
	private javax.swing.JTree progressItemTree;
	private javax.swing.JButton nodeCreateButton;
	private javax.swing.JButton nodeDeleteButton;
	private javax.swing.JButton startButton;
	private javax.swing.JPanel statusBar;
	private javax.swing.JButton stopButton;
	// End of variables declaration
	
	private void postInitComponents () {
		this.progressItemTree.addTreeSelectionListener (new TreeSelectionListener (){
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
		this.progressItemTree.setSelectionRow (0);
		
		this.progressItemTree.addTreeSelectionListener (ProgressListFrame.getInstance ().getProgressTable ());
		this.progressItemTree.setEditable (true);
		this.progressItemTree.setInvokesStopCellEditing (true);
	}
	
	private final void initTreeModelListeners (){
		this.progressItemTree.getModel ().addTreeModelListener (new TreeModelListener (){
			public void treeNodesChanged (TreeModelEvent e){repaint ();}
			public void treeNodesInserted (TreeModelEvent e){repaint ();}
			public void treeNodesRemoved (TreeModelEvent e){repaint ();}
			public void treeStructureChanged (TreeModelEvent e){repaint ();}
		});
		this.progressItemTree.getModel ().addTreeModelListener (ProgressListFrame.getInstance ().getProgressTable ());
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
					final Period currentProgress = currentProgressItem.getCurrentProgress ();
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
			if (progressItemTree.isExpanded (lastExpandCollapsePath)){
				progressItemTree.collapsePath (lastExpandCollapsePath);
			} else {
				progressItemTree.expandPath (lastExpandCollapsePath);
			}
			makeForPath (lastExpandCollapsePath);
		}
		
		public void makeForPath (TreePath path){
			if (path != null) {
				if (progressItemTree.isExpanded (path)){
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
				final TreePath path = progressItemTree.getPathForLocation (x, y);
				/* varia selezione corrente */
				progressItemTree.setSelectionPath (path);
				treeExpandCollapseAction.makeForPath (path);
				progressTreePopup.show (progressItemTree, x, y);
			}
		}
	}
	
	
}

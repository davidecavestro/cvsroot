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
import javax.swing.tree.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;

/**
 *
 * @author  davide
 */
public class MainForm extends javax.swing.JFrame implements Observer {
	
	private Application application;
	private ProgressItemCellRenderer progressItemCellRenderer;
	/** Creates new form MainForm */
	public MainForm(Application app) {
		this.application = app;
		progressItemCellRenderer = new ProgressItemCellRenderer();
		initComponents();
		postInitComponents();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {
		progressTimer = new org.netbeans.examples.lib.timerbean.Timer();
		jPopupMenu = new javax.swing.JPopupMenu();
		jPanelMain = new javax.swing.JPanel();
		jPanelMainToolbar = new javax.swing.JPanel();
		jToolBarMain = new javax.swing.JToolBar();
		nodeCreateButton = new javax.swing.JButton();
		nodeDeleteButton = new javax.swing.JButton();
		jSeparator2 = new javax.swing.JSeparator();
		startButton = new javax.swing.JButton();
		stopButton = new javax.swing.JButton();
		statusBar = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jobProgress = new javax.swing.JProgressBar();
		jLabel3 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jSplitPane1 = new javax.swing.JSplitPane();
		progressTree = new javax.swing.JTree();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		progressTable = new com.ost.timekeeper.ui.ProgressesTable();
		this.application.addObserver (progressTable);
		jTable2 = new javax.swing.JTable();
		jPanel2 = new javax.swing.JPanel();
		jLayeredPane1 = new javax.swing.JLayeredPane();
		jLabel4 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		jTextField2 = new javax.swing.JTextField();
		jLabel6 = new javax.swing.JLabel();
		jTextField3 = new javax.swing.JTextField();
		jMenuBarMain = new javax.swing.JMenuBar();
		jMenuFile = new javax.swing.JMenu();
		jMenuItemNew = new javax.swing.JMenuItem();
		jMenuItemOpen = new javax.swing.JMenuItem();
		jMenuExport = new javax.swing.JMenu();
		jMenuItemClose = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		jMenuItemFinish = new javax.swing.JMenuItem();
		jMenuEdit = new javax.swing.JMenu();
		jMenuItemCut = new javax.swing.JMenuItem();
		jMenuItemCopy = new javax.swing.JMenuItem();
		jMenuItemPaste = new javax.swing.JMenuItem();
		jMenuActions = new javax.swing.JMenu();
		jMenuItemStart = new javax.swing.JMenuItem();
		jMenuItemStop = new javax.swing.JMenuItem();
		jMenuItemCreateProject = new javax.swing.JMenuItem();
		jMenuItemDeleteProject = new javax.swing.JMenuItem();
		jMenuItemOpenProject = new javax.swing.JMenuItem();
		jMenuItemSaveProject = new javax.swing.JMenuItem();
		jMenuItemCreateNode = new javax.swing.JMenuItem();
		jMenuItemDeleteNode = new javax.swing.JMenuItem();
		jMenuItemTreeExpandCollapse = new javax.swing.JMenuItem();
		jMenuTools = new javax.swing.JMenu();
		jMenuItemOptions = new javax.swing.JMenuItem();
		jMenuHelp = new javax.swing.JMenu();
		jMenuItemAbout = new javax.swing.JMenuItem();
		jMenuItemHelp = new javax.swing.JMenuItem();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ResourceSupplier.getString(ResourceClass.UI, "global", "app.title"));
		setBackground(new java.awt.Color(204, 204, 255));
		setFont(new java.awt.Font("Default", 0, 10));
		setIconImage(getIconImage());
		setName("frameMain");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});
		
		jPanelMain.setLayout(new java.awt.BorderLayout());
		
		jPanelMainToolbar.setLayout(new java.awt.BorderLayout());
		
		jToolBarMain.setBorder(new javax.swing.border.EtchedBorder());
		jToolBarMain.setRollover(true);
		nodeCreateButton.setAction(this.application.getNodeCreateAction());
		jToolBarMain.add(nodeCreateButton);
		
		nodeDeleteButton.setAction(this.application.getNodeDeleteAction());
		jToolBarMain.add(nodeDeleteButton);
		
		jToolBarMain.add(jSeparator2);
		
		startButton.setAction(this.application.getProgressStartAction());
		startButton.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.start"));
		jToolBarMain.add(startButton);
		
		stopButton.setAction(this.application.getProgressStopAction());
		jToolBarMain.add(stopButton);
		
		jPanelMainToolbar.add(jToolBarMain, java.awt.BorderLayout.NORTH);
		
		statusBar.setLayout(new javax.swing.BoxLayout(statusBar, javax.swing.BoxLayout.X_AXIS));
		
		statusBar.setMinimumSize(new java.awt.Dimension(10, 30));
		jLabel1.setFont(new java.awt.Font("Default", 0, 12));
		jLabel1.setText("statusLabel");
		jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
		statusBar.add(jLabel1);
		
		jLabel2.setFont(new java.awt.Font("Default", 0, 12));
		jLabel2.setText("jLabel2");
		jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
		statusBar.add(jLabel2);
		
		jobProgress.setBorder(null);
		statusBar.add(jobProgress);
		
		jLabel3.setFont(new java.awt.Font("Default", 0, 12));
		jLabel3.setText("jLabel3");
		jLabel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
		statusBar.add(jLabel3);
		
		jPanelMainToolbar.add(statusBar, java.awt.BorderLayout.SOUTH);
		
		jPanel1.setLayout(new java.awt.BorderLayout());
		
		jSplitPane1.setToolTipText(ResourceSupplier.getString(ResourceClass.UI, "global", "controls.splitter.tooltip"));
		progressTree.setCellEditor(new javax.swing.tree.DefaultTreeCellEditor(progressTree, progressItemCellRenderer));
		progressTree.setCellRenderer(progressItemCellRenderer);
		progressTree.setMaximumSize(null);
		progressTree.setMinimumSize(null);
		progressTree.setModel(null);
		progressTree.setAutoscrolls(true);
		progressTree.setPreferredSize(new java.awt.Dimension(100, 200));
		jSplitPane1.setLeftComponent(progressTree);
		
		jTabbedPane1.setMaximumSize(null);
		jTabbedPane1.addTab("Progresses", progressTable);
		
		jTable2.setModel(new javax.swing.table.DefaultTableModel(
		new Object [][] {
			{null, null, null, null},
			{null, null, null, null},
			{null, null, null, null},
			{null, null, null, null}
		},
		new String [] {
			"Title 1", "Title 2", "Title 3", "Title 4"
		}
		));
		jTable2.setMaximumSize(null);
		jTable2.setMinimumSize(null);
		jTabbedPane1.addTab("tab3", jTable2);
		
		jPanel2.setLayout(new java.awt.BorderLayout());
		
		jPanel2.setAutoscrolls(true);
		jLayeredPane1.setAutoscrolls(true);
		jLabel4.setText("jLabel4");
		jLabel4.setBounds(20, 20, -1, -1);
		jLayeredPane1.add(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jTextField1.setText("jTextField1");
		jTextField1.setMaximumSize(null);
		jTextField1.setMinimumSize(null);
		jTextField1.setBounds(90, 100, 300, -1);
		jLayeredPane1.add(jTextField1, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jLabel5.setText("jLabel4");
		jLabel5.setBounds(20, 100, -1, -1);
		jLayeredPane1.add(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jTextField2.setText("jTextField1");
		jTextField2.setMaximumSize(null);
		jTextField2.setMinimumSize(null);
		jTextField2.setBounds(90, 60, 300, -1);
		jLayeredPane1.add(jTextField2, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jLabel6.setText("jLabel4");
		jLabel6.setBounds(20, 60, -1, -1);
		jLayeredPane1.add(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jTextField3.setText("jTextField1");
		jTextField3.setMaximumSize(null);
		jTextField3.setMinimumSize(null);
		jTextField3.setBounds(90, 20, 300, -1);
		jLayeredPane1.add(jTextField3, javax.swing.JLayeredPane.DEFAULT_LAYER);
		
		jPanel2.add(jLayeredPane1, java.awt.BorderLayout.CENTER);
		
		jTabbedPane1.addTab("tab3", jPanel2);
		
		jSplitPane1.setRightComponent(jTabbedPane1);
		
		jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);
		
		jPanelMainToolbar.add(jPanel1, java.awt.BorderLayout.CENTER);
		
		jPanelMain.add(jPanelMainToolbar, java.awt.BorderLayout.CENTER);
		
		getContentPane().add(jPanelMain, java.awt.BorderLayout.CENTER);
		
		jMenuFile.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "file"));
		jMenuItemNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemNew.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "file.new"));
		jMenuFile.add(jMenuItemNew);
		
		jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemOpen.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "file.open"));
		jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemOpenActionPerformed(evt);
			}
		});
		
		jMenuFile.add(jMenuItemOpen);
		
		jMenuExport.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "file.export"));
		jMenuFile.add(jMenuExport);
		
		jMenuItemClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemClose.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "file.close"));
		jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemCloseActionPerformed(evt);
			}
		});
		
		jMenuFile.add(jMenuItemClose);
		
		jMenuFile.add(jSeparator1);
		
		jMenuItemFinish.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemFinish.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "file.exit"));
		jMenuFile.add(jMenuItemFinish);
		
		jMenuBarMain.add(jMenuFile);
		
		jMenuEdit.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "edit"));
		jMenuItemCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemCut.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "edit.cut"));
		jMenuEdit.add(jMenuItemCut);
		
		jMenuItemCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemCopy.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "edit.copy"));
		jMenuEdit.add(jMenuItemCopy);
		
		jMenuItemPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemPaste.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "edit.paste"));
		jMenuEdit.add(jMenuItemPaste);
		
		jMenuBarMain.add(jMenuEdit);
		
		jMenuActions.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions"));
		
		jMenuItemCreateProject.setAction(this.application.getProjectCreateAction());
		jMenuActions.add(jMenuItemCreateProject);
		
		jMenuItemDeleteProject.setAction(this.application.getProjectDeleteAction());
		jMenuActions.add(jMenuItemDeleteProject);
		
		jMenuItemOpenProject.setAction(this.application.getProjectOpenAction());
		jMenuActions.add(jMenuItemOpenProject);
		
		jMenuItemSaveProject.setAction(this.application.getProjectSaveAction());
		jMenuActions.add(jMenuItemSaveProject);
		
		jMenuActions.addSeparator();
		
		jMenuItemCreateNode.setAction(this.application.getNodeCreateAction());
		jMenuActions.add(jMenuItemCreateNode);
		
		jMenuItemDeleteNode.setAction(this.application.getNodeDeleteAction());
		jMenuActions.add(jMenuItemDeleteNode);
		
		jMenuActions.addSeparator();
		
//		jMenuItemStart.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemStart.setAction(this.application.getProgressStartAction());
//		jMenuItemStart.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.start"));
		jMenuActions.add(jMenuItemStart);
		
//		jMenuItemStop.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemStop.setAction(this.application.getProgressStopAction());
//		jMenuItemStop.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "actions.stop"));
		jMenuActions.add(jMenuItemStop);
		
		jMenuActions.addSeparator();
		jMenuItemTreeExpandCollapse.setAction(treeExpandCollapseAction);
		jMenuActions.add(jMenuItemTreeExpandCollapse);
		
		jMenuBarMain.add(jMenuActions);
		
		jMenuTools.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "tool"));
		jMenuItemOptions.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "tools.options"));
		jMenuTools.add(jMenuItemOptions);
		
		jMenuBarMain.add(jMenuTools);
		
		jMenuHelp.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "help"));
		jMenuItemAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_QUOTE, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
		jMenuItemAbout.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "help.about"));
		jMenuHelp.add(jMenuItemAbout);
		
		jMenuItemHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
		jMenuItemHelp.setText(ResourceSupplier.getString(ResourceClass.UI, "menu", "help.help"));
		jMenuItemHelp.setActionCommand("Help");
		jMenuHelp.add(jMenuItemHelp);
		
		jMenuBarMain.add(jMenuHelp);
		
		setJMenuBar(jMenuBarMain);
		
		jPopupMenu.add(treeExpandCollapseAction);
		jPopupMenu.addSeparator();
		
		progressTree.add (jPopupMenu);
		progressTree.addMouseListener (new PopupTrigger ());
		pack();
	}
	
	private void jMenuItemCloseActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}
	
	private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}
	
	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) {
		System.exit(0);
	}
	
	
	
	// Variables declaration - do not modify
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLayeredPane jLayeredPane1;
	private javax.swing.JMenu jMenuActions;
	private javax.swing.JMenuBar jMenuBarMain;
	private javax.swing.JMenu jMenuEdit;
	private javax.swing.JMenu jMenuExport;
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
	private javax.swing.JMenuItem jMenuItemNew;
	private javax.swing.JMenuItem jMenuItemCreateProject;
	private javax.swing.JMenuItem jMenuItemDeleteProject;
	private javax.swing.JMenuItem jMenuItemOpenProject;
	private javax.swing.JMenuItem jMenuItemSaveProject;
	private javax.swing.JMenuItem jMenuItemCreateNode;
	private javax.swing.JMenuItem jMenuItemOpen;
	private javax.swing.JMenuItem jMenuItemOptions;
	private javax.swing.JMenuItem jMenuItemPaste;
	private javax.swing.JMenuItem jMenuItemStart;
	private javax.swing.JMenuItem jMenuItemStop;
	private javax.swing.JMenu jMenuTools;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanelMain;
	private javax.swing.JPanel jPanelMainToolbar;
	private javax.swing.JPopupMenu jPopupMenu;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JTabbedPane jTabbedPane1;
	private com.ost.timekeeper.ui.ProgressesTable progressTable;
	private javax.swing.JTable jTable2;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;
	private javax.swing.JTextField jTextField3;
	private javax.swing.JToolBar jToolBarMain;
	private javax.swing.JProgressBar jobProgress;
	private javax.swing.JTree progressTree;
	private javax.swing.JButton nodeCreateButton;
	private javax.swing.JButton nodeDeleteButton;
	private org.netbeans.examples.lib.timerbean.Timer progressTimer;
	private javax.swing.JButton startButton;
	private javax.swing.JPanel statusBar;
	private javax.swing.JButton stopButton;
	// End of variables declaration
	
	private void postInitComponents() {
		this.progressTree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e){
				if (e.getNewLeadSelectionPath() == null) {
					//gestione caso cancellazione elemento selezionato
					return;
				}
				TreePath thePath = e.getPath();
				application.setSelectedItem((ProgressItem)thePath.getLastPathComponent());
				treeExpandCollapseAction.makeForPath (thePath);
			}
		});
		this.progressTree.setSelectionRow(0);
		
		this.progressTree.addTreeSelectionListener(this.progressTable);
	}
	
	private final void initTreeModelListeners (){
		this.progressTree.getModel().addTreeModelListener(new TreeModelListener(){
			public void treeNodesChanged(TreeModelEvent e){repaint();}
			public void treeNodesInserted(TreeModelEvent e){repaint();}
			public void treeNodesRemoved(TreeModelEvent e){repaint();}
			public void treeStructureChanged(TreeModelEvent e){repaint();}
		});
		this.progressTree.getModel().addTreeModelListener(this.progressTable);
	}
	
	private final void initTableModelListeners (){
		this.progressTable.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e){repaint();}
		});
//		this.progressTable.getModel().addTableModelListener(this.progressTable);
	}
	
	private ProgressTreeModel progressTreeModel;
	public ProgressTreeModel getProgressTreeModel(){
		return this.progressTreeModel;
	}
	
	private ProgressTableModel progressTableModel;
	public ProgressTableModel getProgressTableModel(){
		return this.progressTableModel;
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals ("project")){
				this.progressTreeModel = new ProgressTreeModel(application.getProject());
				this.progressTree.setModel(this.progressTreeModel);
				initTreeModelListeners ();
				
				this.progressTableModel = new ProgressTableModel(application.getSelectedItem());
				this.progressTable.setModel(this.progressTableModel);
				initTableModelListeners ();
			}
		}
	}
	
	private TreePath lastExpandCollapsePath;
	private TreeExpandCollapseAction treeExpandCollapseAction = new TreeExpandCollapseAction ();
	private final class TreeExpandCollapseAction extends javax.swing.AbstractAction {
		
		/** Creates a new instance of TreeExpandCollapseAction */
		public TreeExpandCollapseAction() {
			super();
			this.setEnabled(true);
		}
		
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (lastExpandCollapsePath==null){
				return;
			}
			if (progressTree.isExpanded(lastExpandCollapsePath)){
				progressTree.collapsePath(lastExpandCollapsePath);
			} else {
				progressTree.expandPath(lastExpandCollapsePath);
			}
			makeForPath (lastExpandCollapsePath);
		}
		
		public void makeForPath (TreePath path){
			if (path != null) {
				if (progressTree.isExpanded(path)){
					treeExpandCollapseAction.putValue(Action.NAME, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.collapse"));
				} else {
					treeExpandCollapseAction.putValue(Action.NAME, ResourceSupplier.getString (ResourceClass.UI, "menu", "actions.expand"));
				}
				lastExpandCollapsePath = path;
			}
		}
	}
	
	class PopupTrigger extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			handlePopUp (e);
		}
		public void mousePressed(MouseEvent e) {
			handlePopUp (e);
		}
		public void handlePopUp (MouseEvent e){
			if (e.isPopupTrigger()) {
				int x = e.getX();
				int y = e.getY();
				TreePath path = progressTree.getPathForLocation(x, y);
				treeExpandCollapseAction.makeForPath (path);
				jPopupMenu.show(progressTree, x, y);
			}
		}
	}
		
}

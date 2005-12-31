/*
 * MainWIndow.java
 *
 * Created on 26 novembre 2005, 14.56
 */

package com.davidecavestro.rbe.gui;

import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.common.gui.table.TableSorter;
import com.davidecavestro.common.util.action.ActionNotifier;
import com.davidecavestro.common.util.action.ActionNotifierImpl;
import com.davidecavestro.common.util.file.CustomFileFilter;
import com.davidecavestro.common.util.file.FileUtils;
import com.davidecavestro.rbe.ApplicationContext;
import com.davidecavestro.rbe.gui.actions.FindAction;
import com.davidecavestro.rbe.gui.actions.NewBundleAction;
import com.davidecavestro.rbe.gui.actions.SaveAction;
import com.davidecavestro.rbe.gui.search.*;
import com.davidecavestro.rbe.model.DefaultResourceBundleModel;
import com.davidecavestro.rbe.model.LocaleComparator;
import com.davidecavestro.rbe.model.LocalizationProperties;
import com.davidecavestro.rbe.model.ResourceBundleModel;
import com.davidecavestro.rbe.model.event.ResourceBundleModelEvent;
import com.davidecavestro.rbe.model.event.ResourceBundleModelListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.decorator.*;

/**
 * La finestra principale dell'applicazione.
 *
 * @author  davide
 */
public class MainWindow extends javax.swing.JFrame implements PersistentComponent, ActionNotifier {
	
	private final ActionNotifierImpl _actionNotifier;
	private final LocalizationTableModel _localizationTableModel;
	
	private final ApplicationContext _context;
	private final WindowManager _wm;
	private final PatternFinder _matcher;
	
	
	/** 
	 * Costruttore.
	 */
	public MainWindow (final ApplicationContext context){
		this._actionNotifier = new ActionNotifierImpl ();
		this._context = context;
		this._wm = context.getWindowManager ();
		this._localizationTableModel = new LocalizationTableModel (this._context.getModel ());
		this._matcher = new PatternFinder ();
		this._matcher.addPropertyChangeListener (_context.getActionManager ().getFindNextAction ());
		
		this._context.getActionManager ().getFindNextAction ().addActionListener (new ActionListener (){
			public void actionPerformed(ActionEvent e){
				
			}
		});
		
		initComponents ();
		updateRecentPathMenu ();
		this._context.getUIPersisteer ().register (new PersistenceTreeAdapter (this.treeScrollPane));
		final Point p = new Point ();
		
		/*
		 * notifica la textarea del cambio di selezione, aggiornandola
		 */
		final ListSelectionListener l = new ListSelectionListener (){
			public void valueChanged (ListSelectionEvent e) {
				int col = valuesTable.getColumnModel ().getSelectionModel ().getLeadSelectionIndex ();
				int row = valuesTable.getSelectionModel ().getLeadSelectionIndex ();
				if (col>=0 && row>=0){
					if (!valueTextArea.hasFocus ()){
						valueTextArea.setEnabled (false);
						valueTextArea.setText ((String)valuesTable.getValueAt (row, col));
						valueTextArea.setEnabled (col!=0);
						p.setLocation (valuesTable.convertColumnIndexToModel (col), 
						((/*@todo rimuovere il cast alla fine (NEtbeans non supporta l'editing di JXTable) */JXTable)valuesTable).convertRowIndexToModel (row));
					}
				} else {
					valueTextArea.setEnabled (false);
					valueTextArea.setText (null);
				}
				
			}
		};
		valuesTable.getSelectionModel ().addListSelectionListener (l);
		valuesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (l);
		

		/*
		 * propaga le modifiche dalla textarea al modello
		 */
		valueTextArea.getDocument ().addDocumentListener (new DocumentListener (){
			public void insertUpdate(DocumentEvent e){apply (e);}
			public void removeUpdate(DocumentEvent e){apply (e);}
			public void changedUpdate(DocumentEvent e){apply (e);}
			
			public void apply (DocumentEvent e){
				if (valueTextArea.isEnabled () && valueTextArea.hasFocus ()) {
					/* propaga modifiche alla tabella modifica solo se textarea abilitata, altrimenti sarebbe un evento spurio */
					valuesTable.getModel ().setValueAt (valueTextArea.getText (), (int)p.getY (), (int)p.getX ());
				}
			}
		});
		
		this._context.getModel ().addPropertyChangeListener ("isModified", new PropertyChangeListener (){
			public void propertyChange(PropertyChangeEvent evt){
				modifiedLabel.setText (_context.getModel ().isModified ()?
				java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("EDT"):
				"");
			}
		});
		
		this._context.getModel ().addPropertyChangeListener (new PropertyChangeListener (){
				public void propertyChange (PropertyChangeEvent e){
					String pName = e.getPropertyName ();
					if (pName.equals ("name") || pName.equalsIgnoreCase ("isModified")){
						setTitle (prepareTitle (_context.getModel ()));
					}
				}
			}
		);
		
		
		
		
		/*test componente ricerca*/
//		SearchHighlighter sh = new SearchHighlighter (null, Color.yellow);
//		sh.setEnabled (true);
//		Highlighter[]   highlighters = new Highlighter[] {
///*
//			new AlternateRowHighlighter (Color.white,
//			new Color (0xF0, 0xF0, 0xE0), null),
// */
////			new PatternHighlighter (null, Color.red, "^s", 0, 0)
//			sh
//			
//		};
//		HighlighterPipeline highlighterPipeline = new HighlighterPipeline(highlighters);
//		valuesTable.setHighlighters (highlighterPipeline);
		
//		mainToolbar.add (new JXSearchPanel ());		
		
//        KeyStroke findStroke = KeyStroke.getKeyStroke("control F");
//        valuesTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(findStroke, "find");
		
		
		this._matcher.setPattern ("aba");
		valuesTable.setCellSelectionEnabled (true);

	}
	
	
	private String prepareTitle (DefaultResourceBundleModel model){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("URBE").append (" - Bundle ").append (model.getName ());
		if (model.isModified ()){
			sb.append (" [")
			.append (java.util.ResourceBundle.getBundle ("com.davidecavestro.rbe.gui.res").getString ("Modified"))
			.append ("]");
			
		}
		return sb.toString ();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        borderLayout1 = new java.awt.BorderLayout();
        bundlePopupMenu = new javax.swing.JPopupMenu();
        addLocaleMenuItem = new javax.swing.JMenuItem();
        localePopupMenu = new javax.swing.JPopupMenu();
        deleteLocaleMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        addEntryMenuItem = new javax.swing.JMenuItem();
        entryPopupMenu = new javax.swing.JPopupMenu();
        deleteEntryMenuItem = new javax.swing.JMenuItem();
        tablePopupMenu = new javax.swing.JPopupMenu();
        openFileChooser = new javax.swing.JFileChooser();
        saveFileChooser = new javax.swing.JFileChooser();
        mainPanel = new javax.swing.JPanel();
        statusPanel = new javax.swing.JPanel();
        modifiedLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        mainToolbar = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        tree_table_splitPane = new javax.swing.JSplitPane();
        treeScrollPane = new javax.swing.JScrollPane();
        bundleTree = new javax.swing.JTree();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        deleteEntryButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        valueTextArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        valuesTable = new JXTable (this._localizationTableModel);
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        recentMenu = new javax.swing.JMenu();
        jSeparator2 = new javax.swing.JSeparator();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        findMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        bundlePopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        addLocaleMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Add_Locale"));
        addLocaleMenuItem.setActionCommand("addlocale");
        addLocaleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLocaleMenuItemActionPerformed(evt);
            }
        });

        bundlePopupMenu.add(addLocaleMenuItem);

        localePopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteLocaleMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Delete_Locale"));
        deleteLocaleMenuItem.setActionCommand("deleteLocale");
        deleteLocaleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteLocaleMenuItemActionPerformed(evt);
            }
        });

        localePopupMenu.add(deleteLocaleMenuItem);

        localePopupMenu.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(addEntryMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Add_Entry"));
        addEntryMenuItem.setActionCommand("addEntry");
        addEntryMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntryMenuItemActionPerformed(evt);
            }
        });

        localePopupMenu.add(addEntryMenuItem);

        entryPopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(deleteEntryMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Delete_Entry"));
        entryPopupMenu.add(deleteEntryMenuItem);

        tablePopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        openFileChooser.setCurrentDirectory(null);
        openFileChooser.setFileFilter(new CustomFileFilter (
            new String []{FileUtils.properties},
            new String []{"Properties files"}
        ));
        openFileChooser.setFont(new java.awt.Font("Dialog", 0, 12));
        saveFileChooser.setCurrentDirectory(null);
        saveFileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        saveFileChooser.setFileFilter(new CustomFileFilter (
            new String []{FileUtils.properties},
            new String []{"Properties files"}
        ));
        saveFileChooser.setFont(new java.awt.Font("Dialog", 0, 12));
        saveFileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileChooserActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        mainPanel.setLayout(new java.awt.BorderLayout());

        statusPanel.setLayout(new javax.swing.BoxLayout(statusPanel, javax.swing.BoxLayout.X_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(modifiedLabel, "   ");
        modifiedLabel.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusPanel.add(modifiedLabel);

        jLabel2.setText("jLabel2");
        jLabel2.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusPanel.add(jLabel2);

        statusPanel.add(progressBar);

        jLabel3.setText("jLabel3");
        jLabel3.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusPanel.add(jLabel3);

        mainPanel.add(statusPanel, java.awt.BorderLayout.SOUTH);

        mainToolbar.setRollover(true);
        jButton1.setText("jButton1");
        mainToolbar.add(jButton1);

        jButton2.setText("jButton2");
        mainToolbar.add(jButton2);

        jButton3.setText("jButton3");
        mainToolbar.add(jButton3);

        mainPanel.add(mainToolbar, java.awt.BorderLayout.NORTH);

        tree_table_splitPane.setMaximumSize(null);
        tree_table_splitPane.setOneTouchExpandable(true);
        treeScrollPane.setMaximumSize(null);
        treeScrollPane.setMinimumSize(new java.awt.Dimension(60, 50));
        bundleTree.setCellRenderer(	new DefaultTreeCellRenderer () {
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel,
                boolean expanded,
                boolean leaf, int row,
                boolean hasFocus) {
                final JLabel label = (JLabel)super.getTreeCellRendererComponent (tree, value, sel, expanded, leaf, row, hasFocus);
                if (value == LocalizationProperties.DEFAULT) {
                    label.setText ("Default");
                }
                return label;
            }
        }
    );
    bundleTree.setMinimumSize(new java.awt.Dimension(50, 50));
    bundleTree.setModel(new LocalizationTreeModel (this._wm.getApplicationContext ().getModel ()));
    bundleTree.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            bundleTreeMousePressed(evt);
        }
    });

    treeScrollPane.setViewportView(bundleTree);

    tree_table_splitPane.setLeftComponent(treeScrollPane);

    jPanel3.setLayout(new java.awt.BorderLayout());

    jPanel1.setLayout(new java.awt.GridBagLayout());

    jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Comment:"));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
    jPanel1.add(jLabel4, gridBagConstraints);

    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
    org.openide.awt.Mnemonics.setLocalizedText(jLabel5, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Value:"));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
    jPanel1.add(jLabel5, gridBagConstraints);

    jPanel2.setLayout(new java.awt.GridBagLayout());

    jButton4.setFont(new java.awt.Font("Dialog", 0, 12));
    org.openide.awt.Mnemonics.setLocalizedText(jButton4, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Add_Entry"));
    jButton4.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Add_a_new_entry"));
    jButton4.setActionCommand("addentry");
    jButton4.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton4ActionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
    jPanel2.add(jButton4, gridBagConstraints);

    deleteEntryButton.setFont(new java.awt.Font("Dialog", 0, 12));
    org.openide.awt.Mnemonics.setLocalizedText(deleteEntryButton, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Delete_Entry"));
    deleteEntryButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Delete_an_entry"));
    deleteEntryButton.setActionCommand("deleteentry");
    deleteEntryButton.setEnabled(false);
    valuesTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener (){
        public void valueChanged(ListSelectionEvent e){
            deleteEntryButton.setEnabled (valuesTable.getSelectedRowCount ()>0);
        }
    });
    deleteEntryButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            deleteEntryButtonActionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
    jPanel2.add(deleteEntryButton, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
    gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
    jPanel1.add(jPanel2, gridBagConstraints);

    jTextArea2.setRows(2);
    jTextArea2.setWrapStyleWord(true);
    jTextArea2.setBorder(new javax.swing.border.EtchedBorder());
    jScrollPane1.setViewportView(jTextArea2);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
    jPanel1.add(jScrollPane1, gridBagConstraints);

    valueTextArea.setRows(2);
    valueTextArea.setWrapStyleWord(true);
    valueTextArea.setBorder(new javax.swing.border.EtchedBorder());
    valueTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            valueTextAreaKeyPressed(evt);
        }
    });

    jScrollPane2.setViewportView(valueTextArea);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
    jPanel1.add(jScrollPane2, gridBagConstraints);

    jPanel3.add(jPanel1, java.awt.BorderLayout.SOUTH);

    jScrollPane3.setBackground(javax.swing.UIManager.getDefaults().getColor("Table.background"));
    jScrollPane3.getViewport ().setBackground (javax.swing.UIManager.getDefaults().getColor("Table.background"));
    valuesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
    valuesTable.setCellEditor(new ValueCellEditor ());
    valuesTable.setFocusCycleRoot(true);
    valuesTable.setGridColor(new java.awt.Color(204, 204, 204));
    valuesTable.setMinimumSize(new java.awt.Dimension(10, 10));
    valuesTable.setDefaultRenderer (Object.class, new DefaultTableCellRenderer () {
        private final Color inactiveCaptionColor = javax.swing.UIManager.getDefaults().getColor("inactiveCaption");
        private final Color tableBackgroundColor = javax.swing.UIManager.getDefaults().getColor("Table.background");
        //	private final Color keyBackgroundColor = javax.swing.UIManager.getDefaults().getColor("control");
        private final Color keyBackgroundColor = javax.swing.UIManager.getDefaults().getColor("Table.background");
        //	private final Color keyForegroundColor = javax.swing.UIManager.getDefaults().getColor("textInactiveText");
        private final Color keyForegroundColor = javax.swing.UIManager.getDefaults().getColor("Menu.acceleratorForeground");
        private final Color valueForegroundColor = javax.swing.UIManager.getDefaults().getColor("Table.foreground");
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

            final JLabel label = (JLabel)super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
            if (0 == column) {
                /* colonna chiavi */
                label.setFont (label.getFont ().deriveFont (Font.BOLD));
                label.setBackground (keyBackgroundColor);
                label.setForeground (keyForegroundColor);
            } else {
                label.setFont (label.getFont ().deriveFont (Font.PLAIN));
                if (null==value) {
                    label.setBackground (inactiveCaptionColor);
                } else {
                    label.setBackground (tableBackgroundColor);
                }
                label.setForeground (valueForegroundColor);
            }
            return label;
        }

    });

    valuesTable.setDefaultEditor (String.class, new ValueCellEditor ());

    /*
    * editor (shortcut per cancellazione
        */
        valuesTable.addKeyListener (new KeyAdapter (){
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown ()){
                    if (e.getKeyCode ()==KeyEvent.VK_DELETE){
                        int rowIdx = valuesTable.getSelectedRow ();
                        int colIdx = valuesTable.getSelectedColumn ();
                        if (colIdx>=0 && rowIdx>=0){
                            //barbatrucco per evitare editazione spuria
                            //valuesTable.editCellAt(rowIdx, colIdx);
                            valuesTable.setValueAt (null, rowIdx, colIdx);
                            e.consume ();
                        }
                    } else if (e.getKeyCode ()==KeyEvent.VK_SPACE){
                        int rowIdx = valuesTable.getSelectedRow ();
                        int colIdx = valuesTable.getSelectedColumn ();
                        if (colIdx>=0 && rowIdx>=0){
                            //barbatrucco per evitare editazione spuria
                            //valuesTable.editCellAt(rowIdx, colIdx);
                            valuesTable.setValueAt ("", rowIdx, colIdx);
                            e.consume ();
                        }
                    }
                }
            }
        });
        valuesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                valuesTableMousePressed(evt);
            }
        });

        jScrollPane3.setViewportView(valuesTable);

        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        tree_table_splitPane.setRightComponent(jPanel3);

        mainPanel.add(tree_table_splitPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        menuBar.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(fileMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&File"));
        fileMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        newMenuItem.setAction(new NewBundleAction (this._wm.getApplicationContext ().getModel (), this._wm));
        newMenuItem.setText("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(newMenuItem);

        org.openide.awt.Mnemonics.setLocalizedText(openMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Open"));
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(openMenuItem);

        org.openide.awt.Mnemonics.setLocalizedText(recentMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Recent"));
        fileMenu.add(recentMenu);

        fileMenu.add(jSeparator2);

        final SaveAction saveAction = new SaveAction (this._wm.getApplicationContext ().getModel ());
        saveMenuItem.setAction(saveAction);
        org.openide.awt.Mnemonics.setLocalizedText(saveMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Save"));
        _context.getModel ().addPropertyChangeListener (saveAction);
        fileMenu.add(saveMenuItem);

        org.openide.awt.Mnemonics.setLocalizedText(saveAsMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Save_&As_..."));
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(saveAsMenuItem);

        fileMenu.add(jSeparator3);

        org.openide.awt.Mnemonics.setLocalizedText(exitMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("E&xit"));
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        org.openide.awt.Mnemonics.setLocalizedText(editMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Edit"));
        editMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        undoMenuItem.setAction(_context.getUndoManager ().getUndoAction());
        editMenu.add(undoMenuItem);

        redoMenuItem.setAction(_context.getUndoManager ().getRedoAction());
        editMenu.add(redoMenuItem);

        editMenu.add(jSeparator4);

        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setText("Delete");
        deleteMenuItem.setActionCommand("delete");
        editMenu.add(deleteMenuItem);

        editMenu.add(jSeparator5);

        findMenuItem.setAction(new FindAction (this._context));
        org.openide.awt.Mnemonics.setLocalizedText(findMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Find"));
        findMenuItem.setActionCommand("find");
        editMenu.add(findMenuItem);

        menuBar.add(editMenu);

        org.openide.awt.Mnemonics.setLocalizedText(helpMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("&Help"));
        helpMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }//GEN-END:initComponents

	private void valueTextAreaKeyPressed (java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valueTextAreaKeyPressed
		if (evt.isControlDown ()){
			/* SHORTCUTS */
			if (evt.getKeyCode ()==KeyEvent.VK_DELETE){
				/* imposta a null */
				valueTextArea.setText (null);
			} else if (evt.getKeyCode ()==KeyEvent.VK_SPACE){
				/* imposta a stringa vuota */
				valueTextArea.setText ("");
			}
		}
	}//GEN-LAST:event_valueTextAreaKeyPressed

	private void valuesTableMousePressed (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_valuesTableMousePressed
		if (evt.getButton ()==MouseEvent.BUTTON3){
			if (evt.getSource ()==this.valuesTable){
				final int x = evt.getX ();
				final int y = evt.getY ();
				
				tablePopupMenu.show ((Component)evt.getSource (), x, y);
			}
		}
	}//GEN-LAST:event_valuesTableMousePressed

	private void deleteLocaleMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteLocaleMenuItemActionPerformed
		if (treePopupNode instanceof Locale){
			deleteLocale ((Locale)treePopupNode);
		}
	}//GEN-LAST:event_deleteLocaleMenuItemActionPerformed

	private void deleteEntryButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEntryButtonActionPerformed
		deleteEntry (this._localizationTableModel._keys[this.valuesTable.getSelectedRow ()]);
	}//GEN-LAST:event_deleteEntryButtonActionPerformed

	private void jButton4ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		showAddEntryDialog ();
	}//GEN-LAST:event_jButton4ActionPerformed

	private void saveAsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
		saveFileChooser.setSelectedFile (new File (this._context.getModel ().getName ()+".properties"));
		if (saveFileChooser.showSaveDialog (this)==JFileChooser.APPROVE_OPTION){
			try {
				this._context.getModel ().saveAs (saveFileChooser.getSelectedFile (), "Created by URBE");
			} catch (Exception e){
				e.printStackTrace (System.err);
			}
		}
	}//GEN-LAST:event_saveAsMenuItemActionPerformed

	private void saveFileChooserActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileChooserActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_saveFileChooserActionPerformed

	private void openMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
		if (!checkForDataLoss ()){
			return;
		}
		if (openFileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			final File file = openFileChooser.getSelectedFile ();
			openFile (file);
		}
	}//GEN-LAST:event_openMenuItemActionPerformed

	private void addLocaleMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLocaleMenuItemActionPerformed
		this._actionNotifier.fireActionPerformed (new ActionEvent (this, -1, "showAddLocaleDialog"));
	}//GEN-LAST:event_addLocaleMenuItemActionPerformed

	Object treePopupNode;
	private void bundleTreeMousePressed (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bundleTreeMousePressed
		if (evt.getButton ()==MouseEvent.BUTTON3){
			if (evt.getSource ()==this.bundleTree){
				final int x = evt.getX ();
				final int y = evt.getY ();
				final TreePath path = this.bundleTree.getPathForLocation (x, y);
				if (path!=null){
					final Object node = path.getLastPathComponent ();
					treePopupNode = node;
					if (node instanceof ResourceBundleModel){
						bundlePopupMenu.show ((Component)evt.getSource (), x, y);
					} else if (node instanceof Locale){
						deleteLocaleMenuItem.setEnabled (node!=LocalizationProperties.DEFAULT);
						localePopupMenu.show ((Component)evt.getSource (), x, y);
					} else if (node instanceof String){
						entryPopupMenu.show ((Component)evt.getSource (), x, y);
					}
				}
			}
		}
	}//GEN-LAST:event_bundleTreeMousePressed

	private void newMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed

	}//GEN-LAST:event_newMenuItemActionPerformed

	private void addEntryMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryMenuItemActionPerformed
		if (treePopupNode instanceof Locale){
			showAddEntryDialog ((Locale)treePopupNode);
		} else {
			showAddEntryDialog ();
		}
	}//GEN-LAST:event_addEntryMenuItemActionPerformed
	
	private void exitMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
		System.exit (0);
	}//GEN-LAST:event_exitMenuItemActionPerformed

	public String getPersistenceKey () {
		return "mainwindow";
	}	
	
	public void makePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);
	}
	
	public boolean restorePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem addEntryMenuItem;
    private javax.swing.JMenuItem addLocaleMenuItem;
    private java.awt.BorderLayout borderLayout1;
    private javax.swing.JPopupMenu bundlePopupMenu;
    private javax.swing.JTree bundleTree;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JButton deleteEntryButton;
    private javax.swing.JMenuItem deleteEntryMenuItem;
    private javax.swing.JMenuItem deleteLocaleMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPopupMenu entryPopupMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem findMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JPopupMenu localePopupMenu;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JToolBar mainToolbar;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel modifiedLabel;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenu recentMenu;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JFileChooser saveFileChooser;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPopupMenu tablePopupMenu;
    private javax.swing.JScrollPane treeScrollPane;
    private javax.swing.JSplitPane tree_table_splitPane;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JTextArea valueTextArea;
    private javax.swing.JTable valuesTable;
    // End of variables declaration//GEN-END:variables


	private class PersistenceTreeAdapter implements PersistentComponent	{
	
		private final JComponent _tree;
		public PersistenceTreeAdapter (JComponent tree){
			this._tree = tree;
		}
		public String getPersistenceKey () {
			return "localizationtree";
		}	

		public void makePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
			PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this._tree);
		}

		public boolean restorePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
			return PersistenceUtils.restorePersistentBoundsToPreferredSize (props, this.getPersistenceKey (), this._tree);
		}
	
	}
	
	private final static String[] voidStringArray = new String[0];
	
	private class LocalizationTableModel extends AbstractTableModel implements ResourceBundleModelListener {
		
		private final DefaultResourceBundleModel _resources;
		
		private Locale[] _locales;
		private String[] _keys;
		
		
		public LocalizationTableModel (DefaultResourceBundleModel resources){
			this._resources = resources;
			reindex ();
			this.fireTableStructureChanged ();
			resources.addResourceBundleModelListener (this);
		}
		
		public int getColumnCount () {
			return this._resources.getLocales ().length+1;
		}
		
		public int getRowCount () {
			return this._resources.getKeySet ().size ();
		}
		
		public Object getValueAt (int rowIndex, int columnIndex) {
			if (columnIndex == 0){
				return this._keys[rowIndex];
			} else {
				return this._resources.getValue (this._locales[columnIndex-1],this._keys[rowIndex]);
			}
		}
		
		private void reindex (){
			this._locales = (Locale[])this._resources.getLocales ().clone ();
			this._keys = (String[])this._resources.getKeySet ().toArray (voidStringArray);
		}
		
	    public void resourceBundleChanged (ResourceBundleModelEvent e){
			reindex ();
			if (e.getLocale ()==ResourceBundleModelEvent.ALL_LOCALES){
				this.fireTableStructureChanged ();
			} else {
				this.fireTableDataChanged ();
			}
			
		}
		
		public String getColumnName (int columnIndex) {
			if (columnIndex==0){
				return "keys";
			} else {
				final Locale locale = this._locales[columnIndex-1];
				if (locale==LocalizationProperties.DEFAULT) {
					return "Default";
				} else {
					return locale.toString ();
				}
			}
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex!=0;
		}
		
		public Class getColumnClass(int col) {
			return String.class;
		}
			
		public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
			String key = this._keys[rowIndex];
			Locale l = this._locales[columnIndex-1];
			if (null==aValue){
				if (this._resources.getLocales (key).size ()==1){
					if (
					JOptionPane.showConfirmDialog (
					MainWindow.this, 
					java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("This_will_cause_key_removal._Continue?"))!=JOptionPane.OK_OPTION){
						return;
					}
				}
			}
			
			this._resources.setValue (l, key, (String)aValue);
			
		}
		
	}
	
	
		
	private class LocalizationTreeModel /*extends DefaultTreeModel */ implements TreeModel, ResourceBundleModelListener, PropertyChangeListener {
		
		private final DefaultResourceBundleModel _model;
		private String[] _keys;
		
		private final EventListenerList listenerList = new EventListenerList ();
		
		public LocalizationTreeModel (DefaultResourceBundleModel model) {
			this._model = model;
			model.addResourceBundleModelListener (this);
			cacheKeys ();
			this.fireTreeStructureChanged (this, new TreePath (new Object[]{this.getRoot ()}));
		}
		
		private void cacheKeys (){
			this._keys = (String[])this._model.getKeySet ().toArray (voidStringArray);
		}
		
	    public void resourceBundleChanged (ResourceBundleModelEvent e){
			cacheKeys ();
			
			if (e.getLocale ()==ResourceBundleModelEvent.ALL_LOCALES){
				this.fireTreeStructureChanged (this, new TreePath (new Object[]{this.getRoot ()}));
				return;
			} else if (e.getKeys ()==ResourceBundleModelEvent.ALL_KEYS){
				this.fireTreeStructureChanged (this, new TreePath (new Object[]{this.getRoot (), e.getLocale ()}));
//				this.fireTreeStructureChanged (this, new TreePath (new Object[]{e.getLocale (), this.getRoot ()}));
				return;
			}
//			this.fireTreeStructureChanged (this, new TreePath (new Object[]{this.getRoot (), e.getLocale (), e.getKeys ()[0]}));
//			this.fireTreeStructureChanged (this, new TreePath (new Object[]{e.getKeys ()[0], e.getLocale (), this.getRoot ()}));

			Locale l = e.getLocale ();
			String[] keys = e.getKeys ();
			if (e.getType ()==ResourceBundleModelEvent.INSERT){
				String[] localeKeys = (String[])this._model.getLocaleKeys (l).toArray (voidStringArray);
				Arrays.sort (localeKeys);
				
				this.fireTreeNodesInserted (e.getLocale (), new Object[]{this.getRoot (), e.getLocale ()}, getIndexes (localeKeys, keys), keys);
//			} else if (e.getType ()==ResourceBundleModelEvent.UPDATE){
//				
			} else if (e.getType ()==ResourceBundleModelEvent.DELETE){
				String[] localeKeys = (String[])this._model.getLocaleKeys (l).toArray (voidStringArray);
				String[] oldLocaleKeys = new String[localeKeys.length+keys.length];
				System.arraycopy (localeKeys, 0, oldLocaleKeys, 0, localeKeys.length);
				System.arraycopy (keys, 0, oldLocaleKeys, localeKeys.length, keys.length);
				Arrays.sort (oldLocaleKeys);
				
				this.fireTreeNodesRemoved (e.getLocale (), new Object[]{this.getRoot (), e.getLocale ()}, getIndexes (oldLocaleKeys, keys), keys);
				
			}
			
//			
//			if (e.getLocale ()==ResourceBundleModelEvent.ALL_LOCALES){
//				this.fireTreeStructureChanged (this, new TreePath (new Object[]{this.getRoot ()}));
//			} else {
//				if (e.getKeys ()==ResourceBundleModelEvent.ALL_KEYS){
//					this.fireTreeStructureChanged (this, new TreePath (new Object[]{this.getRoot (), e.getLocale ()}));
//				} else {
//					this.fireTreeStructureChanged (this, new TreePath (new Object[]{this.getRoot (), e.getLocale (), e.getKeys ()[0]}));
//				}
//			}
		}

		private int[] getIndexes (String[] orderedContainer, String[] wanted){
			final int[] retValue = new int [wanted.length];


			for (int i=0;i<wanted.length;i++){
				retValue[i] = Arrays.binarySearch (orderedContainer, wanted[i]);
			}
			return retValue;
		}
			

		public Object getChild (Object parent, int index) {
			if (parent instanceof ResourceBundleModel){
				Locale[] locales = ((ResourceBundleModel)parent).getLocales ();
				Arrays.sort (locales, _lc);
				return locales[index];
			} else if (parent instanceof Locale){
				Object[] keys = this._model.getLocaleKeys ((Locale)parent).toArray ();
				Arrays.sort (keys);
				return keys[index];
			}
			throw new Error ("Unsupported type for "+parent);
		}
		
		public int getChildCount (Object parent) {
			if (parent instanceof ResourceBundleModel){
				return ((ResourceBundleModel)parent).getLocales ().length;
			} else if (parent instanceof Locale){
				return this._model.getLocaleKeys ((Locale)parent).size ();
			}
			return 0;
		}
	
		public void addTreeModelListener (javax.swing.event.TreeModelListener l) {
			listenerList.add (TreeModelListener.class, l);
		}
		
		private final LocaleComparator _lc = new LocaleComparator ();
		public int getIndexOfChild (Object parent, Object child) {
			if (parent instanceof ResourceBundleModel){
				final ResourceBundleModel model = (ResourceBundleModel)parent;
				final Locale[] locales = model.getLocales ();
				Arrays.sort (locales, _lc);
				return Arrays.binarySearch (locales, child, _lc);
//				for (int i=0;i<locales.length;i++){
//					if (locales[i]==child){
//						return i;
//					}
//				}
//				return -1;
			} else if (parent instanceof Locale){
//				final Locale locale = (Locale)parent;
				int i=0;
				for (final Iterator it = this._model.getLocaleKeys ((Locale)parent).iterator ();it.hasNext ();){
					String key = (String)it.next ();
					if (key.equals (child)){
						return i;
					}
					i++;
				}
				return -1;
				//return this._model.getKeySet ().toArray ()[i]size ();
			}
			throw new Error ("Unsupported type for "+parent);
		}
		
		public Object getRoot () {
			return this._model;
		}
		
		public boolean isLeaf (Object node) {
			return node instanceof String;
		}
		
		public void removeTreeModelListener (javax.swing.event.TreeModelListener l) {
			listenerList.remove (TreeModelListener.class, l);
		}
		
		public void valueForPathChanged (javax.swing.tree.TreePath path, Object newValue) {
		}
		
		private void fireTreeStructureChanged(Object source, TreePath path) {
			// Guaranteed to return a non-null array
			Object[] listeners = listenerList.getListenerList();
			TreeModelEvent e = null;
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length-2; i>=0; i-=2) {
				if (listeners[i]==TreeModelListener.class) {
					// Lazily create the event:
					if (e == null)
						e = new TreeModelEvent(source, path);
					((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
				}
			}
		}
		
		
		
/**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     *
     * @param source the node being changed
     * @param path the path to the root node
     * @param childIndices the indices of the changed elements
     * @param children the changed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, 
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
            }          
        }
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     *
     * @param source the node where new elements are being inserted
     * @param path the path to the root node
     * @param childIndices the indices of the new elements
     * @param children the new elements
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, 
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
            }          
        }
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     *
     * @param source the node where elements are being removed
     * @param path the path to the root node
     * @param childIndices the indices of the removed elements
     * @param children the removed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, 
                                           childIndices, children);
                ((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
            }          
        }
    }		
		
		
		
		
		public void propertyChange(PropertyChangeEvent evt){
			if (evt.getPropertyName ().equals ("name")){
				this.fireTreeStructureChanged (this, new TreePath (new Object[]{this.getRoot ()}));
			}
		}
		
	}
		
	
	private void showAddEntryDialog (){
		this._actionNotifier.fireActionPerformed (new ActionEvent (this, -1, "showAddEntryDialog"));
	}
	
	private void showAddEntryDialog (Locale l){
		this._actionNotifier.fireActionPerformed (new ActionEvent (new NewEntryDialogRequester (l), -1, "showAddEntryDialog"));
	}
	
	public class NewEntryDialogRequester {
		private final Locale _locale;
		public NewEntryDialogRequester (Locale l) {
			this._locale = l;
		}
		public Locale getLocale (){
			return this._locale;
		}
	}
	
	private void deleteEntry (String key){
		this._context.getModel ().removeKey (key);
	}
	
	private void deleteLocale (Locale locale){
		if (JOptionPane.showConfirmDialog (
		this, 
		java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Continue_deleting_locale?"))!=JOptionPane.OK_OPTION){
			return;
		}
		this._context.getModel ().removeLocale (locale);
	}
	
	public void addActionListener (ActionListener l) {
		this._actionNotifier.addActionListener (l);
	}
	
	public ActionListener[] getActionListeners () {
		return this._actionNotifier.getActionListeners ();
	}
	
	public void removeActionListener (ActionListener l) {
		this._actionNotifier.removeActionListener (l);
	}
	
	class ValueCellEditor extends DefaultCellEditor {
		private boolean _nullCalled;
		public ValueCellEditor (){
			super ( new JTextField ());
			final JTextField field = (JTextField)this.getComponent ();
			
			/* condivide il modello con la textarea cos� una modifica da tabella viene subito riflessa nella textarea*/
			valueTextArea.setDocument (field.getDocument ());
//			field.setDocument (valueTextArea.getDocument ());
//			field.getDocument ().addDocumentListener (new DocumentListener (){
//				public void insertUpdate(DocumentEvent e){apply (e);}
//				public void removeUpdate(DocumentEvent e){apply (e);}
//				public void changedUpdate(DocumentEvent e){apply (e);}
//
//				public void apply (DocumentEvent e){
//					if (field.isEnabled () && field.hasFocus ()) {
//						/* propaga modifica solo se editor abilitato, altrimenti sarebbe un evento spurio */
//						valueTextArea.setText (field.getText ());
//					}
//				}
//			});
			
			field.addKeyListener (new KeyAdapter (){
				 public void keyPressed(KeyEvent e) {
					 if (e.isControlDown ()){
						 /* SHORTCUTS */
						 if (e.getKeyCode ()==KeyEvent.VK_DELETE){
							 /* imposta a null */
							 field.setText (null);
							 _nullCalled = true;
							 ValueCellEditor.this.stopCellEditing ();
						 } else if (e.getKeyCode ()==KeyEvent.VK_SPACE){
							 /* imposta a stringa vuota */
							 field.setText ("");
//							 _nullCalled = true;
							 ValueCellEditor.this.stopCellEditing ();
						 }
					 }
				 }
			});
		}
		public boolean stopCellEditing() {
//			_nullCalled = false;
			return super.stopCellEditing ();
		}
		public Object getCellEditorValue() {
			if (this._nullCalled){
				/*consuma il valore!!!*/
				_nullCalled = false;
				return null;
			} else {
				return delegate.getCellEditorValue();
			}
		}
		
	}
	
	private void openFile (File file){
		final StringTokenizer st = new StringTokenizer (file.getName (), "_", false);
		if (st.countTokens ()>1){
			//non si tratta del file Default
			String baseName;
			do {
				baseName = this._wm.specifyBundleName (file);
				if (baseName!=null){
					File baseFile = new File (file.getParentFile (), baseName+".properties");
						this._context.getModel ().load (baseFile);
						return;
				}
			} while (baseName==null);
		}
		this._context.getModel ().load (file);
		this._context.getUserSettings ().setLastPath (file.getPath ());
		final String[] recentPaths = this._context.getUserSettings ().getRecentPaths ();
		String newRecentPath = file.getPath ();;
		boolean saveNewPath = true;
		if (recentPaths!=null && recentPaths.length>0){
			if (newRecentPath.equals (recentPaths[0])){
				/*
				 * il file aperto e' gia' l'ultimo aperto
				 */
				saveNewPath = false;
			}
		}

		if (saveNewPath){
			final String[] newRecentPaths = new String[Math.min (recentPaths.length+1, 10)];
			newRecentPaths[0] = newRecentPath;
			System.arraycopy (recentPaths, 0, newRecentPaths, 1, newRecentPaths.length-1);
			this._context.getUserSettings ().setRecentPaths (newRecentPaths);
		}
		
		updateRecentPathMenu ();
	}
	
	
	private boolean checkForDataLoss (){
		if (this._context.getModel ().isModified ()){
			if (
			JOptionPane.showConfirmDialog (
			this, 
			java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Continue_discarding_all_changes?"))!=JOptionPane.OK_OPTION){
				return false;
			}
		}
		return true;
	}
	
	private void updateRecentPathMenu (){
		final String[] recentPaths = this._context.getUserSettings ().getRecentPaths ();
		recentMenu.removeAll ();
		for (int i=0;i<recentPaths.length;i++){
			final int ix = i;
			final JMenuItem item = new JMenuItem ();
			item.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					if (!checkForDataLoss ()){
						return;
					}
					
					openFile (new File (recentPaths[ix]));
				}
			});
			item.setText (recentPaths[ix]);
			recentMenu.add (item);
		}
		if (recentMenu.getItemCount ()==0){
			JMenuItem disabled = new JMenuItem ();
			disabled.setEnabled (false);
			disabled.setText ("No files");
			recentMenu.add (disabled);
		}
	}
	
	public Matcher getMatcher (){
		return this._matcher;
	}
	
	private class PatternFinder implements Matcher {

		private String p;
		private boolean h = true;
		private PropertyChangeSupport changeSupport;

		public void setPattern (String pattern){
			if (this.p==pattern || (this.p!=null && this.p.equals (pattern))){
				return;
			}
			String old = this.p;
			this.p = pattern;
			changeSupport.firePropertyChange ("pattern", old, pattern);
			forceTableRepaint ();
		}

		public void setHighlight (boolean h){			
			if (this.h == h){
				return;
			}
			this.h = h;
			forceTableRepaint ();
		}

		private void forceTableRepaint (){
			valuesTable.tableChanged (new TableModelEvent(
				valuesTable.getModel(),
				0,
				valuesTable.getModel().getRowCount ()-1,
				TableModelEvent.ALL_COLUMNS,
				TableModelEvent.UPDATE)
			);
		}

		public boolean match (String s){
			if (null==p){
				return false;
			}
			return -1 != s.indexOf (p);
		}

		public String getPattern (){
			return this.p;
		}

		public boolean getHighlight (){
			return this.h;
		}
		
		
		

	public synchronized void addPropertyChangeListener (
	PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new java.beans.PropertyChangeSupport (this);
		}
		changeSupport.addPropertyChangeListener (listener);
	}
	
	public synchronized void removePropertyChangeListener (
	PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener (listener);
	}		
				
	public void nextMatch () {
	}
	
	public void previousMatch () {
	}
	
	}
}

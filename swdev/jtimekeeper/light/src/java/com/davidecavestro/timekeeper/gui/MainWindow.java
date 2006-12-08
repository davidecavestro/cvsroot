/*
 * MainWIndow.java
 *
 * Created on 26 novembre 2005, 14.56
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.gui.SwingWorker;
import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.common.util.IllegalOperationException;
import com.davidecavestro.common.util.StringUtils;
import com.davidecavestro.common.util.action.ActionNotifier;
import com.davidecavestro.common.util.action.ActionNotifierImpl;
import com.davidecavestro.common.util.file.CustomFileFilter;
import com.davidecavestro.common.util.file.FileUtils;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.gui.dnd.DataFlavors;
import com.davidecavestro.timekeeper.gui.dnd.ProgressItemSelection;
import com.davidecavestro.timekeeper.gui.dnd.ProgressItemTransferHandler;
import com.davidecavestro.timekeeper.gui.dnd.ProgressTransferHandler;
import com.davidecavestro.timekeeper.gui.dnd.TransferAction;
import com.davidecavestro.timekeeper.gui.dnd.TransferActionListener;
import com.davidecavestro.timekeeper.help.HelpResources;
import com.davidecavestro.timekeeper.model.TaskTreeModelImpl;
import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.model.TaskTreePath;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelEvent;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelListener;
import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelEvent;
import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelListener;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.util.Duration;
import com.ost.timekeeper.util.LocalizedPeriod;
import com.ost.timekeeper.util.LocalizedPeriodImpl;
import java.awt.Component;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import javax.help.CSH;
import javax.swing.*;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.Sorter;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

/**
 * La finestra principale dell'applicazione.
 *
 * @author  davide
 */
public class MainWindow extends javax.swing.JFrame implements PersistentComponent, ActionNotifier {
	
	private final ActionNotifierImpl _actionNotifier;
	
	private final ApplicationContext _context;
	private final WindowManager _wm;
	
	private final AdvancingTicPump _ticPump;
	
	private MouseEvent _taskTreeFirstMouseEvent;
	
	private final TransferActionListener tal = new TransferActionListener ();

	/**
	 * Costruttore.
	 */
	public MainWindow (final ApplicationContext context){
		this._actionNotifier = new ActionNotifierImpl ();
		this._context = context;
		this._wm = context.getWindowManager ();
		
		/*
		 * va istanziato prima dell'inizializzazione dei componenti, che vi si registrano
		 */
		_ticPump = new AdvancingTicPump (_context.getModel (), 1000);
		_context.getModel ().addWorkAdvanceModelListener (_ticPump);
		
		createActionTable (new JTextArea ());
		
		initComponents ();
//		this._context.getUIPersisteer ().register (new MainWindow.PersistencePanelAdapter (this.taskTreePanel, "taskTreePanel"));
		
		/*
		 * registra l'albero e la tabella per la gestioen del cut&paste dal menu e toolbar
		 */
		tal.register (taskTree);
		tal.register (progressesTable);
		
		
//		/*
//		 * notifica la textarea del cambio di selezione, aggiornandola
//		 */
//		final ListSelectionListener l = new ListSelectionListener (){
//			final Document d = new PlainDocument ();
//			public void valueChanged (ListSelectionEvent e) {
//			}
//		};
//		progressesTable.getSelectionModel ().addListSelectionListener (l);
//		progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (l);
		progressesTable.setDefaultEditor (Date.class, new DateCellEditor ());
		progressesTable.setDefaultEditor (PieceOfWork.class, new DurationCellEditor ());
		progressesTable.setDefaultEditor (Duration.class, new DurationCellEditor ());
		progressesTable.setDefaultEditor (String.class, new DefaultCellEditor (new JTextField ()) {
			
			/**
			 * Editazione parte, in caso di evento del mouse, solo dopo doppio click
			 */
			@Override
			public boolean isCellEditable (EventObject eo) {
				if (eo instanceof MouseEvent) {
					return ((MouseEvent)eo).getClickCount ()>1;
				} else {
					return super.isCellEditable (eo);
				}
			}
		});

		
		
		
		
		
		
		
		
		this._context.getModel ().addPropertyChangeListener (new PropertyChangeListener (){
			public void propertyChange (PropertyChangeEvent e){
				String pName = e.getPropertyName ();
				if (pName.equals ("name")){
					setTitle (prepareTitle (_context.getModel ()));
				}
			}
		}
		);
		
		
		
		_context.addPropertyChangeListener ("isProcessing", new PropertyChangeListener (){
			public void propertyChange (PropertyChangeEvent evt){
				boolean isProcessing = ((Boolean)evt.getNewValue ()).booleanValue ();
				
				progressBar.setIndeterminate (isProcessing);
				progressBar.setValue (isProcessing ?99:0);
//				progressBar.setVisible (isProcessing);
			}
		});
		
		//ascolta se stesso per la status bar (usa variabili interne)
		progressesTable.getModel ().addTableModelListener ((ProgressesJTableModel)progressesTable.getModel ());

		//ascolta l'albero per variare il contenuto della tabella'
		taskTree.addTreeSelectionListener ((ProgressesJTableModel)progressesTable.getModel ());
		
		setTitle (prepareTitle (this._context.getModel ()));
		
		setLocationRelativeTo (null);
		
		/*
		 * Rollover su elementi toolbar
		 */
		final Component[] tc = mainToolbar.getComponents ();
		final Component[] tc1 = mainToolbar1.getComponents ();
		final Component[] tc2 = mainToolbar2.getComponents ();
		
		final Component[] alltc = new Component[tc.length+tc1.length+tc2.length];
		System.arraycopy (tc, 0, alltc, 0, tc.length);
		System.arraycopy (tc1, 0, alltc, tc.length, tc1.length);
		System.arraycopy (tc2, 0, alltc, tc.length+tc1.length, tc2.length);
		
		for (int i=0;i<alltc.length;i++){
			final Component c = alltc[i];
			if (c instanceof JButton){
				final JButton jb = (JButton)c;
				jb.addMouseListener (new MouseAdapter (){
					/**
					 * Invoked when the mouse enters a component.
					 */
					public void mouseEntered (MouseEvent e) {
						jb.setBorderPainted (true);
					}
					
					/**
					 * Invoked when the mouse exits a component.
					 */
					public void mouseExited (MouseEvent e) {
						jb.setBorderPainted (false);
					}
				});
			}
		}

//		//disable internal dnd functionality, because we need our own implementation
//		taskTree.setDragEnabled (false);
		
		//attach transfer handler
		taskTree.setTransferHandler (new TaskTreeTransferHandler ());
		
		progressesTable.setTransferHandler (new ProgressTableTransferHandler ());
		
//		//since we have a transfer handler, we just need to attach mouse listeners
//		//to initiate the drag inside of the transfer handler
//		final MouseAdapter dndMouseAdapter = new MouseAdapter () {
//			public void mousePressed (MouseEvent e) {
//				_taskTreeFirstMouseEvent = e;
//				e.consume ();
//			}
//			
//			public void mouseReleased (MouseEvent e) {
//				_taskTreeFirstMouseEvent = null;
//			}
//		};
//		
//		taskTree.addMouseListener (dndMouseAdapter);
//		taskTree.addMouseMotionListener (new java.awt.event.MouseMotionAdapter () {
//			
//			//define diplacement of five pixel for as drag
//			private static final int PIXEL_DISPLACEMENT = 5;
//			
//			public void mouseDragged (MouseEvent e) {
//				if (_taskTreeFirstMouseEvent != null) {
//					e.consume ();
//					
//					//if the user holds down the control key -> COPY, otherwise MOVE
//					int ctrlMask = java.awt.event.InputEvent.CTRL_DOWN_MASK;
//					int action = ((e.getModifiersEx () & ctrlMask) == ctrlMask) ? TransferHandler.COPY : TransferHandler.MOVE;
//					
//					int dx = Math.abs (e.getX () - _taskTreeFirstMouseEvent.getX ());
//					int dy = Math.abs (e.getY () - _taskTreeFirstMouseEvent.getY ());
//					
//					//define a displacement of at least some pixel as a drag
//					if (dx > PIXEL_DISPLACEMENT || dy > PIXEL_DISPLACEMENT) {
//						//starting to drag...
//						JComponent c = (JComponent) e.getSource ();
//						TransferHandler handler = c.getTransferHandler ();
//						
//						//tell transfer handler to start drag
//						handler.exportAsDrag (c, _taskTreeFirstMouseEvent, action);
//						
//						int x = e.getX ();
//						int y = e.getY ();
//						
//						//reset first mouse event for the next time
//						_taskTreeFirstMouseEvent = null;
//					}
//				}
//			}
//		});
//		
//		//revert MouseListeners order so that our MouseListener is called first
//		//this is important to give drag n drop first priority and prevent the
//		//internal mouse handler of tableUI changing the selection.
//		java.awt.event.MouseListener[] mls = this.getMouseListeners ();
//		for (int i = 0; i < mls.length; i++) {
//			if (mls[i] != dndMouseAdapter) {
//				this.removeMouseListener (mls[i]);
//				this.addMouseListener (mls[i]);
//			}
//		}		
		
		_context.getModel ().addWorkAdvanceModelListener (new WorkAdvanceModelListener () {
			public void elementsInserted (WorkAdvanceModelEvent e) {
				adjustStatusLabel ();
			}
			public void elementsRemoved (WorkAdvanceModelEvent e) {
				adjustStatusLabel ();
			}
			
			private void adjustStatusLabel () {
				if (_context.getModel ().getAdvancing ().isEmpty ()) {
					appStatusLabel.setText ("IDLE");
				} else {
					appStatusLabel.setText ("RUNNING");
				}				
			}
		});
			
		
		/*
		 * toggle sort
		 * 1 asc, 2 desc, 3 null
		 */
		progressesTable.setFilters (new FilterPipeline () {
			protected SortController createDefaultSortController() {
				return new SorterBasedSortController () {
 
					 public void toggleSortOrder(int column, Comparator comparator) {
						 Sorter currentSorter = getSorter();
						 if ((currentSorter != null) 
								 && (currentSorter.getColumnIndex() == column)
								 && !currentSorter.isAscending()) {
							 setSorter(null);
						 } else {
							 super.toggleSortOrder(column, comparator);
						 }
					 }

				 };
			}			
		});
		
		taskTree.getTreeTableModel ().addTreeModelListener (new TreeModelListener () {
			public void treeNodesChanged (TreeModelEvent e) {
			}
			public void treeNodesInserted (TreeModelEvent e) {
				taskTree.packAll ();
			}
			public void treeNodesRemoved (TreeModelEvent e) {
				taskTree.packAll ();
			}
			public void treeStructureChanged (TreeModelEvent e) {
				taskTree.packAll ();
			}
		});
		
		progressesTable.getModel ().addTableModelListener (new TableModelListener () {
			public void tableChanged (TableModelEvent e) {
				if (e.getColumn ()==TableModelEvent.ALL_COLUMNS && e.getLastRow ()==Integer.MAX_VALUE) {
					/* denota un ricaricamento totale */
					progressesTable.packAll ();
				}
			}
		});
	}
	
	
	private String prepareTitle (TaskTreeModelImpl model){
		final StringBuffer sb = new StringBuffer ();
		sb.append (_context.getApplicationData ().getApplicationExternalName ()).append (" - Workspace ").append (model.getRoot ().getName ());
		return sb.toString ();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        borderLayout1 = new java.awt.BorderLayout();
        taskPopupMenu = new javax.swing.JPopupMenu();
        newTaskPopupItem = new javax.swing.JMenuItem();
        taskPopupMenu1 = new javax.swing.JPopupMenu();
        deleteTaskPopupItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        newProgressPopupItem = new javax.swing.JMenuItem();
        progressPopupMenu = new javax.swing.JPopupMenu();
        deleteProgressPopupItem = new javax.swing.JMenuItem();
        tablePopupMenu = new javax.swing.JPopupMenu();
        openFileChooser = new javax.swing.JFileChooser();
        saveFileChooser = new javax.swing.JFileChooser();
        mainPanel = new javax.swing.JPanel();
        tree_table_splitPane = new javax.swing.JSplitPane();
        taskTreePanel = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        taskTree = new org.jdesktop.swingx.JXTreeTable();
        chartsPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        progressesTablePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        progressesTable = new org.jdesktop.swingx.JXTable();
        statusPanel = new javax.swing.JPanel();
        appStatusLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tableTaskNameLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tableProgressCountLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        mainToolbar = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        mainToolbar1 = new javax.swing.JToolBar();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        mainToolbar2 = new javax.swing.JToolBar();
        helpButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newWorkSpaceMenuItem = new javax.swing.JMenuItem();
        newTaskMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenu1 = new javax.swing.JMenu();
        jSeparator3 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        undoMenuItem = new javax.swing.JMenuItem();
        redoMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        getActionByName(DefaultEditorKit.cutAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));

        cutMenuItem = new javax.swing.JMenuItem();
        getActionByName(DefaultEditorKit.copyAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));

        copyMenuItem = new javax.swing.JMenuItem();
        getActionByName(DefaultEditorKit.pasteAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));

        pasteMenuItem = new javax.swing.JMenuItem();
        getActionByName(DefaultEditorKit.deleteNextCharAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        logConsoleMenuItem = new javax.swing.JMenuItem();
        optionsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        contextHelpMenuItem = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();

        taskPopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        newTaskPopupItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Add_Locale"));
        newTaskPopupItem.setActionCommand("newTask");
        newTaskPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTaskPopupItemActionPerformed(evt);
            }
        });

        taskPopupMenu.add(newTaskPopupItem);

        taskPopupMenu1.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteTaskPopupItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Delete_Locale"));
        deleteTaskPopupItem.setActionCommand("deleteTask");
        deleteTaskPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteTaskPopupItemActionPerformed(evt);
            }
        });

        taskPopupMenu1.add(deleteTaskPopupItem);

        taskPopupMenu1.add(jSeparator1);

        org.openide.awt.Mnemonics.setLocalizedText(newProgressPopupItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Add_Entry"));
        newProgressPopupItem.setActionCommand("newProgress");
        newProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProgressPopupItemActionPerformed(evt);
            }
        });

        taskPopupMenu1.add(newProgressPopupItem);

        progressPopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(deleteProgressPopupItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Delete_Entry"));
        deleteProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProgressPopupItemActionPerformed(evt);
            }
        });

        progressPopupMenu.add(deleteProgressPopupItem);

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon (MainWindow.class.getResource ("/com/davidecavestro/timekeeper/gui/images/locale-20x20.png")).getImage ());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        mainPanel.setLayout(new java.awt.BorderLayout());

        tree_table_splitPane.setMaximumSize(null);
        tree_table_splitPane.setOneTouchExpandable(true);
        taskTreePanel.setLayout(new java.awt.BorderLayout());

        taskTreePanel.setAutoscrolls(true);
        taskTreePanel.setPreferredSize(new java.awt.Dimension(120, 354));
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(1.0);
        jSplitPane2.setMaximumSize(null);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(50, 50));
        jSplitPane2.setOneTouchExpandable(true);
        jScrollPane4.setMaximumSize(null);
        jScrollPane4.setMinimumSize(null);
        taskTree.setColumnControlVisible(true);
        taskTree.setDragEnabled(true);
        taskTree.setFocusCycleRoot(true);
        taskTree.setFont(new java.awt.Font("Monospaced", 0, 12));
        taskTree.setHorizontalScrollEnabled(true);
        taskTree.setMaximumSize(null);
        taskTree.setMinimumSize(null);
        taskTree.setRootVisible(true);
        taskTree.setShowVerticalLines(true);
        taskTree.setTreeTableModel(new TaskJTreeModel (_context.getModel ()));
        javax.help.CSH.setHelpIDString (taskTree, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.TASK_TREE ));
        taskTree.setRowSelectionInterval (0, 0);
        jScrollPane4.setViewportView(taskTree);

        jSplitPane2.setTopComponent(jScrollPane4);

        chartsPanel.setLayout(new java.awt.BorderLayout());

        _context.getUIPersisteer ().register (new MainWindow.PersistencePanelAdapter (chartsPanel, "chartsPanel"));

        jSplitPane2.setBottomComponent(chartsPanel);

        taskTreePanel.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        tree_table_splitPane.setLeftComponent(taskTreePanel);

        jPanel3.setLayout(new java.awt.BorderLayout());

        progressesTablePanel.setLayout(new java.awt.BorderLayout());

        this._context.getUIPersisteer ().register (new MainWindow.PersistencePanelAdapter (progressesTablePanel, "progressesTablePanel"));

        jScrollPane3.setBackground(javax.swing.UIManager.getDefaults().getColor("Table.background"));
        jScrollPane3.setMaximumSize(null);
        jScrollPane3.getViewport ().setBackground (javax.swing.UIManager.getDefaults().getColor("Table.background"));
        progressesTable.setModel(new ProgressesJTableModel (_context.getModel ()));
        progressesTable.setAutoStartEditOnKeyStroke(false);
        progressesTable.setColumnControlVisible(true);
        progressesTable.setDragEnabled(true);
        progressesTable.setFont(new java.awt.Font("Monospaced", 0, 12));
        progressesTable.setHorizontalScrollEnabled(true);
        progressesTable.setPreferredSize(null);

        //progressesTable.setDefaultEditor (String.class, new ValueCellEditor ());

        /*
        * editor (shortcut per cancellazione
            */
            progressesTable.addKeyListener (new KeyAdapter (){
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode ()==KeyEvent.VK_DELETE){
                    }
                }
            });

            javax.help.CSH.setHelpIDString (progressesTable, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.PROGRESSES_TABLE ));

            progressesTable.setDefaultRenderer (java.util.Date.class, new DefaultTableCellRenderer (){
                public void setValue (Object value) {
                    setText (
                        com.davidecavestro.common.util.CalendarUtils.getTimestamp (
                            (Date)value,
                            java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("from_to_format_long")
                        )
                    );
                }
            });

            final ImageIcon runningIcon = new ImageIcon (getClass().getResource ("/com/davidecavestro/timekeeper/gui/images/gears.gif"));
            final ImageIcon staticIcon = new ImageIcon ();

            progressesTable.setDefaultRenderer (Duration.class,
                new DefaultTableCellRenderer () {
                    private Font _originalFont;
                    private Font _boldFont;
                    public Component getTableCellRendererComponent (final JTable table, final Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {

                        final JLabel res = (JLabel)super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column);
                        final PieceOfWork progress = (PieceOfWork)value;

                        final boolean progressing = progress.getTo ()==null;
                        //final ImageIcon icon = progressing?runningIcon:staticIcon;
                        //setIcon (icon);

                        if (null==_originalFont) {
                            _originalFont = res.getFont ();
                        }
                        if (null==_boldFont) {
                            _boldFont = _originalFont.deriveFont (Font.BOLD);
                        }

                        if (progressing){
                            res.setFont (_boldFont);
                            /*
                            icon.setImageObserver (new ImageObserver (){
                                public boolean imageUpdate (Image img, int flags, int x, int y, int w, int h) {
                                    if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
                                        Rectangle rect = table.getCellRect (row, column, false);
                                        table.repaint (rect);
                                    }
                                    //			table.repaint ();
                                    return (flags & (ALLBITS | ABORT)) == 0;
                                }
                            });
                            **/
                        } else {
                            res.setFont (_originalFont);
                        }

                        return res;
                    }
                    public void setValue (Object value) {
                        final PieceOfWork progress = (PieceOfWork)value;
                        setText (DurationUtils.formatDuration (progress.getTime ()));
                    }
                }
            );
            jScrollPane3.setViewportView(progressesTable);

            progressesTablePanel.add(jScrollPane3, java.awt.BorderLayout.CENTER);

            statusPanel.setLayout(new java.awt.GridBagLayout());

            statusPanel.setMinimumSize(new java.awt.Dimension(320, 20));
            statusPanel.setPreferredSize(new java.awt.Dimension(436, 20));
            javax.help.CSH.setHelpIDString (statusPanel, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.STATUS_BAR));
            appStatusLabel.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(appStatusLabel, "IDLE");
            appStatusLabel.setToolTipText("Running status");
            appStatusLabel.setMinimumSize(new java.awt.Dimension(90, 19));
            appStatusLabel.setPreferredSize(new java.awt.Dimension(70, 19));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            statusPanel.add(appStatusLabel, gridBagConstraints);

            progressBar.setBorder(null);
            progressBar.setMinimumSize(new java.awt.Dimension(10, 6));
            progressBar.setString("");
            progressBar.setStringPainted(true);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.weightx = 1.0;
            statusPanel.add(progressBar, gridBagConstraints);

            jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            jPanel2.setPreferredSize(new java.awt.Dimension(250, 22));
            jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel1.setText("Task:");
            jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, -1, -1));

            tableTaskNameLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
            jPanel2.add(tableTaskNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 5, 90, 20));

            jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel2.setText("Pr. #:");
            jLabel2.setToolTipText("Progress count");
            jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 5, -1, -1));

            tableProgressCountLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
            jPanel2.add(tableProgressCountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 5, 20, 20));

            statusPanel.add(jPanel2, new java.awt.GridBagConstraints());

            progressesTablePanel.add(statusPanel, java.awt.BorderLayout.SOUTH);

            jPanel3.add(progressesTablePanel, java.awt.BorderLayout.CENTER);

            tree_table_splitPane.setRightComponent(jPanel3);

            mainPanel.add(tree_table_splitPane, java.awt.BorderLayout.CENTER);

            jPanel5.setLayout(new java.awt.GridBagLayout());

            jPanel5.setPreferredSize(new java.awt.Dimension(420, 40));
            mainToolbar.setRollover(true);
            javax.help.CSH.setHelpIDString (mainToolbar, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            jButton1.setAction(new NewTaskAction ());
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/filenew.png")));
            jButton1.setBorderPainted(false);
            jButton1.setMargin(null);
            jButton1.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton1.setOpaque(false);
            jButton1.setPreferredSize(new java.awt.Dimension(30, 30));
            mainToolbar.add(jButton1);

            jButton2.setAction(new DeleteTaskAction ());
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/editdelete.png")));
            jButton2.setBorderPainted(false);
            jButton2.setMargin(null);
            jButton2.setMaximumSize(new java.awt.Dimension(28, 28));
            jButton2.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton2.setOpaque(false);
            jButton2.setPreferredSize(new java.awt.Dimension(30, 30));
            mainToolbar.add(jButton2);

            mainToolbar.add(jSeparator5);

            jButton4.setAction(new NewPieceOfWorkAction ());
            jButton4.setBorderPainted(false);
            jButton4.setMargin(null);
            jButton4.setMaximumSize(new java.awt.Dimension(28, 28));
            jButton4.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton4.setOpaque(false);
            jButton4.setPreferredSize(new java.awt.Dimension(30, 30));
            mainToolbar.add(jButton4);

            jButton3.setAction(new StartProgressAction ());
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/start.png")));
            jButton3.setBorderPainted(false);
            jButton3.setMargin(null);
            jButton3.setMaximumSize(new java.awt.Dimension(28, 28));
            jButton3.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton3.setOpaque(false);
            jButton3.setPreferredSize(new java.awt.Dimension(30, 30));
            mainToolbar.add(jButton3);

            jButton10.setAction(new StopProgressAction ());
            jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/stop.png")));
            jButton10.setBorderPainted(false);
            jButton10.setMargin(null);
            jButton10.setMaximumSize(new java.awt.Dimension(28, 28));
            jButton10.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton10.setOpaque(false);
            jButton10.setPreferredSize(new java.awt.Dimension(30, 30));
            mainToolbar.add(jButton10);

            jPanel5.add(mainToolbar, new java.awt.GridBagConstraints());

            mainToolbar1.setRollover(true);
            javax.help.CSH.setHelpIDString (mainToolbar1, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            jButton6.setAction(new TransferAction (TransferAction.Type.CUT, tal));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/editcut.png")));
            jButton6.setToolTipText("Cut");
            jButton6.setBorderPainted(false);
            jButton6.setMargin(null);
            jButton6.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton6.setOpaque(false);
            jButton6.setPreferredSize(new java.awt.Dimension(30, 30));
            jButton6.setText (null);
            mainToolbar1.add(jButton6);

            jButton7.setAction(new TransferAction (TransferAction.Type.COPY, tal));
            jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/editcopy.png")));
            jButton7.setToolTipText("Copy");
            jButton7.setBorderPainted(false);
            jButton7.setMargin(null);
            jButton7.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton7.setOpaque(false);
            jButton7.setPreferredSize(new java.awt.Dimension(30, 30));
            jButton7.setText (null);
            mainToolbar1.add(jButton7);

            jButton8.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
            jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/editpaste.png")));
            jButton8.setToolTipText("Paste");
            jButton8.setBorderPainted(false);
            jButton8.setMargin(null);
            jButton8.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton8.setOpaque(false);
            jButton8.setPreferredSize(new java.awt.Dimension(30, 30));
            jButton8.setText (null);
            mainToolbar1.add(jButton8);

            undoButton.setAction(_context.getUndoManager ().getUndoAction());
            undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/undo.png")));
            undoButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Undo+SHORTCUT"));
            undoButton.setBorderPainted(false);
            undoButton.setMargin(null);
            undoButton.setMaximumSize(new java.awt.Dimension(28, 28));
            undoButton.setMinimumSize(new java.awt.Dimension(22, 22));
            undoButton.setOpaque(false);
            undoButton.setPreferredSize(new java.awt.Dimension(30, 30));
            /* mantiene nascosto il testo  dell'action */
            undoButton.setText (null);
            undoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar1.add(undoButton);

            redoButton.setAction(_context.getUndoManager ().getRedoAction());
            redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/redo.png")));
            redoButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Redo+SHORTCUT"));
            redoButton.setBorderPainted(false);
            redoButton.setMargin(null);
            redoButton.setMaximumSize(new java.awt.Dimension(28, 28));
            redoButton.setMinimumSize(new java.awt.Dimension(22, 22));
            redoButton.setOpaque(false);
            redoButton.setPreferredSize(new java.awt.Dimension(30, 30));
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar1.add(redoButton);

            jPanel5.add(mainToolbar1, new java.awt.GridBagConstraints());

            mainToolbar2.setRollover(true);
            javax.help.CSH.setHelpIDString (mainToolbar2, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            _context.getHelpManager ().initialize (helpButton);
            helpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/help.png")));
            helpButton.setBorderPainted(false);
            helpButton.setOpaque(false);
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar2.add(helpButton);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            jPanel5.add(mainToolbar2, gridBagConstraints);

            mainPanel.add(jPanel5, java.awt.BorderLayout.NORTH);

            getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

            menuBar.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(fileMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&File"));
            fileMenu.setFont(new java.awt.Font("Dialog", 0, 12));
            newWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            newWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    newWorkSpaceMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(newWorkSpaceMenuItem);

            newTaskMenuItem.setAction(new NewTaskAction ());
            newTaskMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            newTaskMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    newTaskMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(newTaskMenuItem);

            fileMenu.add(jSeparator2);

            jMenu1.setText("Menu");
            fileMenu.add(jMenu1);

            fileMenu.add(jSeparator3);

            exitMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            exitMenuItem.setIcon(new javax.swing.ImageIcon("/usr/local/share/devel/swdev/resourcebundleeditor/src/java/com/davidecavestro/timekeeper/gui/images/transparent.png"));
            org.openide.awt.Mnemonics.setLocalizedText(exitMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("E&xit"));
            exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    exitMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(exitMenuItem);

            menuBar.add(fileMenu);

            org.openide.awt.Mnemonics.setLocalizedText(editMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Edit"));
            editMenu.setFont(new java.awt.Font("Dialog", 0, 12));
            undoMenuItem.setAction(_context.getUndoManager ().getUndoAction());
            undoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            undoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/undo.png")));
            undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    undoMenuItemActionPerformed(evt);
                }
            });

            editMenu.add(undoMenuItem);

            redoMenuItem.setAction(_context.getUndoManager ().getRedoAction());
            redoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            redoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/redo.png")));
            editMenu.add(redoMenuItem);

            editMenu.add(jSeparator4);

            cutMenuItem.setAction(new TransferAction (TransferAction.Type.CUT, tal));
            cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
            cutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            cutMenuItem.setMnemonic('X');
            org.openide.awt.Mnemonics.setLocalizedText(cutMenuItem, "Cut");
            editMenu.add(cutMenuItem);

            copyMenuItem.setAction(new TransferAction (TransferAction.Type.COPY, tal));
            copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
            copyMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            copyMenuItem.setMnemonic('C');
            org.openide.awt.Mnemonics.setLocalizedText(copyMenuItem, "Copy");
            copyMenuItem.setToolTipText("");
            editMenu.add(copyMenuItem);

            pasteMenuItem.setAction(new TransferAction (TransferAction.Type.CUT, tal));
            pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
            pasteMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            pasteMenuItem.setMnemonic('V');
            org.openide.awt.Mnemonics.setLocalizedText(pasteMenuItem, "Paste");
            editMenu.add(pasteMenuItem);

            deleteMenuItem.setAction(getActionByName(DefaultEditorKit.deleteNextCharAction));
            deleteMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            deleteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/editdelete.png")));
            org.openide.awt.Mnemonics.setLocalizedText(deleteMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Delete"));
            deleteMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Delete"));
            editMenu.add(deleteMenuItem);

            menuBar.add(editMenu);

            org.openide.awt.Mnemonics.setLocalizedText(toolsMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Tools"));
            toolsMenu.setFont(new java.awt.Font("Dialog", 0, 12));
            logConsoleMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(logConsoleMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Log_console"));
            logConsoleMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    logConsoleMenuItemActionPerformed(evt);
                }
            });

            toolsMenu.add(logConsoleMenuItem);

            optionsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(optionsMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Options"));
            optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    optionsMenuItemActionPerformed(evt);
                }
            });

            toolsMenu.add(optionsMenuItem);

            menuBar.add(toolsMenu);

            org.openide.awt.Mnemonics.setLocalizedText(helpMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Help"));
            helpMenu.setFont(new java.awt.Font("Dialog", 0, 12));
            _context.getHelpManager ().initialize (contentsMenuItem);
            contentsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            contentsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/contents.png")));
            contentsMenuItem.setText("Contents");
            contentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    contentsMenuItemActionPerformed(evt);
                }
            });

            helpMenu.add(contentsMenuItem);

            contextHelpMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            contextHelpMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/contexthelp.png")));
            contextHelpMenuItem.setText("Item");
            contextHelpMenuItem.addActionListener (new CSH.DisplayHelpAfterTracking (_context.getHelpManager ().getMainHelpBroker ()));
            helpMenu.add(contextHelpMenuItem);

            helpMenu.add(jSeparator7);

            aboutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/info.png")));
            aboutMenuItem.setText("About");
            aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    aboutMenuItemActionPerformed(evt);
                }
            });

            helpMenu.add(aboutMenuItem);

            menuBar.add(helpMenu);

            setJMenuBar(menuBar);

            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            setBounds((screenSize.width-802)/2, (screenSize.height-600)/2, 802, 600);
        }// </editor-fold>//GEN-END:initComponents

	private void newTaskMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTaskMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_newTaskMenuItemActionPerformed
	
	private void optionsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsMenuItemActionPerformed
		_context.getWindowManager ().getOptionsDialog ().show ();
	}//GEN-LAST:event_optionsMenuItemActionPerformed
	
    private void logConsoleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logConsoleMenuItemActionPerformed
		_context.getWindowManager ().getLogConsole ().show ();
    }//GEN-LAST:event_logConsoleMenuItemActionPerformed
	
	private void contentsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contentsMenuItemActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_contentsMenuItemActionPerformed
	
	private void deleteProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProgressPopupItemActionPerformed
		
		
	}//GEN-LAST:event_deleteProgressPopupItemActionPerformed
	
	private void undoMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoMenuItemActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_undoMenuItemActionPerformed
	
	private void formWindowDeactivated (java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
			}//GEN-LAST:event_formWindowDeactivated
	
	private void aboutMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
		_wm.getAbout ().show ();
	}//GEN-LAST:event_aboutMenuItemActionPerformed
	
	private void deleteTaskPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteTaskPopupItemActionPerformed
		final List<Task> l = new ArrayList<Task> ();
		l.add ((Task)treePopupNode);
		deleteTasks (l);
	}//GEN-LAST:event_deleteTaskPopupItemActionPerformed
		
	private void saveFileChooserActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileChooserActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_saveFileChooserActionPerformed
		
	private void newTaskPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTaskPopupItemActionPerformed
		this._actionNotifier.fireActionPerformed (new ActionEvent (this, -1, "showNewTaskDialog"));
	}//GEN-LAST:event_newTaskPopupItemActionPerformed
	
	Object treePopupNode;
	TreePath treePopupPath;
	private void newWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWorkSpaceMenuItemActionPerformed
		
	}//GEN-LAST:event_newWorkSpaceMenuItemActionPerformed
	
	private void newProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProgressPopupItemActionPerformed
		if (treePopupNode instanceof Task){
			showNewProgressDialog ((Task)treePopupNode);
		}
	}//GEN-LAST:event_newProgressPopupItemActionPerformed
	
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
    private javax.swing.JLabel appStatusLabel;
    private java.awt.BorderLayout borderLayout1;
    private javax.swing.JPanel chartsPanel;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem contextHelpMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenuItem deleteProgressPopupItem;
    private javax.swing.JMenuItem deleteTaskPopupItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton helpButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JMenuItem logConsoleMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JToolBar mainToolbar;
    private javax.swing.JToolBar mainToolbar1;
    private javax.swing.JToolBar mainToolbar2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newProgressPopupItem;
    private javax.swing.JMenuItem newTaskMenuItem;
    private javax.swing.JMenuItem newTaskPopupItem;
    private javax.swing.JMenuItem newWorkSpaceMenuItem;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JPopupMenu progressPopupMenu;
    private org.jdesktop.swingx.JXTable progressesTable;
    private javax.swing.JPanel progressesTablePanel;
    private javax.swing.JButton redoButton;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JFileChooser saveFileChooser;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPopupMenu tablePopupMenu;
    private javax.swing.JLabel tableProgressCountLabel;
    private javax.swing.JLabel tableTaskNameLabel;
    private javax.swing.JPopupMenu taskPopupMenu;
    private javax.swing.JPopupMenu taskPopupMenu1;
    private org.jdesktop.swingx.JXTreeTable taskTree;
    private javax.swing.JPanel taskTreePanel;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JSplitPane tree_table_splitPane;
    private javax.swing.JButton undoButton;
    private javax.swing.JMenuItem undoMenuItem;
    // End of variables declaration//GEN-END:variables
	
	
	private class PersistenceTreeAdapter implements PersistentComponent	{
		
		private final JComponent _tree;
		public PersistenceTreeAdapter (JComponent tree){
			this._tree = tree;
		}
		public String getPersistenceKey () {
			return "tasktree";
		}
		
		public void makePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
			PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this._tree);
		}
		
		public boolean restorePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
			return PersistenceUtils.restorePersistentBoundsToPreferredSize (props, this.getPersistenceKey (), this._tree);
		}
		
	}
	
	/**
	 * Implementa la persistenza delle dimensioni per un pannello.
	 */
	private class PersistencePanelAdapter implements PersistentComponent	{
		
		private final JComponent _panel;
		private final String _key;
		public PersistencePanelAdapter (JPanel panel, String key){
			this._panel = panel;
			this._key = key;
		}
		public String getPersistenceKey () {
			return _key;
		}
		
		public void makePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
			PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this._panel);
		}
		
		public boolean restorePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
			return PersistenceUtils.restorePersistentBoundsToPreferredSize (props, this.getPersistenceKey (), this._panel);
		}
		
	}
	
	private final static String[] voidStringArray = new String[0];
	
	/**
	 * Indice della colonna di durata.
	 * <BR>
	 *ATTENZIONE: per questa colonna il modello ritorna il valore del PieceOfWork.
	 */
	public final static int DURATION_COL_INDEX = 0;

	/**
	 * Indice della colonna di inizio.
	 */
	public final static int START_COL_INDEX = 1;

	/**
	 * Indice della colonna di fine.
	 */
	public final static int END_COL_INDEX = 2;
		
	/**
	 * Indice della colonna di descrizione.
	 */
	public final int DESCRIPTION_COL_INDEX = 3;
		
	/**
	 * Il modello della tabella degli avanzamenti.
	 */
	private class ProgressesJTableModel extends AbstractTableModel implements TaskTreeModelListener, TreeSelectionListener, TableModelListener, AdvancingTicListener, WorkAdvanceModelListener {
		
		private final TaskTreeModelImpl _taskTreeModel;
		
		private Task _master;
		private TaskTreePath _masterPath;
		private List<PieceOfWork> _progresses;
		

		
		
		
		public ProgressesJTableModel (final TaskTreeModelImpl ttm){
			this._taskTreeModel = ttm;
//			reload ();
			fireTableStructureChanged ();
			ttm.addTaskTreeModelListener (this);
			ttm.addWorkAdvanceModelListener (this);
			_ticPump.addAdvancingTicListener (this);
		}
		
		public int getColumnCount () {
			return 4;
		}
		
		public int getRowCount () {
			if (null==_master) {
				return 0;
			}
			return _master.getPiecesOfWork ().size ();
		}
		
		private PieceOfWork getPieceOfWork (int rowIndex) {
			return _progresses.get (rowIndex);
		}
		public Object getValueAt (int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case DURATION_COL_INDEX:
					return getPieceOfWork (rowIndex);
//				case 1:
//					return _progresses.get (rowIndex).getTask ().getName ();
				case START_COL_INDEX:
					return getPieceOfWork (rowIndex).getFrom ();
				case END_COL_INDEX:
					return getPieceOfWork (rowIndex).getTo ();
				case DESCRIPTION_COL_INDEX:
					return getPieceOfWork (rowIndex).getDescription ();
			}
			return null;
		}
		
		
		
//		private void reload (){
//			reload (_taskTreeModel.getRoot ());
//		}
		
		private void reload (Task newMaster){
			_master = newMaster;
			cache ();
			fireTableDataChanged ();
		}
		
		private void cache (){
			if (_master == null){
				/*
				 * master non impostato, modello vuoto.
				 */
				_progresses = new ArrayList ();
				_masterPath = null;
				return;
			} else {
				_progresses = _master.getPiecesOfWork ();
				_masterPath = new TaskTreePath (_master);
			}
			cacheAdvancingProgress ();
		}
		
		private final String[] _columnNames = new String[] {
			java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("table_header/Duration")
			, /*"Task", */
			java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("table_header/From"), 
			java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("table_header/To"), 
			java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("table_header/Notes")
		};
		
		public String getColumnName (int columnIndex) {
			return _columnNames[columnIndex];
		}
		
		
		public boolean isCellEditable (int rowIndex, int columnIndex) {
			return 
				!getPieceOfWork (rowIndex).isEndOpened ()
				|| (columnIndex!=DURATION_COL_INDEX && columnIndex!=END_COL_INDEX);
		}
		
		private final Class[] _columnClasses = new Class[] {
			Duration.class,
			Date.class,
			Date.class,
			String.class
		};
		public Class getColumnClass (int col) {
			return _columnClasses[col];
		}
		
		/**
		 * Imposta il valore nel modello della tabella, propagandolo al modello applicativo.
		 */
		public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case DURATION_COL_INDEX:
				{
					/*
					 * asimmetico: riceve una Duration, anche se getValue ritorna una PieceOfWork
					 * Cio' e' dovuto al fattoche l'editazione non imposta date di inizio e fine,ma solo una durata
					 */
					
					final PieceOfWork pow = getPieceOfWork (rowIndex);
					
					
					_context.getModel ().updatePieceOfWork (pow, pow.getFrom (), new Date (pow.getFrom ().getTime ()+((Duration)aValue).getTime ()), pow.getDescription ());
					break;
				}
				case START_COL_INDEX:
				{
					final PieceOfWork pow = getPieceOfWork (rowIndex);
					_context.getModel ().updatePieceOfWork (pow, (Date)aValue, pow.getTo (), pow.getDescription ());
					break;
				}
				case END_COL_INDEX:
				{
					final PieceOfWork pow = getPieceOfWork (rowIndex);
					_context.getModel ().updatePieceOfWork (pow, pow.getFrom (), (Date)aValue, pow.getDescription ());
					break;
				}
				case DESCRIPTION_COL_INDEX:
				{
					final PieceOfWork pow = getPieceOfWork (rowIndex);
					_context.getModel ().updatePieceOfWork (pow, pow.getFrom (), pow.getTo (), (String)aValue);
					break;
				}
				default:
					return;
			}
		}
		
		/**
		 * Gestore delle modifiche ai nodi dell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesChangedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, int type, TaskTreePath path, int[] childIndices, Task[] children){
				//nessun effetto in caso di modifica ai task
				return null;
			}
			public Object inspectPROGRESS (Object source, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (_masterPath==null || !path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
					cache ();
					fireTableDataChanged ();
				} else {
					/*
					 * Modifiche puntuali agli avanzamenti del percorso
					 */
					for (int i=0;i<childIndices.length;i++){
						fireTableRowsUpdated (childIndices[i], childIndices[i]);
					}
				}
				return null;
			}
		};
		public void treeNodesChanged (TaskTreeModelEvent e) {
			e.allow (_treeNodesChangedInspector);
		}
		
		/**
		 * Gestore degli inserimenti nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesInsertedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, int type, TaskTreePath path, int[] childIndices, Task[] children){
				//nessun effetto in caso di inserimento task
				return null;
			}
			public Object inspectPROGRESS (Object source, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (!path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
					cache ();
					fireTableDataChanged ();
				} else {
					/*
					 * Inserimenti puntuali di avanzamenti nel percorso
					 */
					for (int i=0;i<childIndices.length;i++){
						fireTableRowsInserted (childIndices[i], childIndices[i]);
					}
				}
				return null;
			}
		};
		public void treeNodesInserted (TaskTreeModelEvent e) {
			e.allow (_treeNodesInsertedInspector);
		}
		
		/**
		 * Gestore delle rimozioni nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesRemovedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, int type, TaskTreePath path, int[] childIndices, Task[] children){
				if (null==_masterPath) {
					/*
					 * modello in stato inattivo
					 */
					return null;
				}
				if (!_masterPath.contains (path)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				for (int i=0;i<children.length;i++){
					if (_masterPath.contains (children[i])){
						/*
						 * Cambia master
						 */
						reload (null);
						break;
					}
				}
				return null;
			}
			public Object inspectPROGRESS (Object source, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (!path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
					cache ();
					fireTableDataChanged ();
				} else {
					/*
					 * Inserimenti puntuali di avanzamenti nel percorso
					 */
					for (int i=0;i<childIndices.length;i++){
						fireTableRowsDeleted (childIndices[i], childIndices[i]);
					}
				}
				return null;
			}
		};
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			e.allow (_treeNodesRemovedInspector);
		}
		
		public void treeStructureChanged (TaskTreeModelEvent e) {
			if (_masterPath==null) {
				return;
			}
			if (e.getPath ().contains (_masterPath)) {
				cache ();
				fireTableDataChanged ();
			}
		}
		
		public void valueChanged (TreeSelectionEvent e) {
			final TreePath path = e.getPath ();
			if (path==null) {
				return;
			}
			final Object o = path.getLastPathComponent ();
			if (o instanceof Task){
				reload ((Task)o);
			}
		}

		public void tableChanged (TableModelEvent e) {
//			if (e.getType ()==TableModelEvent.UPDATE){
//				return;
//			}
			tableTaskNameLabel.setText (_master==null?"":_master.getName ());
			tableProgressCountLabel.setText (Integer.toString (progressesTable.getModel ().getRowCount ()));
		}

		public void advanceTic (AdvancingTicEvent e) {
			final int l = advancingIdx.length;
			for (int i = 0; i < l; i++) {
				final int idx = advancingIdx[i];
				fireTableCellUpdated (idx, DURATION_COL_INDEX);
			}
//			fireTableChanged (new TableModelEvent (this, 0, getRowCount ()-1, DURATION_COL_INDEX));
		}

		public void elementsInserted (WorkAdvanceModelEvent e) {
			cacheAdvancingProgress ();
		}

		public void elementsRemoved (WorkAdvanceModelEvent e) {
			cacheAdvancingProgress ();
		}
		
		/**
		 * array contenente gli indici delle righe che sono in avanzamento
		 */
		private int[] advancingIdx;
		/**
		 * aggiorna l'array contenente gli indici delle righe che sono in avanzamento
		 */
		private void cacheAdvancingProgress () {
			final Set advancing = _context.getModel ().getAdvancing ();
			int[] tmp = new int[0];
			int counter = 0;
			int i = 0;
			for (final PieceOfWork p : _progresses) {
				if (advancing.contains (p)) {
					final int[] tmp1 = tmp;
					tmp = new int[counter+1];
					
					System.arraycopy (tmp1, 0, tmp, 0, tmp1.length);
					
					tmp[counter] = i;
					counter++;
				}
				i++;
			}
			advancingIdx = new int[tmp.length];
			System.arraycopy (tmp, 0, advancingIdx, 0, tmp.length);
		}
	}
	
	
	/**
	 * Modello dell'albero deglki avanzamenti.
	 */
	private class TaskJTreeModel extends AbstractTreeTableModel implements /*TreeModel,*/ TaskTreeModelListener, PropertyChangeListener, WorkAdvanceModelListener, AdvancingTicListener {
		
		private final TaskTreeModelImpl _model;
		
		/**
		 * Indice della colonna contenente l'albero.
		 */
		public final static int TREE_COLUMN_INDEX = 0;
		/**
		 * Indice della colonna contenente la durata giornaliera.
		 */
		public final static int TODAY_COLUMN_INDEX = 1;
		/**
		 * Indice della colonna contenente la durata globale.
		 */
		public final static int GLOBAL_COLUMN_INDEX = 2;
		
		public TaskJTreeModel (TaskTreeModelImpl model) {
			super (model.getRoot ());
			_model = model;
			model.addTaskTreeModelListener (this);
//			fireTreeStructureChanged (this, new TreePath (new Object[]{getRoot ()}));
			_ticPump.addAdvancingTicListener (this);			
			model.addWorkAdvanceModelListener (this);
		}
		
//		public Object getChild (Object parent, int index) {
//			return ((Task)parent).childAt (index);
//		}
//
//		public int getChildCount (Object parent) {
//			return ((Task)parent).childCount ();
//		}
		
		public void addTreeModelListener (javax.swing.event.TreeModelListener l) {
			listenerList.add (TreeModelListener.class, l);
		}

		public boolean isCellEditable (Object node, int column) {
			//colonna dell'albero editabile'
			return column==0;
		}
		
		public int getIndexOfChild (Object parent, Object child) {
			return ((Task)parent).childIndex ((Task)child);
		}
		
//		public Object getRoot () {
//			return this._model.getRoot ();
//		}
//
//		public boolean isLeaf (Object node) {
//			return ((Task)node).childCount ()==0;
//		}
//
//		public void removeTreeModelListener (javax.swing.event.TreeModelListener l) {
//			listenerList.remove (TreeModelListener.class, l);
//		}
//
//		public void valueForPathChanged (javax.swing.tree.TreePath path, Object newValue) {
//		}
//
//		private void fireTreeStructureChanged(Object source, TreePath path) {
//			// Guaranteed to return a non-null array
//			Object[] listeners = listenerList.getListenerList();
//			TreeModelEvent e = null;
//			// Process the listeners last to first, notifying
//			// those that are interested in this event
//			for (int i = listeners.length-2; i>=0; i-=2) {
//				if (listeners[i]==TreeModelListener.class) {
//					// Lazily create the event:
//					if (e == null)
//						e = new TreeModelEvent(source, path);
//					((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
//				}
//			}
//		}
//
//
//
//		/**
//		 * Notifies all listeners that have registered interest for
//		 * notification on this event type.  The event instance
//		 * is lazily created using the parameters passed into
//		 * the fire method.
//		 *
//		 * @param source the node being changed
//		 * @param path the path to the root node
//		 * @param childIndices the indices of the changed elements
//		 * @param children the changed elements
//		 * @see EventListenerList
//		 */
//		protected void fireTreeNodesChanged(Object source, Object[] path,
//											int[] childIndices,
//											Object[] children) {
//			// Guaranteed to return a non-null array
//			Object[] listeners = listenerList.getListenerList();
//			TreeModelEvent e = null;
//			// Process the listeners last to first, notifying
//			// those that are interested in this event
//			for (int i = listeners.length-2; i>=0; i-=2) {
//				if (listeners[i]==TreeModelListener.class) {
//					// Lazily create the event:
//					if (e == null)
//						e = new TreeModelEvent(source, path,
//											   childIndices, children);
//					((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
//				}
//			}
//		}
//
//		/**
//		 * Notifies all listeners that have registered interest for
//		 * notification on this event type.  The event instance
//		 * is lazily created using the parameters passed into
//		 * the fire method.
//		 *
//		 * @param source the node where new elements are being inserted
//		 * @param path the path to the root node
//		 * @param childIndices the indices of the new elements
//		 * @param children the new elements
//		 * @see EventListenerList
//		 */
//		protected void fireTreeNodesInserted(Object source, Object[] path,
//											int[] childIndices,
//											Object[] children) {
//			// Guaranteed to return a non-null array
//			Object[] listeners = listenerList.getListenerList();
//			TreeModelEvent e = null;
//			// Process the listeners last to first, notifying
//			// those that are interested in this event
//			for (int i = listeners.length-2; i>=0; i-=2) {
//				if (listeners[i]==TreeModelListener.class) {
//					// Lazily create the event:
//					if (e == null)
//						e = new TreeModelEvent(source, path,
//											   childIndices, children);
//					((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
//				}
//			}
//		}
//
//		/**
//		 * Notifies all listeners that have registered interest for
//		 * notification on this event type.  The event instance
//		 * is lazily created using the parameters passed into
//		 * the fire method.
//		 *
//		 * @param source the node where elements are being removed
//		 * @param path the path to the root node
//		 * @param childIndices the indices of the removed elements
//		 * @param children the removed elements
//		 * @see EventListenerList
//		 */
//		protected void fireTreeNodesRemoved(Object source, Object[] path,
//											int[] childIndices,
//											Object[] children) {
//			// Guaranteed to return a non-null array
//			Object[] listeners = listenerList.getListenerList();
//			TreeModelEvent e = null;
//			// Process the listeners last to first, notifying
//			// those that are interested in this event
//			for (int i = listeners.length-2; i>=0; i-=2) {
//				if (listeners[i]==TreeModelListener.class) {
//					// Lazily create the event:
//					if (e == null)
//						e = new TreeModelEvent(source, path,
//											   childIndices, children);
//					((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
//				}
//			}
//		}
		
		
		public Object getChild (Object parent, int index) {
			return ((Task)parent).childAt (index);
		}
		
		public int getChildCount (Object parent) {
			return((Task)parent).childCount ();
		}
		
		public int getColumnCount () {
			return 3;
		}
		
		private String[] columnNames = new String[] {"Task", "Today", "Total"};
		public String getColumnName (int column) {
			return columnNames[column];
		}
		
		
		public void propertyChange (PropertyChangeEvent evt){
		}
		
		
		
		/**
		 * Gestore delle modifiche nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesChangedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesChanged (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				return null;
			}
		};
		public void treeNodesChanged (TaskTreeModelEvent e) {
			e.allow (_treeNodesChangedInspector);
		}
		
		/**
		 * Gestore degli inserimenti nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesInsertedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesInserted (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
//				fireTreeNodesChanged (source, toObjectArray (path.getParentPath ()), new int[] {path.getLastPathComponent ().getParent ().childIndex (path.getLastPathComponent ())}, new Object[] {path.getLastPathComponent ()});
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));

				return null;
			}
		};
		public void treeNodesInserted (TaskTreeModelEvent e) {
			e.allow (_treeNodesInsertedInspector);
		}
		
		/**
		 * Gestore delle rimozioni nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesRemovedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesRemoved (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				return null;
			}
		};
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			e.allow (_treeNodesRemovedInspector);
		}
		
		/**
		 * Gestore delle modifiche alla struttura nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeStructureChangedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, int type, TaskTreePath path, int[] childIndices, Task[] children){
				if (path.getParentPath ()!=null) {
					Object[] ar = null;
					if (children!=null) {
						ar = new Object[children.length];
						System.arraycopy (children, 0, ar, 0, children.length);
					}
					
					fireTreeStructureChanged (source, toObjectArray (path), childIndices, ar);
				} else {
					//radice
					TaskJTreeModel.super.root = path.getLastPathComponent ();
		            fireTreeStructureChanged(source, toObjectArray (path), null, null);
				}
				return null;
			}
			public Object inspectPROGRESS (Object source, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				return null;
			}
		};
		public void treeStructureChanged (TaskTreeModelEvent e) {
			e.allow (_treeStructureChangedInspector);
		}
		
//    /*
//     * Notifies all listeners that have registered interest for
//     * notification on this event type.  The event instance
//     * is lazily created using the parameters passed into
//     * the fire method.
//     *
//     * @param source the node where the tree model has changed
//     * @param path the path to the root node
//     * @see EventListenerList
//     */
//    private void fireTreeStructureChanged(Object source, TreePath path) {
//        // Guaranteed to return a non-null array
//        Object[] listeners = listenerList.getListenerList();
//        TreeModelEvent e = null;
//        // Process the listeners last to first, notifying
//        // those that are interested in this event
//        for (int i = listeners.length - 2; i >= 0; i -= 2) {
//            if (listeners[i] == TreeModelListener.class) {
//                // Lazily create the event:
//                if (e == null)
//                    e = new TreeModelEvent(source, path);
//                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
//            }
//        }
//    }
	
	
		private Object[] toObjectArray (final TaskTreePath p){
			final List l = new ArrayList ();
			populateList (l, p);
			return l.toArray ();
		}
		private void populateList (final List l, final TaskTreePath p){
			final Task t = p.getLastPathComponent ();
			if (t!=null) {
				l.add (0, t);
			}
			final TaskTreePath parentPath = p.getParentPath ();
			if (parentPath!=null) {
				populateList (l, parentPath);
			}
		}
		
		private TreePath toTreePath (final TaskTreePath p){
			return new TreePath (toObjectArray (p));
		}
		
		public Object getValueAt (Object object, int i) {
			switch (i) {
				//colonna  albero
				case TREE_COLUMN_INDEX:
//					return ((Task)object).getName ();
					/*
					 * Ritorna direttamente il task
					 */
					return object;
				//colonna durata giornaliera
				case TODAY_COLUMN_INDEX: {
					long totalDuration = 0;
					for (final Iterator it = ((ProgressItem)object).getSubtreeProgresses ().iterator ();it.hasNext ();){
						final Progress p = (Progress)it.next ();
						totalDuration += p.getDuration ().getTime ();
					}
					
					if (totalDuration==0){
						return "";
					}
					
					
					return DurationUtils.format (new Duration (totalDuration));
				}
				//colonna durata totale
				case GLOBAL_COLUMN_INDEX: /*@todo ottimizzare!*/ {
					_todayPeriod.reset ();
					
					for (final Iterator it = ((ProgressItem)object).getSubtreeProgresses ().iterator ();it.hasNext ();){
						final Progress p = (Progress)it.next ();
						_todayPeriod.computeProgress (p);
					}
					
					final Duration duration = _todayPeriod.getTodayAmount ();
					if (duration.getTime ()==0){
						return "";
					}
					
					return DurationUtils.format (duration);
				}
			}
			return null;
		}
		
		public void setValueAt (Object object, Object object0, int i) {
			switch (i) {
				//colonna  albero
				case TREE_COLUMN_INDEX:
					final ProgressItem t = (ProgressItem)object0;
					_model.updateTask (t, (String)object);
				default:
					return;
			}
		}
		
		
		
		private LocalizedPeriod _today = null;
		
		private final LocalizedPeriod getToday (){
			if (this._today==null){
				final Calendar now = new GregorianCalendar ();
				
				now.set (Calendar.HOUR_OF_DAY, 0);
				now.set (Calendar.MINUTE, 0);
				now.set (Calendar.SECOND, 0);
				now.set (Calendar.MILLISECOND, 0);
				//		now.roll (Calendar.DATE, 1);
				final Date periodStartDate = new Date (now.getTime ().getTime ());
				
				now.add (Calendar.DAY_OF_YEAR, 1);
				final Date periodFinishDate = new Date (now.getTime ().getTime ());
				this._today = new LocalizedPeriodImpl (periodStartDate, periodFinishDate);
			}
			
			return this._today;
			
		}

		public void elementsInserted (WorkAdvanceModelEvent e) {
		}

		public void elementsRemoved (WorkAdvanceModelEvent e) {
		}

		public void advanceTic (AdvancingTicEvent e) {
//			for (final int idx : advancingIdx) {
//				fireTableCellUpdated (idx, DURATION_COL_INDEX);
//			}
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
		        m.fireTableChanged(new TableModelEvent(m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
		}
		
		
		/**
		 * Un periodo.
		 */
		private final class TodayPeriod extends LocalizedPeriodImpl implements Comparable {
			/*
			 * durata calcolata in millisecondi
			 */
			private long _millisecs = 0;
			
			public TodayPeriod (){
				super (getToday ());
				
			}
			
			public void reset (){
				setFrom (getToday ().getFrom ());
				setTo (getToday ().getTo ());
				this._millisecs = 0;
			}
			
			public int compareTo (Object o) {
				return compareToStart ((LocalizedPeriod)o);
			}
			
			/**
			 * Calcola la quota di lavoro appartenente al giorno odierno per l'avanzamento specificato.
			 */
			public void computeProgress ( Progress progress ){
				LocalizedPeriod toIntersect = null;
				if (!progress.isEndOpened ()){
					if (!this.intersects (progress)){
						return;
					}
					toIntersect = progress;
				} else {
					//avanzamento in corso
					toIntersect = new LocalizedPeriodImpl (progress.getFrom (), new Date ());
				}
				
				final LocalizedPeriod intersection = this.intersection (toIntersect);
				this._millisecs += intersection.getDuration ().getTime ();
				
			}
			
			/**
			 * Ritorna la durata risultante del lavoro giornaliero
			 *@returns la durata risultante del lavoro giornaliero
			 */
			public Duration getTodayAmount (){
				return new Duration (this._millisecs);
			}
		}
		
		private final TodayPeriod _todayPeriod = new TodayPeriod ();
		
	}
	
	/**
	 * Mostra la dialog di inserimento nuovo progress.
	 */
	private void showNewProgressDialog (Task t){
		this._actionNotifier.fireActionPerformed (new ActionEvent (this, -1, "showNewProgressDialog"));
	}
	
	/**
	 * Mostra la dialog di inserimento nuovo task.
	 */
	private void showNewTaskDialog (){
//		this._actionNotifier.fireActionPerformed (new ActionEvent (new NewTaskDialogRequester (), -1, "showNewTaskDialog"));
	}
	
	public class NewTaskDialogRequester {
		private final Task _parent;
		public NewTaskDialogRequester (Task parent) {
			this._parent = parent;
		}
		public Task getParent (){
			return this._parent;
		}
	}
	
	public class NewProgressDialogRequester {
		private final Task _task;
		public NewProgressDialogRequester (Task task) {
			this._task = task;
		}
		public Task getTask (){
			return this._task;
		}
	}
	
	/**
	 * Elimina un avanzamento dal modello.
	 */
	private void deleteProgressea (List<PieceOfWork> p){
		if (p.isEmpty ()) {
			return;
		}
		this._context.getModel ().removePiecesOfWork (p.get (0).getTask (), p);
	}
	
	/**
	 * Elimina un locale dal modello.
	 */
	private void deleteTasks (List<Task> t){
		if (t.isEmpty ()) {
			return;
		}
		if (JOptionPane.showConfirmDialog (
			this,
			StringUtils.toStringArray (
			"You are removing a task. All sub-tasks and progresses will be erased. Do you want to continue?"
			)
			)!=JOptionPane.OK_OPTION){
			return;
		}
		this._context.getModel ().removeTasks (t.get (0).getParent (), t);
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
	
	
	
	/**
	 * Stato di operazione in corso.
	 */
	private class Processing {
		private boolean p;
		
		public void setValue (boolean processing){
			p = processing;
		}
		public boolean booleanValue (){
			return p;
		}
	}
	
	
	
	
	boolean postInitialized = false;
	/**
	 * Mostra la finestra, eventualmente completando la fase di inizializzazione.
	 * @see #postInit
	 */
	public void show (){
		if (!postInitialized){
			postInit ();
		}
		super.show ();
	}
	
	/**
	 * Fase di post-inizializzazione.
	 * Consente di inizializzare cio' che prima avrebbe prodottoproblemi di dipendenze:
	 * Registra la dialog di ricerca.
	 */
	private void postInit (){
		postInitialized = true;
	}
	
	
	
	
	/**
	 * Array delle azioni.
	 */
	private final Map actions = new HashMap ();
	
	/**
	 * Crea l'array delle azioni registrate su di un componente testuale.
	 */
	private void createActionTable (JTextComponent textComponent) {
		Action[] actionsArray = textComponent.getActions ();
		for (int i = 0; i < actionsArray.length; i++) {
			Action a = actionsArray[i];
			actions.put (a.getValue (Action.NAME), a);
		}
	}
	
	private Action getActionByName (String name) {
		return (Action)(actions.get (name));
	}
	
	
	
	
	
	/**
	 * Crea un nuovo task.
	 *
	 */
	private class NewTaskAction extends AbstractAction implements TreeSelectionListener {
		
		
		/**
		 * Costruttore.
		 */
		public NewTaskAction () {
			super (null, new javax.swing.ImageIcon (MainWindow.class.getResource ("/com/davidecavestro/timekeeper/gui/images/filenew.png")));
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			final TaskTreeModelImpl m = _context.getModel ();
			m.insertNodeInto (new ProgressItem (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("new_task_name/New_task")), _candidateParent, _candidateParent.childCount ());
		}
		
		Task _candidateParent;
		private boolean isEnabled (final Task candidateParent) {
			return candidateParent!=null;
		}
//		
//		public boolean isEnabled () {
//			return isEnabled (_candidateParent);
//		}

		public void valueChanged (TreeSelectionEvent e) {
			final Task oldCandidateParent = _candidateParent;
			final TreePath path = e.getPath ();
			if (path==null) {
				_candidateParent = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					_candidateParent = (Task)o;
				} else {
					_candidateParent = null;
				}
			}
			if (oldCandidateParent!=_candidateParent){
				setEnabled (isEnabled (_candidateParent));
			}
		}
		
	}
	
	/**
	 * Elimina un task.
	 *
	 */
	private class DeleteTaskAction extends AbstractAction implements TreeSelectionListener {
		
		
		/**
		 * Costruttore.
		 */
		public DeleteTaskAction () {
			super (null, new javax.swing.ImageIcon (MainWindow.class.getResource ("/com/davidecavestro/timekeeper/gui/images/editdelete.png")));
//			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			final TaskTreeModelImpl m = _context.getModel ();
			for (final Task parent : _removalCandidates.keySet ()) {
				m.removeTasks (parent, _removalCandidates.get (parent));
			}
		}
		
		Map<Task, List<Task>> _removalCandidates;
		private boolean isEnabled (final Map<Task, List<Task>> removalCandidates) {
			return !removalCandidates.isEmpty ();
		}

		public void valueChanged (TreeSelectionEvent e) {
			final Map<Task, List<Task>> removalCandidates = new HashMap ();
			for (final int r : taskTree.getSelectedRows ()){
				final Task candidate = (Task) taskTree.getModel ().getValueAt (r, TaskJTreeModel.TREE_COLUMN_INDEX);
				final Task parent = candidate.getParent ();
				/*
				 * e' un Task
				 */
				if (parent!=null) {

					/*
					 * ha un padre: non e' il nodo radice
					 */
					if (removalCandidates.containsKey (parent)) {
						/*
						 * gia' un fratello schedulato per la cancellazione.
						 */
						final List<Task> l = removalCandidates.get (parent);
						l.add (candidate);
					} else {
						/*
						 * nessun fratello gia' in lista.
						 */
						final List<Task> l = new ArrayList ();
						l.add (candidate);
						removalCandidates.put (parent, l);
					}
				}
			}

			_removalCandidates = removalCandidates;
			setEnabled (isEnabled (removalCandidates));
		}
		
	}
	
	/**
	 * Fa partire un nuovo avanzamento.
	 *
	 */
	private class StartProgressAction extends AbstractAction implements TreeSelectionListener, WorkAdvanceModelListener {
		
		
		/**
		 * Costruttore.
		 */
		public StartProgressAction () {
			super (null, new javax.swing.ImageIcon (MainWindow.class.getResource ("/com/davidecavestro/timekeeper/gui/images/start.png")));
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
			taskTree.addTreeSelectionListener (this);
			_context.getModel ().addWorkAdvanceModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			final TaskTreeModelImpl m = _context.getModel ();
			m.insertPieceOfWorkInto (new Progress (new Date (), null, (ProgressItem)_candidateParent), _candidateParent, _candidateParent.pieceOfWorkCount ());
		}
		
		private boolean _isEnabled () {
			return _candidateParent!=null && _context.getModel ().getAdvancing ().isEmpty ();
		}

		Task _candidateParent;
		public void valueChanged (TreeSelectionEvent e) {
			final Task oldCandidateParent = _candidateParent;
			final TreePath path = e.getPath ();
			if (path==null) {
				_candidateParent = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					_candidateParent = (Task)o;
				} else {
					_candidateParent = null;
				}
			}
			if (oldCandidateParent!=_candidateParent){
				setEnabled (_isEnabled ());
			}
		}
		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}

		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
	}
	
	/**
	 * Ferma un avanzamento in progress.
	 *
	 */
	private class StopProgressAction extends AbstractAction implements WorkAdvanceModelListener {
		
		
		/**
		 * Costruttore.
		 */
		public StopProgressAction () {
			super (null, new javax.swing.ImageIcon (MainWindow.class.getResource ("/com/davidecavestro/timekeeper/gui/images/stop.png")));
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
			_context.getModel ().addWorkAdvanceModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			final TaskTreeModelImpl m = _context.getModel ();
			final PieceOfWork pow = _context.getModel ().getAdvancing ().iterator ().next ();
			pow.setTo (new Date ());
			m.pieceOfWorkChanged (pow);
		}

		private void setEnabled () {
			setEnabled (!_context.getModel ().getAdvancing ().isEmpty ());
		}
		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled ();
		}

		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled ();
		}
		
	}
	
	/**
	 * Crea un nuovo avanzamento.
	 *
	 */
	private class NewPieceOfWorkAction extends AbstractAction implements TreeSelectionListener {
		
		
		/**
		 * Costruttore.
		 */
		public NewPieceOfWorkAction () {
			super (null, new javax.swing.ImageIcon (MainWindow.class.getResource ("/com/davidecavestro/timekeeper/gui/images/add.png")));
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			final TaskTreeModelImpl m = _context.getModel ();
			_context.getWindowManager ().getNewPieceOfWorkDialog ().showForTask (_candidateParent);
//			m.insertPieceOfWorkInto (new Progress (new Date (), null, (ProgressItem)_candidateParent), _candidateParent, _candidateParent.pieceOfWorkCount ());
		}
		
		private boolean _isEnabled () {
			return _candidateParent!=null;
		}

		Task _candidateParent;
		public void valueChanged (TreeSelectionEvent e) {
			final Task oldCandidateParent = _candidateParent;
			final TreePath path = e.getPath ();
			if (path==null) {
				_candidateParent = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					_candidateParent = (Task)o;
				} else {
					_candidateParent = null;
				}
			}
			if (oldCandidateParent!=_candidateParent){
				setEnabled (_isEnabled ());
			}
		}
		
		
	}
	
	/**
	 * Elimina un avanzamento.
	 *
	 */
	private class DeletePieceOfWorkAction extends AbstractAction implements ListSelectionListener {
		
		/**
		 * Costruttore.
		 */
		public DeletePieceOfWorkAction () {
			super (null, new javax.swing.ImageIcon (MainWindow.class.getResource ("/com/davidecavestro/timekeeper/gui/images/editdelete.png")));
//			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));

			progressesTable.getSelectionModel ().addListSelectionListener (this);
			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);

			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			final TaskTreeModelImpl m = _context.getModel ();
			for (final Task t : _removalCandidates.keySet ()) {
				m.removePiecesOfWork (t, _removalCandidates.get (t));
			}
		}
		
		Map<Task, List<PieceOfWork>> _removalCandidates;
		private boolean isEnabled (final Map<Task, List<PieceOfWork>> removalCandidates) {
			return !removalCandidates.isEmpty ();
		}

		public void valueChanged (ListSelectionEvent e) {
			final int col = progressesTable.getColumnModel ().getSelectionModel ().getLeadSelectionIndex ();
			final int row = progressesTable.getSelectionModel ().getLeadSelectionIndex ();

			if (e.getValueIsAdjusting ()){
				/* evento spurio */
				return;
			}
			
			final Map<Task, List<PieceOfWork>> removalCandidates = new HashMap ();
			for (final int r : progressesTable.getSelectedRows ()){
				final PieceOfWork candidate = (PieceOfWork) progressesTable.getModel ().getValueAt (r, DURATION_COL_INDEX);
				if (!candidate.isEndOpened ()) {
					/*
					 * non sta avanzando
					 */
					final Task t = candidate.getTask ();

					if (removalCandidates.containsKey (t)) {
						/*
						 * gia' un fratello schedulato per la cancellazione.
						 */
						final List<PieceOfWork> l = removalCandidates.get (t);
						l.add (candidate);
					} else {
						/*
						 * nessun fratello gia' in lista.
						 */
						final List<PieceOfWork> l = new ArrayList ();
						l.add (candidate);
						removalCandidates.put (t, l);
					}
				}
			}

			_removalCandidates = removalCandidates;
			setEnabled (isEnabled (removalCandidates));
		}

		
	}
	
	/**
	 * Notifica l'applicazione l'esecuzione di lavori lunghi, mostrando la barra di progressione.
	 * SOlo se il task dura + di 0.2 secs
	 */
	private abstract class VisibleWorker extends SwingWorker {
		private final String initialMessage;
		
		public VisibleWorker (String initialMessage){
			this.initialMessage = initialMessage;
		}
		
		public abstract void work ();
		public Object construct () {
			final Processing processing = new Processing ();
			processing.setValue (true);
			
			final java.util.Timer timer = new java.util.Timer ("processNotificationTimer", true);
			timer.schedule (new TimerTask (){
				public void run (){
					if (processing.booleanValue ()) {
						_context.setProcessing (true);
						progressBar.setString (initialMessage);
					}
				}},
				/* 2 decimi di secondo */
				200);
				
				
				try{
					work ();
				} finally {
					processing.setValue (false);
					_context.setProcessing (false);
					progressBar.setString ("");
				}
				
				return null;
		}
	}
	
	private class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
		final JFormattedTextField _component;
			
		public DateCellEditor () {
			_component = new JFormattedTextField (new DateFormatter (new SimpleDateFormat (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("from_to_format_long"))));
		}

		/**
		 * Editazione parte, in caso di evento del mouse, solo dopo doppio click
		 */
		@Override
		public boolean isCellEditable (EventObject eo) {
			if (eo instanceof MouseEvent) {
				return ((MouseEvent)eo).getClickCount ()>1;
			} else {
				return super.isCellEditable (eo);
			}
		}

		public Object getCellEditorValue () {
			return (Date)_component.getValue ();
		}

		public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
			_component.setValue ((Date)value);
			return _component;
		}
	}

	private class DurationCellEditor extends AbstractCellEditor implements TableCellEditor {
		final JFormattedTextField _component;
		public DurationCellEditor () {
			_component = new DurationTextField ();;
		}
		
		/**
		 * Editazione parte, in caso di evento del mouse, solo dopo doppio click
		 */
		@Override
		public boolean isCellEditable (EventObject eo) {
			if (eo instanceof MouseEvent) {
				return ((MouseEvent)eo).getClickCount ()>1;
			} else {
				return super.isCellEditable (eo);
			}
		}

		public Object getCellEditorValue () {
			/*
			 * ritorna una durata
			 */
			return (Duration)_component.getValue ();
		}

		public Component getTableCellEditorComponent (JTable table, Object value, boolean isSelected, int row, int column) {
			/*
			 * riceve un avanzamento
			 */
			_component.setValue (((PieceOfWork)value).getDuration ());
			_component.setText (DurationUtils.format (((PieceOfWork)value).getDuration ()));
			return _component;
		}
	}


	/**
	 * Trasferimento dati (DnD, cut&paste) per l'albero
	 */
	private final class TaskTreeTransferHandler extends ProgressItemTransferHandler{
		
		private final DataFlavor progressItemFlavor = DataFlavors.progressItemFlavor;
		
		public TaskTreeTransferHandler () {
			super (_context);
		}
		
		/**
		 * Crea un oggetto trasferibile.
		 *
		 * @param c l'elemento della UI.
		 * @return un oggetto trasferibile.
		 */
		protected Transferable createTransferable (JComponent c) {
			return new ProgressItemSelection (exportProgressItems (c));
		}
		
		public int getSourceActions (JComponent c) {
			return COPY_OR_MOVE;
		}
		
		public boolean importData (JComponent c, Transferable t) {
			if (canImport (c, t.getTransferDataFlavors ())) {
				try {
					if (hasProgressItemFlavor (t.getTransferDataFlavors ())){
						final Task[] progressItems = (Task[])t.getTransferData (progressItemFlavor);
						importProgressItems (c, progressItems);
						return true;
					} else if (hasProgressFlavor (t.getTransferDataFlavors ())){
						
						final PieceOfWork[] progresses = (PieceOfWork[])t.getTransferData (progressFlavor);
						importProgresses (c, progresses);
						return true;
					}
				} catch (UnsupportedFlavorException ufe) {
					_context.getLogger ().warning ("Error transferring UI data.", ufe);
				} catch (IOException ioe) {
					_context.getLogger ().warning ("Error transferring UI data.", ioe);
				}
			}
			
			return false;
		}
		
		protected void exportDone (JComponent c, Transferable data, int action) {
			cleanup (c, action == MOVE);
		}
		
		/**
		 * Does the flavor list have a ProgressItem flavor?
		 */
		protected boolean hasProgressItemFlavor (DataFlavor[] flavors) {
			if (progressItemFlavor == null) {
				return false;
			}
			
			for (int i = 0; i < flavors.length; i++) {
				if (progressItemFlavor.equals (flavors[i])) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Overridden to include a check for a ProgressItem or Progress flavor.
		 */
		public boolean canImport (JComponent c, DataFlavor[] flavors) {
			return hasProgressItemFlavor (flavors) || hasProgressFlavor (flavors);
		}
		
	/*
	 * If the remove argument is true, the drop has been
	 * successful and it's time to remove the selected items
	 * from the list. If the remove argument is false, it
	 * was a Copy operation and the original list is left
	 * intact.
	 */
		protected void cleanup (JComponent c, boolean remove) {
		}
		
		/*
		 * Bundle up the selected items in the list
		 * as a single string, for export.
		 */
		protected Task[] exportProgressItems (JComponent c) {
			if (c!=taskTree){return null;}

			final TreePath[] p = taskTree.getTreeSelectionModel ().getSelectionPaths ();
			if (p==null) {
				return new Task[0];
			}
			final Task[] t = new Task[p.length];
			for (int i = 0; i < p.length; i++) {
				t[i] = (Task)p[i].getLastPathComponent ();
			}
			return t;
		}
		
		/**
		 * Importa i task. 
		 *
		 * @param c il componente.
		 * @param progressItems gli avanzamentida importare.
		 */
		protected void importProgressItems (JComponent c, Task[] progressItems) {
			if (c!=taskTree){return;}
			if (progressItems.length == 0) {
				return;
			}
			final ProgressItem target = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			try {
				final Map<Task, List<Task>> m = new HashMap<Task, List<Task>> ();
				for (final Task t : progressItems) {
					final Task p = t.getParent ();
					if (m.containsKey (p)) {
						m.get (p).add (t);
					} else {
						final List<Task> l = new ArrayList<Task> ();
						l.add (t);
						m.put (p, l);
					}
				}
				for (final Task p : m.keySet ()) {
					_context.getModel ().moveTasksTo (p, m.get (p), target, -1);
				}
			} catch (final IllegalOperationException iae){
				JOptionPane.showMessageDialog (taskTree, iae.getMessage ());
			}
		}
		
		
		
		
		
		private final DataFlavor progressFlavor = DataFlavors.progressFlavor;
		
	
		
		/**
		 * Does the flavor list have a Progress flavor?
		 */
		protected boolean hasProgressFlavor (DataFlavor[] flavors) {
			if (progressFlavor == null) {
				return false;
			}
			
			for (int i = 0; i < flavors.length; i++) {
				if (progressFlavor.equals (flavors[i])) {
					return true;
				}
			}
			return false;
		}

		
		
		
		
		protected com.ost.timekeeper.model.Progress exportProgress (JComponent c) {
			return null;
		}
		
		/**
		 * 
		 * ATTENZIONE: gli avanzamenti devono appartenere allo stesso task.
		 * 
		 * @param c 
		 * @param progresses 
		 */
		protected void importProgresses (JComponent c, PieceOfWork[] progresses) {
			if (c!=taskTree){return;}
			if (progresses.length==0) {
				return;
			}
			final ProgressItem target = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			_context.getModel ().movePiecesOfWorkTo (progresses[0].getTask (), Arrays.asList (progresses), target, -1);
		}
		
	}




	/**
	 * Trasferimento dati (DnD, cut&paste) per la tabella
	 */
	private final class ProgressTableTransferHandler extends ProgressTransferHandler{
		public ProgressTableTransferHandler () {
			super (_context);
		}
		
		protected void cleanup (JComponent c, boolean remove) {
		}
		
		protected PieceOfWork[] exportProgresses (JComponent c) {
			if (c!=progressesTable){
				return null;
			}
			final int[] selectedRows = progressesTable.getSelectedRows ();
			final int selectedRowsCount = selectedRows.length;
			final PieceOfWork[] exportedProgresses = new PieceOfWork[selectedRowsCount];
			for (int i=0;i<selectedRowsCount;i++){
				exportedProgresses[i] = (PieceOfWork)((ProgressesJTableModel)progressesTable.getModel ()).getValueAt (selectedRows[i], DURATION_COL_INDEX);
			}
			System.out.println ("Exported progresses: "+selectedRowsCount);
			return exportedProgresses;
		}
		
		/**
		 * ATTENZIONE: si assume che gli avanzamenti appartengano almedesimo task.
		 */
		protected void importProgresses (JComponent c, PieceOfWork[] progresses) {
			if (c!=progressesTable){
				return;
			}
			if (progresses.length==0) {
				return;
			}
			
			final ProgressItem target = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			_context.getModel ().movePiecesOfWorkTo (progresses[0].getTask (), Arrays.asList (progresses), target, -1);

		}
		
	}

}

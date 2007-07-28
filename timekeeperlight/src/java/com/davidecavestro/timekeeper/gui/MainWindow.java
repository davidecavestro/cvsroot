/*
 * MainWIndow.java
 *
 * Created on 26 novembre 2005, 14.56
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.gui.CompositeIcon;
import com.davidecavestro.common.gui.SwingWorker;
import com.davidecavestro.common.gui.VTextIcon;
import com.davidecavestro.common.gui.persistence.PersistenceStorage;
import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.common.util.CalendarUtils;
import com.davidecavestro.common.util.ExceptionUtils;
import com.davidecavestro.common.util.IllegalOperationException;
import com.davidecavestro.common.util.StringUtils;
import com.davidecavestro.common.util.action.ActionNotifier;
import com.davidecavestro.common.util.action.ActionNotifierImpl;
import com.davidecavestro.common.util.file.CustomFileFilter;
import com.davidecavestro.common.util.file.FileUtils;
import com.davidecavestro.common.util.settings.SettingsSupport;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.backup.XMLAgent;
import com.davidecavestro.timekeeper.backup.XMLImporter;
import com.davidecavestro.timekeeper.gui.dnd.DataFlavors;
import com.davidecavestro.timekeeper.gui.dnd.ProgressItemTransferHandler;
import com.davidecavestro.timekeeper.gui.dnd.ProgressTransferHandler;
import com.davidecavestro.timekeeper.gui.dnd.TransferAction;
import com.davidecavestro.timekeeper.gui.dnd.TransferActionListener;
import com.davidecavestro.timekeeper.gui.dnd.TransferData;
import com.davidecavestro.timekeeper.help.HelpResources;
import com.davidecavestro.timekeeper.model.TaskTreeModelImpl;
import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.model.TaskTreePath;
import com.davidecavestro.timekeeper.model.WorkSpace;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelEvent;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelListener;
import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelEvent;
import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelListener;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import com.ost.timekeeper.util.Duration;
import com.ost.timekeeper.util.LocalizedPeriod;
import com.ost.timekeeper.util.LocalizedPeriodImpl;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import javax.help.CSH;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.decorator.Sorter;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableCellEditor;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

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
	
	private final TransferActionListener tal = new TransferActionListener ();
	
	/**
	 * Lista dei percorsi in fase di avanzamento
	 */
	private final List<TaskTreePath> _progressingPaths = new ArrayList<TaskTreePath> ();
	
	private final static ImageIcon DEFAULT_PROGRESSES_TABLE_BACKGROUND = new ImageIcon ();
	
	/*
	 * progress Table Backgroud Image
	 */
	ImageIcon ptbImage = new ImageIcon ();
	private int ptbImageWidth;
	private int ptbImageHeight;
        
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
		
//		setTableBackoundImage (new ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/Hourglass.png")));

		/*
		 * registrazione modello albero come listener sui modelli  
		 * (viene fatta prima dell'albero dato che le notifiche avvengono in ordine inverso, e la tabella dipende dall'albero. in questo modo si evita che notifiche all'albero scatenino ulteriori notifiche alla tabella prima che essa sia ricaricata a fronte della notifica da parte delmodello centrale)
		 */
		_context.getModel ().addTaskTreeModelListener ((TaskJTreeModel)taskTree.getTreeTableModel ());
		_ticPump.addAdvancingTicListener ((TaskJTreeModel)taskTree.getTreeTableModel ());
		_context.getModel ().addWorkAdvanceModelListener ((TaskJTreeModel)taskTree.getTreeTableModel ());
		
		/*
		 * registrazione modello tabella come listener sui modelli 
		 */
		_context.getModel ().addTaskTreeModelListener ((ProgressesJTableModel)progressesTable.getModel ());
		_ticPump.addAdvancingTicListener ((ProgressesJTableModel)progressesTable.getModel ());
		_context.getModel ().addWorkAdvanceModelListener ((ProgressesJTableModel)progressesTable.getModel ());

		
//		this._context.getUIPersisteer ().register (new MainWindow.PersistencePanelAdapter (this.taskTreePanel, "taskTreePanel"));
		
		/*
		 * registra l'albero e la tabella per la gestione del cut&paste dal menu e toolbar
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
//		progressesTable.addHighlighter (AlternateRowHighlighter.beige);
		progressesTable.addHighlighter (new AlternateRowHighlighter(Color.white, new Color(255, 252, 180), null, true));
//		taskTree.addHighlighter (AlternateRowHighlighter.quickSilver);
		
		/*
		 * Imposta l'ordinamento crescente come default per la colonna di inizio nella tabella degliavanzamenti
		 */
		progressesTable.setSortOrder (progressesTable.convertColumnIndexToView (START_COL_INDEX), SortOrder.ASCENDING);
		
		progressesTable.getColumnExt (progressesTable.convertColumnIndexToView (DURATION_COL_INDEX)).setComparator (new Comparator () {
			public int compare (Object o1, Object o2) {
				return Double.compare ((double)((Progress)o1).getDuration ().getTime (), (double)((Progress)o2).getDuration ().getTime ());
			}
			public boolean equals (Object obj) {
				return super.equals (obj);
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
		
		/*
		 * @workaround
		 * garantisce che la variazione di selezione porti il focus sull'albero, altrimenti serve un ulteriore click perche' il focus arrivi
		 * e siano quindi abilitati il Dnd e cut&paste
		 */
		taskTree.addTreeSelectionListener (new TreeSelectionListener () {
			public void valueChanged (TreeSelectionEvent e) {
				if (e.getPath ()!=null) {
					taskTree.requestFocusInWindow ();
				}
			}
		});
		
		
		
		
		taskTree.addTreeSelectionListener (new TreeSelectionListener () {
			public void valueChanged (TreeSelectionEvent e) {
				final TreePath path = e.getPath ();
				ProgressItem chartRoot = null;
				if (path==null) {
					chartRoot = null;
				} else {
					final Object o = path.getLastPathComponent ();
					if (o instanceof ProgressItem){
						chartRoot = (ProgressItem)o;
					}
				}
				
				tbp.updateProgressTableBackground (chartRoot);
			}
		});
		
		
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
					appStatusLabel.setText (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("MainWindow/StatusBar/StatusLabel/IDLE"));
				} else {
					appStatusLabel.setText (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("MainWindow/StatusBar/StatusLabel/RUNNING"));
				}
			}
		});
		
		taskTree.setDefaultRenderer (TaskTreeDuration.class, new DefaultTableCellRenderer () {
			private Font _originalFont;
			private Font _boldFont;
			
			public Component getTableCellRendererComponent (final JTable table, final Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {
				
				final JLabel res = (JLabel)super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column);
				final TaskTreeDuration tduration = (TaskTreeDuration)value;
				final Duration duration = tduration.getDuration ();
				final Task t = tduration.getTask ();
				
				
				if (null==_originalFont) {
					_originalFont = res.getFont ();
				}
				if (null==_boldFont) {
					_boldFont = _originalFont.deriveFont (Font.BOLD);
				}
				
				boolean progressing = false;
				
				for (final TaskTreePath p : _progressingPaths) {
					if (p.contains (t)) {
						progressing = true;
						break;
					}
				}
				if (progressing) {
					res.setFont (_boldFont);
				} else {
					res.setFont (_originalFont);
				}
				
				if (duration.getTime ()==0){
					res.setText ("");
				} else {
					res.setText (DurationUtils.format (duration));
				}
				
				setHorizontalAlignment (TRAILING);
				return res;
			}
			
		});

		/*
		 * toggle sort
		 * 1 asc, 2 desc, 3 null
		 */
		progressesTable.setFilters (new FilterPipeline () {
			protected SortController createDefaultSortController () {
				return new SorterBasedSortController () {
					
					public void toggleSortOrder (int column, Comparator comparator) {
						Sorter currentSorter = getSorter ();
						if ((currentSorter != null)
						&& (currentSorter.getColumnIndex () == column)
						&& !currentSorter.isAscending ()) {
							setSorter (null);
						} else {
							super.toggleSortOrder (column, comparator);
						}
					}
					
				};
			}
		});
		
		progressesTable.getModel ().addTableModelListener (new TableModelListener () {
			public void tableChanged (TableModelEvent e) {
				if (e.getColumn ()==TableModelEvent.ALL_COLUMNS && e.getLastRow ()==Integer.MAX_VALUE) {
					/* denota un ricaricamento totale */
					/*
					 * @workaround commentato il packall, altrimenti provoca un accesso al modello non valido (in caso di eliminazione chiede righe non piu' esistenti, anche se il modello sembra coerente. Cache interna?)
					 */
//					progressesTable.packAll ();
				}
			}
		});
		
		
		/*
		 * forza il focus sull'albero in caso di variazione del workspace
		 */
		this._context.getModel ().addPropertyChangeListener (new PropertyChangeListener (){
			public void propertyChange (PropertyChangeEvent e){
				final String pName = e.getPropertyName ();
				if (pName.equals ("name")){
					try {
						taskTree.requestFocusInWindow ();
						taskTree.getSelectionModel ().setSelectionInterval (0, 0);
					} catch (Exception ex) {
						/*
						 * Se non dovesse funzionare, non deve bloccare il thread AWT
						 */
						System.out.println (ExceptionUtils.getStackTrace (ex));
					}
				}
			}
		}
		);
		
		/**
		 * Aggiunge azioni mappate con acceleratori all'albero ed alla tabella degli avanzamenti
		 */
		final ActionMap taskTreeActionMap = new ActionMap ();
		taskTreeActionMap.setParent (taskTree.getActionMap ());
		taskTreeActionMap.put ("delete", new DeleteTasksAction ());
		taskTreeActionMap.put ("startProgress", new StartProgressAction ());
		taskTreeActionMap.put ("stopProgress", new StopProgressAction ());
		taskTree.setActionMap (taskTreeActionMap);
		
		final ActionMap progressesTableActionMap = new ActionMap ();
		progressesTableActionMap.setParent (progressesTable.getActionMap ());
		progressesTableActionMap.put ("delete", new DeletePiecesOfWorkAction ());
		progressesTableActionMap.put ("startProgress", new StartProgressAction ());
		progressesTableActionMap.put ("stopProgress", new StopProgressAction ());
		progressesTable.setActionMap (progressesTableActionMap);
		
		taskTree.getTreeTableModel ().addTreeModelListener (new TreeModelListener () {
			public void treeNodesChanged (TreeModelEvent e) {
			}
			public void treeNodesInserted (TreeModelEvent e) {
			}
			public void treeNodesRemoved (TreeModelEvent e) {
				/*
				 * @workaround
				 * forza il repaint per evitare la mancata rimozione (a video) dell'ultima foglia dell'albero
				 */
				taskTree.repaint ();
			}
			public void treeStructureChanged (TreeModelEvent e) {
			}
		});
		
		_context.getModel ().addTaskTreeModelListener (new TaskTreeModelListener () {
			public void treeNodesChanged (TaskTreeModelEvent e) {
				invokePackAll (progressesTable);
			}
			public void treeNodesInserted (TaskTreeModelEvent e) {
				invokePackAll (progressesTable);
			}
			public void treeNodesRemoved (TaskTreeModelEvent e) {
				invokePackAll (progressesTable);
			}
			public void treeStructureChanged (TaskTreeModelEvent e) {
				invokePackAll (progressesTable);
			}
			public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
				invokePackAll (progressesTable);
			}
			
		});
		invokePackAll (taskTree);
		
		
		/**
		 * consente di disegnare in grassetto il percorsoinprogress
		 */
		_context.getModel ().addWorkAdvanceModelListener (new WorkAdvanceModelListener () {
			
			private void setPaths () {
				synchronized (_progressingPaths) {
					_progressingPaths.clear ();
					for (final PieceOfWork pow : _context.getModel ().getAdvancing ()) {
						_progressingPaths.add (new TaskTreePath (_context.getModel ().getWorkSpace (), pow.getTask ()));
					}
				}
			}
			public void elementsInserted (WorkAdvanceModelEvent e) {
				setPaths ();
				taskTree.repaint ();
			}
			public void elementsRemoved (WorkAdvanceModelEvent e) {
				setPaths ();
				taskTree.repaint ();
			}
		});
		
		
		mainContentsTabbedPane.setIconAt (0, 
			new CompositeIcon (
				new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/view_text.png")),
				new VTextIcon(mainContentsTabbedPane, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("MainWindow/MainContentsTabbedPane/TabTitle/Actions"), VTextIcon.ROTATE_DEFAULT)
				)
			);
		
		mainContentsTabbedPane.setIconAt (1, 
			new CompositeIcon (
				new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/colors.png")),
				new VTextIcon(mainContentsTabbedPane, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("MainWindow/MainContentsTabbedPane/TabTitle/Charts"), VTextIcon.ROTATE_DEFAULT)
				)
			);
			
		
		_context.getUIPersisteer ().register (new MainWindow.PersistencePanelAdapter (taskTreePanel, "taskTreePanel"));
		
		_context.getUIPersisteer ().register (new PersistentComponent () {
			public String getPersistenceKey () {
				return "task.tree.visible.columns";
			}

			public void makePersistent (PersistenceStorage props) {
				final List<String> s = new ArrayList<String> ();
				
				for (int i = 0; i< taskTree.getColumnCount (false); i++) {
					final TableColumnExt tce = taskTree.getColumnExt (i);
					if (tce.isVisible ()) {
						s.add (Integer.toString (taskTree.convertColumnIndexToModel (i)));
					}
				}
				SettingsSupport.setStringProperty (props.getRegistry (), getPersistenceKey (), StringUtils.toCSV (s.toArray ()));
			}
		

			public boolean restorePersistent (PersistenceStorage props) {
				final String s = SettingsSupport.getStringProperty (props.getRegistry (), getPersistenceKey ());
				if (s==null) {
					return false;
				}
//				final String[] values = s.split (";");
				final Set<String> values = new HashSet<String> (Arrays.asList (s.split (",")));
				for (int i = taskTree.getColumnCount (false)-1; i>=0; i--) {
					final TableColumnExt tce = taskTree.getColumnExt (i);
					tce.setVisible (values.contains (Integer.toString (taskTree.convertColumnIndexToModel (i))));
				}
				return true;
			}
		});
		
		((RingChartPanel)chartPanel).addRingChartRootChangeListener (new PropertyChangeListener () {
			public void propertyChange (PropertyChangeEvent evt) {
				final Task t = (Task)evt.getNewValue ();
				final TreePath p = taskTree.getTreeSelectionModel ().getSelectionPath ();
				final TaskTreePath ttp = new TaskTreePath (_context.getModel ().getWorkSpace (), t);
				final TreePath p1 = toTreePath (ttp);
				if (!p1.equals (p)) {
					/*
					 * varia selezione albero
					 */
					taskTree.expandPath (p1);
					taskTree.getTreeSelectionModel ().setSelectionPath (p1);
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
        treePopupMenu = new javax.swing.JPopupMenu();
        newTaskPopupItem = new javax.swing.JMenuItem();
        newProgressPopupItem = new javax.swing.JMenuItem();
        startProgressPopupItem = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JSeparator();
        expandPopupItem = new javax.swing.JMenuItem();
        collapsePopupItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        cutTasksMenuItem = new javax.swing.JMenuItem();
        copyTasksMenuItem = new javax.swing.JMenuItem();
        pasteTasksMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        renameTaskPopupItem = new javax.swing.JMenuItem();
        deleteTaskPopupItem = new javax.swing.JMenuItem();
        tablePopupMenu = new javax.swing.JPopupMenu();
        newProgressPopupItem1 = new javax.swing.JMenuItem();
        startProgressPopupItem1 = new javax.swing.JMenuItem();
        startProgressClonePopupItem = new javax.swing.JMenuItem();
        continueProgressPopupItem = new javax.swing.JMenuItem();
        stopProgressMenuItem1 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        cutProgressesMenuItem1 = new javax.swing.JMenuItem();
        copyProgressesMenuItem1 = new javax.swing.JMenuItem();
        pasteProgressesMenuItem1 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        deleteProgressPopupItem = new javax.swing.JMenuItem();
        openFileChooser = new javax.swing.JFileChooser();
        saveFileChooser = new javax.swing.JFileChooser();
        xmlFileChooser = new javax.swing.JFileChooser();
        TabShowingChoiceButtonGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        tree_table_splitPane = new javax.swing.JSplitPane();
        taskTreePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taskTree = new org.jdesktop.swingx.JXTreeTable();
        jPanel3 = new javax.swing.JPanel();
        mainContentsTabbedPane = new javax.swing.JTabbedPane();
        progressesTablePanel = new javax.swing.JPanel();
        progressesTableScrollPane = new javax.swing.JScrollPane();
        progressesTable = new JXTable (){

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer( renderer, row, column);
                // We want renderer component to be transparent
                // so background image is visible
                if( c instanceof JComponent ) {
                    //((JComponent)c).setOpaque(false);
                    final Color oldColor = c.getBackground ();
                    if (oldColor!=null) {
                        c.setBackground (ColorUtil.setAlpha (oldColor, 150));
                    }
                }
                return c;
            }

            public void paint( Graphics g ) {
                // First draw the background image - tiled
                final Dimension d = getSize();

                g.drawImage( ptbImage.getImage(), (d.width - ptbImageWidth)/2, (d.height - ptbImageHeight)/2, null, null );
                // Now let the regular paint code do it's work
                super.paint(g);
            }

        };
        jPanel1 = new javax.swing.JPanel();
        chartPanel = new RingChartPanel ("");
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        chartDepthSlider = new javax.swing.JSlider();
        jPanel5 = new javax.swing.JPanel();
        mainToolbar = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JSeparator();
        mainToolbar1 = new javax.swing.JToolBar();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        undoButton = new javax.swing.JButton();
        redoButton = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JSeparator();
        mainToolbar2 = new javax.swing.JToolBar();
        printButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        appStatusLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tableTaskNameLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tableProgressCountLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newWorkSpaceMenuItem = new javax.swing.JMenuItem();
        newTaskMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        openWorkSpaceMenuItem = new javax.swing.JMenuItem();
        exportWorkSpaceMenuItem = new javax.swing.JMenuItem();
        importWorkSpaceMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        printMenuItem = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JSeparator();
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
        jSeparator8 = new javax.swing.JSeparator();
        getActionByName(DefaultEditorKit.deleteNextCharAction).putValue (Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        showActionsMenuItem = new javax.swing.JRadioButtonMenuItem();
        showChartsMenuItem = new javax.swing.JRadioButtonMenuItem();
        jSeparator16 = new javax.swing.JSeparator();
        linkTableBackgroundMenuItem = new javax.swing.JCheckBoxMenuItem();
        actionstMenu = new javax.swing.JMenu();
        newProgressMenuItem = new javax.swing.JMenuItem();
        startProgressMenuItem = new javax.swing.JMenuItem();
        startProgressCloneMenuItem = new javax.swing.JMenuItem();
        continueProgressMenuItem = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JSeparator();
        stopProgressMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        logConsoleMenuItem = new javax.swing.JMenuItem();
        optionsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        contextHelpMenuItem = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        aboutMenuItem = new javax.swing.JMenuItem();

        treePopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        treePopupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                treePopupMenuPopupMenuWillBecomeVisible(evt);
            }
        });

        newTaskPopupItem.setAction(new NewTaskAction ());
        newTaskPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        newTaskPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/file-new.png")));
        newTaskPopupItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_task"));
        newTaskPopupItem.setActionCommand("newTask");
        newTaskPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTaskPopupItemActionPerformed(evt);
            }
        });

        treePopupMenu.add(newTaskPopupItem);

        newProgressPopupItem.setAction(new NewPieceOfWorkAction ());
        newProgressPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        newProgressPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/add.png")));
        org.openide.awt.Mnemonics.setLocalizedText(newProgressPopupItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_action"));
        newProgressPopupItem.setActionCommand("newProgress");
        newProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProgressPopupItemActionPerformed(evt);
            }
        });

        treePopupMenu.add(newProgressPopupItem);

        startProgressPopupItem.setAction(new StartProgressAction ());
        startProgressPopupItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        startProgressPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        startProgressPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/media-record.png")));
        org.openide.awt.Mnemonics.setLocalizedText(startProgressPopupItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Start_action"));
        startProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startProgressPopupItemActionPerformed(evt);
            }
        });

        treePopupMenu.add(startProgressPopupItem);

        treePopupMenu.add(jSeparator12);

        expandPopupItem.setAction(new ExpandTreeAction ());
        expandPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        expandPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/transparent.png")));
        expandPopupItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Expand"));
        expandPopupItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Expands_subtree"));
        treePopupMenu.add(expandPopupItem);

        collapsePopupItem.setAction(new CollapseTreeAction ());
        collapsePopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        collapsePopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/transparent.png")));
        collapsePopupItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Collapse"));
        treePopupMenu.add(collapsePopupItem);

        treePopupMenu.add(jSeparator6);

        cutTasksMenuItem.setAction(new TransferAction (TransferAction.Type.CUT, tal));
        cutTasksMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutTasksMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        cutTasksMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-cut.png")));
        cutTasksMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Cut"));
        treePopupMenu.add(cutTasksMenuItem);

        copyTasksMenuItem.setAction(new TransferAction (TransferAction.Type.COPY, tal));
        copyTasksMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyTasksMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        copyTasksMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-copy.png")));
        copyTasksMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Copy"));
        treePopupMenu.add(copyTasksMenuItem);

        pasteTasksMenuItem.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
        pasteTasksMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteTasksMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
        pasteTasksMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-paste.png")));
        pasteTasksMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Paste"));
        treePopupMenu.add(pasteTasksMenuItem);

        treePopupMenu.add(jSeparator1);

        renameTaskPopupItem.setAction(new AbstractAction () {
            public void actionPerformed (ActionEvent e) {
                final int row = taskTree.getSelectedRow ();
                taskTree.editCellAt (row, TaskJTreeModel.TREE_COLUMN_INDEX);
                if (taskTree.getCellEditor () instanceof TreeTableCellEditor) {
                    final JTextField editor = (JTextField)taskTree.getCellEditor ().getTableCellEditorComponent (taskTree, taskTree.getModel ().getValueAt (row, TaskJTreeModel.TREE_COLUMN_INDEX), true, row, TaskJTreeModel.TREE_COLUMN_INDEX);
                    editor.requestFocusInWindow ();
                    editor.selectAll ();
                }

            }
        });
        renameTaskPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        renameTaskPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/transparent.png")));
        renameTaskPopupItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Rename"));
        renameTaskPopupItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Rename_selected_task"));
        renameTaskPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renameTaskPopupItemActionPerformed(evt);
            }
        });

        treePopupMenu.add(renameTaskPopupItem);

        deleteTaskPopupItem.setAction(new DeleteTasksAction ());
        deleteTaskPopupItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteTaskPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteTaskPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-delete.png")));
        deleteTaskPopupItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Delete"));
        deleteTaskPopupItem.setActionCommand("deleteTask");
        treePopupMenu.add(deleteTaskPopupItem);

        tablePopupMenu.setFont(new java.awt.Font("Dialog", 0, 12));
        tablePopupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                tablePopupMenuPopupMenuWillBecomeVisible(evt);
            }
        });

        newProgressPopupItem1.setAction(new NewPieceOfWorkAction ());
        newProgressPopupItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        newProgressPopupItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/add.png")));
        org.openide.awt.Mnemonics.setLocalizedText(newProgressPopupItem1, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_action"));
        newProgressPopupItem1.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Creates_a_new_action"));
        newProgressPopupItem1.setActionCommand("newProgress");
        newProgressPopupItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newProgressPopupItem1ActionPerformed(evt);
            }
        });

        tablePopupMenu.add(newProgressPopupItem1);

        startProgressPopupItem1.setAction(new StartProgressAction ());
        startProgressPopupItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        startProgressPopupItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        startProgressPopupItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/media-record.png")));
        org.openide.awt.Mnemonics.setLocalizedText(startProgressPopupItem1, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Start_action"));
        startProgressPopupItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startProgressPopupItem1ActionPerformed(evt);
            }
        });

        tablePopupMenu.add(startProgressPopupItem1);

        startProgressClonePopupItem.setAction(new StartProgressCloneAction ());
        startProgressClonePopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        startProgressClonePopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/transparent.png")));
        org.openide.awt.Mnemonics.setLocalizedText(startProgressClonePopupItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Start_action_clone"));
        startProgressClonePopupItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Start_a_new_action_having_the_same_description_of_the_selected_one"));
        startProgressClonePopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startProgressClonePopupItemActionPerformed(evt);
            }
        });

        tablePopupMenu.add(startProgressClonePopupItem);

        continueProgressPopupItem.setAction(new ContinueProgressAction ());
        continueProgressPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        continueProgressPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/transparent.png")));
        org.openide.awt.Mnemonics.setLocalizedText(continueProgressPopupItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Continue_action"));
        continueProgressPopupItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Continue_previously_stopped_action"));
        continueProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueProgressPopupItemActionPerformed(evt);
            }
        });

        tablePopupMenu.add(continueProgressPopupItem);

        stopProgressMenuItem1.setAction(new StopProgressAction ());
        stopProgressMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        stopProgressMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        stopProgressMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/media-stop.png")));
        org.openide.awt.Mnemonics.setLocalizedText(stopProgressMenuItem1, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Stop"));
        stopProgressMenuItem1.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Stop_current_action"));
        stopProgressMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopProgressMenuItem1ActionPerformed(evt);
            }
        });

        tablePopupMenu.add(stopProgressMenuItem1);

        tablePopupMenu.add(jSeparator9);

        cutProgressesMenuItem1.setAction(new TransferAction (TransferAction.Type.CUT, tal));
        cutProgressesMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        cutProgressesMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        cutProgressesMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-cut.png")));
        cutProgressesMenuItem1.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Cut"));
        tablePopupMenu.add(cutProgressesMenuItem1);

        copyProgressesMenuItem1.setAction(new TransferAction (TransferAction.Type.COPY, tal));
        copyProgressesMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyProgressesMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        copyProgressesMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-copy.png")));
        copyProgressesMenuItem1.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Copy"));
        tablePopupMenu.add(copyProgressesMenuItem1);

        pasteProgressesMenuItem1.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
        pasteProgressesMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        pasteProgressesMenuItem1.setFont(new java.awt.Font("Dialog", 0, 12));
        pasteProgressesMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-paste.png")));
        pasteProgressesMenuItem1.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Paste"));
        tablePopupMenu.add(pasteProgressesMenuItem1);

        tablePopupMenu.add(jSeparator10);

        deleteProgressPopupItem.setAction(new DeletePiecesOfWorkAction ());
        deleteProgressPopupItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        deleteProgressPopupItem.setFont(new java.awt.Font("Dialog", 0, 12));
        deleteProgressPopupItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-delete.png")));
        org.openide.awt.Mnemonics.setLocalizedText(deleteProgressPopupItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Delete"));
        deleteProgressPopupItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProgressPopupItemActionPerformed(evt);
            }
        });

        tablePopupMenu.add(deleteProgressPopupItem);

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

        xmlFileChooser.setCurrentDirectory(new File (System.getProperty ("user.dir")));
        xmlFileChooser.setFileHidingEnabled(false);
        xmlFileChooser.addChoosableFileFilter (new CustomFileFilter (
            new String []{FileUtils.xml},
            new String []{"XML files"}
        ));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon (MainWindow.class.getResource ("/com/davidecavestro/timekeeper/gui/images/ktimer-16x16.png")).getImage ());
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
        taskTreePanel.setPreferredSize(new java.awt.Dimension(160, 354));
        taskTree.setAutoStartEditOnKeyStroke(false);
        taskTree.setColumnControlVisible(true);
        taskTree.setDragEnabled(true);
        taskTree.setFont(new java.awt.Font("Monospaced", 0, 12));
        taskTree.setHorizontalScrollEnabled(true);
        taskTree.setMaximumSize(null);
        taskTree.setMinimumSize(null);
        taskTree.setRootVisible(true);
        taskTree.setTreeTableModel(new TaskJTreeModel (_context.getModel ()));
        javax.help.CSH.setHelpIDString (taskTree, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.TASK_TREE ));

        taskTree.setTreeCellRenderer (new TaskTreeCellRenderer ());

        taskTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                taskTreeMousePressed(evt);
            }
        });

        jScrollPane1.setViewportView(taskTree);

        taskTreePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        tree_table_splitPane.setLeftComponent(taskTreePanel);

        jPanel3.setLayout(new java.awt.BorderLayout());

        mainContentsTabbedPane.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        mainContentsTabbedPane.setFont(new java.awt.Font("Dialog", 0, 12));
        mainContentsTabbedPane.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mainContentsTabbedPaneFocusGained(evt);
            }
        });

        progressesTablePanel.setLayout(new java.awt.BorderLayout());

        progressesTablePanel.setBackground(javax.swing.UIManager.getDefaults().getColor("Table.background"));
        progressesTablePanel.setOpaque(false);
        progressesTablePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                progressesTablePanelComponentShown(evt);
            }
        });
        progressesTablePanel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                progressesTablePanelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                progressesTablePanelFocusLost(evt);
            }
        });

        progressesTableScrollPane.setBackground(java.awt.Color.white);
        progressesTableScrollPane.setMaximumSize(null);
        progressesTableScrollPane.setOpaque(false);
        //jScrollPane3.getViewport ().setBackground (javax.swing.UIManager.getDefaults().getColor("Table.background"));
        progressesTableScrollPane.getViewport ().setBackground (Color.WHITE);
        progressesTable.setComponentPopupMenu(tablePopupMenu);
        progressesTable.setModel(new ProgressesJTableModel (_context.getModel ()));
        progressesTable.setAutoStartEditOnKeyStroke(false);
        progressesTable.setColumnControlVisible(true);
        progressesTable.setDragEnabled(true);
        progressesTable.setFont(new java.awt.Font("Monospaced", 0, 12));
        progressesTable.setHorizontalScrollEnabled(true);
        progressesTable.setOpaque(false);
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

                public Component getTableCellRendererComponent (final JTable table, final Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {
                    final Component c = super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);

                    setHorizontalAlignment (TRAILING);

                    return c;
                }
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

                        setHorizontalAlignment (TRAILING);

                        return res;
                    }
                    public void setValue (Object value) {
                        final PieceOfWork progress = (PieceOfWork)value;
                        setText (DurationUtils.formatDuration (progress.getTime ()));
                    }
                }
            );
            progressesTable.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    progressesTableMousePressed(evt);
                }
            });

            progressesTableScrollPane.setViewportView(progressesTable);

            progressesTablePanel.add(progressesTableScrollPane, java.awt.BorderLayout.CENTER);

            mainContentsTabbedPane.addTab(null, progressesTablePanel);

            jPanel1.setLayout(new java.awt.GridBagLayout());

            jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown(java.awt.event.ComponentEvent evt) {
                    jPanel1ComponentShown(evt);
                }
            });
            jPanel1.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    jPanel1FocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    jPanel1FocusLost(evt);
                }
            });

            chartPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(java.awt.event.ComponentEvent evt) {
                    chartPanelComponentResized(evt);
                }
            });

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            jPanel1.add(chartPanel, gridBagConstraints);

            jPanel4.setMinimumSize(new java.awt.Dimension(46, 20));
            jPanel4.setPreferredSize(new java.awt.Dimension(210, 20));
            jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel3.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("MainWindow/ControlLabel/Visible_chart_levels"));
            jPanel4.add(jLabel3);

            chartDepthSlider.setMaximum(10);
            chartDepthSlider.setMinimum(1);
            chartDepthSlider.setValue(5);
            {
                final int v = _context.getApplicationOptions ().getChartDepth ();
                chartDepthSlider.setValue (v);

                ((RingChartPanel)chartPanel).setDepth (v);
            }
            chartDepthSlider.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    chartDepthSliderFocusLost(evt);
                }
            });
            chartDepthSlider.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    chartDepthSliderStateChanged(evt);
                }
            });

            jPanel4.add(chartDepthSlider);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            jPanel1.add(jPanel4, gridBagConstraints);

            mainContentsTabbedPane.addTab(null, jPanel1);

            jPanel3.add(mainContentsTabbedPane, java.awt.BorderLayout.CENTER);

            tree_table_splitPane.setRightComponent(jPanel3);

            mainPanel.add(tree_table_splitPane, java.awt.BorderLayout.CENTER);

            jPanel5.setLayout(new java.awt.GridBagLayout());

            jPanel5.setMinimumSize(new java.awt.Dimension(400, 28));
            jPanel5.setPreferredSize(new java.awt.Dimension(420, 28));
            mainToolbar.setFloatable(false);
            mainToolbar.setRollover(true);
            mainToolbar.setPreferredSize(new java.awt.Dimension(150, 28));
            javax.help.CSH.setHelpIDString (mainToolbar, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            jButton5.setAction(new NewWorkSpaceAction ());
            jButton5.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/folder-new.png")));
            jButton5.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"));
            jButton5.setBorderPainted(false);
            jButton5.setMargin(null);
            jButton5.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton5.setOpaque(false);
            jButton5.setPreferredSize(new java.awt.Dimension(28, 28));
            mainToolbar.add(jButton5);

            jButton1.setAction(new NewTaskAction ());
            jButton1.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/file-new.png")));
            jButton1.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_task_(Ctrl+N)"));
            jButton1.setBorderPainted(false);
            jButton1.setMargin(null);
            jButton1.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton1.setOpaque(false);
            jButton1.setPreferredSize(new java.awt.Dimension(28, 28));
            mainToolbar.add(jButton1);

            jButton2.setAction(new OpenWorkSpaceAction ());
            jButton2.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/folder-open.png")));
            jButton2.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Open_workspace_(Ctrl+O)"));
            jButton2.setBorderPainted(false);
            jButton2.setMargin(null);
            jButton2.setMaximumSize(new java.awt.Dimension(28, 28));
            jButton2.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton2.setOpaque(false);
            jButton2.setPreferredSize(new java.awt.Dimension(28, 28));
            mainToolbar.add(jButton2);

            jSeparator5.setMinimumSize(new java.awt.Dimension(10, 0));
            mainToolbar.add(jSeparator5);

            jButton4.setAction(new NewPieceOfWorkAction ());
            jButton4.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/add.png")));
            jButton4.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_action"));
            jButton4.setBorderPainted(false);
            jButton4.setMargin(null);
            jButton4.setMaximumSize(new java.awt.Dimension(28, 28));
            jButton4.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton4.setOpaque(false);
            jButton4.setPreferredSize(new java.awt.Dimension(28, 28));
            mainToolbar.add(jButton4);

            jButton3.setAction(new StartProgressAction ());
            jButton3.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/media-record.png")));
            jButton3.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("<HTML>_Start_action_(Ctrl+S)_<BR>__<I>hold_SHIFT_key_pressed_for_options_dialog</I>_</HTML>"));
            jButton3.setBorderPainted(false);
            jButton3.setMargin(null);
            jButton3.setMaximumSize(new java.awt.Dimension(28, 28));
            jButton3.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton3.setOpaque(false);
            jButton3.setPreferredSize(new java.awt.Dimension(28, 28));
            mainToolbar.add(jButton3);

            jButton10.setAction(new StopProgressAction ());
            jButton10.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/media-stop.png")));
            jButton10.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Stop_action_(Ctrl+T)"));
            jButton10.setBorderPainted(false);
            jButton10.setMargin(null);
            jButton10.setMaximumSize(new java.awt.Dimension(28, 28));
            jButton10.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton10.setOpaque(false);
            jButton10.setPreferredSize(new java.awt.Dimension(28, 28));
            mainToolbar.add(jButton10);

            jSeparator14.setMinimumSize(new java.awt.Dimension(40, 0));
            mainToolbar.add(jSeparator14);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            jPanel5.add(mainToolbar, gridBagConstraints);

            mainToolbar1.setFloatable(false);
            mainToolbar1.setRollover(true);
            mainToolbar1.setPreferredSize(new java.awt.Dimension(150, 28));
            javax.help.CSH.setHelpIDString (mainToolbar1, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            jButton6.setAction(new TransferAction (TransferAction.Type.CUT, tal));
            jButton6.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/edit-cut.png")));
            jButton6.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Cut_(Ctrl+X)"));
            jButton6.setBorderPainted(false);
            jButton6.setMargin(null);
            jButton6.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton6.setOpaque(false);
            jButton6.setPreferredSize(new java.awt.Dimension(28, 28));
            jButton6.setText (null);
            mainToolbar1.add(jButton6);

            jButton7.setAction(new TransferAction (TransferAction.Type.COPY, tal));
            jButton7.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/edit-copy.png")));
            jButton7.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Copy_(Ctrl+C)"));
            jButton7.setBorderPainted(false);
            jButton7.setMargin(null);
            jButton7.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton7.setOpaque(false);
            jButton7.setPreferredSize(new java.awt.Dimension(28, 28));
            jButton7.setText (null);
            mainToolbar1.add(jButton7);

            jButton8.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
            jButton8.setFont(new java.awt.Font("Dialog", 0, 12));
            jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/edit-paste.png")));
            jButton8.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Paste_(Ctrl+V)"));
            jButton8.setBorderPainted(false);
            jButton8.setMargin(null);
            jButton8.setMinimumSize(new java.awt.Dimension(22, 22));
            jButton8.setOpaque(false);
            jButton8.setPreferredSize(new java.awt.Dimension(28, 28));
            jButton8.setText (null);
            mainToolbar1.add(jButton8);

            undoButton.setAction(_context.getUndoManager ().getUndoAction());
            undoButton.setFont(new java.awt.Font("Dialog", 0, 12));
            undoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/edit-undo.png")));
            undoButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Undo+SHORTCUT"));
            undoButton.setBorderPainted(false);
            undoButton.setMargin(null);
            undoButton.setMaximumSize(new java.awt.Dimension(28, 28));
            undoButton.setMinimumSize(new java.awt.Dimension(22, 22));
            undoButton.setOpaque(false);
            undoButton.setPreferredSize(new java.awt.Dimension(28, 28));
            /* mantiene nascosto il testo  dell'action */
            undoButton.setText (null);
            undoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar1.add(undoButton);

            redoButton.setAction(_context.getUndoManager ().getRedoAction());
            redoButton.setFont(new java.awt.Font("Dialog", 0, 12));
            redoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/edit-redo.png")));
            redoButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Redo+SHORTCUT"));
            redoButton.setBorderPainted(false);
            redoButton.setMargin(null);
            redoButton.setMaximumSize(new java.awt.Dimension(28, 28));
            redoButton.setMinimumSize(new java.awt.Dimension(22, 22));
            redoButton.setOpaque(false);
            redoButton.setPreferredSize(new java.awt.Dimension(28, 28));
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar1.add(redoButton);

            jSeparator15.setMinimumSize(new java.awt.Dimension(40, 0));
            mainToolbar1.add(jSeparator15);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            jPanel5.add(mainToolbar1, gridBagConstraints);

            mainToolbar2.setFloatable(false);
            mainToolbar2.setRollover(true);
            mainToolbar2.setPreferredSize(new java.awt.Dimension(18, 28));
            javax.help.CSH.setHelpIDString (mainToolbar2, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.MAIN_TOOLBAR ));

            _context.getHelpManager ().initialize (helpButton);
            printButton.setAction(new StartReportAction ());
            printButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/document-print.png")));
            printButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Print_(Ctrl+P)"));
            printButton.setBorderPainted(false);
            printButton.setOpaque(false);
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar2.add(printButton);

            _context.getHelpManager ().initialize (helpButton);
            helpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/help-browser.png")));
            helpButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Show_help"));
            helpButton.setBorderPainted(false);
            helpButton.setOpaque(false);
            /* mantiene nascosto il testo  dell'action */
            redoButton.setText (null);
            redoButton.putClientProperty ("hideActionText", Boolean.TRUE);
            mainToolbar2.add(helpButton);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.weightx = 1.0;
            jPanel5.add(mainToolbar2, gridBagConstraints);

            mainPanel.add(jPanel5, java.awt.BorderLayout.NORTH);

            statusPanel.setLayout(new java.awt.GridBagLayout());

            statusPanel.setMinimumSize(new java.awt.Dimension(320, 20));
            statusPanel.setPreferredSize(new java.awt.Dimension(436, 20));
            javax.help.CSH.setHelpIDString (statusPanel, _context.getHelpManager ().getResolver ().resolveHelpID (HelpResources.STATUS_BAR));
            appStatusLabel.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(appStatusLabel, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("IDLE"));
            appStatusLabel.setToolTipText("Running status");
            appStatusLabel.setMinimumSize(new java.awt.Dimension(60, 19));
            appStatusLabel.setPreferredSize(new java.awt.Dimension(50, 19));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.weightx = 0.2;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
            statusPanel.add(appStatusLabel, gridBagConstraints);

            progressBar.setBorder(null);
            progressBar.setBorderPainted(false);
            progressBar.setFocusable(false);
            progressBar.setMinimumSize(new java.awt.Dimension(10, 6));
            progressBar.setPreferredSize(new java.awt.Dimension(50, 14));
            progressBar.setString("");
            progressBar.setVerifyInputWhenFocusTarget(false);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            statusPanel.add(progressBar, gridBagConstraints);

            jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            jPanel2.setMinimumSize(new java.awt.Dimension(250, 20));
            jPanel2.setPreferredSize(new java.awt.Dimension(250, 22));
            jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel1.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Task:"));
            jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 0, 60, 20));

            tableTaskNameLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
            jPanel2.add(tableTaskNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 130, 20));

            jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel2.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Actions:"));
            jLabel2.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Progress_count"));
            jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, -1, 20));

            tableProgressCountLabel.setFont(new java.awt.Font("Monospaced", 0, 12));
            jPanel2.add(tableProgressCountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 20, 20));

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
            statusPanel.add(jPanel2, gridBagConstraints);

            mainPanel.add(statusPanel, java.awt.BorderLayout.SOUTH);

            getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

            menuBar.setFont(new java.awt.Font("Dialog", 0, 12));
            org.openide.awt.Mnemonics.setLocalizedText(fileMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&File"));
            fileMenu.setFont(new java.awt.Font("Dialog", 0, 12));
            newWorkSpaceMenuItem.setAction(new NewWorkSpaceAction ());
            newWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            newWorkSpaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/folder-new.png")));
            org.openide.awt.Mnemonics.setLocalizedText(newWorkSpaceMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"));
            newWorkSpaceMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Create_a_new_workspace"));
            newWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    newWorkSpaceMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(newWorkSpaceMenuItem);

            newTaskMenuItem.setAction(new NewTaskAction ());
            newTaskMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            newTaskMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/file-new.png")));
            org.openide.awt.Mnemonics.setLocalizedText(newTaskMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_task"));
            newTaskMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Create_a_new_task"));
            newTaskMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    newTaskMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(newTaskMenuItem);

            fileMenu.add(jSeparator2);

            openWorkSpaceMenuItem.setAction(new OpenWorkSpaceAction ());
            openWorkSpaceMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
            openWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            openWorkSpaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/folder-open.png")));
            org.openide.awt.Mnemonics.setLocalizedText(openWorkSpaceMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Open_workspace"));
            openWorkSpaceMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Use_another_existing_workspace"));
            openWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    openWorkSpaceMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(openWorkSpaceMenuItem);

            exportWorkSpaceMenuItem.setAction(new ExportWorkSpaceAction ());
            exportWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            exportWorkSpaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/fileexport.png")));
            org.openide.awt.Mnemonics.setLocalizedText(exportWorkSpaceMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Export_workspace"));
            exportWorkSpaceMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Export_current_workspace_to_file"));
            exportWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    exportWorkSpaceMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(exportWorkSpaceMenuItem);

            importWorkSpaceMenuItem.setAction(new ImportWorkSpaceAction ());
            importWorkSpaceMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            importWorkSpaceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/fileimport.png")));
            org.openide.awt.Mnemonics.setLocalizedText(importWorkSpaceMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Import_workspace"));
            importWorkSpaceMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Import_a_workspace_from_external_file"));
            importWorkSpaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    importWorkSpaceMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(importWorkSpaceMenuItem);

            fileMenu.add(jSeparator3);

            printMenuItem.setAction(new StartReportAction ());
            printMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
            printMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            printMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/document-print.png")));
            printMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Print"));
            printMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    printMenuItemActionPerformed(evt);
                }
            });

            fileMenu.add(printMenuItem);

            fileMenu.add(jSeparator11);

            exitMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/application-exit.png")));
            org.openide.awt.Mnemonics.setLocalizedText(exitMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("E&xit"));
            exitMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Exit_from_application"));
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
            undoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-undo.png")));
            undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    undoMenuItemActionPerformed(evt);
                }
            });

            editMenu.add(undoMenuItem);

            redoMenuItem.setAction(_context.getUndoManager ().getRedoAction());
            redoMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            redoMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-redo.png")));
            editMenu.add(redoMenuItem);

            editMenu.add(jSeparator4);

            cutMenuItem.setAction(new TransferAction (TransferAction.Type.CUT, tal));
            cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
            cutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            cutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-cut.png")));
            cutMenuItem.setMnemonic('X');
            org.openide.awt.Mnemonics.setLocalizedText(cutMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Cut"));
            editMenu.add(cutMenuItem);

            copyMenuItem.setAction(new TransferAction (TransferAction.Type.COPY, tal));
            copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
            copyMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            copyMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-copy.png")));
            copyMenuItem.setMnemonic('C');
            org.openide.awt.Mnemonics.setLocalizedText(copyMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Copy"));
            copyMenuItem.setToolTipText("");
            editMenu.add(copyMenuItem);

            pasteMenuItem.setAction(new TransferAction (TransferAction.Type.PASTE, tal));
            pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
            pasteMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            pasteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-paste.png")));
            pasteMenuItem.setMnemonic('V');
            org.openide.awt.Mnemonics.setLocalizedText(pasteMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Paste"));
            editMenu.add(pasteMenuItem);

            editMenu.add(jSeparator8);

            deleteMenuItem.setAction(new TransferAction ("delete", tal));
            deleteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
            deleteMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            deleteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/edit-delete.png")));
            org.openide.awt.Mnemonics.setLocalizedText(deleteMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Delete"));
            editMenu.add(deleteMenuItem);

            menuBar.add(editMenu);

            viewMenu.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("/MainMenu/Menu/View"));
            viewMenu.setFont(new java.awt.Font("Dialog", 0, 12));
            TabShowingChoiceButtonGroup.add(showActionsMenuItem);
            showActionsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            showActionsMenuItem.setSelected(true);
            showActionsMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("/MainMenu/MenuItem/Show_actions"));
            showActionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    showActionsMenuItemActionPerformed(evt);
                }
            });

            viewMenu.add(showActionsMenuItem);

            TabShowingChoiceButtonGroup.add(showChartsMenuItem);
            showChartsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            showChartsMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("/MainMenu/MenuItem/Show_charts"));
            showChartsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    showChartsMenuItemActionPerformed(evt);
                }
            });

            viewMenu.add(showChartsMenuItem);

            viewMenu.add(jSeparator16);

            linkTableBackgroundMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            linkTableBackgroundMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("/MainMenu/MenuItem/Show_chats_ghost_on_actions_table"));
            linkTableBackgroundMenuItem.setSelected (_context.getApplicationOptions ().isChartGhostEnabled ());

            linkTableBackgroundMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    linkTableBackgroundMenuItemActionPerformed(evt);
                }
            });

            viewMenu.add(linkTableBackgroundMenuItem);

            menuBar.add(viewMenu);

            org.openide.awt.Mnemonics.setLocalizedText(actionstMenu, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("&Actions"));
            actionstMenu.setFont(new java.awt.Font("Dialog", 0, 12));
            newProgressMenuItem.setAction(new NewPieceOfWorkAction ());
            newProgressMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            newProgressMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/add.png")));
            org.openide.awt.Mnemonics.setLocalizedText(newProgressMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_action"));
            newProgressMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Creates_a_new_action"));
            newProgressMenuItem.setActionCommand("newProgress");
            newProgressMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    newProgressMenuItemActionPerformed(evt);
                }
            });

            actionstMenu.add(newProgressMenuItem);

            startProgressMenuItem.setAction(new StartProgressAction ());
            startProgressMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
            startProgressMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            startProgressMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/media-record.png")));
            org.openide.awt.Mnemonics.setLocalizedText(startProgressMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Start_action"));
            startProgressMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("<HTML>_Start_action_(Ctrl+S)_<BR>__<I>hold_SHIFT_key_pressed_for_options_dialog</I>_</HTML>"));
            startProgressMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startProgressMenuItemActionPerformed(evt);
                }
            });

            actionstMenu.add(startProgressMenuItem);

            startProgressCloneMenuItem.setAction(new StartProgressCloneAction ());
            startProgressCloneMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            startProgressCloneMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/transparent.png")));
            org.openide.awt.Mnemonics.setLocalizedText(startProgressCloneMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Start_action_clone"));
            startProgressCloneMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Start_a_new_action_having_the_same_description_of_the_selected_one"));
            startProgressCloneMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startProgressCloneMenuItemActionPerformed(evt);
                }
            });

            actionstMenu.add(startProgressCloneMenuItem);

            continueProgressMenuItem.setAction(new ContinueProgressAction ());
            continueProgressMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            continueProgressMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/transparent.png")));
            org.openide.awt.Mnemonics.setLocalizedText(continueProgressMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Continue_action"));
            continueProgressMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Continue_previously_stopped_action"));
            continueProgressMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    continueProgressMenuItemActionPerformed(evt);
                }
            });

            actionstMenu.add(continueProgressMenuItem);

            actionstMenu.add(jSeparator13);

            stopProgressMenuItem.setAction(new StopProgressAction ());
            stopProgressMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
            stopProgressMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            stopProgressMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/media-stop.png")));
            org.openide.awt.Mnemonics.setLocalizedText(stopProgressMenuItem, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Stop_action"));
            stopProgressMenuItem.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Stop_current_action"));
            stopProgressMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    stopProgressMenuItemActionPerformed(evt);
                }
            });

            actionstMenu.add(stopProgressMenuItem);

            menuBar.add(actionstMenu);

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
            contentsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/help-browser.png")));
            contentsMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Contents"));
            contentsMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    contentsMenuItemActionPerformed(evt);
                }
            });

            helpMenu.add(contentsMenuItem);

            contextHelpMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            contextHelpMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/contexthelp.png")));
            contextHelpMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Item"));
            contextHelpMenuItem.addActionListener (new CSH.DisplayHelpAfterTracking (_context.getHelpManager ().getMainHelpBroker ()));
            helpMenu.add(contextHelpMenuItem);

            helpMenu.add(jSeparator7);

            aboutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12));
            aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/small/dialog-information.png")));
            aboutMenuItem.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("About"));
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

	private void chartDepthSliderFocusLost (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chartDepthSliderFocusLost
		tbp.repaintProgressTableBackground ();
	}//GEN-LAST:event_chartDepthSliderFocusLost

	private void progressesTablePanelComponentShown (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_progressesTablePanelComponentShown
		showActionsMenuItem.doClick ();
	}//GEN-LAST:event_progressesTablePanelComponentShown

	private void jPanel1ComponentShown (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentShown
		showChartsMenuItem.doClick ();
		tbp.revalidateIfNeeded ();
	}//GEN-LAST:event_jPanel1ComponentShown

	private void jPanel1FocusLost (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusLost
		
	}//GEN-LAST:event_jPanel1FocusLost

	private void progressesTablePanelFocusLost (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_progressesTablePanelFocusLost
		
	}//GEN-LAST:event_progressesTablePanelFocusLost

	private void progressesTablePanelFocusGained (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_progressesTablePanelFocusGained

	}//GEN-LAST:event_progressesTablePanelFocusGained

	private void jPanel1FocusGained (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanel1FocusGained

	}//GEN-LAST:event_jPanel1FocusGained

	private void mainContentsTabbedPaneFocusGained (java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mainContentsTabbedPaneFocusGained

	}//GEN-LAST:event_mainContentsTabbedPaneFocusGained

	private void linkTableBackgroundMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkTableBackgroundMenuItemActionPerformed
		_context.getUserSettings ().setChartGhostEnabled (linkTableBackgroundMenuItem.isSelected ());
		setTableBackoundImage (DEFAULT_PROGRESSES_TABLE_BACKGROUND);
		/*
		 * @workaround chiamata necessaria per ridisegnare lo sfondo della tabella anche quandoil thread dighost non gira (disabilitato)
		 */
		progressesTable.repaint ();
		tbp.repaintProgressTableBackground ();
	}//GEN-LAST:event_linkTableBackgroundMenuItemActionPerformed

	private void showChartsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showChartsMenuItemActionPerformed
		mainContentsTabbedPane.setSelectedIndex (1);
	}//GEN-LAST:event_showChartsMenuItemActionPerformed

	private void showActionsMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActionsMenuItemActionPerformed
		mainContentsTabbedPane.setSelectedIndex (0);
	}//GEN-LAST:event_showActionsMenuItemActionPerformed

	private void chartPanelComponentResized (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_chartPanelComponentResized
		tbp.repaintProgressTableBackground ();
	}//GEN-LAST:event_chartPanelComponentResized

	private void chartDepthSliderStateChanged (javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chartDepthSliderStateChanged
		final int v = chartDepthSlider.getValue ();
		((RingChartPanel)chartPanel).setDepth (v);
		_context.getUserSettings ().setCharDepth (Integer.valueOf (v));
	}//GEN-LAST:event_chartDepthSliderStateChanged

	private void renameTaskPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renameTaskPopupItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_renameTaskPopupItemActionPerformed

	private void stopProgressMenuItem1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopProgressMenuItem1ActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_stopProgressMenuItem1ActionPerformed

	private void stopProgressMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopProgressMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_stopProgressMenuItemActionPerformed

	private void newProgressMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProgressMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_newProgressMenuItemActionPerformed

	private void continueProgressMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueProgressMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_continueProgressMenuItemActionPerformed

	private void startProgressMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressMenuItemActionPerformed

	private void startProgressCloneMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressCloneMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressCloneMenuItemActionPerformed

	private void continueProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueProgressPopupItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_continueProgressPopupItemActionPerformed

	private void startProgressClonePopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressClonePopupItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressClonePopupItemActionPerformed

	private void printMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_printMenuItemActionPerformed
	
	private void startProgressPopupItem1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressPopupItem1ActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressPopupItem1ActionPerformed
	
	private void newProgressPopupItem1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProgressPopupItem1ActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_newProgressPopupItem1ActionPerformed
	
	private void startProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProgressPopupItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_startProgressPopupItemActionPerformed
	
	private void taskTreeMousePressed (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_taskTreeMousePressed
		if (evt.getButton ()!=MouseEvent.BUTTON3) {
			return;
		}
		final int clickedRow = taskTree.getRowForPath (taskTree.getPathForLocation (evt.getX (), evt.getY ()));
		if (clickedRow >=0) {
			final int[] selectedRows = taskTree.getSelectedRows ();
			Arrays.sort (selectedRows);
			if (Arrays.binarySearch (selectedRows, clickedRow)<0) {
				//la riga cliccata non e' attualmente selezionata: seleziona solo quella
				taskTree.getSelectionModel ().clearSelection ();
				taskTree.getSelectionModel ().addSelectionInterval (clickedRow, clickedRow);
			}
		}
		treePopupMenu.show (taskTree, evt.getX (), evt.getY ());
	}//GEN-LAST:event_taskTreeMousePressed
	
	private void exportWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportWorkSpaceMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_exportWorkSpaceMenuItemActionPerformed
	
	private void importWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importWorkSpaceMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_importWorkSpaceMenuItemActionPerformed
	
	private void openWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openWorkSpaceMenuItemActionPerformed
// TODO add your handling code here:
	}//GEN-LAST:event_openWorkSpaceMenuItemActionPerformed
	
	private void progressesTableMousePressed (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_progressesTableMousePressed
//		prepareTablePopupMenu (evt);
	}//GEN-LAST:event_progressesTableMousePressed
	
	private void tablePopupMenuPopupMenuWillBecomeVisible (javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_tablePopupMenuPopupMenuWillBecomeVisible
//		prepareTablePopupMenu (evt);
	}//GEN-LAST:event_tablePopupMenuPopupMenuWillBecomeVisible
	
	private void treePopupMenuPopupMenuWillBecomeVisible (javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_treePopupMenuPopupMenuWillBecomeVisible
		prepareTreePopupMenu (evt);
	}//GEN-LAST:event_treePopupMenuPopupMenuWillBecomeVisible
	
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
		
	private void saveFileChooserActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileChooserActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_saveFileChooserActionPerformed
	
	private void newTaskPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTaskPopupItemActionPerformed
		this._actionNotifier.fireActionPerformed (new ActionEvent (this, -1, "showNewTaskDialog"));
	}//GEN-LAST:event_newTaskPopupItemActionPerformed
	
	private void newWorkSpaceMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newWorkSpaceMenuItemActionPerformed
			}//GEN-LAST:event_newWorkSpaceMenuItemActionPerformed
	
	private void newProgressPopupItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProgressPopupItemActionPerformed
			}//GEN-LAST:event_newProgressPopupItemActionPerformed
	
	private void exitMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
		/*
		 * Questo evento notifica la richiesta di chiusura della finestra.
		 * Il listener registrato da Application dovrebbe provvedere alla chisuura effettiva dell'applicazione.
		 */
		dispatchEvent (new WindowEvent (this, WindowEvent.WINDOW_CLOSING));
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
    private javax.swing.ButtonGroup TabShowingChoiceButtonGroup;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenu actionstMenu;
    private javax.swing.JLabel appStatusLabel;
    private java.awt.BorderLayout borderLayout1;
    private javax.swing.JSlider chartDepthSlider;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JMenuItem collapsePopupItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem contextHelpMenuItem;
    private javax.swing.JMenuItem continueProgressMenuItem;
    private javax.swing.JMenuItem continueProgressPopupItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem copyProgressesMenuItem1;
    private javax.swing.JMenuItem copyTasksMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem cutProgressesMenuItem1;
    private javax.swing.JMenuItem cutTasksMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenuItem deleteProgressPopupItem;
    private javax.swing.JMenuItem deleteTaskPopupItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem expandPopupItem;
    private javax.swing.JMenuItem exportWorkSpaceMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton helpButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importWorkSpaceMenuItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JCheckBoxMenuItem linkTableBackgroundMenuItem;
    private javax.swing.JMenuItem logConsoleMenuItem;
    private javax.swing.JTabbedPane mainContentsTabbedPane;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JToolBar mainToolbar;
    private javax.swing.JToolBar mainToolbar1;
    private javax.swing.JToolBar mainToolbar2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newProgressMenuItem;
    private javax.swing.JMenuItem newProgressPopupItem;
    private javax.swing.JMenuItem newProgressPopupItem1;
    private javax.swing.JMenuItem newTaskMenuItem;
    private javax.swing.JMenuItem newTaskPopupItem;
    private javax.swing.JMenuItem newWorkSpaceMenuItem;
    private javax.swing.JFileChooser openFileChooser;
    private javax.swing.JMenuItem openWorkSpaceMenuItem;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem pasteProgressesMenuItem1;
    private javax.swing.JMenuItem pasteTasksMenuItem;
    private javax.swing.JButton printButton;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JProgressBar progressBar;
    private org.jdesktop.swingx.JXTable progressesTable;
    private javax.swing.JPanel progressesTablePanel;
    private javax.swing.JScrollPane progressesTableScrollPane;
    private javax.swing.JButton redoButton;
    private javax.swing.JMenuItem redoMenuItem;
    private javax.swing.JMenuItem renameTaskPopupItem;
    private javax.swing.JFileChooser saveFileChooser;
    private javax.swing.JRadioButtonMenuItem showActionsMenuItem;
    private javax.swing.JRadioButtonMenuItem showChartsMenuItem;
    private javax.swing.JMenuItem startProgressCloneMenuItem;
    private javax.swing.JMenuItem startProgressClonePopupItem;
    private javax.swing.JMenuItem startProgressMenuItem;
    private javax.swing.JMenuItem startProgressPopupItem;
    private javax.swing.JMenuItem startProgressPopupItem1;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenuItem stopProgressMenuItem;
    private javax.swing.JMenuItem stopProgressMenuItem1;
    private javax.swing.JPopupMenu tablePopupMenu;
    private javax.swing.JLabel tableProgressCountLabel;
    private javax.swing.JLabel tableTaskNameLabel;
    private org.jdesktop.swingx.JXTreeTable taskTree;
    private javax.swing.JPanel taskTreePanel;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JPopupMenu treePopupMenu;
    private javax.swing.JSplitPane tree_table_splitPane;
    private javax.swing.JButton undoButton;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JFileChooser xmlFileChooser;
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
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AI MODELLI
	 **********************************************/
	
	
	
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
		private WorkSpace _workspace;
		private TaskTreePath _masterPath;
		private List<PieceOfWork> _progresses;
		
		
		public ProgressesJTableModel (final TaskTreeModelImpl ttm){
			this._taskTreeModel = ttm;
			reload (null, null);
			fireTableStructureChanged ();
			ttm.addTaskTreeModelListener (this);
			ttm.addWorkAdvanceModelListener (this);
			_ticPump.addAdvancingTicListener (this);
		}
		
		public int getColumnCount () {
			return 4;
		}
		
		public int getRowCount () {
			if (null==_progresses) {
				return 0;
			}
			return _progresses.size ();
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
		
		
		private void reload (WorkSpace workspace, Task newMaster){
			_workspace = workspace;
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
				_masterPath = new TaskTreePath (_workspace, _master);
			}
			cacheAdvancingProgress ();
		}
		
		private final String[] _columnNames = new String[] {
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("table_header/Duration")
			, /*"Task", */
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("table_header/From"),
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("table_header/To"),
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("table_header/Notes")
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
					 * @warning
					 * asimmetria: riceve una Duration, anche se getValue ritorna una PieceOfWork
					 * Cio' e' dovuto al fatto che l'editazione non imposta date di inizio e fine, ma solo una durata
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
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				//nessun effetto in caso di modifica ai task
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (_masterPath==null || !path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				cache ();
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
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
			checkForReload (e);
			e.allow (_treeNodesChangedInspector);
		}
		
		/**
		 * Gestore degli inserimenti nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesInsertedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				//nessun effetto in caso di inserimento task
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (!path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				cache ();
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
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
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
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
						reload (null, null);
						break;
					}
				}
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				if (!path.equals (_masterPath)) {
					/*
					 * Evento esterno al percorso di interesse.
					 */
					return null;
				}
				cache ();
				if (childIndices.length==0) {
					/*
					 * Modifica estesa agli avanzamenti del percorso
					 */
					fireTableDataChanged ();
				} else {
					/*
					 * Inserimenti puntuali di avanzamenti nel percorso
					 */
//					for (int i=0;i<childIndices.length;i++){
//						fireTableRowsDeleted (childIndices[i], childIndices[i]);
//					}
					/*
					 * @todo ottimizzare con codice riportato sopra
					 * al momento tale codice comporta problemi dato che esso e' pensato per rimozioni contigue
					 */
					fireTableDataChanged ();
				}
				return null;
			}
		};
		
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			e.allow (_treeNodesRemovedInspector);
		}
		
		public void treeStructureChanged (TaskTreeModelEvent e) {
//			System.out.println ("table treeStructureChanged");
			checkForReload (e);
			if (_masterPath==null) {
				return;
			}
//			if (e.getWorkSpace ()!=)
			if (e.getPath ().contains (_masterPath)) {
				cache ();
				fireTableDataChanged ();
			}
		}
		
		private void checkForReload (TaskTreeModelEvent e) {
			/*
			 * l'evento da un workspace diversoviene consideratocome un comando di reinizializzazione su tale workspace
			 */
			if (e.getWorkSpace ()!=_workspace) {
				final WorkSpace ws = e.getWorkSpace ();
				if (ws!=null) {
					reload (ws, ws.getRoot ());
//					System.out.println ("reloading table for "+ws);
				} else {
					reload (null, null);
//					System.out.println ("reloading table for null");
				}
			}
		}
		
		public void valueChanged (TreeSelectionEvent e) {
			final int r = taskTree.getSelectedRow ();
			if (r<0) {
				reload (null, null);
			} else {
				reload (_context.getModel ().getWorkSpace (), (Task)taskTree.getModel ().getValueAt (taskTree.convertRowIndexToModel (r), taskTree.convertColumnIndexToModel (TaskJTreeModel.TREE_COLUMN_INDEX)));
			}
			
//			final TreePath path = e.getPath ();
//			if (path==null) {
//				return;
//			}
//			final Object o = path.getLastPathComponent ();
//			if (!e.isAddedPath ()) {
//				reload (null, null);
//			} else if (o instanceof Task){
//				reload (_context.getModel ().getWorkSpace (), (Task)o);
//			}
		}
		
		public void tableChanged (TableModelEvent e) {
			tableTaskNameLabel.setText (_master==null?"":_master.getName ());
			tableTaskNameLabel.setToolTipText (_master==null?"":_master.getName ());
			tableProgressCountLabel.setText (Integer.toString (progressesTable.getModel ().getRowCount ()));
		}
		public void advanceTic (AdvancingTicEvent e) {
			final int l = advancingIdx.length;
			for (int i = 0; i < l; i++) {
				final int idx = advancingIdx[i];
				SwingUtilities.invokeLater (new Runnable () {
					public void run () {
						/*
						 * @workaround invoca in modo asincrono la notifica della variazione nella tabella, sullo stesso thread degli eventi AWT.
						 * questo dovrebbe evitare i problemi di accesso concorrente in JDORI, finc&egrave; l'accesso a JDO avviene sul thread AWT!!!
						 */
						fireTableCellUpdated (idx, DURATION_COL_INDEX);
					}
				});
//				fireTableCellUpdated (idx, DURATION_COL_INDEX);
			}
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

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}
	}
	
	
	/**
	 * Modello dell'albero degli avanzamenti.
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
		}
		
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
		
		public Object getChild (Object parent, int index) {
			return ((Task)parent).childAt (index);
		}
		
		public int getChildCount (Object parent) {
			return((Task)parent).childCount ();
		}
		
		public int getColumnCount () {
			return 3;
		}
		
		private final Class[] _columnClasses = new Class[] {
			Object.class,
			TaskTreeDuration.class,
			TaskTreeDuration.class
		};
		
		public Class getColumnClass (int col) {
			if (col==0) {
				return super.getColumnClass (col);
			}
			return _columnClasses[col];
		}
		
		private String[] columnNames = new String[] {
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("Task"), 
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("Today"), 
			java.util.ResourceBundle.getBundle ("com.davidecavestro.timekeeper.gui.res").getString ("Total")
		};
		public String getColumnName (int column) {
			return columnNames[column];
		}
		
		
		public void propertyChange (PropertyChangeEvent evt){
		}
		
		
    /*
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path the path to the root node
     * @see EventListenerList
     */
    private void fireTreeStructureChanged(Object source, TreePath path) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }
	
	
		
		private void checkForReload (TaskTreeModelEvent e) {
			final WorkSpace ws = e.getWorkSpace ();
			
			
			if (ws!=null && getRoot ()!=ws.getRoot ()) {
			/*
			 *workspace cambiato
			 */
				Task oldRoot = (Task)getRoot ();
				Task newRoot = (Task)ws.getRoot ();
				this.root = newRoot;
				if (newRoot == null && oldRoot != null) {
					fireTreeStructureChanged (this, null);
				} else {
					final TaskTreePath path = new TaskTreePath (ws, newRoot);
					try {
					fireTreeStructureChanged (this, toObjectArray (path), null, null);
					} catch (final NullPointerException npe) {
						/*
						 * @workaround saltuariamente (la frequenza varia da sistema a sistema) in fase di avvio 
						 * viene sollevata un'accezione al momento inspiegabile che va silenziata.
						 * Appena possibile bisogna rimuovere questo catch che potrebbe fermare altre eccezioni. NullPointerException e' moooolto generica.
						 * L'eccezione sembra dovuta ad una parziale inizializzazione del TreeUI
						 *
						 *
Exception in thread "main" java.lang.NullPointerException
        at
javax.swing.plaf.basic.BasicTreeUI.getLeadSelectionPath(BasicTreeUI.java:2358)
        at
javax.swing.plaf.basic.BasicTreeUI.updateLeadRow(BasicTreeUI.java:2362)
        at
javax.swing.plaf.basic.BasicTreeUI.access$1400(BasicTreeUI.java:46)
        at
javax.swing.plaf.basic.BasicTreeUI$Handler.treeStructureChanged(BasicTreeUI.java:3723)
        at
org.jdesktop.swingx.treetable.AbstractTreeTableModel.fireTreeStructureChanged(AbstractTreeTableModel.java:315)
        at
com.davidecavestro.timekeeper.gui.MainWindow$TaskJTreeModel.checkForReload(MainWindow.java:2559)

						 */
						System.err.println ("Exception detected on reloading workspace");
						npe.printStackTrace (System.err);
					}
				}
			}
		}
		/**
		 * Gestore delle modifiche nell'albero di modello principale.
		 */
		private TaskTreeModelEvent.Inspector _treeNodesChangedInspector = new TaskTreeModelEvent.Inspector (){
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesChanged (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
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
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesInserted (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				SwingUtilities.invokeLater (new Runnable () {
					public void run () {
				/*
				 * @workaround per evitare problemi in casodi passaggio difont da plain a bold (puntini)
				 */
						taskTree.updateUI ();
					}
				});
				
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
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
				fireTreeNodesRemoved (source, toObjectArray (path), childIndices, children);
				return null;
			}
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				SwingUtilities.invokeLater (new Runnable () {
					public void run () {
				/*
				 * @workaround per evitare problemi in casodi passaggio difont da plain a bold (puntini)
				 */
						taskTree.updateUI ();
					}
				});
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
			
			public Object inspectTASK (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, Task[] children){
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
					fireTreeStructureChanged (source, toObjectArray (path), null, null);
				}
				return null;
			}
			
			public Object inspectPROGRESS (Object source, WorkSpace workspace, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses){
				final AbstractTableModel m = (AbstractTableModel)taskTree.getModel ();
				/*
				 * Evento modifica colonne durata
				 */
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
				m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
				return null;
			}
		};
		
		public void treeStructureChanged (TaskTreeModelEvent e) {
//			System.out.println ("tree treeStructureChanged");
			checkForReload (e);
			e.allow (_treeStructureChangedInspector);
		}
		
		final TaskTreeDuration _rubberStampDuration = new TaskTreeDuration ();
		
		public Object getValueAt (Object object, int i) {
			switch (i) {
				//colonna  albero
				case TREE_COLUMN_INDEX: {
					/*
					 * Ritorna direttamente il task
					 */
					return object;
				}
				//colonna durata giornaliera
				case TODAY_COLUMN_INDEX: /*@todo ottimizzare!*/ {
					_todayPeriod.reset ();
					final Task t = (Task) object;
					final List progresses = t.getSubtreeProgresses ();
					for (final Iterator it = progresses.iterator ();it.hasNext ();){
						final Progress p = (Progress)it.next ();
						_todayPeriod.computeProgress (p);
					}
					_rubberStampDuration.setTask (t);
					_rubberStampDuration.setDuration (_todayPeriod.getTodayAmount ());
					return _rubberStampDuration;
				}
				
				//colonna durata totale
				case GLOBAL_COLUMN_INDEX: {
					long totalDuration = 0;
					final Task t = (Task)object;
					for (final Iterator it = t.getSubtreeProgresses ().iterator ();it.hasNext ();){
						final Progress p = (Progress)it.next ();
						totalDuration += p.getDuration ().getTime ();
					}
					
					_rubberStampDuration.setTask (t);
					_rubberStampDuration.setDuration (new Duration (totalDuration));
					return _rubberStampDuration;
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
			m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), TODAY_COLUMN_INDEX));
			m.fireTableChanged (new TableModelEvent (m, 0, m.getRowCount (), GLOBAL_COLUMN_INDEX));
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
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
				if (intersection!=null) {
					this._millisecs += intersection.getDuration ().getTime ();
				}
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
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AI MODELLI
	 **********************************************/
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AI METODI DI SERVIZIO
	 **********************************************/
	
	
	/**
	 * Ritorna il modello dell'albero delle attivita'.
	 */
	TreeTableModel getTaskTreeTableModel () {
		return taskTree.getTreeTableModel ();
	}

	private void forceUpperExpansion (final TreePath tp) {
		if (!taskTree.isExpanded (tp)) {
			final TreePath parentPath = tp.getParentPath ();
			if (parentPath!=null) {
				/*
				 * Ricorsione su percorso superire.
				 */
				forceUpperExpansion (parentPath);
			}
			taskTree.expandPath (tp);
		}
	}
	
	private void deepExpand (final WorkSpace ws, final Task t) {
		deepExpand (ws, t, true);
	}
	private void deepExpand (final WorkSpace ws, final Task t, final boolean forceUpperExpansion) {
		final TreePath tp = toTreePath (new TaskTreePath (ws, t));
		if (forceUpperExpansion) {
			forceUpperExpansion (tp);
		}
		taskTree.expandPath (tp);
		for (final Iterator i = t.getChildren ().iterator (); i.hasNext ();) {
			final Task c = (Task) i.next ();
			deepExpand (ws, c, false);
		}
	}
	
	
	
//	/**
//	 * Elimina un avanzamento dal modello.
//	 */
//	private void deleteProgresses (List<PieceOfWork> p){
//		if (p.isEmpty ()) {
//			return;
//		}
//		this._context.getModel ().removePiecesOfWork (p.get (0).getTask (), p);
//	}
	
	/**
	 * Elimina un insieme di task dal modello.
	 */
	private void deleteTasks (List<Task> t){
		if (t.isEmpty ()) {
			return;
		}
		if (JOptionPane.showConfirmDialog (
			this,
			StringUtils.toStringArray (
			"Removing a task you remove all sub-tasks and actions. Please confirm if you want to continue."
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
	 * Notifica l'applicazione l'esecuzione di lavori lunghi, mostrando la barra di progressione.
	 * Solo se il task dura + di 0.2 secs
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
	
	private void invokePackAll (final JXTable t) {
		SwingUtilities.invokeLater (new Runnable () {
			public void run () {
				/*
				 * @workaround invoca in modo asincrono il packall.
				 * questo dovrebbe evitare i problemi di accesso concorrente in JDORI, finc&egrave; l'accesso a JDO avviene sul thread AWT!!!
				 */
				t.packAll ();
			}
		});
	}
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AI METODI DI SERVIZIO
	 **********************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AGLI EDITOR
	 **********************************************/
	
	/**
	 * Editor per le date.
	 */
	private class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
		final JFormattedTextField _component;

		public DateCellEditor () {
			_component = new JFormattedTextField (new DateFormatter (new SimpleDateFormat (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("from_to_format_long"))));
			_component.setHorizontalAlignment (JTextField.TRAILING);
//			_component.addFocusListener (new FocusListener () {
//				public void focusGained (FocusEvent e) {
//					_component.requestFocusInWindow ();
//					_component.selectAll ();
//				}
//				public void focusLost (FocusEvent e) {
//				}
//			});

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
//			_component.selectAll ();
			_component.setCaretPosition (0);
			return _component;
		}
	}
	
	/**
	 * Editor per le durate.
	 */
	private class DurationCellEditor extends AbstractCellEditor implements TableCellEditor {
		final JFormattedTextField _component;
		public DurationCellEditor () {
			_component = new DurationTextField ();;
			_component.setHorizontalAlignment (JTextField.TRAILING);
//			_component.addFocusListener (new FocusListener () {
//				public void focusGained (FocusEvent e) {
//					_component.requestFocusInWindow ();
//					_component.selectAll ();
//				}
//				public void focusLost (FocusEvent e) {
//				}
//			});
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
//			_component.selectAll ();
			_component.setCaretPosition (0);
			return _component;
		}
	}
	
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AGLI EDITOR
	 **********************************************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AI POPUP
	 **********************************************/
	
	
//	/**
//	 * Riferimento all'avanzamento interesato dalle azioni del popup menu per la tabella degli avanzamenti.
//	 *<P>
//	 *La presenza di questo riferimento sirende necessaria dato che non &egrave; possibile determinare
//	 *la riga interessata dal menu di popup in sede di gestione degli eventi di tipo PopupMenuEvent, che
//	 *non danno alcuna coordinata.
//	 */
//	private PieceOfWork _tablePopupMenuSubject;
//	private void prepareTablePopupMenu (MouseEvent evt) {
//		if (evt.getButton ()==MouseEvent.BUTTON3){
////			if (evt.getSource ()==progressesTable){
//			_tablePopupMenuSubject = getPieceOfWorkForEvent (evt);
////			}
//		}
//		deleteProgressPopupItem.setEnabled (_tablePopupMenuSubject!=null);
//	}
//	
//	private void prepareTablePopupMenu (PopupMenuEvent evt) {
//		deleteProgressPopupItem.setEnabled (_tablePopupMenuSubject!=null);
//	}
	
	/**
	 * Ritorna l'avanzamento della tabella candidato come soggetto di esecuzione delle azioni riferite alle coordinate dell'evento del mosue.
	 * <P>
	 * Viene usato dal popup menu.
	 * @param evt
	 * @return
	 */
	private PieceOfWork getPieceOfWorkForEvent (MouseEvent evt) {
		final int x = evt.getX ();
		final int y = evt.getY ();
		final Point p = new Point (x, y);
		final int r = progressesTable.rowAtPoint (p);
		if (r<0) {
			return null;
		}
		return (PieceOfWork) progressesTable.getModel ().getValueAt (progressesTable.convertRowIndexToModel (r), progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
	}
	
	private void prepareTreePopupMenu (PopupMenuEvent evt) {
//					if (evt.getSource ()==this.bundleTree){
//				final int x = evt.getX ();
//				final int y = evt.getY ();
//				treePopupPath = this.bundleTree.getPathForLocation (x, y);
//				if (treePopupPath!=null){
//					final Object node = treePopupPath.getLastPathComponent ();
//					treePopupNode = node;
//					if (node instanceof ResourceBundleModel){
//						bundlePopupMenu.show ((Component)evt.getSource (), x, y);
		
	}
	
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AI POPUP
	 **********************************************/
	
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA ALLE ACTION
	 **********************************************/
	
	
	
	
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
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			final TaskTreeModelImpl m = _context.getModel ();
			_context.getLogger ().debug ("Creating new task...");
			final Task t = new ProgressItem (_candidateParent.getName ()+"."+(_candidateParent.childCount ()+1));
			m.insertNodeInto (t, _candidateParent, _candidateParent.childCount ());
			SwingUtilities.invokeLater (new Runnable () {
				public void run () {
					/*
					 * @workaround invoca in modo asincrono
					 */
					final WorkSpace ws = _context.getModel ().getWorkSpace ();
					deepExpand (ws, t);
					final TreePath path = toTreePath (new TaskTreePath (ws, t));
					taskTree.scrollPathToVisible (path);
					taskTree.getTreeSelectionModel ().setSelectionPath (path);
					final int row = taskTree.getSelectedRow ();
					taskTree.editCellAt (row, TaskJTreeModel.TREE_COLUMN_INDEX);
					if (taskTree.getCellEditor () instanceof TreeTableCellEditor) {
						final JTextField editor = (JTextField)taskTree.getCellEditor ().getTableCellEditorComponent (taskTree, t.getName (), true, row, TaskJTreeModel.TREE_COLUMN_INDEX);
						editor.requestFocusInWindow ();
						editor.selectAll ();
					}
				}
			});
			
			_context.getLogger ().debug ("New task created");
		}
		
		Task _candidateParent;
		private boolean isEnabled (final Task candidateParent) {
			return candidateParent!=null;
		}
		
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
	 * Elimina i task selezionati.
	 *
	 */
	private class DeleteTasksAction extends AbstractAction implements TreeSelectionListener {
		
		
		/**
		 * Costruttore.
		 */
		public DeleteTasksAction () {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			/*
			 * @workaround forza il cambio di selezione, datoche la modifica dell'albero non modifica la selezione, comportandfonullpinter nella tabella, che usa datinon piu' validi
			 */
//			final Map<Task, List<Task>> candidatesBackup = new HashMap<Task, List<Task>> (_removalCandidates);
//			int sel = taskTree.getSelectionModel ().getMinSelectionIndex ();
//			if (sel>0) {
//				sel--;
//			} else {
//				sel = 0;
//			}
//			taskTree.getSelectionModel ().setSelectionInterval (sel,sel);
//			final TaskTreeModelImpl m = _context.getModel ();
//			for (final Task parent : candidatesBackup.keySet ()) {
//				m.removeTasks (parent, candidatesBackup.get (parent));
//			}
			_context.getLogger ().debug ("Removing tasks...");
			final TaskTreeModelImpl m = _context.getModel ();
			for (final Task parent : _removalCandidates.keySet ()) {
				m.removeTasks (parent, _removalCandidates.get (parent));
			}
			_context.getLogger ().debug ("Tasks removed");
		}
		
		Map<Task, List<Task>> _removalCandidates;
		private boolean isEnabled (final Map<Task, List<Task>> removalCandidates) {
			return !removalCandidates.isEmpty ();
		}
		
		public void valueChanged (TreeSelectionEvent e) {
			final Map<Task, List<Task>> removalCandidates = new HashMap ();
			for (final int r : taskTree.getSelectedRows ()){
				final Task candidate = (Task) taskTree.getModel ().getValueAt (taskTree.convertRowIndexToModel (r), taskTree.convertColumnIndexToModel (TaskJTreeModel.TREE_COLUMN_INDEX));
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
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
			taskTree.addTreeSelectionListener (this);
			_context.getModel ().addWorkAdvanceModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			if (0!=(e.getModifiers() & ActionEvent.SHIFT_MASK)){
				_context.getWindowManager ().showStartPieceOfWorkDialog (_candidateParent);
			} else {
				_context.getLogger ().debug ("Starting new action...");
				final TaskTreeModelImpl m = _context.getModel ();
				m.insertPieceOfWorkInto (new Progress (new Date (), null, (ProgressItem)_candidateParent), _candidateParent, _candidateParent.pieceOfWorkCount ());
				_context.getLogger ().debug ("Action started");
			}
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
	 * Fa partire un nuovo avanzamento clone.
	 *
	 */
	private class StartProgressCloneAction extends AbstractAction implements ListSelectionListener, WorkAdvanceModelListener {
		
		
		/**
		 * Costruttore.
		 */
		public StartProgressCloneAction () {
			_context.getModel ().addWorkAdvanceModelListener (this);
			progressesTable.getSelectionModel ().addListSelectionListener (this);
			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);
			
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Starting progress clone...");
			final TaskTreeModelImpl m = _context.getModel ();
			final ProgressItem parent = (ProgressItem)_candidateSource.getTask ();
			final Progress clone = new Progress (new Date (), null, parent);
			clone.setDescription (_candidateSource.getDescription ());
			clone.setNotes (_candidateSource.getNotes ());
			m.insertPieceOfWorkInto (clone, parent, parent.pieceOfWorkCount ());
			_context.getLogger ().debug ("Clone progress started");
		}
		
		private Progress _candidateSource;
		
		private boolean _isEnabled () {
			return _candidateSource!=null && _context.getModel ().getAdvancing ().isEmpty ();
		}
		
		private void resetEnabled () {
			_candidateSource = null;
			setEnabled ();
		}
		
		private void setEnabled () {
			setEnabled (_isEnabled ());
		}

		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()){
				/* evento spurio */
				return;
			}
			if (progressesTable.getSelectedRows ().length>1) {
				resetEnabled ();
				return;
			}
			int r = progressesTable.getSelectedRow ();
			if (r<0) {
				resetEnabled ();
				return;
			}
			
			r = progressesTable.convertRowIndexToModel (r);
			if (r<0 || r>=progressesTable.getModel ().getRowCount ()) {
				resetEnabled ();
				return;
			} 
			_candidateSource = (Progress)progressesTable.getModel ().getValueAt (r, progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
			setEnabled ();
		}
		
	}
		
	/**
	 * Continua un avanzamento come se non fosse mai terminato.
	 *
	 */
	private class ContinueProgressAction extends AbstractAction implements ListSelectionListener, WorkAdvanceModelListener {
		
		
		/**
		 * Costruttore.
		 */
		public ContinueProgressAction () {
			_context.getModel ().addWorkAdvanceModelListener (this);
			progressesTable.getSelectionModel ().addListSelectionListener (this);
			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);
			
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Continuing a previously closed action...");
			final TaskTreeModelImpl m = _context.getModel ();
			m.updatePieceOfWork (_candidate, _candidate.getFrom (), null, _candidate.getDescription ());
			_context.getLogger ().debug ("Action (re)started");
		}
		
		private Progress _candidate;
		
		private boolean _isEnabled () {
			return _candidate!=null && _context.getModel ().getAdvancing ().isEmpty ();
		}
		
		private void resetEnabled () {
			_candidate = null;
			setEnabled ();
		}
		
		private void setEnabled () {
			setEnabled (_isEnabled ());
		}

		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled (_isEnabled ());
		}
		
		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()){
				/* evento spurio */
				return;
			}
			if (progressesTable.getSelectedRows ().length>1) {
				resetEnabled ();
				return;
			}
			int r = progressesTable.getSelectedRow ();
			if (r<0) {
				resetEnabled ();
				return;
			}
			
			r = progressesTable.convertRowIndexToModel (r);
			if (r<0 || r>=progressesTable.getModel ().getRowCount ()) {
				resetEnabled ();
				return;
			} 
			_candidate = (Progress)progressesTable.getModel ().getValueAt (r, progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
			setEnabled ();
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
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
			_context.getModel ().addWorkAdvanceModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Stopping current action...");
			final TaskTreeModelImpl m = _context.getModel ();
			final PieceOfWork pow = _context.getModel ().getAdvancing ().iterator ().next ();
			_context.getModel ().updatePieceOfWork (pow, pow.getFrom (), new Date (), pow.getDescription ());
			m.pieceOfWorkChanged (pow);
			_context.getLogger ().debug ("Current action stopped");
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
			taskTree.addTreeSelectionListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Creating new action...");
//			final TaskTreeModelImpl m = _context.getModel ();
			_context.getWindowManager ().getNewPieceOfWorkDialog ().showForTask (_candidateParent);
//			m.insertPieceOfWorkInto (new Progress (new Date (), null, (ProgressItem)_candidateParent), _candidateParent, _candidateParent.pieceOfWorkCount ());
			_context.getLogger ().debug ("New action created");
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
	 * Elimina gli avanzamenti selezionati.
	 *
	 */
	private class DeletePiecesOfWorkAction extends AbstractAction implements ListSelectionListener, WorkAdvanceModelListener, TaskTreeModelListener {
		
		/**
		 * Costruttore.
		 */
		public DeletePiecesOfWorkAction () {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_DELETE, 0));
			
			progressesTable.getSelectionModel ().addListSelectionListener (this);
			progressesTable.getColumnModel ().getSelectionModel ().addListSelectionListener (this);
			
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			_context.getLogger ().debug ("Deleting actions...");
			final TaskTreeModelImpl m = _context.getModel ();
			for (final Task t : _removalCandidates.keySet ()) {
				m.removePiecesOfWork (t, _removalCandidates.get (t));
			}
			_context.getLogger ().debug ("Actions deleted");
		}
		
		private final Map<Task, List<PieceOfWork>> _removalCandidates = new HashMap<Task, List<PieceOfWork>>  ();
		private boolean isEnabled (final Map<Task, List<PieceOfWork>> removalCandidates) {
			final boolean b = !removalCandidates.isEmpty ();
//			System.out.println ("isEnabled "+b);
//			System.out.println ("Enabling progress removalaction: "+b);
			return b;
		}
		
		/*
		 * @todo ottimizzare le chiamate a questo metodo
		 */
		private void setEnabled () {
			_removalCandidates.clear ();
			final int[] selectedRows = progressesTable.getSelectedRows ();
			/*
			 * @workarond rowCount prende dal modello e non dalla tabella, dato che la tabella risponde ancora con il
			 * numero di righe totali anche incaso di eliminazione in corso.
			 * @warning purtroppo sembra che questo comportamento anomalo sia time-dependant
			 */
			//final int rowCount = progressesTable.getRowCount ();
			final int rowCount = progressesTable.getModel ().getRowCount ();
			
			for (int r : selectedRows){
				r = progressesTable.convertRowIndexToModel (r);
				/*
				 * @workaround in questo momento la selezione Puo' ancora essere su righe ormai rimosse e fuori tabella
				 */
				if (r<0 || r>=rowCount) {
//					System.out.println ("Jumping, as per selected row "+r+" of "+rowCount);
					continue;
//				} else {
//					System.out.println ("OK, as per selected row "+r+" of "+rowCount);
//					System.out.println ("Selected rows: ["+Arrays.toString (selectedRows)+"]");
				}
				final PieceOfWork candidate = (PieceOfWork) progressesTable.getModel ().getValueAt (r, progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
				if (!candidate.isEndOpened ()) {
					/*
					 * non sta avanzando
					 */
					final Task t = candidate.getTask ();
					
					if (_removalCandidates.containsKey (t)) {
						/*
						 * gia' un fratello schedulato per la cancellazione.
						 */
						final List<PieceOfWork> l = _removalCandidates.get (t);
						l.add (candidate);
					} else {
						/*
						 * nessun fratello gia' in lista.
						 */
						final List<PieceOfWork> l = new ArrayList<PieceOfWork> ();
						l.add (candidate);
						_removalCandidates.put (t, l);
					}
				}
			}
			
			setEnabled (isEnabled (_removalCandidates));
//			System.out.println ("setEnabled "+ isEnabled ()+"for action "+hashCode ());
		}
		
		public void valueChanged (ListSelectionEvent e) {
			if (e.getValueIsAdjusting ()){
				/* evento spurio */
				return;
			}
			
			setEnabled ();
		}
		
		public void elementsInserted (WorkAdvanceModelEvent e) {
			setEnabled ();
		}
		
		public void elementsRemoved (WorkAdvanceModelEvent e) {
			setEnabled ();
		}
		
		public void treeNodesChanged (TaskTreeModelEvent e) {
			setEnabled ();
		}
		
		public void treeNodesInserted (TaskTreeModelEvent e) {
			setEnabled ();
		}
		
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			setEnabled ();
		}
		
		public void treeStructureChanged (TaskTreeModelEvent e) {
			setEnabled ();
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}
		
		
	}
	
	
	/**
	 * Crea ed imposta nell'applicazione un nuovo progetto.
	 */
	private class NewWorkSpaceAction extends AbstractAction {
		public NewWorkSpaceAction () {
		}
		
		public void actionPerformed (ActionEvent e) {
			_context.getLogger ().debug ("Creating new workspace");
			final String projectName = (String)JOptionPane.showInputDialog (MainWindow.this,
				java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Please_insert_new_workspace_name"),
				java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"),
				JOptionPane.PLAIN_MESSAGE,
				null, null, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace"));
			if (projectName==null || projectName.trim ().length ()==0) {
				return;
			}

			/*
			 * @todo spostare il controllo di univocità del nome all'interno del modello.
			 */
			for (final WorkSpace ws : _context.getWorkSpaceModel ().toArray ()) {
				if (projectName.equals (ws.getName ())) {
					JOptionPane.showMessageDialog (MainWindow.this, 
						java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("New_workspace_conflicting_name_message"));
					return;
				}
			}
			final WorkSpace prj = new Project (projectName, new ProgressItem (projectName));
			_context.getWorkSpaceModel ().addElement (prj);
			_context.getModel ().setWorkSpace (prj);
			_context.getLogger ().debug ("New workspace created");
		}
	}
	
	private class OpenWorkSpaceAction extends AbstractAction {
		public void actionPerformed (ActionEvent e) {
			this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
			_context.getWindowManager ().showOpenWorkSpaceDialog (_context.getModel ().getWorkSpace ());
		}
	}
	
	private class ImportWorkSpaceAction extends AbstractAction {
		public void actionPerformed (ActionEvent e) {
			
			_context.getLogger ().debug ("Restoring workspace backup...");
			
			xmlFileChooser.setDialogTitle (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Import_workspace_from"));
			final int returnVal = xmlFileChooser.showOpenDialog (MainWindow.this);
			if (returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			final SAXBuilder builder = new SAXBuilder ();
			try {
				final org.jdom.Document d = builder.build (new FileInputStream (xmlFileChooser.getSelectedFile ()));

				if (new XMLAgent (_context).restore (d, new XMLImporter.ImportConflictsResolver () {
					public XMLImporter.ConflictResolution chooseResolution () {
						final XMLImporter.ConflictResolution[] options = new XMLImporter.ConflictResolution[] {XMLImporter.ConflictResolution.OVERWRITE, XMLImporter.ConflictResolution.RENAME, XMLImporter.ConflictResolution.CANCEL};
						final XMLImporter.ConflictResolution defaultValue = XMLImporter.ConflictResolution.OVERWRITE;
						final int choice = JOptionPane.showOptionDialog (MainWindow.this, 
							java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Workspace_import_conflict_resolution_message"), 
							java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Import_conflict"), 
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, defaultValue);
						if (choice<0) {
							return XMLImporter.ConflictResolution.CANCEL;
						}
						return options[choice];
					}

					public String getNewName () {
						return JOptionPane.showInputDialog (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Rename_importing_workspace_to"));
					}
					
					
					public boolean continueOverwritingCurrentWorkspace () {
						return JOptionPane.YES_OPTION==JOptionPane.showConfirmDialog (MainWindow.this, 
							java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Current_workspace_overwrite_confirm_equest"), 
							java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Overwriting_current_workspace"), 
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					}
				})) {
					JOptionPane.showMessageDialog (MainWindow.this, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Import_completed"));
				}
			} catch (final FileNotFoundException fnfe) {
				throw new RuntimeException (fnfe);
			} catch (final IOException ioe) {
				throw new RuntimeException (ioe);
			} catch (final JDOMException jde) {
				throw new RuntimeException (jde);
			}
			_context.getLogger ().debug ("Workspace backup restored");
		}
		
	}
	
	private class ExportWorkSpaceAction extends AbstractAction {
		public void actionPerformed (ActionEvent e) {
			_context.getLogger ().debug ("Generating workspace backup...");
			
			int returnVal;
			boolean isNullFile;
			boolean fileExists;
			do {
				xmlFileChooser.setSelectedFile (new File (_context.getModel ().getWorkSpace ().getName ()+"_"+CalendarUtils.getTimestamp (new Date (), "yyyyMMdd")+".xml"));
				xmlFileChooser.setDialogTitle (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Export_workspace_to"));
				returnVal = xmlFileChooser.showSaveDialog(MainWindow.this);
				if (returnVal != JFileChooser.APPROVE_OPTION) {
					return;
				}
				isNullFile = xmlFileChooser.getSelectedFile ()==null;
				final boolean overwriting = !isNullFile && xmlFileChooser.getSelectedFile ().exists ();
				if (overwriting){
					final int canOverwrite = JOptionPane.showConfirmDialog (MainWindow.this, "The selected file already exixts. Do you really want to overwrite it?");
					if (canOverwrite==JOptionPane.CANCEL_OPTION){
						return;
					}
					fileExists = canOverwrite!=JOptionPane.OK_OPTION;
				} else {
					fileExists = false;
				}
			} while (isNullFile || fileExists);			
			
			final org.jdom.Document d = new org.jdom.Document ();
			if (new XMLAgent (_context).backup (_context.getModel ().getWorkSpace (), d)) {
				try {
					new XMLOutputter ().output (d, new FileOutputStream (xmlFileChooser.getSelectedFile ()));
					JOptionPane.showMessageDialog (MainWindow.this, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Export_completed"));
				} catch (FileNotFoundException ex) {
					throw new RuntimeException (ex);
				} catch (IOException ex) {
					throw new RuntimeException (ex);
				}
			}
			_context.getLogger ().debug ("Workspace backup generated");
		}
	}
	
	private class ImportSubtreeAction extends AbstractAction {
		public void actionPerformed (ActionEvent e) {
		}
	}
	
	private class DeleteAction extends TransferAction {
		public DeleteAction (final TransferActionListener tal) {
			super ("delete", tal);
		}
		
	}
	
	private class StartReportAction extends AbstractAction {
		public void actionPerformed (ActionEvent e) {
			final int r = taskTree.getSelectedRow ();
			Task reportRoot;
			if (r<0) {
				reportRoot = _context.getModel ().getRoot ();
			} else {
				reportRoot = (Task) taskTree.getModel ().getValueAt (taskTree.convertRowIndexToModel (r), taskTree.convertColumnIndexToModel (TaskJTreeModel.TREE_COLUMN_INDEX));
			}
			
			_context.getWindowManager ().showReportDialog (reportRoot);
		}
	}
	
	
		
	/**
	 * Espande l'albero.
	 *
	 */
	private class ExpandTreeAction extends AbstractAction implements TreeSelectionListener, TaskTreeModelListener  {
		
		
		/**
		 * Costruttore.
		 */
		public ExpandTreeAction () {
			taskTree.addTreeSelectionListener (this);
			_context.getModel ().addTaskTreeModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			deepExpand (_context.getModel ().getWorkSpace (), _candidate);
		}
		
		public void setEnabled () {
			setEnabled (_isEnabled ());
		}
		
		private boolean _isEnabled () {
			return _candidate!=null /*&& _candidate.childCount ()!=0*/;
		}
		
		Task _candidate;
		public void valueChanged (TreeSelectionEvent e) {
			final TreePath path = e.getPath ();
			Task candidate;
			if (path==null) {
				candidate = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					candidate = (Task)o;
				} else {
					candidate = null;
				}
			}
			setCandidate (candidate);
		}
		
		private void setCandidate (Task candidate) {
			final Task oldCandidate = _candidate;
			_candidate = candidate;
			if (oldCandidate !=_candidate){
				setEnabled ();
			}
		}

		public void treeNodesChanged (TaskTreeModelEvent e) {
		}

		public void treeNodesInserted (TaskTreeModelEvent e) {
			dataChanged ();
		}

		protected void dataChanged () {
			final TreePath p = taskTree.getTreeSelectionModel ().getSelectionPath ();
			if (p!=null) {
				setCandidate ((Task)p.getLastPathComponent ());
			}
		}
		
		public void treeNodesRemoved (TaskTreeModelEvent e) {
			dataChanged ();
		}

		public void treeStructureChanged (TaskTreeModelEvent e) {
			dataChanged ();
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}

	}
	
	/**
	 * Collassa l'albero.
	 *
	 */
	private class CollapseTreeAction extends AbstractAction implements TreeSelectionListener, TaskTreeModelListener  {
		
		
		/**
		 * Costruttore.
		 */
		public CollapseTreeAction () {
			taskTree.addTreeSelectionListener (this);
			_context.getModel ().addTaskTreeModelListener (this);
			setEnabled (false);
		}
		
		public void actionPerformed (java.awt.event.ActionEvent e) {
			taskTree.collapsePath (toTreePath (new TaskTreePath (_context.getModel ().getWorkSpace (), _candidate)));
		}
		
		public void setEnabled () {
			setEnabled (_isEnabled ());
		}
		
		private boolean _isEnabled () {
			return _candidate!=null /*&& taskTree.isExpanded (toTreePath (new TaskTreePath (_context.getModel ().getWorkSpace (), _candidate)))*/;
		}
		
		Task _candidate;
		public void valueChanged (TreeSelectionEvent e) {
			final TreePath path = e.getPath ();
			Task candidate;
			if (path==null) {
				candidate = null;
			} else {
				final Object o = path.getLastPathComponent ();
				if (o instanceof Task){
					candidate = (Task)o;
				} else {
					candidate = null;
				}
			}
			setCandidate (candidate);
		}
		
		private void setCandidate (Task candidate) {
			final Task oldCandidate = _candidate;
			_candidate = candidate;
			if (oldCandidate !=_candidate){
				setEnabled ();
			}
		}

		public void treeNodesChanged (TaskTreeModelEvent e) {
		}

		public void treeNodesInserted (TaskTreeModelEvent e) {
			dataChanged ();
		}

		
		protected void dataChanged () {
			final TreePath p = taskTree.getTreeSelectionModel ().getSelectionPath ();
			if (p!=null) {
				setCandidate ((Task)p.getLastPathComponent ());
			}
		}
		

		public void treeNodesRemoved (TaskTreeModelEvent e) {
			dataChanged ();
		}

		public void treeStructureChanged (TaskTreeModelEvent e) {
			dataChanged ();
		}

		public void workSpaceChanged (WorkSpace oldWS, WorkSpace newWS) {
		}

	}
	
	/**********************************************
	 * FINE SEZIONE DEDICATA ALLE ACTION
	 **********************************************/
	
	
	
	
	
	
	
	
	
	
	
	/**********************************************
	 * INIZIO SEZIONE DEDICATA AI TRANSFER HANDLER
	 **********************************************/
	
	/**
	 * Trasferimento dati (DnD, cut&paste) per l'albero
	 */
	private final class TaskTreeTransferHandler extends ProgressItemTransferHandler{
		
		private final DataFlavor progressItemFlavor = DataFlavors.progressItemFlavor;
		
		public TaskTreeTransferHandler () {
			super (_context);
		}
		
		
		/**
		 * Overridden to include a check for a ProgressItem or Progress flavor.
		 */
		@Override
			public boolean canImport (JComponent c, DataFlavor[] flavors) {
			return hasProgressItemFlavor (flavors) || hasProgressFlavor (flavors);
		}
		
		/*
		 * Bundle up the selected items in the list
		 * as a single string, for export.
		 */
		@Override
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
		@Override
			protected void importProgressItems (JComponent c, Task[] progressItems, boolean removeFromSource) {
			
			final ProgressItem releaseTarget = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			final boolean rootTarget = releaseTarget.getParent ()==null;
			
			final Object[] optionsArray = new Object[] {PasteMode.BEFORE, PasteMode.AFTER, PasteMode.AS_LAST_CHILD};
			final StringBuffer sourceNames = new StringBuffer ();
			
			for (int i = 0; i< progressItems.length; i++) {
				if (progressItems[i]==releaseTarget) {
					/*
					 * uno dei task da incollare e' la destinazione stessa.
					 * Non si fa niente!!!
					 */
					return;
				}
			}
			
			if (progressItems.length>0) {
				sourceNames.append (progressItems[0]);
				for (int i = 1; i< progressItems.length; i++) {
					sourceNames.append (", ").append (progressItems[i]);
				}
			}
			
			final int selectedOption = rootTarget?
				//sul nodo radice si incolla solo come figlio
				Arrays.binarySearch (optionsArray, PasteMode.AS_LAST_CHILD):
				JOptionPane.showOptionDialog (
				taskTree,
				MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("PasteOptionsDialog/MainMessage"), releaseTarget.getName (), sourceNames.toString ()),
				java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("PasteOptionsDialog/Title"),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				optionsArray,
				PasteMode.AS_LAST_CHILD
				);
			
			if (selectedOption == JOptionPane.CLOSED_OPTION) {
				/*
				 * paste cancellato
				 */
				return;
			}
			
			final PasteMode pasteMode = (PasteMode)optionsArray[selectedOption];
			
			Task target;
			if (pasteMode==PasteMode.AS_LAST_CHILD) {
				target = releaseTarget;
			} else if (pasteMode==PasteMode.BEFORE) {
				target = releaseTarget.getParent ();
			} else {
				//after
				target = releaseTarget.getParent ();
			}
			
			if (c!=taskTree){return;}
			if (progressItems.length == 0) {
				return;
			}
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
				if (removeFromSource) {
					for (final Task p : m.keySet ()) {
						final List<Task> l = m.get (p);
						_context.getModel ().moveTasksTo (p, l, target, getPastePosition (pasteMode, releaseTarget, l, false));
						expandPastedTask (pasteMode, l);
					}
				} else {
					for (final Task p : m.keySet ()) {
						final List<Task> l = m.get (p);
						copySubtree (l, target, getPastePosition (pasteMode, releaseTarget, l, false));
						expandPastedTask (pasteMode, l);
					}
				}
			} catch (final IllegalOperationException iae){
				JOptionPane.showMessageDialog (taskTree, iae.getMessage ());
			}
		}
		
		private void expandPastedTask (final PasteMode pasteMode, final List<Task> l) {
			if (pasteMode==PasteMode.AS_LAST_CHILD && !l.isEmpty ()) {
				taskTree.expandPath (toTreePath (new TaskTreePath (_context.getModel ().getWorkSpace (), l.get (0))));
			}
		}
		
		/**
		 * Ritorna la posizione da usare per il paste della lista di task fratelli, con la modalit&agrave; ed il target specificati.
		 */
		private int getPastePosition (final PasteMode pasteMode, final Task releaseTarget, final List<Task> sources, final boolean copying) {
			if (pasteMode==PasteMode.AS_LAST_CHILD) {
				return -1;
			} else if (pasteMode==PasteMode.BEFORE) {
				return releaseTarget.getParent ().childIndex (releaseTarget);
			} else {
				if (copying) {
					return releaseTarget.getParent ().childIndex (releaseTarget)+1;
				} else {
					//after
					final Task releaseParent = releaseTarget.getParent ();
					final int releasePos = releaseParent.childIndex (releaseTarget);
					
					int diff = 0;
					for (final Task source : sources) {
						final Task sourceParent = source.getParent ();
						
						if (sourceParent!=null && sourceParent.equals (releaseTarget.getParent ())) {
							//paste su task "fratello"
							final int sourcePos = sourceParent.childIndex (source);
							if (sourcePos<releasePos) {
								/*
								 * aumenta il contatore degli elementi da sottrarre
								 */
								diff++;
							}
						}
					}
					return releasePos - diff + 1;
				}
			}
		}
		
		
		private void copySubtree (final List<Task> sources, final Task target, final int pos) {
			copySubtree (sources, target, target, target.getChildren (), pos);
		}
		
		
		private void copySubtree (final List<Task> sources, final Task target, final Task start, final List<Task> startChildren, final int pos) {
			final List<Task> copies = new ArrayList<Task> ();
			for (final Task t : sources) {
				final Task copy = new ProgressItem ((ProgressItem)t);
				copies.add (copy);
			}
			_context.getModel ().insertTasksInto (copies, target, pos);
			final int size = copies.size ();
			for (int i = 0; i < size; i++) {
				final Task s = sources.get (i);
				final Task c = copies.get (i);
				
				if (sources.get (i) == start) {
					/*
					 * sta copiando il target, bisogna prendere gli elementi originari, senza eventuali elementi introdotti dalla copia
					 */
					copySubtree (startChildren, c, start, startChildren, -1);
				} else {
					copySubtree (s.getChildren (), c, start, startChildren, -1);
				}
				_importProgresses ((PieceOfWork[])s.getPiecesOfWork ().toArray (new PieceOfWork[0]), (ProgressItem)c, false);
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
		
		
		
		
		/**
		 * Non esporta nulla. Caso non previsto, questo albero non gestisce gli avanzamenti.
		 */
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
		protected void importProgresses (JComponent c, PieceOfWork[] progresses, boolean removeFromSource) {
			if (c!=taskTree){return;}
			if (progresses.length==0) {
				return;
			}
			final ProgressItem target = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			_importProgresses (progresses, target, removeFromSource);
		}
		
		@Override
			public boolean importData (JComponent c, Transferable t) {
			if (!canImport (c, t.getTransferDataFlavors ())) {
				return false;
			}
			
			try {
				if (hasProgressItemFlavor (t.getTransferDataFlavors ())){
					final TransferData<Task> td = (TransferData<Task>)t.getTransferData (progressItemFlavor);
					final Task[] progressItems = (Task[])td.getData ();
					importProgressItems (c, progressItems, td.getAction ()!=TransferHandler.COPY);
				} else if (hasProgressFlavor (t.getTransferDataFlavors ())){
					final TransferData<PieceOfWork> td = (TransferData<PieceOfWork>)t.getTransferData (progressFlavor);
					final PieceOfWork[] progresses = td.getData ();
					importProgresses (c, progresses, td.getAction ()!=TransferHandler.COPY);
				}
			} catch (UnsupportedFlavorException ufe) {
				_context.getLogger ().warning ("Error transferring UI data.", ufe);
				return false;
			} catch (IOException ioe) {
				_context.getLogger ().warning ("Error transferring UI data.", ioe);
				return false;
			}
			return true;
		}
		
		protected void cleanup (JComponent c, Transferable data, int action) {
		}
		
		public boolean canCopy () {
			final Task[] t = exportProgressItems (taskTree);
			return t.length>0;
		}
		
		public boolean canCut () {
			final Task[] sel = exportProgressItems (taskTree);
			if (sel.length==0) {
				return false;
			}
			for (final Task t : sel) {
				if (t.getParent ()==null) {
					//nodo radice selezionato
					return false;
				}
			}
			return true;
		}
		
		public boolean canPaste () {
			return true;
		}
		
	}
	
	
	
	
	/**
	 * Trasferimento dati (DnD, cut&paste) per la tabella
	 */
	private final class ProgressTableTransferHandler extends ProgressTransferHandler {
		public ProgressTableTransferHandler () {
			super (_context);
		}
		
		protected PieceOfWork[] exportProgresses (JComponent c) {
			if (c!=progressesTable){
				return null;
			}
			final int[] selectedRows = progressesTable.getSelectedRows ();
			final int selectedRowsCount = selectedRows.length;
			final PieceOfWork[] exportedProgresses = new PieceOfWork[selectedRowsCount];
			for (int i=0;i<selectedRowsCount;i++){
				exportedProgresses[i] = (PieceOfWork)((ProgressesJTableModel)progressesTable.getModel ()).getValueAt (progressesTable.convertRowIndexToModel (selectedRows[i]), progressesTable.convertColumnIndexToModel (DURATION_COL_INDEX));
			}
			System.out.println ("Exported progresses: "+selectedRowsCount);
			return exportedProgresses;
		}
		
		/**
		 * ATTENZIONE: si assume che gli avanzamenti appartengano almedesimo task.
		 */
		@Override
			protected void importProgresses (JComponent c, PieceOfWork[] progresses, boolean removeFromSource) {
			if (c!=progressesTable){
				return;
			}
			if (progresses.length==0) {
				return;
			}
			
			final ProgressItem target = (ProgressItem)taskTree.getTreeSelectionModel ().getSelectionPath ().getLastPathComponent ();
			_importProgresses (progresses, target, removeFromSource);
		}
		
		protected void cleanup (JComponent c, Transferable data, int action) {
		}
		
		public boolean canCopy () {
			final PieceOfWork[] pow = exportProgresses (progressesTable);
			return pow.length>0;
		}
		
		public boolean canCut () {
			final PieceOfWork[] sel = exportProgresses (progressesTable);
			if (sel.length==0) {
				return false;
			}
			return true;
		}
		
		public boolean canPaste () {
			return true;
		}
	}
	
	/**
	 * Effettua il trasferimento effettivo dei dati relativo agliavanzamenti, in caso di Dnd, Cut&Paste, etc
	 */
	private void _importProgresses (final PieceOfWork[] progresses, final ProgressItem target, final boolean removeFromSource) {
		if (removeFromSource) {
			/*
			 * sposta
			 */
			_context.getModel ().movePiecesOfWorkTo (progresses[0].getTask (), Arrays.asList (progresses), target, -1);
		} else {
			/*
			 * copia
			 */
			final List<PieceOfWork> l = new ArrayList<PieceOfWork> ();
			for (final PieceOfWork pow : Arrays.asList (progresses)) {
				if (pow.isEndOpened ()) {
					/*
					 * non copia gli avanzamenti in corso
					 */
					continue;
				}
				final PieceOfWork copy = new Progress ((Progress)pow);
				copy.setTask (target);
				copy.setFrom (pow.getFrom ());
				copy.setTo (pow.getTo ());
				copy.setDescription (pow.getDescription ());
				l.add (copy);
			}
			_context.getModel ().insertPiecesOfWorkInto (l, target, -1);
		}
		
		
	}
	
	private static enum PasteMode {
		BEFORE {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("GeneralChoice/PasteMode/before");
			}
		},
		AFTER {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("GeneralChoice/PasteMode/after");
			}
		},
		AS_LAST_CHILD {
			public String toString () {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("GeneralChoice/PasteMode/as_last_child");
			}
		}
		
	}
	
	/**********************************************
	 * FINE SEZIONE DEDICATA AI TRANSFER HANDLER
	 **********************************************/
	
	private static Object[] toObjectArray (final TaskTreePath p){
		final List l = new ArrayList ();
		populateList (l, p);
		return l.toArray ();
	}
	
	private static void populateList (final List l, final TaskTreePath p){
		final Task t = p.getLastPathComponent ();
		if (t!=null) {
			l.add (0, t);
		}
		final TaskTreePath parentPath = p.getParentPath ();
		if (parentPath!=null) {
			populateList (l, parentPath);
		}
	}
	
	private static TreePath toTreePath (final TaskTreePath p){
		return new TreePath (toObjectArray (p));
	}

	
	/**
	 * Imposta l'immagine di background per la tabella degli avanzamenti
	 */
	private void setTableBackoundImage (final ImageIcon image) {
		ptbImage = image;
		ptbImageWidth = ptbImage.getIconWidth();
		ptbImageHeight = ptbImage.getIconHeight();
	}
	
	/**
	 * consente di disegnare in grassetto il percorsoinprogress
	 */
	private class TaskTreeCellRenderer extends DefaultTreeCellRenderer {
		private Font _originalFont;
		private Font _boldFont;
		
		public Component getTreeCellRendererComponent (JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			final JLabel res = (JLabel)super.getTreeCellRendererComponent ( tree, value, selected, expanded, leaf, row, hasFocus);
			final Task t = (Task)value;

			if (null==_originalFont) {
				_originalFont = res.getFont ();
			}
			if (null==_boldFont) {
				_boldFont = _originalFont.deriveFont (Font.BOLD);
			}

			boolean progressing = false;
			
			for (final TaskTreePath p : _progressingPaths) {
				if (p.contains (t)) {
					res.setFont (_boldFont);
					return res;
				}
			}
			res.setFont (_originalFont);

			return res;
		}
	}
	
	private class TaskTreeDuration  {
		private Duration _d;
		private Task _t;
		
		public TaskTreeDuration () {
		}
		
		public TaskTreeDuration (final Duration d, final Task t) {
			_d = d;
			_t = t;
		}
		public Duration getDuration () {
			return _d;
		}
		public Task getTask () {
			return _t;
		}
		public void setDuration (Duration d) {
			_d = d;
		}
		public void setTask (Task t) {
			_t = t;
		}
	}
	
	
	
	private final static AlphaComposite ac = AlphaComposite.getInstance (AlphaComposite.SRC_OVER, 0.5f);
		
	private final RingChartPainter tbp = new RingChartPainter ();
	
	private class RingChartPainter {
		
		private final Semaphore semaphore = new Semaphore (0);
		
		private ProgressItem _chartRoot = null;
		
		/**
		 * Ritorna la radice del grafico.
		 */
		public ProgressItem getChartRoot () {
			return _chartRoot;
		}
		
		private boolean _needRevalidation;
		/**
		 * Indica se &egrave; un ricaricamento deidati del grafico.
		 * E' necessario quando, a fronte di un evento di variazione della radice, essonon viene 
		 * utilizzato per ricaricare il grafico, in quanto ilgrafico non &egrave; attualmente visibile, e non &egrave; nemmeno
		 * abilitato il ghost nella tabella.
		 */
		public boolean needsRevalidation () {
			return _needRevalidation;
		}
		
		/**
		 * Provoca il ricaricamento del grafico se necessario.
		 */
		public void revalidateIfNeeded () {
			if (_needRevalidation) {
				((RingChartPanel)chartPanel).reloadChart (getChartRoot ());				
			}
		}
		
		RingChartPainter (){
			final Thread t = new Thread (new Runnable () {
				public void run () {
					while (true) {
						semaphore.acquireUninterruptibly ();
						ProgressItem chartRoot = null;
//						synchronized (_chartRoot) {
						chartRoot = _chartRoot;
//						}
						if (chartRoot!=null) {
							if (chartPanel.isVisible () || _context.getApplicationOptions ().isChartGhostEnabled ()) {
								
								_needRevalidation = false;
								((RingChartPanel)chartPanel).reloadChart (_chartRoot);
								
								if (_context.getApplicationOptions ().isChartGhostEnabled ()) {
									/*
									 * Abilita il thread per il ridisegno dello sfondo solo se abilitato
									 */
									final int w =progressesTable.getWidth ();
									final int h = progressesTable.getHeight ();
									if (w == 0 || h == 0 ) {
										/*
										 * @workaround necessario per evitare problemi in fase di inizializzazione della tabella
										 */
										continue;
									}
									final BufferedImage i = (BufferedImage)chartPanel.createImage (w, h);
									final Graphics2D g = i.createGraphics ();
									g.setComposite (ac);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
									SwingUtilities.invokeLater (new Runnable () {
										public void run () {
											chartPanel.paint (g);
											setTableBackoundImage (new ImageIcon (i));
											progressesTable.repaint ();
										}
									});
								}
							} else {
								_needRevalidation = true;
							}
						}
						
					}
				}
			}, "table-background-chart-painter");
			t.setPriority (Thread.MIN_PRIORITY);
			t.setDaemon (true);
			t.start ();
		}
		
		public void updateProgressTableBackground (final ProgressItem chartRoot) {
//			synchronized (_chartRoot) {
				if (_chartRoot == chartRoot) {
					return;
				}
				if (chartRoot==null) {
					return;
				}
				_chartRoot = chartRoot;
//			}
			repaintProgressTableBackground ();
		}
		
		public void repaintProgressTableBackground () {
			semaphore.release ();
		}
	}

}

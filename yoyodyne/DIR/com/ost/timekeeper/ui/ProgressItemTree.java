/*
 * ProgressItemTree.java
 *
 * Created on 29 dicembre 2004, 11.05
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.view.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.JTextField;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.tree.TreeCellEditor;

/**
 * Albero dei nodi di avanzamento.
 *
 * @author  davide
 * @todo implementare funzionalit? TreeTable.
 * @todo supporto icone custom.
 */
public final class ProgressItemTree extends com.ost.timekeeper.ui.support.treetable.JTreeTable implements Observer{
	
	private ProgressTreeModel _progressTreeModel;
	
	/**
	 * Costruttore.
	 * @param progressTreeModel il modello sottostante.
	 */
	public ProgressItemTree (final ProgressTreeModel progressTreeModel) {
		super (progressTreeModel);
		this._progressTreeModel= progressTreeModel;
		init (progressTreeModel);
	}
	
	/**
	 * Inizializza questo albero.
	 *
	 * @param progressTreeModel il modello.
	 */
	private void init (final ProgressTreeModel progressTreeModel){
		//		final ProgressItemCellRenderer progressItemCellRenderer = new ProgressItemCellRenderer ();
		final TableCellEditor treeCellEditor = this.getDefaultEditor (ProgressItem.class);
		treeCellEditor.addCellEditorListener (new CellEditorListener (){
			public void editingStopped (ChangeEvent e){
				CellEditor source = (CellEditor)e.getSource ();
				String newValue = (String)source.getCellEditorValue ();
				Application.getInstance ().getSelectedItem ().setName (newValue);
			}
			
			public void editingCanceled (ChangeEvent e){
				//				System.out.println (e);
			}
		});
		this.setCellEditor (treeCellEditor);
		//		this.setCellRenderer (progressItemCellRenderer);
		//		this.setModel (progressTreeModel);
		
		this.getSelectionModel ().setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		//		this.getSelectionModel ().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
//		this.setDragEnabled (true);
//		
		this.setTransferHandler (new ProgressItemTreeTransferHandler ());
		
//		java.awt.event.MouseMotionListener mml = new java.awt.event.MouseMotionAdapter () {
//			private boolean dragStarted = false;
//			public void mouseDragged(MouseEvent e) {dragStarted = true;}
//		};
//		
//		this.addMouseMotionListener (mml);
//		
//		java.awt.event.MouseListener ml = new java.awt.event.MouseAdapter () {
//			public void mouseReleased(java.awt.event.MouseEvent e) {
//				int selRow = tree.getRowForLocation (e.getX (), e.getY ());
//				TreePath selPath = tree.getPathForLocation (e.getX (), e.getY ());
//				if(selRow != -1) {
//					if(e.getClickCount () == 1) {
////						if (!ProgressItemTree.this.getUI ().isDragPossible(e)){
////						ProgressItemTree.this.getTree ().getUI ().handleSelection(e);
////						}
//					}
//					else if(e.getClickCount () == 2) {
////						myDoubleClick (selRow, selPath);
//					}
//				}
//			}
//		};
//		this.addMouseListener (ml);
		
//		DragSourceMotionListener
//		DragSourceListener
//		DropTargetListener
//		this.setDropTarget (new DropTarget(this, new DragPatch ()));
	}
	
//	private class DragPatch extends DragSourceAdapter implements DragSourceMotionListener {
//		private boolean success = false;
//	    public void dragDropEnd(DragSourceDropEvent dsde) {
//			this.success = dsde.getDropSuccess ();
//		}
//	}
	
	private final class ProgressItemTreeTransferHandler extends ProgressItemTransferHandler{
		
		private final DataFlavor progressItemFlavor = DataFlavors.progressItemFlavor;
		
		
		//	protected abstract ProgressItem exportProgressItem (JComponent c);
		//	protected abstract void importProgressItem (JComponent c, ProgressItem progressItem);
		//	protected abstract void cleanup (JComponent c, boolean remove);
		
		/**
		 * Crea un oggetto trasferibile.
		 *
		 * @param c l'elemento della UI.
		 * @return un oggetto trasferibile.
		 */
		protected Transferable createTransferable (JComponent c) {
			return new ProgressItemSelection (exportProgressItem (c));
		}
		
		public int getSourceActions (JComponent c) {
			return COPY_OR_MOVE;
		}
		
		public boolean importData (JComponent c, Transferable t) {
			if (canImport (c, t.getTransferDataFlavors ())) {
				try {
					if (hasProgressItemFlavor (t.getTransferDataFlavors ())){
						ProgressItem progressItem = (ProgressItem)t.getTransferData (progressItemFlavor);
						importProgressItem (c, progressItem);
						return true;
					} else if (hasProgressFlavor (t.getTransferDataFlavors ())){
						
						final Progress progress = (Progress)t.getTransferData (progressFlavor);
						importProgress (c, progress);
						return true;
					}
				} catch (UnsupportedFlavorException ufe) {
					Application.getLogger ().warning ("Error transferring UI data.", ufe);
				} catch (IOException ioe) {
					Application.getLogger ().warning ("Error transferring UI data.", ioe);
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
		protected com.ost.timekeeper.model.ProgressItem exportProgressItem (JComponent c) {
			if (c!=ProgressItemTree.this){return null;}
			return (ProgressItem)ProgressItemTree.this.getTree ().getSelectionPath ().getLastPathComponent ();
		}
		
		/*
		 * Take the incoming string and wherever there is a
		 * newline, break it into a separate item in the list.
		 */
		protected void importProgressItem (JComponent c, com.ost.timekeeper.model.ProgressItem progressItem) {
			if (c!=ProgressItemTree.this){return;}
			final ProgressItem target = (ProgressItem)ProgressItemTree.this.getTree ().getSelectionPath ().getLastPathComponent ();
			if (progressItem==target){
				/* Evita drop su se stesso. */
				return;
			}
			new MoveNode (progressItem, target, -1).execute ();
		}
		
		
		
		
		
		private final DataFlavor progressFlavor = DataFlavors.progressFlavor;
		
		
		//		/**
		//		 * Crea un oggetto trasferibile.
		//		 *
		//		 * @param c l'elemento della UI.
		//		 * @return un oggetto trasferibile.
		//		 */
		//		protected Transferable createTransferable (JComponent c) {
		//			return new ProgressSelection (exportProgress (c));
		//		}
		//
		//		public int getSourceActions (JComponent c) {
		//			return MOVE;
		//		}
		
		//		public boolean importData (JComponent c, Transferable t) {
		//			System.out.println ("Importing progress data ");
		//			if (canImport (c, t.getTransferDataFlavors ())) {
		//				try {
		//					final Progress progress = (Progress)t.getTransferData (progressFlavor);
		//					importProgress (c, progress);
		//					return true;
		//				} catch (UnsupportedFlavorException ufe) {
		//					Application.getLogger ().warning ("Error transferring UI data.", ufe);
		//				} catch (IOException ioe) {
		//					Application.getLogger ().warning ("Error transferring UI data.", ioe);
		//				}
		//			}
		//
		//			return false;
		//		}
		//
		//		protected void exportDone (JComponent c, Transferable data, int action) {
		//			cleanup (c, action == MOVE);
		//		}
		
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
		
		//		/**
		//		 * Overridden to include a check for a Progress flavor.
		//		 */
		//		public boolean canImport (JComponent c, DataFlavor[] flavors) {
		//			return hasProgressFlavor (flavors);
		//		}
		
		
		
		
		protected com.ost.timekeeper.model.Progress exportProgress (JComponent c) {
			return null;
			//		if (c!=SubtreeProgressesTable.this){return null;}
			//		return Application.getInstance ().getSelectedProgress ();//(Progress)SubtreeProgressesTable.this.getTree ().getSelectionPath ().getLastPathComponent ();
		}
		
		protected void importProgress (JComponent c, com.ost.timekeeper.model.Progress progress) {
			if (c!=ProgressItemTree.this){return;}
			final ProgressItem target = Application.getInstance ().getSelectedItem ();
			new MoveProgress (progress, target, -1).execute ();
		}
		
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && (arg.equals (ObserverCodes.ITEMPROGRESSINGPERIODCHANGE) || arg.equals (ObserverCodes.ITEMPROGRESSINGCHANGE))){
				//				this.reloadModel(((Application)o).getSelectedItem());
				final javax.swing.table.AbstractTableModel tModel = (javax.swing.table.AbstractTableModel)this.getModel ();
				if (tModel.getRowCount ()>0){
					tModel.fireTableChanged (new TableModelEvent (tModel, 0, tModel.getRowCount ()-1, 1));
				}
			}
		}
	}
	
//	public boolean isCellEditable(EventObject e) {
//		super.isCellEditable (e);
//	    return true;
//	}

}

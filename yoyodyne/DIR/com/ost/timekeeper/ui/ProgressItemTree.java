/*
 * ProgressItemTree.java
 *
 * Created on 29 dicembre 2004, 11.05
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.persistence.PersistenceStorage;
import com.ost.timekeeper.ui.persistence.PersistenceUtils;
import com.ost.timekeeper.ui.persistence.PersistentComponent;
import com.ost.timekeeper.ui.persistence.UIPersister;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.util.IllegalOperationException;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
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
public final class ProgressItemTree extends com.ost.timekeeper.ui.support.treetable.JTreeTable implements Observer, PersistentComponent{
	
	private ProgressTreeModel _progressTreeModel;
	
	/**
	 * Costruttore.
	 * @param progressTreeModel il modello sottostante.
	 */
	public ProgressItemTree (final ProgressTreeModel progressTreeModel) {
		super (progressTreeModel);
		final ProgressItemCellRenderer progressItemCellRenderer = new ProgressItemCellRenderer ();
		this._progressTreeModel= progressTreeModel;
		ToolTipManager.sharedInstance ().registerComponent (this.tree);
		this.tree.setCellRenderer (progressItemCellRenderer);
		init (progressTreeModel);
		this.getColumnModel ().removeColumn (this.getColumnModel ().getColumn (1));
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer ();
		renderer.setHorizontalAlignment (renderer.RIGHT);
		this.getColumnModel ().addColumn (new TableColumn (1, 100, renderer, null));
		
		final boolean inited = UIPersister.getInstance ().register (this, true);
	}
	
	/**
	 * Inizializza questo albero.
	 *
	 * @param progressTreeModel il modello.
	 */
	private void init (final ProgressTreeModel progressTreeModel){
		//		final TableCellEditor treeCellEditor = this.getDefaultEditor (ProgressItem.class);
		//		treeCellEditor.addCellEditorListener (new CellEditorListener (){
		//			public void editingStopped (ChangeEvent e){
		//				CellEditor source = (CellEditor)e.getSource ();
		//				String newValue = (String)source.getCellEditorValue ();
		//				Application.getInstance ().getSelectedItem ().setName (newValue);
		//			}
		//
		//			public void editingCanceled (ChangeEvent e){
		//				//				System.out.println (e);
		//			}
		//		});
		//		this.setCellEditor (treeCellEditor);
		//		this.setModel (progressTreeModel);
		
		this.getSelectionModel ().setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		//		this.getSelectionModel ().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		//disable internal dnd functionality, because we need our own implementation
		this.setDragEnabled (false);
		
		//attach transfer handler
		this.setTransferHandler (new ProgressItemTreeTransferHandler ());
		
		//since we have a transfer handler, we just need to attach mouse listeners
		//to initiate the drag inside of the transfer handler
		DnDMouseAdapter dndMouseAdapter = new DnDMouseAdapter ();
		this.addMouseListener (dndMouseAdapter);
		this.addMouseMotionListener (new DnDMouseMotionAdapter ());
		
		//revert MouseListeners order so that our MouseListener is called first
		//this is important to give drag n drop first priority and prevent the
		//internal mouse handler of tableUI changing the selection.
		java.awt.event.MouseListener[] mls = this.getMouseListeners ();
		for (int i = 0; i < mls.length; i++) {
			if (mls[i] != dndMouseAdapter) {
				this.removeMouseListener (mls[i]);
				this.addMouseListener (mls[i]);
			}
		}
		
		//set multiple selection
		this.getSelectionModel ().setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		
		//set selection mode for tree
		tree.getSelectionModel ().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		
		
		
		tree.setEditable (false);
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
			return new ProgressItemSelection (exportProgressItems (c));
		}
		
		public int getSourceActions (JComponent c) {
			return COPY_OR_MOVE;
		}
		
		public boolean importData (JComponent c, Transferable t) {
			if (canImport (c, t.getTransferDataFlavors ())) {
				try {
					if (hasProgressItemFlavor (t.getTransferDataFlavors ())){
						ProgressItem[] progressItems = (ProgressItem[])t.getTransferData (progressItemFlavor);
						importProgressItems (c, progressItems);
						return true;
					} else if (hasProgressFlavor (t.getTransferDataFlavors ())){
						
						final Progress[] progresses = (Progress[])t.getTransferData (progressFlavor);
						importProgresses (c, progresses);
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
		protected com.ost.timekeeper.model.ProgressItem[] exportProgressItems (JComponent c) {
			if (c!=ProgressItemTree.this){return null;}

//			TreePath path = ProgressItemTree.this.getTree ().getSelectionPath ();
//			if (path!=null){
//				return (ProgressItem)path.getLastPathComponent ();
//			} else {
//				if (dragPath!=null){
//					System.out.println ("Exporting last drag path "+dragPath.getLastPathComponent ());
//					return new ProgressItem[] {(ProgressItem)dragPath.getLastPathComponent ()};
//				} else 
//				if (ProgressItemTree.this.getTree ().getSelectionPath ()!=null){
//					System.out.println ("Exporting current selection path "+(ProgressItem)ProgressItemTree.this.getTree ().getSelectionPath ().getLastPathComponent ());
//					return new ProgressItem[] {(ProgressItem)ProgressItemTree.this.getTree ().getSelectionPath ().getLastPathComponent ()};
//				} else {
					int x = firstMouseEvent.getX ();
					int y = firstMouseEvent.getY ();
					System.out.println ("Exporting first mouse event path "+(ProgressItem)ProgressItemTree.this.getTree ().getPathForLocation (x, y).getLastPathComponent ());
					return new ProgressItem[] {(ProgressItem)ProgressItemTree.this.getTree ().getPathForLocation (x, y).getLastPathComponent ()};
//				}

//			}
		}
		
		/*
		 * Take the incoming string and wherever there is a
		 * newline, break it into a separate item in the list.
		 */
		protected void importProgressItems (JComponent c, com.ost.timekeeper.model.ProgressItem[] progressItems) {
			if (c!=ProgressItemTree.this){return;}
			final ProgressItem target = (ProgressItem)ProgressItemTree.this.getTree ().getSelectionPath ().getLastPathComponent ();
			try {
				for (int i=0;i<progressItems.length;i++){
					final ProgressItem progressItem = progressItems[i];
					System.out.println ("Importing node: "+progressItem);
					if (progressItem==target){
						/* Evita drop su se stesso. */
						continue;
					}
					new MoveNode (progressItem, target, -1).execute ();
				}
			} catch (final IllegalOperationException iae){
				JOptionPane.showMessageDialog (ProgressItemTree.this, ResourceSupplier.getString (ResourceClass.UI, "controls", "Illegal.operation"));
			}
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
		
		protected void importProgresses (JComponent c, com.ost.timekeeper.model.Progress[] progresses) {
			if (c!=ProgressItemTree.this){return;}
			final ProgressItem target = Application.getInstance ().getSelectedItem ();
			for (int i=0;i<progresses.length;i++){
				new MoveProgress (progresses[i], target, -1).execute ();
			}
		}
		
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null){
				if (arg.equals (ObserverCodes.ITEMPROGRESSINGPERIODCHANGE) ||
				arg.equals (ObserverCodes.ITEMPROGRESSINGCHANGE)){
					//				this.reloadModel(((Application)o).getSelectedItem());
					final javax.swing.table.AbstractTableModel tModel = (javax.swing.table.AbstractTableModel)this.getModel ();
					if (tModel.getRowCount ()>0){
						tModel.fireTableChanged (new TableModelEvent (tModel, 0, tModel.getRowCount ()-1, 1));
					}
				}
				if (arg.equals (ObserverCodes.CURRENTITEMCHANGE)){
					repaint ();
				}
				
				if (arg.equals (ObserverCodes.LOOKANDFEEL_CHANGING)){
					this.tree.setCellRenderer (new ProgressItemCellRenderer ());
				}
				
			}
		}
	}
	
	private MouseEvent firstMouseEvent;
	class DnDMouseAdapter extends java.awt.event.MouseAdapter {
		public void mousePressed (MouseEvent e) {
			firstMouseEvent = e;
			e.consume ();
		}
		
		public void mouseReleased (MouseEvent e) {
			firstMouseEvent = null;
		}
	}
	
	private TreePath dragPath;
	class DnDMouseMotionAdapter extends java.awt.event.MouseMotionAdapter {
		
		//define diplacement of five pixel for as drag
		private static final int PIXEL_DISPLACEMENT = 5;
		
		public void mouseDragged (MouseEvent e) {
			if (firstMouseEvent != null) {
				e.consume ();
				
				//if the user holds down the control key -> COPY, otherwise MOVE
				int ctrlMask = java.awt.event.InputEvent.CTRL_DOWN_MASK;
				int action = ((e.getModifiersEx () & ctrlMask) == ctrlMask) ? TransferHandler.COPY : TransferHandler.MOVE;
				
				int dx = Math.abs (e.getX () - firstMouseEvent.getX ());
				int dy = Math.abs (e.getY () - firstMouseEvent.getY ());
				
				//define a displacement of at least some pixel as a drag
				if (dx > DnDMouseMotionAdapter.PIXEL_DISPLACEMENT || dy > DnDMouseMotionAdapter.PIXEL_DISPLACEMENT) {
					//starting to drag...
					JComponent c = (JComponent) e.getSource ();
					TransferHandler handler = c.getTransferHandler ();
					
					//tell transfer handler to start drag
					handler.exportAsDrag (c, firstMouseEvent, action);
					
					int x = e.getX ();
					int y = e.getY ();
					
//					final TreePath path = ProgressItemTree.this.getTree ().getSelectionPath ();
//					if (path!=null){
//						/* c'e' una selezione corrente */
//						dragPath = path;
//					} else {
//						/* nessuna selezione */
						dragPath = ProgressItemTree.this.getTree ().getPathForLocation (x, y);
//					}
					System.out.println ("Setting last drag path to "+dragPath);
					//reset first mouse event for the next time
					firstMouseEvent = null;
				}
			}
		}
	}
	
	/**
	 * Ritorna la chiave usata per la persistenza degli attributi di questo componente.
	 *
	 * @return la chiave usata per la persistenza degli attributi di questo componente.
	 */	
	public String getPersistenceKey (){return "progressitemtree";}
	
	/**
	 * Rende persistente lo statodi questo componente.
	 * @param props lo storage delle impostazioni persistenti.
	 */
	public void makePersistent (final PersistenceStorage props){
		PersistenceUtils.makeColumnWidthsPersistent (props, this.getPersistenceKey (), this);
	}
	
	/**
	 * Ripristina lo stato persistente, qualora esista.
	 * @param props lo storage delle impostazioni persistenti.
	 * @return <TT>true</TT> se sono stati ripristinati i dati persistenti.
	 */
	public boolean restorePersistent (final PersistenceStorage props){
		PersistenceUtils.restorePersistentColumnWidths (props, this.getPersistenceKey (), this);
		return true;
	}
}

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
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;

/**
 * Albero dei nodi di avanzamento.
 *
 * @author  davide
 * @todo implementare funzionalità TreeTable.
 * @todo supporto icone custom.
 */
public final class ProgressItemTree extends com.ost.timekeeper.ui.support.treetable.JTreeTable{
	
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
		
		this.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.setDragEnabled(true);
		
		this.setTransferHandler (new ProgressItemTreeTransferHandler ());
	}
	
	private final class ProgressItemTreeTransferHandler extends ProgressItemTransferHandler{
		
		/*
		 * If the remove argument is true, the drop has been
		 * successful and it's time to remove the selected items
		 * from the list. If the remove argument is false, it
		 * was a Copy operation and the original list is left
		 * intact.
		 */
		protected void cleanup (JComponent c, boolean remove) {
//			if (remove && indices != null) {
//				JList source = (JList)c;
//				DefaultListModel model  = (DefaultListModel)source.getModel ();
//				//If we are moving items around in the same list, we
//				//need to adjust the indices accordingly, since those
//				//after the insertion point have moved.
//				if (addCount > 0) {
//					for (int i = 0; i < indices.length; i++) {
//						if (indices[i] > addIndex) {
//							indices[i] += addCount;
//						}
//					}
//				}
//				for (int i = indices.length - 1; i >= 0; i--) {
//					model.remove (indices[i]);
//				}
//			}
//			indices = null;
//			addCount = 0;
//			addIndex = -1;
		}
		
		/*
		 * Bundle up the selected items in the list
		 * as a single string, for export.
		 */
		protected com.ost.timekeeper.model.ProgressItem exportProgressItem (JComponent c) {
//        JList list = (JList)c;
//        indices = list.getSelectedIndices();
//        Object[] values = list.getSelectedValues();
//        
//        StringBuffer buff = new StringBuffer();
//
//        for (int i = 0; i < values.length; i++) {
//            Object val = values[i];
//            buff.append(val == null ? "" : val.toString());
//            if (i != values.length - 1) {
//                buff.append("\n");
//            }
//        }
//        
//        return buff.toString();
		if (c!=ProgressItemTree.this){return null;}
		return (ProgressItem)ProgressItemTree.this.getTree ().getSelectionPath ().getLastPathComponent ();
	}
		
		/*
		 * Take the incoming string and wherever there is a
		 * newline, break it into a separate item in the list.
		 */
		protected void importProgressItem (JComponent c, com.ost.timekeeper.model.ProgressItem progressItem) {
//			JList target = (JList)c;
//			DefaultListModel listModel = (DefaultListModel)target.getModel ();
//			int index = target.getSelectedIndex ();
//			
//			//Prevent the user from dropping data back on itself.
//			//For example, if the user is moving items #4,#5,#6 and #7 and
//			//attempts to insert the items after item #5, this would
//			//be problematic when removing the original items.
//			//So this is not allowed.
//			if (indices != null && index >= indices[0] - 1 &&
//			index <= indices[indices.length - 1]) {
//				indices = null;
//				return;
//			}
//			
//			int max = listModel.getSize ();
//			if (index < 0) {
//				index = max;
//			} else {
//				index++;
//				if (index > max) {
//					index = max;
//				}
//			}
//			addIndex = index;
//			String[] values = str.split ("\n");
//			addCount = values.length;
//			for (int i = 0; i < values.length; i++) {
//				listModel.add (index++, values[i]);
//			}
			if (c!=ProgressItemTree.this){return;}
//			ProgressItemTree.this.getDropTarget ().get
			final ProgressItem target = (ProgressItem)ProgressItemTree.this.getTree ().getSelectionPath ().getLastPathComponent ();
			if (progressItem==target){
				/* Evita drop su se stesso. */
				return;
			}
			new MoveNode (progressItem, target, -1).execute ();
		}
		
	}
}

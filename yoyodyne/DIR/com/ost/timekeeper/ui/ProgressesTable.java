/*
 * ProgressesTable.java
 *
 * Created on 2 maggio 2004, 8.41
 */

package com.ost.timekeeper.ui;

import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;

/**
 *
 * @author  davide
 */
public class ProgressesTable extends javax.swing.JTable implements TreeSelectionListener, TreeModelListener, Observer{
	
	private TreePath currentPath;
	private TableSorter sorter;
	/** Creates a new instance of ProgressesTable */
	public ProgressesTable() {
		super();
		this.sorter = new TableSorter(new ProgressTableModel(null));
		setModel(this.sorter);
		setMaximumSize(null);
		setMinimumSize(null);
		setAutoCreateColumnsFromModel(true);
		
		DefaultTableCellRenderer dateColumnRenderer = new DefaultTableCellRenderer() {
	    public void setValue(Object value) {
	        setText(com.ost.timekeeper.util.CalendarUtils.toTSString((Date)value));
	    }
        };
		this.setDefaultRenderer(java.util.Date.class, dateColumnRenderer);
	}
	
//	public void setModel (TableModel model){
//		if (this.sorter!=null){
//			this.sorter.setModel (model);
//		} else {
//			//permette inizializzazione
//			super.setModel (model);
//		}
//	}
	
	public void reloadModel(TreePath newPath){
		if (this.currentPath!=newPath){
			this.currentPath = newPath;
			ProgressItem root = null;
			if (this.currentPath!=null){
				root = (ProgressItem)this.currentPath.getLastPathComponent();
			}
			reloadModel (root);
		}
	}
	
	public void reloadModel(ProgressItem root){
		this.setModel(new ProgressTableModel(root));
	}	
	public void valueChanged(TreeSelectionEvent e){
		 if (e.getNewLeadSelectionPath() == null) {
			 //gestione caso cancellazione elemento selezionato
			 return;
		 }
		this.reloadModel(e.getPath());
	}
	
		
	
	private final boolean checkForReloadNeed (TreeModelEvent e){
		TreePath path = e.getTreePath ();
		return path.isDescendant(this.currentPath);
	}
	private final void reloadIfNeeded (TreeModelEvent e){
		if (checkForReloadNeed (e)){
			this.reloadModel(e.getTreePath ());
		}
	}
	
	public void treeNodesChanged(TreeModelEvent e){
		reloadIfNeeded(e);
	}
	public void treeNodesInserted(TreeModelEvent e){
		reloadIfNeeded(e);
	}
	public void treeNodesRemoved(TreeModelEvent e){
		reloadIfNeeded(e);
	}
	public void treeStructureChanged(TreeModelEvent e){
		reloadIfNeeded(e);
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals ("currentitem")){
				this.reloadModel(((Application)o).getSelectedItem());
			}
		}
	}
	
}

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
public class ProgressesTable extends javax.swing.JTable implements TreeSelectionListener, TreeModelListener{
	
	private TreePath currentPath;
	private TableSorter sorter;
	/** Creates a new instance of ProgressesTable */
	public ProgressesTable() {
		super();
		this.sorter = new TableSorter(new ProgressTableModel(new ArrayList()));
		setModel(this.sorter);
		setMaximumSize(null);
		setMinimumSize(null);
		setAutoCreateColumnsFromModel(true);
		
		DefaultTableCellRenderer dateColumnRenderer = new DefaultTableCellRenderer() {
	    public void setValue(Object value) {
	        setText(com.ost.timekeeper.util.CalendarUtils.toTSString((Calendar)value));
	    }
        };
		this.setDefaultRenderer(java.util.Calendar.class, dateColumnRenderer);
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
			List subTreeProgresses = null;
			if (this.currentPath!=null){
				root = (ProgressItem)this.currentPath.getLastPathComponent();
			}
			subTreeProgresses = root.getSubtreeProgresses();
			this.setModel(new ProgressTableModel(subTreeProgresses));
		}
	}
	
	public void valueChanged(TreeSelectionEvent e){
		 if (e.getNewLeadSelectionPath() == null) {
			 //gestione caso cancellazione elemento selezionato
			 return;
		 }
		this.reloadModel(e.getPath());
	}
	
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
		
	private final class ProgressTableModel extends javax.swing.table.AbstractTableModel {
		private List progresses;
		private Object[] columns;
		
		public ProgressTableModel(final List progresses){
			this.columns = new Object[]{
				ResourceSupplier.getString(ResourceClass.UI, "components", "progress.startdate"),
				ResourceSupplier.getString(ResourceClass.UI, "components", "progress.finishdate"),
				ResourceSupplier.getString(ResourceClass.UI, "components", "progress.duration"),
				ResourceSupplier.getString(ResourceClass.UI, "components", "progress.running"),
			};
			this.progresses = progresses;
		}
		
		public int getColumnCount() {
			return columns.length;
		}
		
		public int getRowCount() {
			return progresses.size();
		}
		
		private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			Period period = (Period)progresses.get(rowIndex);
			switch (columnIndex){
				case 0: return period.getFrom();
				case 1: return period.getTo();
				case 2: 
					Duration duration = period.getDuration ();
					StringBuffer sb = new StringBuffer ();
					/*
					sb.append (durationNumberFormatter.format(duration.getDays()))
					.append (":")
					 */
					sb.append (durationNumberFormatter.format(duration.getHours()))
					.append (":")
					.append (durationNumberFormatter.format(duration.getMinutes()))
					.append (":")
					.append (durationNumberFormatter.format(duration.getSeconds()));
					return sb.toString ();
				case 3: return new Boolean(period.getTo()==null);
				default: return null;
			}
		}
		
		public String getColumnName(int column) {return (String)columns[column];}
		public Class getColumnClass(int c) {
			Object firstRowCell = getValueAt(0, c);
			if (firstRowCell!=null){
				return firstRowCell.getClass();
			} else {
				return Object.class;
			}
		}
		public boolean isCellEditable(int row, int col) {return false;}
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
}

/*
 * SubtreeProgressesTable.java
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
import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;
import java.awt.event.*;

/**
 * Componente grafica per la gestione della vista delgi avanzamenti relativi ad un sottoalbero.
 *
 * @author  davide
 */
public class SubtreeProgressesTable extends javax.swing.JTable implements TreeSelectionListener, TreeModelListener, Observer{
	
	/**
	 * Il percorso determinato dalla selezione corrente nell'albero.
	 */
	private TreePath currentPath;
	
	/** 
	 * Costruttore vuoto. 
	 */
	public SubtreeProgressesTable() {
		super();
		setModel(new ProgressTableModel(null));
		
		// Use a scrollbar, in case there are many columns.
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		setMaximumSize(null);
		setMinimumSize(null);
		setAutoCreateColumnsFromModel(true);
		
		DefaultTableCellRenderer dateColumnRenderer = new DefaultTableCellRenderer() {
			public void setValue(Object value) {
				setText(com.ost.timekeeper.util.CalendarUtils.toTSString((Date)value));
			}
		};
		this.setDefaultRenderer(java.util.Date.class, dateColumnRenderer);
		
		this.addMouseListener (new MouseAdapter (){
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount ()>1){
					final ProgressStopAction stopAction = ActionPool.getInstance ().getProgressStopAction ();
					if (stopAction.isEnabled ()){
						stopAction.actionPerformed (new ActionEvent (this, 0, null));
					}
				}
			}
		});
	}
	
	/**
	 * Componente delegata alla gestione dell'ordinamento nella tabella.
	 */
	private ProgressTableSorter dataModel;
	private class ProgressTableSorter extends TableSorter {
		private ProgressTableModel progressTableModel;
		public ProgressTableSorter (ProgressTableModel progressTableModel){
			super ();
			load (progressTableModel);
		}
		
		public void load (ProgressTableModel progressTableModel){
			super.setModel (progressTableModel);
			Application.getInstance().deleteObserver(this.progressTableModel);
			this.progressTableModel=progressTableModel;
		}
		
		public void fireCurrentPeriodUpdated (){
			this.fireTableChanged(new TableModelEvent (this, this.progressTableModel.getCurrentPeriodIndex()));
		}
		
		public final int getCurrentPeriodIndex (){
			return this.progressTableModel.getCurrentPeriodIndex();
		}
		
		public void sort(Object sender) {
			super.sort(sender);
			//sincronizza indice avanzamento corrente, dopo riordino
			this.progressTableModel.synchCurrentPeriodIdx();
			Application.getInstance().setChanged();
			Application.getInstance().notifyObservers(ObserverCodes.ITEMPROGRESSINGPERIODCHANGE);
		}
		
	}
	
	private ProgressTableModel progressTableModel;
	public void setModel(ProgressTableModel dataModel) {
		if (this.dataModel!=null){
			this.dataModel.removeTableModelListener(this);
		}
		this.progressTableModel=dataModel;
		this.dataModel = new ProgressTableSorter(dataModel);
		// Install a mouse listener in the TableHeader as the sorter UI.
		this.dataModel.addMouseListenerToHeaderInTable(this);
		super.setModel(this.dataModel);
	}
	
	public ProgressTableModel getProgressTableModel (){
		return this.progressTableModel;
	}
	
	public void reloadModel(TreePath newPath){
		if (this.currentPath!=newPath){
			this.currentPath = newPath;
			ProgressItem root = null;
			if (this.currentPath!=null){
				root = (ProgressItem)this.currentPath.getLastPathComponent();
			}
			reloadModel(root);
		}
	}
	
	/**
	 * Ricarica il modello su di un sottoalbero target.
	 *
	 * @param root la radice del sottoalbero da ricaricare.
	 */
	public void reloadModel(ProgressItem root){
		/*
		 * Il tableMOdel effettivo può essere di due tipi:
		 * il wrapper per l'ordinamento (ProgressTableSorter)
		 * oppure l'originale ProgressTableModel.
		 */
		TableModel actualTableModel = this.getModel();
		if (actualTableModel instanceof ProgressTableModel){
			((ProgressTableModel)actualTableModel).load(root);
		} else if (actualTableModel instanceof ProgressTableSorter){
			((ProgressTableSorter)actualTableModel).progressTableModel.load(root);
			((ProgressTableSorter)actualTableModel).load(((ProgressTableSorter)actualTableModel).progressTableModel);
		}
	}
	
	public void valueChanged(TreeSelectionEvent e){
	}
	
	
	
	private final boolean checkForReloadNeed(TreeModelEvent e){
		TreePath path = e.getTreePath();
		return path.isDescendant(this.currentPath);
	}
	private final void reloadIfNeeded(TreeModelEvent e){
		if (checkForReloadNeed(e)){
			this.reloadModel(e.getTreePath());
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
			if (arg!=null && arg.equals(ObserverCodes.SELECTEDITEMCHANGE)){
				this.reloadModel(((Application)o).getSelectedItem());
			} else if (arg!=null && arg.equals(ObserverCodes.ITEMPROGRESSINGCHANGE)){
				//				this.reloadModel(((Application)o).getSelectedItem());
				this.dataModel.fireTableChanged(new TableModelEvent(getModel(), this.dataModel.getCurrentPeriodIndex()));
			}
		}
	}
	
}

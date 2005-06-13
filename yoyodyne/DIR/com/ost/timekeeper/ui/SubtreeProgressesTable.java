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
import com.ost.timekeeper.actions.commands.MoveProgress;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.persistence.PersistenceStorage;
import com.ost.timekeeper.ui.persistence.PersistenceUtils;
import com.ost.timekeeper.ui.persistence.PersistentComponent;
import com.ost.timekeeper.ui.persistence.UIPersister;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;

/**
 * Componente grafica per la gestione della vista delgi avanzamenti relativi ad un sottoalbero.
 *
 * @author  davide
 */
public class SubtreeProgressesTable extends javax.swing.JTable implements TreeSelectionListener, TreeModelListener, Observer, PersistentComponent{
	
	/**
	 * Il percorso determinato dalla selezione corrente nell'albero.
	 */
	private TreePath currentPath;
	
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	
	private final ImageIcon runningIcon = ResourceSupplier.getImageIcon (ResourceClass.UI, "running.gif");
	private final ImageIcon staticIcon = ResourceSupplier.getImageIcon (ResourceClass.UI, "stopped.gif");
	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();
	/**
	 * Costruttore vuoto.
	 */
	public SubtreeProgressesTable () {
		super ();
		setModel (new ProgressTableModel (null));
		
		// Use a scrollbar, in case there are many columns.
		this.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);
		
		setMaximumSize (null);
		setMinimumSize (null);
		setAutoCreateColumnsFromModel (true);
		
		final TableCellRenderer durationColumnRenderer = new DefaultTableCellRenderer () {
			public Component getTableCellRendererComponent (final JTable table, final Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {
				
				final Component res = super.getTableCellRendererComponent ( table, value, isSelected, hasFocus, row, column);
				final Progress progress = (Progress)value;
				
				final boolean progressing = progress.getTo ()==null;
				final ImageIcon icon = progressing?runningIcon:staticIcon;
				setIcon (icon);
				
				if (progressing){
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
				}
				
				return res;
			}
			public void setValue (Object value) {
				
				final Progress progress = (Progress)value;
				final Duration duration = progress.getDuration ();
				final StringBuffer sb = new StringBuffer ();

//				final long days = duration.getDays();
//				
//				if (0==days){
//					sb.append ("__");
//				} else {
//					sb.append (durationNumberFormatter.format(duration.getDays()));
//				}
//				sb.append (" - ");
				
				sb.append (durationNumberFormatter.format (duration.getTotalHours ()))
				.append (":")
				.append (durationNumberFormatter.format (duration.getMinutes ()))
				.append (":")
				.append (durationNumberFormatter.format (duration.getSeconds ()));
				
				setText (sb.toString ());
			}
		};
		this.setDefaultRenderer (Progress.class, durationColumnRenderer);
		
		
		DefaultTableCellRenderer dateColumnRenderer = new DefaultTableCellRenderer () {
			public void setValue (Object value) {
				setText (com.ost.timekeeper.util.CalendarUtils.getTimestamp ((Date)value, "MM/dd HH:mm"));
			}
		};
		this.setDefaultRenderer (java.util.Date.class, dateColumnRenderer);
		
		TableCellRenderer nodeColumnRenderer = new DefaultTableCellRenderer () {
			public void setValue (Object value) {
				final ProgressItem node = (ProgressItem)value;
				final StringBuffer sb = new StringBuffer ();
				
				final String code = node.getCode ();
				if (code!=null && code.length ()>0){
					sb.append (code).append (" - ");
				}
				sb.append (node.getName ());
				setText (sb.toString ());
			}
		};
		this.setDefaultRenderer (java.util.Date.class, dateColumnRenderer);
		
		this.addMouseListener (new MouseAdapter (){
			public void mouseClicked (MouseEvent e) {
				if (e.getClickCount ()>1){
					//					final ProgressStopAction stopAction = ActionPool.getInstance ().getProgressStopAction ();
					//					if (stopAction.isEnabled ()){
					//						stopAction.actionPerformed (new ActionEvent (this, 0, null));
					//					}
				}
			}
		});
		
		this.addMouseListener (new MouseAdapter (){
			public void mouseClicked (MouseEvent e) {
				if (e.getClickCount ()>1){
					/*
					 * Almeno doppio click.
					 */
					Desktop.getInstance ().bringToTop (ProgressInspectorFrame.getInstance ());
				}
			}
		});
		this.setAutoscrolls (true);
		
		this.setShowHorizontalLines (false);
		
		this.setTransferHandler (new ProgressTableTransferHandler ());
		this.setImageObserver ();
		
		final boolean inited = UIPersister.getInstance ().register (this, true);
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
			Application.getInstance ().deleteObserver (this.progressTableModel);
			this.progressTableModel=progressTableModel;
		}
		
		public void fireCurrentPeriodUpdated (){
			this.fireTableChanged (new TableModelEvent (this, this.progressTableModel.getCurrentPeriodIndex ()));
		}
		
		public final int getCurrentPeriodIndex (){
			return this.progressTableModel.getCurrentPeriodIndex ();
		}
		
		public void sort (Object sender) {
			super.sort (sender);
			//sincronizza indice avanzamento corrente, dopo riordino
			this.progressTableModel.synchCurrentPeriodIdx ();
			Application.getInstance ().setChanged ();
			Application.getInstance ().notifyObservers (ObserverCodes.CURRENT_PROGRESS_TIC);
		}
		
		public ProgressTableModel getProgressTableModel (){
			return (ProgressTableModel)this.getModel ();
		}
	}
	
	private ProgressTableModel progressTableModel;
	public void setModel (ProgressTableModel dataModel) {
		if (this.dataModel!=null){
			this.dataModel.removeTableModelListener (this);
		}
		this.progressTableModel=dataModel;
		this.dataModel = new ProgressTableSorter (dataModel);
		// Install a mouse listener in the TableHeader as the sorter UI.
		this.dataModel.addMouseListenerToHeaderInTable (this);
		super.setModel (this.dataModel);
		setImageObserver ();
	}
	
	public ProgressTableModel getProgressTableModel (){
		return this.progressTableModel;
	}
	
	public void reloadModel (TreePath newPath){
		if (this.currentPath!=newPath){
			this.currentPath = newPath;
			ProgressItem root = null;
			if (this.currentPath!=null){
				root = (ProgressItem)this.currentPath.getLastPathComponent ();
			}
			reloadModel (root);
		}
	}
	
	/**
	 * Ricarica il modello su di un sottoalbero target.
	 *
	 * @param root la radice del sottoalbero da ricaricare.
	 */
	public void reloadModel (ProgressItem root){
		/*
		 * Il tableMOdel effettivo pu? essere di due tipi:
		 * il wrapper per l'ordinamento (ProgressTableSorter)
		 * oppure l'originale ProgressTableModel.
		 */
		TableModel actualTableModel = this.getModel ();
		if (actualTableModel instanceof ProgressTableModel){
			((ProgressTableModel)actualTableModel).load (root);
		} else if (actualTableModel instanceof ProgressTableSorter){
			((ProgressTableSorter)actualTableModel).progressTableModel.load (root);
			((ProgressTableSorter)actualTableModel).load (((ProgressTableSorter)actualTableModel).progressTableModel);
		}
		setImageObserver ();
	}
	
	public void valueChanged (TreeSelectionEvent e){
	}
	
	
	
	private final boolean checkForReloadNeed (TreeModelEvent e){
		TreePath path = e.getTreePath ();
		return path.isDescendant (this.currentPath);
	}
	private final void reloadIfNeeded (TreeModelEvent e){
		if (checkForReloadNeed (e)){
			this.reloadModel (e.getTreePath ());
		}
	}
	
	public void treeNodesChanged (TreeModelEvent e){
		reloadIfNeeded (e);
	}
	public void treeNodesInserted (TreeModelEvent e){
		reloadIfNeeded (e);
	}
	public void treeNodesRemoved (TreeModelEvent e){
		reloadIfNeeded (e);
	}
	public void treeStructureChanged (TreeModelEvent e){
		reloadIfNeeded (e);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.SELECTEDITEMCHANGE)){
				this.reloadModel (((Application)o).getSelectedItem ());
			} else if (arg!=null && (arg.equals (ObserverCodes.CURRENT_PROGRESS_TIC) || arg.equals (ObserverCodes.SELECTEDNODE_INTERNALCHANGE))){
				//				this.reloadModel(((Application)o).getSelectedItem());
				this.dataModel.fireTableChanged (new TableModelEvent (getModel (), this.dataModel.getCurrentPeriodIndex ()));
			}
		}
	}
	
	private final class ProgressTableTransferHandler extends ProgressTransferHandler{
		
		protected void cleanup (JComponent c, boolean remove) {
		}
		
		protected com.ost.timekeeper.model.Progress[] exportProgresses (JComponent c) {
			if (c!=SubtreeProgressesTable.this){
				return null;
			}
			final int[] selectedRows = SubtreeProgressesTable.this.getSelectedRows ();
			final int selectedRowsCount = selectedRows.length;
			final Progress[] exportedProgresses = new Progress[selectedRowsCount];
			final java.util.List tableProgresses = SubtreeProgressesTable.this.dataModel.getProgressTableModel ().getProgresses ();
			for (int i=0;i<selectedRowsCount;i++){
				exportedProgresses[i] = (Progress)tableProgresses.get (SubtreeProgressesTable.this.dataModel.getIndex (selectedRows[i]));
			}
			return exportedProgresses;
		}
		
		protected void importProgresses (JComponent c, com.ost.timekeeper.model.Progress[] progresses) {
			if (c!=SubtreeProgressesTable.this){return;}
			final ProgressItem target = Application.getInstance ().getSelectedItem ();
			for (int i=0;i<progresses.length;i++){
				new MoveProgress (progresses[i], target, -1).execute ();
			}
			
			//			final int[] selectedrows = SubtreeProgressesTable.this.getSelectedRows ();
			//			if (selectedrows!=null){
			//				final ProgressItem target = Application.getInstance ().getSelectedItem ();
			//				for (int i=0;i<selectedrows.length;i++){
			//					final Progress selectedPeriod = (Progress)SubtreeProgressesTable.this.getProgressTableModel ().getProgresses ().get (selectedrows[i]);
			//
			//					new MoveProgress (selectedPeriod, target, -1).execute ();
			//				}
			//			}
			
		}
		
	}
	
	private void setImageObserver () {
		TableModel model = getModel ();
		int colCount = model.getColumnCount ();
		int rowCount = model.getRowCount ();
		for (int col = 0; col < colCount; col++) {
			if (ImageIcon.class == model.getColumnClass (col)) {
				for (int row = 0; row < rowCount; row++) {
					ImageIcon icon = (ImageIcon) model.getValueAt (row, col);
					if (icon != null) {
						icon.setImageObserver (new CellImageObserver (this, row,
						col));
					}
				}
			}
		}
	}
	
	class CellImageObserver implements ImageObserver {
		JTable table;
		
		int row;
		
		int col;
		
		CellImageObserver (JTable table, int row, int col) {
			this.table = table;
			this.row = row;
			this.col = col;
		}
		
		public boolean imageUpdate (Image img, int flags, int x, int y, int w,
		int h) {
			if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
				Rectangle rect = table.getCellRect (row, col, false);
				table.repaint (rect);
			}
			//			table.repaint ();
			return (flags & (ALLBITS | ABORT)) == 0;
		}
	}
	
	/**
	 * Ritorna la chiave usata per la persistenza degli attributi di questo componente.
	 *
	 * @return la chiave usata per la persistenza degli attributi di questo componente.
	 */	
	public String getPersistenceKey (){return "subtreeprogressestable";}
	
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

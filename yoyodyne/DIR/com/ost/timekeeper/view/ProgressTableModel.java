/*
 * ProgressTableModel.java
 *
 * Created on 25 aprile 2004, 10.02
 */

package com.ost.timekeeper.view;

import java.util.*;
import java.text.*;

import javax.swing.event.*;
import javax.swing.table.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public final class ProgressTableModel extends AbstractTableModel implements Observer {
	
	private ProgressItem root;
	private int currentPeriodIdx;
	
	/** Crea una nuova istanza di ProgressTableModel */
	public ProgressTableModel(ProgressItem root) {
		System.out.println ("Creating new ProgressTableModel");
		this.currentPeriodIdx=-1;
		this.columns = new Object[]{
			ResourceSupplier.getString(ResourceClass.UI, "components", "progress.startdate"),
			ResourceSupplier.getString(ResourceClass.UI, "components", "progress.finishdate"),
			ResourceSupplier.getString(ResourceClass.UI, "components", "progress.duration"),
			ResourceSupplier.getString(ResourceClass.UI, "components", "progress.running"),
		};		
		load (root);
		//registra interesse per eventi di variazione periodo avanzamento corrente
		Application.getInstance().addObserver(this);
		synchCurrentPeriodIdx ();
	}

	/**
	 * Ricarica il modello deidati.
	 * @param root la radice.
	 */	
	public final void load (ProgressItem root){
		this.root = root;
		reloadProgresses ();
		//valorizza indice avanzamento corrente
		synchCurrentPeriodIdx ();
		this.fireTableChanged(new TableModelEvent (this));
	}
	
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	
	private List progresses;
	private Object[] columns;

	/**
	 * @return
	 */	
	public int getColumnCount() {
		return columns.length;
	}

	/**
	 * @return
	 */	
	public int getRowCount() {
		return progresses.size();
	}

	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();

	/**
	 * @param object
	 * @param rowIndex
	 * @param columnIndex
	 */	
	public void setValueAt(Object object, int rowIndex, int columnIndex) {
	}
	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */	
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

	/**
	 * @param column
	 * @return
	 */	
	public String getColumnName(int column) {return (String)columns[column];}
	/**
	 * @param c
	 * @return
	 */	
	public Class getColumnClass(int c) {
		Object firstRowCell = getValueAt(0, c);
		if (firstRowCell!=null){
			return firstRowCell.getClass();
		} else {
			return Object.class;
		}
	}
	/**
	 * @param row
	 * @param col
	 * @return
	 */	
	public boolean isCellEditable(int row, int col) {return false;}
	
	/**
	 * @return
	 */	
	public int getCurrentPeriodIndex (){return this.currentPeriodIdx;}
	
	public void fireCurrentPeriodUpdated (){
		this.fireTableChanged(new TableModelEvent (this, this.currentPeriodIdx));
	}
	
	/**
	 * Ritorna la lista di avanzamenti.
	 * @return la lista di avanzamenti.
	 */	
	public final List getProgresses(){
		return this.progresses;
	}
	
	/**
	 * Ricalcola la lista di avanzamenti del modello.
	 */
	public final void reloadProgresses (){
		if (root!=null){
			this.progresses = root.getSubtreeProgresses ();
		} else {
			this.progresses = new ArrayList ();
		}
		synchCurrentPeriodIdx ();
	}
	
	/**
	 * Ricalcola l'indice relativo al periodo di avanzamento corrnte (attualmente in avanzamento).
	 */
	public final void synchCurrentPeriodIdx (){
		//valorizza indice avanzamento corrente
		Application app = Application.getInstance();
		if (app.getCurrentItem()!=null){
			List progresses = this.getProgresses ();
			for (int i=0;i<progresses.size();i++){
				Period p = (Period)progresses.get (i);
				if (p==app.getCurrentItem().getCurrentProgress()){
					System.out.println ("Changing current period idxfrom: "+currentPeriodIdx+" to:"+i+"\nstacktrace: \n"+ExceptionUtils.getStackStrace(new Throwable ()));
					this.currentPeriodIdx=i;
					break;
				}
			}
		}
	}
	
	/**
	 * Gestione notifica eventi di interesse.
	 * @param o la sorgente dell'evento.
	 * @param arg l'argomento.
	 */	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals(ObserverCodes.ITEMPROGRESSINGPERIODCHANGE)){
//				reloadProgresses ();
				synchCurrentPeriodIdx ();
				this.fireTableChanged(new TableModelEvent(this/*, this.getCurrentPeriodIndex()*/));
				System.out.println (this.hashCode()+" current period Idx: "+this.getCurrentPeriodIndex()+"\nstacktrace: \n"+ExceptionUtils.getStackStrace(new Throwable ()));
			}
		}
	}
	
}

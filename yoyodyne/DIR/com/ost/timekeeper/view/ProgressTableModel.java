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
 * La tabella di visualizzazione degli avanzamenticontenente degli avanzamenti.
 *
 * @author  davide
 */
public final class ProgressTableModel extends AbstractTableModel implements Observer {
	
	/**
	 * Tipo di lista degli avanzamenti (LOCALE o SOTTOALBERO).
	 */
	private ProgressListType _progressListType = ProgressListType.LOCAL;
	
	private ProgressItem root;
	private int currentPeriodIdx;
	
	/** 
	 * Costruttore con nodo radice.
	 *
	 * @param root il nodo radice.
	 */
	public ProgressTableModel(ProgressItem root) {
		System.out.println ("Creating new ProgressTableModel");
		this.currentPeriodIdx=-1;
		this.columns = new Object[]{
			ResourceSupplier.getString(ResourceClass.UI, "controls", "from"),
			ResourceSupplier.getString(ResourceClass.UI, "controls", "to"),
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
	 * Ritorna il numero di colonne di questa tabella.
	 * @return il numero di colonne.
	 */	
	public int getColumnCount() {
		return columns.length;
	}

	/**
	 * Ritorna il numero di righe di questa tabella.
	 * @return il numero di righe.
	 */	
	public int getRowCount() {
		return progresses.size();
	}

	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();

	/**
	 * Imposta il valore di una cella.
	 *
	 * @param object il nuovo valore.
	 * @param rowIndex l'indice di riga.
	 * @param columnIndex l'indice di colonna.
	 */	
	public void setValueAt(Object object, int rowIndex, int columnIndex) {
	}
	/**
	 * Ritorna il valore di una cella.
	 *
	 * @param rowIndex l'indice di riga.
	 * @param columnIndex l'indice di colonna.
	 * @return il valore associato alla cella di riga <TT>rowIndex</TT> e colonna <TT>columnIndex</TT>.
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
	 * Ritorna il nome di una colonna.
	 *
	 * @param column l'indice di colonna.
	 * @return il nome della colonna avente indice <TT>column</TT>.
	 */	
	public String getColumnName(int column) {return (String)columns[column];}
	/**
	 * Ritorna il tipo di una colonna.
	 *
	 * @param c l'indice di colonna.
	 * @return la classe che rappresenta il tipo dei valori contenuti nella colonna con indice <TT>c</TT>.
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
	 * Ritorna lo stato di modificabilità di una cella.
	 *
	 * @param row l'indice della riga.
	 * @param col l'indice della colonna.
	 * @return la modificabilitò della cella alle coordinate <TT>row</TT, <TT>col</TT>.
	 */	
	public boolean isCellEditable(int row, int col) {return false;}
	
	/**
	 * Ritorna l'indice del perdiodo di avanzamento corrente.
	 *
	 * @return l'indice del perdiodo di avanzamento corrente.
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
			if (this._progressListType==ProgressListType.LOCAL){
				/*
				 * Lista avanzamenti locali al nodo.
				 */
				this.progresses = root.getProgresses ();
			} else {
				/*
				 * Lista avanzamenti del sottoalbero.
				 */
				this.progresses = root.getSubtreeProgresses ();
			}
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
//					System.out.println ("Changing current period idxfrom: "+currentPeriodIdx+" to:"+i+"\nstacktrace: \n"+ExceptionUtils.getStackStrace(new Throwable ()));
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
//				System.out.println (this.hashCode()+" current period Idx: "+this.getCurrentPeriodIndex()+"\nstacktrace: \n"+ExceptionUtils.getStackStrace(new Throwable ()));
			}
		}
	}
	
	/**
	 * Imposta il tipo di lista di questo modello.
	 *
	 * @param progressListType il tipo di lista.
	 */	
	public void setProgressListType (final ProgressListType progressListType){
		if (progressListType==null){
			return;
		}
		this._progressListType = progressListType;
		this.load (this.root);
	}
}

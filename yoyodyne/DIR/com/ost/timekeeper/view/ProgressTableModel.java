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

import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public class ProgressTableModel extends AbstractTableModel {
	
	private ProgressItem root;
	
	/** Crea una nuova istanza di ProgressTableModel */
	public ProgressTableModel(ProgressItem root) {
		this.columns = new Object[]{
			ResourceSupplier.getString(ResourceClass.UI, "components", "progress.startdate"),
			ResourceSupplier.getString(ResourceClass.UI, "components", "progress.finishdate"),
			ResourceSupplier.getString(ResourceClass.UI, "components", "progress.duration"),
			ResourceSupplier.getString(ResourceClass.UI, "components", "progress.running"),
		};		
		this.root = root;
		if (root!=null){
			this.progresses = root.getSubtreeProgresses ();
		} else {
			this.progresses = new ArrayList ();
		}
	}

	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	
	private List progresses;
	private Object[] columns;

	public int getColumnCount() {
		return columns.length;
	}

	public int getRowCount() {
		return progresses.size();
	}

	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();

	public void setValueAt(Object object, int rowIndex, int columnIndex) {
	}
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

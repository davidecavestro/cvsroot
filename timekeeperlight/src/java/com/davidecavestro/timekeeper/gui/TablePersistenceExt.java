/*
 * TablePersistenceExt.java
 *
 * Created on November 3, 2007, 7:06 PM
 *
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.gui.persistence.PersistenceStorage;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.common.util.StringUtils;
import com.davidecavestro.common.util.settings.SettingsSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * Supporto alla persistenza dellostato di visualizzazione e ordinamento colonne per una tabella.
 *
 * @author Davide Cavestro
 */
public abstract class TablePersistenceExt implements PersistentComponent {
	
	private final JXTable _table;
	
	/**
	 * Costruttore.
	 * 
	 * @param table la tabella da estendere
	 */
	public TablePersistenceExt (final JXTable table) {
		_table = table;
	}
	
	
	public void makePersistent (PersistenceStorage props) {
		final List<String> s1 = new ArrayList<String> ();
		final List<String> s2 = new ArrayList<String> ();

		for (int i = 0; i< _table.getColumnCount (false); i++) {
			final TableColumnExt tce = _table.getColumnExt (i);
			final int columnIndex = _table.convertColumnIndexToModel (i);
			
			if (tce.isVisible ()) {
				s1.add (Integer.toString (columnIndex));
			}
			final SortOrder so = _table.getSortOrder (i);
			if (so!=null && so!=SortOrder.UNSORTED) {
				s2.add (Integer.toString (toColumnSortIndex (columnIndex, so)));
			}
		}
		SettingsSupport.setStringProperty (props.getRegistry (), getVisibleColumnsPersistenceKey (), StringUtils.toCSV (s1.toArray ()));
		//_table.getSortedColumn ()
		
		SettingsSupport.setStringProperty (props.getRegistry (), getSortedColumnsPersistenceKey (), StringUtils.toCSV (s2.toArray ()));
	}


	public boolean restorePersistent (PersistenceStorage props) {
		final String s1 = SettingsSupport.getStringProperty (props.getRegistry (), getVisibleColumnsPersistenceKey ());
//		if (s==null) {
//			return false;
//		}
//				final String[] values = s.split (";");
		final String s2 = SettingsSupport.getStringProperty (props.getRegistry (), getSortedColumnsPersistenceKey ());
		
		final Set<String> values1 = new HashSet<String> ();
		if (s1!=null) {
			values1.addAll (Arrays.asList (s1.split (",")));
		}
		
		final Set<String> values2 = new HashSet<String> ();
		if (s2!=null) {
			values2.addAll (Arrays.asList (s2.split (",")));
		}
		if (!values1.isEmpty () && s1!=null && s1.length ()>0) {
			/*
			 * Se non e' impostata alcuna colonna come visibile, le lascia tutte visualizzate
			 */
			for (int i = _table.getColumnCount (false)-1; i>=0; i--) {
				final TableColumnExt tce = _table.getColumnExt (i);
				tce.setVisible (values1.contains (Integer.toString (_table.convertColumnIndexToModel (i))));
			}
		}
		
		for (final String s : values2) {
			if (s!=null && s.length ()>0) {
				setColumnSort (Integer.parseInt (s));
			}
		}
		
		return true;
	}
	
	private String getVisibleColumnsPersistenceKey () {
		return getPersistenceKey () + ".visible.columns";
	}
	
	private String getSortedColumnsPersistenceKey () {
		return getPersistenceKey () + ".sorted.columns";
	}
	
	/*
	 * Ritorna l'indice codificato relativo che specifica la posizione e l'ordine applicatosulla colonna.
	 * Chiamare questo metodo solo per indici di modello relativi a colonne con ordinamento impostato
	 */
	private int toColumnSortIndex (final int columnModelIndex, final SortOrder sortOrder) {
		return (columnModelIndex + 1) * (sortOrder.isAscending ()?1:-1);
	}
	
	private void setColumnSort (final int columnSortIndex) {
		//_table.getColumnExt (Math.abs (columnSortIndex) - 1).setSortable (true);
		_table.setSortOrder (Math.abs (columnSortIndex) - 1, columnSortIndex>0?SortOrder.ASCENDING:SortOrder.DESCENDING);
	}
}

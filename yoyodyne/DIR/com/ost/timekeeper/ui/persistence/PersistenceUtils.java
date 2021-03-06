/*
 * PersistenceUtils.java
 *
 * Created on 23 maggio 2005, 23.42
 */

package com.ost.timekeeper.ui.persistence;

import com.ost.timekeeper.conf.SettingsSupport;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Metodi di utilita' per la gestione dello stato eprsistente.
 *
 * @author  davide
 */
public class PersistenceUtils {
	
	/** Costruttore privato */
	private PersistenceUtils () {
	}
	
	/**
	 * Rende persistenti i dati relativialle dimensioni e poisizione del componente specificato.
	 *
	 * @param persistenceStorage ilcontenitore dei dati persistenti.
	 * @param key la chiave per la persistenza.
	 * @param component il componente.
	 */	
	public final static void makeBoundsPersistent (final PersistenceStorage persistenceStorage, final String key, final Component component ){
		SettingsSupport.setRectangle (
			persistenceStorage.getRegistry (), 
			component.getBounds (),
			key+"xpos",
			key+"ypos",
			key+"width",
			key+"height");
	}
	
	/**
	 * Ripristina le dimensioni persistenti per il componente specificato, identificato dalla chiave specificata.
	 *
	 * @param persistenceStorage il contenitore dei dati persistenti.
	 * @param key la chiave.
	 * @param component il componente.
	 * @return <TT>true</TT> se sono stati recuperati e ripristinati i dati persistenti.
	 */	
	public final static boolean restorePersistentBounds (final PersistenceStorage persistenceStorage, final String key, final Component component ){
		final Rectangle bounds =  SettingsSupport.getRectangle (
			persistenceStorage.getRegistry (), 
			key+"xpos",
			key+"ypos",
			key+"width",
			key+"height");
		if (bounds!=null){
			component.setBounds (bounds);
			return true;
		}
		return false;
	}
	
	
	public final static void makeColumnWidthsPersistent (final PersistenceStorage persistenceStorage, final String key, final JTable table ){
		final TableColumnModel columns = table.getColumnModel ();
		int i=0;
		for (final Enumeration en = columns.getColumns ();en.hasMoreElements ();){
			final TableColumn column = (TableColumn)en.nextElement ();
			SettingsSupport.setIntegerProperty (
			persistenceStorage.getRegistry (), 
			key+"colwidth"+i++, 
			new Integer (column.getWidth ()));
		}
	}
	
	public final static void restorePersistentColumnWidths (final PersistenceStorage persistenceStorage, final String key, final JTable table ){
		final TableColumnModel columns = table.getColumnModel ();
		int i=0;
		for (final Enumeration en = columns.getColumns ();en.hasMoreElements ();){
			final TableColumn column = (TableColumn)en.nextElement ();
			final Integer width = SettingsSupport.getIntegerProperty (
			persistenceStorage.getRegistry (), 
			key+"colwidth"+i++);
			
			if (width!=null){
				column.setPreferredWidth (width.intValue ());
			}
		}
	}
	
}

/*
 * DataFlavors.java
 *
 * Created on 28 dicembre 2004, 18.47
 */

package com.davidecavestro.timekeeper.gui.dnd;

import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.Task;
import java.awt.datatransfer.*;

/**
 * Definisce i tipi di dato trasferibili non standard.
 *
 * @author  davide
 */
public final class DataFlavors {
	
	/**
	 * L'identificatore del tipo <TT>ProgressItem</TT>.
	 */
	private final static String progressItemMimeType = DataFlavor.javaJVMLocalObjectMimeType +";class="+Task.class.getName ();
	
	/**
	 * Ritorna il tipo relativo alla classe {@link com.ost.timekeeper.model.ProgressItem}.
	 */
	public final static DataFlavor progressItemFlavor = createProgressItemFlavor ();
	
	/**
	 * L'identificatore del tipo <TT>Progress</TT>.
	 */
	private final static String progressMimeType = DataFlavor.javaJVMLocalObjectMimeType +";class="+PieceOfWork.class.getName ();
	
	/**
	 * Ritorna il tipo relativo alla classe {@link com.ost.timekeeper.model.Progress}.
	 */
	public final static DataFlavor progressFlavor = createProgressFlavor ();
	
	/**
	 * Crea il tipo di dato per <TT>ProgressItem</TT>.
	 */
	private static DataFlavor createProgressItemFlavor () {
		//Try to create a DataFlavor for progress item.
		try {
			return new DataFlavor (progressItemMimeType);
		} catch (ClassNotFoundException cnfe) {
			/** Caso non previsto. */
			throw new RuntimeException (cnfe);
		}
	}
	
	/**
	 * Crea il tipo di dato per <TT>Progress</TT>.
	 */
	private static DataFlavor createProgressFlavor () {
		//Try to create a DataFlavor for progress.
		try {
			return new DataFlavor (progressMimeType);
		} catch (ClassNotFoundException cnfe) {
			/** Caso non previsto. */
			throw new RuntimeException (cnfe);
		}
	}
	
	/** Costruttore privato.*/
	private DataFlavors () {}
}

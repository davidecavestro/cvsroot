/*
 * DataFlavors.java
 *
 * Created on 28 dicembre 2004, 18.47
 */

package com.ost.timekeeper.ui.support;

import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.*;
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
	private final static String progressItemMimeType = DataFlavor.javaJVMLocalObjectMimeType +";class="+ProgressItem.class.getName ();
	
	/**
	 * Ritorna il tipo relativo alla classe {@link com.ost.timekeeper.model.ProgressItem}.
	 */
	public final static DataFlavor progressItemFlavor = createProgressItemFlavor ();
	
	/**
	 * Crea il tipo di dato per <TT>ProgressItem</TT>.
	 */
	private static DataFlavor createProgressItemFlavor () {
		//Try to create a DataFlavor for progress item.
		try {
			return new DataFlavor (progressItemMimeType);
		} catch (ClassNotFoundException cnfe) {
			/** Caso non previsto. */
			throw new NestedRuntimeException (cnfe);
		}
	}
	
	/** Costruttore privato.*/
	private DataFlavors () {}
}

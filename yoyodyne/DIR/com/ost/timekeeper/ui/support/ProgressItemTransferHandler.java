/*
 * ProgressItemTransferHandler.java
 *
 * Created on 28 dicembre 2004, 18.27
 */

package com.ost.timekeeper.ui.support;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;

/**
 * Gestore del trasferimento dei dati di tipo {@link com.ost.timekeeper.model.ProgressItem}.
 *
 * @author  davide
 */
public abstract class ProgressItemTransferHandler extends TransferHandler {
	
	private final DataFlavor progressItemFlavor = DataFlavors.progressItemFlavor;
	
	/**
	 * Costruttore.
	 */
	public ProgressItemTransferHandler () {
	}
	
	protected abstract ProgressItem exportProgressItem (JComponent c);
	protected abstract void importProgressItem (JComponent c, ProgressItem progressItem);
	protected abstract void cleanup (JComponent c, boolean remove);
	
	/**
	 * Crea un oggetto trasferibile.
	 *
	 * @param c l'elemento della UI.
	 * @return un oggetto trasferibile.
	 */	
	protected Transferable createTransferable (JComponent c) {
		return new ProgressItemSelection (exportProgressItem (c));
	}
	
	public int getSourceActions (JComponent c) {
		return COPY_OR_MOVE;
	}
	
	public boolean importData (JComponent c, Transferable t) {
		if (canImport (c, t.getTransferDataFlavors ())) {
			try {
				ProgressItem progressItem = (ProgressItem)t.getTransferData (progressItemFlavor);
				importProgressItem (c, progressItem);
				return true;
			} catch (UnsupportedFlavorException ufe) {
				Application.getLogger ().warning ("Error transferring UI data.", ufe);
			} catch (IOException ioe) {
				Application.getLogger ().warning ("Error transferring UI data.", ioe);
			}
		}
		
		return false;
	}
	
	protected void exportDone (JComponent c, Transferable data, int action) {
		cleanup (c, action == MOVE);
	}
	
    /**
     * Does the flavor list have a ProgressItem flavor?
     */
    protected boolean hasProgressItemFlavor(DataFlavor[] flavors) {
        if (progressItemFlavor == null) {
             return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (progressItemFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
	
	/**
     * Overridden to include a check for a ProgressItem flavor.
     */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return hasProgressItemFlavor(flavors);
    }
	
}

/*
 * ProgressTransferHandler.java
 *
 * Created on 09 marzo 2005, 00.07
 */

package com.ost.timekeeper.ui.support;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;

/**
 * Gestore del trasferimento dei dati di tipo {@link com.ost.timekeeper.model.Progress}.
 *
 * @author  davide
 */
public abstract class ProgressTransferHandler extends TransferHandler {
	
	private final DataFlavor progressFlavor = DataFlavors.progressFlavor;
	
	/**
	 * Costruttore.
	 */
	public ProgressTransferHandler () {
	}
	
	protected abstract Progress exportProgress (JComponent c);
	protected abstract void importProgress (JComponent c, Progress progress);
	protected abstract void cleanup (JComponent c, boolean remove);
	
	/**
	 * Crea un oggetto trasferibile.
	 *
	 * @param c l'elemento della UI.
	 * @return un oggetto trasferibile.
	 */	
	protected Transferable createTransferable (JComponent c) {
		return new ProgressSelection (exportProgress (c));
	}
	
	public int getSourceActions (JComponent c) {
		return MOVE;
	}
	
	public boolean importData (JComponent c, Transferable t) {
		System.out.println ("Importing progress data ");
		if (canImport (c, t.getTransferDataFlavors ())) {
			try {
				final Progress progress = (Progress)t.getTransferData (progressFlavor);
				importProgress (c, progress);
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
     * Does the flavor list have a Progress flavor?
     */
    protected boolean hasProgressFlavor(DataFlavor[] flavors) {
        if (progressFlavor == null) {
             return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (progressFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
	
	/**
     * Overridden to include a check for a Progress flavor.
     */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return hasProgressFlavor(flavors);
    }
	
}

/*
 * ProgressItemTransferHandler.java
 *
 * Created on 28 dicembre 2004, 18.27
 */

package com.davidecavestro.timekeeper.gui.dnd;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.Task;
import com.ost.timekeeper.model.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;

/**
 * Gestore del trasferimento dei dati di tipo {@link com.ost.timekeeper.model.ProgressItem}.
 *
 * @author  davide
 */
public abstract class ProgressItemTransferHandler extends TransferHandler implements PredictiveTransferhandler {
	
	private final DataFlavor progressItemFlavor = DataFlavors.progressItemFlavor;
	
	private final ApplicationContext _context;
	
	/**
	 * Costruttore.
	 */
	public ProgressItemTransferHandler (final ApplicationContext context) {
		_context = context;
	}
	
	protected abstract Task[] exportProgressItems (JComponent c);
	protected abstract void importProgressItems (JComponent c, Task[] progressItem, boolean removeFromSource);
	protected abstract void cleanup (JComponent c, Transferable data, int action);
	
	/**
	 * Crea un oggetto trasferibile.
	 *
	 * @param c l'elemento della UI.
	 * @return un oggetto trasferibile.
	 */	
	@Override
	protected Transferable createTransferable (JComponent c) {
		return new ProgressItemSelection (exportProgressItems (c));
	}
	
	@Override
	public int getSourceActions (JComponent c) {
		return COPY_OR_MOVE;
	}
	
	@Override
	public boolean importData (JComponent c, Transferable t) {
		if (canImport (c, t.getTransferDataFlavors ())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Scavalcato per impostare l'azione di esportazione utilizata.
	 */
	@Override
	protected void exportDone (JComponent c, Transferable data, int action) {
		((MemoTransferable)data).setAction (action);
		cleanup (c, data, action);
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
	@Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return hasProgressItemFlavor(flavors);
    }

}

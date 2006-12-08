/*
 * ProgressTransferHandler.java
 *
 * Created on 09 marzo 2005, 00.07
 */

package com.davidecavestro.timekeeper.gui.dnd;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.PieceOfWork;
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
	
	private final ApplicationContext _context;
	/**
	 * Costruttore.
	 */
	public ProgressTransferHandler (final ApplicationContext context) {
		_context = context;
	}
	
	protected abstract PieceOfWork[] exportProgresses (JComponent c);
	protected abstract void importProgresses (JComponent c, PieceOfWork[] progress);
	protected abstract void cleanup (JComponent c, boolean remove);
	
	/**
	 * Crea un oggetto trasferibile.
	 *
	 * @param c l'elemento della UI.
	 * @return un oggetto trasferibile.
	 */	
	protected Transferable createTransferable (JComponent c) {
		return new ProgressSelection (exportProgresses (c));
	}
	
	public int getSourceActions (JComponent c) {
		return MOVE;
	}
	
	public boolean importData (JComponent c, Transferable t) {
		System.out.println ("Importing progress data ");
		if (canImport (c, t.getTransferDataFlavors ())) {
			try {
				final PieceOfWork[] progresses = (PieceOfWork[])t.getTransferData (progressFlavor);
				importProgresses (c, progresses);
				return true;
			} catch (UnsupportedFlavorException ufe) {
				_context.getLogger ().warning ("Error transferring UI data.", ufe);
			} catch (IOException ioe) {
				_context.getLogger ().warning ("Error transferring UI data.", ioe);
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

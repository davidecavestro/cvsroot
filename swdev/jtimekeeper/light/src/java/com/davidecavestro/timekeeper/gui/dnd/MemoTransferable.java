/*
 * MemoTransferable.java
 *
 * Created on December 10, 2006, 11:05 AM
 *
 */

package com.davidecavestro.timekeeper.gui.dnd;

import java.awt.datatransfer.Transferable;

/**
 * Transferable con l'aggiunta dell'azione utilizzata per il trasferimento.
 *
 * @author Davide Cavestro
 */
public interface MemoTransferable extends Transferable {
	/**
	 * Imposta l'azione usata per il trasferimentodi questo <CODE>Transferable</CODE>.
	 * <P>
	 * I valori ammissibili sono quelli definiti in <CODE>TransferHandler</CODE>.
	 */
	void setAction (int action);
}

/*
 * TransferData.java
 *
 * Created on December 10, 2006, 10:07 AM
 *
 */

package com.davidecavestro.timekeeper.gui.dnd;

import javax.swing.TransferHandler;

/**
 * Wrapper dei dati da trasferire. Contiene anche un descrittore dell'azione di esportazione.
 * <P>
 * L'utilizzo di wuesto wrapper si rende necessariodato che l'azione di import dalla clipboard non mette a disposizione
 * la modalit&agrave; di esportazione precedentemente utilizzata in fase di esportazione.
 * <BR>
 * Nei casi in cui l'effettivo trasferimento dei dati (e quindi anche l'eventuale rimozione effettiva di essi dalla sorgente, in caso di MOVE)
 * debba essere implementato all'atto dell'import, ci si trova impossibilitati a stabilire quale sia stata l'azione originaria (COPY o MOVE?).
 * <BR>
 * La presenza dell'azione in questo wrapper cerca di ovviare a tale problema.
 *
 * @author Davide Cavestro
 */
public class TransferData<T> {
	
	private final T[] _data;
	private int _action;
	/**
	 * Costruttore con azione.
	 * @param data i dati da trasferire.
	 * @param action l'azione utilizzata in fase di export dei dati.
	 */
	public TransferData (final T[] data, final int action) {
		_data = data;
		_action = action;
	}
	
	/**
	 * Costruttore.
	 * <P>
	 * Imposta l'azione {@link TransferHandler#NONE}.
	 *
	 * @param data i dati da trasferire.
	 */
	public TransferData (final T[] data) {
		_data = data;
		_action = TransferHandler.NONE;
	}
	
	public T[] getData () {
		return _data;
	}
	
	public int getAction () {
		return _action;
	}

	/**
	 * Imposta l'azionedi esportazione.
	 * <P>
	 * Questo metodo viene esposto poich&egrave; in fase di creazione del Transferable non &egrave; possibile
	 * accedere all'azione.
	 * 
	 * @param action 
	 * @return 
	 */
	public void setAction (final int action) {
		_action = action;
	}

	public String toString () {
		final StringBuffer sb = new StringBuffer ();
		sb.append ("data ").append (_data).append (" ");
		sb.append ("action ").append (_action);
		return sb.toString ();
	}
}

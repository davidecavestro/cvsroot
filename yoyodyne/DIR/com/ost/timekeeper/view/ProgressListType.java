/*
 * ProgressListType.java
 *
 * Created on 6 dicembre 2004, 21.36
 */

package com.ost.timekeeper.view;

/**
 * Tipo di lista di avanzamenti (locale o sottoalbero).
 *
 * @author  davide
 */
public final class ProgressListType {
	private String _type;
	
	/**
	 * Lista avanzamento locali al nodo di avanzamento.
	 */
	public final static ProgressListType LOCAL = new ProgressListType ("LOCAL");
	/**
	 * Lista avanzamenti appartenenti al sottoalbero avente come radice il nodo di avanzamento.
	 */
	public final static ProgressListType SUBTREE = new ProgressListType ("SUBTREE");
	
	/** Costruttore privato.
	 * @param type
	 */
	private ProgressListType (final String type) {
		this._type=type;
	}
	
	/**
	 * Ritorna un rappresentazione in formato stringa di questo tipo di lista.
	 *
	 * @return una stringa che rappresenta questo tipo di lista.
	 */	
	public String toString (){
		StringBuffer sb = new StringBuffer ();
		sb.append (this._type);
		return sb.toString ();
	}
}

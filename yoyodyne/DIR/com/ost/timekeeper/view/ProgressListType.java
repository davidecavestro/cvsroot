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
	 * Valore del tipo {@link #LOCAL}.
	 */
	public final static String LOCAL_VALUE = "local";
	/**
	 * Valore del tipo {@link #SUBTREE}.
	 */
	public final static String SUBTREE_VALUE = "subtree";
	
	/**
	 * Lista avanzamento locali al nodo di avanzamento.
	 */
	public final static ProgressListType LOCAL = new ProgressListType (LOCAL_VALUE);
	/**
	 * Lista avanzamenti appartenenti al sottoalbero avente come radice il nodo di avanzamento.
	 */
	public final static ProgressListType SUBTREE = new ProgressListType (SUBTREE_VALUE);
	
	/**
	 * Array di tutti i valori ammissibili per questo tipo.
	 */
	private final static ProgressListType[] _values = new ProgressListType[]{LOCAL, SUBTREE};
	
	/** Costruttore privato.
	 * @param type
	 */
	private ProgressListType (final String type) {
		this._type=type;
	}
	
	/**
	 * Ritorna un valore di questo enumerato in funzione della stringa specificata.
	 *
	 * @param type il tipo in formato stringa. 
	 * I possibili valori validi sono:
	 *	<UL>
	 *		<LI>{@link #LOCAL_VALUE};
	 *		<LI>{@link #SUBTREE_VALUE};
	 *	</UL>
	 * @return un valore di questo enumerato in funzione della stringa specificata.
	 */	
	public static ProgressListType getType (final String type) {
		if (type==null){
			return null;
		}
		for (int i=0;i<_values.length;i++){
			final ProgressListType value = _values[i];
			if (type.equals (value.getInternalValue ())){
				return value;
			}
		}
		return null;
	}
	
	/**
	 * Ritorna il valore interno del tipo, in formato stringa.
	 *
	 * @return il valore interno del tipo, in formato stringa.
	 */	
	public String getInternalValue (){
		return this._type;
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

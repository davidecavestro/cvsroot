/*
 * AbstractKey.java
 *
 * Created on 29 dicembre 2004, 11.45
 */

package com.ost.timekeeper.actions.commands.attributes.keys;

/**
 * Implementazione di chiave generica astratta.
 *
 * @author  davide
 */
public class AbstractKey implements Key{
	
	/**
	 * Il valore di questa chiave.
	 */
	private String _key;
	
	/**
	 * Costruttore.
	 * @param key il valore della chiave.
	 */
	public AbstractKey (String key) {
		this._key=key;
	}
	
	/**
	 * Ritorna il valore di questa chiave.
	 *
	 * @return il valore di questa chiave.
	 */	
	public final String getKey () {
		return this._key;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questa <TT>Key</TT>.
	 *
	 * @return una rappresentazione in formato stringa di questa <TT>Key</TT>.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (_key);
		return sb.toString ();
	}
	
	private final static String HASHCODE_SEPARATOR = "@@";
	
	/**
	 * Calcola l'hash code per questa <TT>Key</TT>.
	 *
	 * @return un valore di hash code per questa <TT>Key</TT>.
	 */	
	public int hashCode (){
		final StringBuffer sb = new StringBuffer ();
//		sb.append (this.getClass ().getName ())
//		sb.append (HASHCODE_SEPARATOR)
		sb.append (this._key);
		return sb.toString ().hashCode ();
	}
}

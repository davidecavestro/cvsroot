/*
 * StringAttribute.java
 *
 * Created on 29 dicembre 2004, 11.59
 */

package com.ost.timekeeper.actions.commands.attributes;

import com.ost.timekeeper.actions.commands.attributes.keys.*;

/**
 * Attributo di tipo <TT>String</TT>.
 *
 * @author  davide
 */
public final class StringAttribute extends AbstractAttribute{
	
	/**
	 * La chiave di questo attributo.
	 */
	private StringKey _key;
	
	/**
	 * Il valore di questo attributo.
	 */
	private String _value;
		
	/**
	 * Costruttore con chiave e valore.
	 *
	 * @param key la chiave.
	 * @param value il valore.
	 */
	public StringAttribute (final StringKey key,final String value) {
		super (key);
		this._key = key;
		this._value = value;
	}
	
	/**
	 * Ritorna la chiave di questo <TT>StringAttribute</TT>.
	 *
	 * @return la chiave di questo <TT>StringAttribute</TT>.
	 */	
	public final StringKey getStringKey () {
		return this._key;
	}
	
	/**
	 * Ritorna il valore di questo <TT>StringAttribute</TT>.
	 *
	 * @return il valore di questo <TT>StringAttribute</TT>.
	 */	
	public final String getValue () {
		return this._value;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo <TT>StringAttribute</TT>.
	 *
	 * @return una rappresentazione in formato stringa di questo <TT>StringAttribute</TT>.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("key: ").append (_key);
		sb.append (" value: ").append (_value);
		return sb.toString ();
	}
	
	private final static String HASHCODE_SEPARATOR = "@@";
	
	/**
	 * Calcola l'hash code per questo <TT>StringAttribute</TT>.
	 *
	 * @return un valore di hash code per questo <TT>StringAttribute</TT>.
	 */	
	public int hashCode (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (this._key.getKey ());
		sb.append (HASHCODE_SEPARATOR);
		sb.append (this._value);
		return sb.toString ().hashCode ();
	}
}

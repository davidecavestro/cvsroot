/*
 * DoubleAttribute.java
 *
 * Created on 29 dicembre 2004, 12.08
 */

package com.ost.timekeeper.actions.commands.attributes;

import com.ost.timekeeper.actions.commands.attributes.keys.*;

/**
 * Attributo di tipo <TT>Double</TT>.
 *
 * @author  davide
 */
public final class DoubleAttribute  extends AbstractAttribute{
	
	/**
	 * La chiave di questo attributo.
	 */
	private DoubleKey _key;
	
	/**
	 * Il valore di questo attributo.
	 */
	private Double _value;
		
	/**
	 * Costruttore con chiave e valore.
	 *
	 * @param key la chiave.
	 * @param value il valore.
	 */
	public DoubleAttribute (final DoubleKey key, final Double value) {
		super (key);
		this._key = key;
		this._value = value;
	}
	
	/**
	 * Ritorna la chiave di questo <TT>DoubleAttribute</TT>.
	 *
	 * @return la chiave di questo <TT>DoubleAttribute</TT>.
	 */	
	public final DoubleKey getDoubleKey () {
		return this._key;
	}
	
	/**
	 * Ritorna il valore di questo <TT>DoubleAttribute</TT>.
	 *
	 * @return il valore di questo <TT>DoubleAttribute</TT>.
	 */	
	public final Double getValue () {
		return this._value;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo <TT>DoubleAttribute</TT>.
	 *
	 * @return una rappresentazione in formato stringa di questo <TT>DoubleAttribute</TT>.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("key: ").append (_key);
		sb.append (" value: ").append (_value);
		return sb.toString ();
	}
	
	private final static String HASHCODE_SEPARATOR = "@@";
	
	/**
	 * Calcola l'hash code per questo <TT>DoubleAttribute</TT>.
	 *
	 * @return un valore di hash code per questo <TT>DoubleAttribute</TT>.
	 */	
	public int hashCode (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (this._key.getKey ());
		sb.append (HASHCODE_SEPARATOR);
		sb.append (this._value);
		return sb.toString ().hashCode ();
	}
}

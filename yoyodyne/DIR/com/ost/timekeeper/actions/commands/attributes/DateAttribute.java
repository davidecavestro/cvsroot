/*
 * DateAttribute.java
 *
 * Created on 29 dicembre 2004, 12.06
 */

package com.ost.timekeeper.actions.commands.attributes;

import com.ost.timekeeper.actions.commands.attributes.keys.*;
import java.util.*;

/**
 * Attributo di tipo <TT>Date</TT>.
 *
 * @author  davide
 */
public final class DateAttribute extends AbstractAttribute{
	
	/**
	 * La chiave di questo attributo.
	 */
	private DateKey _key;
	
	/**
	 * Il valore di questo attributo.
	 */
	private Date _value;
		
	/**
	 * Costruttore con chiave e valore.
	 *
	 * @param key la chiave.
	 * @param value il valore.
	 */
	public DateAttribute (final DateKey key, final Date value) {
		super (key);
		this._key = key;
		this._value = value;
	}
	
	/**
	 * Ritorna la chiave di questo <TT>DateAttribute</TT>.
	 *
	 * @return la chiave di questo <TT>DateAttribute</TT>.
	 */	
	public final DateKey getDateKey () {
		return this._key;
	}
	
	/**
	 * Ritorna il valore di questo <TT>DateAttribute</TT>.
	 *
	 * @return il valore di questo <TT>DateAttribute</TT>.
	 */	
	public final Date getValue () {
		return this._value;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo <TT>DateAttribute</TT>.
	 *
	 * @return una rappresentazione in formato stringa di questo <TT>DateAttribute</TT>.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("key: ").append (_key);
		sb.append (" value: ").append (_value);
		return sb.toString ();
	}
	
	private final static String HASHCODE_SEPARATOR = "@@";
	
	/**
	 * Calcola l'hash code per questo <TT>DateAttribute</TT>.
	 *
	 * @return un valore di hash code per questo <TT>DateAttribute</TT>.
	 */	
	public int hashCode (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (this._key.getKey ());
		sb.append (HASHCODE_SEPARATOR);
		sb.append (this._value);
		return sb.toString ().hashCode ();
	}
}

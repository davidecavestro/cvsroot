/*
 * AbstractAttribute.java
 *
 * Created on 1 gennaio 2003, 3.03
 */

package com.ost.timekeeper.actions.commands.attributes;

import com.ost.timekeeper.actions.commands.attributes.keys.*;

/**
 * Implementazione di attributo generico, non tipizzato.
 *
 * @author  davide
 */
public abstract class AbstractAttribute implements Attribute{
	
	/**
	 * La chiave di questo attributo.
	 */
	private Key _key;
	
	/**
	 * Costruttore con chiave.
	 *
	 * @param key la chiave.
	 */
	public AbstractAttribute (final Key key) {
		this._key = key;
	}
	
	/**
	 * Ritorna la chiave di questo <TT>Attribute</TT>.
	 *
	 * @return la chiave di questo <TT>Attribute</TT>.
	 */	
	public Key getKey () {
		return this._key;
	}
}

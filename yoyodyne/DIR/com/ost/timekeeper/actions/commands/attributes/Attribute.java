/*
 * Attribute.java
 *
 * Created on 1 gennaio 2003, 3.02
 */

package com.ost.timekeeper.actions.commands.attributes;

import com.ost.timekeeper.actions.commands.attributes.keys.*;

/**
 * Un generico attributo.
 *
 * @author  davide
 */
public interface Attribute {
	/**
	 * Ritorna la chiave per questo attributo.
	 *
	 * @return la chiave.
	 */
	public Key getKey ();
}

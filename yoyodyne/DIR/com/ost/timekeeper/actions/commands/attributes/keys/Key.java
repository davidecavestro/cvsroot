/*
 * Key.java
 *
 * Created on 29 dicembre 2004, 11.43
 */

package com.ost.timekeeper.actions.commands.attributes.keys;

/**
 * La chiave della mappatura attributo=valore.
 *
 * @author  davide
 */
public interface Key {
	/**
	 * Ritorna il valore di questa chiave.
	 *
	 * @return il valore di questa chiave.
	 */	
	public String getKey ();
}

/*
 * DateKey.java
 *
 * Created on 29 dicembre 2004, 11.56
 */

package com.ost.timekeeper.actions.commands.attributes.keys;

/**
 * Chiave per un attributo di tipo <TT>java.util.Date</TT>.
 *
 * @author  davide
 */
public final class DateKey extends AbstractKey{
	
	/**
	 * Costruttore con valore della chiave.
	 *
	 * @param key il valore per la chiave.
	 */
	public DateKey (final String key) {
		super (key);
	}
	
}

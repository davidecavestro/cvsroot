/*
 * AbstractCommand.java
 *
 * Created on 29 dicembre 2004, 12.39
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.actions.commands.attributes.*;

/**
 *
 * @author  davide
 */
public abstract class AbstractCommand implements Command {
	
	/**
	 * Ma mappa degli attributi disponibili per questo comando.
	 */
	private final AttributeMap _attributes = new AttributeMap ();
	
	/**
	 * Costruttore vuoto.
	 */
	public AbstractCommand () {}
	
	/**
	 * Costruttore con attributi.
	 *
	 * @param attributes gli attributi.
	 */
	public AbstractCommand (final Attribute[] attributes) {
		this._attributes.putAttributes (attributes);
	}
	
	/**
	 * Ritorna la mappa degli attributi.
	 *
	 * @return la mappa degli attributi.
	 */	
	protected AttributeMap getAttributeMap (){
		return this._attributes;
	}
	
}

/*
 * Command.java
 *
 * Created on 18 aprile 2004, 14.20
 */

package com.ost.timekeeper.actions.commands;

/**
 * Comando generico.
 *
 * @author  davide
 */
public interface Command {
	
	/**
	 * Esegue questo comando.
	 */
	public void execute ();
}

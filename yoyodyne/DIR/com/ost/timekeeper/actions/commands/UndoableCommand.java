/*
 * UndoableCommand.java
 *
 * Created on 18 aprile 2004, 14.24
 */

package com.ost.timekeeper.actions.commands;

/**
 * Comando reversibile generico.
 *
 * @author  davide
 */
public interface UndoableCommand extends Command {
	
	/**
	 * Ritorna alla situazione precedente all'esecuzione di questo comando.
	 */
	public void unexecute();
}

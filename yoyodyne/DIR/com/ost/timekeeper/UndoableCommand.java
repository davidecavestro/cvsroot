/*
 * UndoableCommand.java
 *
 * Created on 18 aprile 2004, 14.24
 */

package com.ost.timekeeper;

/**
 *
 * @author  davide
 */
public abstract class UndoableCommand extends Command {
	
	/** Creates a new instance of UndoableCommand */
	public UndoableCommand() {
	}
	
	public abstract void unexecute();
}

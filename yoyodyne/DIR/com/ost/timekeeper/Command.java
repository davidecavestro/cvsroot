/*
 * Command.java
 *
 * Created on 18 aprile 2004, 14.20
 */

package com.ost.timekeeper;

/**
 *
 * @author  davide
 */
public abstract class Command {
	
	/** Creates a new instance of Command */
	public Command() {
	}
	
	public abstract void execute ();
}

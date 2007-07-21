/*
 * PersistenceNodeException.java
 *
 * Created on December 23, 2006, 10:51 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.davidecavestro.timekeeper.persistence;

/**
 *
 * @author Davide Cavestro
 */
public class PersistenceNodeException extends java.lang.Exception {
	
	/**
	 * Creates a new instance of <code>PersistenceNodeException</code> without detail message.
	 */
	public PersistenceNodeException () {
	}
	
	
	/**
	 * Constructs an instance of <code>PersistenceNodeException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public PersistenceNodeException (String msg) {
		super (msg);
	}
}

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
 * Eccezione generica da utilizzare incasodi probleminella gestioen dellapersistenza.
 *
 * @author Davide Cavestro
 */
public class PersistenceNodeException extends java.lang.Exception {
	
	/**
	 * Costruttore vuoto.
	 */
	public PersistenceNodeException () {
	}
	
	
	/**
	 * Costrttore con messaggio
	 * @param msg il messaggiodi dettaglio.
	 */
	public PersistenceNodeException (String msg) {
		super (msg);
	}
	
	/**
	 * Costruttore con causa.
	 * 
	 * @param cause la causa.
	 */
	public PersistenceNodeException (final Throwable cause) {
		super (cause);
	}
}

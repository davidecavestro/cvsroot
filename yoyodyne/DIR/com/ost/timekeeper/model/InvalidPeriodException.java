/*
 * InvalidPeriodException.java
 *
 * Created on 4 aprile 2004, 12.14
 */

package com.ost.timekeeper.model;

/**
 * Segnala che un periodo temporale non è valido.
 * UN periodo temporale è valido quando i suoi limiti sono definiti ed il limite
 * inferiore precede quello superiore.
 * @author  davide
 */
public class InvalidPeriodException extends java.lang.IllegalStateException {
	
	/**
	 * Crea una nuova <code>InvalidPeriodException</code> senza messaggi di dettaglio.
	 */
	public InvalidPeriodException() {
	}
	
	
	/**
	 * CCrea una nuova <code>InvalidPeriodException</code> con il messaggio di dettaglio specificato.
	 * @param msg il messaggio di dettaglio.
	 */
	public InvalidPeriodException(String msg) {
		super(msg);
	}
}

/*
 * InvalidPeriodException.java
 *
 * Created on 4 aprile 2004, 12.14
 */

package com.ost.timekeeper.model;

/**
 * Segnala che un periodo temporale non &egrave; valido.
 * UN periodo temporale &egrave; valido quando i suoi limiti sono definiti ed il limite
 * inferiore precede quello superiore.
 *
 * @author  davide
 */
public final class InvalidPeriodException extends java.lang.IllegalStateException {
	
	/**
	 * Costruttore vuoto.
	 */
	public InvalidPeriodException() {
	}
	
	
	/**
	 * Costruttore con il messaggio di dettaglio.
	 *
	 * @param msg il messaggio di dettaglio.
	 */
	public InvalidPeriodException(String msg) {
		super(msg);
	}
	
}

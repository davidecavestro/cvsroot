/*
 * Logger.java
 *
 * Created on 11 dicembre 2004, 19.10
 */

package com.ost.timekeeper.log;

/**
 * Componente per le registrazione di messaggi inerenti allo stato 
 * dell'applicazione ed alle elaborazioni in atto.
 *
 * @author  davide
 */
public interface Logger {
	/**
	 * Registra un messaggio di DEBUG.
	 */
	public void debug (final String message);
	/**
	 * Registra un messaggio di INFORMAZIONE.
	 */
	public void info (final String message);
	/**
	 * Registra un messaggio di AVVISO.
	 */
	public void warning (final String message);
	/**
	 * Registra un messaggio di ERRORE.
	 */
	public void error (final String message);
}

/*
 * ParallelLogger.java
 *
 * Created on 11 dicembre 2004, 20.45
 */

package com.ost.timekeeper.log;

/**
 * Logger composto. Implementa una catena di responsabilità, permettendo 
 * l'utilizo in sequenza di diversi tipi di logger.
 *
 * @author  davide
 */
public final class CompositeLogger implements Logger{
	
	/**
	 * Il logger effettivo, a cui viene delegata la registrazione dei messaggi.
	 */
	private Logger _actualLogger;
	
	/**
	 * Il logger successivo nella catena.
	 */
	private Logger _successor;
	
	/**
	 * Costruttore.
	 * @param actualLogger il logger effettivo,
	 * @param successor il logger successivo nella catena.
	 */
	public CompositeLogger (final Logger actualLogger, final Logger successor) {
		this._actualLogger = actualLogger;
		this._successor = successor;
	}
	
	public void debug (String message) {
		if (this._actualLogger!=null){
			this._actualLogger.debug (message);
		}
		if (this._successor!=null){
			this._successor.debug (message);
		}
	}
	
	public void error (String message) {
		if (this._actualLogger!=null){
			this._actualLogger.error (message);
		}
		if (this._successor!=null){
			this._successor.error (message);
		}
	}
	
	public void info (String message) {
		if (this._actualLogger!=null){
			this._actualLogger.info (message);
		}
		if (this._successor!=null){
			this._successor.info (message);
		}
	}
	
	public void warning (String message) {
		if (this._actualLogger!=null){
			this._actualLogger.warning (message);
		}
		if (this._successor!=null){
			this._successor.warning (message);
		}
	}
	
}

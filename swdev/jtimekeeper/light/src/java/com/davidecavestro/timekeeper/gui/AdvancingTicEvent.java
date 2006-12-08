/*
 * AdvancingTicEvent.java
 *
 * Created on November 30, 2006, 12:19 AM
 *
 */

package com.davidecavestro.timekeeper.gui;

/**
 * Evento di temporizzazione dell'avanzamento.
 *<P>
 *Il suo unico attributo &egrave; il periodo di temporizzazione.
 *
 * @author Davide Cavestro
 */
public class AdvancingTicEvent {
	
	private int _period;
	
	/**
	 * Costruttore.
	 * @param period il periodo di temporizzazione.
	 */
	public AdvancingTicEvent (final int period) {
		_period = period;
	}
	
	/**
	 * Ritorna il periodo di temporizzazione di questo evento.
	 * 
	 * @return il periodo di temporizzazione.
	 */
	public int getPeriod () {
		return _period;
	}
}

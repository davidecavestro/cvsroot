/*
 * StepChangeEvent.java
 *
 * Created on 27 aprile 2005, 22.37
 */

package com.ost.timekeeper.wizard;

/**
 * Evento notifica cambiamento di passo procedura guidata.
 *
 * @author  davide
 */
public class StepChangeEvent extends java.util.EventObject {
	
	/**
	 *
	 * Costruttore
	 * @param source l'oggetto che ha originato questo evento.
	 */
	public StepChangeEvent (final Object source) {
		super (source);
	}
	
}

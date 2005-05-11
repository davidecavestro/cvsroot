/*
 * StepChangeNotifierImpl.java
 *
 * Created on 30 aprile 2005, 20.04
 */

package com.ost.timekeeper.wizard;

import javax.swing.event.EventListenerList;

/**
 * Implementazione del modello di notifica delle modifiche agli Step.
 *
 * @author  davide
 */
public class StepChangeNotifierImpl {
	
	/** Costruttore vuoto. */
	public StepChangeNotifierImpl () {
	}
	private final EventListenerList listenerList = new EventListenerList ();
	private StepChangeEvent stepChangeEvent = null;
	
	/**
	 * Aggiunge un listener che deve essere notificato ad ogni modifica di stepChange.
	 *
	 * @param	l		il listener
	 */
	public void addStepChangeListener (StepChangeListener l){
		listenerList.add (StepChangeListener.class, l);
	}
	
	/**
	 * Rimuove un listener registrato du questo modello.
	 *
	 * @param l il listenenr da rimuovere.
	 */	
	public void removeStepChangeListener (StepChangeListener l) {
		listenerList.remove (StepChangeListener.class, l);
	}
	
	/**
	 * notifica i listener dell'evento di modifica dello step.
	 */
	public void fireStepChanged () {
		// Guaranteed to return a non-null array
		final Object[] listeners = listenerList.getListenerList ();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==StepChangeListener.class) {
				// Lazily create the event:
				if (stepChangeEvent == null)
					stepChangeEvent = new StepChangeEvent (this);
				((StepChangeListener)listeners[i+1]).stepChanged (stepChangeEvent);
			}
		}
	}
	
}

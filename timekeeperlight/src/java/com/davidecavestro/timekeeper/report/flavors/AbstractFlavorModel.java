/*
 * AbstractFlavorModel.java
 *
 * Created on 5 aprile 2005, 23.27
 */

package com.davidecavestro.timekeeper.report.flavors;

import javax.swing.event.EventListenerList;

/**
 * Implementazione parziale di FlavorModel.
 *
 * @author  davide
 */
public abstract class AbstractFlavorModel implements FlavorModel, FlavorListener {
	
	private final EventListenerList listenerList = new EventListenerList ();
	private FlavorEvent flavorEvent = null;
	
	/**
	 * Aggiunge un listener che deve essere notificato ad ogni modifica di flavor.
	 *
	 * @param	l		il listener
	 */
	public void addFlavorListener (FlavorListener l){
		listenerList.add (FlavorListener.class, l);
	}
	
	/**
	 * Rimuove un listener registrato du questo modello.
	 *
	 * @param l il listenenr da rimuovere.
	 */	
	public void removeFlavorListener (FlavorListener l) {
		listenerList.remove (FlavorListener.class, l);
	}
	
	/**
	 * notifica i listener dell'evento di modifica del flavor.
	 */
	protected void fireFlavorChanged () {
		// Guaranteed to return a non-null array
		final Object[] listeners = listenerList.getListenerList ();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==FlavorListener.class) {
				// Lazily create the event:
				if (flavorEvent == null)
					flavorEvent = new FlavorEvent (this);
				((FlavorListener)listeners[i+1]).flavorChanged (flavorEvent);
			}
		}
	}

	
}

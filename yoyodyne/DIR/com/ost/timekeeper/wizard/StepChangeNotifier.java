/*
 * StepChangeNotifier.java
 *
 * Created on 30 aprile 2005, 20.02
 */

package com.ost.timekeeper.wizard;

/**
 * Modello di notifica delle modifiche ad uno step.
 *
 * @author  davide
 */
public interface StepChangeNotifier {
	
	/**
	 * Aggiunge un listener che deve essere notificato ad ogni modifica di step.
	 *
	 * @param	l		il listener
	 */
	void addStepChangeListener (StepChangeListener l);
	
	/**
	 * Rimuove un listener registrato su questo modello.
	 *
	 * @param l il listener da rimuovere.
	 */	
	void removeStepChangeListener (StepChangeListener l);
	
	/**
	 * Notifica i listener dell'evento di modifica dello step.
	 */
	void fireStepChanged ();
}

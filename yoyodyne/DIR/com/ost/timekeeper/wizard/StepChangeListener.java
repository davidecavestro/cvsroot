/*
 * StepChangeListener.java
 *
 * Created on 27 aprile 2005, 22.34
 */

package com.ost.timekeeper.wizard;

import java.util.EventListener;

/**
 * Listener di cambiamento di passo di procedura guidata.
 *
 * @author  davide
 */
public interface StepChangeListener extends EventListener {
	/**
	 * Viene notificato il cambiamento di passo. 
	 * @param sce l'evento 
	 */
	void stepChanged (StepChangeEvent sce);
}

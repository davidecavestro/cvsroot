/*
 * FlavorListener.java
 *
 * Created on 4 aprile 2005, 22.58
 */

package com.davidecavestro.timekeeper.report.flavors;

import java.util.EventListener;

/**
 * Listener di modifiche al modello del tipo di report.
 *
 * @author  davide
 */
public interface FlavorListener extends EventListener {
	/**
	 * Notifica la variazione del tipo di report.
	 */
	public void flavorChanged (FlavorEvent e);
	
}

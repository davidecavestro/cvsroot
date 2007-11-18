/*
 * WorkAdvanceModelListener.java
 *
 * Created on November 25, 2006, 6:24 PM
 *
 */

package com.davidecavestro.timekeeper.model.event;

import java.util.EventListener;

/**
 * Definisce l'interfaccia per la notifica delle variazioni dell'elenco degli avanzamenti in corso (WorkAdvanceModel).
 *
 * @author Davide Cavestro
 */
public interface WorkAdvanceModelListener extends EventListener {
	/**
	 * Invocato a fronte dell'inserimento di nuovi elementi nell'elenco (nuovi avanzamenti).
	 */
	void elementsInserted (WorkAdvanceModelEvent e);
	/**
	 * Invocato a fronte della rimozione degli elementi dell'elenco (rimossi avanzamenti).
	 */
	void elementsRemoved (WorkAdvanceModelEvent e);
}

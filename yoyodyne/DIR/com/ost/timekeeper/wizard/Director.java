/*
 * Director.java
 *
 * Created on 25 aprile 2005, 10.22
 */

package com.ost.timekeeper.wizard;

import java.util.Iterator;

/**
 * Il coordinatore dei passi della procedura guidata.
 * La procedura guidata è caratterizzata da una sequenza dipassi, di cui uno è corrente.
 *
 * @author  davide
 */
public interface Director extends StepChangeNotifier {
	
	/**
	 * Sposta la procedura al passo successivo.
	 *
	 * @return il passo successivo.
	 */	
	Step next ();
	
	/**
	 * Ritorna <TT>true</TT> se e' disponibile un passo precedente.
	 *
	 * @return <TT>true</TT> se e' disponibile un passo precedente.
	 */	
	boolean hasNext ();
	
	/**
	 * Sposta la procedura al passo precedente.
	 *
	 * @return il passo precedente.
	 */	
	Step previous ();
	
	/**
	 * Ritorna il passo corrente.
	 *
	 * @return il passo corrente.
	 */	
	Step current ();
	
	/**
	 * Ritorna <TT>true</TT> se e' disponibile un passo successivo.
	 *
	 * @return <TT>true</TT> se e' disponibile un passo successivo.
	 */	
	boolean hasPrevious ();
	
	/**
	 * Ritorna <TT>true</TT>, terminando la procedura con successo, se possibile.
	 * @return <TT>true</TT> se la procedura è terminata con successo.
	 */	
	boolean finish ();
	
	/**
	 * Ritorna <TT>true</TT> se e' possibile terminare la procedura con successo.
	 *
	 * @return <TT>true</TT> se e' possibile terminare la procedura con successo.
	 */	
	boolean canFinish ();
	
	/**
	 * Interrompe la procedura.
	 */	
	void cancel ();
	
	
	/**
	 * Ritorna l'iteratore sui passi.
	 *
	 * @return l'iteratore sui passi.
	 */	
	Iterator iterateSteps ();
}

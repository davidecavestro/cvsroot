/*
 * AbstractStep.java
 *
 * Created on 29 aprile 2005, 0.29
 */

package com.ost.timekeeper.wizard;

import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import javax.swing.Icon;

/**
 * Implementazione parziale
 *
 * @author  davide
 */
public abstract class AbstractStep extends StepChangeNotifierImpl implements Step{
	
	final Director _director;
	
	private boolean _current;
	
	/** Costruttore */
	public AbstractStep (final Director director) {
		this._director=director;
	}
	
	/**
	 * Ritorna l'icona rappresentativa di questo passo, ovvero un'icona di default.
	 *
	 * @return l'icona rappresentativa di questo passo.
	 */	
	public Icon getIcon (){
		return ResourceSupplier.getImageIcon (ResourceClass.UI, "foo");
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo passo è corrente.
	 * @return <TT>true</TT> se questo passo è corrente.
	 */
	public boolean isCurrent (){
		return this._current;
	}
	
	/**
	 * Imposta il valore dello stato "corrente" per questo passo.
	 * @param current il nuovo stato.
	 */
	public void setCurrent (final boolean current){
		final boolean changed = this._current!=current;
		this._current = current;
		if (changed){
			fireStepChanged ();
		}

	}
}

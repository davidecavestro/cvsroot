/*
 * Progress.java
 *
 * Created on 6 marzo 2005, 12.23
 */

package com.ost.timekeeper.model;

import java.util.Date;

/**
 * Un avanzamento.
 *
 * @author  davide
 */
public final class Progress extends Period {
	
	private ProgressItem progressItem;
	
	/** Costruttore. */
	private Progress () {
	}
	
	/** 
	 * Costruttore con data di inizio, fine, enodo di appartenenza.
	 *
	 * @param from la data di inizio.
	 * @param to la data di fine.
	 * @param progressItem il nodo di appartenenza dell'avanzamento.
	 */
	public Progress (final Date from, final Date to, final ProgressItem progressItem) {
		super(from, to);
		this.progressItem = progressItem;
	}
	
	/**
	 *
	 * Costruttore copia.
	 *
	 * @param source la sorgente della copia.
	 */
	public Progress (final Progress source) {
		super(source);
		this.progressItem=source.getProgressItem ();
	}
	/**
	 * Ritorna il nodo di appartnenenza dell'avanzamento.
	 *
	 * @return il nodo di appartnenenza dell'avanzamento.
	 */	
	public ProgressItem getProgressItem (){return this.progressItem;}
	/**
	 * Imposta il nodo di appartnenenza dell'avanzamento.
	 *
	 * @param progressItem il nodo di appartnenenza dell'avanzamento.
	 */	
	public void setProgressItem (final ProgressItem progressItem){this.progressItem=progressItem;}
	
}

/*
 * Progress.java
 *
 * Created on 6 marzo 2005, 12.23
 */

package com.ost.timekeeper.model;

import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.Task;
import java.util.Date;

/**
 * Un avanzamento.
 * <P>
 * Anche se per implementare l'interfaccia <TT>PieceOfWork</TT>, utilizza <TT>Task</TT>, questa classe DEVE ricevere oggetti di tipo effettivo <TT>ProgressItem</TT> per funzionare correttamente.
 *<BR>
 *Questo comportamento anomalo &egrave; dovuto all'utilizzo di codice JDO legacy.
 *
 * @author  davide
 */
public final class Progress extends Period implements PieceOfWork {
	
	private ProgressItem progressItem;
	
	/** Costruttore vuoto. */
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
		this.progressItem=source.progressItem;
	}
	/**
	 * Ritorna il nodo di appartnenenza dell'avanzamento.
	 *
	 * @return il nodo di appartnenenza dell'avanzamento.
	 */	
	public Task getTask (){return this.progressItem;}
	/**
	 * Imposta il nodo di appartnenenza dell'avanzamento.
	 *
	 * @param progressItem il nodo di appartnenenza dell'avanzamento.
	 */	
	public void setTask (final Task progressItem){this.progressItem=(ProgressItem)progressItem;}
	
}

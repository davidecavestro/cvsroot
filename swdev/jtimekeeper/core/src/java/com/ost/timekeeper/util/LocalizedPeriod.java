/*
 * LocalizedPeriod.java
 *
 * Created on 19 marzo 2005, 19.56
 */

package com.ost.timekeeper.util;

import java.util.Date;

/**
 * Un periodo temporale localizzato. 
 * Qui per <I>localizzato</I> si intende <I>individuabile nell'asse temporale</I>.
 *
 * @author  davide
 */
public interface LocalizedPeriod {
	/**
	 * La data di inizio di questo periodo.
	 * @return la data di inizio di questo periodo.
	 */
	Date getFrom ();
	/**
	 * La data di fine di questo periodo.
	 * @return la data di fine di questo periodo.
	 */
	Date getTo ();
	
	/**
	 * Verifica che l'intersezione tra questo periodo e quello specificatonon sia vuota.
	 *
	 * @param period il periodo da testare.
	 * @return <code>true</code> se questo periodo temporale interseca <code>period</code>;
	 * <code>false</code> altrimenti.
	 */
	boolean intersects (final LocalizedPeriod period);
	
	/**
	 * Ritorna l'intersezione tra questo periodo e quello specificato.
	 *
	 * @param period il periodo da intersecare.
	 * @return l'intersezione tra questo periodo e quello specificato.
	 */
	LocalizedPeriod intersection (final LocalizedPeriod period);
	
	/**
	 * Verifica se questo periodo è valido.
	 *
	 * @return <code>true</code> se questo è un periodo temporale valido;
	 * <code>false</code> altrimenti.
	 */
	boolean isValid ();
	
	/**
	 * Verifica se questo periodo non è terminato.
	 *
	 * @return <code>true</code> se questo è un periodo temporale non terminato;
	 * <code>false</code> altrimenti.
	 */
	boolean isEndOpened ();
	
	/**
	 * Ritorna la durata di questo periodo.
	 *
	 * @return la durata.
	 */
	Duration getDuration ();
}

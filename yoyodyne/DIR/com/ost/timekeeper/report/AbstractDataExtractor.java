/*
 * AbstractDataExtractor.java
 *
 * Created on 31 gennaio 2005, 23.28
 */

package com.ost.timekeeper.report;

import com.ost.timekeeper.report.filter.*;
import com.ost.timekeeper.util.*;
import java.text.*;

/**
 * Implementazione parziale di un estrattore di dati per la generazione di report.
 *
 * @author  davide
 */
public abstract class AbstractDataExtractor implements DataExtractor{
	
	/**
	 * I filtri per la selezione dei dati.
	 */
	private TargetedFilterContainer[] _filters;
	
	/**
	 * Costruttore.
	 *
	 * @param filters i filtri per la selezione dei dati.
	 */
	public AbstractDataExtractor (final com.ost.timekeeper.report.filter.TargetedFilterContainer[] filters) {
		this._filters = filters;
	}
	
//	/**
//	 * RItorna i filtri impostatiper questo estrattore.
//	 *
//	 * @return i filtri impostatiper questo estrattore.
//	 */	
//	final protected Filter[] getFilters (){
//		return this._filters;
//	}
	
	/**
	 * Ritorna <TT>true</TT> se le condizioni degli eventuali filtri impostati sull'obiettivo 
	 * specificato sono soddisfatte per il valore specificato.
	 *
	 * @param target l'obiettivoda filtrare.
	 * @param targetValue il valore da filtrare.
	 * @return <TT>true</TT> se le condizioni degli eventuali filtri impostati sull'obiettivo 
	 * specificato sono soddisfatte per il valore specificato.
	 */	
	protected boolean match (final Target target, final Object targetValue){
			if (_filters==null){
				return true;
			}
			for (int i=0;i<this._filters.length;i++){
				final TargetedFilterContainer filterContainer = (TargetedFilterContainer)this._filters[i];
				final Target filterContainerTarget = filterContainer.getTarget ();
				if (filterContainerTarget==target || 
					(filterContainerTarget!=null && target!=null && filterContainerTarget.equals (target))){
					final boolean matches = filterContainer.getFilter ().matches (targetValue);
					if (!matches){
						/* filtro non passato */
						return false;
					}
				}
			}
			/* passati eventuali filtri */
			return true;
	}
	
	/**
	 * Ritorna la stringa che rappresenta la data specificata nel formato adatto all'estrazione dei dati.
	 *
	 * @param date la data da trasformare.
	 * @return la stringa che rappresenta la data specificata nel formato adatto all'estrazione dei dati.
	 */	
	protected final String getText (final java.util.Date date){
		return com.ost.timekeeper.util.CalendarUtils.toTSString (date);
	}
	
	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	
	/**
	 * Ritorna la stringa che rappresenta la durata specificata nel formato adatto all'estrazione dei dati.
	 *
	 * @param duration la durata da trasformare.
	 * @return la stringa che rappresenta la durata specificata nel formato adatto all'estrazione dei dati.
	 */	
	protected final String getText (final Duration duration){
		final StringBuffer sb = new StringBuffer ();
		sb.append (durationNumberFormatter.format (duration.getHours ()))
		.append (":")
		.append (durationNumberFormatter.format (duration.getMinutes ()))
		.append (":")
		.append (durationNumberFormatter.format (duration.getSeconds ()));
		return sb.toString ();
	}
}

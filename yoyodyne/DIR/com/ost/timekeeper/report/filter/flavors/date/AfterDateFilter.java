/*
 * AfterDateFilter.java
 *
 * Created on 31 gennaio 2005, 23.01
 */

package com.ost.timekeeper.report.filter.flavors.date;

import com.ost.timekeeper.report.filter.*;
import java.util.*;

/**
 * Implementa il filtro 'data posteriore a'.
 *
 * @author  davide
 */
public final class AfterDateFilter extends AbstractFilter{
	
	/**
	 * La data del filtro.
	 */
	private Date _date;
	
	/**
	 * Costruttore.
	 *
	 * @param date la data di test.
	 */
	public AfterDateFilter (final Date date) {
		this._date = date;
	}
	
	/**
	 * Ritorna la data di test del filtro.
	 *
	 * @return la data di test del filtro.
	 */	
	public Date getDate (){
		return this._date;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo filtro è uguale al filtro specificato.
	 *
	 * @param obj ilfiltro da testare.
	 * @return <TT>true</TT> se questo filtro è uguale al filtro specificato.
	 */	
	public boolean equals (Object obj){
		if (this==obj){
			/* identita' */
			return true;
		}
		if (obj instanceof AfterDateFilter){
			final AfterDateFilter filter = (AfterDateFilter)obj;
			return this._date==filter._date || this._date.equals (filter._date);
		}
		/* tipo incompatibile */
		return false;
	}
	
	/**
	 * Ritorna <TT>true</TT> se l'oggetto specificato soddisfa questo filtro.
	 *
	 * @param obj l'oggetto da filtrare-
	 * @return <TT>true</TT> se l'oggetto specificato soddisfa questo filtro.
	 */	
	public boolean matches (Object obj) {
		return ((Date)obj).after (this._date);
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo filtro.
	 *
	 * @return una stringa che rappresenta questo filtro.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("[AFTER: ");
		sb.append (com.ost.timekeeper.util.CalendarUtils.toTSString (this._date));
		sb.append ("] ");
		return sb.toString ();
	}
}

/*
 * AndFilter.java
 *
 * Created on 30 gennaio 2005, 10.23
 */

package com.ost.timekeeper.report.filter;

/**
 * Implementa l'applicazione ad un filtro dell'operatore di NOT logico.
 *
 * @author  davide
 */
public final class NegateFilter extends AbstractFilter{
	
	/**
	 * Il filtro da negare.
	 */
	private Filter _filter;
	
	/** Costruttore. */
	public NegateFilter (final Filter filter) {
		this._filter = filter;
	}
	
	/**
	 * Ritorna il filtro da negare.
	 *
	 * @return il filtro da negare.
	 */	
	public Filter geFilter (){
		return this._filter;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo filtro.
	 *
	 * @return una stringa che rappresenta questo filtro.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("[ NOT ");
		sb.append (this._filter);
		sb.append ("] ");
		return sb.toString ();
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo filtro è uguale all'ogggetto specificato.
	 *
	 * @param obj l'oggetto da testare.
	 * @return <TT>true</TT> se questo filtro è uguale a quello specificato.
	 */	
	public boolean equals (Object obj){
		if (this==obj){
			/* identita' */
			return true;
		}
		if (obj instanceof NegateFilter){
			final NegateFilter nf = (NegateFilter)obj;
			return this._filter==nf._filter || this._filter!=null && nf._filter!=null && this._filter.equals (nf._filter);
		}
		
		/* tipo incompatibile */
		return false;
	}
	
	/**
	 * Ritorna <TT>true</TT> se l'oggetto specificato soddisfa la condizione di questo filtro.
	 *
	 * @param obj l'oggetto da filtrare.
	 * @return <TT>true</TT> se l'oggetto specificato soddisfa la condizione di questo filtro.
	 */	
	public boolean matches (Object obj) {
		return 
			!this._filter.matches (obj);
	}
	
}

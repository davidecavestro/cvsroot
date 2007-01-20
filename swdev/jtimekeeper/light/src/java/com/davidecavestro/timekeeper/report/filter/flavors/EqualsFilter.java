/*
 * EqualsFilter.java
 *
 * Created on 31 gennaio 2005, 23.07
 */

package com.davidecavestro.timekeeper.report.filter.flavors;

import com.davidecavestro.timekeeper.report.filter.AbstractFilter;

/**
 * Implementa il filtro 'uguale a'.
 *
 * @author  davide
 */
public final class EqualsFilter extends AbstractFilter{
	
	/**
	 * L'oggetto di test del filtro.
	 */
	private Object _testObject;
	
	/**
	 * Costruttore.
	 *
	 * @param testObject l'oggetto di test.
	 */
	public EqualsFilter (final Object testObject) {
		this._testObject = testObject;
	}
	
	/**
	 * Ritorna l'oggetto di test del filtro.
	 *
	 * @return l''oggetto di test del filtro.
	 */	
	public Object getObject (){
		return this._testObject;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo filtro � uguale al filtro specificato.
	 *
	 * @param obj ilfiltro da testare.
	 * @return <TT>true</TT> se questo filtro � uguale al filtro specificato.
	 */	
	public boolean equals (Object obj){
		if (this==obj){
			/* identita' */
			return true;
		}
		if (obj instanceof EqualsFilter){
			final EqualsFilter filter = (EqualsFilter)obj;
			return this._testObject==filter._testObject || 
				(this._testObject!=null && this._testObject.equals (filter._testObject));
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
		return this._testObject==obj || 
			(this._testObject!=null && this._testObject.equals (obj));
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo filtro.
	 *
	 * @return una stringa che rappresenta questo filtro.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("[EQUALS: ");
		sb.append (this._testObject);
		sb.append (" ] ");
		return sb.toString ();
	}
}

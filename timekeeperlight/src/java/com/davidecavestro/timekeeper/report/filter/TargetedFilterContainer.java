/*
 * TargetedFilterContainer.java
 *
 * Created on 31 gennaio 2005, 23.47
 */

package com.davidecavestro.timekeeper.report.filter;

/**
 * Un contenitore di filtro associato all'obiettivo su cui � incentrato.
 *
 * @author  davide
 */
public final class TargetedFilterContainer {
	
	/**
	 * Il filtro.
	 */
	private Filter _filter;
	
	/**
	 * L'obiettivo.
	 */
	private Target _target;
	
	/** Costruttore. */
	public TargetedFilterContainer (final Target target, final Filter filter) {
		this._target=target;
		this._filter=filter;
	}
	
	/**
	 * Ritorna l'obiettivo.
	 *
	 * @return l'obiettivo.
	 */	
	public Target getTarget (){
		return this._target;
	}

	/**
	 * Ritorna il filtro.
	 *
	 * @return il filtro.
	 */	
	public Filter getFilter (){
		return this._filter;
	}
	
	
	/**
	 * Ritorna <TT>true</TT> se questo contenitore � uguale all'oggetto specificato.
	 *
	 * @param obj l'oggettos�dicui testare l'uguaglianza.
	 * @return <TT>true</TT> se questo contenitore � uguale all'oggetto specificato.
	 */	
	public boolean equals (Object obj){
		if (this==obj){
			/* identita' */
			return true;
		}
		if (obj instanceof TargetedFilterContainer){
			final TargetedFilterContainer tfc = (TargetedFilterContainer)obj;
			return
				(this._filter==tfc._filter ||
					(this._filter!=null && this._filter.equals (tfc._filter)))
				&&
				(this._target==tfc._target ||
					(this._target!=null && this._target.equals (tfc._target)))
				;
		}
		
		/* tipo incompatibile*/
		return false;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo contenitore.
	 *
	 * @return una stringa che rappresenta questo contenitore.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("target: ").append (this._target);
		sb.append (" filter: ").append (this._filter);
		return sb.toString ();
	}
}

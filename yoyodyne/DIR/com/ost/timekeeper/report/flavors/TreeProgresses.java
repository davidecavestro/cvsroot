/*
 * TreeProgresses.java
 *
 * Created on 31 gennaio 2005, 21.15
 */

package com.ost.timekeeper.report.flavors;

import com.ost.timekeeper.model.*;
import com.ost.timekeeper.report.*;

/**
 * Estrae i deti per il report dell'albero di avanzamenti.
 *
 * @author  davide
 */
public final class TreeProgresses extends AbstractDataExtractor {
	
	/**
	 * La radice del sottoalbero di interesse.
	 */
	private ProgressItem _subtreeRoot;
	
	/**
	 * Costruttore.
	 *
	 * @param filters i filtri da applicare.
	 * @param subtreeRoot la radice del sottoalbero di interesse per il report.
	 */
	public TreeProgresses (final ProgressItem subtreeRoot, final com.ost.timekeeper.report.filter.Filter[] filters) {
		super (filters);
		this._subtreeRoot = subtreeRoot;
	}
	
	/**
	 * Ritorna la radice del sottoalbero di interesse per il report.
	 *
	 * @return la radice del sottoalbero di interesse per il report.
	 */	
	public ProgressItem getSubtreeRoot (){
		return this._subtreeRoot;
	}
	
	/**
	 * Estrae e ritorna i dati per il report.
	 *
	 * @return i dati per il report.
	 */	
	public org.jdom.Document extract () {
		final org.jdom.Document data = new org.jdom.Document ();
		/*@todo implementare l'estrazione dati */
		return data;
	}

	/**
	 * Ritorna una rappresentazion ein formato stringa di questo estrattore.
	 *
	 * @return una stringa che rappresenta questo estrattore di dati.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (" subtree root: ");
		sb.append (this._subtreeRoot);
		return sb.toString ();
	}
}

/*
 * JRBindings.java
 *
 * Created on 2 febbraio 2005, 22.22
 */

package com.davidecavestro.timekeeper.report;

import java.io.*;

/**
 * Le impostazioni di configurazione di Jasper per la generazione del report.
 *
 * @author  davide
 */
public class JRBindings {
	
	private InputStream _reportDescriptor;
	
	/**
	 * Costruttore.
	 * @param reportDescriptor il descrittore del report.
	 */
	public JRBindings (final InputStream reportDescriptor) {
		this._reportDescriptor = reportDescriptor;
	}
	
	/**
	 * Ritorna il descrittore del report.
	 *
	 * @return il descrittore del report.
	 */
	public InputStream getReportDescriptor (){
		return this._reportDescriptor;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di queste preferenze.
	 *
	 * @return una stringa che rappresenta queste preferenze.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (" reportDescriptor: ").append (this._reportDescriptor);
		return sb.toString ();
	}
}

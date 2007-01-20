/*
 * ReportPreferences.java
 *
 * Created on 30 gennaio 2005, 20.59
 */

package com.davidecavestro.timekeeper.report;

import java.io.*;

/**
 * Preferenze relative alla generazione del report.
 *
 * @author  davide
 */
public final class ReportPreferences {
	/**
	 * Il file contenente il report.
	 */
	private File _output;
	
	/**
	 * Costruttore.
	 *
	 * @param output il report generato.
	 */
	public ReportPreferences (final File output) {
		this._output = output;
	}
	
	/**
	 * Ritorna il file contenente il report.
	 *
	 * @return il risultato del report.
	 */	
	public File getOutput (){
		return this._output;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di queste preferenze.
	 *
	 * @return una stringa che rappresenta queste preferenze.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (" output: ").append (this._output);
		return sb.toString ();
	}
}

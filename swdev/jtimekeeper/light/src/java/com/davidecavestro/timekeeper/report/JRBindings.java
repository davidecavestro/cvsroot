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
	/** Comando di visualizzazione. */
	public static final String TASK_VIEW = "view";
	/** Comando di stampa. */
	public static final String TASK_PRINT = "print";
	/** Comando di generazione PDF. */
	public static final String TASK_PDF = "pdf";
	/** Comando di generazione XML. */
	public static final String TASK_XML = "xml";
	/** Comando di generazione XML ENBEDDED. */
	public static final String TASK_XML_EMBED = "xmlEmbed";
	/** Comando di generazione HTML. */
	public static final String TASK_HTML = "html";
	/** Comando di generazione XLS. */
	public static final String TASK_XLS = "xls";
	/** Comando di generazione CSV. */
	public static final String TASK_CSV = "csv";
	
	private InputStream _reportDescriptor;
	private String _recordPath;
	private String _taskType;
	
	/**
	 * Costruttore.
	 * @param reportDescriptor il descrittore del report.
	 * @param recordPath il percorso dei record
	 * @param taskType il tipo di output desiderato
	 */
	public JRBindings (final InputStream reportDescriptor, final String recordPath, final String taskType) {
		this._reportDescriptor = reportDescriptor;
		this._recordPath = recordPath;
		this._taskType = taskType;
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
	 * Ritorna il percorso dei record.
	 * @return il percorso dei record.
	 */
	public String getRecordPath (){
		return this._recordPath;
	}
	
	/**
	 * Ritorna il tipo di output desiderato.
	 * @return il tipo di output desiderato.
	 */
	public String getTaskType (){
		return this._taskType;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di queste preferenze.
	 *
	 * @return una stringa che rappresenta queste preferenze.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (" reportDescriptor: ").append (this._reportDescriptor);
		sb.append (" recordPath: ").append (this._recordPath);
		sb.append (" taskType: ").append (this._taskType);
		return sb.toString ();
	}
}

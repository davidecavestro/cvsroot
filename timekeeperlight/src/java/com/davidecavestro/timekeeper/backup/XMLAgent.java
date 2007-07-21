/*
 * XMLWorkSpaceExporter.java
 *
 * Created on January 2, 2007, 3:05 PM
 *
 */

package com.davidecavestro.timekeeper.backup;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.backup.XMLImporter.ImportConflictsResolver;
import com.davidecavestro.timekeeper.model.WorkSpace;
import org.jdom.Document;

/**
 * Gestisce l'import/export dati da/per XML.
 *
 * @author Davide Cavestro
 */
public class XMLAgent {
	
	final ApplicationContext _context;
	
	/**
	 * Costruttore.
	 * @param context 
	 */
	public XMLAgent (final ApplicationContext context) {
		_context = context;
	}
	
	/**
	 * Effettua il backup su file dei dati relativi ad un progetto.
	 * Il file generato &egrave; in formato XML.
	 *
	 * @param project il progetto da salvare.
	 * @param document il documento XML da usare.
	 *
	 * @return <CODE>true</CODE> se il backup /egrave; andato a buon fine.
	 */
	public boolean backup (final WorkSpace project, final Document document) {
		return new XMLExporter (_context).backup (project, document);
	}
	
	/**
	 * Effettua il restore da file dei dati relativi ad un progetto.
	 * Il file utilizzato &egrave; in formato XML.
	 * 
	 * 
	 * @param icr il gestore della risoluzione conflitti di importazione.
	 * @param document il documento XML da usare.
	 *
	 * @return <CODE>true</CODE> se il ripristino &egrave; andato a buon fine.
	 */
	public boolean restore (final Document document, final ImportConflictsResolver icr) {
		return new XMLImporter (_context).restore (document, icr);
	}
	
}

/*
 * UpdateProgress.java
 *
 * Created on 06 marzo 2005, 10.21
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;
import java.util.Date;

/**
 * Comando di modifica periodo di avanzamento.
 *
 * @author  davide
 */
public final class UpdateProgress extends AbstractCommand {
	
	/**
	 * La chiave per l'attributo DA del periodo.
	 */
	public final static DateKey PROGRESSFROM = new DateKey ("progressfrom");
	
	/**
	 * La chiave per l'attributo A del periodo.
	 */
	public final static DateKey PROGRESSTO = new DateKey ("progressto");
	
	/**
	 * La chiave per l'attributo DESCRIZIONE del periodo.
	 */
	public final static StringKey PROGRESSDESCRIPTION = new StringKey ("progressdescription");
	
	/**
	 * La chiave per l'attributo NOTE del periodo.
	 */
	public final static StringKey PROGRESSNOTES = new StringKey ("progressnotes");
	
	/**
	 * Il periodo da modificare.
	 */
	private Progress _progress;
	
	/**
	 * Costruttore.
	 *
	 * @param progress il periodo da modificare.
	 * @param attributes gli attributi da impostare per il periodo.
	 */
	public UpdateProgress (final Progress progress, final Attribute[] attributes) {
		super (attributes);
		this._progress = progress;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		final AttributeMap attributes = getAttributeMap ();
		
		final Date from = attributes.getAttribute (PROGRESSFROM).getValue ();
		final Date to = attributes.getAttribute (PROGRESSTO).getValue ();
		final String description = attributes.getAttribute (PROGRESSDESCRIPTION).getValue ();
		final String notes = attributes.getAttribute (PROGRESSNOTES).getValue ();
		
		final Application app = Application.getInstance ();
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager();
		final javax.jdo.Transaction tx = pm.currentTransaction();
		tx.begin();
		try {
			this._progress.setFrom (from);
			this._progress.setTo (to);
			this._progress.setDescription (description);
			this._progress.setNotes (notes);
			
			tx.commit();			
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
	}
}

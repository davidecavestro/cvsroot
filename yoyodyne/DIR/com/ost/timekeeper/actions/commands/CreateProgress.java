/*
 * CreateProgress.java
 *
 * Created on 16 aprile 2005, 11.33
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;
import java.util.Date;

/**
 * Comando di creazione nuovo periodo di avanzamento.
 *
 * @author  davide
 */
public final class CreateProgress extends AbstractCommand {
	
	/**
	 * La chiave per l'attributo FROM del nuovo avanzamento.
	 */
	public final static DateKey FROM = new DateKey ("from");
	
	/**
	 * La chiave per l'attributo TO del nuovo avanzamento.
	 */
	public final static DateKey TO = new DateKey ("to");
	
	/**
	 * La chiave per l'attributo DESCRIZIONE del nuovo avanzamento.
	 */
	public final static StringKey DESCRIPTION = new StringKey ("description");
	
	/**
	 * La chiave per l'attributo NOTE del nuovo avanzamento.
	 */
	public final static StringKey NOTES = new StringKey ("notes");
	
	
	/**
	 * Il nodo di avanzamento.
	 */
	private ProgressItem _node;
	
	/**
	 * Costruttore.
	 *
	 * @param node il nodo di avanzamento.
	 * @param attributes gli attributi da impostare per il nuovo avanzamento.
	 */
	public CreateProgress (final ProgressItem node, final Attribute[] attributes) {
		super (attributes);
		this._node = node;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		final AttributeMap attributes = getAttributeMap ();
		
		final Date from = attributes.getAttribute (FROM).getValue ();
		final Date to = attributes.getAttribute (TO).getValue ();
		String description = null;
		{
			final StringAttribute attr = attributes.getAttribute (DESCRIPTION);
			if (attr!=null){
				description = attr.getValue ();
			}
		}
		String notes = null;
		{
			final StringAttribute attr = attributes.getAttribute (NOTES);
			if (attr!=null){
				notes = attr.getValue ();
			}
		}
		
		final Progress newProgress = new Progress (from, to, _node);
		newProgress.setDescription (description);
		newProgress.setNotes (notes);
		final Application app = Application.getInstance ();
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager();
		final javax.jdo.Transaction tx = pm.currentTransaction();
		tx.begin();
		try {
			_node.addProgress (newProgress);
		
			tx.commit();			
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
	}
}

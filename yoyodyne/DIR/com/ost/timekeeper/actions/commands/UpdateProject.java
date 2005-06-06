/*
 * UpdateProject.java
 *
 * Created on 30 maggio 2005, 23.09
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;

/**
 * Modifica di progetto.
 *
 * @author  davide
 */
public final class UpdateProject extends AbstractCommand {
	
	/**
	 * La chiave per l'attributo NOME del progetto.
	 */
	public final static StringKey NAME = new StringKey ("name");
	
	/**
	 * La chiave per l'attributo DESCRIZIONE del progetto.
	 */
	public final static StringKey DESCRIPTION = new StringKey ("description");
	
	/**
	 * La chiave per l'attributo NOTE del progetto.
	 */
	public final static StringKey NOTES = new StringKey ("notes");
	
	/**
	 * Il progetto da modificare.
	 */
	private Project _project;
	
	/**
	 * Costruttore.
	 *
	 * @param project il progetto da modificare.
	 * @param attributes gli attributi da impostare per il progetto.
	 */
	public UpdateProject (final Project project, final Attribute[] attributes) {
		super (attributes);
		this._project = project;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		final AttributeMap attributes = getAttributeMap ();
		
		final String name = attributes.getAttribute (NAME).getValue ();
		final String description = attributes.getAttribute (DESCRIPTION).getValue ();
		final String notes = attributes.getAttribute (NOTES).getValue ();
		
		final Application app = Application.getInstance ();
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager();
		final javax.jdo.Transaction tx = pm.currentTransaction();
		tx.begin();
		try {
			this._project.setName (name);
			this._project.setDescription (description);
			this._project.setNotes (notes);
			
			tx.commit();			
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
		Application.getLogger ().debug ("Project updated");
	}
}

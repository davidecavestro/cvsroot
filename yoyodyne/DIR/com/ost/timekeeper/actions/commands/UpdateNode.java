/*
 * UpdateNode.java
 *
 * Created on 06 marzo 2005, 10.14
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;

/**
 * Comando di modifica nodo di avanzamento.
 *
 * @author  davide
 */
public final class UpdateNode extends AbstractCommand {
	
	/**
	 * La chiave per l'attributo CODICE del nodo.
	 */
	public final static StringKey NODECODE = new StringKey ("nodecode");
	
	/**
	 * La chiave per l'attributo NOME del nodo.
	 */
	public final static StringKey NODENAME = new StringKey ("nodename");
	
	/**
	 * La chiave per l'attributo DESCRIZIONE del nodo.
	 */
	public final static StringKey NODEDESCRIPTION = new StringKey ("nodedescription");
	
	/**
	 * La chiave per l'attributo NOTE del nodo.
	 */
	public final static StringKey NODENOTES = new StringKey ("nodenotes");
	
	/**
	 * Il nodo da modificare.
	 */
	private ProgressItem _node;
	
	/**
	 * Costruttore.
	 *
	 * @param node il nodo da modificare.
	 * @param attributes gli attributi da impostare per il nodo.
	 */
	public UpdateNode (final ProgressItem node, final Attribute[] attributes) {
		super (attributes);
		this._node = node;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		final AttributeMap attributes = getAttributeMap ();
		
		final String nodeName = attributes.getAttribute (NODENAME).getValue ();
		final String nodeCode = attributes.getAttribute (NODECODE).getValue ();
		final String nodeDescription = attributes.getAttribute (NODEDESCRIPTION).getValue ();
		final String nodeNotes = attributes.getAttribute (NODENOTES).getValue ();
		
		final Application app = Application.getInstance ();
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager();
		final javax.jdo.Transaction tx = pm.currentTransaction();
		tx.begin();
		try {
			this._node.setCode (nodeCode);
			this._node.setName (nodeName);
			this._node.setDescription (nodeDescription);
			this._node.setNotes (nodeNotes);
			
			tx.commit();			
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
		Application.getLogger ().debug ("Node updated");
	}
}

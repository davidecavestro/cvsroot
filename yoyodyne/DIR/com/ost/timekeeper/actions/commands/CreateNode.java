/*
 * CreateNode.java
 *
 * Created on 29 dicembre 2004, 11.22
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;

/**
 * Comando di creazione nuovo nodo di avanzamento.
 *
 * @author  davide
 */
public final class CreateNode extends AbstractCommand {
	
	/**
	 * La chiave per l'attributo NOME del nuovo nodo.
	 */
	public final static StringKey NEWNODENAME = new StringKey ("newnodename");
	
	/**
	 * La posizione del nuovo nodo tra i figli esistenti dl padre.
	 */
	private int _position;
	
	/**
	 * Il padre del nuovo nodo.
	 */
	private ProgressItem _parent;
	
	/**
	 * Costruttore.
	 *
	 * @param position la posizione del nuovo nodo. Specificare qualsiasi valore negativo per 
	 * accodare il nuovo nodo ai fratelli esistenti.
	 * @param parent il nodo padre.
	 * @param attributes gli attributi da impostare per il nuovo nodo.
	 */
	public CreateNode (final ProgressItem parent, final Attribute[] attributes, final int position) {
		super (attributes);
		this._parent = parent;
		this._position = position;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		final AttributeMap attributes = getAttributeMap ();
		
		final String newNodeName = attributes.getAttribute (NEWNODENAME).getValue ();
		
		final ProgressItem newNode = new ProgressItem (newNodeName);
		final Application app = Application.getInstance ();
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager();
		final javax.jdo.Transaction tx = pm.currentTransaction();
		tx.begin();
		try {
			if (this._position>=0){
				this._parent.insert (newNode, this._position);
				app.getMainForm ().getProgressTreeModel ().insertNodeInto (newNode, _parent, this._position);
			} else {
				final int position = this._parent.insert (newNode);			
				app.getMainForm ().getProgressTreeModel ().insertNodeInto (newNode, _parent, position);
			}
		
			tx.commit();			
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
	}
}

/*
 * MoveNode.java
 *
 * Created on 29 dicembre 2004, 18.55
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;
import java.util.*;

/**
 * Comando di spostamento nodo di avanzamento.
 *
 * @author  davide
 */
public final class MoveNode extends AbstractCommand {
	
	/**
	 * Il nodo da spostare.
	 */
	private ProgressItem _movingNode;
	
	/**
	 * Il nuovo padre del nodo da spostare.
	 */
	private ProgressItem _newParent;
	
	/**
	 * La nuova posizione dle nodo da spostare.
	 */
	private int _newPosition;
	
	/**
	 * Costruttore.
	 *
	 * @param movingNode il nodo da spostare.
	 * @param newParent il nuovo padre.
	 * @param newPosition la nuova posizione del nodo. Specificare qualsiasi valore negativo per
	 * accodare il nodo ai fratelli esistenti.
	 */
	public MoveNode (final ProgressItem movingNode, final ProgressItem newParent, final int newPosition) {
		super ();
		this._movingNode = movingNode;
		this._newParent = newParent;
		this._newPosition = newPosition;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		//		if (_movingNode.isRoot ()){
		//			//non si sposta la radice;
		//			throw new IllegalArgumentException("Cannot remove root node.");
		//		}
		
		if (_newParent==null){
			//non si crea una nuova radice;
			throw new IllegalArgumentException ("Cannot generate a new  root node.");
		}
		
		final Application app = Application.getInstance ();
		app.getMainForm ().getProgressTreeModel ().removeNodeFromParent (_movingNode);
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager ();
		final javax.jdo.Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			final ProgressItem oldParent = _movingNode.getParent ();
			oldParent.remove (_movingNode);
			if (this._newPosition>=0){
				this._newParent.insert (_movingNode, this._newPosition);
				app.getMainForm ().getProgressTreeModel ().insertNodeInto (_movingNode, _newParent, this._newPosition);
			} else {
				final int position = this._newParent.insert (_movingNode);
				app.getMainForm ().getProgressTreeModel ().insertNodeInto (_movingNode, _newParent, position);
			}
			
			tx.commit ();
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
	}
	
}

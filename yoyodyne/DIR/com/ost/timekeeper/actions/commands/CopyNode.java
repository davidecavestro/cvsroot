/*
 * CopyNode.java
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
 * Comando di copia nodo di avanzamento.
 *
 * @author  davide
 */
public final class CopyNode extends AbstractCommand {
	
	/**
	 * Il nodo da copiare.
	 */
	private ProgressItem _copyingNode;
	
	/**
	 * Il padre della nuova copia.
	 */
	private ProgressItem _newParent;
	
	/**
	 * La posizione della copia.
	 */
	private int _copyPosition;
	
	/**
	 * Costruttore.
	 *
	 * @param copyingNode il nodo da copiare.
	 * @param newParent il padre ella copia.
	 * @param copyPosition la posizione della copia. Specificare qualsiasi valore negativo per 
	 * accodare il nodo ai fratelli esistenti.
	 */
	public CopyNode (final ProgressItem copyingNode, final ProgressItem newParent, final int copyPosition) {
		super ();
		this._copyingNode = copyingNode;
		this._newParent = newParent;
		this._copyPosition = copyPosition;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		if (_newParent==null){
			//non si crea una nuova radice;
			throw new IllegalArgumentException("Cannot generate a new  root node.");
		}
		
		final Application app = Application.getInstance ();
		
		final ProgressItem oldParent = _copyingNode.getParent ();
		final ProgressItem nodeCopy = new ProgressItem (_copyingNode);
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager();
		final javax.jdo.Transaction tx = pm.currentTransaction();
		tx.begin();
		try {
			if (this._copyPosition>=0){
				this._newParent.insert (nodeCopy, this._copyPosition);
				app.getMainForm ().getProgressTreeModel ().insertNodeInto (nodeCopy, _newParent, this._copyPosition);
			} else {
				this._newParent.insert (nodeCopy);
				app.getMainForm ().getProgressTreeModel ().insertNodeInto (nodeCopy, _newParent, _newParent.childCount ());
			}
			copySubtree(app, _copyingNode, nodeCopy);
			
			tx.commit();			
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
	}
	
	/**
	 * Propaga la copia la sottoalbero, radice esclusa, avanzamenti inclusi.
	 *
	 * @param app l'applicazione.
	 * @param node la radice del sottoalbero sorgente.
	 * @param target la radice del sottoalbero destinazione.
	 */	
	private void copySubtree (final Application app, final ProgressItem source, final ProgressItem target){
		for (final Iterator it = source.getChildren ().iterator ();it.hasNext ();){
			final ProgressItem child = (ProgressItem)it.next ();
			final ProgressItem newChild = new ProgressItem (child);
			target.insert (child);
			app.getMainForm ().getProgressTreeModel ().insertNodeInto (newChild, target, target.childCount ());
			copySubtree (app, child, newChild);
		}
		final List progresses = new ArrayList ();
		for (final Iterator it = source.getProgresses ().iterator ();it.hasNext ();){
			final Progress sourceProgress = (Progress)it.next ();
			final Progress targetProgress = new Progress (sourceProgress);
			progresses.add (targetProgress);
		}
		target.setProgresses (progresses);
	}
}

/*
 * DeleteNode.java
 *
 * Created on 29 dicembre 2004,18.36
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;
import java.util.*;

/**
 * Comando di rimozione nodo di avanzamento.
 *
 * @author  davide
 */
public final class DeleteNode extends AbstractCommand {
	
	/**
	 * Il nodo da rimuovere.
	 */
	private ProgressItem _deletingNode;
	
	/**
	 * Costruttore.
	 *
	 * @param deletingNode il nodo da rimuovere.
	 */
	public DeleteNode (final ProgressItem deletingNode) {
		super ();
		this._deletingNode = deletingNode;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		final ProgressItem parent = _deletingNode.getParent ();
		if (parent==null){
			//non si rimuove la radice;
			throw new IllegalArgumentException("Cannot remove root node.");
		}
		
		final Application app = Application.getInstance ();
		app.getMainForm ().getProgressTreeModel ().removeNodeFromParent (_deletingNode);
		parent.remove(_deletingNode);
		deleteSubtreePersistent (app, _deletingNode);
	}
	
	/**
	 * Rimuove il sottoalbero, avanzamenti compresi.
	 *
	 * @param toDelete la radice del sottoalbero da rimuovere.
	 */	
	private void deleteSubtreePersistent (final Application app, final ProgressItem toDelete){
		for (final Iterator it = toDelete.getChildren ().iterator ();it.hasNext ();){
			deleteSubtreePersistent (app, (ProgressItem)it.next ());
		}
		for (final Iterator it = toDelete.getProgresses ().iterator ();it.hasNext ();){
			app.getPersistenceManager ().deletePersistent ((Period)it.next ());
		}
		app.getPersistenceManager ().deletePersistent (toDelete);
	}
}

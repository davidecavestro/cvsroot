/*
 * MoveProgress.java
 *
 * Created on 09 marzo 2005, 00.17
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;
import java.util.*;

/**
 * Comando di spostamento avanzamento.
 *
 * @author  davide
 */
public final class MoveProgress extends AbstractCommand {
	
	/**
	 * Avanzamento da spostare.
	 */
	private Progress _movingProgress;
	
	/**
	 * Il nuovo padre dell'avanzamento da spostare.
	 */
	private ProgressItem _newParent;
	
	/**
	 * La nuova posizione dell'avanzamento da spostare.
	 */
	private int _newPosition;
	
	/**
	 * Costruttore.
	 *
	 * @param movingProgress l'avanzamento da spostare.
	 * @param newParent il nuovo padre.
	 * @param newPosition la nuova posizione dell'avanzamento. Specificare qualsiasi valore negativo per
	 * accodare l'avanzamento ai fratelli esistenti.
	 */
	public MoveProgress (final Progress movingProgress, final ProgressItem newParent, final int newPosition) {
		super ();
		this._movingProgress = movingProgress;
		this._newParent = newParent;
		this._newPosition = newPosition;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		
		if (_newParent==null){
			//non si crea un avanzamento senza nodo padre;
			throw new IllegalArgumentException ("Cannot have a progress without a parent node.");
		}
		
		final Application app = Application.getInstance ();
//		app.getMainForm ().getProgressTreeModel ().removeNodeFromParent (_movingNode);
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager ();
		final javax.jdo.Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			final ProgressItem oldParent = _movingProgress.getProgressItem ();
			oldParent.deleteProgress (_movingProgress);
			if (this._newPosition>=0){
				this._newParent.insert (_movingProgress, this._newPosition);
			} else {
				final int position = this._newParent.insert (_movingProgress);
			}
			
			tx.commit ();
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
		Application.getInstance ().setChanged ();
		Application.getInstance ().notifyObservers (ObserverCodes.SELECTEDITEMCHANGE);
		Application.getInstance ().setChanged ();
		Application.getInstance ().notifyObservers (ObserverCodes.ITEMPROGRESSINGCHANGE);
	}
	
}

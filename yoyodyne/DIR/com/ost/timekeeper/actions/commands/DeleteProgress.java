/*
 * DeleteProgress.java
 *
 * Created on 06 marzo 2005,11.35
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.NestedRuntimeException;
import java.util.*;

/**
 * Comando di rimozione periodo di avanzamento.
 *
 * @author  davide
 */
public final class DeleteProgress extends AbstractCommand {
	
	/**
	 * Il periodo da rimuovere.
	 */
	private Progress _deletingProgress;
	
	/**
	 * Costruttore.
	 *
	 * @param deletingProgress l'avanzamento da rimuovere.
	 */
	public DeleteProgress (final Progress deletingProgress) {
		super ();
		this._deletingProgress = deletingProgress;
	}
	
	/**
	 * Esegue questo comando.
	 */
	public void execute (){
		final Application app = Application.getInstance ();
		
		final javax.jdo.PersistenceManager pm = app.getPersistenceManager ();
		final javax.jdo.Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			final ProgressItem node = this._deletingProgress.getProgressItem ();
			if (node!=null){
				node.deleteProgress (this._deletingProgress);
//				pm.makePersistent (node);
			}
			pm.deletePersistent (this._deletingProgress);
			
			tx.commit ();
		} catch (final Throwable t){
			tx.rollback ();
			throw new NestedRuntimeException (t);
		}
		Application.getInstance ().setChanged ();
		Application.getInstance ().notifyObservers (ObserverCodes.SELECTEDITEMCHANGE);
		Application.getInstance ().setChanged ();
		Application.getInstance ().notifyObservers (ObserverCodes.ITEMPROGRESSINGCHANGE);
	}
	
}

/*
 * DeleteProject.java
 *
 * Created on 12 marzo 2005, 20.57
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import com.ost.timekeeper.util.NestedRuntimeException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Rimuove il progetto dai dati persistenti.
 *
 * @author  davide
 */
public class DeleteProject implements Command{
	
	private Project _project;
	
	/**
	 * Costruttore.
	 * @param project il progetto.
	 */
	public DeleteProject (final Project project) {
		this._project=project;
	}
	
	public void execute () {
		final Collection toDelete = new ArrayList ();
		
		final Application app = Application.getInstance ();
//		if (project.jdoIsPersistent ()){
			/*
			 * Progetto persistente.
			 */
			toDelete.add (this._project);
			final ProgressItem root = this._project.getRoot ();
			if (root!=null){
				
				toDelete.add (root);
				for (final Iterator it=root.getSubtreeProgresses ().iterator ();it.hasNext ();){
					final Progress progress = (Progress)it.next ();
	//				if (progress.jdoIsPersistent ()){
						/*
						 * Avanzamento persistente.
						 */
						toDelete.add (progress);
	//				}
				}

				for (Iterator it=root.getDescendants ().iterator ();it.hasNext ();){
					ProgressItem node = (ProgressItem)it.next ();
	//				if (node.jdoIsPersistent ()){
						/*
						 * Nodo persistente.
						 */
						toDelete.add (node);
	//				}
				}
	//		}
		}

		final javax.jdo.PersistenceManager pm = app.getPersistenceManager ();
		final javax.jdo.Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {		/*
		 * Rimuove persistenza oggetti determinati.
		 */
			app.getPersistenceManager ().deletePersistentAll (toDelete);
			
			
			tx.commit ();
		} catch (final Throwable t){
			tx.rollback ();
			throw new NestedRuntimeException (t);
		}
	}
	
}

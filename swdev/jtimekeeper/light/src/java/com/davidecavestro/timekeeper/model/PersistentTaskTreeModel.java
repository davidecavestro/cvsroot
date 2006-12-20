/*
 * PersistentTaskTreeModel.java
 *
 * Created on December 18, 2006, 11:55 PM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.common.log.Logger;
import com.davidecavestro.common.undo.RBUndoManager;
import com.davidecavestro.common.util.NestedRuntimeException;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import com.davidecavestro.timekeeper.persistence.PersistenceNode;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

/**
 * Estensione al modello con supporto alla persistenza.
 *
 * @author Davide Cavestro
 */
public class PersistentTaskTreeModel extends UndoableTaskTreeModel {
	
	private final PersistenceNode _persistenceNode;
	private final Logger _logger;
	
	/**
	 * Costruttore.
	 * 
	 * @param persistenceNode 
	 * @param peh 
	 * @param workSpace 
	 * @param applicationOptions le opzioni di configurazione.
	 */
	public PersistentTaskTreeModel (final PersistenceNode persistenceNode, final RBUndoManager um, final ApplicationOptions applicationOptions, final Logger logger, final TaskTreeModelExceptionHandler peh, final WorkSpace workSpace) {
		super (um, applicationOptions, peh, workSpace);
		_persistenceNode = persistenceNode;
		_logger = logger;
	}
	
	
	public void removePieceOfWork (final PieceOfWork pow) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.removePieceOfWork (pow);
			
			pm.deletePersistent (pow);
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	public void insertPieceOfWorkInto (final PieceOfWork newPOW, final Task t, final int index) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.insertPieceOfWorkInto (newPOW, t, index);
			
			pm.makePersistent (newPOW);
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	public void removeNodeFromParent (final Task node) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.removeNodeFromParent (node);
		
			deleteSubtreePersistent (pm, node);
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	public void insertNodeInto (final Task newChild, final Task parent, final int index) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.insertNodeInto (newChild, parent, index);
		
			pm.makePersistent (newChild);
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	public void removeTasks (final Task parent, final List<Task> l) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.removeTasks (parent, l);
		
			deleteSubtreePersistent (pm, l);
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	public void removePiecesOfWork (final Task t, final List<PieceOfWork> l) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.removePiecesOfWork (t, l);
	
			for (final PieceOfWork pow : l) {
				pm.deletePersistent (pow);
			}
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	public void moveTasksTo (final Task previousParent, final List<Task> l, final Task target, final int position) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.moveTasksTo (previousParent, l, target, position);
	
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	public void movePiecesOfWorkTo (final Task previousTask, final List<PieceOfWork> l, final Task target, final int position) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.movePiecesOfWorkTo (previousTask, l, target, position);
	
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	public void updateTask (final Task t, final String name) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.updateTask (t, name);
	
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}
	
	
	public void updatePieceOfWork (final PieceOfWork pow, final Date from, final Date to, final String description) {
		final PersistenceManager pm = _persistenceNode.getPersistenceManager ();
		final Transaction tx = pm.currentTransaction ();
		tx.begin ();
		try {
			super.updatePieceOfWork (pow, from, to, description);
	
			tx.commit ();
		} catch (final Throwable tw){
			tx.rollback ();
			_logger.error ("Unexpected error during persistence transaction", tw);
			/*
			 * Eccezione non altrimenti gestita.
			 */
			throw new NestedRuntimeException (tw);
		}
	}	
	
	/**
	 * Rimuove il sottoalbero, avanzamenti compresi.
	 * 
	 * @param pm 
	 * @param toDelete la radice del sottoalbero da rimuovere.
	 */
	private void deleteSubtreePersistent (final PersistenceManager pm, final List<Task> toDelete){
		for (final Task t : toDelete) {
			deleteSubtreePersistent (pm, t);
		}
	}
	
	/**
	 * Rimuove il sottoalbero, avanzamenti compresi.
	 *
	 * @param toDelete la radice del sottoalbero da rimuovere.
	 */
	private void deleteSubtreePersistent (final PersistenceManager pm, final Task toDelete){
		for (final Iterator it = toDelete.getChildren ().iterator ();it.hasNext ();){
			deleteSubtreePersistent (pm, (Task)it.next ());
		}
		for (final Iterator it = toDelete.getPiecesOfWork ().iterator ();it.hasNext ();){
			pm.deletePersistent ((PieceOfWork)it.next ());
		}
		pm.deletePersistent (toDelete);
	}

}

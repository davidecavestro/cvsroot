/*
 * PersistentTaskTreeModel.java
 *
 * Created on December 18, 2006, 11:55 PM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.common.log.Logger;
import com.davidecavestro.common.undo.RBUndoManager;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import com.davidecavestro.timekeeper.persistence.PersistenceManager;
import com.davidecavestro.timekeeper.persistence.PersistenceNode;
import com.davidecavestro.timekeeper.persistence.Transaction;
import java.util.Collection;

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
	
	/**
	 * RIferimentoalla transazione fake, creata lazy.
	 */
	private Transaction _tx;
	/**
	 * Ritorna una transazione difacciata, con una implementazione vuota.
	 * <P>
	 * Scavalcare questo metodo per fornire una adeguata implementazione di Transactionse necessario (ad esempio una transazione JDO).
	 */
	protected Transaction getTransaction () {
		if (null == _tx) {
			_tx = new Transaction () {
				public void begin () {
					_persistenceNode.getPersistenceManager ().currentTransaction ().begin ();
				}
				public void commit () {
					_persistenceNode.getPersistenceManager ().currentTransaction ().commit ();
				}
				public void rollback () {
					_persistenceNode.getPersistenceManager ().currentTransaction ().rollback ();
				}
			};
		}
		return _tx;
	}
	
	/**
	 * RIferimentoalla transazione fake, creata lazy.
	 */
	private PersistenceManager _pm;
	/**
	 * Ritorna un persistence manager difacciata, con una implementazione vuota.
	 * <P>
	 * Scavalcare questo metodo per fornire una adeguata implementazione di PersistenceManager se necessario (ad esempio un PersistenceManager JDO).
	 */
	protected PersistenceManager getPersistenceManager () {
		if (null == _pm) {
			_pm = new PersistenceManager () {
				public void deletePersistent (Object o) {
					_persistenceNode.getPersistenceManager ().deletePersistent (o);
				}
				public void makePersistent (Object o) {
					_persistenceNode.getPersistenceManager ().makePersistent (o);
				}
				public void deletePersistentAll (Collection c) {
					_persistenceNode.getPersistenceManager ().deletePersistentAll (c);
				}
				public void makePersistentAll (Collection c) {
					_persistenceNode.getPersistenceManager ().makePersistentAll (c);
				}
			};
		}
		return _pm;
	}
		
}

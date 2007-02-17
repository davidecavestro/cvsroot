/*
 * PersistentWorkSpaceModel.java
 *
 * Created on January 3, 2007, 10:28 AM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.common.log.Logger;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import com.davidecavestro.timekeeper.persistence.PersistenceManager;
import com.davidecavestro.timekeeper.persistence.PersistenceNode;
import com.davidecavestro.timekeeper.persistence.PersistenceNodeException;
import com.davidecavestro.timekeeper.persistence.Transaction;
import java.util.Collection;

/**
 * Fornisce l'implementazione della persistenza.
 *
 * @author Davide Cavestro
 */
public class PersistentWorkSpaceModel extends WorkSpaceModelImpl {
	
	
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
	public PersistentWorkSpaceModel (final PersistenceNode persistenceNode, final ApplicationOptions applicationOptions, final Logger logger) {
		super ();
		_persistenceNode = persistenceNode;
		_logger = logger;
	}
	
	public void init () throws PersistenceNodeException {
		init (_persistenceNode.getAvailableWorkSpaces ());
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

/*
 * PersistenceManager.java
 *
 * Created on December 21, 2006, 12:33 AM
 *
 */

package com.davidecavestro.timekeeper.persistence;

import java.util.Collection;

/**
 * Interfaccia per l'accesso alle funzionalit&agrave; di gestione della persistenza.
 * <P>
 * Consente di disaccoppiare l'utilizzo della persistenza dall'effettiva implementazione utilizzata (JDO).
 *
 * @author Davide Cavestro
 */
public interface PersistenceManager {
	
	/**
	 * Rnùende persistente l'oggetto.
	 */
	void makePersistent (Object o);
	
	/**
	 * Rnùende persistente gli oggetti.
	 */
	void makePersistentAll (Collection c);
	
	/**
	 * RImuove la persistenza dell'oggetto.
	 */
	void deletePersistent (Object o);
	
	/**
	 * RImuove la persistenza gli oggetti.
	 */
	void deletePersistentAll (Collection c);
}

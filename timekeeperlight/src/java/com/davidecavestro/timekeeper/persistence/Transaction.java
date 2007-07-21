/*
 * Transaction.java
 *
 * Created on December 20, 2006, 11:04 PM
 *
 */

package com.davidecavestro.timekeeper.persistence;

/**
 * Interfaccia per l'accesso alle funzionalit&agrave; di logica transazionale.
 * <P>
 * Consente di disaccoppiare l'utilizzo delle transazioni dall'effettiva implementazione utilizzata (JDO).
 *
 * @author Davide Cavestro
 */
public interface Transaction {
	/**
	 * Inizia la transazione.
	 */
	void begin();
	
	/**
	 * Effettua il commit di questa transazione.
	 */
	void commit ();
	
	/**
	 * Effettua il rollback di questa transazione.
	 */
	void rollback ();
}

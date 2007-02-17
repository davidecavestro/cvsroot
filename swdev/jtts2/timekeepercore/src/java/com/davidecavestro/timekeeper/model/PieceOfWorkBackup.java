/*
 * PieceOfWorkBackup.java
 *
 * Created on December 26, 2006, 8:43 PM
 *
 */

package com.davidecavestro.timekeeper.model;

/**
 * Copia di backup dell'avanzamento.
 * @workaround dovuto a problemi di implementazione dell'undo/redo (annullamento campi interni con JDO)
 *
 * @author Davide Cavestro
 */
public interface PieceOfWorkBackup extends PieceOfWork {
	/**
	 * Ritorna il riferimento all'avanzamento originale da cui &egrave; stata estratta questa copia.
	 * @return l'avanzamento originale.
	 */
	PieceOfWork getSource ();
	/**
	 * Ripristina lo stato interno salvato in questo backup.
	 * @param pow l'avanzamento su cui ripristinare lo stato.
	 */
	void restore ();
}

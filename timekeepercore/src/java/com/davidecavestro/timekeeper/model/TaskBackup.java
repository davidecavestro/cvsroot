/*
 * TaskBackup.java
 *
 * Created on December 26, 2006, 8:40 PM
 *
 */

package com.davidecavestro.timekeeper.model;

/**
 * Copia di backup del task.
 * @workaround dovuto a problemi di implementazione dell'undo/redo (annullamento campi interni con JDO)
 *
 * @author Davide Cavestro
 */
public interface TaskBackup extends Task {
	/**
	 * Ritorna il riferimento al task originale da cui &egrave; stata estratta questa copia.
	 * @return il task originale.
	 */
	Task getSource ();
	
	/**
	 * Ripristina lo stato interno salvato in questo backup.
	 */
	void restore ();
}

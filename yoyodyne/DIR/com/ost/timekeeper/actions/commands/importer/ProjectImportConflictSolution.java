/*
 * ProjectImportConflictSolution.java
 *
 * Created on 12 marzo 2005, 20.20
 */

package com.ost.timekeeper.actions.commands.importer;

/**
 * Le possibili soluzioni a conflitti di importazione (progetto con stesso nome gia' eistente).
 *
 * @author  davide
 */
public final class ProjectImportConflictSolution {
	/**
	 * Rinomina.
	 */
	public final static ProjectImportConflictSolution RENAME = new ProjectImportConflictSolution ();
	/**
	 * Sovrascrittura.
	 */
	public final static ProjectImportConflictSolution OVERWRITE = new ProjectImportConflictSolution ();
	
	private ProjectImportConflictSolution (){};
}

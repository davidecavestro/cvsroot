/*
 * XMLResources.java
 *
 * Created on January 2, 2007, 3:06 PM
 *
 */

package com.davidecavestro.timekeeper.backup;

/**
 * Nomi degli elementi e degli attributidel file XML generato a scopo di backup.
 *
 * @todo introdurre l'XSD
 *
 * @author Davide Cavestro
 */
public interface XMLResources {
	/**
	 * L'elemento PROGETTO.
	 */
	public final static String PROJECT_ELEMENT = "project";
	
	/**
	 * Il tag di nodo.
	 */
	public final static String PROGRESSITEM_ELEMENT = "progressitem";
	/**
	 * Il tag di avanzamento.
	 */
	public final static String PROGRESS_ELEMENT = "progress";
	/**
	 * Il tag di inizio avanzamento.
	 */
	public final static String FROM_ELEMENT = "from";
	/**
	 * Il tag di fine avanzamento.
	 */
	public final static String TO_ELEMENT = "to";

	/**
	 * Il tag di descrizione dell'avanzamento.
	 */
	public final static String DESCRIPTION_ELEMENT = "description";
	/**
	 * Il tag di annotazioni dell'avanzamento.
	 */
	public final static String NOTES_ELEMENT = "notes";
	
	/**
	 * Il tag del nodo figlio.
	 */
	public final static String CHILDPROGRESS_ELEMENT = "child";
	
	/**
	 * Il tag del nodo di appartenenza.
	 */
	public final static String NODE_ELEMENT = "node";

	/**
	 * Il nome dell'attributo identificativo.
	 */
	public final static String ELEMENTID_PROPERTY = "id";
	
	/**
	 * Il nome dell'attributo TO.
	 */
	public final static String TO_PROPERTY = "to";
	
	/**
	 * Il nome dell'attributo FROM.
	 */
	public final static String FROM_PROPERTY = "from";
	
	/**
	 * Il nome dell'attributo CODE.
	 */
	public final static String CODE_PROPERTY = "code";
	
	/**
	 * Il nome dell'attributo NOME.
	 */
	public final static String NAME_PROPERTY = "name";
	
	/**
	 * Il nome dell'attributo DESCRIPTION.
	 */
	public final static String DESCRIPTION_PROPERTY = "description";
	
	/**
	 * Il nome dell'attributo NOTES.
	 */
	public final static String NOTES_PROPERTY = "notes";
}

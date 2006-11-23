/*
 * WorkSpace.java
 *
 * Created on November 11, 2006, 12:59 PM
 *
 */

package com.davidecavestro.timekeeper.model;

/**
 * Spacixio di lavoro.
 *
 * @author Davide Cavestro
 */
public interface WorkSpace {


	/**
	 * Ritorna il nome di questo progetto.
	 *
	 * @return il nome.
	 */	
	String getName ();
	
	/**
	 * Imposta il nome di questo progetto.
	 *
	 * @param name il nome da impostare.
	 */	
	void setName (String name);
	
	/**
	 * Ritorna la radice della gerarchia degli avanzamenti di questo progetto.
	 *
	 * @return la radice della gerarchia dei nodi di avanzamento.
	 */	
	Task getRoot ();
	
	
	/** 
	 * Imposta il nodo radice della gerarchia di avanzamenti.
	 *
	 * @param root la radice della gerarchia di nodi.
	 */
	void setRoot(Task root);
	
	/** 
	 * Ritorna la descrizione di questo progetto.
	 *
	 * @return la descrizione.
	 */
	String getDescription();
	
	/** 
	 * Imposta la descrizione di questo progetto.
	 *
	 * @param description la descrizione.
	 */
	void setDescription(String description);
	
	/** 
	 * Ritorna le annotazioni relative a questo progetto.
	 *
	 * @return le note.
	 */
	String getNotes();
	
	/** 
	 * Imposta le annotazioni relative a questo progetto.
	 *
	 * @param notes le note.
	 */
	void setNotes(String notes);
	
}

/*
 * Project.java
 *
 * Created on 18 aprile 2004, 14.25
 */

package com.ost.timekeeper.model;

/**
 * Implementa l'entità di tipo PROGETTO. 
 * I principali attributi di un progetto sono:
 *	<UL>
 *		<LI>il nome;
 *		<LI>una gerarchia di nodi sui quali è possibile effettuare avanzamenti.
 *	</UL>
 *
 * @author  davide
 */
public final class Project {
	
	/**
	 * La radice della gerarchia di avanzamenti.
	 */
	private ProgressItem root;
	
	/**
	 * Il nome.
	 */
	private String name;
	
	/** 
	 * La descrizione. 
	 */
	private String description;
	
	/**
	 * Le annotazioni.
	 */
	private String notes;
	
	/** 
	 * Costruttore vuoto.
	 */
	public Project() {
	}
	
	/**
	 * Costruttore con nome e gerarchia nodi.
	 * @param name
	 * @param root
	 */
	public Project(String name, ProgressItem root) {
		this.name = name;
		this.root = root;
	}
	
	/**
	 * Ritorna il nome di questo progetto.
	 *
	 * @return il nome.
	 */	
	public String getName (){
		return this.name;
	}
	
	/**
	 * Imposta il nome di questo progetto.
	 *
	 * @param name il nome da impostare.
	 */	
	public void setName (String name){
		this.name = name;
	}
	
	/**
	 * Ritorna la radice della gerarchia degli avanzamenti di questo progetto.
	 *
	 * @return la radice della gerarchia dei nodi di avanzamento.
	 */	
	public ProgressItem getRoot (){
		return this.root;
	}
	
	/**
	 * Ritorna la rappresentazione in formato stringa di questo progetto.
	 *
	 * @return una stringa che rappresenta questo progetto.
	 */	
	public String toString (){
		StringBuffer sb = new StringBuffer ();
		sb.append ("name: ").append (this.name)
		.append (" root: ").append (this.root);
		return sb.toString();
	}
	
	/** 
	 * Imposta il nodo radice della gerarchia di avanzamenti.
	 *
	 * @param root la radice della gerarchia di nodi.
	 */
	public void setRoot(ProgressItem root) {
		this.root=root;
	}
	
	/** 
	 * Ritorna la descrizione di questo progetto.
	 *
	 * @return la descrizione.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/** 
	 * Imposta la descrizione di questo progetto.
	 *
	 * @param description la descrizione.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/** 
	 * Ritorna le annotazioni relative a questo progetto.
	 *
	 * @return le note.
	 */
	public String getNotes() {
		return this.notes;
	}
	
	/** 
	 * Imposta le annotazioni relative a questo progetto.
	 *
	 * @param notes le note.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}

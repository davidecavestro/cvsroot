/*
 * ObserverCodes.java
 *
 * Created on 4 settembre 2004, 20.03
 */

package com.ost.timekeeper;

/**
 * Codici di notifica eventi di interesse generale per l'applicazione.
 *
 * @author  davide
 */
public interface ObserverCodes {
	
	/**
	 * Variazione del periodo di avanzamento corrente.
	 */
	public final static String CURRENT_PROGRESS_TIC = "CURRENT_PROGRESS_TIC ";
	
	/**
	 * Variazione degli attributi del nodo selezionato.
	 */
	public final static String SELECTEDNODE_INTERNALCHANGE = "SELECTEDNODE_INTERNALCHANGE";
	
	/**
	 * Variazione progetto.
	 */
	public final static String PROJECTCHANGE = "projectchange";
	
	/**
	 * Variazione nodo corrente del sistema.
	 * Per nodo corrente di sistema si intende il nodo attualmente in avanzamento.
	 * Esso coincide con il valore restituito dalla chiamata a 
	 * {@link com.ost.timekeeper.Application#getCurrentItem()}.
	 */
	public final static String CURRENTITEMCHANGE = "currentitemchange";
	
	/**
	 * Variazione del nodo selezionato.
	 */
	public final static String SELECTEDITEMCHANGE = "selecteditemchange";

	/**
	 * Variazione dell'avanzamento selezionato.
	 */
	public final static String SELECTEDPROGRESSCHANGE = "selectedprogresschange";
	
	/**
	 * Variazione dello stato di "elaborazione in corso".
	 */
	public final static String PROCESSINGCHANGE = "processingchange";
	
	/**
	 * Modifica delle impostazioni utente.
	 */
	public final static String USERSETTINGSCHANGE = "usersettingschange";

	/**
	 * Modifica delle impostazioni di sistema.
	 */
	public final static String SYSTEMSETTINGSCHANGE = "systemsettingschange";

	/**
	 * Modifica delle opzioni dell'applicazione.
	 */
	public final static String APPLICATIONOPTIONSCHANGE = "applicationoptionsschange";

	/**
	 * Applicazione in fase di chiusura.
	 */
	public final static String APPLICATIONEXITING = "applicationexiting";

	/**
	 * Applicazione in fase di modifica L&F.
	 */
	public final static String LOOKANDFEEL_CHANGING = "lookandfeelchanging";


}

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
	public final static String ITEMPROGRESSINGPERIODCHANGE = "itemprogressingperiodchange";
	
	/**
	 * Variazione del nodo in corso di avanzamento.
	 */
	public final static String ITEMPROGRESSINGCHANGE = "itemprogressingchange";
	
	/**
	 * Variazione progetto.
	 */
	public final static String PROJECTCHANGE = "projectchange";
	
	/**
	 * Variazione nodo di avanzamento corrente.
	 */
	public final static String CURRENTITEMCHANGE = "currentitemchange";
	
	/**
	 * Modifica selezione nodo di avanzamento.
	 */
	public final static String SELECTEDITEMCHANGE = "selecteditemchange";

	/**
	 * Modifica selezione avanzamento.
	 */
	public final static String SELECTEDPROGRESSCHANGE = "selectedprogresschange";
	
	/**
	 * Variazione stato "elaborazione in corso".
	 */
	public final static String PROCESSINGCHANGE = "processingchange";
	
	/**
	 * Modifica impostazioni utente.
	 */
	public final static String USERSETTINGSCHANGE = "usersettingschange";

	/**
	 * Modifica impostazioni sistema.
	 */
	public final static String SYSTEMSETTINGSCHANGE = "systemsettingschange";

	/**
	 * Modifica opzioni applicazione.
	 */
	public final static String APPLICATIONOPTIONSCHANGE = "applicationoptionsschange";

	/**
	 * Applicazione in fase di chiusura.
	 */
	public final static String APPLICATIONEXITING = "applicationexiting";


}

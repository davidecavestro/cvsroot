/*
 * Step.java
 *
 * Created on 24 aprile 2005, 19.52
 */

package com.ost.timekeeper.wizard;

import java.awt.Component;
import java.awt.Panel;
import javax.swing.Icon;

/**
 * Passo di procedura guidata.
 *
 * @author  davide
 */
public interface Step extends StepChangeNotifier {
	/**
	 * Ritorna il conmponente di interfaccia grafica di questo passo.
	 *
	 * @return il conmponente di interfaccia grafica di questo passo.
	 */	
	Component getUI ();
	
	/**
	 * Ritorna l'icona rappresentativa di questo passo.
	 *
	 * @return l'icona rappresentativa di questo passo.
	 */	
	Icon getIcon ();
	
	/**
	 * Applica le modifiche previste da questo passo di procedura.
	 */
	void apply ();
	
	/**
	 * Scarta le modifiche previste da questo passo di procedura.
	 */
	void abort ();
	
	/**
	 * Reimposta lo stato di passo di procedura.
	 */
	void configure ();
	
	/**
	 * Ritorna <TT>true</TT> se questo passo è valido.
	 * @return <TT>true</TT> se questo passo è valido.
	 */
	boolean isValid ();
	
	/**
	 * Ritorna <TT>true</TT> se questo passo è corrente.
	 * @return <TT>true</TT> se questo passo è corrente.
	 */
	boolean isCurrent ();
	
	/**
	 * Imposta il valore dello stato "corrente" per questo passo.
	 * @param current il nuovo stato.
	 */
	void setCurrent (final boolean current);
}

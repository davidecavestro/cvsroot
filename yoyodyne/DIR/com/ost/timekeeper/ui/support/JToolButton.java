/*
 * JToolButton.java
 *
 * Created on 11 aprile 2005, 21.50
 */

package com.ost.timekeeper.ui.support;

import java.awt.Insets;
import javax.swing.Action;
import javax.swing.JButton;

/**
 * Pulsante per toolbar.
 *
 * @author  davide
 */
public class JToolButton extends JButton {
	
	/** Costruttore vuoto. */
	public JToolButton () {
		makeToolButton (this);
	}
	
	/**
	 * Costruttore con azione.
	 * @param action l'azione.
	 */
	public JToolButton (final Action action) {
		makeToolButton (this, action);
	}
	
	/**
	 * Allinea il pulsante specificato allo standard dei pulsanti da toolbar..
	 *
	 * @param toolButton il pulsante.
	 */	
	public final static void makeToolButton (final JButton toolButton){
		toolButton.setBorderPainted (false);
		toolButton.setRolloverEnabled (true);
		toolButton.setMargin (new Insets (0,0,0,0));
		toolButton.setSize (25, 25);
	}
	
	/**
	 * Allinea il pulsante specificato allo standard dei pulsanti da toolbar, associandolo all'azione specificata.
	 *
	 * @param toolButton il pulsante.
	 * @param action l'azione.
	 */	
	public final static void makeToolButton (final JButton toolButton, final Action action){
		toolButton.setAction (action);
		toolButton.setText ("");
		toolButton.setBorderPainted (false);
		toolButton.setRolloverEnabled (true);
		toolButton.setMargin (new Insets (0,0,0,0));
		toolButton.setSize (25, 25);
	}
}

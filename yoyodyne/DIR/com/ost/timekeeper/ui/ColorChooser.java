/*
 * ColorChooser.java
 *
 * Created on 13 dicembre 2004, 22.16
 */

package com.ost.timekeeper.ui;

import java.awt.*;
import javax.swing.*;

/**
 * Componente per la scelta del colore.
 *
 * @author  davide
 */
public final class ColorChooser extends JButton{
	
	private Color _color;
	
	/**
	 *
	 * Costruttore.
	 *
	 */
	public ColorChooser () {
		super ();
		setInternalColor (null);
	}
	
	/**
	 *
	 * Costruttore con valore di default.
	 *
	 * @param defaultColor il colore predefinito.
	 */
	public ColorChooser (Color defaultColor) {
		super ();
		setInternalColor (defaultColor);
	}
	
	/**
	 * Imposta il valore scelto.
	 *
	 * @param color il valore scelto.
	 */	
	public void setColor (Color color){
		setInternalColor (color);
	}
	
	/**
	 * Ritorna il valore scelto.
	 *
	 * @return il valore scelto.
	 */	
	public Color getColor (){
		return this._color;
	}
	
	/**
	 * Imposta il campo interno, e aggiorna il colore di sfondo.
	 *
	 * @param color il colore.
	 */	
	private void setInternalColor (Color color){
		this._color = color;
		this.setBackground (color);
	}
}

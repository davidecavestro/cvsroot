/*
 * TopBorderPane.java
 *
 * Created on 26 febbraio 2005, 11.32
 */

package com.ost.timekeeper.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Pannello contenente una linea di separazione, con testo opzionale alla sinistra.
 *
 * @author  davide
 */
public class TopBorderPane extends JPanel{
	private TopBorder topBorder = new TopBorder ();
	private final JLabel label = new JLabel ();
	private Font titleFont;
	private Color titleColor;
	
	/**
	 * Costruttore vuoto.
	 */	
	public TopBorderPane () {
		this (null);
	}
	
	/**
	 * Costruttore con testo.
	 * @param text il testo.
	 */
	public TopBorderPane (final String text) {
		super (new BorderLayout ());
		setText (text);
//		topBorder.setBorder (new TitledBorder (labelText));
//		label.setBorder (new TitledBorder (new EmptyBorder (label.getInsets ()), labelText, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP));
//		label.setBorder (new TitledBorder (labelText));
        Font f = titleFont;
		if (f == null){
			f = UIManager.getFont("TitledBorder.font");
			titleFont = f;
		}
		Color c = titleColor;
		if (c==null){
			c = UIManager.getColor ("TitledBorder.titleColor");
			titleColor = c;
		}
		label.setFont (f);
		label.setForeground (c);
		this.add (label, BorderLayout.WEST);
		this.add (topBorder, BorderLayout.CENTER);
	}
	
//	public void setFont (Font f) {
//		super.setFont (f);
//		if (label!=null){
//			label.setFont (f);
//		}
//	}
//	
//	public void setForeground (Color c) {
//		super.setForeground (c);
//		if (label!=null){
//			label.setForeground (c);
//		}
//	}
	
	/**
	 * Imposta il testo.
	 *
	 * @param text il testo.
	 */	
	public void setText (final String text){
		String labelText = null;
		if (text!=null &&  text.length ()>0){
			/* stacca bordo dalla scritta */
			labelText = new StringBuffer (text).append (" ").toString ();
		} else {
			labelText = text;
		}
		this.label.setText (labelText);
	}
}

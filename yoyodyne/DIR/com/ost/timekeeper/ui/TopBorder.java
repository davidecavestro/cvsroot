/*
 * TopBorder.java
 *
 * Created on 26 febbraio 2005, 11.18
 */

package com.ost.timekeeper.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 *
 * @author  davide
 */
public class TopBorder extends JComponent{

	private EtchedBorder etchedBorder;
	
	/** Creates a new instance of TopBorder */
	public TopBorder () {
		etchedBorder = new EtchedBorder (EtchedBorder.LOWERED);
//		setBorder (new LineBorder (Color.BLACK));
	}
	
    /**
     * Paints the border for the specified component with the 
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
	int w = width;
	int h = height;
	
	g.translate(x, y);
	
	g.setColor(etchedBorder.getEtchType () == etchedBorder.LOWERED? etchedBorder.getShadowColor(c) : etchedBorder.getHighlightColor(c));
	g.drawRect(0, 0, w-2, h-2);
	
	g.setColor(etchedBorder.getEtchType () == etchedBorder.LOWERED? etchedBorder.getHighlightColor(c) : etchedBorder.getShadowColor(c));
	g.drawLine(1, h-3, 1, 1);
	g.drawLine(1, 1, w-3, 1);
	
	g.drawLine(0, h-1, w-1, h-1);
	g.drawLine(w-1, h-1, w-1, 0);
	
	g.translate(-x, -y);
    }

	protected void paintBorder (java.awt.Graphics g) {
		super.paintBorder (g);
		final int x  = this.getX ();
		final int y  = this.getY ();
		final int width  = this.getWidth ();
		final int height  = this.getHeight ();
		final int borderHeight = 3;
		final int borderY = y + (height/2);
		this.paintBorder (this, g, 0, borderY, width, borderHeight);
	}
	
}

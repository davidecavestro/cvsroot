/*
 * GradientPanel.java
 *
 * Created on 15 marzo 2005, 20.34
 */

package com.ost.timekeeper.ui.support;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Una pannello con sfondo a gradiente
 *
 * @author  davide
 */
public class GradientPanel extends JPanel {
	public final static int HORIZONTAL = 0;
	public final static int VERTICAL = 1;
	public final static int OBLIQUE = 2;
	
	private int _gradientType;
	
	public GradientPanel (LayoutManager lm, Color background, int gradientType) {
		super (lm);
		setBackground (background);
		this._gradientType = gradientType;
	}
	
	public GradientPanel (Color background, int gradientType) {
		super ();
		setBackground (background);
		this._gradientType = gradientType;
	}
	
	public int getGradientType (){return this._gradientType;}
	
	public void paintComponent (Graphics g) {
		super.paintComponent (g);
		if (!isOpaque ()) {
			return;
		}
		Color control = UIManager.getColor ("control");
		int width  = getWidth ();
		int height = getHeight ();
		
		Graphics2D g2 = (Graphics2D) g;
		Paint storedPaint = g2.getPaint ();
		if (this._gradientType==HORIZONTAL){
			g2.setPaint (new GradientPaint (0, 0, getBackground (), width, 0, control));
		} else if (this._gradientType==VERTICAL){
			g2.setPaint (new GradientPaint (0, 0, getBackground (), 0, height, control));
		} else {
			g2.setPaint (new GradientPaint (0, 0, getBackground (), width, height, control));
		}
		g2.fillRect (0, 0, width, height);
		g2.setPaint (storedPaint);
	}
}
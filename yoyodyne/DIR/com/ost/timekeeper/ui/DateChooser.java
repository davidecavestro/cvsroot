/*
 * DateChooser.java
 *
 * Created on 26 febbraio 2005, 12.12
 */

package com.ost.timekeeper.ui;

import com.toedter.calendar.*;
import java.awt.*;

/**
 *
 * @author  davide
 */
public class DateChooser extends JDateChooser{
	
	/** Creates a new instance of DateChooser */
	public DateChooser () {
	}
	
	public void setEnabled (boolean b) {
		super.setEnabled (b);
		Component[] components = this.getComponents ();
		for (int i=0;i<components.length;i++){
			components[i].setEnabled (b);
		}
	}
	
}

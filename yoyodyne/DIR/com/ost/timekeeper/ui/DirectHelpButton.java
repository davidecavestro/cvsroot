/*
 * DirectHelpButton.java
 *
 * Created on 28 dicembre 2004, 10.55
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.help.*;
import com.ost.timekeeper.util.*;
import java.awt.event.*;
import javax.help.*;
import javax.swing.*;

/**
 * Pulsante dedicato al lancio dell'help contestuale.
 *
 * @author  davide
 */
public class DirectHelpButton extends JButton {
	
	/** Costruttore. */
	public DirectHelpButton () {
		super (ResourceSupplier.getImageIcon (ResourceClass.UI, "contexthelp.png"));
		addActionListener (new CSH.DisplayHelpAfterTracking(HelpManager.getInstance ().getMainHelpBroker ()));
		this.setBorderPainted (false);
		this.setToolTipText (ResourceSupplier.getString (ResourceClass.UI, "controls", "direct.help"));
	}
	
}

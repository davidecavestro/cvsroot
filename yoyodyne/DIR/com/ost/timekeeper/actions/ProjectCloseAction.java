/*
 * ProjectCloseAction.java
 *
 * Created on 13 giugno 2004, 10.42
 */

package com.ost.timekeeper.actions;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 * Chiude il progetto attualmente aperto dall'applicazione.
 *
 * @author  davide
 */
public final class ProjectCloseAction extends javax.swing.AbstractAction implements Observer{
	
	/**
	 * Costruttore vuoto.
	 */
	public ProjectCloseAction () {
		super (ResourceSupplier.getString (ResourceClass.UI, "menu", "file.close")/*, ResourceSupplier.getImageIcon (ResourceClass.UI, "closeproject.gif")*/);
		this.putValue (SHORT_DESCRIPTION, ResourceSupplier.getString (ResourceClass.UI, "menu", "file.close.tooltip"));
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		this.execute ();
	}
	
	public void execute (){
		Application app = Application.getInstance ();
		app.setProject (null);
	}
	
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.PROJECT)){
				this.setEnabled (((Application)o).getProject ()!=null);
			}
		}
	}
}

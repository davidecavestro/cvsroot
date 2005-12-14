/*
 * NewBundleAction.java
 *
 * Created on 6 dicembre 2005, 23.14
 */

package com.davidecavestro.rbe.gui.actions;

import com.davidecavestro.rbe.model.DefaultResourceBundleModel;
import com.davidecavestro.rbe.model.ResourceBundleModel;
import javax.swing.AbstractAction;

/**
 * Crea e imposta nell'appliczione un nuovo bundle di risorse localizzate.
 *
 * @author  davide
 */
public class NewBundleAction extends AbstractAction {
	
	private final DefaultResourceBundleModel _rbm;
	
	/** Costruttore. */
	public NewBundleAction (DefaultResourceBundleModel rbm) {
		this._rbm = rbm;
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		//this._rbm.
	}
	
}

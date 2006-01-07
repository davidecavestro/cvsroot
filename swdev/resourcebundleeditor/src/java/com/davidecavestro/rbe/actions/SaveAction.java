/*
 * SaveAction.java
 *
 * Created on 17 dicembre 2005, 19.13
 */

package com.davidecavestro.rbe.actions;

import com.davidecavestro.rbe.model.DefaultResourceBundleModel;
import com.davidecavestro.rbe.model.LocalizationProperties;
import com.davidecavestro.rbe.model.ResourceBundleModel;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;

/**
 * salva i file di properties del bundle di risorse localizzate.
 *
 * @author  davide
 */
public class SaveAction extends AbstractAction implements PropertyChangeListener {
	
	private final DefaultResourceBundleModel _rbm;
	
	/** Costruttore. */
	public SaveAction (DefaultResourceBundleModel rbm) {
		this._rbm = rbm;
		this.setEnabled (false);
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		try {
			this._rbm.store ("Created by URBE");
		} catch (FileNotFoundException fnfe){
			fnfe.printStackTrace (System.err);
		} catch (IOException ioe){
			ioe.printStackTrace (System.err);
		}
	}
	
	public void propertyChange (java.beans.PropertyChangeEvent evt) {
		if (evt.getSource ()==this._rbm){
			if (evt.getPropertyName ().equals ("isModified")){
				this.setEnabled (this._rbm.isModified () && this._rbm.getPath ()!=null);
			}
		}
	}
}

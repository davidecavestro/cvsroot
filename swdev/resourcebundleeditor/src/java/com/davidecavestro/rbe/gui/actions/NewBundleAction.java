/*
 * NewBundleAction.java
 *
 * Created on 6 dicembre 2005, 23.14
 */

package com.davidecavestro.rbe.gui.actions;

import com.davidecavestro.rbe.gui.MainWindow;
import com.davidecavestro.rbe.gui.WindowManager;
import com.davidecavestro.rbe.model.DefaultResourceBundleModel;
import com.davidecavestro.rbe.model.LocalizationProperties;
import com.davidecavestro.rbe.model.ResourceBundleModel;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * Crea e imposta nell'appliczione un nuovo bundle di risorse localizzate.
 *
 * @author  davide
 */
public class NewBundleAction extends AbstractAction {
	
	private final DefaultResourceBundleModel _rbm;
	private WindowManager _wm;
	
	/**
	 * Costruttore.
	 * @param rbm le risorse localizzate.
	 */
	public NewBundleAction (DefaultResourceBundleModel rbm, WindowManager wm) {
		this._rbm = rbm;
		this._wm = wm;
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		if (this._rbm.isModified ()){
			if (
			JOptionPane.showConfirmDialog (
			this._wm.getMainWindow (), 
			java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Discard_all_changes?"))!=JOptionPane.OK_OPTION){
				return;
			}
		}
		
		this._rbm.setBundles (new LocalizationProperties [] {new LocalizationProperties (LocalizationProperties.DEFAULT, new Properties ())}); 
		String baseName = (String)JOptionPane.showInputDialog (this._wm.getMainWindow (), 
			java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Insert_bundle_base_name"),
			java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Insert_bundle_base_name"),
			JOptionPane.PLAIN_MESSAGE,
			null, null, "blank");
		this._rbm.setName (baseName!=null &&baseName.length ()>0?baseName:"blank");
		this._rbm.setPath (null);
	}
	
	
}

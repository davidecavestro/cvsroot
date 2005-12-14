/*
 * WIndowManager.java
 *
 * Created on 28 novembre 2005, 22.10
 */

package com.davidecavestro.rbe.gui;

import com.davidecavestro.common.gui.dialog.DialogListener;
import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.rbe.model.DefaultResourceBundleModel;
import com.davidecavestro.rbe.model.LocalizationProperties;
import com.davidecavestro.rbe.model.ResourceBundleModel;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * Il gestore delle finestre.
 *
 * @author  davide
 */
public class WindowManager implements ActionListener, DialogListener {

	private UIPersister _uiPersister;
	private DefaultResourceBundleModel _resourceBundleModel;
	
	
	/** Costruttore. */
	public WindowManager () {
	}
	
	/**
	 * Inizializza le risorse necessarie.
	 *
	 */
	public void init (final DefaultResourceBundleModel resourceBundleModel, final UIPersister uiPersister){
		this._uiPersister = uiPersister;
		this._resourceBundleModel = resourceBundleModel;
	}

	private MainWindow _mainWindow;	
	/**
	 * Ritorna la finestra principale.
	 * @return la finestra principale.
	 */
	public MainWindow getMainWindow (){
		if (this._mainWindow==null){
			this._mainWindow = new MainWindow (this._resourceBundleModel, this._uiPersister);
			this._uiPersister.register (this._mainWindow);
			this._mainWindow.addActionListener (this);
		}
		return this._mainWindow;
	}
	
	private AddEntryDialog _addEntryDialog;	
	/**
	 * Ritorna la dialog di inserimento nuova entry.
	 * @return la dialog di inserimento nuova entry.
	 */
	private AddEntryDialog getAddEntryDialog (){
		if (this._addEntryDialog==null){
			this._addEntryDialog = new AddEntryDialog (getMainWindow (), true);
			this._uiPersister.register (this._addEntryDialog);
			this._addEntryDialog.addDialogListener (this);
		}
		return this._addEntryDialog;
	}
	
	public void showEntryDialog (Locale l) {
		getAddEntryDialog ().showForLocale (l);		
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		if (e!=null && e.getActionCommand ()!=null){
			if (e.getActionCommand ().equals ("showAddEntryDialog")){
				Object source = e.getSource ();
				if (source instanceof MainWindow.NewEntryDialogRequester){
					showEntryDialog (((MainWindow.NewEntryDialogRequester)source).getLocale ());
				} else {
					showEntryDialog (LocalizationProperties.DEFAULT);
				}
			} else if (e.getActionCommand ().equals ("showAddLocaleDialog")){
				getAddLocaleDialog ().show ();
			}
		}
	}
	
	private AddLocaleDialog _addLocaleDialog;	
	/**
	 * Ritorna la dialog di inserimento nuova entry.
	 * @return la dialog di inserimento nuova entry.
	 */
	private AddLocaleDialog getAddLocaleDialog (){
		if (this._addLocaleDialog==null){
			this._addLocaleDialog = new AddLocaleDialog (getMainWindow (), true);
			this._uiPersister.register (this._addLocaleDialog);
			this._addLocaleDialog.addDialogListener (this);
		}
		return this._addLocaleDialog;
	}
	
	public void dialogChanged (com.davidecavestro.common.gui.dialog.DialogEvent e) {
		if (e.getSource ()==this._addLocaleDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				final Locale l = this._addLocaleDialog.getSelectedLocale ();
				if (this._resourceBundleModel.containsLocale (l)){
					JOptionPane.showMessageDialog (this._mainWindow, "Duplicate locale");
				} else {
					this._resourceBundleModel.addLocale (new LocalizationProperties (l, new Properties ()));
				}
			}
		} else if (e.getSource ()==this._addEntryDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				this._resourceBundleModel.addKey (this._addEntryDialog.getLocale (), this._addEntryDialog.getKeyText (), this._addEntryDialog.getValueText ());
			}
		}
		
	}
	
}

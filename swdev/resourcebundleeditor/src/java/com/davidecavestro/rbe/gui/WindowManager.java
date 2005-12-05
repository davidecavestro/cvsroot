/*
 * WIndowManager.java
 *
 * Created on 28 novembre 2005, 22.10
 */

package com.davidecavestro.rbe.gui;

import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.rbe.model.ResourceBundleModel;

/**
 * Il gestore delle finestre.
 *
 * @author  davide
 */
public class WindowManager {

	private UIPersister _uiPersister;
	private ResourceBundleModel _resourceBundleModel;
	
	
	/** Costruttore. */
	public WindowManager () {
	}
	
	/**
	 * Inizializza le risorse necessarie.
	 *
	 */
	public void init (final ResourceBundleModel resourceBundleModel, final UIPersister uiPersister){
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
			this._mainWindow = new MainWindow (this._resourceBundleModel);
			this._uiPersister.register (this._mainWindow);
		}
		return this._mainWindow;
	}
}

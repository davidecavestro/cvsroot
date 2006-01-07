/*
 * WIndowManager.java
 *
 * Created on 28 novembre 2005, 22.10
 */

package com.davidecavestro.rbe.gui;

import com.davidecavestro.common.gui.dialog.DialogListener;
import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.common.util.*;
import com.davidecavestro.rbe.ApplicationContext;
import com.davidecavestro.rbe.model.DefaultResourceBundleModel;
import com.davidecavestro.rbe.model.LocalizationProperties;
import com.davidecavestro.rbe.model.ResourceBundleModel;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 * Il gestore delle finestre.
 *
 * @author  davide
 */
public class WindowManager implements ActionListener, DialogListener {

	private ApplicationContext _context;
	
	
	/** Costruttore. */
	public WindowManager () {
	}
	
	/**
	 * Inizializza le risorse necessarie.
	 *
	 */
	public void init (final ApplicationContext context){
		this._context= context;
	}

	private MainWindow _mainWindow;	
	/**
	 * Ritorna la finestra principale.
	 * @return la finestra principale.
	 */
	public MainWindow getMainWindow (){
		if (this._mainWindow==null){
			this._mainWindow = new MainWindow (this._context);
			this._context.getUIPersisteer ().register (this._mainWindow);
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
			this._context.getUIPersisteer ().register (this._addEntryDialog);
			this._addEntryDialog.addDialogListener (this);
		}
		return this._addEntryDialog;
	}
	
	public void showEntryDialog (Locale l) {
		getAddEntryDialog ().showForLocale (l);		
	}
	
	private SpecifyBundleNameDialog _specifyBundleNameDialog;
	/**
	 * Ritorna la dialog di inserimento nuova entry.
	 * @return la dialog di inserimento nuova entry.
	 */
	private SpecifyBundleNameDialog getSpecifyBundleNameDialog (){
		if (this._specifyBundleNameDialog==null){
			this._specifyBundleNameDialog = new SpecifyBundleNameDialog (getMainWindow (), true);
			this._specifyBundleNameDialog.addDialogListener (this);
		}
		return this._specifyBundleNameDialog;
	}
	
	public String specifyBundleName (File f) {
		return getSpecifyBundleNameDialog ().showForFile (f);
	}
	
	private AddLocaleDialog _addLocaleDialog;	
	/**
	 * Ritorna la dialog di inserimento nuova entry.
	 * @return la dialog di inserimento nuova entry.
	 */
	private AddLocaleDialog getAddLocaleDialog (){
		if (this._addLocaleDialog==null){
			this._addLocaleDialog = new AddLocaleDialog (getMainWindow (), true);
			this._context.getUIPersisteer ().register (this._addLocaleDialog);
			this._addLocaleDialog.addDialogListener (this);
		}
		return this._addLocaleDialog;
	}
	
	private FindDialog _findDialog;	
	/**
	 * Ritorna la dialog di inserimento nuova entry.
	 * @return la dialog di inserimento nuova entry.
	 */
	public FindDialog getFindDialog (){
		if (this._findDialog==null){
			this._findDialog = new FindDialog (getMainWindow (), true, _context.getActionManager ().getFindNextAction ());
			this._findDialog.setLocationRelativeTo (null);
		}
		return this._findDialog;
	}
	
	public void dialogChanged (com.davidecavestro.common.gui.dialog.DialogEvent e) {
		if (e.getSource ()==this._addLocaleDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				final Locale l = this._addLocaleDialog.getSelectedLocale ();
				if (this._context.getModel ().containsLocale (l)){
					JOptionPane.showMessageDialog (this._mainWindow, "Duplicate locale");
				} else {
					this._context.getModel ().addLocale (new LocalizationProperties (l, new CommentedProperties ()));
				}
			}
		} else if (e.getSource ()==this._addEntryDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				this._context.getModel ().addKey (
					this._addEntryDialog.getLocale (), 
					this._addEntryDialog.getKeyText (), 
					this._addEntryDialog.getValueText (),
					this._addEntryDialog.getCommentText ()
					);
			}
		}
		
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
			} else if (e.getActionCommand ().equals ("showFindDialog")){
				getFindDialog ().show ();
			}
		}
	}
	
	public ApplicationContext getApplicationContext (){
		return this._context;
	}
}

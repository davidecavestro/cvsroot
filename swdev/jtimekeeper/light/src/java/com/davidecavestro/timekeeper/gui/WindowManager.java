/*
 * WIndowManager.java
 *
 * Created on 28 novembre 2005, 22.10
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.gui.dialog.DialogListener;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.Task;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import java.awt.event.ActionListener;
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

	private Splash _splash;	
	/**
	 * Ritorna la finestra principale.
	 * @return la finestra principale.
	 * @param appData i dati dell'applicazione.
	 * Sono necessari dato che tipicamente lo Splash viene usato prima
	 * diinizializzare il contesto applicativo.
	 */
	public Splash getSplashWindow (ApplicationData appData){
		if (this._splash==null){
			this._splash = new Splash (appData);
		}
		return this._splash;
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
	
	private NewPieceOfWorkDialog _mewPOWDialog;	
	/**
	 * Ritorna la dialog di inserimento nuovo avanzamento.
	 * @return la dialog di inserimento nuovo avanzamento.
	 */
	public NewPieceOfWorkDialog getNewPieceOfWorkDialog () {
		if (_mewPOWDialog==null){
			_mewPOWDialog = new NewPieceOfWorkDialog (getMainWindow (), true);
			_context.getUIPersisteer ().register (_mewPOWDialog);
			_mewPOWDialog.addDialogListener (this);
		}
		return _mewPOWDialog;
	}
	
	/**
	 * Rende visibile la dialog di inserimento nuovo avanzamento.
	 */
	public void showNewPieceOfWorkDialog (final Task parent) {
		getNewPieceOfWorkDialog ().showForTask (parent);
	}
	
	
	private NewTaskDialog _mewTaskDialog;	
	/**
	 * Ritorna la dialog di inserimento nuovo ytask.
	 * @return la dialog di inserimento nuovo ytask.
	 */
	public NewTaskDialog getNewTaskDialog () {
		if (this._mewTaskDialog==null){
			this._mewTaskDialog = new NewTaskDialog (getMainWindow (), true);
			this._context.getUIPersisteer ().register (this._mewTaskDialog);
			this._mewTaskDialog.addDialogListener (this);
		}
		return this._mewTaskDialog;
	}
	
	public void showNewTaskDialog (final Task parent) {
		getNewTaskDialog ().showForParent (parent);
	}
	

	
	private LogConsole _logConsole;	
	/**
	 * Ritorna la console dilog.
	 * @return la console dilog.
	 */
	public LogConsole getLogConsole (){
		if (this._logConsole==null){
			this._logConsole = new LogConsole (_context);
			this._context.getUIPersisteer ().register (this._logConsole);
		}
		return this._logConsole;
	}
	
	private OptionsDialog _optionsDialog;	
	/**
	 * Ritorna la dialog di impostazione delle opzioni.
	 * 
	 * @return la dialog di impostazione delle opzioni.
	 */
	public OptionsDialog getOptionsDialog (){
		if (this._optionsDialog==null){
			this._optionsDialog = new OptionsDialog (getMainWindow (), true, _context);
		}
		return this._optionsDialog;
	}
	
	public void dialogChanged (com.davidecavestro.common.gui.dialog.DialogEvent e) {
		if (e.getSource ()==this._mewTaskDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				final Task parent = this._mewTaskDialog.getParentTask ();
				this._context.getModel ().insertNodeInto (
					new ProgressItem (
						_mewTaskDialog.getCodeText (), 
						_mewTaskDialog.getNameText (), 
						_mewTaskDialog.getDescriptionText (), 
						_mewTaskDialog.getNotesText ()
					),
					parent, 
					-1
					);
			}
		} else if (e.getSource ()==_mewPOWDialog){
			if (e.getType ()==JOptionPane.OK_OPTION){
				final ProgressItem t = (ProgressItem)_mewPOWDialog.getTask ();
				final Progress p = new Progress (
						_mewPOWDialog.getFromDate (), 
						_mewPOWDialog.getToDate (), 
						t
					);
				p.setDescription (_mewPOWDialog.getDescriptionText ());
				this._context.getModel ().insertPieceOfWorkInto (
					p,
					t, 
					-1
					);
			}
		}
		
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		if (e!=null && e.getActionCommand ()!=null){
			if (e.getActionCommand ().equals ("showNewTaskDialog")){
				Object source = e.getSource ();
				showNewTaskDialog (((MainWindow.NewTaskDialogRequester)source).getParent ());
			}
		}
	}
	
	public ApplicationContext getApplicationContext (){
		return this._context;
	}
	
	private About _about;	
	/**
	 * Ritorna la dialog di inserimento nuova entry.
	 * @return la dialog di inserimento nuova entry.
	 */
	public About getAbout (){
		if (this._about==null){
			this._about = new About (getMainWindow (), true, _context);
		}
		return this._about;
	}

	
}

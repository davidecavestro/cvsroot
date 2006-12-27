/*
 * ApplicationContext.java
 *
 * Created on 21 dicembre 2005, 20.48
 */

package com.davidecavestro.timekeeper;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.common.help.HelpManager;
import com.davidecavestro.common.log.Logger;
import com.davidecavestro.common.undo.RBUndoManager;
import com.davidecavestro.timekeeper.model.TaskTreeModelExceptionHandler;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import com.davidecavestro.timekeeper.conf.UserSettings;
import com.davidecavestro.timekeeper.gui.WindowManager;
import com.davidecavestro.timekeeper.actions.ActionManager;
import com.davidecavestro.timekeeper.conf.ApplicationEnvironment;
import com.davidecavestro.timekeeper.model.TaskTreeModelImpl;
import com.davidecavestro.timekeeper.model.UndoableTaskTreeModel;
import com.davidecavestro.timekeeper.persistence.PersistenceNode;
import java.beans.PropertyChangeListener;

/**
 * Contesto applicativo.
 * Rappresenta l'abiente di esecuzione dell'applicazione, e consente l'accesso ai vari gestori.
 *
 * @author  davide
 */
public class ApplicationContext {
	
	private final WindowManager _windowManager;
	private final ApplicationOptions _applicationOptions;
	private final UIPersister _uiPersister;
	private final Logger _logger;
	private final UserSettings _userSettings;
	private final ApplicationData _applicationData;
	private final ApplicationEnvironment _env;
	private final UndoableTaskTreeModel _ttm;
	private final RBUndoManager _undoManager;
	private ActionManager _actionManager;
	private HelpManager _helpManager;
	private final TaskTreeModelExceptionHandler _propsExceptionHandler;
	
    private java.beans.PropertyChangeSupport changeSupport;
	
	private boolean _isProcessing;
	private final PersistenceNode _persistenceNode;

	
	/** 
	 * Costruttore.
	 */
	public ApplicationContext (
		final ApplicationEnvironment env,
		final ApplicationOptions applicationOptions,
		final WindowManager windowManager,
		final UIPersister uiPersister,
		final Logger logger,
		final UserSettings userSettings,
		final ApplicationData applicationData,
		final UndoableTaskTreeModel ttm,
		final RBUndoManager undoManager,
		final ActionManager actionManager,
		final HelpManager helpManager,
		final TaskTreeModelExceptionHandler propsExceptionHandler, 
		final PersistenceNode persistenceNode
		) {
			
		_env = env;
		_applicationOptions = applicationOptions;
		_logger = logger;
		_applicationData = applicationData;
		_windowManager = windowManager;
		_userSettings = userSettings;
		_uiPersister = uiPersister;
		_ttm = ttm;
		_undoManager = undoManager;
		_actionManager = actionManager;
		_helpManager = helpManager;
		_propsExceptionHandler = propsExceptionHandler;
		_persistenceNode = persistenceNode;
	}
	
	public ApplicationEnvironment getApplicationEnvironment (){
		return this._env;
	}
	
	public ApplicationOptions getApplicationOptions (){
		return this._applicationOptions;
	}
	
	public WindowManager getWindowManager (){
		return this._windowManager;
	}
	
	public UIPersister getUIPersisteer (){
		return this._uiPersister;
	}
	
	public UserSettings getUserSettings (){
		return this._userSettings;
	}
	
	public TaskTreeModelImpl getModel (){
		return this._ttm;
	}
	
	public RBUndoManager getUndoManager (){
		return this._undoManager;
	}
	
	public ActionManager getActionManager (){
		return this._actionManager;
	}
	
	public HelpManager getHelpManager (){
		return this._helpManager;
	}
	
	public TaskTreeModelExceptionHandler getPropertiesExceptionHandler (){
		return this._propsExceptionHandler;
	}	
	
	public ApplicationData getApplicationData (){
		return _applicationData;
	}
	
	public Logger getLogger (){
		return _logger;
	}
	
	public synchronized void addPropertyChangeListener (PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new java.beans.PropertyChangeSupport (this);
		}
		changeSupport.addPropertyChangeListener (listener);
	}
	
	public synchronized void removePropertyChangeListener (PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener (listener);
	}
	
	public synchronized PropertyChangeListener[] getPropertyChangeListeners () {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners ();
	}
	
	public synchronized void addPropertyChangeListener (String propertyName, PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new java.beans.PropertyChangeSupport (this);
		}
		changeSupport.addPropertyChangeListener (propertyName, listener);
	}
	
	public synchronized void removePropertyChangeListener (String propertyName, PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener (propertyName, listener);
	}
	
	public synchronized PropertyChangeListener[] getPropertyChangeListeners ( String propertyName) {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners (propertyName);
	}
	
	protected void firePropertyChange (String propertyName, Object oldValue, Object newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
	protected void firePropertyChange (String propertyName, boolean oldValue, boolean newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
	protected void firePropertyChange (String propertyName, int oldValue, int newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
	public void setProcessing (final boolean processing) {
		final boolean oldValue = this._isProcessing;
		this._isProcessing = processing;
		if (oldValue!=processing) {
			firePropertyChange ("isProcessing", oldValue, processing);
		}
	}
	
	public boolean isProcessing (){
		return this._isProcessing;
	}

	public PersistenceNode getPersistenceNode () {
		return _persistenceNode;
	}
}

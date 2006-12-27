/*
 * TaskTreeModelImpl.java
 *
 * Created on 2 dicembre 2005, 20.49
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import java.beans.PropertyChangeListener;

/**
 * Il modello di albero degli avanzamenti.
 *
 * @author  davide
 */
public class TaskTreeModelImpl extends AbstractTaskTreeModel {
	

	
    /**
     * If any <code>PropertyChangeListeners</code> have been registered,
     * the <code>changeSupport</code> field describes them.
     *
     * @serial
     * @since 1.2
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     * @see #firePropertyChange
     */
    private java.beans.PropertyChangeSupport changeSupport;
	
	private WorkSpace _workSpace;
	
	private final ApplicationOptions _applicationOptions;
	private final TaskTreeModelExceptionHandler _peh;
	
	private final static String[] voidStringArray = new String[0];
	
	/**
	 * Costruttore.
	 * @param applicationOptions le opzioni di configurazione.
	 * @param name il nome.
	 * @param resources le risorse di localizzazione.
	 */
	public TaskTreeModelImpl (final ApplicationOptions applicationOptions, final TaskTreeModelExceptionHandler peh, final WorkSpace workSpace) {
		super (workSpace.getRoot ());
		_applicationOptions = applicationOptions;
		_peh = peh;
		
		setWorkSpace (workSpace);
	}

	/**
	 * Imposta il nuovo workspace.
	 *Provvede inoltre alla notifica di variazione della property "name".
	 * 
	 * @param workSpace 
	 */
	public void setWorkSpace (final WorkSpace workSpace){
		final WorkSpace oldWS = _workSpace;
		this._workSpace = workSpace;
		setRoot (workSpace.getRoot ());
		reload (workSpace.getRoot ());
		final String oldName = oldWS!=null?oldWS.getName ():null;
		final String newName = workSpace!=null?workSpace.getName ():null;
		if (oldName!=newName)
		firePropertyChange ("name", oldName, newName);
	}
	

	
	/**
	 * Adds a PropertyChangeListener to the listener list. The listener is
	 * registered for all bound properties of this class, including the
	 * following:
	 * <ul>
	 *    <li>il 'name' di questo modello
	 *    <li>il 'path' di questo modello
	 * </ul>
	 * <p>
	 * If listener is null, no exception is thrown and no action is performed.
	 *
	 * @param    listener  the PropertyChangeListener to be added
	 *
	 * @see #removePropertyChangeListener
	 * @see #getPropertyChangeListeners
	 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public synchronized void addPropertyChangeListener (
	PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new java.beans.PropertyChangeSupport (this);
		}
		changeSupport.addPropertyChangeListener (listener);
	}
	
	/**
	 * Removes a PropertyChangeListener from the listener list. This method
	 * should be used to remove PropertyChangeListeners that were registered
	 * for all bound properties of this class.
	 * <p>
	 * If listener is null, no exception is thrown and no action is performed.
	 *
	 * @param listener the PropertyChangeListener to be removed
	 *
	 * @see #addPropertyChangeListener
	 * @see #getPropertyChangeListeners
	 * @see #removePropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
	 */
	public synchronized void removePropertyChangeListener (
	PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener (listener);
	}
	
	/**
	 * Returns an array of all the property change listeners
	 * registered on this component.
	 *
	 * @return all of this component's <code>PropertyChangeListener</code>s
	 *         or an empty array if no property change
	 *         listeners are currently registered
	 *
	 * @see      #addPropertyChangeListener
	 * @see      #removePropertyChangeListener
	 * @see      #getPropertyChangeListeners(java.lang.String)
	 * @see      java.beans.PropertyChangeSupport#getPropertyChangeListeners
	 * @since    1.4
	 */
	public synchronized PropertyChangeListener[] getPropertyChangeListeners () {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners ();
	}
	
	/**
	 * Adds a PropertyChangeListener to the listener list for a specific
	 * property. The specified property may be user-defined, or one of the
	 * following:
	 * <ul>
	 *    <li>il 'name' di questo modello
	 *    <li>il 'path' di questo modello
	 * </ul>
	 * Note that if this Component is inheriting a bound property, then no
	 * event will be fired in response to a change in the inherited property.
	 * <p>
	 * If listener is null, no exception is thrown and no action is performed.
	 *
	 * @param propertyName one of the property names listed above
	 * @param listener the PropertyChangeListener to be added
	 *
	 * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 * @see #getPropertyChangeListeners(java.lang.String)
	 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public synchronized void addPropertyChangeListener (
	String propertyName,
	PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new java.beans.PropertyChangeSupport (this);
		}
		changeSupport.addPropertyChangeListener (propertyName, listener);
	}
	
	/**
	 * Removes a PropertyChangeListener from the listener list for a specific
	 * property. This method should be used to remove PropertyChangeListeners
	 * that were registered for a specific bound property.
	 * <p>
	 * If listener is null, no exception is thrown and no action is performed.
	 *
	 * @param propertyName a valid property name
	 * @param listener the PropertyChangeListener to be removed
	 *
	 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 * @see #getPropertyChangeListeners(java.lang.String)
	 * @see #removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public synchronized void removePropertyChangeListener (
	String propertyName,
	PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener (propertyName, listener);
	}
	
	/**
	 * Returns an array of all the listeners which have been associated
	 * with the named property.
	 *
	 * @return all of the <code>PropertyChangeListeners</code> associated with
	 *         the named property or an empty array if no listeners have
	 *         been added
	 *
	 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 * @see #getPropertyChangeListeners
	 * @since 1.4
	 */
	public synchronized PropertyChangeListener[] getPropertyChangeListeners (
	String propertyName) {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners (propertyName);
	}
	
	/**
	 * Support for reporting bound property changes for Object properties.
	 * This method can be called when a bound property has changed and it will
	 * send the appropriate PropertyChangeEvent to any registered
	 * PropertyChangeListeners.
	 *
	 * @param propertyName the property whose value has changed
	 * @param oldValue the property's previous value
	 * @param newValue the property's new value
	 */
	protected void firePropertyChange (String propertyName,
	Object oldValue, Object newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
	/**
	 * Support for reporting bound property changes for boolean properties.
	 * This method can be called when a bound property has changed and it will
	 * send the appropriate PropertyChangeEvent to any registered
	 * PropertyChangeListeners.
	 *
	 * @param propertyName the property whose value has changed
	 * @param oldValue the property's previous value
	 * @param newValue the property's new value
	 */
	protected void firePropertyChange (String propertyName,
	boolean oldValue, boolean newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
	/**
	 * Support for reporting bound property changes for integer properties.
	 * This method can be called when a bound property has changed and it will
	 * send the appropriate PropertyChangeEvent to any registered
	 * PropertyChangeListeners.
	 *
	 * @param propertyName the property whose value has changed
	 * @param oldValue the property's previous value
	 * @param newValue the property's new value
	 */
	protected void firePropertyChange (String propertyName,
	int oldValue, int newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
}

/*
 * TransferActionListener.java
 *
 * Created on December 3, 2006, 11:45 AM
 *
 */

package com.davidecavestro.timekeeper.gui.dnd;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JComponent;

/**
 * Binding azioni di cut&paste.
 * Per abilitare un componente all'utilizzo di questo listener, occorre registrarlo tramite il metodo <CODE>register</CODE>.
 *
 * @author Davide Cavestro
 */
public class TransferActionListener implements ActionListener, PropertyChangeListener {
	private JComponent focusOwner = null;
	
	/**
	 * Set contenente i componenti registrati per i quali quindi si deve gestire il transfer.
	 */
	private final Set<JComponent> eligible = new HashSet<JComponent> ();
	
    /**
     * A description of any PropertyChangeListeners which have been registered.
     */
    private PropertyChangeSupport changeSupport;
	
	
	public TransferActionListener () {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager ();
		manager.addPropertyChangeListener ("permanentFocusOwner", this);
	}
	
	public void register (JComponent c) {
		eligible.add (c);
	}
	
	public void propertyChange (PropertyChangeEvent e) {
		Object o = e.getNewValue ();
		if (o instanceof JComponent && eligible.contains ((JComponent)o)) {
			final JComponent oldValue = focusOwner;
			focusOwner = (JComponent)o;
			firePropertyChange ("focusOwner", oldValue, focusOwner);
//		} else {
//			focusOwner = null;
		}
	}
	
	public void actionPerformed (ActionEvent e) {
		if (focusOwner == null)
			return;
		String action = (String)e.getActionCommand ();
		Action a = focusOwner.getActionMap ().get (action);
		if (a != null) {
			a.actionPerformed (new ActionEvent (focusOwner,
				ActionEvent.ACTION_PERFORMED,
				null));
		}
	}
	
	
	
	
    /**
     * Adds a PropertyChangeListener to the listener list. The listener is
     * registered for all bound properties of this class, including the
     * following:
     * <ul>
     *    <li>the focus owner ("focusOwner")</li>
     * </ul>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param listener the PropertyChangeListener to be added
     * @see #removePropertyChangeListener
     * @see #getPropertyChangeListeners
     * @see #addPropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
	    synchronized (this) {
	        if (changeSupport == null) {
		    changeSupport = new PropertyChangeSupport(this);
		}
		changeSupport.addPropertyChangeListener(listener);
	    }
	}
    }

    /**
     * Removes a PropertyChangeListener from the listener list. This method
     * should be used to remove the PropertyChangeListeners that were
     * registered for all bound properties of this class.
     * <p>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param listener the PropertyChangeListener to be removed
     * @see #addPropertyChangeListener
     * @see #getPropertyChangeListeners
     * @see #removePropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
	    synchronized (this) {
	        if (changeSupport != null) {
		    changeSupport.removePropertyChangeListener(listener);
		}
	    }
	}
    }

    /**
     * Returns an array of all the property change listeners
     * registered on this class.
     *
     * @return all of this class
     *         <code>PropertyChangeListener</code>s
     *         or an empty array if no property change 
     *         listeners are currently registered
     *
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     * @see #getPropertyChangeListeners(java.lang.String)
     * @since 1.4
     */
    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        return changeSupport.getPropertyChangeListeners();
    }

    /**
     * Adds a PropertyChangeListener to the listener list for a specific
     * property. The specified property may be user-defined, or one of the
     * following:
     * <ul>
     *    <li>the focus owner ("focusOwner")</li>
     * </ul>
     * If listener is null, no exception is thrown and no action is performed.
     *
     * @param propertyName one of the property names listed above
     * @param listener the PropertyChangeListener to be added
     * @see #addPropertyChangeListener(java.beans.PropertyChangeListener)
     * @see #removePropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
     * @see #getPropertyChangeListeners(java.lang.String)
     */
    public void addPropertyChangeListener(String propertyName,
					  PropertyChangeListener listener) {
        if (listener != null) {
	    synchronized (this) {
	        if (changeSupport == null) {
		    changeSupport = new PropertyChangeSupport(this);
		}
		changeSupport.addPropertyChangeListener(propertyName,
							listener);
	    }
	}
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
     * @see #addPropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
     * @see #getPropertyChangeListeners(java.lang.String)
     * @see #removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(String propertyName,
					     PropertyChangeListener listener) {
        if (listener != null) {
	    synchronized (this) {
	        if (changeSupport != null) {
		    changeSupport.removePropertyChangeListener(propertyName,
							       listener);
		}
	    }
	}
    }

    /**
     * Returns an array of all the <code>PropertyChangeListener</code>s
     * associated with the named property.
     *
     * @return all of the <code>PropertyChangeListener</code>s associated with
     *         the named property or an empty array if no such listeners have
     *         been added.
     *         
     * @see #addPropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
     * @see #removePropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
     * @since 1.4
     */
    public synchronized PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        return changeSupport.getPropertyChangeListeners(propertyName);
    }

    /**
     * Fires a PropertyChangeEvent in response to a change in a bound property.
     * The event will be delivered to all registered PropertyChangeListeners.
     * No event will be delivered if oldValue and newValue are the same.
     *
     * @param propertyName the name of the property that has changed
     * @param oldValue the property's previous value
     * @param newValue the property's new value
     */
    protected void firePropertyChange(String propertyName, Object oldValue,
                                      Object newValue) {
        PropertyChangeSupport changeSupport = this.changeSupport;
        if (changeSupport != null) {
	    changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
    }	
	
}

/*
 * TransferAction.java
 *
 * Created on December 3, 2006, 11:37 AM
 *
 */

package com.davidecavestro.timekeeper.gui.dnd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.TransferHandler;
import javax.swing.event.EventListenerList;

/**
 * Azione di trasferimento dati (da usare per i widget di cut/copy&paste).
 *
 * @author Davide Cavestro
 */
public class TransferAction extends AbstractAction {
	
	public enum Type {
		CUT,
		COPY,
		PASTE
	}
	
	/**
	 * Costruttore.
	 */
	public TransferAction (final Type t, final TransferActionListener tal) {
		super ((String)
		(
			t==Type.CUT
			?
				TransferHandler.getCutAction ()
				:
				(
			t==Type.PASTE
			?
				TransferHandler.getPasteAction ()
				:
				TransferHandler.getCopyAction ()
				)
				)
		
		.getValue (Action.NAME));
		
		addActionListener (tal);
	}
	
    /** A list of event listeners for this component. */
    protected EventListenerList listenerList = new EventListenerList();
	
	 /**
     * Adds an <code>ActionListener</code> to the button.
     * @param l the <code>ActionListener</code> to be added
     */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }
    
    /**
     * Removes an <code>ActionListener</code> from the button.
     * If the listener is the currently set <code>Action</code>
     * for the button, then the <code>Action</code>
     * is set to <code>null</code>.
     *
     * @param l the listener to be removed
     */
    public void removeActionListener(ActionListener l) {
	    listenerList.remove(ActionListener.class, l);
    }
    
    /**
     * Returns an array of all the <code>ActionListener</code>s added
     * to this AbstractButton with addActionListener().
     *
     * @return all of the <code>ActionListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ActionListener[] getActionListeners() {
        return (ActionListener[])(listenerList.getListeners(
            ActionListener.class));
    }

	public void actionPerformed (ActionEvent e) {
		fireActionPerformed (new ActionEvent (e.getSource (), ActionEvent.ACTION_PERFORMED, (String)getValue (Action.NAME)));
	}
	

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the <code>event</code> 
     * parameter.
     *
     * @param event  the <code>ActionEvent</code> object
     * @see EventListenerList
     */
    protected void fireActionPerformed(ActionEvent event) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ActionListener.class) {
                // Lazily create the event:
                if (e == null) {
                      String actionCommand = event.getActionCommand();
                      if(actionCommand == null) {
                         actionCommand = event.getActionCommand();
                      }
                      e = new ActionEvent(event.getSource (),
                                          ActionEvent.ACTION_PERFORMED,
                                          actionCommand,
                                          event.getWhen(),
                                          event.getModifiers());
                }
                ((ActionListener)listeners[i+1]).actionPerformed(e);
            }          
        }
    }
  	
}

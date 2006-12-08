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
import java.util.HashSet;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JComponent;

/**
 * Binding azioni di cut&paste.
 * Per abilitare uncomponente all'utilizzo di questo listener, occorre registrarlo tramite il metodo <CODE>register</CODE>.
 *
 * @author Davide Cavestro
 */
public class TransferActionListener implements ActionListener, PropertyChangeListener {
	private JComponent focusOwner = null;
	
	private final Set<JComponent> eligible = new HashSet<JComponent> ();
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
			focusOwner = (JComponent)o;
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
}

/*
 * ObservablePanel.java
 *
 * Created on 31 dicembre 2004, 17.15
 */

package com.ost.timekeeper.ui;

import java.util.*;
import javax.swing.*;

/**
 * Pannello con gestore
 *
 * @author  davide
 */
public class ObservablePanel extends JPanel{
	
	private final InnerObservable _notifier = new InnerObservable ();
	
	/** Costruttore. */
	public ObservablePanel () {
	}
	
	public final void addObserver (Observer o){
		this._notifier.addObserver (o);
	}
	
	public final void notifyObservers (Object arg) {
		this._notifier.notifyObservers (arg);
	}
	
	public final synchronized void deleteObservers () {
		this._notifier.deleteObservers ();
	}
	
	protected final void setChanged (){
		this._notifier.setChanged ();
	}
	
	private final class InnerObservable extends Observable {
		public void setChanged (){
			super.setChanged ();
		}
	}
}

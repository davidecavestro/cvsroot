/*
 * ApplicationOptionsNotifier.java
 *
 * Created on 13 dicembre 2004, 23.06
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.*;
import java.util.*;

/**
 * Notifica gli osservatori registrati delle modifiche avvenute alle impostazioni applicative.
 *
 * @author  davide
 */
public final class ApplicationOptionsNotifier extends Observable implements Observer{
	
	/**
	 * Istanza del singleton.
	 */
	private static ApplicationOptionsNotifier _instance;
	
	/** 
	 * Costruttore.
	 */
	private ApplicationOptionsNotifier () {
	}
	
	/**
	 * Ritorna l'istanza di questo notifier.
	 *
	 * @return l'istanza.
	 */	
	public static ApplicationOptionsNotifier getInstance (){
		if (_instance==null){
			_instance = new ApplicationOptionsNotifier ();
			UserSettingsNotifier.getInstance ().addObserver (_instance);
		}
		return _instance;
	}
	
	/**
	 * Marca questo notifier come modificato.
	 */
    protected synchronized void setChanged() {
		super.setChanged ();
    }
	
	public void update (Observable o, Object arg) {
		if (arg!=null && arg.equals (ObserverCodes.USERSETTINGSCHANGE)){
			this.setChanged ();
			this.notifyObservers (ObserverCodes.APPLICATIONOPTIONSCHANGE);
		}
	}
	
}

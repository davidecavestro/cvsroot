/*
 * UserSettingsNotifier.java
 *
 * Created on 13 dicembre 2004, 22.58
 */

package com.ost.timekeeper.conf;

import java.util.*;

/**
 * Notifica gli osservatori registrati delle modifiche avvenute alle impostazioni utente.
 *
 * @author  davide
 */
public final class UserSettingsNotifier extends Observable{
	
	/**
	 * Istanza del singleton.
	 */
	private static UserSettingsNotifier _instance;
	
	/** 
	 * Costruttore.
	 */
	private UserSettingsNotifier () {
	}
	
	public static UserSettingsNotifier getInstance (){
		if (_instance==null){
			_instance = new UserSettingsNotifier ();
		}
		return _instance;
	}
	
	/**
	 * Marca questo notifier come modificato.
	 */
    protected synchronized void setChanged() {
		super.setChanged ();
    }
}

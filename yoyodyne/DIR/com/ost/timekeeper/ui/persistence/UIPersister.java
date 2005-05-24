/*
 * UIPersister.java
 *
 * Created on 16 maggio 2005, 22.41
 */

package com.ost.timekeeper.ui.persistence;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.ObserverCodes;
import com.ost.timekeeper.conf.SettingsSupport;
import com.ost.timekeeper.conf.UserSettings;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

/**
 * Gestore della persistenza per l'interfaccia utente (posizione form, dimensioni controlli).
 *
 * @author  davide
 */
public class UIPersister implements Observer{
	
	private final List _register = new ArrayList ();
	
	private final UserUIStorage _userUIStorage = new UserUIStorage();
		
	private static UIPersister _instance;
	
	/** Costruttore. */
	private UIPersister () {
		Application.getInstance ().addObserver (this);
	}
	
	/**
	 * Ritorna l'istanza del singleton.
	 *
	 * @return l'istanza del singleton.
	 */	
	public final static UIPersister getInstance (){
		if (_instance==null){
			_instance=new UIPersister ();
		}
		return _instance;
	}
	
	/**
	 * Registra il componente specificato per la persistenza.
	 *
	 * @param component il componente.
	 * @param init <TT>true</TT> per inizializzare il componente con i dati precedentemente salvati.
	 * @return <TT>true</TT> se il <TT>component</TT> è stato inizializzato con i dati persistenti.
	 */	
	public boolean register (final PersistentComponent component, final boolean init) {
		if (!this._register.contains (component)){
			this._register.add (component);
		}
		if (init){
//			final Rectangle savedData = this.getPersistentData (component);
//			if (savedData!=null){
				return component.restorePersistent (this._userUIStorage);
//				return true;
//			}
		}
		return false;
	}
	
	/**
	 * Rimuove il componente specificato dal registro.
	 *
	 * @param component il componente da rimuovere dal registro.
	 */	
	public void unregister (final PersistentComponent component){
		this._register.remove (component);
	}
	
	/**
	 * Salva le impostazioni di tuttii componenti registrati.
	 */
	public void makePersistentAll (){
		for (final Iterator it = this._register.iterator ();it.hasNext ();){
			((PersistentComponent)it.next ()).makePersistent (this._userUIStorage);
		}
	}
	
	public void update (Observable o, Object arg) {
		if (arg!=null && arg.equals (ObserverCodes.APPLICATIONEXITING)){
			makePersistentAll ();
		}
	}
	
	private final class UserUIStorage implements PersistenceStorage {
		
		private final Properties _userProperties = UserSettings.getInstance ().getProperties ();
		
		public java.util.Properties getRegistry () {
			return _userProperties;
		}
		
	}
}

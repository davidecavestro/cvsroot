/*
 * ApplicationOptions.java
 *
 * Created on 4 dicembre 2004, 14.12
 */

package com.davidecavestro.rbe.conf;

import javax.swing.UIManager;

/**
 * Opzioni di configurazione dell'applicazione. E' possibile implementare una catena
 * di responsabilità, innestando diversi oggetti di questo tipo.
 *
 * @author  davide
 */
public final class ApplicationOptions {
	
	/**
	 * Le impostazioni.
	 */
	private ApplicationSettings _settings;
	
	/**
	 * L'anello successore nella catena di responsabilità.
	 */
	private ApplicationOptions _successor;
	
	/**
	 *
	 * Costruttore privato, evita istanzazione dall'esterno.
	 *
	 * @param settings le impostazioni.
	 * @param successor l'anello successore nella catena di responsabilità.
	 */
	public ApplicationOptions (ApplicationSettings settings, ApplicationOptions successor) {
		this._settings = settings;
		this._successor = successor;
	}
	
	
	public String getLogDirPath (){
		final String returnValue = this._settings.getLogDirPath ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue;
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.getLogDirPath ();
			} else {
					/*
					 * Informazione non disponibile.
					 */
				return null;
			}
		}
	}
	

	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public int getPlainTextLogBufferSize (){
		final Integer returnValue = this._settings.getPlainTextLogBufferSize ();
		if (returnValue!=null){
			/*
			 * Risposta locale.
			 */
			return returnValue.intValue ();
		} else {
			if (_successor!=null){
				/*
				 * Delega successore.
				 */
				return _successor.getPlainTextLogBufferSize ();
			} else {
				/*
				 * Informazione non disponibile.
				 * Funzionalità disabilitata.
				 */
				return 8192;
			}
		}
	}
	
	
	/**
	 * Ritorna il L&F impostato.
	 *
	 * @return il L&F impostato.
	 */	
	public String getLookAndFeel (){
		final String returnValue = this._settings.getLookAndFeel ();
		if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
			return returnValue;
		} else {
			if (_successor!=null){
					/*
					 * Delega successore.
					 */
				return _successor.getLookAndFeel ();
			} else {
					/*
					 * Default di sistema non disponibile.
					 */
				return UIManager.getSystemLookAndFeelClassName ();
			}
		}
	}

}

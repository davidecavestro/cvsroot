/*
 * ApplicationOptions.java
 *
 * Created on 4 dicembre 2004, 14.12
 */

package com.davidecavestro.timekeeper.conf;

import javax.swing.UIManager;

/**
 * Opzioni di configurazione dell'applicazione. E' possibile implementare una catena
 * di responsabilita'', innestando diversi oggetti di questo tipo.
 *
 * @author  davide
 */
public final class ApplicationOptions {
	
	/**
	 * Le impostazioni.
	 */
	private ApplicationSettings _settings;
	
	/**
	 * L'anello successore nella catena di responsabilita''.
	 */
	private ApplicationOptions _successor;
	
	/**
	 *
	 * Costruttore privato, evita istanzazione dall'esterno.
	 *
	 * @param settings le impostazioni.
	 * @param successor l'anello successore nella catena di responsabilita''.
	 */
	public ApplicationOptions (ApplicationSettings settings, ApplicationOptions successor) {
		_settings = settings;
		_successor = successor;
	}
	
	
	public String getLogDirPath (){
		final String returnValue = _settings.getLogDirPath ();
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
		final Integer returnValue = _settings.getPlainTextLogBufferSize ();
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
				 * Funzionalit� disabilita'ta.
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
		final String returnValue = _settings.getLookAndFeel ();
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


	

	
	/**
	 * Ritorna il percorso per il database.
	 *
	 * @return il percorso per il database.
	 */	
	public String getJDOStorageDirPath (){
		final String returnValue = _settings.getJDOStorageDirPath ();
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
				return _successor.getJDOStorageDirPath ();
			} else {
				/*
				 * Informazione non disponibile
				 */
				return null;
			}
		}
	}

	/**
	 * Ritorna il nome del db.
	 *
	 * @return il nome del db.
	 */	
	public String getJDOStorageName (){
		final String returnValue = _settings.getJDOStorageName ();
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
				return _successor.getJDOStorageName ();
			} else {
				/*
				 * Informazione non disponibile
				 */
				return null;
			}
		}
	}
	
	/**
	 * Ritorna il nome del db.
	 *
	 * @return il nome del db.
	 */	
	public String getJDOUserName (){
		final String returnValue = _settings.getJDOUserName ();
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
				return _successor.getJDOUserName ();
			} else {
				/*
				 * Informazione non disponibile
				 */
				return null;
			}
		}
	}
	
	/**
	 * Ritorna il nome dell'ultimo progettoutilizzato.
	 *
	 * @return il nome dell'ultimo progettoutilizzato.
	 */	
	public String getLastProjectName (){
		final String returnValue = _settings.getLastProjectName ();
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
				return _successor.getLastProjectName ();
			} else {
				/*
				 * Informazione non disponibile
				 */
				return null;
			}
		}
	}
	
}
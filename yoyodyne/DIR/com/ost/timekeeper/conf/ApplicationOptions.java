/*
 * ApplicationOptions.java
 *
 * Created on 4 dicembre 2004, 14.12
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.view.*;
import java.awt.*;

/**
 * Opzioni di configurazione dell'applicazione. E' possibile implementare una catena
 * di responsabilità, innestando diversi oggetti diquesto tipo.
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
		
	/** 
	 * Ritorna la posizione iniziale della finestra principale.
	 * @return la posizione iniziale della finestra principale.
	 */	
		public Rectangle getMainFormBounds (){
			final Rectangle returnValue = this._settings.getMainFormBounds ();
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
					return _successor.getMainFormBounds ();
				} else {
					/*
					 * Informazione non disponibile.
					 */
					return null;
				}
			}
		}
		
	/** 
	 * Ritorna la posizione iniziale della finestra di dettaglion odo di avanzamento.
	 * @return la posizione iniziale della finestra di dettaglion odo di avanzamento.
	 */	
	public Rectangle getProgressItemInspectorBounds (){
			final Rectangle returnValue = this._settings.getProgressItemInspectorBounds ();
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
					return _successor.getProgressItemInspectorBounds ();
				} else {
					/*
					 * Informazione non disponibile.
					 */
					return null;
				}
			}
		}
	
	/** 
	 * Ritorna la posizione iniziale della finestra di dettaglio periodo di avanzamento.
	 * @return la posizione iniziale della finestra di dettaglio periodo di avanzamento.
	 */	
	public Rectangle getProgressPeriodInspectorBounds (){
			final Rectangle returnValue = this._settings.getProgressPeriodInspectorBounds ();
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
					return _successor.getProgressPeriodInspectorBounds ();
				} else {
					/*
					 * Informazione non disponibile.
					 */
					return null;
				}
			}
		}
		
	/** 
	 * Ritorna la posizione iniziale della finestra di elenco avanzamenti.
	 * @return la posizione iniziale della finestra di elenco avanzamenti.
	 */	
	public Rectangle getProgressListFrameBounds (){
			final Rectangle returnValue = this._settings.getProgressListFrameBounds ();
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
					return _successor.getProgressListFrameBounds ();
				} else {
					/*
					 * Informazione non disponibile.
					 */
					return null;
				}
			}
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
	 * Ritorna il colore del desktop.
	 *
	 * @return ilcolore del desktop.
	 */	
	public Color getDesktopColor () {
			final Color returnValue = this._settings.getDesktopColor ();
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
					return _successor.getDesktopColor ();
				} else {
					/*
					 * Informazione non disponibile.
					 */
					return null;
				}
			}
	}
	
	public boolean beepOnEvents () {
			final Boolean returnValue = this._settings.beepOnEvents ();
			if (returnValue!=null){
				/*
				 * Risposta locale.
				 */
				return returnValue.booleanValue ();
			} else {
				if (_successor!=null){
					/*
					 * Delega successore.
					 */
					return _successor.beepOnEvents ();
				} else {
					/*
					 * Informazione non disponibile.
					 * Funzionalità disabilitata.
					 */
					return false;
				}
			}
	}
	
	/**
	 * Ritorna il tipo di lista degli avanzamenti.
	 *
	 * @return il tipo di lista degli avanzamenti.
	 */
	public ProgressListType getProgressListType (){
		final ProgressListType returnValue = this._settings.getProgressListType ();
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
				return _successor.getProgressListType ();
			} else {
				/*
				 * Informazione non disponibile.
				 * Funzionalità disabilitata.
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
}

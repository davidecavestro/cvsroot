/*
 * ApplicationOptions.java
 *
 * Created on 4 dicembre 2004, 14.12
 */

package com.ost.timekeeper.conf;

import com.ost.timekeeper.conf.*;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import com.ost.timekeeper.view.*;
import java.awt.*;
import javax.swing.UIManager;

/**
 * Opzioni di configurazione dell'applicazione. E' possibile implementare una catena
 * di responsabilitą, innestando diversi oggetti diquesto tipo.
 *
 * @author  davide
 */
public final class ApplicationOptions {
	
	/**
	 * Le impostazioni.
	 */
	private ApplicationSettings _settings;
	
	/**
	 * L'anello successore nella catena di responsabilitą.
	 */
	private ApplicationOptions _successor;
	
	/**
	 *
	 * Costruttore privato, evita istanzazione dall'esterno.
	 *
	 * @param settings le impostazioni.
	 * @param successor l'anello successore nella catena di responsabilitą.
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
	
	/**
	 * Ritorna la posizione iniziale della finestra di gestione grafici.
	 * @return la posizione iniziale della finestra di gestione grafici.
	 */
	public Rectangle getChartFrameBounds (){
		final Rectangle returnValue = this._settings.getChartFrameBounds ();
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
				return _successor.getChartFrameBounds ();
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
					 * Funzionalitą disabilitata.
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
				 * Funzionalitą disabilitata.
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
				 * Funzionalitą disabilitata.
				 */
				return 8192;
			}
		}
	}
	
	/**
	 * Ritorna il percorso della directory contenente i dati persistenti (JDO).
	 *
	 * @return il percorso della directory contenente i dati persistenti (JDO).
	 */
	public String getJDOStorageDirPath (){
		final String returnValue = this._settings.getJDOStorageDirPath ();
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
					 * Informazione non disponibile.
					 */
				return null;
			}
		}
	}
	
	/**
	 * Ritorna il nome dello storage JDO (i file contenenti i dati persistenti e gli indici).
	 *
	 * @return il nome dello storage JDO (i file contenenti i dati persistenti e gli indici).
	 */
	public String getJDOStorageName (){
		final String returnValue = this._settings.getJDOStorageName ();
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
					 * Informazione non disponibile.
					 */
				return null;
			}
		}
	}
	
	/**
	 * Ritorna il nome dell'utente JDO.
	 *
	 * @return il nome dell'utente JDO.
	 */
	public String getJDOUserName (){
		final String returnValue = this._settings.getJDOUserName ();
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
					 * Informazione non disponibile.
					 */
				return null;
			}
		}
	}
	
	/**
	 * Ritorna l'ampiezza dell'albero dei nodi di avanzamento.
	 *
	 * @return l'ampiezza dell'albero dei nodi di avanzamento..
	 */
	public int getProgressItemTreeWidth (){
		final Integer returnValue = this._settings.getProgressItemTreeWidth ();
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
				return _successor.getProgressItemTreeWidth ();
			}
		}
		/*
		 * Informazione non disponibile.
		 */
		return -1;
	}
	
	/**
	 * Ritorna il numero di livelli visibili per il grafico ad anello.
	 * @return il numero di livelli visibili per il grafico ad anello.
	 */
	public int getRingChartVisibleLevels (){
		final Integer returnValue = this._settings.getRingChartVisibleLevels ();
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
				return _successor.getRingChartVisibleLevels ();
			}
		}
		/*
		 * valore default
		 */
		return 4;
	}
	
	
	public boolean ringChartAutoload () {
		final Boolean returnValue = this._settings.ringChartAutoload ();
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
				return _successor.ringChartAutoload ();
			} else {
					/*
					 * Informazione non disponibile.
					 * Funzionalitą disabilitata.
					 */
				return false;
			}
		}
	}
	
	public boolean barChartAutoload () {
		final Boolean returnValue = this._settings.barChartAutoload ();
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
				return _successor.barChartAutoload ();
			} else {
					/*
					 * Informazione non disponibile.
					 * Funzionalitą disabilitata.
					 */
				return false;
			}
		}
	}
	
	/**
	 * Ritorna il formato di visualizzazione ed editazione di una data contenente anche le ore.
	 *
	 * @return il formato di visualizzazione ed editazione di una data contenente anche le ore.
	 */	
	public String getDateTimeFormat (){
		return ResourceSupplier.getString (ResourceClass.UI, "global", "date.time.format");
	}
	
	/**
	 * Ritorna il formato di visualizzazione ed editazione di una data.
	 *
	 * @return il formato di visualizzazione ed editazione di una data.
	 */	
	public String getDateFormat (){
		return ResourceSupplier.getString (ResourceClass.UI, "global", "date.format");
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
	
	/**
	 * Ritorna il nome dell'ultimo progetto caricato.
	 *
	 * @return il nome dell'ultimo progetto caricato.
	 */	
	public String getLastProjectName (){
		final String returnValue = this._settings.getLastProjectName ();
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
					 * Default di sistema non disponibile.
					 */
				return null;
			}
		}
	}
	
}

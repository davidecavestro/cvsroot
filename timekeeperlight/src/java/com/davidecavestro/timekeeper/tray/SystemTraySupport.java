/*
 * SystemTraySupport.java
 *
 * Created on November 7, 2007, 7:22 PM
 *
 */

package com.davidecavestro.timekeeper.tray;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.gui.*;
import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.Task;
import com.ost.timekeeper.util.Duration;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Fornisce metodi di utilit&agrave; per la gestione della tray icon.
 * Rende trasparente il controllo di abilitazione del supporto alla tray icon da parte dell'ambiente grafico (potrebbe non supportarla ).
 * Si occupa, inoltre, di disaccoppiare l'applicazione dalle librerie digestione della SystemTray, disponibili solamente da Java 6.
 * In questo modo l'applicazione rimane compatibile com Java 5 (ma senza tray icon).
 *
 * @author Davide Cavestro
 */
public class SystemTraySupport {
	
	private final ApplicationContext _context;
	
	private PieceOfWork _advancingPOW;
	private Task _selectedTask;
	
	/**
	 * Costruttore.
	 */
	public SystemTraySupport (final ApplicationContext context) {
		_context = context;
	}
	
	
	Object trayIcon;
	
	final Image workImage = Toolkit.getDefaultToolkit ().getImage (getClass ().getResource ("/com/davidecavestro/timekeeper/gui/images/small/clock.png"));
	final Image pauseImage = Toolkit.getDefaultToolkit ().getImage (getClass ().getResource ("/com/davidecavestro/timekeeper/gui/images/small/clock_red.png"));
	
	/**
	 * Registra l'icona nella system tray.
	 *
	 *
	 * @return l'esito della registrazione.
	 * @param pow l'azione correntemente attiva. Pu&ograve; essere nulla.
	 * @param popup il menu da associare alla tray icon. Pu&ograve; essere nullo.
	 * @throws java.lang.ClassNotFoundException in caso di utilizo con una versionedi Java anteriore alla 6
	 */
	public boolean register (final PopupMenu popup, final PieceOfWork pow) throws ClassNotFoundException {
		
		if (SystemTray.isSupported ()) {
			
			final SystemTray tray = SystemTray.getSystemTray ();
			
			trayIcon = new CustomTrayIcon (pauseImage, null, popup, this);
			
			setWorking (pow);
			
			getTrayIcon ().setImageAutoSize (true);
			
			try {
				tray.add (getTrayIcon ());
				getTrayIcon ().setToolTip ("foo");
			} catch (AWTException e) {
				System.err.println ("TrayIcon is not supported.");
				e.printStackTrace (System.err);
			}
			
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Imposta un nuovo enu per la tray icon.
	 *
	 * @param popup ilnuovo menu.
	 */
	public void setMenu (final PopupMenu popup) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().setPopupMenu (popup);
	}
	
	/**
	 * Ritorna il menu associato alla tray icon.
	 *
	 * @return il menu associato alla tray icon.
	 */
	public PopupMenu getMenu () {
		if (getTrayIcon ()==null) {
			return null;
		}
		return getTrayIcon ().getPopupMenu ();
	}
	
	/**
	 * Imposta un nuovo tooltip.
	 * @param text il nuovo tooltip.
	 */
	public void setTooltip (final String text) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().setToolTip (text);
	}
	
	/**
	 * Imposta l'attivit&agrave; in corso. Il valore <TT>null</TT> va usato per indicare che non cisono attivit&agrave; in corso.
	 * @param t l'attivit&agrave; correntemente in fase di misurazione.
	 */
	public void setWorking (final PieceOfWork spow) {
		
		if (getTrayIcon ()==null) {
			return;
		}
		_advancingPOW = spow;
		if (spow!=null) {
			getTrayIcon ().setImage (workImage);
		} else {
			getTrayIcon ().setImage (pauseImage);
		}
		
		refreshTooltip ();
	}
	
	private void refreshTooltip () {
		final PieceOfWork pow = getRunningAction ();
		if (pow!=null) {
			setTooltip (MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TrayIcon/Tooltip/Working"), pow.getTask ().getName (), DurationUtils.format (new Duration (pow.getFrom (), new Date ()))));
		} else {
			final Task selectedTask = getSelectedTask ();
			if (selectedTask!=null) {
				setTooltip (MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TrayIcon/Tooltip/Pause_on_task"), selectedTask.getName ()));
			} else {
				setTooltip (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TrayIcon/Tooltip/Pause"));
			}
		}
	}
	
	
	/**
	 * Mostra un messaggio dierrore.
	 *
	 * @param caption il titolo delmessaggio.
	 * @param message il testo del messaggio.
	 */
	public void showError (final String caption, final String message) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().displayMessage (caption, message, TrayIcon.MessageType.ERROR);
	}
	/**
	 * Mostra un messaggio di info.
	 *
	 * @param caption il titolo delmessaggio.
	 * @param message il testo del messaggio.
	 */
	public void displayInfo (final String caption, final String message) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().displayMessage (caption, message, TrayIcon.MessageType.INFO);
	}
	/**
	 * Mostra un messaggio di warning.
	 *
	 * @param caption il titolo delmessaggio.
	 * @param message il testo del messaggio.
	 */
	public void displayWarning (final String caption, final String message) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().displayMessage (caption, message, TrayIcon.MessageType.WARNING);
	}
	/**
	 * Mostra un messaggio.
	 *
	 * @param caption il titolo delmessaggio.
	 * @param message il testo del messaggio.
	 */
	public void displayMessage (final String caption, final String message) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().displayMessage (caption, message, TrayIcon.MessageType.NONE);
	}
	
	
	/**
	 * Rimuove la tray icon dalla System Tray.
	 */
	public void release () {
		if (getTrayIcon ()==null) {
			return;
		}
		final SystemTray tray = SystemTray.getSystemTray ();
		
		tray.remove (getTrayIcon ());
	}
	
	public void addMouseListener (final MouseListener l) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().addMouseListener (l);
	}
	
	public void removeMouseListener (final MouseListener l) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().removeMouseListener (l);
	}
	
	public void addMouseMotionListener (final MouseMotionListener l) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().addMouseMotionListener (l);
	}
	
	public void removeMouseMotionListener (final MouseMotionListener l) {
		if (getTrayIcon ()==null) {
			return;
		}
		getTrayIcon ().removeMouseMotionListener (l);
	}
	
	
	public PieceOfWork getRunningAction () {
		return _advancingPOW;
	}
	
	public Task getSelectedTask () {
		return _selectedTask;
	}
	
	private TrayIcon getTrayIcon () {
		return (TrayIcon)trayIcon;
	}

	/**
	 * Imposta il correntemente selezionato.
	 * Serve per poter mostrare neitooltip il nome deltask selezionato (quello su cuieventualmente sifa partire un'azione)
	 */
	public void setSelected (final Task t) {
		_selectedTask = t;
		refreshTooltip ();
	}
	
}

/*
 * TrayMessageNotifier.java
 *
 * Created on November 17, 2007, 9:54 AM
 *
 */

package com.davidecavestro.timekeeper.tray;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.gui.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Notifica eventi alla tray icon tramite un timer configurabile.
 *
 * @author Davide Cavestro
 */
public abstract class TrayMessageNotifier {
	
	private Timer _timer;
		
	private final long _period;
	private final long _delay;
	
	private final SystemTraySupport _traySupport;
	
	/**
	 * Costruttore.
	 */
	public TrayMessageNotifier (final ApplicationContext context, final long delay) {
		_traySupport = context.getWindowManager ().getSystemTraySupport ();
		_period = -1;
		_delay = delay;
	}
	
	/**
	 * Costruttore.
	 */
	public TrayMessageNotifier (final ApplicationContext context, final long delay, final long period) {
		_traySupport = context.getWindowManager ().getSystemTraySupport ();
		_period = period;
		_delay = delay;
	}
 
	/**
	 * Attiva il timer di notifica.
	 */
	public void activate () {
		_timer = new Timer ("tray-notifier", true);
		
		final TimerTask task = 
			new TimerTask () {
				public void run() {
					notifyTray (_traySupport);
				}
			};
			
		if (_period<0) {
			_timer.schedule (task, _delay);
		} else {
			_timer.schedule (task, _delay, _period);
		}
	}
	
	/**
	 * Disttiva il timer di notifica.
	 */
	public void deactivate () {
		if (_timer!=null) {
			_timer.cancel ();
			_timer = null;
		}
	}
	
	/**
	 * Effettua la notifica alla system tray.
	 */
	public abstract void notifyTray (final SystemTraySupport traySupport);
}

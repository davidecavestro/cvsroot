/*
 * Created on November 12, 2007, 12:39 PM
 *
 */

package com.davidecavestro.timekeeper.tray;

import com.davidecavestro.timekeeper.gui.*;
import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.ost.timekeeper.util.Duration;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.text.MessageFormat;
import java.util.Date;

/**
 * Tray icon dedicata all'applicazione.
 *
 * @author Davide Cavestro
 */
public class CustomTrayIcon extends TrayIcon {
	
	private final SystemTraySupport _systemTraySupport;
	
		public CustomTrayIcon (final Image image, final String tooltip, final PopupMenu popup, final SystemTraySupport sts) {
			super (image, tooltip, popup);
			_systemTraySupport = sts;
		}

		@Override
		public String getToolTip () {
			final PieceOfWork pow =_systemTraySupport.getRunningAction ();
			if (pow!=null) {
				return MessageFormat.format (java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TrayIcon/Tooltip/Working"), pow.getTask ().getName (), DurationUtils.format (new Duration (pow.getFrom (), new Date ())));
			} else {
				return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("TrayIcon/Tooltip/Pause");
			}
		}	
}

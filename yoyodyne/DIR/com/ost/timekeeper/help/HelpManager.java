/*
 * HelpManager.java
 *
 * Created on 24 dicembre 2004, 15.04
 */

package com.ost.timekeeper.help;

import com.ost.timekeeper.*;
import javax.help.*;
import javax.swing.*;
import java.net.*;

/**
 * Gestore centralizzato dell'help dell'applicazione.
 *
 * @author  davide
 */
public final class HelpManager {
	/**
	 * L'istanza del singleton.
	 */
	private static HelpManager _instance;
	
	private static HelpBroker _mainHelpBroker = null;
	private static CSH.DisplayHelpFromSource _csh = null;
	private static final String _mainHelpSetName = "help/TimekeeperHelp.hs";
	
	/** Costruttore vuoto. */
	private HelpManager () {
		// try to find the helpset and create a HelpBroker object
		if (_mainHelpBroker == null){
			HelpSet mainHelpSet = null;
			try {
				URL hsURL = HelpSet.findHelpSet (null, _mainHelpSetName);
				if (hsURL == null)
					Application.getLogger ().warning ("HelpSet " + _mainHelpSetName + " not found.");
				else
					mainHelpSet = new HelpSet (null, hsURL);
			} catch (HelpSetException ee) {
				Application.getLogger ().warning ("HelpSet " + _mainHelpSetName + " could not be opened.", ee);
			}
			if (mainHelpSet != null)
				_mainHelpBroker = mainHelpSet.createHelpBroker ();
			if (_mainHelpBroker != null)
				// CSH.DisplayHelpFromSource is a convenience class to display the helpset
				_csh = new CSH.DisplayHelpFromSource (_mainHelpBroker);
		}
	}
	
	
	/**
	 * Ritorna l'istanza di questo manager di help.
	 *
	 * @return l'istanza di questo manager.
	 */	
	public static HelpManager getInstance (){
		if (_instance==null){
			_instance = new HelpManager ();
		}
		return _instance;
	}
	
	/**
	 * Inizializza la voce di menu specificata per l'utilizzo come lancio dell'help.
	 *
	 * @param helpItem la voce di menu.
	 */	
	public void initialize (JMenuItem helpItem){
		
		// listen to ActionEvents from the helpItem
		if (_csh != null)
			helpItem.addActionListener (_csh);
		
	}
	
	/**
	 * Ritorna il broker principale.
	 *
	 * @return il broker principale.
	 */	
	public HelpBroker getMainHelpBroker (){
		return _mainHelpBroker;
	}
}

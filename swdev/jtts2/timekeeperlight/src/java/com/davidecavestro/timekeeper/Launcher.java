/*
 * Launcher.java
 *
 * Created on 26 novembre 2005, 14.55
 */

package com.davidecavestro.timekeeper;

import com.davidecavestro.timekeeper.conf.CommandLineApplicationEnvironment;
import com.davidecavestro.timekeeper.gui.MainWindow;

/**
 * lancia l'applicazione.
 *
 * @author  davide
 */
public class Launcher {
	
	/** Costruttore vuoto. */
	public Launcher () {
	}
	
	/**
	 * Metodo di lancio dell'applicazione.
	 * @param args gli argomenti della inea di comando.
	 */
	public static void main (String[] args) {
		new Application (new CommandLineApplicationEnvironment (args)).start ();
	}
	
}

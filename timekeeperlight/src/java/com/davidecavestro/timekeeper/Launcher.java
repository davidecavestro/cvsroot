/*
 * Launcher.java
 *
 * Created on 26 novembre 2005, 14.55
 */

package com.davidecavestro.timekeeper;

import com.davidecavestro.common.log.NotificationUtils;
import com.davidecavestro.timekeeper.conf.CommandLineApplicationEnvironment;
import com.davidecavestro.timekeeper.conf.UserResources;
import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

/**
 * lancia l'applicazione.
 *
 * @author  davide
 */
public class Launcher {
	
	private final int PORT = 63229;
	
	private final String REQUEST = "how are you";
	private final String RESPONSE = "fine, thanks";
	private final String REQUEST_USER_NAME = "who do you work for";
	private final String RESPONSE_UP_TO_YOU = "up to you";
	
	private final Semaphore _semaphore;
	
	/** Costruttore vuoto. */
	private Launcher () {
		_semaphore = new Semaphore (0);
	}
	
	private boolean startServer (int port) {
		try {
			final ServerSocket ss = new ServerSocket (port, 10, InetAddress.getLocalHost ());
			
			final Thread t = new Thread (
				new Runnable () {
				
					public void run () {
						try {
							while (true) {
								final Socket s = ss.accept ();
								try {
									final DataInputStream in = new DataInputStream (s.getInputStream ());
									final String greetings = in.readLine ();
									final PrintStream out = new PrintStream (s.getOutputStream ());

									if (REQUEST.equals (greetings)) {
										/*
										 * richiesta corretta (come da protocollo)
										 */
										out.println (RESPONSE);

										final String secondRequest = in.readLine ();
										
										if (REQUEST_USER_NAME.equals (secondRequest)) {
											/*
											 * e' stato richiesto il nome utente
											 * risponde
											 */
											out.println (UserResources.getUserAccount ());
											
											
											final String thirdRequest = in.readLine ();

											if (RESPONSE_UP_TO_YOU.equals (thirdRequest)) {
												/*
												 * ok, si e' arreso :-)
												 * prova a tornare in primo piano
												 */
												rightRequest ();
											}
										}
									}
										
								} finally {
									s.close ();
								}
							}
						} catch (IOException ex) {
							ex.printStackTrace ();
						}
					}
				}, "only-one-instance-per-user-check"
			);
				t.setDaemon (true);
				t.start ();
				
			/*
			 * La socket e' stata inizializata correttamente
			 * cio' significa che nessun'altra istanza e' attiva
			 */
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		} catch (BindException ex) {
//			ex.printStackTrace();
			try {
				/*
				 * potrebbe esserci un'altra istanza attiva
				 */
				final Socket s = new Socket (InetAddress.getLocalHost (), port);
				try {
					s.setSoTimeout (500);
					final PrintStream out = new PrintStream (s.getOutputStream ());
					final DataInputStream in = new DataInputStream (s.getInputStream ());
					out.println (REQUEST);
					if (RESPONSE.equals (in.readLine ())) {
						/*
						 * Risposta corretta dal server
						 */
						out.println (REQUEST_USER_NAME);
						if (UserResources.getUserAccount ().equals (in.readLine ())) {
							/*
							 * Medesimo utente...
							 * E' l'unico caso in cui l'applicazione non parte di propria volonta'
							 */
							out.println (RESPONSE_UP_TO_YOU);
							return false;
						} else {
							return startServer (port + 1);
						}
					}
				} finally {
					s.close ();
				}
			} catch (UnknownHostException ex1) {
				ex.printStackTrace();
			} catch (IOException ex1) {
				ex.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;		
	}

	/**
	 * Controlla che non esistano altre istanze dell'applicazione lanciate dallo stesso utente
	 */
	private boolean checkForOtherInstances () {
		return startServer (PORT);
	}
	
	private Application _application;
	private void bind (Application a) {
		_application = a;
		_semaphore.release ();
	}
	
	private boolean _firstCall = true;
	private void rightRequest () {
		if (_firstCall) {
			_firstCall = false;
			_semaphore.acquireUninterruptibly ();
		}
		_application.bringToFront ();
	}
	
	/**
	 * Metodo di lancio dell'applicazione.
	 * @param args gli argomenti della inea di comando.
	 */
	public static void main (String[] args) {
		final Launcher l = new Launcher ();
		if (!l.checkForOtherInstances ()) {
			final NotificationUtils notification = new NotificationUtils ();
			final String[] message = {
				java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Launcher/ErrorMessage/DuplicatedAppInstance/Row1"),
				java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Launcher/ErrorMessage/DuplicatedAppInstance/Row2"),
				java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Launcher/ErrorMessage/DuplicatedAppInstance/Row3")
				};
			notification.error (message);
			System.exit (1);
		}
		final Application a = new Application (new CommandLineApplicationEnvironment (args));
		try {
			a.start ();
		} catch (final HeadlessException he) {
			final NotificationUtils notification = new NotificationUtils ();
			final String[] message = {
				java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Launcher/ErrorMessage/AWTUnavailable"),
				};
			notification.error (message);
			throw he;
		}
		
		l.bind (a);
	}
}

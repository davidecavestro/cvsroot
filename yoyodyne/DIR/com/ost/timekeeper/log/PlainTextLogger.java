/*
 * PlainTextLogger.java
 *
 * Created on 11 dicembre 2004, 20.18
 */

package com.ost.timekeeper.log;

import com.ost.timekeeper.util.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author  davide
 */
public class PlainTextLogger implements Logger{
	/**
	 * Il carattere di A CAPO.
	 */
	private final static String CR = "\n";
	
	/**
	 */
	private FileWriter _fw;
	
	/**
	 * Costruttore.
	 */
	public PlainTextLogger (File logFile, boolean append) throws IOException {
		FileUtils.makeFilePath (logFile);
		this._fw = new FileWriter (logFile, append);
	}
	
	public void debug (String message) {
		printMessage (MessageType.DEBUG, message);
	}
	
	public void error (String message) {
		printMessage (MessageType.ERROR, message);
	}
	
	public void info (String message) {
		printMessage (MessageType.INFO, message);
	}
	
	public void warning (String message) {
		printMessage (MessageType.WARNING, message);
	}
	
	private final void printMessage (MessageType type, String message){
		final StringBuffer sb = new StringBuffer ();
		sb.append (CalendarUtils.toTSString (Calendar.getInstance ().getTime ()));
		sb.append (": ");
		sb.append (message);
		sb.append (CR);
		try {
			this._fw.write (sb.toString ());
		} catch (IOException ioe){
			System.out.println (ExceptionUtils.getStackStrace (ioe));
		}
	}
	
	private final static class MessageType {
		/**
		 * Messaggio di DEBUG.
		 */
		public final static MessageType DEBUG = new MessageType ("DEBUG");
		
		/**
		 * Messaggio di INFO.
		 */
		public final static MessageType INFO = new MessageType ("INFO");
		
		/**
		 * Messaggio di WARNING.
		 */
		public final static MessageType WARNING = new MessageType ("WARNING");
		
		/**
		 * Messaggio di ERROR.
		 */
		public final static MessageType ERROR = new MessageType ("ERROR");
		
		/**
		 * L adescrizione.
		 */
		private String _type;
		/**
		 * Costruttore privato. Evita istanziazione.
		 * @param type la descrizione di questo tipo.
		 */
		private MessageType (final String type){
			this._type = type;
		}
	}
}
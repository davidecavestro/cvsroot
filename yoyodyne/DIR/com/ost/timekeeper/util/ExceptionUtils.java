/*
 * ExceptionUtils.java
 *
 * Created on 15 maggio 2004, 19.41
 */

package com.ost.timekeeper.util;

import java.io.*;

/**
 *
 * @author  davide
 */
public final class ExceptionUtils {
	
	/** 
	 * Costruttore. 
	 */
	private ExceptionUtils() {
	}
	
	/**
	 * Ritorna un buffer contenente lo stack delle chiamate a metodo che ha 
	 * portato al lancio di un
	 * oggetto di tipo {@link java.lang.Throwable}.
	 *
	 * @param t il Throwable.
	 * @return lo stack delle chiamate del thread che ha generato <TT>t</TT>.
	 */	
	public static StringBuffer getStackStrace (Throwable t){
		StringBuffer sb = new StringBuffer ();
		StringWriter sw = new StringWriter ();
		t.printStackTrace(new PrintWriter (sw));
		return sw.getBuffer();
	}
	
	/**
	 * Ritorna un buffer contenente lo stack delle chiamate a metodo che ha 
	 * portato al lancio di una eccezione innestata.
	 *
	 * @param nestedException l'eccezione.
	 * @return lo stack delle chiamate del thread che ha generato <TT>nestedException</TT>.
	 */	
	public static StringBuffer getStackStrace (NestedRuntimeException nestedException){
		Throwable rootCause = nestedException.getRootCause();
		return new StringBuffer ().append (getStackStrace ((Throwable)nestedException)).append ("\nRoot cause:\n").append (getStackStrace (rootCause));
	}
}

/*
 * StringUtils.java
 *
 * Created on 6 maggio 2004, 0.03
 */

package com.ost.timekeeper.util;

/**
 * Utilit� per la manipolazione di stringhe.
 *
 * @author  davide
 */
public final class StringUtils {
	
	/** 
	 * Costruttore vuoto.
	 */
	private StringUtils() {
	}
	
	/**
	 * Verifica se un array di stringhe ne contiene una scelta.
	 *
	 * @param container il contenitore.
	 * @param pattern la stringa da cercare.
	 * @return <code<true</code>se <code>container</code> contiene <code>pattern</code>;
	 * <code>false</code> altrimenti.
	 */	
	public final static boolean contains (String[] container, String pattern){
		boolean nullContainer = container==null;
		boolean nullPattern = pattern==null;
		if (nullContainer && nullPattern){
			return true;
		}
		for (int i=0;i<container.length;i++){
			String s = container[i];
			if (s!=null){
				if (s.equals(pattern)){
					return true;
				}
			} else {
				//elemento nullo 
				if (nullPattern){
					return true;
				}
			}
		}
		return false;
	}
	
}

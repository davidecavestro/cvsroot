/*
 * CalendarUtils.java
 *
 * Created on 24 aprile 2004, 11.06
 */

package com.ost.timekeeper.util;

import java.text.*;
import java.util.*;

/**
 * Classe di utilità per oggetti rappresentanti date.
 * @author  davide
 */
public final class CalendarUtils {
	
	/** Costruttore privato, la clase deve esporre solometodi statici */
	private CalendarUtils() {
	}
	
	/**
	 * Permette di valutare l'uguaglianza fra due <code>Calendar</code>, comprese
	 * istanze <code>null</code>.
	 * @param c1 la prima istanza da confrontare.
	 * @param c2 la seconda istanza da confrontare.
	 * @return <code>true</code> se <code>c1</code> è uguale a <code>c2</code>; 
	 * <code>false</code> altrimenti.
	 */	
	public static boolean equals (Calendar c1, Calendar c2){
		return (c1==null && c2==null) || 
			(c1!=null && c2!=null &&
				(c1.equals(c2)));
	}
	
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
	
	public static String toTSString (Calendar calendar){
		if (calendar==null){
			return null;
		}
		return dateFormat.format(calendar.getTime());
	}
}

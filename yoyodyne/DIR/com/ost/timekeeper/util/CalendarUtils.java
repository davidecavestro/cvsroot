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
	/**
	 * Formato timestamp.
	 */
	public final static String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/** Costruttore privato, la clase deve esporre solometodi statici */
	private CalendarUtils() {
	}
	
	/**
	 * Permette di valutare l'uguaglianza fra due <code>Date</code>, comprese
	 * istanze <code>null</code>.
	 * @param c1 la prima istanza da confrontare.
	 * @param c2 la seconda istanza da confrontare.
	 * @return <code>true</code> se <code>c1</code> è uguale a <code>c2</code>; 
	 * <code>false</code> altrimenti.
	 */	
	public static boolean equals (Date c1, Date c2){
		return (c1==null && c2==null) || 
			(c1!=null && c2!=null &&
				(c1.equals(c2)));
	}
	
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
	
	public static String toTSString (Date calendar){
		if (calendar==null){
			return null;
		}
		return dateFormat.format(calendar);
	}
	
	/** Ritorna una istanza di  <TT>Calendar</TT> valorizzata con la data
	 * specificata dal timestamp speccificato.
	 *
	 * @return una <TT>Calendar</TT> valorizzato con <TT>timestamp</TT>, interpretato
	 * nel formato <TT>timestampFormat</TT>.
	 * @see {@link java.text.SimpleDateFormat} per il formato del timestamp.
	 * @param timeStamp il timestamp.
	 * @param timestampFormat il formato del timestamp.
	 * @throws NestedRuntimeException in caso di timestamp non interpretabile.
	 */	
	public static Calendar getCalendar ( String timeStamp, String timestampFormat ) throws NestedRuntimeException{
		if (timeStamp==null || timeStamp.length ()==0){
			return null;
		}
		
		final SimpleDateFormat tsFormat = new SimpleDateFormat ( timestampFormat );
		try {
			final Date date = tsFormat.parse ( timeStamp );
			final Calendar calendar = new GregorianCalendar ();
			calendar.setTime ( date );

			return calendar;
		} catch (java.text.ParseException pe) {
			throw new NestedRuntimeException (pe);
		}
	}
	
	
}

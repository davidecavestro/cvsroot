/*
 * DurationUtils.java
 *
 * Created on November 28, 2006, 12:33 AM
 *
 */

package com.davidecavestro.timekeeper.gui;

import com.ost.timekeeper.util.Duration;
import java.text.DecimalFormat;

/**
 * Utilit&agrave; per la formattazione delle durate.
 *
 * @author Davide Cavestro
 */
public class DurationUtils {
	
	private DurationUtils (){}
	
	private final static DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();

	public static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			setMinimumIntegerDigits (2);
		}
	}
	
	public static String format (final Duration d) {
		final StringBuffer sb = new StringBuffer ();
					
		sb.append (durationNumberFormatter.format (d.getTotalHours ()))
		.append (":")
		.append (durationNumberFormatter.format (d.getMinutes ()))
		.append (":")
		.append (durationNumberFormatter.format (d.getSeconds ()));
		return sb.toString ();		
	}
	
	public static String formatDuration (final long d) {
		final StringBuffer sb = new StringBuffer ();
		
		final long[] f = Duration.getDurationFields (d);
		
		sb.append (durationNumberFormatter.format (f[Duration.HOURS_SLOT]))
		.append (":")
		.append (durationNumberFormatter.format (f[Duration.MINUTES_SLOT]))
		.append (":")
		.append (durationNumberFormatter.format (f[Duration.SECONDS_SLOT]));
		return sb.toString ();		
	}
	

	
}

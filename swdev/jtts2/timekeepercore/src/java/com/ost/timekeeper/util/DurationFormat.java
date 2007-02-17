/*
 * DurationFormat.java
 *
 * Created on 26 marzo 2005, 17.55
 */

package com.ost.timekeeper.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;

/**
 * FOrmatta una durata.
 * Attenzione. questa classe estende impropiamente NumberFormat.
 *
 * @author  davide
 */
public class DurationFormat extends NumberFormat {
	
	private DecimalFormat _decimalFormat;
	
	public DurationFormat (){
		this._decimalFormat = new DecimalFormat ();
		_decimalFormat.setMinimumIntegerDigits (2);
	}

   /**
     * Specialization of format.
     * @see java.text.Format#format
     */
    public StringBuffer format(double millisecs,
                                        StringBuffer toAppendTo,
                                        FieldPosition pos){
		return format ((long)millisecs, toAppendTo, pos);
	}
	
	public StringBuffer format (long millisecs, StringBuffer toAppendTo, FieldPosition pos) {
		final Duration duration = new Duration (millisecs);
		toAppendTo.append (this._decimalFormat.format (duration.getHours ()))
		.append (":")
		.append (this._decimalFormat.format (duration.getMinutes ()))
		.append (":")
		.append (this._decimalFormat.format (duration.getSeconds ()));
		return toAppendTo;
	}
	
	public Number parse (String source, java.text.ParsePosition parsePosition) {
		return this._decimalFormat.parse (source, parsePosition);
	}
	
}

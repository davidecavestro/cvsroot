/*
 * DurationTextField.java
 *
 * Created on November 28, 2006, 12:30 AM
 *
 */

package com.davidecavestro.timekeeper.gui;

import com.ost.timekeeper.util.Duration;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JFormattedTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.InternationalFormatter;

/**
 * Campo di editazione della durata.
 *
 * @author Davide Cavestro
 */
public class DurationTextField extends JFormattedTextField {
	
	/**
	 * Costruttore.
	 */
	public DurationTextField () {
		super (new DurationFormatter ());
	}
	
	private static class DurationFormatter extends InternationalFormatter {
		public DurationFormatter () {
			super (	new Format () {
				@Override
					public StringBuffer format (Object obj, StringBuffer toAppendTo, FieldPosition pos) {
					final StringBuffer sb = new StringBuffer ();
					final Duration d = (Duration)obj;
					if (d==null){
						return sb;
					}
					final String f = DurationUtils.format (d);
					
					int newEndIdx = pos.getBeginIndex ()+8;
					if (d.getSeconds ()<0) {
						newEndIdx++;
					}
					if (d.getMinutes ()<0) {
						newEndIdx++;
					}
					if (d.getTotalHours ()<0) {
						newEndIdx++;
					}
					pos.setEndIndex (newEndIdx);
					sb.append (f.substring (pos.getBeginIndex (), pos.getEndIndex ()));
					return sb;
				}
				
				@Override
					public Object parseObject (String source, ParsePosition pos) {
					final String[] s = source.split (":");
					if (s.length<3) {
						pos.setErrorIndex (pos.getIndex ());
						return null;
					}
					
					int i = 0;
					
					int newPos = pos.getIndex ();
					
					final int hours = Integer.parseInt (s[i]);
					newPos+=s[i].length ()+1;
					i++;
					
					final int minutes = Integer.parseInt (s[i]);
					if (minutes>59) {
						pos.setErrorIndex (newPos);
						return null;
					}
					newPos+=s[i].length ()+1;
					i++;
					
					try {
						final int seconds = Integer.parseInt (s[i]);
						if (seconds>59) {
							pos.setErrorIndex (newPos);
							return null;
						}
						newPos+=s[i].length ();
						i++;
						
						pos.setIndex (newPos);
						return new Duration (hours, minutes, seconds, 0);
					} catch (final NumberFormatException nfe) {
						throw new RuntimeException ("Invalid data: "+source);
					}
				}
				
			});
		}
		
//		/**
//		 * Returns true, as DateFormatterFilter will support
//		 * incrementing/decrementing of the value.
//		 */
//			@Override
//			public boolean getSupportsIncrement () {
//			return true;
//		}
//		
//			@Override
//    public Action[] getActions() {
//        return super.getActions();
//    }
//		/**
//		 * Adjusts the Date if FieldPosition identifies a known calendar
//		 * field.
//		 */
//			public Object adjustValue (Object value, Map attributes, Object key, int direction) throws BadLocationException, ParseException {
//			if (key != null) {
//				int field;
//				
////// HOUR1 has no corresponding calendar field, thus, map
////// it to HOUR0 which will give the correct behavior.
////					if (key == DateFormat.Field.HOUR1) {
////						key = DateFormat.Field.HOUR0;
////					}
////					field = ((DateFormat.Field)key).getCalendarField ();
////
////					Calendar calendar = getCalendar ();
////
////					if (calendar != null) {
////						calendar.setTime ((Date)value);
////
////						int fieldValue = calendar.get (field);
////
////						try {
////							calendar.add (field, direction);
////							value = calendar.getTime ();
////						} catch (Throwable th) {
////							value = null;
////						}
////						return value;
////					}
//				return null;
//			}
//			return null;
//		}
	}
}

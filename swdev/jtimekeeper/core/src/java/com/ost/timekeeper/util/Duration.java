/*
 * Duration.java
 *
 * Created on 8 maggio 2004, 11.17
 */

package com.ost.timekeeper.util;

import java.util.*;

/**
 * Modella una durata. La durata pu&ograve; anche non avere una data di inizio e di fine associate.
 * Questo tipo di durata &egrave; immutabile.
 *
 * @author  davide
 */
public final class Duration {
	
	public final static int MILLISECONDS = 0;
	public final static int SECONDS = 1;
	public final static int MINUTES = 2;
	public final static int HOURS = 3;
	public final static int DAYS = 4;
	public final static int MONTHS = 5;
	public final static int YEARS = 6;
	
	public final static long HOURS_PER_DAY = 24;
	public final static long MINUTES_PER_HOUR = 60;
	public final static long SECONDS_PER_MINUTE = 60;

	public final static long MILLISECONDS_PER_SECOND = 1000;
	
	public final static long MILLISECONDS_PER_MINUTE = MILLISECONDS_PER_SECOND * SECONDS_PER_MINUTE;
	public final static long MILLISECONDS_PER_HOUR = MILLISECONDS_PER_MINUTE * MINUTES_PER_HOUR;
	public final static long MILLISECONDS_PER_DAY = MILLISECONDS_PER_HOUR * HOURS_PER_DAY;


	private Date from;
	private Date to;
	
	/**
	 * Durata nulla.
	 */
	public final static Duration ZERODURATION = new Duration (0);
	
	/**
	 * Costruttore con data di inizio e fine periodo.
	 *
	 * @param from data d'inizio.
	 * @param to data di fine del periodo.
	 */
	public Duration(Date from, Date to) {
		this.from=from;
		this.to=to;
		computeFields ();
	}
	
	/**
	 * Costruttore con durata in millisecondi.
	 *
	 * @param milliseconds il numero di millisecondi.
	 */	
	public Duration (final long milliseconds) {
		this.totalMilliseconds = milliseconds;
		computedTotalMilliseconds = true;
		computeFields ();
	}
	
	/**
	 * Costruttore con durata in ore minuti secondi e millisecondi.
	 *
	 * @param hour ore
	 * @param minutes minuti
	 * @param seconds secondi
	 * @param milliseconds millisecondi.
	 */	
	public Duration (final int hours, final int minutes, final int seconds, final int milliseconds) {
		this.totalMilliseconds = milliseconds + MILLISECONDS_PER_SECOND*seconds+MILLISECONDS_PER_MINUTE*minutes+MILLISECONDS_PER_HOUR*hours;;
		computedTotalMilliseconds = true;
		computeFields ();
	}
	
	private final void computeFields (){
		if (!computedTotalMilliseconds){
			computeTotalMilliseconds ();
		}
		if (!computedMilliseconds){
			computeMilliseconds ();
		}
		if (!computedSeconds){
			computeSeconds ();
		}
		if (!computedMinutes){
			computeMinutes ();
		}
		if (!computedHours){
			computeHours ();
		}
		if (!computedDays){
			computeDays ();
		}
	}
	
	private boolean computedTotalMilliseconds;
	
	private boolean computedMilliseconds = false;
	private long totalMilliseconds = 0;
	private long modMilliseconds = 0;
	
	private final void computeTotalMilliseconds (){
		this.totalMilliseconds = this.to.getTime() - this.from.getTime();
		computedTotalMilliseconds = true;
	}
	private final void computeMilliseconds (){
		this.modMilliseconds = this.totalMilliseconds % MILLISECONDS_PER_SECOND;
		computedMilliseconds = true;
	}
	
	private boolean computedSeconds = false;
	private long totalSeconds = 0;
	private long modSeconds = 0;
	private final void computeSeconds (){
		this.totalSeconds = this.totalMilliseconds / MILLISECONDS_PER_SECOND;
		this.modSeconds = this.totalSeconds % SECONDS_PER_MINUTE;
		computedSeconds = true;
	}
	
	private boolean computedMinutes = false;
	private long totalMinutes = 0;
	private long modMinutes = 0;
	private final void computeMinutes (){
		this.totalMinutes = this.totalMilliseconds / MILLISECONDS_PER_MINUTE;
		this.modMinutes = this.totalMinutes % MINUTES_PER_HOUR;
		computedMinutes = true;
	}
	
	private boolean computedHours = false;
	private long totalHours = 0;
	private long modHours = 0;
	private final void computeHours (){
		this.totalHours = this.totalMilliseconds / MILLISECONDS_PER_HOUR;
		this.modHours = this.totalHours % HOURS_PER_DAY;
		computedHours = true;
	}
	
	private boolean computedDays = false;
	private long totalDays = 0;
	private final void computeDays (){
		this.totalDays = this.totalMilliseconds / MILLISECONDS_PER_DAY;
		computedDays = true;
	}
	
	/**
	 * Ritorna i millisecondi di questa durata.
	 *
	 * @return i millisecondi di questa durata.
	 */	
	public long getMilliseconds (){
		return this.modMilliseconds;
	}
	
	/**
	 * Ritorna i secondi di questa durata.
	 *
	 * @return i secondi di questa durata.
	 */	
	public long getSeconds (){
		return this.modSeconds;
	}
	
	/**
	 * Ritorna i minuti di questa durata.
	 *
	 * @return i minuti di questa durata.
	 */	
	public long getMinutes (){
		return this.modMinutes;
	}
	
	/**
	 * Ritorna le ore di questa durata.
	 *
	 * @return le ore di questa durata.
	 */	
	public long getHours (){
		return this.modHours;
	}
	
	/**
	 * Ritorna i giorni di questa durata.
	 *
	 * @return i giorni di questa durata.
	 */	
	public long getDays (){
		return this.totalDays;
	}
	
	/**
	 * Ritorna il numero totale di ore di questa durata.
	 *
	 * @return il numero totale di ore di questa durata.
	 */	
	public long getTotalHours (){
		return this.totalHours;
	}
	
	/**
	 * Ritorna questa durata in millisecondi.
	 *
	 * @return questa durata in millisecondi.
	 */	
	public long getTime (){
		return this.totalMilliseconds;
	}
}

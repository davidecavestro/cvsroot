/*
 * Duration.java
 *
 * Created on 8 maggio 2004, 11.17
 */

package com.ost.timekeeper.util;

import java.util.*;

/**
 * Modella una durata.
 * @author  davide
 */
public class Duration {
	
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
	
	/** Crea una nuova istanza di Duration */
	public Duration(Date from, Date to) {
		this.from=from;
		this.to=to;
		computeFields ();
	}
	
	private final void computeFields (){
//			switch (field){
//				case MILLISECONDS: computeMilliseconds ();
//				case 1: return period.getTo();
//				case 2: return new Boolean(period.getTo()==null);
//				default: return null;
//			}
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
	
	private boolean computedMilliseconds = false;
	private long totalMilliseconds = 0;
	private long modMilliseconds = 0;
	private final void computeMilliseconds (){
		this.totalMilliseconds = this.to.getTime() - this.from.getTime();
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
	
	public long getMilliseconds (){
		return this.modMilliseconds;
	}
	
	public long getSeconds (){
		return this.modSeconds;
	}
	public long getMinutes (){
		return this.modMinutes;
	}
	public long getHours (){
		return this.modHours;
	}
	public long getDays (){
		return this.totalDays;
	}
	
	public long getTotalHours (){
		return this.totalHours;
	}
}

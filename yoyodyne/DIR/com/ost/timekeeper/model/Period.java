/*
 * Period.java
 *
 * Created on 4 aprile 2004, 11.51
 */

package com.ost.timekeeper.model;

import java.util.*;

/**
 * Rappresenta un periodo temporale. I metodi che non ammettono periodi temporali 
 * non validi possono sollevare code>InvalidPeriodException</code>
 * @author  davide
 */
public class Period {
	
	/** Holds value of property from. */
	private Calendar from;
	
	/** Holds value of property to. */
	private Calendar to;
	
	/** Crea una nuova istanza di Period.
	 * @param from
	 * @param to
	 */
	public Period(final Calendar from, final Calendar to) {
		this.from = from;
		this.to = to;
	}
	
	/** Getter for property from.
	 * @return Value of property from.
	 *
	 */
	public Calendar getFrom() {
		return this.from;
	}
	
	/** Setter for property from.
	 * @param from New value of property from.
	 *
	 */
	public void setFrom(Calendar from) {
		this.from = from;
	}
	
	/** Getter for property to.
	 * @return Value of property to.
	 *
	 */
	public Calendar getTo() {
		return this.to;
	}
	
	/** Setter for property to.
	 * @param to New value of property to.
	 *
	 */
	public void setTo(Calendar to) {
		this.to = to;
	}
	
	/**
	 * Specifica se vi è un'intersezione non vuota tra due periodi.
	 * @param period il periodo da testare.
	 * @return <code>true</code> se questo periodo temporale interseca <code>period</code>; 
	 * <code>false</code> altrimenti.
	 */	
	public boolean intersects (final Period period){
		if (!this.isValid() || !period.isValid()){
			throw new InvalidPeriodException ();
		}
		return ! (this.getFrom ().after(period.getTo()) 
			|| this.getTo().before(period.getFrom()));
	}
	
	/** Specifica se questo periodo è valido
	 * @return <code>true</code> se questo è un periodo temporale valido; 
	 * <code>false</code> altrimenti.
	 *
	 */
	public boolean isValid() {
		return this.from!=null 
			&& this.to!=null
			&& !this.from.after(this.to);
	}
	
	public String toString (){
		StringBuffer sb = new StringBuffer ();
		sb.append ("From: ").append (this.from)
		.append (" to: ").append (this.to);
		return sb.toString ();
	}
}

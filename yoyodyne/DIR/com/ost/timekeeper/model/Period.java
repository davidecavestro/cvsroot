/*
 * Period.java
 *
 * Created on 4 aprile 2004, 11.51
 */

package com.ost.timekeeper.model;

import java.util.*;

import com.ost.timekeeper.util.*;

/**
 * Rappresenta un periodo temporale. I metodi che non ammettono periodi temporali 
 * non validi possono sollevare <code>{@link com.ost.timekeeper.model.InvalidPeriodException}</code>.
 *
 * @todo estendere LocalizedPeriodImpl
 * @author  davide
 */
public class Period extends Observable implements LocalizedPeriod{
	
	/** 
	 * La data di inizio periodo. 
	 */
	private Date from;
	
	/** 
	 * La data di fine periodo. 
	 */
	private Date to;
	
	/**
	 * Durata calcolata (valida).
	 */
	private transient boolean isDurationComputed = false;
	
	/**
	 * La durata effettiva attuale (se <TT>isDurationComputed</TT> value <TT>tre</TT>.
	 */
	private transient Duration computedDuration;
	
	/** 
	 * La descrizione di questo periodo. 
	 */
	private String description;
	
	/** 
	 * Le note di questo periodo. 
	 */
	private String notes;
	
	/** 
	 * Costruttore vuoto.
	 */
	public Period () {
	}
	
	/** 
	 * Costruttore con data di inizio e fine.
	 *
	 * @param from la data di inizio.
	 * @param to la data di fine.
	 */
	public Period (final Date from, final Date to) {
		this.from = from;
		this.to = to;
	}
	
	/**
	 *
	 * Costruttore copia.
	 *
	 * @param source la sorgente della copia.
	 */
	public Period (final Period source) {
		this.description = source.description;
		this.notes = source.notes;
		this.from = source.from;
		this.to = source.to;
	}
	
	/** 
	 * Ritorna la data di inizio di questo periodo.
	 *
	 * @return la data d'inizio.
	 */
	public Date getFrom() {
		return this.from;
	}
	
	/** 
	 * Imposta la data d'inizio per questo periodo.
	 *
	 * @param from la nuova data d'inizio.
	 */
	public synchronized void setFrom(Date from) {
		if (!CalendarUtils.equals(this.from,from)){
			this.from = from;
			this.isDurationComputed = false;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/** 
	 * Ritorna la data di fine di questo periodo.
	 *
	 * @return la data di fine.
	 */
	public Date getTo() {
		return this.to;
	}
	
	/** 
	 * Imposta la data di fine per questo periodo.
	 *
	 * @param to la nuova data di fine del periodo.
	 */
	public synchronized void setTo(Date to) {
		if (!CalendarUtils.equals(this.to,to)){
			this.to = to;
			isDurationComputed = false;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * Verifica l'intersezione non vuota tra due periodi.
	 *
	 * @param period il periodo da testare.
	 * @return <code>true</code> se questo periodo temporale interseca <code>period</code>; 
	 * <code>false</code> altrimenti.
	 */	
	public synchronized boolean intersects (final LocalizedPeriod period){
//		final Period period = (LocalizedPeriod)lPeriod;
//		if (!this.isValid() || !period.isValid()){
//			throw new InvalidPeriodException ();
//		}
		return ! (this.getFrom ().after(period.getTo()) 
			|| this.getTo().before(period.getFrom()));
	}
	
	/**
	 * Verifica l'intersezione non vuota tra due periodi.
	 *
	 * @param period il periodo da testare.
	 * @return <code>true</code> se questo periodo temporale interseca <code>period</code>; 
	 * <code>false</code> altrimenti.
	 */	
	public synchronized LocalizedPeriod intersection (final LocalizedPeriod period){
//		if (!this.isValid() || !period.isValid()){
//			throw new InvalidPeriodException ();
//		}
		
		final long maxFrom = Math.max (this.getFrom ().getTime (), period.getFrom ().getTime ());
		final long minTo = Math.min (this.getTo ().getTime (), period.getTo ().getTime ());

		if (maxFrom<minTo){
			return new Period (new Date (maxFrom), new Date (minTo));
		}
		return null;
	}
	
	/** 
	 * Verifica se questo periodo è valido.
	 *
	 * @return <code>true</code> se questo è un periodo temporale valido; 
	 * <code>false</code> altrimenti.
	 */
	public boolean isValid() {
		return this.from!=null 
			&& this.to!=null
			&& !this.from.after(this.to);
	}
	
	/** 
	 * Verifica se questo periodo non è terminato.
	 *
	 * @return <code>true</code> se questo è un periodo temporale non terminato; 
	 * <code>false</code> altrimenti.
	 */
	public synchronized boolean isEndOpened() {
		return this.from!=null 
			&& this.to==null;
	}
	
	/**
	 * Ritorna la durata di questo periodo.
	 *
	 * @return la durata.
	 */	
	public synchronized Duration getDuration (){
		if (this.isEndOpened()){
			return new Duration (this.from, new Date ());
		} else {
			if (!isDurationComputed){
				this.computedDuration = new Duration (this.from, this.to);
				isDurationComputed = true;
			}
			return this.computedDuration;
		}
	}
	
	/**
	 * Ritorna la rappresentazione di formato stringa di questo periodo.
	 *
	 * @return una stringa che rappresenta questo periodo.
	 */	
	public String toString (){
		StringBuffer sb = new StringBuffer ();
		sb.append ("from: ").append (CalendarUtils.toTSString(this.from))
		.append (" to: ").append (CalendarUtils.toTSString(this.to));
		return sb.toString ();
	}
	
	/** 
	 * Ritorna la descrizione di questo periodo.
	 *
	 * @return la descrizione.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/** 
	 * Imposta la descrizione di questo periodo.
	 *
	 * @param description la nuova descrizione.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/** 
	 * Ritorna le note relative a questo periodo.
	 *
	 * @return le note del periodo.
	 */
	public String getNotes() {
		return this.notes;
	}
	
	/** 
	 * Imposta le note per questo periodo.
	 *
	 * @param notes il nuovo valore delle note.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}

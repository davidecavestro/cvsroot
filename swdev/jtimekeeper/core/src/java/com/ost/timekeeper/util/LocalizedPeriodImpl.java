/*
 * AbstractLocalizedPeriod.java
 *
 * Created on 20 marzo 2005, 8.48
 */

package com.ost.timekeeper.util;

import com.davidecavestro.common.util.CalendarUtils;
import java.util.Date;
import java.util.Observable;

/**
 * Una implementazione di periodo localizzato.
 *
 * @author  davide
 */
public class LocalizedPeriodImpl extends Observable implements LocalizedPeriod {
	
	/**
	 * La data di inizio periodo.
	 */
	private Date _from;
	
	/**
	 * La data di fine periodo.
	 */
	private Date _to;
	
	/**
	 * Durata calcolata (valida).
	 */
	private transient boolean isDurationComputed = false;
	
	/**
	 * La durata effettiva attuale (se <TT>isDurationComputed</TT> value <TT>tre</TT>.
	 */
	private transient Duration computedDuration;
	
	/**
	 * Costruttore vuoto.
	 */
	public LocalizedPeriodImpl () {
	}
	
	/**
	 * Costruttore con data di inizio e fine.
	 *
	 * @param from la data di inizio.
	 * @param to la data di fine.
	 */
	public LocalizedPeriodImpl (final Date from, final Date to) {
		this._from = from;
		this._to = to;
	}
	
	/**
	 *
	 * Costruttore copia.
	 *
	 * @param source la sorgente della copia.
	 */
	public LocalizedPeriodImpl (final LocalizedPeriod source) {
		this._from = source.getFrom ();
		this._to = source.getTo ();
	}
	
	/**
	 * Ritorna la data di inizio di questo periodo.
	 *
	 * @return la data d'inizio.
	 */
	public Date getFrom () {
		return this._from;
	}
	
	/**
	 * Imposta la data d'inizio per questo periodo.
	 *
	 * @param from la nuova data d'inizio.
	 */
	public synchronized void setFrom (Date from) {
		if (!CalendarUtils.equals (this._from,from)){
			this._from = from;
			this.isDurationComputed = false;
			this.setChanged ();
			this.notifyObservers ();
		}
	}
	
	/**
	 * Ritorna la data di fine di questo periodo.
	 *
	 * @return la data di fine.
	 */
	public Date getTo () {
		return this._to;
	}
	
	/**
	 * Imposta la data di fine per questo periodo.
	 *
	 * @param to la nuova data di fine del periodo.
	 */
	public synchronized void setTo (Date to) {
		if (!CalendarUtils.equals (this._to,to)){
			this._to = to;
			isDurationComputed = false;
			this.setChanged ();
			this.notifyObservers ();
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
		if (!isValid ()){
			return false;
		}
		if (!period.isValid ()){
			return false;
		}
		return ! (this.getFrom ().after (period.getTo ())
		|| this.getTo ().before (period.getFrom ()));
	}
	
	/**
	 * Calcola l'intersezione tra due periodi.
	 *
	 * @param period il periodo da testare.
	 * @return il periodo di intersezione.
	 */
	public synchronized LocalizedPeriod intersection (final LocalizedPeriod period){
		//		if (!this.isValid() || !period.isValid()){
		//			throw new InvalidPeriodException ();
		//		}
		
		final long maxFrom = Math.max (this.getFrom ().getTime (), period.getFrom ().getTime ());
		final long minTo = Math.min (this.getTo ().getTime (), period.getTo ().getTime ());
		
		if (maxFrom<=minTo){
			return new LocalizedPeriodImpl (new Date (maxFrom), new Date (minTo));
		}
		return null;
	}
	
	/**
	 * Verifica se questo periodo � valido.
	 *
	 * @return <code>true</code> se questo � un periodo temporale valido;
	 * <code>false</code> altrimenti.
	 */
	public boolean isValid () {
		return this._from!=null
		&& this._to!=null
		&& !this._from.after (this._to);
	}
	
	/**
	 * Verifica se questo periodo non � terminato.
	 *
	 * @return <code>true</code> se questo � un periodo temporale non terminato;
	 * <code>false</code> altrimenti.
	 */
	public synchronized boolean isEndOpened () {
		return this._from!=null
		&& this._to==null;
	}
	
	/**
	 * Ritorna la durata di questo periodo.
	 *
	 * @return la durata.
	 */
	public synchronized Duration getDuration (){
		if (this.isEndOpened ()){
			return new Duration (this._from, new Date ());
		} else {
			if (!isDurationComputed){
				this.computedDuration = new Duration (this._from, this._to);
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
		sb.append ("from: ").append (CalendarUtils.toTSString (this._from))
		.append (" to: ").append (CalendarUtils.toTSString (this._to));
		return sb.toString ();
	}
	
	/**
	 * Compara dueperiodi per l'ordinamento secondo la data d'inizio.
	 *
	 * @param compare il periodo da comparare.
	 * @return il valore <TT>0</TT> se le date di inizio sono uguali;
	 * un valore negativo se la data d'inizio del periodo specificato � 
	 * antecedente all'inizio di questo periodo;
	 * e un valore positivo se la data d'inizio del periodo specificato � 
	 * posteriore all'inizio di questo periodo;
	 * @see java.util.Date#compareTo
	 */	
	public int compareToStart (LocalizedPeriod compare){
		return this._from.compareTo (compare.getFrom ());
	}
	
	/**
	 * Compara due periodi per l'ordinamento secondo la data di fine.
	 *
	 * @param compare il periodo da comparare.
	 * @return il valore <TT>0</TT> se le date di fine sono uguali;
	 * un valore negativo se la data di fine del periodo specificato � 
	 * antecedente alla fine di questo periodo;
	 * e un valore positivo se la data di fine del periodo specificato � 
	 * posteriore alla fine di questo periodo;
	 * @see java.util.Date#compareTo
	 */	
	public int compareToFinish (LocalizedPeriod compare){
		return this._to.compareTo (compare.getTo ());
	}
	
	/**
	 *
	 * @param object
	 * @return
	 */	
	public boolean equals (Object object){
		if (this == object){
			return true;
		}
		if (null == object){
			return false;
		}
		if (object instanceof LocalizedPeriod){
			final LocalizedPeriod test = (LocalizedPeriod)object;
			if (!test.isValid ()){
				return false;
			}
			if (!this.isValid ()){
				return false;
			}
			return test.getFrom ().equals (this._from) && test.getTo ().equals (this._to);
		}
		return false;
	}
	
	public int hashCode (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (this.getClass ().getName ()).append ("@@@");
		sb.append (this._from);
		sb.append ("@@@");
		sb.append (this._to);
		return sb.toString ().hashCode ();
	}
	
}


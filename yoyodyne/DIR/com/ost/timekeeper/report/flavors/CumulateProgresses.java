/*
 * CumulateProgresses.java
 *
 * Created on 19 marzo 2005, 14.38
 */

package com.ost.timekeeper.report.flavors;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.report.*;
import com.ost.timekeeper.report.filter.*;
import com.ost.timekeeper.util.Duration;
import com.ost.timekeeper.util.LocalizedPeriod;
import com.ost.timekeeper.util.LocalizedPeriodImpl;
import java.text.DecimalFormat;
import java.util.*;
import org.jdom.*;

/**
 * Estrae i deti per il report degli avanzamenti cumulati.
 *
 * @author  davide
 */
public final class CumulateProgresses extends AbstractDataExtractor {
	/**
	 * Il tag radice.
	 */
	public final static String ROOT_ELEMENT = "cumulateprogresses";
	/**
	 * Il tag di periodo.
	 */
	public final static String PERIOD_ELEMENT = "period";
	/**
	 * Il tag di nome periodo.
	 */
	public final static String PERIODNAME_ELEMENT = "periodname";
	/**
	 * Il tag di durata avanzamento locale in millisecondi.
	 */
	public final static String MILLISECLOCALDURATION_ELEMENT = "milliseclocalduration";
	/**
	 * Il tag di durata avanzamento locale.
	 */
	public final static String LOCALDURATION_ELEMENT = "localduration";
	
	/**
	 * Il tag di durata avanzamento del sottoalbero in millisecondi.
	 */
	public final static String MILLISECSUBTREEDURATION_ELEMENT = "millisecsubtreeduration";
	/**
	 * Il tag di durata avanzamento del sottoalbero .
	 */
	public final static String SUBTREEDURATION_ELEMENT = "subtreeduration";
	
	/**
	 * Identificatore dell'attributo <TT>FROM</TT> in qualit� di obiettivo di un filtro.
	 */
	public final static Target PROGRESS_FROM = new Target (){};
	
	/**
	 * Identificatore dell'attributo <TT>TO</TT> in qualit� di obiettivo di un filtro.
	 */
	public final static Target PROGRESS_TO = new Target (){};
	
	/**
	 * La radice del sottoalbero di interesse.
	 */
	private ProgressItem _subtreeRoot;
	
	/**
	 * Costruttore.
	 * @param filters i filtri da applicare.
	 * @param subtreeRoot la radice del sottoalbero di interesse per il report.
	 */
	public CumulateProgresses (final ProgressItem subtreeRoot, final com.ost.timekeeper.report.filter.TargetedFilterContainer[] filters) {
		super (filters);
		this._subtreeRoot = subtreeRoot;
	}
	
	/**
	 * Ritorna la radice del sottoalbero di interesse per il report.
	 *
	 * @return la radice del sottoalbero di interesse per il report.
	 */
	public ProgressItem getSubtreeRoot (){
		return this._subtreeRoot;
	}
	
	/**
	 * Estrae e ritorna i dati per il report.
	 *
	 * @return i dati per il report.
	 */
	public org.jdom.Document extract () {
		final Element rootElement = new Element (ROOT_ELEMENT);
		final ApplicationData applicationData = ApplicationData.getInstance ();
		rootElement.addContent (new Comment ("Generated by "+applicationData.getApplicationInternalName ()+ " v."+applicationData.getVersionNumber ()+ " build "+applicationData.getBuildNumber ()));
		final org.jdom.Document data = new org.jdom.Document (rootElement);
		
		final List localProgresses = new ArrayList ();
		final List subtreeProgresses = new ArrayList ();
		
		Calendar now = new GregorianCalendar ();
		
		now.set (Calendar.HOUR_OF_DAY, 0);
		now.set (Calendar.MINUTE, 0);
		now.set (Calendar.SECOND, 0);
		now.set (Calendar.MILLISECOND, 0);
//		now.roll (Calendar.DATE, 1);
		final Date periodFinishDate = new Date (now.getTime ().getTime ());
		
		now.roll (Calendar.DATE, -7);
		final Date periodStartDate = new Date (now.getTime ().getTime ());
		
		final TimeCumulationScale map = new TimeCumulationScale (periodStartDate, periodFinishDate, 1);
		
//		Date currentPeriodStartDate = periodStartDate;
//		Calendar c = new GregorianCalendar ();
//		c.set (Calendar.HOUR_OF_DAY, 0);
//		c.set (Calendar.MINUTE, 0);
//		c.set (Calendar.SECOND, 0);
//		c.set (Calendar.MILLISECOND, 0);
		
//		final int step = 1;
//		c.roll (Calendar.DATE, step);
//		Date currentPeriodFInishDate = new Date (c.getTime ().getTime ());
		
//		CumulationPeriod currentCumulationPeriod;
		final ProgressItem root = this._subtreeRoot;
//		boolean jumpToNextPeriod = false;
		/*@todo implementare l'estrazione dati */
		for (final Iterator it = root.getSubtreeProgresses ().iterator ();it.hasNext ();){
			final Progress progress = (Progress)it.next ();
			
			/* avanzamento locale */
			map.addDuration (root, progress);
		}
		
		for (final Iterator it = map.iterateCumulationPeriod ();it.hasNext ();){
			final CumulationPeriod cumulationPeriod = (CumulationPeriod)it.next ();
			final Cumulation cumulation = map.getCumulation (cumulationPeriod);
			
			final Element progressElement = new Element (PERIOD_ELEMENT);
			rootElement.addContent (progressElement);
			
			{
				final Element element = new Element (PERIODNAME_ELEMENT);
				element.setText (com.ost.timekeeper.util.CalendarUtils.getTimestamp (cumulationPeriod.getFrom (), "MM/dd"));
				progressElement.addContent (element);
			}
			
			{
				final Element element = new Element (MILLISECLOCALDURATION_ELEMENT);
				element.setText (Double.toString (cumulation.getLocalDuration ()));
				progressElement.addContent (element);
			}
			
			{
				final Element element = new Element (LOCALDURATION_ELEMENT);
				element.setText (getDurationLabel (new Duration ((long)cumulation.getLocalDuration ())));
				progressElement.addContent (element);
			}
			
			{
				final Element element = new Element (MILLISECSUBTREEDURATION_ELEMENT);
				element.setText (Double.toString (cumulation.getSubtreeDuration ()));
				progressElement.addContent (element);
			}
			
			{
				final Element element = new Element (SUBTREEDURATION_ELEMENT);
				element.setText (getDurationLabel (new Duration ((long)cumulation.getSubtreeDuration ())));
				progressElement.addContent (element);
			}
			
		}
		return data;
	}
	
	/**
	 * Ritorna una rappresentazion ein formato stringa di questo estrattore.
	 *
	 * @return una stringa che rappresenta questo estrattore di dati.
	 */
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (" subtree root: ");
		sb.append (this._subtreeRoot);
		return sb.toString ();
	}
	
	private String getDurationLabel (Duration duration){
		if (duration==null){
			duration = Duration.ZERODURATION;
		}
		final StringBuffer sb = new StringBuffer ();
		/*
		sb.append (durationNumberFormatter.format(duration.getDays()))
		.append (":")
		 */
		sb.append (durationNumberFormatter.format (duration.getHours ()))
		.append (":")
		.append (durationNumberFormatter.format (duration.getMinutes ()))
		.append (":")
		.append (durationNumberFormatter.format (duration.getSeconds ()));
		return sb.toString ();
	}
	
	
	private final class TimeCumulationScale {
		
		private SortedMap _map = new TreeMap ();
		
		public TimeCumulationScale (final Date from, final Date to, final int step){
			Date current = from;
			while (!current.after (to)){
				final Date currentEnd = new Date (current.getTime ()+step*Duration.MILLISECONDS_PER_DAY);
				_map.put (new CumulationPeriod (current, currentEnd), new Cumulation (0, 0));
				current = currentEnd;
			}
		}
		
		public Iterator iterateCumulationPeriod (){
			return this._map.keySet ().iterator ();
		}
		
		public Cumulation getCumulation (CumulationPeriod cumulationPeriod){
			return (Cumulation)_map.get (cumulationPeriod);
		}
		
		public void addDuration (final ProgressItem progressItem, final Progress progress){
//			final Date from = progress.getFrom ();
//			final Date to = progress.getTo ();
			
			/*
			 * pessimizzazione!!!
			 * @todo diminuire complessit� algoritmo di ricerca, magari usando la TreeMap come dio comanda (adesso no, ho sonno)
			 */
			//			final Map subMap = _map.subMap (from, to);
			final Map subMap = _map;
			for (final Iterator it = subMap.keySet ().iterator ();it.hasNext ();){
				final CumulationPeriod cumulationPeriod = (CumulationPeriod)it.next ();
				testForCumulationUpdate (progressItem, progress, cumulationPeriod);
			}
		}
		
		/**
		 * Determinala necessit� ed eventualmenteeffettua l'aggiornamento delperiodo di cumulo.
		 */
		private void testForCumulationUpdate (final ProgressItem progressItem, final Progress progress, final CumulationPeriod cumulationPeriod){
			if (cumulationPeriod.intersects (progress)){
				boolean localDuration = progress.getProgressItem ()==progressItem;
				
				final Cumulation cumulation = (Cumulation)_map.get (cumulationPeriod);
				if (localDuration){
					cumulation.setLocalDuration (cumulation.getLocalDuration ()+progress.getDuration ().getTime ());
				} else {
					cumulation.setSubtreeDuration (cumulation.getSubtreeDuration ()+progress.getDuration ().getTime ());
				}
			}
		}
		
		public Cumulation geDuration (final CumulationPeriod period){
			return (Cumulation)_map.get (period);
		}
	}
	
	private final class CumulationPeriod extends LocalizedPeriodImpl implements Comparable {
		public CumulationPeriod (final Date from, final Date to){
			super (from, to);
			if (null==from){
				throw new IllegalArgumentException ("Invalid 'from' value: "+from);
			}
			if (null==to){
				throw new IllegalArgumentException ("Invalid 'to' value: "+to);
			}
		}
		
		public int compareTo (Object o) {
			return compareToStart ((CumulationPeriod)o);
		}
		
	}
	
	private final class Cumulation {
		private double _localDuration;
		private double _subtreeDuration;
		
		public Cumulation (final double localDuration, final double subtreeDuration){
			this._localDuration=localDuration;
			this._subtreeDuration=subtreeDuration;
		}
		
		public void setLocalDuration (final double duration){
			this._localDuration = duration;
		}
		public void setSubtreeDuration (final double duration){
			this._subtreeDuration = duration;
		}
		public double getLocalDuration (){
			return this._localDuration;
		}
		public double getSubtreeDuration (){
			return this._subtreeDuration;
		}
	}
	
	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	
	
}

/*
 * CumulateLocalProgresses.java
 *
 * Created on 04 aprile 2005, 19.45
 */

package com.davidecavestro.timekeeper.report.flavors;

import com.csvreader.CsvWriter;
import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.util.CalendarUtils;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.report.AbstractDataExtractor;
import com.davidecavestro.timekeeper.report.filter.Target;
import com.davidecavestro.timekeeper.report.filter.TargetedFilterContainer;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.util.Duration;
import com.ost.timekeeper.util.LocalizedPeriod;
import com.ost.timekeeper.util.LocalizedPeriodImpl;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Estrae i deti per il report degli avanzamenti cumulati locali al nodo radice ed al sottoalbero.
 *
 * @author  davide
 */
public final class CumulateLocalProgresses extends AbstractDataExtractor {
	/**
	 * Identificatore dell'attributo <TT>FROM</TT> in qualita' di obiettivo di un filtro.
	 */
	public final static Target PROGRESS_FROM = new Target (){};
	
	/**
	 * Identificatore dell'attributo <TT>TO</TT> in qualita' di obiettivo di un filtro.
	 */
	public final static Target PROGRESS_TO = new Target (){};
	
	/**
	 * La radice del sottoalbero di interesse.
	 */
	private final Task _subtreeRoot;
	
	/**
	 * Il numero di giorni dilunghezza del periodo
	 */
	private final int _periodLength;
	
	/**
	 * Il numero di periodi di interesse
	 */
	private final int _periodCount;
	
	private final Date _date;

	private final ApplicationContext _context ;
	
	/**
	 * Costruttore.
	 * @param date la data di partenza
	 * @param periodLength la lunghezza (in giorni) del periodo
	 * @param periodCount il numero di periodi
	 * @param filters i filtri da applicare.
	 * @param subtreeRoot la radice del sottoalbero di interesse per il report.
	 */
	public CumulateLocalProgresses (final ApplicationContext context, final Task subtreeRoot, final TargetedFilterContainer[] filters, final Date date, final int periodLength, final int periodCount) {
		super (filters);
		this._subtreeRoot = subtreeRoot;
		this._date=date;
		this._periodLength = periodLength;
		this._periodCount = periodCount;
		_context = context;
	}
	
	/**
	 * Ritorna la radice del sottoalbero di interesse per il report.
	 *
	 * @return la radice del sottoalbero di interesse per il report.
	 */
	public Task getSubtreeRoot (){
		return this._subtreeRoot;
	}
	
	/**
	 * Estrae e ritorna i dati per il report.
	 *
	 * @return i dati per il report.
	 */
	public Collection extract () {
		final ApplicationData applicationData = _context.getApplicationData ();
		
		final List localProgresses = new ArrayList ();
		final List subtreeProgresses = new ArrayList ();
		
		final Calendar now = new GregorianCalendar ();
		
		
		if (this._date!=null){
			now.setTime (this._date);
		}
		now.set (Calendar.HOUR_OF_DAY, 0);
		now.set (Calendar.MINUTE, 0);
		now.set (Calendar.SECOND, 0);
		now.set (Calendar.MILLISECOND, 0);
		//		now.roll (Calendar.DATE, 1);
		final Date periodFinishDate = new Date (now.getTime ().getTime ());
		
		now.add (Calendar.DAY_OF_YEAR, -1*_periodLength*_periodCount);
		final Date periodStartDate = new Date (now.getTime ().getTime ());
		
		final TimeCumulationScale map = new TimeCumulationScale (periodStartDate, periodFinishDate, _periodLength);
		
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
		final Task root = this._subtreeRoot;
		//		boolean jumpToNextPeriod = false;
		
		for (final Iterator it = root.getSubtreeProgresses ().iterator ();it.hasNext ();){
			final Progress progress = (Progress)it.next ();
			
			if (match (PROGRESS_FROM, progress.getFrom ()) 
				&& match (PROGRESS_TO, progress.getTo ())){
				/* filtri superati */
				map.addDuration (progress);
			}
		}
		
		final Collection<CumulateLocalProgressesRowBean> data = new ArrayList<CumulateLocalProgressesRowBean> ();
		
		
		
		final IdentificatorAssigner ia = new IdentificatorAssigner ();
		try {
		final CsvWriter csvw = new CsvWriter ("/tmp/prova.csv");
		csvw.setDelimiter (';');
			csvw.writeRecord (
				new String[] {
				"periodID",
				"periodName",
				"periodStart",
				"periodTotalEffort",
				"progressDescription",
				"progressEffort",
				"progressEnd",
				"progressNotes",
				"progressStart",
				"taskHierarchy",
				"taskID",
				"taskName",
				"taskTotalEffort"
				}
				);
		/*
		 *
		 * per ogni peridodo
		 */
		for (final Iterator<CumulationPeriod> periodIterator = map.iterateCumulationPeriod ();periodIterator.hasNext ();){
			final CumulationPeriod cumulationPeriod = periodIterator.next ();
			
			
			final int periodID = ia.assignPeriodID ();
			final String periodName = CalendarUtils.getTimestamp (cumulationPeriod.getFrom (), "MM/dd");
			
			System.out.println ("processing period "+periodName);
			
			final Timestamp periodStart = new Timestamp (cumulationPeriod.getFrom ().getTime ());
//			final String periodName = CalendarUtils.getTimestamp (cumulationPeriod.getFrom (), "MM/dd");

			double periodDuration = 0;
			/* 
			 * qui si duplica l'iterazione per calcolare la durata del periodo (abbstanza osceno, pardon)
			 * @todo ottimizzare
			 */
			for (final Iterator<Task> nodeIterator = cumulationPeriod.iterateProgressItems ();nodeIterator.hasNext ();){
				final Task progressItem = nodeIterator.next ();
				final NodeProgresses detail = cumulationPeriod.getDetail (progressItem);
				periodDuration += detail.getDuration ();
			}
			final Time periodTotalEffort = new Time ((long)periodDuration);
			
			for (final Iterator<Task> nodeIterator = cumulationPeriod.iterateProgressItems ();nodeIterator.hasNext ();){
				final Task progressItem = nodeIterator.next ();
				final NodeProgresses detail = cumulationPeriod.getDetail (progressItem);
				final double duration = detail.getDuration ();
				
			System.out.println ("processing task "+progressItem.getName ());

				final Time taskTotalEffort = new Time ((long)detail.getDuration ());
				
				final String taskName = progressItem.getName ();
				final int taskID = ia.getTaskID (progressItem);
				
				/* Gerarchia */
				final StringBuffer hierarchyData = new StringBuffer ();
				Task parent = progressItem.getParent ();
				while (parent!=null) {
					final StringBuffer ancestorData = new StringBuffer ();
					ancestorData.append ("/");
					final String code = parent.getCode ();
					if (code!=null && code.length ()>0){
						ancestorData.append (code).append (" - ");
					}
					ancestorData.append (parent.getName ()).append (" ");
					hierarchyData.insert (0, ancestorData);
					parent = parent.getParent ();
				}

				final String taskHierarchy = hierarchyData.toString ();
				
				for (final Iterator<CumulationPeriodNodeProgress> detailIterator = detail.iterateProgresses ();detailIterator.hasNext ();){
					final CumulationPeriodNodeProgress nodeProgress = detailIterator.next ();
					final Progress progress = nodeProgress.getProgress ();

					final LocalizedPeriod periodOfInterest = nodeProgress.getPeriodOfInterest ();
					
					final CumulateLocalProgressesRowBean row = new CumulateLocalProgressesRowBean ();

					data.add (row);
					
					row.setProgressStart (new Timestamp (periodOfInterest.getFrom ().getTime ()));
					row.setProgressEnd (new Timestamp (periodOfInterest.getTo ().getTime ()));
					
					row.setProgressEffort (new Time (periodOfInterest.getDuration ().getTime ()));
					row.setProgressDescription (progress.getDescription ());
					row.setProgressNotes (progress.getNotes ());
					
					System.out.println ("processing progress "+progress);
					
					row.setPeriodID (periodID);
					
					row.setTaskHierarchy (taskHierarchy);
					row.setTaskName (taskName);
					row.setTaskTotalEffort (taskTotalEffort);
					row.setPeriodTotalEffort (periodTotalEffort);
					row.setPeriodStart (periodStart);
					row.setPeriodName (periodName);
					row.setTaskID (taskID);
					
					csvw.writeRecord (
						new String[] {
						Integer.toString (row.getPeriodID ()),
						row.getPeriodName (),
						row.getPeriodStart ()!=null?row.getPeriodStart ().toString ():null,
						row.getPeriodTotalEffort ()!=null?row.getPeriodTotalEffort ().toString ():null,
						row.getProgressDescription (),
						row.getProgressEffort ()!=null?row.getProgressEffort ().toString ():null,
						row.getProgressEnd ()!=null?row.getProgressEnd ().toString ():null,
						row.getProgressNotes (),
						row.getProgressStart ()!=null?row.getProgressStart ().toString ():null,
						row.getTaskHierarchy (),
						Integer.toString (row.getTaskID ()),
						row.getTaskName (),
						row.getTaskTotalEffort ()!=null?row.getTaskTotalEffort ().toString ():null
						}
						);
					
				}

				
			}
		}
			csvw.flush ();
		} catch (IOException ioe) {
			throw new RuntimeException (ioe);
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

		sb.append (durationNumberFormatter.format (duration.getTotalHours ()))
		.append (":")
		.append (durationNumberFormatter.format (duration.getMinutes ()))
		.append (":")
		.append (durationNumberFormatter.format (duration.getSeconds ()));
		return sb.toString ();
	}
	
	
	private final class TimeCumulationScale {
		
		private SortedSet<CumulationPeriod> _set = new TreeSet<CumulationPeriod> ();
		
		public TimeCumulationScale (final Date from, final Date to, final int step){
			Date current = from;
			while (!current.after (to)){
				final Date currentEnd = new Date (current.getTime ()+step*Duration.MILLISECONDS_PER_DAY);
				_set.add (new CumulationPeriod (current, currentEnd));
				current = currentEnd;
			}
		}
		
		public Iterator<CumulationPeriod> iterateCumulationPeriod (){
			return _set.iterator ();
		}
		
		public void addDuration (final Progress progress){
			
			/*
			 * @todo diminuire complessita' algoritmo di ricerca, magari usando la TreeMap come dio comanda (adesso no, ho sonno)
			 */
			//			final Map subMap = _map.subMap (from, to);
			final Set subSet = _set;
			for (final Iterator<CumulationPeriod> it = subSet.iterator ();it.hasNext ();){
				final CumulationPeriod cumulationPeriod = it.next ();
				cumulationPeriod.computeProgress (progress);
			}
		}
		
	}
	
	/**
	 * Un periodo.
	 */
	private final class CumulationPeriod extends LocalizedPeriodImpl implements Comparable {
		private final Map<Task, NodeProgresses> _map;
		
		public CumulationPeriod (final Date from, final Date to){
			super (from, to);
			if (null==from){
				throw new IllegalArgumentException ("Invalid 'from' value: "+from);
			}
			if (null==to){
				throw new IllegalArgumentException ("Invalid 'to' value: "+to);
			}
			_map = new HashMap<Task, NodeProgresses> ();
		}
		
		public int compareTo (Object o) {
			return compareToStart ((CumulationPeriod)o);
		}
		
		public void computeProgress ( final Progress progress ){
			if (!this.intersects (progress)){
				return;
			}
			final LocalizedPeriod intersection = this.intersection (progress);
			final Task progressItem = progress.getTask ();
			final double duration = intersection.getDuration ().getTime ();
			
			NodeProgresses detail = _map.get (progressItem);
			if (detail==null){
				detail = new NodeProgresses ();
				_map.put (progressItem, detail);
			}
			detail.addProgress (progress, intersection);
		}
		
		/**
		 * Ritorna l'iteratore sui nodi implicati.
		 *
		 * @return l'iteratore sui nodi implicati.
		 */
		public Iterator<Task> iterateProgressItems (){
			return _map.keySet ().iterator ();
		}
		
		public NodeProgresses getDetail (final Task progressItem){
			return _map.get (progressItem);
		}
	}
	
	/**
	 * Avanzamenti di interesse (nel periodo) per un nodo.
	 */
	private final class NodeProgresses {

		private double _duration = 0;
		private final List<CumulationPeriodNodeProgress> _nodeProgresses = new ArrayList<CumulationPeriodNodeProgress> ();
		public NodeProgresses (){}
		public void addProgress (final Progress progress, final LocalizedPeriod periodOfInterest){
			final double duration = periodOfInterest.getDuration ().getTime ();
			_duration += duration;
			_nodeProgresses.add (new CumulationPeriodNodeProgress (progress, periodOfInterest));
		}
		
		public double getDuration (){
			return this._duration;
		}
		
		public Iterator<CumulationPeriodNodeProgress> iterateProgresses (){
			return _nodeProgresses.iterator ();
		}
	}
	
	/**
	 * Avanzamento con durata di interesse associata (nel periodo).
	 */
	private final class CumulationPeriodNodeProgress {
		private final Progress _progress;
		private final LocalizedPeriod _periodOfInterest;
		
		public CumulationPeriodNodeProgress (final Progress progress, final LocalizedPeriod periodOfInterest){
			this._progress = progress;
			this._periodOfInterest = periodOfInterest;
		}
		
		public Progress getProgress (){
			return this._progress;
		}
		
		public LocalizedPeriod getPeriodOfInterest (){
			return this._periodOfInterest;
		}
	}
	
	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	

	private class IdentificatorAssigner {
		int periodCounter = 0;
		int taskCounter = 0;
		
		final Map<Task, Integer> taskIDMap = new HashMap<Task, Integer> ();

		public int assignPeriodID () {
			return ++periodCounter;
		}
		
		public int currentPeriodID () {
			return periodCounter;
		}
		
		public int assignTaskID (final Task t){
			taskIDMap.put (t, Integer.valueOf (taskCounter++));
			return taskCounter;
		}
		
		public int getTaskID (final Task t){
			if (taskIDMap.containsKey (t)) {
				return taskIDMap.get (t);
			} else {
				return assignTaskID (t);
			}
		}
	}
}

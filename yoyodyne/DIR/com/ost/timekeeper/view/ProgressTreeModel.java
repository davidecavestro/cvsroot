/*
 * ProgressTreeModel.java
 *
 * Created on 25 aprile 2004, 10.02
 */

package com.ost.timekeeper.view;

import java.lang.Comparable;

import com.ost.timekeeper.util.LocalizedPeriodImpl;

import java.util.*;

import javax.swing.event.*;
import javax.swing.tree.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.support.treetable.*;
import com.ost.timekeeper.util.*;
import java.text.*;

/**
 * Modello di albero degli avanzamenti.
 *
 * @author  davide
 */
public class ProgressTreeModel extends com.ost.timekeeper.ui.support.treetable.AbstractTreeTableModel implements Observer{
	
	private ProgressItem root;
	
 // Types of the columns.
    static protected Class[]  cTypes = { TreeTableModel.class,
					 String.class, String.class};
					 
	/**
	 * Costruttore con nodo radice.
	 *
	 * @param root il nodo radice.
	 */
	public ProgressTreeModel(final ProgressItem root) {
		super (root);
		load (root);
		initColumnsStructure ();
	}
	
	/**
	 * Costruttore con progetto.
	 *
	 * @param project il progetto.
	 */
	public ProgressTreeModel(final Project project) {
		super(project!=null?project.getRoot ():null);
		load (project);
		initColumnsStructure ();
	}
	
	private void initColumnsStructure (){
		this.columns = new Object[]{
			ResourceSupplier.getString(ResourceClass.UI, "controls", "node"),
			ResourceSupplier.getString(ResourceClass.UI, "controls", "duration"),
			ResourceSupplier.getString(ResourceClass.UI, "controls", "Today")
		};		
	}
	
	public final void load (ProgressItem root){
		this.root = root;
	}
	
	/**
	 * Inizializa il modello con i dati relativi al progetto.
	 * @param project il progetto.
	 */	
	public final void load (Project project){
		ProgressItem oldRoot = this.root;
		if (project!=null){
			//progetto non nullo
			this.root = project.getRoot();
		} else {
			//progetto nullo
			this.root = null;
		}
		if (this.root!=null){
			//c'e' una radice
			/*
			 * pessimizzazione!!!
			 */
			final TreeModelEvent tme = new TreeModelEvent (this, this.getPathToRoot(this.root));
			this.fireTreeStructureChanged(this, tme.getPath (), tme.getChildIndices (), tme.getChildren ());
		} else if (oldRoot!=null){
			//la radice non c'e', ma ce n'era una prima
			/*
			 * pessimizzazione!!!
			 */
			final TreeModelEvent tme = new TreeModelEvent (this, new Object[]{new ProgressItem ()});
			this.fireTreeStructureChanged(this, tme.getPath (), tme.getChildIndices (), tme.getChildren ());
		}
	}
	
	public Object getChild(Object parent, int index) {
		return ((ProgressItem)parent).childAt(index);
	}
	
	public int getChildCount(Object parent) {
		return ((ProgressItem)parent).childCount();
	}
	
	public int getIndexOfChild(Object parent, Object child) {
		return ((ProgressItem)parent).childIndex((ProgressItem)child);
	}
	
	public Object getRoot() {
		return this.root;
	}
	
	public boolean isLeaf(Object node) {
		return false;
	}
	
	public void valueForPathChanged(TreePath path, Object newValue) {
		ProgressItem   aNode = (ProgressItem)path.getLastPathComponent();
		
		//        aNode.setUserObject(newValue);
		aNode.itemChanged();
	}
	
	public void insertNodeInto(ProgressItem newChild, ProgressItem parent, int index){
		/* pessimizzazione ||| */
		final TreeModelEvent tme = new TreeModelEvent(this, getPathToRoot(parent),
		new int[]{index}, new Object[]{newChild});
		fireTreeNodesInserted(this, tme.getPath (), tme.getChildIndices (), tme.getChildren ());
	}
	
	public void removeNodeFromParent(ProgressItem toRemove){
		final ProgressItem parent = toRemove.getParent();
		int[] childIndex = new int[]{parent.childIndex(toRemove)};
		/* pessimizzazione||| */
		final TreeModelEvent tme =new TreeModelEvent(this, getPathToRoot(parent),
		childIndex, new Object[]{toRemove});
		fireTreeNodesRemoved(this, tme.getPath (), tme.getChildIndices (), tme.getChildren ());
	}
	
	/**
	 * Builds the parents of node up to and including the root node,
	 * where the original node is the last element in the returned array.
	 * The length of the returned array gives the node's depth in the
	 * tree.
	 *
	 * @param aNode the TreeNode to get the path for
	 */
	public ProgressItem[] getPathToRoot(ProgressItem aNode) {
		return getPathToRoot(aNode, 0);
	}
	
	/**
	 * Builds the parents of node up to and including the root node,
	 * where the original node is the last element in the returned array.
	 * The length of the returned array gives the node's depth in the
	 * tree.
	 *
	 * @param aNode  the TreeNode to get the path for
	 * @param depth  an int giving the number of steps already taken towards
	 *        the root (on recursive calls), used to size the returned array
	 * @return an array of TreeNodes giving the path from the root to the
	 *         specified node
	 */
	protected ProgressItem[] getPathToRoot(ProgressItem aNode, int depth) {
		ProgressItem[]              retNodes;
		// This method recurses, traversing towards the root in order
		// size the array. On the way back, it fills in the nodes,
		// starting from the root and working back to the original node.
		
		/* Check for null, in case someone passed in a null node, or
		   they passed in an element that isn't rooted at root. */
		if(aNode == null) {
			if(depth == 0)
				return null;
			else
				retNodes = new ProgressItem[depth];
		}
		else {
			depth++;
			if(aNode == root)
				retNodes = new ProgressItem[depth];
			else
				retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}
	
	public void update(Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals(ObserverCodes.PROJECTCHANGE)){
				this.load (((Application)o).getProject());
//			} else if (arg!=null && arg.equals(ObserverCodes.SELECTEDITEMCHANGE)){
				/*
				 * pessimizzazione!!!
				 */
//				final ProgressItem[] path = this.getPathToRoot(Application.getInstance ().getCurrentItem ());
//				if (path!=null){
//					final TreeModelEvent tme = new TreeModelEvent (this, path);
//					this.fireTreeNodesChanged (this, tme.getPath (), tme.getChildIndices (), tme.getChildren ());
////					this.fireTableChanged(this, tme.getPath (), tme.getChildIndices (), tme.getChildren ());
//				}
//			} else if (arg!=null && arg.equals(ObserverCodes.ITEMPROGRESSINGPERIODCHANGE)){
//				/*
//				 * pessimizzazione!!!
//				 */
//				final ProgressItem[] path = this.getPathToRoot(Application.getInstance ().getCurrentItem ());
//				if (path!=null){
//					final TreeModelEvent tme = new TreeModelEvent (this, path);
////					this.fireTreeNodesChanged (this, tme.getPath (), tme.getChildIndices (), tme.getChildren ());
////					this.fireTableChanged(this, tme.getPath (), tme.getChildIndices (), tme.getChildren ());
//				}
			}
		}
	}
	
	/** Le colonne.*/
	private Object[] columns;
	
	public int getColumnCount () {
		return cTypes.length;
	}
	
	public String getColumnName (int column) {
		return (String)columns[column];
	}
	
	private LocalizedPeriod _today = null;
	
	private final LocalizedPeriod getToday (){
		if (this._today==null){
			final Calendar now = new GregorianCalendar ();

			now.set (Calendar.HOUR_OF_DAY, 0);
			now.set (Calendar.MINUTE, 0);
			now.set (Calendar.SECOND, 0);
			now.set (Calendar.MILLISECOND, 0);
			//		now.roll (Calendar.DATE, 1);
			final Date periodStartDate = new Date (now.getTime ().getTime ());

			now.add (Calendar.DAY_OF_YEAR, 1);
			final Date periodFinishDate = new Date (now.getTime ().getTime ());
			this._today = new LocalizedPeriodImpl (periodStartDate, periodFinishDate);
		}
		
		return this._today;
		
	}
	
	
	/**
	 * Un periodo.
	 */
	private final class TodayPeriod extends LocalizedPeriodImpl implements Comparable {
		/*
		 * durata calcolata in millisecondi
		 */
		private long _millisecs = 0;
		
		public TodayPeriod (){
			super (getToday ());
			
		}
		
		public void reset (){
			setFrom (getToday ().getFrom ());
			setTo (getToday ().getTo ());
			this._millisecs = 0;
		}
		
		public int compareTo (Object o) {
			return compareToStart ((LocalizedPeriod)o);
		}
		
		/**
		 * Calcola la quota di lavoro appartenente al giorno odierno per l'avanzamento specificato.
		 */
		public void computeProgress ( Progress progress ){
			LocalizedPeriod toIntersect = null;
			if (!progress.isEndOpened ()){
				if (!this.intersects (progress)){
					return;
				}
				toIntersect = progress;
			} else {
				 //avanzamento in corso
				toIntersect = new LocalizedPeriodImpl (progress.getFrom (), new Date ());
			}
			
			final LocalizedPeriod intersection = this.intersection (toIntersect);
			this._millisecs += intersection.getDuration ().getTime ();
			
		}
		
		/**
		 * Ritorna la durata risultante del lavoro giornaliero
		 *@returns la durata risultante del lavoro giornaliero
		 */
		public Duration getTodayAmount (){
			return new Duration (this._millisecs);
		}
	}
	
	private final TodayPeriod _todayPeriod = new TodayPeriod ();
	
	
	public Object getValueAt (Object node, int column) {
		final ProgressItem progressItem = (ProgressItem)node;
		switch (column){
			case 0: return progressItem.getName ();
			case 1: 
			{
				long totalDuration = 0;
				for (final Iterator it = progressItem.getSubtreeProgresses ().iterator ();it.hasNext ();){
					final Progress period = (Progress)it.next ();
					totalDuration += period.getDuration ().getTime ();
				}
				
				if (totalDuration==0){
					return "";
				}
				
				final Duration duration = new Duration (totalDuration);
				final StringBuffer sb = new StringBuffer ();
				
//				final long days = duration.getDays();
//				
//				if (0==days){
//					sb.append ("__");
//				} else {
//					sb.append (durationNumberFormatter.format(duration.getDays()));
//				}
//				sb.append (" - ");
				 
				sb.append (durationNumberFormatter.format(duration.getTotalHours ()))
				.append (":")
				.append (durationNumberFormatter.format(duration.getMinutes()))
				.append (":")
				.append (durationNumberFormatter.format(duration.getSeconds()));
				return sb.toString ();
			}	
			case 2: /*@todo ottimizzare!*/
			{
				_todayPeriod.reset ();

				for (final Iterator it = progressItem.getSubtreeProgresses ().iterator ();it.hasNext ();){
					_todayPeriod.computeProgress ((Progress)it.next ());
				}

				final Duration duration = _todayPeriod.getTodayAmount ();
				if (duration.getTime ()==0){
					return "";
				}
				
				final StringBuffer sb = new StringBuffer ();
				
				sb.append (durationNumberFormatter.format(duration.getTotalHours ()))
				.append (":")
				.append (durationNumberFormatter.format(duration.getMinutes()))
				.append (":")
				.append (durationNumberFormatter.format(duration.getSeconds()));
				return sb.toString ();
			}	
			default: return null;
		}
	}
	
	private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();
	
	private static class DurationNumberFormatter extends DecimalFormat {
		public DurationNumberFormatter (){
			this.setMinimumIntegerDigits (2);
		}
	}
	
    /**
     * Returns the class for the particular column.
     */
    public Class getColumnClass(int column) {
	return cTypes[column];
    }

}

/*
 * WorkSpaceModelEvent.java
 *
 * Created on January 3, 2007, 9:35 AM
 *
 */

package com.davidecavestro.timekeeper.model.event;

import java.util.EventObject;

/**
 * Mutuato da ListDataEvent.
 *
 * @see javax.swing.event.ListDataEvent
 *
 * @author Davide Cavestro
 */
public class WorkSpaceModelEvent extends EventObject{
	
	/** Identifies one or more changes in the lists contents. */
	public static final int CONTENTS_CHANGED = 0;
	/** Identifies the addition of one or more contiguous items to the list */
	public static final int INTERVAL_ADDED = 1;
	/** Identifies the removal of one or more contiguous items from the list */
	public static final int INTERVAL_REMOVED = 2;
	
	private int type;
	private int index0;
	private int index1;
	
	/**
	 * Returns the event type. The possible values are:
	 * <ul>
	 * <li> {@link #CONTENTS_CHANGED}
	 * <li> {@link #INTERVAL_ADDED}
	 * <li> {@link #INTERVAL_REMOVED}
	 * </ul>
	 *
	 * @return an int representing the type value
	 */
	public int getType () { return type; }
	
	/**
	 * Returns the lower index of the range. For a single
	 * element, this value is the same as that returned by {@link #getIndex1}.
	 *
	 *
	 * @return an int representing the lower index value
	 */
	public int getIndex0 () { return index0; }
	/**
	 * Returns the upper index of the range. For a single
	 * element, this value is the same as that returned by {@link #getIndex0}.
	 *
	 * @return an int representing the upper index value
	 */
	public int getIndex1 () { return index1; }
	
	/**
	 * Constructs a ListDataEvent object. If index0 is >
	 * index1, index0 and index1 will be swapped such that
	 * index0 will always be <= index1.
	 *
	 * @param source  the source Object (typically <code>this</code>)
	 * @param type    an int specifying {@link #CONTENTS_CHANGED},
	 *                {@link #INTERVAL_ADDED}, or {@link #INTERVAL_REMOVED}
	 * @param index0  one end of the new interval
	 * @param index1  the other end of the new interval
	 */
	public WorkSpaceModelEvent (Object source, int type, int index0, int index1) {
		super (source);
		this.type = type;
		this.index0 = Math.min (index0, index1);
		this.index1 = Math.max (index0, index1);
	}
	
	/**
	 * Returns a string representation of this ListDataEvent. This method
	 * is intended to be used only for debugging purposes, and the
	 * content and format of the returned string may vary between
	 * implementations. The returned string may be empty but may not
	 * be <code>null</code>.
	 *
	 * @since 1.4
	 * @return  a string representation of this ListDataEvent.
	 */
	public String toString () {
		return getClass ().getName () +
			"[type=" + type +
			",index0=" + index0 +
			",index1=" + index1 + "]";
	}
}

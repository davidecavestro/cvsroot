/*
 * WorkSpaceModelListener.java
 *
 * Created on January 3, 2007, 9:38 AM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.model.event.WorkSpaceModelEvent;
import java.util.EventListener;

/**
 * Mutuato da ListDAtaListener.
 * @see javax.swing.ListDataListener
 *
 * @author Davide Cavestro
 */
public interface WorkSpaceModelListener extends EventListener {
	/**
	 * Sent after the indices in the index0,index1
	 * interval have been inserted in the data model.
	 * The new interval includes both index0 and index1.
	 *
	 * @param e  a <code>WorkSpaceModelEvent</code> encapsulating the
	 *    event information
	 */
	void intervalAdded (WorkSpaceModelEvent e);
	
	
	/**
	 * Sent after the indices in the index0,index1 interval
	 * have been removed from the data model.  The interval
	 * includes both index0 and index1.
	 *
	 * @param e  a <code>WorkSpaceModelEvent</code> encapsulating the
	 *    event information
	 */
	void intervalRemoved (WorkSpaceModelEvent e);
	
	
	/**
	 * Sent when the contents of the list has changed in a way
	 * that's too complex to characterize with the previous
	 * methods. For example, this is sent when an item has been
	 * replaced. Index0 and index1 bracket the change.
	 *
	 * @param e  a <code>WorkSpaceModelEvent</code> encapsulating the
	 *    event information
	 */
	void contentsChanged (WorkSpaceModelEvent e);
}

/*
 * WorkSpaceModel.java
 *
 * Created on January 3, 2007, 9:26 AM
 *
 */

package com.davidecavestro.timekeeper.model;

/**
 * Lista degli "workspace" o progetti.
 *
 * @author Davide Cavestro
 */
public interface WorkSpaceModel {
	/**
	 * Returns the length of the list.
	 * @return the length of the list
	 */
	int getSize ();
	
	/**
	 * Returns the value at the specified index.
	 * @param index the requested index
	 * @return the value at <code>index</code>
	 */
	WorkSpace getElementAt (int index);
	
	/**
	 * Adds a listener to the list that's notified each time a change
	 * to the data model occurs.
	 * @param l the <code>WorkSpaceModelListener</code> to be added
	 */
	void addWorkSpaceModelListener (WorkSpaceModelListener l);
	
	/**
	 * Removes a listener from the list that's notified each time a
	 * change to the data model occurs.
	 * @param l the <code>WorkSpaceModelListener</code> to be removed
	 */
	void removeWorkSpaceModelListener (WorkSpaceModelListener l);
	
}

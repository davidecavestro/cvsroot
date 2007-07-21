/*
 * WorkSpaceModelImpl.java
 *
 * Created on January 3, 2007, 9:46 AM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.persistence.PersistenceManager;
import com.davidecavestro.timekeeper.persistence.Transaction;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Lista di workspace. Ne viene gestita la persistenza.
 *
 * Mutuato da javax.swing.DefaultListModel
 * @see javax.swing.DefaultListModel
 *
 * @author Davide Cavestro
 */
public class WorkSpaceModelImpl extends AbstractWorkSpaceModel {
	
	private Vector<WorkSpace> delegate = new Vector<WorkSpace> ();
	
	/**
	 * Costruttore vuoto.
	 */
	public WorkSpaceModelImpl () {
	}
	
	/**
	 * Costruttore con inizializzazione della lista.
	 * 
	 * @param l la lista degli workspace gi&agrave; presenti.
	 */
	public WorkSpaceModelImpl (final List<WorkSpace> l) {
		init (l);
	}
	
	/**
	 * Inizializza la lista con gli workspace spacificati. Si assume che ssi godano gi&agrave; della persistenza.
	 * 
	 * @param l igli workspace.
	 */
	public void init (final List<WorkSpace> l) {
		delegate.addAll (l);
	}
	
	/**
	 * Returns the number of components in this list.
	 * <p>
	 * This method is identical to <code>size</code>, which implements the
	 * <code>List</code> interface defined in the 1.2 Collections framework.
	 * This method exists in conjunction with <code>setSize</code> so that
	 * <code>size</code> is identifiable as a JavaBean property.
	 *
	 * @return  the number of components in this list
	 * @see #size()
	 */
	public int getSize () {
		return delegate.size ();
	}
	
	/**
	 * Returns the component at the specified index.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>get(int)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 * @param      index   an index into this list
	 * @return     the component at the specified index
	 * @exception  ArrayIndexOutOfBoundsException  if the <code>index</code>
	 *             is negative or greater than the current size of this
	 *             list
	 * @see #get(int)
	 */
	public WorkSpace getElementAt (int index) {
		return delegate.elementAt (index);
	}
	
	/**
	 * Copies the components of this list into the specified array.
	 * The array must be big enough to hold all the objects in this list,
	 * else an <code>IndexOutOfBoundsException</code> is thrown.
	 *
	 * @param   anArray   the array into which the components get copied
	 * @see Vector#copyInto(WorkSpace[])
	 */
	public void copyInto (WorkSpace anArray[]) {
		delegate.copyInto (anArray);
	}
	
	/**
	 * Trims the capacity of this list to be the list's current size.
	 *
	 * @see Vector#trimToSize()
	 */
	public void trimToSize () {
		delegate.trimToSize ();
	}
	
	/**
	 * Increases the capacity of this list, if necessary, to ensure
	 * that it can hold at least the number of components specified by
	 * the minimum capacity argument.
	 *
	 * @param   minCapacity   the desired minimum capacity
	 * @see Vector#ensureCapacity(int)
	 */
	public void ensureCapacity (int minCapacity) {
		delegate.ensureCapacity (minCapacity);
	}
	
//	/**
//	 * Sets the size of this list.
//	 *
//	 * @param   newSize   the new size of this list
//	 * @see Vector#setSize(int)
//	 */
//	public void setSize (int newSize) {
//		int oldSize = delegate.size ();
//		delegate.setSize (newSize);
//		if (oldSize > newSize) {
//			fireIntervalRemoved (this, newSize, oldSize-1);
//		} else if (oldSize < newSize) {
//			fireIntervalAdded (this, oldSize, newSize-1);
//		}
//	}
	
	/**
	 * Returns the current capacity of this list.
	 *
	 * @return  the current capacity
	 * @see Vector#capacity()
	 */
	public int capacity () {
		return delegate.capacity ();
	}
	
	/**
	 * Returns the number of components in this list.
	 *
	 * @return  the number of components in this list
	 * @see Vector#size()
	 */
	public int size () {
		return delegate.size ();
	}
	
	/**
	 * Tests whether this list has any components.
	 *
	 * @return  <code>true</code> if and only if this list has
	 *          no components, that is, its size is zero;
	 *          <code>false</code> otherwise
	 * @see Vector#isEmpty()
	 */
	public boolean isEmpty () {
		return delegate.isEmpty ();
	}
	
	/**
	 * Returns an enumeration of the components of this list.
	 *
	 * @return  an enumeration of the components of this list
	 * @see Vector#elements()
	 */
	public Enumeration<WorkSpace> elements () {
		return delegate.elements ();
	}
	
	/**
	 *
	 * @see Vector#iterator()
	 */
	public Iterator<WorkSpace> iterator () {
		return delegate.iterator ();
	}
	
	/**
	 * Tests whether the specified object is a component in this list.
	 *
	 * @param   elem   an object
	 * @return  <code>true</code> if the specified object
	 *          is the same as a component in this list
	 * @see Vector#contains(WorkSpace)
	 */
	public boolean contains (WorkSpace elem) {
		return delegate.contains (elem);
	}
	
	/**
	 * Searches for the first occurrence of <code>elem</code>.
	 *
	 * @param   elem   an object
	 * @return  the index of the first occurrence of the argument in this
	 *          list; returns <code>-1</code> if the object is not found
	 * @see Vector#indexOf(WorkSpace)
	 */
	public int indexOf (WorkSpace elem) {
		return delegate.indexOf (elem);
	}
	
//	/**
//	 * Searches for the first occurrence of <code>elem</code>, beginning
//	 * the search at <code>index</code>.
//	 *
//	 * @param   elem    an desired component
//	 * @param   index   the index from which to begin searching
//	 * @return  the index where the first occurrence of <code>elem</code>
//	 *          is found after <code>index</code>; returns <code>-1</code>
//	 *          if the <code>elem</code> is not found in the list
//	 * @see Vector#indexOf(WorkSpace,int)
//	 */
//	public int indexOf (WorkSpace elem, int index) {
//		return delegate.indexOf (elem, index);
//	}
	
//	/**
//	 * Returns the index of the last occurrence of <code>elem</code>.
//	 *
//	 * @param   elem   the desired component
//	 * @return  the index of the last occurrence of <code>elem</code>
//	 *          in the list; returns <code>-1</code> if the object is not found
//	 * @see Vector#lastIndexOf(WorkSpace)
//	 */
//	public int lastIndexOf (WorkSpace elem) {
//		return delegate.lastIndexOf (elem);
//	}
	
//	/**
//	 * Searches backwards for <code>elem</code>, starting from the
//	 * specified index, and returns an index to it.
//	 *
//	 * @param  elem    the desired component
//	 * @param  index   the index to start searching from
//	 * @return the index of the last occurrence of the <code>elem</code>
//	 *          in this list at position less than <code>index</code>;
//	 *          returns <code>-1</code> if the object is not found
//	 * @see Vector#lastIndexOf(WorkSpace,int)
//	 */
//	public int lastIndexOf (WorkSpace elem, int index) {
//		return delegate.lastIndexOf (elem, index);
//	}
	
	/**
	 * Returns the component at the specified index.
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is negative or not less than the size of the list.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>get(int)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      index   an index into this list
	 * @return     the component at the specified index
	 * @see #get(int)
	 * @see Vector#elementAt(int)
	 */
	public WorkSpace elementAt (int index) {
		return delegate.elementAt (index);
	}
	
	/**
	 * Returns the first component of this list.
	 * Throws a <code>NoSuchElementException</code> if this
	 * vector has no components.
	 * @return     the first component of this list
	 * @see Vector#firstElement()
	 */
	public WorkSpace firstElement () {
		return delegate.firstElement ();
	}
	
	/**
	 * Returns the last component of the list.
	 * Throws a <code>NoSuchElementException</code> if this vector
	 * has no components.
	 *
	 * @return  the last component of the list
	 * @see Vector#lastElement()
	 */
	public WorkSpace lastElement () {
		return delegate.lastElement ();
	}
	
	/**
	 * Sets the component at the specified <code>index</code> of this
	 * list to be the specified object. The previous component at that
	 * position is discarded.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is invalid.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>set(int,WorkSpace)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      obj     what the component is to be set to
	 * @param      index   the specified index
	 * @see #set(int,WorkSpace)
	 * @see Vector#setElementAt(WorkSpace,int)
	 */
	public void setElementAt (WorkSpace obj, int index) {
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), delegate.elementAt (index));
			makePersistent (getPersistenceManager (), obj);
			
			delegate.setElementAt (obj, index);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireContentsChanged (this, index, index);
	}
	
	/**
	 * Deletes the component at the specified index.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is invalid.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>remove(int)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      index   the index of the object to remove
	 * @see #remove(int)
	 * @see Vector#removeElementAt(int)
	 */
	public void removeElementAt (int index) {
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), delegate.elementAt (index));
			
			delegate.removeElementAt (index);
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireIntervalRemoved (this, index, index);
	}
	
	/**
	 * Inserts the specified object as a component in this list at the
	 * specified <code>index</code>.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index
	 * is invalid.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>add(int,WorkSpace)</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @param      obj     the component to insert
	 * @param      index   where to insert the new component
	 * @exception  ArrayIndexOutOfBoundsException  if the index was invalid
	 * @see #add(int,WorkSpace)
	 * @see Vector#insertElementAt(WorkSpace,int)
	 */
	public void insertElementAt (WorkSpace obj, int index) {
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			delegate.insertElementAt (obj, index);
			
			makePersistent (getPersistenceManager (), obj);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireIntervalAdded (this, index, index);
	}
	
	/**
	 * Adds the specified component to the end of this list.
	 *
	 * @param   obj   the component to be added
	 * @see Vector#addElement(WorkSpace)
	 */
	public void addElement (WorkSpace obj) {
		int index = delegate.size ();
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			delegate.addElement (obj);
			
			makePersistent (getPersistenceManager (), obj);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireIntervalAdded (this, index, index);
	}
	
	/**
	 * Removes the first (lowest-indexed) occurrence of the argument
	 * from this list.
	 *
	 * @param   obj   the component to be removed
	 * @return  <code>true</code> if the argument was a component of this
	 *          list; <code>false</code> otherwise
	 * @see Vector#removeElement(WorkSpace)
	 */
	public boolean removeElement (WorkSpace obj) {
		int index = indexOf (obj);
		boolean rv = false;
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), obj);
			
			rv = delegate.removeElement (obj);
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		if (index >= 0) {
			fireIntervalRemoved (this, index, index);
		}
		return rv;
	}
	
	
	/**
	 * Removes all components from this list and sets its size to zero.
	 * <blockquote>
	 * <b>Note:</b> Although this method is not deprecated, the preferred
	 *    method to use is <code>clear</code>, which implements the
	 *    <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @see #clear()
	 * @see Vector#removeAllElements()
	 */
	public void removeAllElements () {
		int index1 = delegate.size ()-1;
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			
			deletePersistent (getPersistenceManager (), delegate);
			
			delegate.removeAllElements ();
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		if (index1 >= 0) {
			fireIntervalRemoved (this, 0, index1);
		}
	}
	
	
	/**
	 * Returns a string that displays and identifies this
	 * object's properties.
	 *
	 * @return a String representation of this object
	 */
	public String toString () {
		return delegate.toString ();
	}
	
	
	/* The remaining methods are included for compatibility with the
	 * Java 2 platform Vector class.
	 */
	
	/**
	 * Returns an array containing all of the elements in this list in the
	 * correct order.
	 *
	 * @return an array containing the elements of the list
	 * @see Vector#toArray()
	 */
	public WorkSpace[] toArray () {
		WorkSpace[] rv = new WorkSpace[delegate.size ()];
		delegate.copyInto (rv);
		return rv;
	}
	
	/**
	 * Returns the element at the specified position in this list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code>
	 * if the index is out of range
	 * (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index index of element to return
	 */
	public WorkSpace get (int index) {
		return delegate.elementAt (index);
	}
	
	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code>
	 * if the index is out of range
	 * (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index index of element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 */
	public WorkSpace set (int index, WorkSpace element) {
		WorkSpace rv = null;
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			rv = delegate.elementAt (index);
			
			deletePersistent (getPersistenceManager (), rv);
			makePersistent (getPersistenceManager (), element);
			
			delegate.setElementAt (element, index);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireContentsChanged (this, index, index);
		return rv;
	}
	
	/**
	 * Inserts the specified element at the specified position in this list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the
	 * index is out of range
	 * (<code>index &lt; 0 || index &gt; size()</code>).
	 *
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 */
	public void add (int index, WorkSpace element) {
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			delegate.insertElementAt (element, index);
			makePersistent (getPersistenceManager (), element);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		fireIntervalAdded (this, index, index);
	}
	
	/**
	 * Removes the element at the specified position in this list.
	 * Returns the element that was removed from the list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code>
	 * if the index is out of range
	 * (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index the index of the element to removed
	 */
	public WorkSpace remove (int index) {
		WorkSpace rv = delegate.elementAt (index);
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), rv);
			
			delegate.removeElementAt (index);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		fireIntervalRemoved (this, index, index);
		return rv;
	}
	
	/**
	 * Removes all of the elements from this list.  The list will
	 * be empty after this call returns (unless it throws an exception).
	 */
	public void clear () {
		int index1 = delegate.size ()-1;
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			deletePersistent (getPersistenceManager (), delegate);
			
			delegate.removeAllElements ();
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new RuntimeException (e);
		}
		
		if (index1 >= 0) {
			fireIntervalRemoved (this, 0, index1);
		}
	}
	
//	/**
//	 * Deletes the components at the specified range of indexes.
//	 * The removal is inclusive, so specifying a range of (1,5)
//	 * removes the component at index 1 and the component at index 5,
//	 * as well as all components in between.
//	 * <p>
//	 * Throws an <code>ArrayIndexOutOfBoundsException</code>
//	 * if the index was invalid.
//	 * Throws an <code>IllegalArgumentException</code> if
//	 * <code>fromIndex &gt; toIndex</code>.
//	 *
//	 * @param      fromIndex the index of the lower end of the range
//	 * @param      toIndex   the index of the upper end of the range
//	 * @see	   #remove(int)
//	 */
//	public void removeRange (int fromIndex, int toIndex) {
//		if (fromIndex > toIndex) {
//			throw new IllegalArgumentException ("fromIndex must be <= toIndex");
//		}
//		for(int i = toIndex; i >= fromIndex; i--) {
//			delegate.removeElementAt (i);
//		}
//		fireIntervalRemoved (this, fromIndex, toIndex);
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*-------------------------------------------------------------
	 * INIZIO sezione contenente i metodi di supporto alla persistenza
	 --------------------------------------------------------------*/
	
	
	/**
	 * Riferimento alla transazione fake, creata lazy.
	 */
	private Transaction _tx;
	/**
	 * Ritorna una transazione difacciata, con una implementazione vuota.
	 * <P>
	 * Scavalcare questo metodo per fornire una adeguata implementazione di Transactionse necessario (ad esempio una transazione JDO).
	 */
	protected Transaction getTransaction () {
		if (null == _tx) {
			_tx = new Transaction () {
				public void begin () {
				}
				public void commit () {
				}
				public void rollback () {
				}
			};
		}
		return _tx;
	}
	
	/**
	 * RIferimentoalla transazione fake, creata lazy.
	 */
	private PersistenceManager _pm;
	/**
	 * Ritorna un persistence manager difacciata, con una implementazione vuota.
	 * <P>
	 * Scavalcare questo metodo per fornire una adeguata implementazione di PersistenceManager se necessario (ad esempio un PersistenceManager JDO).
	 */
	protected PersistenceManager getPersistenceManager () {
		if (null == _pm) {
			_pm = new PersistenceManager () {
				public void deletePersistent (Object o) {
				}
				public void makePersistent (Object o) {
				}
				public void deletePersistentAll (Collection c) {
				}
				public void makePersistentAll (Collection c) {
				}
			};
		}
		return _pm;
	}
	
	
	/**
	 * Rimuove il sottoalbero, avanzamenti compresi.
	 *
	 * @param pm
	 * @param toDelete la radice del sottoalbero da rimuovere.
	 */
	private void deleteSubtreePersistent (final PersistenceManager pm, final List<Task> toDelete){
		for (final Task t : toDelete) {
			deleteSubtreePersistent (pm, t);
		}
	}
	
	/**
	 * Rimuove il sottoalbero, avanzamenti compresi.
	 *
	 * @param toDelete la radice del sottoalbero da rimuovere.
	 */
	private void deleteSubtreePersistent (final PersistenceManager pm, final Task toDelete){
		for (final Iterator it = toDelete.getChildren ().iterator ();it.hasNext ();){
			deleteSubtreePersistent (pm, (Task)it.next ());
		}
		for (final Iterator it = toDelete.getPiecesOfWork ().iterator ();it.hasNext ();){
			pm.deletePersistent ((PieceOfWork)it.next ());
		}
		pm.deletePersistent (toDelete);
	}
	
	/**
	 * Rimuove il workspace.
	 *
	 * @param toDelete il workspace da rimuovere.
	 */
	private void deletePersistent (final PersistenceManager pm, final WorkSpace toDelete){
		deleteSubtreePersistent (pm, toDelete.getRoot ());
		pm.deletePersistent (toDelete);
	}
	
	/**
	 * Rimuove i workspace.
	 *
	 * @param toDelete i workspace da rimuovere.
	 */
	private void deletePersistent (final PersistenceManager pm, final List<WorkSpace> toDelete){
		for (final WorkSpace ws : toDelete) {
			deletePersistent (pm, ws);
		}
	}
	
	
	

	/**
	 * Rende persistente il sottoalbero, avanzamenti compresi.
	 *
	 * @param pm
	 * @param toPersist la radice del sottoalbero da rendere persistente.
	 */
	private void makeSubtreePersistent (final PersistenceManager pm, final List<Task> toPersist){
		for (final Task t : toPersist) {
			makeSubtreePersistent (pm, t);
		}
	}
	
	/**
	 * Rende persistente il sottoalbero, avanzamenti compresi.
	 *
	 * @param toPersist la radice del sottoalbero da rendere persistente.
	 */
	private void makeSubtreePersistent (final PersistenceManager pm, final Task toPersist){
		for (final Iterator it = toPersist.getChildren ().iterator ();it.hasNext ();){
			makeSubtreePersistent (pm, (Task)it.next ());
		}
		for (final Iterator it = toPersist.getPiecesOfWork ().iterator ();it.hasNext ();){
			pm.makePersistent ((PieceOfWork)it.next ());
		}
		pm.makePersistent (toPersist);
	}
	
	/**
	 * Rende persistente il workspace.
	 *
	 * @param toPersist il workspace da rendere persistente.
	 */
	private void makePersistent (final PersistenceManager pm, final WorkSpace toPersist){
		makeSubtreePersistent (pm, toPersist.getRoot ());
		pm.makePersistent (toPersist);
	}
	
	/**
	 * Rende persistenti i workspace.
	 *
	 * @param toPersist i workspace da rendere persistenti.
	 */
	private void makePersistent (final PersistenceManager pm, final List<WorkSpace> toPersist){
		for (final WorkSpace ws : toPersist) {
			makePersistent (pm, ws);
		}
	}
		
	/*-------------------------------------------------------------
	 * FINE sezione contenente i metodi di supporto alla persistenza
	 --------------------------------------------------------------*/
	
	
}

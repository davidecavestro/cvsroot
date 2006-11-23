/*
 * DefaultTaskTreeModel.java
 *
 * Created on 2 dicembre 2005, 20.49
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelEvent;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelListener;
import java.util.EventListener;
import javax.swing.event.EventListenerList;

/**
 * Implementazione parziale dell'albero dei task di modello.
 *
 * <P>
 * Fornisce metodi di utilit&agrave; generale.
 *
 * @author  davide
 */
public abstract class AbstractTaskTreeModel implements TaskTreeModel {
	
	protected final EventListenerList listenerList = new EventListenerList ();
	
	private Task _root;
	
	/** Costruttore vuoto. */
	public AbstractTaskTreeModel () {
	}
	
	/**
	 * Costruttore con radice.
	 * @param root la radice dell'albero.
	 */
	public AbstractTaskTreeModel (final Task root) {
		_root = root;
	}
	
	
	/**
	 * Aggiunge un listener che deve essere notificato ad ogni modifica al modello.
	 *
	 * @param l il listener
	 */
	public void addTaskTreeModelListener (TaskTreeModelListener l) {
		listenerList.add (TaskTreeModelListener.class, l);
	}
	
	/**
	 * Rimuove un listener registrato du questo modello.
	 *
	 * @param l il listener da rimuovere.
	 */
	public void removeTaskTreeModelListener (TaskTreeModelListener l) {
		listenerList.remove (TaskTreeModelListener.class, l);
	}
	
	
	
	
	/**
	 * Ritonra un array di tutti gli oggetti attualmente registrati come
	 * listener del tipo <code><em>Foo</em>Listener</code>, su questo modello,
	 * dove <em>Foo</em>  e'  individuato dal tipo specificato.
	 *
	 *
	 * @return un array di oggetti regitrati come
	 *          <code><em>Foo</em>Listener</code>s su questo modello,
	 *			oppure un array vuoto, se nessun listener di tale tipo  e' ' stato registrato,
	 * @see #getTaskTreeModelListeners
	 * @param listenerType il tipo di lestener richiesto; questo parametro
	 *			dovrebbe individuare un'interfaccia che derivi da
	 *          <code>java.util.EventListener</code>
	 */
	public EventListener[] getListeners (Class listenerType) {
		return listenerList.getListeners (listenerType);
	}
	
	
	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 *
	 * @param source the node being changed
	 * @param path the path to the root node
	 * @param childIndices the indices of the changed elements
	 * @param children the changed elements
	 * @see EventListenerList
	 */
	protected void fireTasksChanged (Object source, TaskTreePath path,
		int[] childIndices,
		Task[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		TaskTreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskTreeModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TaskTreeModelEvent (source, path,
						childIndices, children);
				}
				((TaskTreeModelListener)listeners[i+1]).treeNodesChanged (e);
			}
		}
	}
	
	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 *
	 * @param source the node where new elements are being inserted
	 * @param path the path to the root node
	 * @param childIndices the indices of the new elements
	 * @param children the new elements
	 * @see EventListenerList
	 */
	protected void fireTasksInserted (Object source, TaskTreePath path,
		int[] childIndices,
		Task[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		TaskTreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskTreeModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TaskTreeModelEvent (source, path,
						childIndices, children);
				}
				((TaskTreeModelListener)listeners[i+1]).treeNodesInserted (e);
			}
		}
	}
	
	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 *
	 * @param source the node where elements are being removed
	 * @param path the path to the root node
	 * @param childIndices the indices of the removed elements
	 * @param children the removed elements
	 * @see EventListenerList
	 */
	protected void fireTasksRemoved (Object source, TaskTreePath path,
		int[] childIndices,
		Task[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		TaskTreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskTreeModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TaskTreeModelEvent (source, path,
						childIndices, children);
				}
				((TaskTreeModelListener)listeners[i+1]).treeNodesRemoved (e);
			}
		}
	}
	
	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 *
	 * @param source the node where the tree model has changed
	 * @param path the path to the root node
	 * @param childIndices the indices of the affected elements
	 * @param children the affected elements
	 * @see EventListenerList
	 */
	protected void fireTreeStructureChanged (Object source, TaskTreePath path,
		int[] childIndices,
		Task[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		TaskTreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskTreeModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TaskTreeModelEvent (source, path,
						childIndices, children);
				}
				((TaskTreeModelListener)listeners[i+1]).treeStructureChanged (e);
			}
		}
	}
	
	/*
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 *
	 * @param source the node where the tree model has changed
	 * @param path the path to the root node
	 * @see EventListenerList
	 */
	private void fireTreeStructureChanged (Object source, TaskTreePath path) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		TaskTreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskTreeModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TaskTreeModelEvent (source, path);
				}
				((TaskTreeModelListener)listeners[i+1]).treeStructureChanged (e);
			}
		}
	}
	
	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 *
	 * @param source the node being changed
	 * @param path the path to the root node
	 * @param childIndices the indices of the changed elements
	 * @param children the changed elements
	 * @see EventListenerList
	 */
	protected void firePiecesOfWorkChanged (Object source, TaskTreePath path,
		int[] childIndices,
		PieceOfWork[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		TaskTreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskTreeModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TaskTreeModelEvent (source, path,
						childIndices, children);
				}
				((TaskTreeModelListener)listeners[i+1]).treeNodesChanged (e);
			}
		}
	}
	
	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 *
	 * @param source the node where new elements are being inserted
	 * @param path the path to the root node
	 * @param childIndices the indices of the new elements
	 * @param children the new elements
	 * @see EventListenerList
	 */
	protected void firePiecesOfWorkInserted (Object source, TaskTreePath path,
		int[] childIndices,
		PieceOfWork[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		TaskTreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskTreeModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TaskTreeModelEvent (source, path,
						childIndices, children);
				}
				((TaskTreeModelListener)listeners[i+1]).treeNodesInserted (e);
			}
		}
	}
	
	/**
	 * Notifies all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 *
	 * @param source the node where elements are being removed
	 * @param path the path to the root node
	 * @param childIndices the indices of the removed elements
	 * @param children the removed elements
	 * @see EventListenerList
	 */
	protected void firePiecesOfWorkRemoved (Object source, TaskTreePath path,
		int[] childIndices,
		PieceOfWork[] children) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		TaskTreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==TaskTreeModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TaskTreeModelEvent (source, path,
						childIndices, children);
				}
				((TaskTreeModelListener)listeners[i+1]).treeNodesRemoved (e);
			}
		}
	}
	
	
	/**
	 * Sets the root to <code>root</code>. A null <code>root</code> implies
	 * the tree is to display nothing, and is legal.
	 */
	public void setRoot (Task root) {
		final Object oldRoot = _root;
		_root = root;
		if (root == null && oldRoot != null) {
			fireTreeStructureChanged (this, null);
		} else {
			nodeStructureChanged (root);
		}
	}
	
	public Task getRoot () {
		return _root;
	}
	
	/**
	 * Invoke this method if you've totally changed the children of
	 * node and its childrens children...  This will post a
	 * treeStructureChanged event.
	 */
	public void nodeStructureChanged (Task node) {
		if(node != null) {
			fireTreeStructureChanged (this, getPathToRoot (node), null, null);
		}
	}
	
	/**
	 * Builds the parents of node up to and including the root node,
	 * where the original node is the last element in the returned array.
	 * The length of the returned array gives the node's depth in the
	 * tree.
	 *
	 * @param aNode the Task to get the path for
	 */
	public TaskTreePath getPathToRoot (Task aNode) {
		return new TaskTreePath (aNode);
	}
	
	
	/**
	 * Invoke this method if you've modified the TreeNodes upon which this
	 * model depends.  The model will notify all of its listeners that the
	 * model has changed.
	 */
	public void reload () {
		reload (_root);
	}
	
//    /**
//      * This sets the user object of the Task identified by path
//      * and posts a node changed.  If you use custom user objects in
//      * the TreeModel you're going to need to subclass this and
//      * set the user object of the changed node to something meaningful.
//      */
//    public void valueForPathChanged(TaskTreePath path, Task newValue) {
//	Task   aNode = path.getLastPathComponent();
//
//        aNode.setUserObject(newValue);
//        nodeChanged(aNode);
//    }
	
	/**
	 * Invoked this to insert newChild at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate
	 * event. This is the preferred way to add children as it will create
	 * the appropriate event.
	 */
	public void insertNodeInto (Task newChild,
		Task parent, int index){
		parent.insert (newChild, index);
		
		int[]           newIndexs = new int[1];
		
		newIndexs[0] = index;
		nodesWereInserted (parent, newIndexs);
	}
	
	/**
	 * Message this to remove node from its parent. This will message
	 * nodesWereRemoved to create the appropriate event. This is the
	 * preferred way to remove a node as it handles the event creation
	 * for you.
	 */
	public void removeNodeFromParent (Task node) {
		Task         parent = node.getParent ();
		
		if(parent == null)
			throw new IllegalArgumentException ("node does not have a parent.");
		
		int[]            childIndex = new int[1];
		Task[]         removedArray = new Task[1];
		
		childIndex[0] = parent.childIndex (node);
		parent.remove (childIndex[0]);
		removedArray[0] = node;
		nodesWereRemoved (parent, childIndex, removedArray);
	}
	
	/**
	 * Invoke this method after you've changed how node is to be
	 * represented in the tree.
	 */
	public void nodeChanged (Task node) {
		if(listenerList != null && node != null) {
			Task         parent = node.getParent ();
			
			if(parent != null) {
				int        anIndex = parent.childIndex (node);
				if(anIndex != -1) {
					int[]        cIndexs = new int[1];
					
					cIndexs[0] = anIndex;
					nodesChanged (parent, cIndexs);
				}
			} else if (node == getRoot ()) {
				nodesChanged (node, null);
			}
		}
	}
	
	/**
	 * Invoke this method if you've modified the TreeNodes upon which this
	 * model depends.  The model will notify all of its listeners that the
	 * model has changed below the node <code>node</code> (PENDING).
	 */
	public void reload (Task node) {
		if(node != null) {
			fireTreeStructureChanged (this, getPathToRoot (node), null, null);
		}
	}
	
	/**
	 * Invoke this method after you've inserted some TreeNodes into
	 * node.  childIndices should be the index of the new elements and
	 * must be sorted in ascending order.
	 */
	public void nodesWereInserted (Task node, int[] childIndices) {
		if(listenerList != null && node != null && childIndices != null
			&& childIndices.length > 0) {
			int               cCount = childIndices.length;
			Task[]          newChildren = new Task[cCount];
			
			for(int counter = 0; counter < cCount; counter++)
				newChildren[counter] = node.childAt (childIndices[counter]);
			fireTasksInserted (this, getPathToRoot (node), childIndices,
				newChildren);
		}
	}
	
	/**
	 * Invoke this method after you've removed some TreeNodes from
	 * node.  childIndices should be the index of the removed elements and
	 * must be sorted in ascending order. And removedChildren should be
	 * the array of the children objects that were removed.
	 */
	public void nodesWereRemoved (Task node, int[] childIndices,
		Task[] removedChildren) {
		if(node != null && childIndices != null) {
			fireTasksRemoved (this, getPathToRoot (node), childIndices,
				removedChildren);
		}
	}
	
	/**
	 * Invoke this method after you've changed how the children identified by
	 * childIndicies are to be represented in the tree.
	 */
	public void nodesChanged (Task node, int[] childIndices) {
		if(node != null) {
			if (childIndices != null) {
				int            cCount = childIndices.length;
				
				if(cCount > 0) {
					Task[]       cChildren = new Task[cCount];
					
					for(int counter = 0; counter < cCount; counter++)
						cChildren[counter] = node.childAt
							(childIndices[counter]);
					fireTasksChanged (this, getPathToRoot (node),
						childIndices, cChildren);
				}
			} else if (node == getRoot ()) {
				fireTasksChanged (this, getPathToRoot (node), null, null);
			}
		}
	}
	
	/**
	 * Invoke this method after you've changed how node is to be
	 * represented in the tree.
	 */
	public void pieceOfWorkChanged (PieceOfWork pow) {
		if(listenerList != null && pow != null) {
			Task         parent = pow.getTask ();
			
			if(parent != null) {
				int        anIndex = parent.pieceOfWorkIndex (pow);
				if(anIndex != -1) {
					int[]        cIndexs = new int[1];
					
					cIndexs[0] = anIndex;
					nodesChanged (parent, cIndexs);
				}
			}
		}
	}
}

/*
 * DefaultTaskTreeModel.java
 *
 * Created on 2 dicembre 2005, 20.49
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.common.util.IllegalOperationException;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelEvent;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelListener;
import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelEvent;
import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.event.EventListenerList;

/**
 * Implementazione parziale dell'albero dei task di modello.
 *
 * <P>
 * Fornisce metodi di utilit&agrave; generale.
 *
 * @author  davide
 */
public abstract class AbstractTaskTreeModel implements TaskTreeModel, WorkAdvanceModel {
	
	private final Set<PieceOfWork> _advancingPOWs;
	
	protected final EventListenerList listenerList = new EventListenerList ();
	
	private Task _root;
	
	/** Costruttore vuoto. */
	private AbstractTaskTreeModel () {
		_advancingPOWs = new HashSet ();
	}
	
	/**
	 * Costruttore con radice.
	 * @param root la radice dell'albero.
	 */
	public AbstractTaskTreeModel (final Task root) {
		this ();
		if (root==null) {
			throw new NullPointerException ();
		}
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
		final Task oldRoot = _root;
		_root = root;
        if (root == null && oldRoot != null) {
			fireTreeStructureChanged (this, null);
		} else {
			nodeStructureChanged (root);
		}
		if (root!=oldRoot) {
			if (oldRoot!=null) {
				final PieceOfWork[] removed = eraseOldAdvancingProgresses (oldRoot);
				if (removed.length>0) {
					fireAdvancingRemoved (this, removed);
				}
			}
			
			if (root!=null) {
				final PieceOfWork[] discovered = discoverNewAdvancingProgresses (root);
				if (discovered.length>0) {
					fireAdvancingInserted (this, discovered);
				}
			}
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
		
		if (index < 0) {
			index = parent.childCount ();
		}
		parent.insert (newChild, index);
		
		int[]           newIndexs = new int[1];
		
		newIndexs[0] = index;
		
		final PieceOfWork[] discovered = discoverNewAdvancingProgresses (newChild);
		nodesWereInserted (parent, newIndexs);
		if (discovered.length>0) {
			fireAdvancingInserted (this, discovered);
		}
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

		final PieceOfWork[] erased = eraseOldAdvancingProgresses (node);
		
		childIndex[0] = parent.childIndex (node);
		parent.remove (childIndex[0]);
		removedArray[0] = node;
		nodesWereRemoved (parent, childIndex, removedArray);
		
		if (erased.length>0) {
			fireAdvancingRemoved (this, erased);
		}
		
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
	 * Invoked this to insert newPOW at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate
	 * event. This is the preferred way to add children as it will create
	 * the appropriate event.
	 */
	public void insertPieceOfWorkInto (PieceOfWork newPOW,
		Task t, int index){
		if (index < 0){
			index = t.pieceOfWorkCount ();
		}
		t.insert (newPOW, index);
		
		int[]           newIndexs = new int[1];
		
		newIndexs[0] = index;
		piecesOfWorkWereInserted (t, newIndexs);
		if (addToAdvancingIfNeeded (newPOW)){
			fireAdvancingInserted (this, new PieceOfWork [] {newPOW});
		}
	}
	
	/**
	 * Invocare questo metodo per rimuovere un avanzmaneto.
	 * Questo metodo si occupa di notificare la rimozione tramite .
	 */
	public void removePieceOfWork (PieceOfWork pow){
		final Task t = pow.getTask ();
		final int pos = t.pieceOfWorkIndex (pow);
		t.removePieceOfWOrk (pow);
		
		piecesOfWorkWereRemoved (t, new int[] {pos}, new PieceOfWork[] {pow});
		
		if (eraseIfNeeded (pow, false)){
			fireAdvancingRemoved (this, new PieceOfWork [] {pow});
		}
	}
	
	
	
	/**
	 * RImuove avanzamenti fratelli.
	 * 
	 * @param t il rask che contiene gli avazamenti da rimuovere.
	 * @param l gli avanzamenti da rimuovere.
	 */
	public void removePiecesOfWork (final Task t, final List<PieceOfWork> l) {
		final int count = l.size ();
		final int[] indices = new int [count];
		final PieceOfWork[] removed = new PieceOfWork [count];
		
		int i=0;
		for (final PieceOfWork p : l) {
			indices [i] = t.pieceOfWorkIndex (p);
			removed[i] = p;
			i++;
		}
		for (final PieceOfWork p : l) {
			p.getTask ().removePieceOfWOrk (p);
		}
		firePiecesOfWorkRemoved (this, new TaskTreePath (t), indices, removed);
	}
	
	/**
	 * Rimuove task fratelli.
	 * 
	 * @param parent il padre dei task da rimuovere.
	 * @param l i taskda rimuovere.
	 */
	public void removeTasks (final Task parent, final List<Task> l) {
		final TaskTreePath parentPath = new TaskTreePath (parent);
		final int count = l.size ();
		final int[] indices = new int [count];
		final Task[] removed = new Task [count];
		
		int i=0;
		for (final Task t : l) {
			indices [i] = parent.childIndex (t);
			removed[i] = t;
			i++;
		}
		for (final Task t : l) {
			parent.remove (parent.childIndex (t));
		}
		fireTasksRemoved (this, parentPath, indices, removed);
	}
	

	public void moveTasksTo (final Task previousParent, final List<Task> l, final Task target, final int position) {
		final TaskTreePath targetPath = new TaskTreePath (target);
		for (final Task t : l) {
			if (targetPath.contains (t)) {
				throw new IllegalOperationException ("Cannot move a task into its subtree!");
			}
		}
		removeTasks (previousParent, l);
		final List<PieceOfWork> reverse = new ArrayList (l);
		Collections.reverse (reverse);
		for (final Task t : l) {
			insertNodeInto (t, target, position);
		}
	}
	
	public void movePiecesOfWorkTo (final Task previousTask, final List<PieceOfWork> l, final Task target, final int position) {
		removePiecesOfWork (previousTask, l);
		final List<PieceOfWork> reverse = new ArrayList (l);
		Collections.reverse (reverse);
		for (final PieceOfWork pow : l) {
			insertPieceOfWorkInto (pow, target, position);
		}
	}
	
	public void updateTask (final Task t, final String name) {
		t.setName (name);
		nodeChanged (t);
	}
	
	public void updatePieceOfWork (final PieceOfWork pow, final Date from, final Date to, final String description) {
		pow.setFrom (from);
		pow.setTo (to);
		pow.setDescription (description);
		
		pieceOfWorkChanged (pow);
	}
	
	/**
	 * Invoke this method after you've changed how node is to be
	 * represented in the tree.
	 */
	public void pieceOfWorkChanged (PieceOfWork pow) {
		if (pow != null) {
			
			if (listenerList!=null) {
				Task         task = pow.getTask ();

				if(task != null) {
					int        anIndex = task.pieceOfWorkIndex (pow);
					if(anIndex != -1) {
						int[]        cIndexs = new int[1];

						cIndexs[0] = anIndex;
						firePiecesOfWorkChanged (this, getPathToRoot (task),
						cIndexs, new PieceOfWork[] {pow});
					}
				}
			}
			if (addToAdvancingIfNeeded (pow)) {
				fireAdvancingInserted (this, new PieceOfWork[] {pow});
			} else if (eraseIfNeeded (pow, true)) {
				fireAdvancingRemoved (this, new PieceOfWork[] {pow});
			}
		}
	}
	
	/**
	 * Invoke this method after you've inserted some TreeNodes into
	 * node.  childIndices should be the index of the new elements and
	 * must be sorted in ascending order.
	 */
	public void piecesOfWorkWereInserted (Task node, int[] childIndices) {
		if(listenerList != null && node != null && childIndices != null
			&& childIndices.length > 0) {
			int               cCount = childIndices.length;
			PieceOfWork[]          newChildren = new PieceOfWork[cCount];
			
			for(int counter = 0; counter < cCount; counter++)
				newChildren[counter] = node.pieceOfWorkAt (childIndices[counter]);
			firePiecesOfWorkInserted (this, getPathToRoot (node), childIndices,
				newChildren);
		}
	}
	
	/**
	 * Invoke this method after you've removed some TreeNodes from
	 * node.  childIndices should be the index of the removed elements and
	 * must be sorted in ascending order. And removedChildren should be
	 * the array of the children objects that were removed.
	 */
	public void piecesOfWorkWereRemoved (Task node, int[] childIndices,
		PieceOfWork[] removedChildren) {
		if(node != null && childIndices != null) {
			firePiecesOfWorkRemoved (this, getPathToRoot (node), childIndices,
				removedChildren);
		}
	}

	public Set<PieceOfWork> getAdvancing () {
		return _advancingPOWs;
	}

	public void addWorkAdvanceModelListener (WorkAdvanceModelListener l) {
		listenerList.add (WorkAdvanceModelListener.class, l);
	}

	public void removeWorkAdvanceModelListener (WorkAdvanceModelListener l) {
		listenerList.remove (WorkAdvanceModelListener.class, l);
	}
	
	private final static PieceOfWork[] _voidPOWArray = new PieceOfWork [0];
	
	/**
	 * Cerca e inserisce nell'elenco eventuali avanzamenti in fase di progresso appartenenti al sottoalbero.
	 * 
	 * @param t la radice del sottoalbero da esaminare.
	 * @return i nuovi avanzamenti inseriti nell'elenco.
	 */
	private PieceOfWork[] discoverNewAdvancingProgresses (final Task t) {
		final List<PieceOfWork> discovered = new ArrayList ();
		final List<PieceOfWork> l = t.getSubtreeProgresses ();
		synchronized (_advancingPOWs) {
			for (final PieceOfWork pow : l) {
				if (addToAdvancingIfNeeded (pow)){
					discovered.add (pow);
				}
			}
		}
		return discovered.toArray (_voidPOWArray);
	}
	
	/**
	 * Valuta se &egrave; necessario inserire l'avanzmaneto nell'elenco di quelli in fase di progress.
	 * <P>
	 * L'avanzamento verrà inserito se risulta in progress e non &egrave; ancora presente nell'elenco;
	 *
	 */
	private boolean addToAdvancingIfNeeded (final PieceOfWork pow) {
		synchronized (_advancingPOWs) {
			if (pow.isEndOpened () && !_advancingPOWs.contains (pow)){
				_advancingPOWs.add (pow);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Cerca e rimuove dall'elenco eventuali avanzamenti in fase di progresso appartenenti al sottoalbero.
	 * 
	 * @param t la radice del sottoalbero da esaminare.
	 * @return gli avanzamenti rimossi nell'elenco.
	 */
	private PieceOfWork[] eraseOldAdvancingProgresses (final Task t) {
		final List<PieceOfWork> removed = new ArrayList ();
		final List<PieceOfWork> l = t.getSubtreeProgresses ();
		synchronized (_advancingPOWs) {
			for (final PieceOfWork pow : l) {
				if (l.contains (pow)) {
					if (eraseIfNeeded (pow, false)){
						removed.add (pow);
					}
				}
			}
		}
		return removed.toArray (_voidPOWArray);
	}
	
	/**
	 * Valuta se &egrave; necessario rimuovere l'avanzamento nell'elenco di quelli in progress.
	 * <P>
	 * Per essere rimosso, l'avanzamento dele essere gi&agrave;presente nell'elenco.
	 * Se <CODE>onlyIfClosed</CODE> value <CODE>true</CODE>, l'avanzamento deve anche avere la dat di fine impostata.
	 * 
	 */
	private boolean eraseIfNeeded (final PieceOfWork pow, final boolean onlyIfClosed) {
		if (!onlyIfClosed || !pow.isEndOpened ()) {
			return _advancingPOWs.remove (pow);
		}
		return false;
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
	protected void fireAdvancingInserted (Object source, PieceOfWork[] pow) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		WorkAdvanceModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==WorkAdvanceModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new WorkAdvanceModelEvent (source, pow, WorkAdvanceModelEvent.INSERT);
				}
				((WorkAdvanceModelListener)listeners[i+1]).elementsInserted (e);
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
	protected void fireAdvancingRemoved (Object source, PieceOfWork[] pow) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList ();
		WorkAdvanceModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
			if (listeners[i]==WorkAdvanceModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new WorkAdvanceModelEvent (source, pow, WorkAdvanceModelEvent.DELETE);
				}
				((WorkAdvanceModelListener)listeners[i+1]).elementsRemoved (e);
			}
		}
	}
	
}

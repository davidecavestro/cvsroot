/*
 * DefaultTaskTreeModel.java
 *
 * Created on 2 dicembre 2005, 20.49
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.common.util.IllegalOperationException;
import com.davidecavestro.common.util.NestedRuntimeException;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelEvent;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelListener;
import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelEvent;
import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelListener;
import com.davidecavestro.timekeeper.persistence.PersistenceManager;
import com.davidecavestro.timekeeper.persistence.Transaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.event.EventListenerList;

/**
 * Implementazione parziale dell'albero dei task di modello.
 *
 * <P>
 * Fornisce i metodi di manipolazione del modello, quali ad esempio, l'inserimento o rimozione deinodi,degli avanzamenti.
 * QUestimetodi provvedono alla notifica delle modifiche apportate tramite gli eventi di tipo <CODE>TaskTreeModelEvent</CODE> e <CODE>WorkAdvanceModelEvent</CODE>.
 * <P>
 *
 * <H3>Note per l'estensione</H3>
 *
 * <H3>Note per i manutentori</H3>
 * Sono sconsigliate le chiamate interne dirette ai metodi esposti (public). Per ogni metodo di interfaccia &egrave; disponibile
 * una controparte <CODE>private</CODE> con la stessa firma preceduta da un carattere di <TT>_</TT> (underscore).
 * Tali metodi sono richiamabili in modo sicuro, poich&egrave; la loro visibilit&agrave; ridotta ne garantisce l'affidabilit&agrave;.
 *
 * Questa restrizione &egrave; dovuta alla scelta di rendere questa classe estensibile, allo scopo di supportare 
 * "decorazioni" quali l'implementazione dell'undo/redo. Eventuali chiamate dirette ai metodi pubblici po&ograve; provocare la chiamata
 * involontaria di agginte come il supporto all'undo/redo. 
 *
 * Esempio: la classe che estende questo modello per aggiungere le funzionalit&agrave; di undo, presenter&agrave; override del tipo
 * del tipo 
 * <PRE>
 * &#x0040;Override
 * public void methodX () {
 *    super.methodX ();
 *
 *    //gestione undo
 *   ...
 *   super.inverse_methodX ();
 * }
 * </PRE>
 * Le chiamate ai metodi della classe che si estende, tipo <CODE>super.methodX ()</CODE> e <CODE>super.inverse_methodX ()</CODE> 
 * sono corrette, dato che una modifica di UNDO/REDO non deve apparire come nuova azione dello storico.
 * <BR>
 * Fin qui tutto bene.
 * <BR>
 * Il problema sorge nel caso in cui <CODE>inverse_methodX ()</CODE> nella classe superiore utilizzi a sua volta un altro metodo scavalcato per la gestione dell'undo.
 * <PRE>
 * public void inverse_methodX () {
 *    methodY ();
 * }
 * </PRE>
 * Se <CODE>inverse_methodX ()</CODE> &egrave; scavalcato per ottenere il supporto all'undo, si ottiene una chiamata di undo che si registra come azione "undoable". Ci&ograve; sarebbe errato.
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
				final PieceOfWork[] removed = discardObsoleteAdvancing (oldRoot);
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
			} else if (checkForDiscard (pow, true)) {
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
	private PieceOfWork[] discoverNewAdvancingProgresses (final List<Task> tasks) {
		final List<PieceOfWork> discovered = new ArrayList ();
		synchronized (_advancingPOWs) {
			for (final Task t : tasks) {
				final List<PieceOfWork> l = t.getSubtreeProgresses ();
				for (final PieceOfWork pow : l) {
					if (addToAdvancingIfNeeded (pow)){
						discovered.add (pow);
					}
				}
			}
		}
		return discovered.toArray (_voidPOWArray);
	}
	
	private PieceOfWork[] discoverNewAdvancingProgresses (final Task t) {
		final List<Task> l = new ArrayList<Task> ();
		l.add (t);
		return discoverNewAdvancingProgresses (l);
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
	 * Garantisce l'esecuzione del controllo su eventuali nuovi avanzamenti in corso, aggiunta alla cache e notifica.
	 */
	private void lookForNewAdvancing (final List<PieceOfWork> pows) {
		final List<PieceOfWork> added = new ArrayList<PieceOfWork> ();
		for (final PieceOfWork pow : pows) {
			if (addToAdvancingIfNeeded (pow)) {
				added.add (pow);
			}
		}
		
		if (!added.isEmpty ()) {
			fireAdvancingInserted (this, added.toArray (_voidPOWArray));
		}
	}
	
	/**
	 * Cerca e rimuove dall'elenco eventuali avanzamenti in fase di progresso appartenenti al sottoalbero.
	 *
	 * @param t la radice del sottoalbero da esaminare.
	 * @return gli avanzamenti rimossi nell'elenco.
	 */
	private PieceOfWork[] discardObsoleteAdvancing (final List<Task> l) {
		final List<PieceOfWork> removed = new ArrayList ();
		synchronized (_advancingPOWs) {
			for (final Task t : l) {
				final List<PieceOfWork> pows = t.getSubtreeProgresses ();
				for (final PieceOfWork pow : pows) {
					if (_advancingPOWs.contains (pow)) {
						if (checkForDiscard (pow, false)){
							removed.add (pow);
						}
					}
				}
			}
		}
		return removed.toArray (_voidPOWArray);
	}
	
	private PieceOfWork[] discardObsoleteAdvancing (final Task t) {
		final List<Task> l = new ArrayList<Task> ();
		l.add (t);
		return discardObsoleteAdvancing (l);
	}
	/**
	 * Rimuove, se obsoleto, l'avanzamento dall'elenco di quelli attualmente in fase di avanzamento.
	 * <P>
	 * Possono essere rimossi solamente glielementi effettivamente presenti nell'elenco.
	 * Se <CODE>onlyIfClosed</CODE> vale <CODE>true</CODE>, l'avanzamento deve anche avere la data di fine impostata.
	 *
	 */
	private boolean checkForDiscard (final PieceOfWork pow, final boolean onlyIfClosed) {
		if (!onlyIfClosed || !pow.isEndOpened ()) {
			return _advancingPOWs.remove (pow);
		}
		return false;
	}
	
	/**
	 * Garantisce l'esecuzione del controllo su eventuali avanzamenti obsoleti, rimozione dalla cache e notifica.
	 */
	private void lookForObsoleteAdvancing (final List<PieceOfWork> pows, final boolean onlyIfClosed) {
		final List<PieceOfWork> removed = new ArrayList<PieceOfWork> ();
		for (final PieceOfWork pow : pows) {
			if (checkForDiscard (pow, onlyIfClosed)) {
				removed.add (pow);
			}
		}
		
		if (!removed.isEmpty ()) {
			fireAdvancingRemoved (this, removed.toArray (_voidPOWArray));
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
	
	
	
	
	
	/*-------------------------------------------------------------
	 * INIZIO sezione contenente i metodi di supportoalla persistenza
	 --------------------------------------------------------------*/
	
	
	/**
	 * RIferimentoalla transazione fake, creata lazy.
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
	
	/*-------------------------------------------------------------
	 * FINE sezione contenente i metodi di supportoalla persistenza
	 --------------------------------------------------------------*/
	
	
	
	
	
	
	
	
	/*-------------------------------------------------------------
	 * INIZIO sezione contenente i metodi di modifica del modello
	 --------------------------------------------------------------*/
	
	
	/**
	 * Invoked this to insert newChild at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate
	 * event. This is the preferred way to add children as it will create
	 * the appropriate event.
	 * @see insertTasksInto
	 */
	public void insertNodeInto (Task newChild, Task parent, int index){
		final List<Task> l = new ArrayList<Task> ();
		l.add (newChild);
		_insertTasksInto (l, parent, index);
	}
	
	/**
	 * Metodo da invocare per inserire un elenco di task figli.
	 * Utilizza il metodo insertTasksInto_TX per l'esecuzione a basso livello dell'azione.
	 *
	 * @param l i nuovi task figli.
	 * @param parent il task superiore.
	 * @param index l'indice da cui partire. Usare <CODE>-1</CODE> per accodare.
	 * @see insertTasksInto_TX
	 */
	public void insertTasksInto (final List<Task> l, final Task parent, int index) {
		_insertTasksInto (l, parent, index);
	}
	
	/**
	 * Versione privata del metodo, garantisce che nessuno lo possa scavalcare, pertantorisulta per l'implementazione dell'undo.
	 * @see insertTasksInto(final List<Task> l, final Task parent, int index)
	 */
	private void _insertTasksInto (final List<Task> l, final Task parent, int index) {
		_insertTasksInto (l, parent, index, true);
	}
	
	/**
	 * Implementazione con opzione per l'alterazione dellostato di persistenza.
	 * <P>
	 * In caso di utilizzo composito (move implica rimozione e successivo reinserimento) pu&ograve; tornare utile non variare lo stato di persistenza degli oggetti.
	 */
	private void _insertTasksInto (final List<Task> l, final Task parent, int index, final boolean alterPersistence) {
		final List<PieceOfWork> reverse = new ArrayList (l);
		Collections.reverse (reverse);
		
		if (index < 0) {
			index = parent.childCount ();
		}
		
		final int[] newIndexs = insertTasksInto_TX (l, parent, index, alterPersistence);
		
		final PieceOfWork[] discovered = discoverNewAdvancingProgresses (l);
		nodesWereInserted (parent, newIndexs);
		if (discovered.length>0) {
			fireAdvancingInserted (this, discovered);
		}
		
	}
	
	/**
	 * Azione a basso livello di inserimento dei task figli.
	 * <P>
	 * Non si occupa della notifica di alcun evento. Contiene esclusivamente il blocco dicodice interessato dalla transazione.
	 *
	 * @see insertTasksInto
	 */
	private int[] insertTasksInto_TX (final List<Task> l, final Task parent, int index, final boolean alterPersistence) {
		int[] newIndexs = new int[l.size ()];
		
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			int i = 0;
			for (final Task newChild : l) {
				newIndexs[i] = parent.insert (newChild, index++);
				i++;
			}
			if (alterPersistence) {
				getPersistenceManager ().makePersistentAll (l);
			}
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new NestedRuntimeException (e);
		}
		
		return newIndexs;
	}
	
	/**
	 * Invoked this to insert newPOW at location index in parents children.
	 * This will then message nodesWereInserted to create the appropriate
	 * event. This is the preferred way to add children as it will create
	 * the appropriate event.
	 */
	public void insertPieceOfWorkInto (PieceOfWork newPOW, Task t, int index){
		final List<PieceOfWork> l = new ArrayList<PieceOfWork> ();
		l.add (newPOW);
		_insertPiecesOfWorkInto (l, t, index);
	}
	
	
	/**
	 * Inserisce nuovi avanzamenti.
	 */
	public void insertPiecesOfWorkInto (final List<PieceOfWork> l, final Task t, int index) {
		_insertPiecesOfWorkInto (l, t, index);
	}
	/**
	 * Versione privata del metodo, garantisce che nessuno lo possa scavalcare, pertantorisulta per l'implementazione dell'undo.
	 * @see insertPiecesOfWorkInto(List<final PieceOfWork> l, final Task t, int index)
	 */
	private void _insertPiecesOfWorkInto (final List<PieceOfWork> l, final Task t, int index) {
		_insertPiecesOfWorkInto (l, t, index, true);
	}
	
	/**
	 * Implementazione con opzione per l'alterazione dellostato di persistenza.
	 * <P>
	 * In caso di utilizzo composito (move implica rimozione e successivo reinserimento) pu&ograve; tornare utile non variare lo stato di persistenza degli oggetti.
	 */
	private void _insertPiecesOfWorkInto (final List<PieceOfWork> l, final Task t, int index, final boolean alterPersistence) {
		final List<PieceOfWork> reverse = new ArrayList (l);
		Collections.reverse (reverse);
		
		final int[] newIndexs = insertPiecesOfWorkInto_TX (l, t, index, alterPersistence);
		
		if (index < 0){
			index = t.pieceOfWorkCount ();
		}
		
		piecesOfWorkWereInserted (t, newIndexs);
		lookForNewAdvancing (l);
		
	}
	
	/**
	 * Azione a basso livello di inserimento nuovi avanzamenti.
	 * <P>
	 * Non si occupa della notifica di alcun evento. Contiene esclusivamente la porzione di codice protetta dalla transazione..
	 *
	 * @see insertPiecesOfWorkInto
	 */
	private int[] insertPiecesOfWorkInto_TX (final List<PieceOfWork> l, final Task t, int index, final boolean alterPersistence) {
		int[] newIndexs = new int[l.size ()];
		int i = 0;
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			for (final PieceOfWork newPOW : l) {
				newIndexs[i] = t.insert (newPOW, index++);
				i++;
			}
			if (alterPersistence) {
				getPersistenceManager ().makePersistentAll (l);
			}
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new NestedRuntimeException (e);
		}
		
		return newIndexs;
	}
	
	/**
	 * Invocare questo metodo per rimuovere un avanzmaneto.
	 * Questo metodo si occupa di notificare la rimozione tramite .
	 */
	public void removePieceOfWork (PieceOfWork pow){
		final List<PieceOfWork> l = new ArrayList<PieceOfWork> ();
		l.add (pow);
		_removePiecesOfWork (pow.getTask (), l);
	}
	
	
	
	/**
	 * Rimuove avanzamenti fratelli.
	 *
	 * @param t il rask che contiene gli avazamenti da rimuovere.
	 * @param l gli avanzamenti da rimuovere.
	 */
	public void removePiecesOfWork (final Task t, final List<PieceOfWork> l) {
		_removePiecesOfWork (t, l);
	}
	/**
	 * Versione privata del metodo, garantisce che nessuno lo possa scavalcare, pertantorisulta per l'implementazione dell'undo.
	 * @see removePiecesOfWork(final Task t, final List<PieceOfWork> l)
	 */
	private void _removePiecesOfWork (final Task t, final List<PieceOfWork> l) {
		_removePiecesOfWork (t, l, true);
	}
	
	
	/**
	 * Implementazione con opzione per l'alterazione dellostato di persistenza.
	 * <P>
	 * In caso di utilizzo composito (move implica rimozione e successivo reinserimento) pu&ograve; tornare utile non variare lo stato di persistenza degli oggetti.
	 */
	private void _removePiecesOfWork (final Task t, final List<PieceOfWork> l, final boolean alterPersistence) {
		final int count = l.size ();
		final int[] indices = new int [count];
		final PieceOfWork[] removed = new PieceOfWork [count];
		
		int i=0;
		for (final PieceOfWork p : l) {
			indices [i] = t.pieceOfWorkIndex (p);
			removed[i] = p;
			i++;
		}
		removePiecesOfWork_TX (t, l, alterPersistence);
		firePiecesOfWorkRemoved (this, new TaskTreePath (t), indices, removed);
		lookForObsoleteAdvancing (l, false);
	}
	
	/**
	 * Azione a basso livello di rimozione avanzamenti.
	 * <P>
	 * Non si occupa della notifica di alcun evento. Contiene esclusivamente la porzione di codice protetta dalla transazione..
	 *
	 * @see removePiecesOfWork
	 */
	private void removePiecesOfWork_TX (final Task t, final List<PieceOfWork> l, final boolean alterPersistence) {
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			for (final PieceOfWork p : l) {
				t.removePieceOfWork (p);
			}
			if (alterPersistence) {
				getPersistenceManager ().deletePersistentAll (l);
			}
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new NestedRuntimeException (e);
		}
	}
	
	/**
	 * Message this to remove node from its parent. This will message
	 * nodesWereRemoved to create the appropriate event. This is the
	 * preferred way to remove a node as it handles the event creation
	 * for you.
	 */
	public void removeNodeFromParent (Task node) {
		final List<Task> l = new ArrayList<Task> ();
		l.add (node);
		_removeTasks (node.getParent (), l);
	}
	
	/**
	 * Rimuove task fratelli.
	 *
	 * @param parent il padre dei task da rimuovere.
	 * @param l i taskda rimuovere.
	 */
	public void removeTasks (final Task parent, final List<Task> l) {
		_removeTasks (parent, l);
	}
	/**
	 * Versione privata del metodo, garantisce che nessuno lo possa scavalcare, pertantorisulta per l'implementazione dell'undo.
	 * @see removeTasks(final Task parent, final List<Task> l)
	 */
	private void _removeTasks (final Task parent, final List<Task> l) {
		_removeTasks (parent, l, true);
	}
	
	/**
	 * Implementazione con opzione per l'alterazione dellostato di persistenza.
	 * <P>
	 * In caso di utilizzo composito (move implica rimozione e successivo reinserimento) pu&ograve; tornare utile non variare lo stato di persistenza degli oggetti.
	 */
	private void _removeTasks (final Task parent, final List<Task> l, final boolean alterPersistence) {
		final TaskTreePath parentPath = new TaskTreePath (parent);
		final int count = l.size ();
		final int[] indices = new int [count];
		final Task[] removed = new Task [count];
		
		final PieceOfWork[] discardedAdvancing = discardObsoleteAdvancing (parent);
		
		int i=0;
		for (final Task t : l) {
			indices [i] = parent.childIndex (t);
			removed[i] = t;
			i++;
		}
		
		i = 0;
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			for (final Task t : l) {
				parent.remove (indices[i]);
				i++;
			}
			if (alterPersistence) {
				deleteSubtreePersistent (getPersistenceManager (), l);
			}
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new NestedRuntimeException (e);
		}
		
		fireTasksRemoved (this, parentPath, indices, removed);
		
		
		if (discardedAdvancing.length>0) {
			fireAdvancingRemoved (this, discardedAdvancing);
		}
		
	}
	
	
	public void moveTasksTo (final Task previousParent, final List<Task> l, final Task target, final int position) {
		final TaskTreePath targetPath = new TaskTreePath (target);
		for (final Task t : l) {
			if (targetPath.contains (t)) {
				throw new IllegalOperationException ("Cannot move a task into its subtree!");
			}
		}
		final List<PieceOfWork> reverse = new ArrayList (l);
		Collections.reverse (reverse);
		
		_removeTasks (previousParent, l, false);
		
		_insertTasksInto (l, target, position, false);
	}
	
	public void movePiecesOfWorkTo (final Task previousTask, final List<PieceOfWork> l, final Task target, final int position) {
		_removePiecesOfWork (previousTask, l, false);
		_insertPiecesOfWorkInto (l, target, position, false);
	}
	
	public void updateTask (final Task t, final String name) {
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			t.setName (name);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new NestedRuntimeException (e);
		}
		
		nodeChanged (t);
	}
	
	public void updatePieceOfWork (final PieceOfWork pow, final Date from, final Date to, final String description) {
		final Transaction tx = getTransaction ();
		tx.begin ();
		try {
			pow.setFrom (from);
			pow.setTo (to);
			pow.setDescription (description);
			
			tx.commit ();
		} catch (final Exception e) {
			tx.rollback ();
			throw new NestedRuntimeException (e);
		}
		
		pieceOfWorkChanged (pow);
	}
	
	/*-------------------------------------------------------------
	 * FINE sezione contenente i metodi di modifica del modello
	 --------------------------------------------------------------*/
	
	
}

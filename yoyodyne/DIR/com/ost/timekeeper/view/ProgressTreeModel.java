/*
 * ProgressTreeModel.java
 *
 * Created on 25 aprile 2004, 10.02
 */

package com.ost.timekeeper.view;

import java.util.*;

import javax.swing.event.*;
import javax.swing.tree.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;

/**
 *
 * @author  davide
 */
public class ProgressTreeModel extends AbstractTreeModel implements Observer{
	
	private ProgressItem root;
	
	/** Crea una nuova istanza di ProgressTreeModel */
	public ProgressTreeModel(ProgressItem root) {
		load (root);
	}
	
	/** Crea una nuova istanza di ProgressTreeModel */
	public ProgressTreeModel(Project project) {
		load (project);
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
			//c'и una radice
			this.fireTreeStructureChanged(new TreeModelEvent (this, this.getPathToRoot(this.root)));
		} else if (oldRoot!=null){
			//la radice non cми, ma ce n'era una prima
			this.fireTreeStructureChanged(new TreeModelEvent (this, new Object[]{new ProgressItem ()}));
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
		fireTreeNodesInserted(new TreeModelEvent(this, getPathToRoot(parent),
		new int[]{index}, new Object[]{newChild}));
	}
	
	public void removeNodeFromParent(ProgressItem toRemove){
		final ProgressItem parent = toRemove.getParent();
		int[] childIndex = new int[]{parent.childIndex(toRemove)};
		fireTreeNodesRemoved(new TreeModelEvent(this, getPathToRoot(parent),
		childIndex, new Object[]{toRemove}));
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
			}
		}
	}
}

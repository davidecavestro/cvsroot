/*
 * ProgressItemNode.java
 *
 * Created on 25 aprile 2004, 10.05
 */

package com.ost.timekeeper.view;

import javax.swing.tree.*;

import com.ost.timekeeper.model.*;

/**
 *
 * @author  davide
 */
public class ProgressItemNode extends DefaultMutableTreeNode {
	
	/** Creates a new instance of ProgressItemNode */
	public ProgressItemNode(ProgressItem progressItem) {
		super (progressItem);
	}
	
	public void setUserObject(Object object) {
		super.setUserObject(object);
	}
	
	public boolean getAllowsChildren() {
		boolean retValue;
		
		retValue = super.getAllowsChildren();
		return retValue;
	}
	
	public void insert(MutableTreeNode child, int index) {
		ProgressItem current = getProgressItem ();
		current.insert(((ProgressItemNode)child).getProgressItem (), index);
		super.insert(child, index);
	}
	
	public void remove(MutableTreeNode node) {
		ProgressItem current = getProgressItem ();
		current.remove(((ProgressItemNode)node).getProgressItem ());
		super.remove(node);
	}
	
//	public void remove(int index) {
//		ProgressItem current = getProgressItem ();
//		current.remove(index);
//		super.remove(index);
//	}
	
	public void setParent(MutableTreeNode newParent) {
		super.setParent(newParent);
	}
	
	public void removeFromParent() {
		ProgressItem current = getProgressItem ();
		current.getParent().remove(current);
		super.removeFromParent();
	}
	
	public final ProgressItem getProgressItem (){
		return (ProgressItem)this.getUserObject();
	}
}

/*
 * ProgressTreeModel.java
 *
 * Created on 25 aprile 2004, 10.02
 */

package com.ost.timekeeper.view;

import java.util.*;
import javax.swing.tree.*;

import com.ost.timekeeper.model.*;

/**
 *
 * @author  davide
 */
public class ProgressTreeModel extends DefaultTreeModel {
	
	/** Creates a new instance of ProgressTreeModel */
	public ProgressTreeModel(Project project) {
		super (new ProgressItemNode (project.getRoot()));
	}
	
	/**
	 * Ritorna gli avanzamenti apparteneneti ad un sottoalbero.
	 * @param root la radice del sottoalbero.
	 * @return gli avanzamenti apparteneneti al sottoalbero con radice 
	 * <code>root</code>.
	 */	
	public List getProgressesFromRoot (ProgressItemNode root){
		ProgressItem item = root.getProgressItem();
		return item.getSubtreeProgresses();
	}
}

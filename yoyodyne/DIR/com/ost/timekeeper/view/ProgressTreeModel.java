/*
 * ProgressTreeModel.java
 *
 * Created on 25 aprile 2004, 10.02
 */

package com.ost.timekeeper.view;

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
	
}

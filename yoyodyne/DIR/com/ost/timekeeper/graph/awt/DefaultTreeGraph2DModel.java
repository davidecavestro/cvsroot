/*
 * DefaultTreeGraph2DModel.java
 *
 * Created on 28 febbraio 2005, 21.41
 */

package com.ost.timekeeper.graph.awt;

import JSci.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
* The DefaultTreeGraph2DModel class provides a default implementation
* of the TreeGraph2DModel interface.
 * @author  davide
 */
public class DefaultTreeGraph2DModel  extends AbstractGraphModel implements TreeGraph2DModel {
	
	private SerieNode _root;
	
	public DefaultTreeGraph2DModel (SerieNode root){
		this._root=root;
	}
	public com.ost.timekeeper.graph.awt.SerieNode getRoot () {
		return this._root;
	}
	
	public void setRoot (com.ost.timekeeper.graph.awt.SerieNode root) {
		this._root = root;
		fireGraphDataChanged ();
	}
}

/*
 * DefaultSerieNode.java
 *
 * Created on 1 marzo 2005, 0.41
 */

package com.ost.timekeeper.graph.awt;

/**
 *
 * @author  davide
 */
public class DefaultSerieNode implements SerieNode {
	
	private String _name;
	private double _value;
	private SerieNode[] _children;
	private final static SerieNode[] noChildren = new SerieNode[0];
	public DefaultSerieNode (String name, double value) {
		this._name=name;
		this._value=value;
		this._children=noChildren;
	}
	/** Creates a new instance of DefaultSerieNode */
	public DefaultSerieNode (String name, double value, SerieNode[] children) {
		this._name=name;
		this._value=value;
		this._children=children;
	}
	
	public com.ost.timekeeper.graph.awt.SerieNode childAt (int param) {
		return this._children[param];
	}
	
	public int childrenLength () {
		return this._children.length;
	}
	
	private double _childrenValue=-1;
	public double getChildrenValue () {
		if (0>_childrenValue) {
			_childrenValue = 0;
			for (int i=0;i< this._children.length;i++){
				_childrenValue+=this._children[i].getTotalValue ();
			}
		}
		return _childrenValue;
	}
	
	public String getName () {
		return this._name;
	}
	
	public double getValue () {
		return this._value;
	}
	
	private double _totalValue=-1;
	public double getTotalValue () {
		if (0>_totalValue) {
			this._totalValue =this._value+this.getChildrenValue ();
		}
		return _totalValue;
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questonodo.
	 *
	 * @return na stringa che rappresenta questo nodo.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("name: ").append (this._name)
		.append (" value: ").append (this.getValue ())
		.append (" childrenValue: ").append (this.getChildrenValue ())
		.append (" childrenLength: ").append (this.childrenLength ());
		return sb.toString ();
	}
}

/*
 * Project.java
 *
 * Created on 18 aprile 2004, 14.25
 */

package com.ost.timekeeper.model;

/**
 *
 * @author  davide
 */
public class Project {
	
	private ProgressItem root;
	private String name;
	
	/** Costruttore vuoto*/
	public Project() {
	}
	
	/** Creates a new instance of Project */
	public Project(String name, ProgressItem root) {
		this.name = name;
		this.root = root;
	}
	
	public String getName (){
		return this.name;
	}
	
	public void setName (String name){
		this.name = name;
	}
	
	public ProgressItem getRoot (){
		return this.root;
	}
	
	public String toString (){
		StringBuffer sb = new StringBuffer ();
		sb.append ("name: ").append (this.name)
		.append (" root: ").append (this.root);
		return sb.toString();
	}
	
	/** Setter for property root.
	 * @param root New value of property root.
	 *
	 */
	public void setRoot(ProgressItem root) {
		this.root=root;
	}
	
}

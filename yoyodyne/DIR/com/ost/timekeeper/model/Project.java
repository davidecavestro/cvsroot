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
	
	/** Creates a new instance of Project */
	public Project(String name, ProgressItem root) {
		this.name = name;
		this.root = root;
	}
	
	public String getName (){
		return this.name;
	}
	
	public ProgressItem getRoot (){
		return this.root;
	}
}

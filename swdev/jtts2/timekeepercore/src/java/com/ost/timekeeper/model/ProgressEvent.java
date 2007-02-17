/*
 * PRogressEvent.java
 *
 * Created on 17 aprile 2004, 14.52
 */

package com.ost.timekeeper.model;

import java.util.*;

/**
 *
 * @author  davide
 */
public class ProgressEvent extends EventObject {
	public final static int PE_PROGRESS_START = 0;
	public final static int PE_PROGRESS_STOP = 1;
	public final static int PE_PROGRESS_ADD = 2;
	public final static int PE_PROGRESS_REMOVE = 3;
	public final static int PE_NODE_ADD = 4;
	public final static int PE_NODE_REMOVE = 5;
	
	private int id;
	
	/** Creates a new instance of ProgressEvent */
	public ProgressEvent(ProgressItem source, int id) {
		super (source);
		this.id=id;
	}
	
}

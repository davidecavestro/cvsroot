/*
 * NestedRuntimeException.java
 *
 * Created on 23 maggio 2004, 19.09
 */

package com.ost.timekeeper.util;

/**
 *
 * @author  davide
 */
public class NestedRuntimeException extends java.lang.RuntimeException {
	
	private Throwable rootCause;
	/**
	 * Creates a new instance of <code>NestedRuntimeException</code> without detail message.
	 */
	public NestedRuntimeException() {
	}
	
	
	/**
	 * Constructs an instance of <code>NestedRuntimeException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public NestedRuntimeException(String msg) {
		super(msg);
	}
	
	
	/**
	 * Creates a new instance of <code>NestedRuntimeException</code> without detail message.
	 */
	public NestedRuntimeException(Throwable rootCause) {
		super ();
		this.rootCause = rootCause;
	}
	
	public Throwable getRootCause (){
		return this.rootCause;
	}
	
	public String toString (){
		return new StringBuffer (super.toString ()).append(" rootCause: "+this.rootCause).toString ();
	}
}

/*
 * ExceptionUtils.java
 *
 * Created on 15 maggio 2004, 19.41
 */

package com.ost.timekeeper.util;

import java.io.*;

/**
 *
 * @author  davide
 */
public final class ExceptionUtils {
	
	/** Creates a new instance of ExceptionUtils */
	private ExceptionUtils() {
	}
	
	public static StringBuffer getStackStrace (Throwable t){
		StringBuffer sb = new StringBuffer ();
		StringWriter sw = new StringWriter ();
		t.printStackTrace(new PrintWriter (sw));
		return sw.getBuffer();
	}
}

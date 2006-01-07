/*
 * Matcher.java
 *
 * Created on 28 dicembre 2005, 23.07
 */

package com.davidecavestro.rbe.gui.search;

/**
 *
 * @author  davide
 */
public interface Matcher {
	boolean match (String s);
	void setPattern (String p);
	String getPattern ();
	void setHighlight (boolean v);
	boolean getHighlight ();
}

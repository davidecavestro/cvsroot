/*
 * SearchNavigator.java
 *
 * Created on 1 gennaio 2006, 9.58
 */

package com.davidecavestro.rbe.gui.search;

/**
 *
 * @author  davide
 */
public interface SearchNavigator {
	void nextMatch (Matcher m);
	void previousMatch (Matcher m);
}

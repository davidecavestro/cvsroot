/*
 * ApplicationSettings.java
 *
 * Created on 2 maggio 2004, 19.59
 */

package com.ost.timekeeper.conf;

import java.util.*;

import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public abstract class SettingsUtils {
	
	private final static String[] TRUE = new String[]{"TRUE", "true"};
	private final static String[] FALSE = new String[]{"FALSE", "false"};
	
	/** Creates a new instance of ApplicationSettings */
	protected SettingsUtils () {
	}
	
	public final Boolean getBoolean (String key){
		String value =this.getProperties ().getProperty (key);
		return new Boolean (value!=null && StringUtils.contains(TRUE, value));
	}
	
	public abstract Properties getProperties ();
}

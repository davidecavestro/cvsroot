/*
 * GlobalSettings.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper.conf;

/**
 *
 * @author  davide
 */
public final class GlobalSettings {
	
	private static GlobalSettings instance;
	
	/** Creates a new instance of GlobalSettings */
	private GlobalSettings() {
	}
	
	public GlobalSettings getInstance (){
		if (instance==null) {
			instance  = new GlobalSettings ();
		}
		return instance;
	}
}

/*
 * UserSettings.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper.conf;

/**
 *
 * @author  davide
 */
public class UserSettings {
	
	private static UserSettings instance;
	
	/** Creates a new instance of UserSettings */
	private UserSettings() {
	}
	
	public UserSettings getInstance (){
		if (instance==null) {
			instance  = new UserSettings ();
		}
		return instance;
	}
}

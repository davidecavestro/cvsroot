/*
 * DefaultSettings.java
 *
 * Created on 13 settembre 2004, 22.50
 */

package com.ost.timekeeper.conf;

import java.awt.*;

/**
 *
 * @author  davide
 */
public class DefaultSettings {
	
	private static DefaultSettings instance;
	
	/** Creates a new instance of DefaultSettings */
	private DefaultSettings() {
	}
	
	public DefaultSettings getInstance (){
		if (instance==null) {
			instance  = new DefaultSettings ();
		}
		return instance;
	}
	
	private final static Rectangle defaultBounds = new Rectangle (640, 480);
	public Rectangle getMainFormBounds (){
		return defaultBounds;
	}
}

/*
 * ResourceSupplier.java
 *
 * Created on 2 maggio 2004, 8.47
 */

package com.ost.timekeeper.util;

import java.util.*;
import javax.swing.*;

/**
 * Fornisce risorse quali stringhe localizzate, immagini.
 * @author  davide
 */
public final class ResourceSupplier {
	
	/** Creates a new instance of ResourceSupplier */
	private ResourceSupplier() {
	}
	
	public final static String getString (ResourceClass resClass, String bundle, String key){
		String bundlePath = "";
		if (resClass==ResourceClass.UI){
			bundlePath = "com/ost/timekeeper/ui/bundle/";
		}
		try {
			return java.util.ResourceBundle.getBundle(bundlePath+bundle).getString(key);
		} catch (MissingResourceException mre){
			return bundle+"_"+key;
		}
	} 
	
	public final static ImageIcon getImageIcon (ResourceClass resClass, String name){
		String iamgePath = "";
		if (resClass==ResourceClass.UI){
			iamgePath = "/com/ost/timekeeper/ui/images/";
		}
		return new javax.swing.ImageIcon(ResourceSupplier.class.getResource(iamgePath+name));
	}
}

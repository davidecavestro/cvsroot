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
 *
 * @author  davide
 */
public final class ResourceSupplier {
	private final static String BUNDLE_PATH		= "com/ost/timekeeper/ui/bundle/";
	private final static String IMAGE_PATH		= "/com/ost/timekeeper/ui/images/";
	
	/** 
	 * Costruttore vuoto. 
	 */
	private ResourceSupplier() {
	}
	
	/**
	 * Ritorna una risorsa di tipo stringa.
	 *
	 * @param resClass
	 * @param bundle
	 * @param key
	 * @return
	 */	
	public final static String getString (ResourceClass resClass, String bundle, String key){
		String bundlePath = "";
		if (resClass==ResourceClass.UI){
			bundlePath = BUNDLE_PATH;
		}
		try {
			return java.util.ResourceBundle.getBundle(bundlePath+bundle).getString(key);
		} catch (MissingResourceException mre){
			return bundle+"_"+key;
		}
	} 
	
	/**
	 * Ritorna un arisorsa di tipo icona.
	 *
	 * @param resClass
	 * @param name
	 * @return
	 */	
	public final static ImageIcon getImageIcon (ResourceClass resClass, String name){
		String iamgePath = "";
		if (resClass==ResourceClass.UI){
			iamgePath = IMAGE_PATH;
		}
		try {
			return new javax.swing.ImageIcon(ResourceSupplier.class.getResource(iamgePath+name));
		} catch (Exception e){
			return new javax.swing.ImageIcon ();
		}
	}
}

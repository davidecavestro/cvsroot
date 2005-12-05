/*
 * DefaultResourceBundleModel.java
 *
 * Created on 2 dicembre 2005, 20.49
 */

package com.davidecavestro.rbe.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Il modello di ResourceBundle.
 *
 * @author  davide
 */
public class DefaultResourceBundleModel extends AbstractResourceBundleModel {
	
	private final Map _resources = new HashMap ();
	private Locale[] _locales;
	private final Set _keys = new HashSet ();
	
	private final static LocalizationProperties[] voidResourceArray = new LocalizationProperties[0];
	/** Costruttore. */
	public DefaultResourceBundleModel (LocalizationProperties[] resources) {
		setBundles (resources);
	}

	public void setBundles (LocalizationProperties[] resources){
		cacheResources (resources);
		fireResourceBundleStructureChanged ();
	}
	
	public LocalizationProperties[] getResources (){
		return (LocalizationProperties[])this._resources.values ().toArray (voidResourceArray);
	}
	
	public java.util.Set getKeySet () {
		return this._keys;
	}
	
	public java.util.Locale[] getLocales () {
		return this._locales;
	}

	private void cacheResources (LocalizationProperties[] resources){
		mapResources (resources);
		cacheLocales (resources);
		cacheKeys (resources);
	}
	
	private void mapResources (LocalizationProperties[] resources){
		final Map map = this._resources;
		for (int i = 0; i < resources.length;i++){
			final LocalizationProperties properties = resources[i];
			map.put (properties.getLocale (), properties);
		}
		
	}
	
	private void cacheLocales (LocalizationProperties[] resources) {
		final Locale[] locales  = new Locale [resources.length];
		for (int i = 0 ; i <resources.length;i++){
			final LocalizationProperties properties = resources[i];
			locales[i] = properties.getLocale ();
		}
		
		this._locales = locales;
	}
	
	private void cacheKeys (LocalizationProperties[] resources) {
		this._keys.clear ();
		for (int i = 0 ; i <resources.length;i++){
			final LocalizationProperties properties = resources[i];
			this._keys.addAll (properties.getProperties ().keySet ());
		}
	}
	
	private LocalizationProperties getLocalizationProperties (Locale locale){
		return (LocalizationProperties)this._resources.get (locale);
	}
	
	public String getValue (Locale locale, String key) {
		return getLocalizationProperties (locale).getProperties ().getProperty (key);
	}
	
	public void setValue (Locale locale, String key, String value) {
		getLocalizationProperties (locale).getProperties ().setProperty (key, value);
		fireResourceBundleValueUpdated (locale, key);
	}
	
	public void removeKey (String key){
		for (int i = 0;i<this._locales.length;i++){
			final Locale locale = this._locales[i];
			getLocalizationProperties (locale).getProperties ().remove (key);
		}
		fireKeysDeleted (new String[]{key});
	}
	
	public void addKey (String key, String[] values){
		for (int i = 0;i<this._locales.length;i++){
			final Locale locale = this._locales[i];
			getLocalizationProperties (locale).getProperties ().setProperty (key, values[i]);
		}
		fireKeysDeleted (new String[]{key});
	}
}

/*
 * DefaultResourceBundleModel.java
 *
 * Created on 2 dicembre 2005, 20.49
 */

package com.davidecavestro.rbe.model;

import com.davidecavestro.common.util.NestedRuntimeException;
import com.davidecavestro.rbe.model.event.ResourceBundleModelEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Il modello di ResourceBundle.
 *
 * @author  davide
 */
public class DefaultResourceBundleModel extends AbstractResourceBundleModel {
	
	private final Map _resourceMap = new HashMap ();
	private Locale[] _locales;
	private LocalizationProperties[] _resources;
	private final Set _keys = new HashSet ();
	private final Set _localesSet = new HashSet ();
	
	private String _name;
	
    /**
     * If any <code>PropertyChangeListeners</code> have been registered,
     * the <code>changeSupport</code> field describes them.
     *
     * @serial
     * @since 1.2
     * @see #addPropertyChangeListener
     * @see #removePropertyChangeListener
     * @see #firePropertyChange
     */
    private java.beans.PropertyChangeSupport changeSupport;
	
	private final static LocalizationProperties[] voidResourceArray = new LocalizationProperties[0];
	/**
	 * Costruttore.
	 * @param name il nome.
	 * @param resources le risorse di localizzazione.
	 */
	public DefaultResourceBundleModel (String name, LocalizationProperties[] resources) {
		setName (name);
		setBundles (resources);
	}

	public void setName (String name){
		if (this._name!=name){
			final String old = this._name;
			this._name = name;
			firePropertyChange ("name", old, name);
		}
	}
	
	public void setBundles (LocalizationProperties[] resources){
		this._resources = resources;
		cacheResources (resources);
		fireResourceBundleStructureChanged ();
	}
	
	public LocalizationProperties[] getResources (){
		return this._resources;
		//return (LocalizationProperties[])this._resourceMap.values ().toArray (voidResourceArray);
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
		final Map map = this._resourceMap;
		map.clear ();
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
		this._localesSet.clear ();
		this._localesSet.addAll (Arrays.asList (this._locales));
		
	}
	
	private void cacheKeys (LocalizationProperties[] resources) {
		this._keys.clear ();
		for (int i = 0 ; i <resources.length;i++){
			final LocalizationProperties properties = resources[i];
			this._keys.addAll (properties.getProperties ().keySet ());
		}
	}
	
	private LocalizationProperties getLocalizationProperties (Locale locale){
		return (LocalizationProperties)this._resourceMap.get (locale);
	}
	
	public String getValue (Locale locale, String key) {
		return getLocalizationProperties (locale).getProperties ().getProperty (key);
	}
	
	public void setValue (Locale locale, String key, String value) {
		Properties props = getLocalizationProperties (locale).getProperties ();
		setModified (true);
		if (null==value){
			props.remove (key);
			if (getLocales (key).isEmpty ()){
				fireResourceBundleModelChanged (new ResourceBundleModelEvent (this, ResourceBundleModelEvent.ALL_LOCALES, new String[]{key}, ResourceBundleModelEvent.DELETE));
				return;
			}
		} else {
			props.setProperty (key, value);
		}
		fireResourceBundleValueUpdated (locale, key);
	}
	
	public void removeKey (String key){
		for (int i = 0;i<this._locales.length;i++){
			final Locale locale = this._locales[i];
			getLocalizationProperties (locale).getProperties ().remove (key);
		}
		this._keys.remove (key);
		setModified (true);
		fireKeysDeleted (new String[]{key});
	}
	
	/**
	 * Inserisce una entry per il Locale di default (LocalizationProeprties.DEFAULT).
	 *
	 * @param key la chiave.
	 * @param value il valore.
	 */	
	public void addKey (String key, String value){
		addKey (LocalizationProperties.DEFAULT, key, value);
	}
	
	public void addKey (Locale locale, String key, String value){
		getLocalizationProperties (locale).getProperties ().setProperty (key, value);
		this._keys.add (key);
		setModified (true);
		fireKeysInserted (new String[]{key});
	}
	
	public void addKey (String key, String[] values){
		for (int i = 0;i<values.length;i++){
			final Locale locale = this._locales[i];
			getLocalizationProperties (locale).getProperties ().setProperty (key, values[i]);
		}
		this._keys.add (key);
		setModified (true);
		fireKeysInserted (new String[]{key});
	}
	
	/**
	 * Aggiunge un Locale con le properties associate.
	 *
	 * @param resource le properties di locale.
	 *@see #setBundles
	 */	
	public void addLocale (LocalizationProperties resource){
		if (containsLocale (resource.getLocale ())){
			throw new IllegalArgumentException ("Duplicate locale");
		}
		final LocalizationProperties[] backup = this._resources;
		final int oldLength = backup.length;
		final int newLength = oldLength+1;
		final LocalizationProperties[] newResources = new LocalizationProperties [newLength];
		System.arraycopy (backup, 0, newResources, 0, oldLength);
		newResources [newLength-1] = resource;
		setModified (true);
		setBundles (newResources);
	}
	
	/**
	 * Rimuove un Locale con le properties associate.
	 *
	 * @param resource le properties di locale.
	 *@see #setBundles
	 */	
	public void removeLocale (Locale locale){
		if (locale==LocalizationProperties.DEFAULT){
			throw new IllegalArgumentException ("Cannot remove DEFAULT locale");
		}
		final LocalizationProperties[] backup = this._resources;
		final int oldLength = backup.length;
		final int newLength = oldLength-1;
		final LocalizationProperties[] newResources = new LocalizationProperties [newLength];

		for (int i=0;i<newLength;i++){
			final LocalizationProperties lp = backup[i];
			if (lp.getLocale ().equals (locale)){
				if (i<newLength){
					System.arraycopy (backup, i+1, newResources, i, oldLength-i-1);
				}
				break;
			} else {
				newResources[i] = lp;
			}
		}
		setModified (true);
		setBundles (newResources);
	}
	
	public String getName () {
		return this._name;
	}
	
	public String toString (){
		return getName ();
	}
	
	public void load (File file){
		final String fileName = file.getName ();		
		setName (fileName.substring (0, fileName.lastIndexOf (".properties")));
		setPath (file.getParentFile ());
		setBundles (buildResources (file));
	}

	private File _path;
	/**
	 * Imposta la directory di salvataggio dei file di properties.
	 *
	 * @param path la directory che contiene i file di properties.
	 */	
	public void setPath (File path){
		if (this._path!=path){
			final File old = this._path;
			this._path = path;
			firePropertyChange ("path", old, path);
		}
	}
	
	/**
	 * Ritorna la directory di salvataggio delle properties.
	 */
	public File getPath (){
		return this._path;
	}

	/**
	 * Prepara e ritorna le risorse (Properties) individuate a partire da un file.
	 * Utilizza un algoritmo di ricerca analogo a quello implmentato in ResourceBundle.
	 *
	 * @param file il file base. Dovrebbe essere un Properties file (estensione .properties).
	 * @return tutte le properties associate al file specificato, secondo la logica di ResourceBundle.
	 */	
	private LocalizationProperties[] buildResources (File file){
		/*
		 * Individua il nome del file senza estensione, per la ricerca.
		 */
		final String fileName = file.getName ();
		int extensionIdx = fileName.indexOf ('.');
		final String baseName = extensionIdx>=0?fileName.substring (0, extensionIdx):fileName;
		final File parentDirectory = file.getParentFile ();
		final File[] properties = parentDirectory.listFiles (new FilenameFilter (){
			public boolean accept(File dir, String name){
				return name.startsWith (baseName) && name.endsWith (".properties");
			}
		});
		boolean defaultFound = false;
		final List retValue = new ArrayList ();
		for (int i=0; i<properties.length;i++){
			final File f = properties[i];
			try {
				int idx = f.getName ().indexOf (".properties");
				Locale l = getBundleLocale (baseName, f.getName ().substring (0, idx));
				if (!defaultFound && l == LocalizationProperties.DEFAULT){
					defaultFound = true;
				}
				final Properties p = new Properties ();
				p.load (new FileInputStream (f));
				retValue.add (new LocalizationProperties (l, p));
			} catch (IllegalArgumentException iae){
				/* caso gestito */
			} catch (FileNotFoundException fnfe){
				throw new NestedRuntimeException (fnfe);
			} catch (IOException ioe){
				throw new NestedRuntimeException (ioe);
			}
		}
		if (!defaultFound){
			/*
			 * Aggiunge Locale di default
			 */
			retValue.add (0, new LocalizationProperties (LocalizationProperties.DEFAULT, new Properties ()));
		}
		return (LocalizationProperties[])retValue.toArray (voidResourceArray);
		
	}
	
	/**
	 * Ritorna il Locale del bundle specificato. (Codice estratto da Resourcebundle);
	 * @param baseName the bundle's base name
	 * @param bundleName the complete bundle name including locale
	 * extension.
	 * @return
	 */
    private Locale getBundleLocale (String baseName, String bundleName) {
		if (baseName.length () == bundleName.length ()) {
			return LocalizationProperties.DEFAULT;
		} else if (baseName.length () < bundleName.length ()) {
			int pos = baseName.length ();
			String temp = bundleName.substring (pos + 1);
			pos = temp.indexOf ('_');
			if (pos == -1) {
				return new Locale (temp, "", "");
			}
			
			String language = temp.substring (0, pos);
			temp = temp.substring (pos + 1);
			pos = temp.indexOf ('_');
			if (pos == -1) {
				return new Locale (language, temp, "");
			}
			
			String country = temp.substring (0, pos);
			temp = temp.substring (pos + 1);
			
			return new Locale (language, country, temp);
		} else {
			//The base name is longer than the bundle name.  Something is very wrong
			//with the calling code.
			throw new IllegalArgumentException ();
		}
		
	}
	
	
	/**
	 * Rende persistente lo stato del modello.
	 * @param comment un commento.
	 */
	public void saveAs (File file, String comment) throws FileNotFoundException, IOException{
		setName (file.getName ());
		setPath (file.getParentFile ());
		store (comment);
	}
	
	/**
	 * Rende persistente lo stato del modello.
	 * @param comment un commento.
	 */
	public void store (String comment) throws FileNotFoundException, IOException{
		for (int i =0;i<this._resources.length;i++){
			final LocalizationProperties lp = this._resources[i];
			final Properties p = lp.getProperties ();
			final Locale l = lp.getLocale ();
			final String language = l.getLanguage ();
			final String country = l.getCountry ();
			final String variant = l.getVariant ();
			final StringBuffer fileName = new StringBuffer (this.getName ());
			
			if (language!=null&&language.length ()>0){
				fileName.append ('_').append (language);
			}
			if (country!=null && country.length ()>0){
				fileName.append ('_').append (country);
			}
			if (variant!=null && variant.length ()>0){
				fileName.append ('_').append (variant);
			}
			fileName.append (".properties");
			lp.store (new File (_path.getPath (), fileName.toString ()), comment);
		}
		setModified (false);
	}	
	
	/**
	 * Adds a PropertyChangeListener to the listener list. The listener is
	 * registered for all bound properties of this class, including the
	 * following:
	 * <ul>
	 *    <li>il 'name' di questo modello
	 *    <li>il 'path' di questo modello
	 * </ul>
	 * <p>
	 * If listener is null, no exception is thrown and no action is performed.
	 *
	 * @param    listener  the PropertyChangeListener to be added
	 *
	 * @see #removePropertyChangeListener
	 * @see #getPropertyChangeListeners
	 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public synchronized void addPropertyChangeListener (
	PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new java.beans.PropertyChangeSupport (this);
		}
		changeSupport.addPropertyChangeListener (listener);
	}
	
	/**
	 * Removes a PropertyChangeListener from the listener list. This method
	 * should be used to remove PropertyChangeListeners that were registered
	 * for all bound properties of this class.
	 * <p>
	 * If listener is null, no exception is thrown and no action is performed.
	 *
	 * @param listener the PropertyChangeListener to be removed
	 *
	 * @see #addPropertyChangeListener
	 * @see #getPropertyChangeListeners
	 * @see #removePropertyChangeListener(java.lang.String,java.beans.PropertyChangeListener)
	 */
	public synchronized void removePropertyChangeListener (
	PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener (listener);
	}
	
	/**
	 * Returns an array of all the property change listeners
	 * registered on this component.
	 *
	 * @return all of this component's <code>PropertyChangeListener</code>s
	 *         or an empty array if no property change
	 *         listeners are currently registered
	 *
	 * @see      #addPropertyChangeListener
	 * @see      #removePropertyChangeListener
	 * @see      #getPropertyChangeListeners(java.lang.String)
	 * @see      java.beans.PropertyChangeSupport#getPropertyChangeListeners
	 * @since    1.4
	 */
	public synchronized PropertyChangeListener[] getPropertyChangeListeners () {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners ();
	}
	
	/**
	 * Adds a PropertyChangeListener to the listener list for a specific
	 * property. The specified property may be user-defined, or one of the
	 * following:
	 * <ul>
	 *    <li>il 'name' di questo modello
	 *    <li>il 'path' di questo modello
	 * </ul>
	 * Note that if this Component is inheriting a bound property, then no
	 * event will be fired in response to a change in the inherited property.
	 * <p>
	 * If listener is null, no exception is thrown and no action is performed.
	 *
	 * @param propertyName one of the property names listed above
	 * @param listener the PropertyChangeListener to be added
	 *
	 * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 * @see #getPropertyChangeListeners(java.lang.String)
	 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public synchronized void addPropertyChangeListener (
	String propertyName,
	PropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (changeSupport == null) {
			changeSupport = new java.beans.PropertyChangeSupport (this);
		}
		changeSupport.addPropertyChangeListener (propertyName, listener);
	}
	
	/**
	 * Removes a PropertyChangeListener from the listener list for a specific
	 * property. This method should be used to remove PropertyChangeListeners
	 * that were registered for a specific bound property.
	 * <p>
	 * If listener is null, no exception is thrown and no action is performed.
	 *
	 * @param propertyName a valid property name
	 * @param listener the PropertyChangeListener to be removed
	 *
	 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 * @see #getPropertyChangeListeners(java.lang.String)
	 * @see #removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public synchronized void removePropertyChangeListener (
	String propertyName,
	PropertyChangeListener listener) {
		if (listener == null || changeSupport == null) {
			return;
		}
		changeSupport.removePropertyChangeListener (propertyName, listener);
	}
	
	/**
	 * Returns an array of all the listeners which have been associated
	 * with the named property.
	 *
	 * @return all of the <code>PropertyChangeListeners</code> associated with
	 *         the named property or an empty array if no listeners have
	 *         been added
	 *
	 * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 * @see #getPropertyChangeListeners
	 * @since 1.4
	 */
	public synchronized PropertyChangeListener[] getPropertyChangeListeners (
	String propertyName) {
		if (changeSupport == null) {
			return new PropertyChangeListener[0];
		}
		return changeSupport.getPropertyChangeListeners (propertyName);
	}
	
	/**
	 * Support for reporting bound property changes for Object properties.
	 * This method can be called when a bound property has changed and it will
	 * send the appropriate PropertyChangeEvent to any registered
	 * PropertyChangeListeners.
	 *
	 * @param propertyName the property whose value has changed
	 * @param oldValue the property's previous value
	 * @param newValue the property's new value
	 */
	protected void firePropertyChange (String propertyName,
	Object oldValue, Object newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
	/**
	 * Support for reporting bound property changes for boolean properties.
	 * This method can be called when a bound property has changed and it will
	 * send the appropriate PropertyChangeEvent to any registered
	 * PropertyChangeListeners.
	 *
	 * @param propertyName the property whose value has changed
	 * @param oldValue the property's previous value
	 * @param newValue the property's new value
	 */
	protected void firePropertyChange (String propertyName,
	boolean oldValue, boolean newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
	/**
	 * Support for reporting bound property changes for integer properties.
	 * This method can be called when a bound property has changed and it will
	 * send the appropriate PropertyChangeEvent to any registered
	 * PropertyChangeListeners.
	 *
	 * @param propertyName the property whose value has changed
	 * @param oldValue the property's previous value
	 * @param newValue the property's new value
	 */
	protected void firePropertyChange (String propertyName,
	int oldValue, int newValue) {
		java.beans.PropertyChangeSupport changeSupport = this.changeSupport;
		if (changeSupport == null) {
			return;
		}
		changeSupport.firePropertyChange (propertyName, oldValue, newValue);
	}
	
	public boolean containsLocale (Locale l){
		return this._localesSet.contains (l);
	}
	
	public Set getLocaleKeys (Locale l){
		return getLocalizationProperties (l).getProperties ().keySet ();
	}
	
	/**
	 * RItorna il set di Locale che contengono la chiave specificata.
	 *
	 * @param key la chiave.
	 * @return il set di Locale che contengono la chiave specificata.
	 */	
	public Set getLocales (String key){
		final Set s = new HashSet ();
		for (int i = 0; i< this._resources.length;i++){
			LocalizationProperties lp = this._resources[i];
			if (lp.getProperties ().keySet ().contains (key)){
				s.add (lp.getLocale ());
			}
		}
		return s;
	}
	
	private boolean _isModified = false;
	/**
	 * Imposta lo stato di "modificato" al valore specificato..
	 * Se lo stato viene variato, notifica i PropertyChangeListener della modifica alla property "isModified".
	 *
	 * @param modified lo stato di "modificato".

	 */	
	private void setModified (boolean modified){
		if (this._isModified!=modified){
			this._isModified = modified;
			firePropertyChange ("isModified", !modified, modified);
		}
	}
	
	/**
	 * Ritorna <TT>true</TT> se ci sono modifiche pendenti non salvate.
	 *
	 * @return <TT>true</TT> se ci sono modifiche pendenti non salvate.
	 */	
	public boolean isModified (){
		return this._isModified;
	}
	
}

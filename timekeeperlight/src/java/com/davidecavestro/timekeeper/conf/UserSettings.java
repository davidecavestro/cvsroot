/*
 * UserSettings.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.davidecavestro.timekeeper.conf;

import com.davidecavestro.common.util.settings.SettingsSupport;
import com.davidecavestro.timekeeper.Application;
import java.io.File;

/**
 * Le impostazioni personalizzate dell'utente.
 *
 * @author  davide
 */
public final class UserSettings extends AbstractSettings {
	
	/**
	 * header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("_***_USER_SETTINGS_***_");


	private final UserResources  _userResources;
	
	/**
	 * Costruttore.
	 * @param application l'applicazione.
	 */
	public UserSettings (final Application application, final UserResources  userResources ) {
		super (application);
		this._userResources = userResources;
	}
	
	/**
	 * Ritorna il percorso del file di properties.
	 *
	 * @return il percorso del file di properties.
	 */	
	public String getPropertiesFileName () {
		return new File (_userResources.getUserApplicationSettingsDirPath (), ResourceNames.USER_SETTINGSFILE_NAME).getPath ();
	}

	public String getPropertiesHeader () {
		return PROPERTIES_HEADER;
	}

	public String getLastProjectName () {
		return SettingsSupport.getStringProperty (this.getProperties (), PROPNAME_LASTPROJECTNAME);
	}

}

/*
 * ApplicationContext.java
 *
 * Created on 21 dicembre 2005, 20.48
 */

package com.davidecavestro.rbe;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.common.gui.persistence.UIPersister;
import com.davidecavestro.rbe.conf.UserResources;
import com.davidecavestro.rbe.conf.UserSettings;
import com.davidecavestro.rbe.gui.WindowManager;
import com.davidecavestro.rbe.model.DefaultResourceBundleModel;

/**
 *
 * @author  davide
 */
public class ApplicationContext {
	
	private final WindowManager _windowManager;
	private final UIPersister _uiPersister;
//	private final Logger _logger;
	private final UserSettings _userSettings;
	private final ApplicationData _applicationData;
//	private final ApplicationEnvironment _env;
	private final DefaultResourceBundleModel _resourceBundleModel;
	
	/** Creates a new instance of ApplicationCOntext */
	public ApplicationContext (
	WindowManager windowManager,
	UIPersister uiPersister,
//	private final Logger _logger;
	UserSettings userSettings,
	ApplicationData applicationData,
//	private final ApplicationEnvironment _env;
	DefaultResourceBundleModel resourceBundleModel
	) {
		this._applicationData = applicationData;
		this._windowManager = windowManager;
		this._userSettings = userSettings;
		this._uiPersister = uiPersister;
		this._resourceBundleModel = resourceBundleModel;
	}
	
	public WindowManager getWindowManager (){
		return this._windowManager;
	}
	
	public UIPersister getUIPersisteer (){
		return this._uiPersister;
	}
	
	public UserSettings getUserSettings (){
		return this._userSettings;
	}
	
	public DefaultResourceBundleModel getModel (){
		return this._resourceBundleModel;
	}
	
}

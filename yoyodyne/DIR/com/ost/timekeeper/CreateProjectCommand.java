/*
 * CreateProjectCommand.java
 *
 * Created on 18 aprile 2004, 14.30
 */

package com.ost.timekeeper;

import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.*;

/**
 *
 * @author  davide
 */
public class CreateProjectCommand extends Command {
	
	private Application application;
	/** Creates a new instance of CreateProjectCommand */
	public CreateProjectCommand(Application a) {
		this.application = a;
	}
	
	public void execute() {
		String projectName = askUserForProjectName ();
		execute (projectName);
	}
	
	public void execute (String projectName){
		application.setProject (new Project (projectName, new ProgressItem (projectName)));
	}
	
	public String askUserForProjectName (){
		return StringInputDialog.createDialog(application.getMainForm (), "Ask user", "Enter new project name", true);
	}
	
}

/*
 * Application.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper;

import com.ost.timekeeper.ui.*;

/**
 *
 * @author  davide
 */
public class Application {
	
	private MainForm mainForm;
	/** Creates a new instance of Application */
	public Application(MainForm mainForm) {
		this.mainForm=mainForm;
	}
	
	public MainForm getMainForm (){
		return this.mainForm;
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		Application a = new Application (new MainForm());
		a.getMainForm().show();
	}
}

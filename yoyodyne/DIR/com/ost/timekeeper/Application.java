/*
 * Application.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper;

import javax.swing.*;

import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;

/**
 *
 * @author  davide
 */
public class Application {
	
	private CreateProjectCommand createProjectCommand = new CreateProjectCommand(this);
	
	private MainForm mainForm;
	/** Creates a new instance of Application */
	private Application() {
		this.project = new Project("Void", new ProgressItem("Void"));
	}
	
	private static Application instance = null;
	public static Application getInstance(){
		if (instance == null){
			instance = new Application();
			instance.mainForm = new MainForm(instance);
		}
		return instance;
	}
	
	public MainForm getMainForm(){
		return this.mainForm;
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		Application a = getInstance();
		try{
			a.getMainForm().show();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(a.getMainForm(),
			ex.toString(), "Warning",
			JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private Project project;
	
	public void setProject(Project project){
		this.project = project;
	}
	
	public Project getProject(){
		return this.project;
	}
	
	private ProgressItem currentItem;
	
	public void setCurrentItem(ProgressItem current){
		this.currentItem = current;
		this.getProgressStopAction().setEnabled(current!=null);
		this.getProgressStartAction().setEnabled(current==null);
	}
	
	public ProgressItem getCurrentItem(){
		return this.currentItem;
	}
	
	private ProgressItem selectedItem;
	
	public void setSelectedItem(ProgressItem selected){
		this.selectedItem = selected;
		this.getProgressStartAction().setEnabled(this.currentItem==null);
		this.getNodeCreateAction().setEnabled(this.selectedItem!=null);
		this.getNodeDeleteAction().setEnabled(this.selectedItem!=null);
	}
	
	public ProgressItem getSelectedItem(){
		return this.selectedItem;
	}
	
	private ProgressStartAction progressStartAction = new ProgressStartAction();
	public ProgressStartAction getProgressStartAction(){
		return this.progressStartAction;
	}
	
	private ProgressStopAction progressStopAction = new ProgressStopAction();
	public ProgressStopAction getProgressStopAction(){
		return this.progressStopAction;
	}
	
	private NodeCreateAction nodeCreateAction = new NodeCreateAction();
	public NodeCreateAction getNodeCreateAction(){
		return this.nodeCreateAction;
	}
	
	private NodeDeleteAction nodeDeleteAction = new NodeDeleteAction();
	public NodeDeleteAction getNodeDeleteAction(){
		return this.nodeDeleteAction;
	}
}

/*
 * Application.java
 *
 * Created on 18 aprile 2004, 12.08
 */

package com.ost.timekeeper;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.jdo.*;
import javax.swing.*;

import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.persistence.*;
import com.ost.timekeeper.view.*;
import com.ost.timekeeper.ui.*;
import com.ost.timekeeper.util.*;

/**
 *
 * @author  davide
 */
public class Application extends Observable{
	
	private MainForm mainForm;
	/** Creates a new instance of Application */
	private Application() {
		initPersistence ();
		this.addObserver(this.nodeCreateAction);
		this.addObserver(this.nodeDeleteAction);
		this.addObserver(this.progressStartAction);
		this.addObserver(this.progressStopAction);
		this.addObserver(this.projectCreateAction);
		this.addObserver(this.projectDeleteAction);
		this.addObserver(this.projectOpenAction);
		this.addObserver(this.projectSaveAction);
		this.addObserver(this.projectXMLExportAction);
		this.addObserver(this.projectXMLImportAction);
	}
	
	private static Application instance = null;
	private javax.swing.Timer timer;
	public static Application getInstance(){
		if (instance == null){
			instance = new Application();
			instance.mainForm = new MainForm(instance);
			instance.addObserver (instance.mainForm);
			ActionListener timerActionPerformer = new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					instance.setChanged ();
					instance.notifyObservers ("currentitem");
				}
			};
			instance.timer = new javax.swing.Timer (1000, timerActionPerformer);
			instance.timer.stop();
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
		a.getProjectCreateAction ().execute ("Void sample");
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
		if (project!=null){
			this.setSelectedItem (project.getRoot());
		} else {
			this.setSelectedItem (null);
		}
//		this.getProjectCreateAction().setEnabled(true);
//		this.getProjectDeleteAction().setEnabled(project!=null);
		this.setChanged();
		this.notifyObservers("project");
	}
	
	public Project getProject(){
		return this.project;
	}
	
	private ProgressItem currentItem;
	
	public void setCurrentItem(ProgressItem current){
		this.currentItem = current;
		this.setChanged();
		this.notifyObservers("currentitem");
//		this.getProgressStopAction().setEnabled(current!=null);
//		this.getProgressStartAction().setEnabled(current==null);
		if (current!=null){
			instance.timer.start();
		} else {
			instance.timer.stop();
		}
	}
	
	public ProgressItem getCurrentItem(){
		return this.currentItem;
	}
	
	private ProgressItem selectedItem;
	
	public void setSelectedItem(ProgressItem selected){
		this.selectedItem = selected;
		this.setChanged();
		this.notifyObservers("selecteditem");
		this.setChanged();
		this.notifyObservers("currentitem");
//		this.getProgressStartAction().setEnabled(this.currentItem==null);
//		this.getNodeCreateAction().setEnabled(this.selectedItem!=null);
//		this.getNodeDeleteAction().setEnabled(this.selectedItem!=null);
	}
	
	public ProgressItem getSelectedItem(){
		return this.selectedItem;
	}
	
	private final ProgressStartAction progressStartAction = new ProgressStartAction();
	public ProgressStartAction getProgressStartAction(){
		return this.progressStartAction;
	}
	
	private final ProgressStopAction progressStopAction = new ProgressStopAction();
	public ProgressStopAction getProgressStopAction(){
		return this.progressStopAction;
	}
	
	private final NodeCreateAction nodeCreateAction = new NodeCreateAction();
	public NodeCreateAction getNodeCreateAction(){
		return this.nodeCreateAction;
	}
	
	private final NodeDeleteAction nodeDeleteAction = new NodeDeleteAction();
	public NodeDeleteAction getNodeDeleteAction(){
		return this.nodeDeleteAction;
	}
	
	private final ProjectCreateAction projectCreateAction = new ProjectCreateAction();
	public ProjectCreateAction getProjectCreateAction(){
		return this.projectCreateAction;
	}
	
	private ProjectDeleteAction projectDeleteAction = new ProjectDeleteAction();
	public ProjectDeleteAction getProjectDeleteAction(){
		return this.projectDeleteAction;
	}
	
	private final ProjectOpenAction projectOpenAction = new ProjectOpenAction();
	public ProjectOpenAction getProjectOpenAction(){
		return this.projectOpenAction;
	}
	
	private final ProjectSaveAction projectSaveAction = new ProjectSaveAction();
	public ProjectSaveAction getProjectSaveAction(){
		return this.projectSaveAction;
	}
	
	private final ProjectXMLExportAction projectXMLExportAction = new ProjectXMLExportAction();
	public ProjectXMLExportAction getProjectXMLExportAction(){
		return this.projectXMLExportAction;
	}
	
	private final ProjectXMLImportAction projectXMLImportAction = new ProjectXMLImportAction();
	public ProjectXMLImportAction getProjectXMLImportAction(){
		return this.projectXMLImportAction;
	}
	
	private PersistenceManager pm;
	private void initPersistence (){
		Properties properties = DataStoreUtil.getDataStoreProperties ();
		PersistenceManagerFactory pmf =
					  JDOHelper.getPersistenceManagerFactory(properties);
		this.pm = pmf.getPersistenceManager();
		this.pm.currentTransaction().begin();
	}
	public final PersistenceManager getPersistenceManager (){
		return this.pm;
	}
	
	public void flushData (){
		Transaction tx = this.pm.currentTransaction();
		try {
			tx.commit();
			tx.begin();
		} catch (Exception e){
			System.out.println (ExceptionUtils.getStackStrace (e).toString ());
			e.printStackTrace();
			tx.rollback();
		}
	}
}

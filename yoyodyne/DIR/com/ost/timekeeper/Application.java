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
 * Il controller centrale dell'applicazione.
 *
 * @author  davide
 */
public class Application extends Observable{
	
	/**
	 * La finestra principale della GUI.
	 */
	private MainForm mainForm;
	/** 
	 * Costruttore privato. 
	 */
	private Application() {
		//inizializza il supporto per la persistenza dei dati
		initPersistence ();
		//registra le azioni su Application
		this.addObserver(this.nodeCreateAction);
		this.addObserver(this.nodeDeleteAction);
		this.addObserver(this.progressStartAction);
		this.addObserver(this.progressStopAction);
		this.addObserver(this.projectCreateAction);
		this.addObserver(this.projectDeleteAction);
		this.addObserver(this.projectCloseAction);
		this.addObserver(this.projectOpenAction);
		this.addObserver(this.projectSaveAction);
		this.addObserver(this.projectXMLExportAction);
		this.addObserver(this.projectXMLImportAction);
	}
	
	/**
	 * L'istanza di questa applicazione (singleton).
	 */
	private static Application instance = null;
	/**
	 * Il timer lù per la notifica degli avanzamenti.
	 */
	private javax.swing.Timer timer;
	
	/** 
	 * Ritorna l'istanza di questa applicazione.
	 * @return l'istanza di questa applicazione.
	 */
	public static Application getInstance(){
		if (instance == null){
			//istanziazione lazy
			instance = new Application();
			
			//istanzia la finestra principale dell'interfaccia
			instance.mainForm = new MainForm(instance);
			
			//registra la finestra principale su Application per le notoifiche
			instance.addObserver (instance.mainForm);
			
			//prepara il timer per la notifica del progressing dell'avanzamento
			ActionListener timerActionPerformer = new ActionListener (){
				/**
				 * Gestisce evento timer.
				 */
				public void actionPerformed (ActionEvent ae){
					instance.setChanged ();
					//notifica l'avanzamento
					instance.notifyObservers (ObserverCodes.ITEMPROGRESSING);
				}
			};
			instance.timer = new javax.swing.Timer (1000, timerActionPerformer);
			instance.timer.stop();
		}
		return instance; 
	}
	
	/** 
	 * Ritorna la finestra principale della GUI per questa applicazione.
	 * @return la finestra principale della GUI per questa applicazione.
	 */
	public MainForm getMainForm(){
		return this.mainForm;
	}
	
	/**
	 * Metodo di lancio.
	 * @param args gli argomenti della linea di comando.
	 */
	public static void main(String args[]) {
		Application a = getInstance();
//		a.getProjectCreateAction ().execute ("Void project");
		a.getProjectCloseAction ().execute ();
		try{
			a.getMainForm().setBounds(0, 0, 800, 600);
			a.getMainForm().show();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(a.getMainForm(),
			ex.toString(), "Warning",
			JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * Il progetto corrente.
	 */
	private Project project;
	
	/**
	 * Imposta il progetto corrente.
	 * @param project il progetto corrente.
	 */	
	public void setProject(Project project){
		this.project = project;
		if (project!=null){
			this.setSelectedItem (project.getRoot());
		} else {
			this.setSelectedItem (null);
		}
		//notifica cambiamento di progetto
		this.setChanged();
		this.notifyObservers(ObserverCodes.PROJECT);
	}
	
	
	/**
	 * Ritorna il progetto corrente.
	 * @return il progetto corrente.
	 */	
	public Project getProject(){
		return this.project;
	}
	
	/**
	 * L'elemento corrente.
	 */
	private ProgressItem currentItem;
	
	/**
	 * Imposta l'elemento corrente.
	 * @param current l'elemento corrente.
	 */	
	public void setCurrentItem(ProgressItem current){
		this.currentItem = current;
		//notifica cambiamento elemento corrente
		this.setChanged();
		this.notifyObservers(ObserverCodes.CURRENTITEM);
		
		/*
		 * Gestione timer notifica avanzamento.
		 */
		if (current!=null){
			//avvia timer
			instance.timer.start();
		} else {
			//arresta timer
			instance.timer.stop();
		}
	}
	
	/**
	 * Ritorna l'elemento corrente.
	 * @return l'elemento corrente.
	 */	
	public ProgressItem getCurrentItem(){
		return this.currentItem;
	}
	
	/**
	 * L'elemento selezionato.
	 */
	private ProgressItem selectedItem;
	
	/**
	 * Imposta l'elemento selezionato.
	 * @param selected l'elemento selezionato.
	 */	
	public void setSelectedItem(ProgressItem selected){
		this.selectedItem = selected;
		//notifica variazione elemento selezionato
		this.setChanged();
		this.notifyObservers(ObserverCodes.SELECTEDITEM);
	}
	
	/**
	 * Ritorna l'elemento selezionato.
	 * @return l'elemento selezionato.
	 */	
	public ProgressItem getSelectedItem(){
		return this.selectedItem;
	}
	
	/**
	 * L'avanzamento selezionato.
	 */
	private Period selectedProgress;
	
	/**
	 * Imposta l'avanzamento selezionato.
	 * @param selected l'avanzamento selezionato.
	 */	
	public void setSelectedProgress(Period selected){
		this.selectedProgress = selected;
		//notifica variazione selezione elemento
		this.setChanged();
		this.notifyObservers(ObserverCodes.SELECTEDPROGRESS);
	}
	
	/**
	 * Ritorna l'acanzamento selezionato.
	 * @return l'acanzamento selezionato.
	 */	
	public Period getSelectedProgress(){
		return this.selectedProgress;
	}

	/**
	 * Azione di partenza avanzamento.
	 */
	private final ProgressStartAction progressStartAction = new ProgressStartAction();
	
	/**
	 * Ritorna l'azione di partenza avanzamento.
	 * @return l'azione di partenza avanzamento.
	 */	
	public ProgressStartAction getProgressStartAction(){
		return this.progressStartAction;
	}
	
	/**
	 * Azione di arresto avanzamento.
	 */
	private final ProgressStopAction progressStopAction = new ProgressStopAction();
	/**
	 * Ritorna l'azione di arresto avanzamento.
	 * @return l'azione di arresto avanzamento.
	 */	
	public ProgressStopAction getProgressStopAction(){
		return this.progressStopAction;
	}
	
	/**
	 * Azione di creazione nuovo elemento.
	 */
	private final NodeCreateAction nodeCreateAction = new NodeCreateAction();
	/**
	 * Ritorna l'azione di creazione nuovo elemento.
	 * @return l'azione di creazione nuovo elemento.
	 */	
	public NodeCreateAction getNodeCreateAction(){
		return this.nodeCreateAction;
	}
	
	/**
	 * L'azione di rimozione nuovo elemento.
	 */	
	private final NodeDeleteAction nodeDeleteAction = new NodeDeleteAction();
	/**
	 * Ritorna l'azione di rimozione nuovo elemento.
	 * @return l'azione di rimozione nuovo elemento.
	 */	
	public NodeDeleteAction getNodeDeleteAction(){
		return this.nodeDeleteAction;
	}
	
	/**
	 * L'azione di creazione nuovo progetto.
	 */	
	private final ProjectCreateAction projectCreateAction = new ProjectCreateAction();
	/**
	 * Ritorna l'azione di creazione nuovo progetto.
	 * @return l'azione di creazione nuovo progetto.
	 */	
	public ProjectCreateAction getProjectCreateAction(){
		return this.projectCreateAction;
	}
	
	/**
	 * L'azione di rimozione progetto.
	 */	
	private ProjectDeleteAction projectDeleteAction = new ProjectDeleteAction();
	/**
	 * Ritorna l'azione di rimozione progetto.
	 * @return l'azione di rimozione progetto.
	 */	
	public ProjectDeleteAction getProjectDeleteAction(){
		return this.projectDeleteAction;
	}
	
	/**
	 * L'azione di apertura progetto.
	 */	
	private final ProjectOpenAction projectOpenAction = new ProjectOpenAction();
	/**
	 * Ritorna l'azione di apertura progetto.
	 * @return l'azione di apertura progetto.
	 */	
	public ProjectOpenAction getProjectOpenAction(){
		return this.projectOpenAction;
	}
	
	/**
	 * L'azione di salvataggio progetto.
	 */	
	private final ProjectSaveAction projectSaveAction = new ProjectSaveAction();
	/**
	 * Ritorna l'azione di salvataggio progetto.
	 * @return l'azione di salvataggio progetto.
	 */	
	public ProjectSaveAction getProjectSaveAction(){
		return this.projectSaveAction;
	}
	
	/**
	 * L'azione di chiusura progetto.
	 */	
	private final ProjectCloseAction projectCloseAction = new ProjectCloseAction();
	/**
	 * Ritorna l'azione di chiusura progetto.
	 * @return l'azione di chiusura progetto.
	 */	
	public ProjectCloseAction getProjectCloseAction(){
		return this.projectCloseAction;
	}
	
	/**
	 * L'azione di esportazione progetto da fonte XML.
	 */	
	private final ProjectXMLExportAction projectXMLExportAction = new ProjectXMLExportAction();
	/**
	 * Ritorna l'azione di esportazione progetto da fonte XML.
	 * @return l'azione di esportazione progetto da fonte XML.
	 */	
	public ProjectXMLExportAction getProjectXMLExportAction(){
		return this.projectXMLExportAction;
	}
	
	/**
	 * L'azione di importazione progetto da fonte XML.
	 */	
	private final ProjectXMLImportAction projectXMLImportAction = new ProjectXMLImportAction();
	/**
	 * Ritorna l'azione di importazione progetto da fonte XML.
	 * @return l'azione di importazione progetto da fonte XML.
	 */	
	public ProjectXMLImportAction getProjectXMLImportAction(){
		return this.projectXMLImportAction;
	}
	
	/**
	 * Il gestore della persistenza deidati.
	 */
	private PersistenceManager pm;
	/**
	 * Inizializza la gestione della persistenza dei dati.
	 */
	private void initPersistence (){
		Properties properties = DataStoreUtil.getDataStoreProperties ();
		PersistenceManagerFactory pmf =
					  JDOHelper.getPersistenceManagerFactory(properties);
		this.pm = pmf.getPersistenceManager();
		this.pm.currentTransaction().begin();
	}
	/**
	 * Ritorna il gestore della persistenza dei dati.
	 * @return il gestore della persistenza dei dati.
	 */	
	public final PersistenceManager getPersistenceManager (){
		return this.pm;
	}
	
	/**
	 * Forza la persistenza delle modifiche.
	 */
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
	
	/**
	 * Ritorna i progetti disponibili.
	 * @return i progetti disponibili.
	 */	
	public List getAvailableProjects (){
		List retValue = new ArrayList ();
		for (Iterator it = getPersistenceManager().getExtent(Project.class, true).iterator();it.hasNext ();){
			retValue.add (it.next());
		}
		return retValue;
	}
	
    /**
     * Marca questo <tt>Observable</tt> come modificato; il metodo
     * <tt>hasChanged</tt> ora ritornerà <tt>true</tt>.
     */
    public final synchronized void setChanged() {
		super.setChanged ();
    }	
}

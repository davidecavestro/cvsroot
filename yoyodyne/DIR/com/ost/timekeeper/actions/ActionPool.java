/*
 * ActionPool.java
 *
 * Created on 25 ottobre 2004, 23.25
 */

package com.ost.timekeeper.actions;

import java.util.*;

/**
 * Pool di azioni.
 *
 * @author  davide
 */
public final class ActionPool extends Observable{
	
	/**
	 * L'istanza del singleton.
	 */
	private static ActionPool _instance;
	
	/** 
	 * Costruttore vuoto. 
	 */
	private ActionPool() {
	}
	
	/**
	 * Ritorna l'istanza di questo pool.
	 * Se il pool non è ancora stato referenziato, allora 
	 * l'istanza viene inizializzata.
	 *
	 * @return l'istanza di questo pool.
	 */	
	public static ActionPool getInstance (){
		if (_instance==null){
			/*
			 * Istanziazione lazy.
			 */
			_instance = new ActionPool ();
		}
		return _instance;
	}
	
	/**
	 * Azione di partenza avanzamento.
	 */
	private ProgressStartAction progressStartAction;
	/**
	 * Azione di arresto avanzamento.
	 */
	private ProgressStopAction progressStopAction;
	/**
	 * Azione di creazione nuovo elemento.
	 */
	private NodeCreateAction nodeCreateAction;
	/**
	 * L'azione di rimozione nuovo elemento.
	 */	
	private NodeDeleteAction nodeDeleteAction;
	/**
	 * L'azione di creazione nuovo progetto.
	 */	
	private ProjectCreateAction projectCreateAction;
	/**
	 * L'azione di rimozione progetto.
	 */	
	private ProjectDeleteAction projectDeleteAction;
	/**
	 * L'azione di apertura progetto.
	 */	
	private ProjectOpenAction projectOpenAction;
	/**
	 * L'azione di salvataggio progetto.
	 */	
	private ProjectSaveAction projectSaveAction;
	/**
	 * L'azione di chiusura progetto.
	 */	
	private ProjectCloseAction projectCloseAction;
	/**
	 * L'azione di esportazione progetto da fonte XML.
	 */	
	private ProjectXMLExportAction projectXMLExportAction;
	/**
	 * L'azione di importazione progetto da fonte XML.
	 */	
	private ProjectXMLImportAction projectXMLImportAction;
	
	/**
	 * L'azione di importazione progetto da fonte XML.
	 */	
	private ProgressItemUpdateAction progressItemUpdateAction;
	/**
	 * Ritorna l'azione di modifica nodo di avanzamento.
	 * @return l'azione di modifica nodo di avanzamento.
	 */	
	public ProgressStartAction getProgressStartAction(){
		if (this.progressStartAction==null){
			//istanziazione lazy
			this.progressStartAction = new ProgressStartAction ();
		}
		return this.progressStartAction;
	}
	
	/**
	 * Ritorna l'azione di arresto avanzamento.
	 * @return l'azione di arresto avanzamento.
	 */	
	public ProgressStopAction getProgressStopAction(){
		if (this.progressStopAction==null){
			//istanziazione lazy
			this.progressStopAction = new ProgressStopAction ();
		}
		return this.progressStopAction;
	}
	
	/**
	 * Ritorna l'azione di creazione nuovo elemento.
	 * @return l'azione di creazione nuovo elemento.
	 */	
	public NodeCreateAction getNodeCreateAction(){
		if (this.nodeCreateAction==null){
			//istanziazione lazy
			this.nodeCreateAction = new NodeCreateAction ();
		}
		return this.nodeCreateAction;
	}
	
	/**
	 * Ritorna l'azione di rimozione nuovo elemento.
	 * @return l'azione di rimozione nuovo elemento.
	 */	
	public NodeDeleteAction getNodeDeleteAction(){
		if (this.nodeDeleteAction==null){
			//istanziazione lazy
			this.nodeDeleteAction = new NodeDeleteAction ();
		}
		return this.nodeDeleteAction;
	}
	
	/**
	 * Ritorna l'azione di creazione nuovo progetto.
	 * @return l'azione di creazione nuovo progetto.
	 */	
	public ProjectCreateAction getProjectCreateAction(){
		if (this.projectCreateAction==null){
			//istanziazione lazy
			this.projectCreateAction = new ProjectCreateAction ();
		}
		return this.projectCreateAction;
	}
	
	/**
	 * Ritorna l'azione di rimozione progetto.
	 * @return l'azione di rimozione progetto.
	 */	
	public ProjectDeleteAction getProjectDeleteAction(){
		if (this.projectDeleteAction==null){
			//istanziazione lazy
			this.projectDeleteAction = new ProjectDeleteAction ();
		}
		return this.projectDeleteAction;
	}
	
	/**
	 * Ritorna l'azione di apertura progetto.
	 * @return l'azione di apertura progetto.
	 */	
	public ProjectOpenAction getProjectOpenAction(){
		if (this.projectOpenAction==null){
			//istanziazione lazy
			this.projectOpenAction = new ProjectOpenAction ();
		}
		return this.projectOpenAction;
	}
	
	/**
	 * Ritorna l'azione di salvataggio progetto.
	 * @return l'azione di salvataggio progetto.
	 */	
	public ProjectSaveAction getProjectSaveAction(){
		if (this.projectSaveAction==null){
			//istanziazione lazy
			this.projectSaveAction = new ProjectSaveAction ();
		}
		return this.projectSaveAction;
	}
	
	/**
	 * Ritorna l'azione di chiusura progetto.
	 * @return l'azione di chiusura progetto.
	 */	
	public ProjectCloseAction getProjectCloseAction(){
		if (this.projectCloseAction==null){
			//istanziazione lazy
			this.projectCloseAction = new ProjectCloseAction ();
		}
		return this.projectCloseAction;
	}
	
	/**
	 * Ritorna l'azione di esportazione progetto da fonte XML.
	 * @return l'azione di esportazione progetto da fonte XML.
	 */	
	public ProjectXMLExportAction getProjectXMLExportAction(){
		if (this.projectXMLExportAction==null){
			//istanziazione lazy
			this.projectXMLExportAction = new ProjectXMLExportAction ();
		}
		return this.projectXMLExportAction;
	}
	
	/**
	 * Ritorna l'azione di importazione progetto da fonte XML.
	 * @return l'azione di importazione progetto da fonte XML.
	 */	
	public ProjectXMLImportAction getProjectXMLImportAction(){
		if (this.projectXMLImportAction==null){
			//istanziazione lazy
			this.projectXMLImportAction = new ProjectXMLImportAction ();
		}
		return this.projectXMLImportAction;
	}
		
	/**
	 * Ritorna l'azione di modifica nodi di avaznamento.
	 * @return l'azione di modifica nodi di avaznamento.
	 */	
	public ProgressItemUpdateAction getProgressItemUpdateAction(){
		if (this.progressItemUpdateAction==null){
			//istanziazione lazy
			this.progressItemUpdateAction = new ProgressItemUpdateAction ();
		}
		return this.progressItemUpdateAction;
	}
	
}

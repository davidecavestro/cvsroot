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
	private ActionPool () {
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
	 * Azione di partenza di un nuovo avanzamento.
	 */
	private ProgressStartAction progressStartAction;
	
	/**
	 * Ritorna l'azione di partenza di un nuovo avanzamento.
	 * @return l'azione di patenza di un nuovo avanzamento.
	 */
	public ProgressStartAction getProgressStartAction (){
		if (this.progressStartAction==null){
			//istanziazione lazy
			this.progressStartAction = new ProgressStartAction ();
		}
		return this.progressStartAction;
	}
	
	/**
	 * Azione di arresto avanzamento.
	 */
	private ProgressStopAction progressStopAction;
	
	/**
	 * Ritorna l'azione di arresto avanzamento.
	 * @return l'azione di arresto avanzamento.
	 */
	public ProgressStopAction getProgressStopAction (){
		if (this.progressStopAction==null){
			//istanziazione lazy
			this.progressStopAction = new ProgressStopAction ();
		}
		return this.progressStopAction;
	}
	
	/**
	 * Gestisce l'abilitazione del flusso di aggiornamento dello stato persistente di un periodo di avanzamento.
	 */
	private ProgressUpdateAction progressUpdateAction;
	
	/**
	 * Ritorna l'azione che gestisce l'abilitazione del flusso di aggiornamento dello stato persistente di un periodo di avanzamento.
	 * @return l'azione che gestisce l'abilitazione del flusso di aggiornamento dello stato persistente di un periodo di avanzamento.
	 */
	public ProgressUpdateAction getProgressUpdateAction (){
		if (this.progressUpdateAction==null){
			//istanziazione lazy
			this.progressUpdateAction = new ProgressUpdateAction ();
		}
		return this.progressUpdateAction;
	}
	
	/**
	 * Azione di creazione di un nuovo nodo di avanzamento.
	 */
	private NodeCreateAction nodeCreateAction;
	
	/**
	 * Ritorna l'azione di creazione nuovo nodo di avanzamento.
	 * @return l'azione di creazione nuovo nodo di avanzamento.
	 */
	public NodeCreateAction getNodeCreateAction (){
		if (this.nodeCreateAction==null){
			//istanziazione lazy
			this.nodeCreateAction = new NodeCreateAction ();
		}
		return this.nodeCreateAction;
	}
	
	/**
	 * L'azione di rimozione nodo di avanzamento.
	 */
	private NodeDeleteAction nodeDeleteAction;
	
	/**
	 * Ritorna l'azione di rimozione nodo di avanzamento.
	 * @return l'azione di rimozione di un nodo di avanzamento.
	 */
	public NodeDeleteAction getNodeDeleteAction (){
		if (this.nodeDeleteAction==null){
			//istanziazione lazy
			this.nodeDeleteAction = new NodeDeleteAction ();
		}
		return this.nodeDeleteAction;
	}
	
	/**
	 * L'azione di creazione di un nuovo progetto.
	 */
	private ProjectCreateAction projectCreateAction;
	
	/**
	 * Ritorna l'azione di creazione di un nuovo progetto.
	 * @return l'azione di creazione di un nuovo progetto.
	 */
	public ProjectCreateAction getProjectCreateAction (){
		if (this.projectCreateAction==null){
			//istanziazione lazy
			this.projectCreateAction = new ProjectCreateAction ();
		}
		return this.projectCreateAction;
	}
	
	/**
	 * L'azione di rimozione progetto.
	 */
	private ProjectDeleteAction projectDeleteAction;
	
	/**
	 * Ritorna l'azione di rimozione progetto.
	 * @return l'azione di rimozione progetto.
	 */
	public ProjectDeleteAction getProjectDeleteAction (){
		if (this.projectDeleteAction==null){
			//istanziazione lazy
			this.projectDeleteAction = new ProjectDeleteAction ();
		}
		return this.projectDeleteAction;
	}
	
	/**
	 * L'azione di apertura progetto.
	 */
	private ProjectOpenAction projectOpenAction;
	
	/**
	 * Ritorna l'azione di apertura progetto.
	 * @return l'azione di apertura progetto.
	 */
	public ProjectOpenAction getProjectOpenAction (){
		if (this.projectOpenAction==null){
			//istanziazione lazy
			this.projectOpenAction = new ProjectOpenAction ();
		}
		return this.projectOpenAction;
	}
	
	/**
	 * L'azione di salvataggio dello stato progetto.
	 */
	private ProjectSaveAction projectSaveAction;
	
	/**
	 * Ritorna l'azione di salvataggio dello stato di un progetto.
	 * @return l'azione di salvataggio progetto.
	 */
	public ProjectSaveAction getProjectSaveAction (){
		if (this.projectSaveAction==null){
			//istanziazione lazy
			this.projectSaveAction = new ProjectSaveAction ();
		}
		return this.projectSaveAction;
	}
	
	/**
	 * L'azione di chiusura progetto.
	 */
	private ProjectCloseAction projectCloseAction;
	
	/**
	 * Ritorna l'azione di chiusura progetto.
	 * @return l'azione di chiusura progetto.
	 */
	public ProjectCloseAction getProjectCloseAction (){
		if (this.projectCloseAction==null){
			//istanziazione lazy
			this.projectCloseAction = new ProjectCloseAction ();
		}
		return this.projectCloseAction;
	}
	
	/**
	 * L'azione di esportazione progetto in formato XML.
	 */
	private ProjectXMLExportAction projectXMLExportAction;
	
	/**
	 * Ritorna l'azione di esportazione progetto in formato XML.
	 * @return l'azione di esportazione progetto in formato XML.
	 */
	public ProjectXMLExportAction getProjectXMLExportAction (){
		if (this.projectXMLExportAction==null){
			//istanziazione lazy
			this.projectXMLExportAction = new ProjectXMLExportAction ();
		}
		return this.projectXMLExportAction;
	}
	
	/**
	 * L'azione di importazione progetto da fonte XML.
	 */
	private ProjectXMLImportAction projectXMLImportAction;
	
	/**
	 * Ritorna l'azione di importazione progetto da fonte XML.
	 * @return l'azione di importazione progetto da fonte XML.
	 */
	public ProjectXMLImportAction getProjectXMLImportAction (){
		if (this.projectXMLImportAction==null){
			//istanziazione lazy
			this.projectXMLImportAction = new ProjectXMLImportAction ();
		}
		return this.projectXMLImportAction;
	}
	
	/**
	 * Gestisce l'abilitazione del flusso di aggiornamento dello stato persistente di un nodo di avanzamento.
	 */
	private NodeUpdateAction nodeUpdateAction;
	
	/**
	 * Ritorna l'azione che gestisce l'abilitazione del flusso di aggiornamento dello stato persistente di un nodo di avanzamento.
	 * @return l'azione che gestisce l'abilitazione del flusso di aggiornamento dello stato persistente di un nodo di avanzamento.
	 */
	public NodeUpdateAction getNodeUpdateAction (){
		if (this.nodeUpdateAction==null){
			//istanziazione lazy
			this.nodeUpdateAction = new NodeUpdateAction ();
		}
		return this.nodeUpdateAction;
	}
	
	/**
	 * L'azione di inizio editazione di un nodo di avanzamento.
	 */
	private StartNodeEdit startNodeEdit;
	
	/**
	 * Ritorna l'azione di inizio editazione di un nodo di avaznamento.
	 * @return l'azione di inizio editazione di un nodo di avaznamento.
	 */
	public StartNodeEdit getStartNodeEdit (){
		if (this.startNodeEdit==null){
			//istanziazione lazy
			this.startNodeEdit = new StartNodeEdit ();
		}
		return this.startNodeEdit;
	}
	
	/**
	 * L'azione di inizio editazione di un periodo di avanzamento.
	 */
	private StartProgressEdit startProgressEdit;
	
	/**
	 * Ritorna l'azione di inizio editazione di un periodo di avaznamento.
	 * @return l'azione di inizio editazione di un periodo di avaznamento.
	 */
	public StartProgressEdit getStartProgressEdit (){
		if (this.startProgressEdit==null){
			//istanziazione lazy
			this.startProgressEdit = new StartProgressEdit ();
		}
		return this.startProgressEdit;
	}
	
	/**
	 * L'azione di inizio rimozione di un periodo di avanzamento.
	 */
	private ProgressDeleteAction progressDeleteAction;
	
	/**
	 * Ritorna l'azione di rimozione di un periodo di avaznamento.
	 * @return l'azione di rimozione di un periodo di avaznamento.
	 */
	public ProgressDeleteAction getProgressDeleteAction (){
		if (this.progressDeleteAction==null){
			//istanziazione lazy
			this.progressDeleteAction = new ProgressDeleteAction ();
		}
		return this.progressDeleteAction;
	}
	
}

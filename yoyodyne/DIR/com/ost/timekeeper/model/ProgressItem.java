/*
 * ProgressItem.java
 *
 * Created on 4 aprile 2004, 11.42
 */

package com.ost.timekeeper.model;

import java.util.*;

/**
 * Questa classe implementa un generico nodo della gerarchia.
 * @author  davide
 */
public class ProgressItem extends Observable{
	private List children = new ArrayList ();
	
	/** Holds value of property progressing. */
	private boolean progressing;
	
	private String name;
	private Period currentProgress;
	private List progresses = new ArrayList ();
	
	private ProgressItem parent = null;
	
	private Project project = null;
	
	private final List progressListeners = new ArrayList ();
	
	/** Costruttore vuoto.
	 */
	public ProgressItem() {
	}
	
	/** Crea una nuova istanza di ProgressItem assegnandogli un nome.
	 * @param name il nome.
	 */
	public ProgressItem(String name) {
		this.name=name;
	}
	
	public String getName (){return this.name;}
	
	public ProgressItem getParent() {
		return this.parent;
	}
	
	public Project getProject() {
		return this.project;
	}
	
	/**
	 * Inserisce un nuovo elemento figlio alla posizione desiderata.
	 * @param child il nuovo figlio.
	 * @param pos la posizione del figlio.
	 */	
	public void insert (ProgressItem child, int pos){
		this.children.add (pos, child);
		child.parent=this;
		child.project = this.project;
		
		this.setChanged();
		child.setChanged();
		this.notifyObservers();
		child.notifyObservers();
	}
	
	/**
	 * Inserisce un nuovo elemento figlio in coda agli altri.
	 * @param child il nuovo figlio.
	 */	
	public void insert (ProgressItem child){
		insert (child, this.children.size());
	}
	
	public void remove(int pos) {
		ProgressItem child = (ProgressItem)this.children.remove(pos);
		child.parent=null;
		child.project = null;
		
		this.setChanged();
		child.setChanged();
		this.notifyObservers();
		child.notifyObservers();
	}
	
	public void remove(ProgressItem child) {
		int ix = childIndex (child);
		this.remove(ix);
	}
	
	public int childIndex (ProgressItem child){
		for (int i=0;i<this.children.size();i++){
			ProgressItem childAt = (ProgressItem)this.children.get(i);
			if (child.equals(childAt)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Fa partire un avanzamento.
	 */	
	public void startPeriod() {
		if (this.progressing){
			//non ferma avanzamento esistente, lo continua
			return;
		}
		this.progressing = true;
		this.currentProgress = new Period (new GregorianCalendar ().getTime(), null);
		this.progresses.add (this.currentProgress);
		
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Termina l'avanzamento corrente.
	 * @return il periodo determinato dall'avanzamento terminato.
	 */	
	public Period stopPeriod() {
		if (!this.progressing){
			throw new IllegalStateException ();
		}
		this.currentProgress.setTo (new GregorianCalendar ().getTime());
		
		this.setChanged();
		this.notifyObservers();
		
		this.progressing = false;
		return this.currentProgress;
	}
	
	/** 
	 * Specifica se questo elemento ha un avanzamento correntemente attivo.
	 * @return <code>true</code> se c'è un avanzamento attivo;<code>false</code> altrimenti.
	 *
	 */
	public boolean isProgressing() {
		return this.progressing;
	}
	
	public void accept (ProgressVisitor visitor){
		for (Iterator it=this.children.iterator();it.hasNext();){
			visitor.visit ((ProgressItem)it.next ());
		}
		visitor.visit (this);
	}
	
	public void addProgressListener(ProgressListener l) {
		progressListeners.add (l);
	}
	
	public void removeProgressListener(ProgressListener l) {
		progressListeners.remove (l);
	}
	
	public String toString (){
		return this.name;
	}
	
	/**
	 * Ritorna la lista dei figli. Da usare solo in lettura. L'eventuale 
	 * aggiunta di un figlio direttamente alla lista non aggiorna il riferimento 
	 * al padre.
	 * @return la lista dei figli.
	 */	
	public List getChildren (){
		return this.children;
	}
	
	public ProgressItem childAt (int pos){
		return (ProgressItem)this.children.get (pos);
	}
	
	public int childCount (){
		return this.children.size();
	}
	
	/**
	 * Ritorna la lista di avanzamenti appartnenti a queto item. Non dovrebbe 
	 * essere usata per apportare modifiche agli avanzamenti.
	 * @return la lista di avanzamenti appartnenti a queto item.
	 */	
	public List getProgresses (){
		return this.progresses;
	}
	
	/**
	 * Ritorna gli avanzamenti apparteneneti ad un sottoalbero.
	 * @return gli avanzamenti apparteneneti al sottoalbero avente questo item 
	 * come radice.
	 */	
	public List getSubtreeProgresses (){
		List subProgresses = new ArrayList (this.getProgresses());
		for (Iterator it = this.getChildren().iterator(); it.hasNext();){
			subProgresses.addAll (((ProgressItem)it.next ()).getSubtreeProgresses ());
		}
		return subProgresses;
	}
	/**
	 * Ritorna gli elementi delsottoalbero.
	 * @return gli elementi del sottoalbero avente questo item 
	 * come radice.
	 */	
	public List getDescendants (){
		List children = getChildren ();
		List retValue = new ArrayList (children);
		for (Iterator it = children.iterator(); it.hasNext();){
			retValue.addAll (((ProgressItem)it.next ()).getDescendants());
		}
		return retValue;
	}
	
	public void itemChanged() {
		this.setChanged();
		this.notifyObservers();
	}
	
	/** Setter for property children.
	 * @param children New value of property children.
	 *
	 */
	public void setChildren(List children) {
		this.children=children;
	}
	
	/** Setter for property name.
	 * @param name New value of property name.
	 *
	 */
	public void setName(String name) {
		this.name=name;
	}
	
	/** Setter for property parent.
	 * @param parent New value of property parent.
	 *
	 */
	public void setParent(ProgressItem parent) {}
	
	/** Setter for property progresses.
	 * @param progresses New value of property progresses.
	 *
	 */
	public void setProgresses(List progresses) {
		this.progresses=progresses;
	}
	
	/** Setter for property progressing.
	 * @param progressing New value of property progressing.
	 *
	 */
	public void setProgressing(boolean progressing) {
		this.progressing=progressing;
	}
	
	/** Setter for property project.
	 * @param project New value of property project.
	 *
	 */
	public void setProject(Project project) {
		this.project=project;
	}
	
}

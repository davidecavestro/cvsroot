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
	private final List children = new ArrayList ();
	
	/** Holds value of property progressing. */
	private boolean progressing;
	
	private String name;
	/** Crea una nuova istanza di ProgressItem assegnandogli un nome.
	 * @param name il nome.
	 */
	public ProgressItem(String name) {
		this.name=name;
	}
	
	public String getName (){return this.name;}
	
	private ProgressItem parent = null;
	public ProgressItem getParent() {
		return this.parent;
	}
	
	/**
	 * Inserisce un nuovo elemento figlio alla posizione desiderata.
	 * @param child il nuovo figlio.
	 * @param pos la posizione del figlio.
	 */	
	public void insert (ProgressItem child, int pos){
		this.children.add (pos, child);
		child.parent=this;
		
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
	
	private Period currentProgress;
	private List progresses = new ArrayList ();
	/**
	 * Fa partire un avanzamento.
	 */	
	public void startPeriod() {
		if (this.progressing){
			//non ferma avanzamento esistente, lo continua
			return;
		}
		this.progressing = true;
		this.currentProgress = new Period (new GregorianCalendar (), null);
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
		this.currentProgress.setTo (new GregorianCalendar ());
		
		this.setChanged();
		this.notifyObservers();
		
		this.progressing = false;
		return this.currentProgress;
	}
	
	/** 
	 * Specifica se questo elemento ha un avanzamento correntemente attivo.
	 * @return <code>true</code> se c'� un avanzamento attivo;<code>false</code> altrimenti.
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
	
	private final List progressListeners = new ArrayList ();
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
}

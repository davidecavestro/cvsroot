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
public class ProgressItem {
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
	 * Inserisce un nuovo elemento figlio.
	 * @param child il nuovo figlio.
	 * @param pos la posizione del figlio.
	 */	
	public void insert (ProgressItem child, int pos){
		this.children.add (pos, child);
		child.parent=this;
	}
	
	public void remove(int pos) {
		((ProgressItem)this.children.remove(pos)).parent=null;
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
		this.currentProgress = new Period (new GregorianCalendar (), null);
		this.progresses.add (this.currentProgress);
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
	
	private final List progressListeners = new ArrayList ();
	public void addProgressListener(ProgressListener l) {
		progressListeners.add (l);
	}
	
	public void removeProgressListener(ProgressListener l) {
		progressListeners.remove (l);
	}
	
}

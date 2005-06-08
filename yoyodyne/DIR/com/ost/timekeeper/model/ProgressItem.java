/*
 * ProgressItem.java
 *
 * Created on 4 aprile 2004, 11.42
 */

package com.ost.timekeeper.model;

import java.util.*;

/**
 * Generico nodo della gerarchia di avanzamenti (nodo di avanzamento).
 * Un nodo può avere figli, nonchè avanzamenti associati.
 * Un nodo può avere un padre, se non lo ha esso funge da radice della gerarchia.
 * Un nodo è associato ad un progetto (lo stesso dei figli e del padre).
 *
 * @author  davide
 */
public final class ProgressItem extends Observable{
	
	/**
	 * Il codice di questo nodo.
	 */
	private String code;
	
	/**
	 * Il nome di questo nodo.
	 */
	private String name;
	
	/**
	 * La descrizione di qeusto nodo.
	 */
	private String description;
	
	/**
	 * Le annotazioni relative a questo nodo.
	 */
	private String notes;
	
	/**
	 * Stato di avanzamento
	 */
	private boolean progressing;
	
	/**
	 * Il padre.
	 */
	private ProgressItem parent;
	
	/**
	 * I figli di questo nodo.
	 */
	private List children = new ArrayList ();
	
	/**
	 * Gli avanzamenti effettuati su questo nodo.
	 */
	private List progresses = new ArrayList ();
	
	/**
	 * L'avanzamento corrente di questo nodo.
	 */
	private Progress currentProgress;
	
	/**
	 * Il progetto di appartenenza.
	 */
	private Project project;
	
	
	/**
	 * I listener registrati per questo nodo.
	 */
	private final List progressListeners = new ArrayList ();
	
	/**
	 * Costruttore vuoto.
	 */
	public ProgressItem () {
	}
	
	/**
	 * Costruttore con nome.
	 *
	 * @param name il nome.
	 */
	public ProgressItem (String name) {
		this.name=name;
	}
	
	/**
	 * Costruttore copia.
	 *
	 * @param source il nodo sorgente.
	 */
	public ProgressItem (final ProgressItem source) {
		this.code=source.code;
		this.description = source.description;
		this.name = source.name;
		this.notes = source.notes;
	}
	
	/**
	 * Ritorna il codice di questo nodo.
	 *
	 * @return il codice.
	 */
	public String getCode (){return this.code;}
	
	/**
	 * Ritorna il nome di questo nodo.
	 *
	 * @return il nome.
	 */
	public String getName (){return this.name;}
	
	/**
	 * Ritorna il padre di questo nodo.
	 *
	 * @return il padre
	 */
	public ProgressItem getParent () {
		return this.parent;
	}
	
	/**
	 * Ritorna il progettodi appartenenza di questo nodo.
	 *
	 * @return il progetto di appartenenza.
	 */
	public Project getProject () {
		return this.project;
	}
	
	/**
	 * Inserisce un nuovo elemento figlio alla posizione desiderata.
	 * L'inserimento del figlio viene notificata ai listener registrati su
	 * questo nodo e sul nuovo figlio.
	 *
	 * @param child il nuovo figlio.
	 * @param pos la posizione del figlio.
	 */
	public void insert (ProgressItem child, int pos){
		this.children.add (pos, child);
		child.parent=this;
		child.project = this.project;
		
		this.setChanged ();
		child.setChanged ();
		this.notifyObservers ();
		child.notifyObservers ();
	}
	
	/**
	 * Inserisce un nuovo elemento figlio in coda agli altri.
	 * L'inserimento del figlio viene notificata ai listener registrati su
	 * questo nodo e sul nuovo figlio.
	 *
	 * @param child il nuovo figlio.
	 * @return la posizione di inserimento del nuovo nodo.
	 */
	public int insert (ProgressItem child){
		final int position = this.children.size ();
		insert (child, position);
		return position;
	}
	
	/**
	 * Rimuove da questo nodo il figlio alla posizione <TT>pos</TT>.
	 * La rimozione del figlio viene notificata ai listener registrati su
	 * questo nodo e sul figlio rimosso.
	 *
	 * @param pos la posizione del figlio da rimuovere.
	 */
	public void remove (int pos) {
		ProgressItem child = (ProgressItem)this.children.remove (pos);
		child.parent=null;
		child.project = null;
		
		this.setChanged ();
		child.setChanged ();
		this.notifyObservers ();
		child.notifyObservers ();
	}
	
	/**
	 * Rimuove il figlio da questo nodo.
	 * La rimozione del figlio viene notificata ai listener registrati su
	 * questo nodo e sul figlio rimosso.
	 *
	 * @param child il figlio da rimuovere.
	 */
	public void remove (ProgressItem child) {
		int ix = childIndex (child);
		this.remove (ix);
	}
	
	/**
	 * Ritorna l'indice relativo alla posizione del figlio tra tutti i figli di
	 * questo nodo.
	 *
	 * @param child il figlio.
	 * @return l'indice relativo alla posizione del figlio.
	 */
	public int childIndex (ProgressItem child){
		for (int i=0;i<this.children.size ();i++){
			ProgressItem childAt = (ProgressItem)this.children.get (i);
			if (child.equals (childAt)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Fa partire un avanzamento su questo nodo.
	 *
	 * Questa azione viene notificata ai listener registrati su
	 * questo nodo.
	 */
	public synchronized Progress startPeriod () {
		return startPeriod (new Date ());
	}
	
	/**
	 * Fa partire un avanzamento su questo nodo dal momento specificato.
	 *
	 * Questa azione viene notificata ai listener registrati su
	 * questo nodo.
	 */
	public synchronized Progress startPeriod (final Date startDate) {
		if (this.progressing){
			//non ferma avanzamento esistente, lo continua
			return null;
		}
		this.progressing = true;
		this.currentProgress = new Progress (startDate, null, this);
		this.progresses.add (this.currentProgress);
		
		this.setChanged ();
		this.notifyObservers ();
		return this.currentProgress;
	}
	
	/**
	 * Termina l'avanzamento corrente su questo nodo.
	 * Questa azione viene notificata ai listener registrati su
	 * questo nodo.
	 *
	 * @return il periodo determinato dall'avanzamento terminato.
	 */
	public synchronized Progress stopPeriod () {
		if (!this.progressing){
			throw new IllegalStateException ();
		}
		this.currentProgress.setTo (new Date ());
		
		this.setChanged ();
		this.notifyObservers ();
		
		this.progressing = false;
		return this.currentProgress;
	}
	
	/**
	 * Verifica se questo elemento ha un avanzamento correntemente attivo.
	 *
	 * @return <code>true</code> se c'è un avanzamento attivo;<code>false</code> altrimenti.
	 */
	public boolean isProgressing () {
		return this.progressing;
	}
	
	/**
	 * Aggiunge un avanzamento a questo nodo.
	 *
	 * Questa azione viene notificata ai listener registrati su
	 * questo nodo.
	 */
	public synchronized void addProgress (final Progress progress) {
		this.progresses.add (progress);
		
		this.setChanged ();
		this.notifyObservers ();
	}
	
	/**
	 * Accetta il visitor.
	 *
	 * @param recurse specificare se applicare la visita ricorsivamente sul sottoalbero.
	 * @param visitor il visitor.
	 */
	public void accept (ProgressItemVisitor visitor, boolean recurse){
		for (Iterator it=this.children.iterator ();it.hasNext ();){
			((ProgressItem)it.next ()).accept (visitor, recurse);
		}
		visitor.visit (this);
	}
	
	/**
	 * Registra un nuovo listener.
	 *
	 * @param l il nuovo listener.
	 */
	public void addProgressListener (ProgressListener l) {
		progressListeners.add (l);
	}
	
	/**
	 * Rimuove un listener precedentemente registrato.
	 *
	 * @param l il listener da rimuovere.
	 */
	public void removeProgressListener (ProgressListener l) {
		progressListeners.remove (l);
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questo nodo.
	 *
	 * @return una stringa che rappresenta questo nodo.
	 */
	public String toString (){
		return this.name;
	}
	
	/**
	 * Ritorna la lista dei figli di questo nodo. Da usare solo in lettura. L'eventuale
	 * aggiunta di un figlio direttamente alla lista non aggiorna il riferimento
	 * al padre.
	 *
	 * @return la lista dei figli.
	 */
	public List getChildren (){
		return new ArrayList (this.children);
	}
	
	/**
	 * Ritorna il figlio che occupa una determinata posizione tra i tutti i figli
	 * di questo nodo.
	 *
	 * @param pos la posizione del figlio da cercare.
	 * @return il figlio di questo nodo avente posizione <TT>pos</TT>.
	 */
	public ProgressItem childAt (int pos){
		return (ProgressItem)this.children.get (pos);
	}
	
	/**
	 * Ritorna il numero di figli di questo nodo.
	 *
	 * @return il numero dei figli.
	 */
	public int childCount (){
		return this.children.size ();
	}
	
	/**
	 * Ritorna la lista di avanzamenti appartnenti a queto nodo. Non dovrebbe
	 * essere usata per apportare modifiche agli avanzamenti.
	 *
	 * @return la lista di avanzamenti appartnenti a queto nodo.
	 */
	public List getProgresses (){
		return new ArrayList (this.progresses);
	}
	
	/**
	 * Ritorna gli avanzamenti apparteneneti al sottoalbero avente questo nodo
	 * come radice.
	 * Una lista di {@link com.ost.timekeeper.model.Progress}.
	 *
	 * @return gli avanzamenti apparteneneti al sottoalbero.
	 */
	public List getSubtreeProgresses (){
		final List subProgresses = new ArrayList (this.progresses);
		for (final Iterator it = this.children.iterator (); it.hasNext ();){
			subProgresses.addAll (((ProgressItem)it.next ()).getSubtreeProgresses ());
		}
		return subProgresses;
	}
	/**
	 * Ritorna gli elementi del sottoalbero.
	 * Una lista di {@link com.ost.timekeeper.model.ProgressItem}
	 * @return gli elementi del sottoalbero avente questo item
	 * come radice.
	 */
	public List getDescendants (){
		final List children = getChildren ();
		final List retValue = new ArrayList (children);
		for (Iterator it = children.iterator (); it.hasNext ();){
			retValue.addAll (((ProgressItem)it.next ()).getDescendants ());
		}
		return retValue;
	}
	
	/**
	 * Notifica i listener registrati su questo nodo delle modifiche avvenute.
	 */
	public void itemChanged () {
		this.setChanged ();
		this.notifyObservers ();
	}
	
	/**
	 * Imposta la lista dei figli di questo nodo.
	 * <BR>
	 * N.B.: questo metodo non modifica i riferimenti dei figli versoilnuovo padre.
	 * @see com.ost.timekeeper.model.ProgressItem#insert .
	 *
	 * @param children la nuova lista dei figli di questo nodo.
	 */
	public void setChildren (List children) {
		this.children=new ArrayList (children);
	}
	
	/**
	 * Imposta il codice di questo nodo.
	 *
	 * @param code il nuovo codice.
	 */
	public void setCode (String code) {
		this.code=code;
	}
	
	/**
	 * Imposta il nome di questo nodo.
	 *
	 * @param name il nuovo nome.
	 */
	public void setName (String name) {
		this.name=name;
	}
	
	/**
	 * Imposta il padre di questo nodo.
	 * <BR>
	 * N.B.: questo metodo non modifica i riferimenti del nuovo e del vechio padre verso questo oggetto.
	 * @see com.ost.timekeeper.model.ProgressItem#insert .
	 *
	 * @param parent il nuovo padre.
	 */
	public void setParent (ProgressItem parent) {this.parent=parent;}
	
	/**
	 * Imposta gli avanzamenti relativi a questo nodo.
	 * ESCLUSIVAMENTE AD USO DEL PERSISTENT MANAGER.
	 *
	 * @param progresses Gli avanzamenti.
	 */
	public synchronized void setProgresses (List progresses) {
		this.progresses = new ArrayList (progresses);
	}
	
	/**
	 * Imposta lo stato di avanzamento di questo nodo.
	 * ESCLUSIVAMENTE AD USO DEL PERSISTENT MANAGER.
	 *
	 * @param progressing lo stato di avanzamento di qeusto nodo.
	 */
	public void setProgressing (boolean progressing) {
		this.progressing=progressing;
	}
	
	/**
	 * Imposta il progetto di appartenenza di questo nodo.
	 * ESCLUSIVAMENTE AD USO DEL PERSISTENT MANAGER.
	 *
	 * @param project il progetto di appartenenza.
	 */
	public void setProject (Project project) {
		this.project=project;
	}
	
	/**
	 * Ritorna l'avanzamento corrente.
	 *
	 * @return l'avanzamento corrente.
	 */
	public Progress getCurrentProgress (){
		return this.currentProgress;
	}
	
	/**
	 * Ritorna la descrizione di questo nodo.
	 *
	 * @return la descrizione.
	 */
	public String getDescription () {
		return this.description;
	}
	
	/**
	 * Imposta la descrizione di questo nodo.
	 *
	 * @param description la descrizione.
	 */
	public void setDescription (String description) {
		this.description = description;
	}
	
	/**
	 * Ritorna le annotazioni relative a questo nodo.
	 *
	 * @return le note.
	 */
	public String getNotes () {
		return this.notes;
	}
	
	/**
	 * Imposta le annotazioni per questo nodo.
	 *
	 * @param notes le nuove note.
	 */
	public void setNotes (String notes) {
		this.notes = notes;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo nodo è la radice della gerarchia dei 
	 * nodi di avanzamento del progetto.
	 *
	 * @return <TT>true</TT> se questo nodo è la radice della gerarchia dei 
	 * nodi di avanzamento del progetto.
	 */	
	public boolean isRoot (){
		return this.parent==null;
	}
	
	/**
	 * Rimuove il periodo di avanzamento specificato da questo nodo.
	 * @param progress l'avanzamento da rimuovere.
	 */
	public synchronized void deleteProgress (final Progress progress){
		final int size = this.progresses.size ();
		for (int i=0;i<size;i++){
			final Progress candidate = (Progress)this.progresses.get (i);
			if (candidate==progress){
				this.progresses.remove (i);
				break;
			}
		}
		this.setChanged ();
		this.notifyObservers ();
	}
	
	/**
	 * Aggiunge il periodo di avanzamento specificato a questo nodo.
	 * @param progress l'avanzamento da aggiungere.
	 * @return la posizione del nuovo avanzamento.
	 */
	public synchronized int insert (final Progress progress){
		progress.setProgressItem (this);
		final int position = this.progresses.size ();
		this.progresses.add (progress);
		this.setChanged ();
		this.notifyObservers ();
		return position;
	}
	
	/**
	 * Aggiunge il periodo di avanzamento specificato a questo nodo.
	 * @param progress l'avanzamento da aggiungere.
	 */
	public synchronized void insert (final Progress progress, int position){
		progress.setProgressItem (this);
		this.progresses.add (position, progress);
		this.setChanged ();
		this.notifyObservers ();
	}
}

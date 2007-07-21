/*
 * ProgressItem.java
 *
 * Created on 4 aprile 2004, 11.42
 */

package com.ost.timekeeper.model;

import com.davidecavestro.common.util.ExceptionUtils;
import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.PieceOfWorkBackup;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.model.TaskBackup;
import java.util.*;

/**
 * Generico nodo della gerarchia di avanzamenti (nodo di avanzamento).
 * Un nodo pu&ograve; avere figli, nonch� avanzamenti associati.
 * Un nodo pu&ograve; avere un padre, se non lo ha esso funge da radice della gerarchia.
 * Un nodo &egrave; associato ad un progetto (lo stesso dei figli e del padre).
 *
 *<P>
 * Pur implementando l'interfaccia <TT>Task</TT>, questa classe DEVE ricevere oggetti di tipo effettivo <TT>ProgressItem</TT> per funzionare correttamente.
 *<BR>
 *Questo comportamento anomalo &egrave; dovuto all'utilizzo di codice JDO legacy.
 *
 * @author  davide
 */
public class ProgressItem extends Observable implements Task {
	
	/**
	 * Il codice di questo nodo.
	 */
	protected String code;
	
	/**
	 * Il nome di questo nodo.
	 */
	protected String name;
	
	/**
	 * La descrizione di qeusto nodo.
	 */
	protected String description;
	
	/**
	 * Le annotazioni relative a questo nodo.
	 */
	protected String notes;
	
	/**
	 * Stato di avanzamento
	 * @deprecated campo non pi&ugrave; gestito dalla versione 2.
	 */
	private boolean progressing;
	
	/**
	 * Il padre.
	 */
	protected ProgressItem parent;
	
	/**
	 * I figli di questo nodo.
	 */
	protected List children = new ArrayList ();
	
	/**
	 * Gli avanzamenti effettuati su questo nodo.
	 */
	protected List progresses = new ArrayList ();
	
	/**
	 * L'avanzamento corrente di questo nodo.
	 * @deprecated campo non pi&ugrave; gestito dalla versione 2.
	 */
	private Progress currentProgress;
	
	/**
	 * Il progetto di appartenenza.
	 * @warning non utilizzato, per retrocompatibilit&agrave; con JTTS v1 (BUG)
	 */
	Project project;
	
	
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
	 * Costruttore.
	 *
	 */
	public ProgressItem (final String code, final String name, final String description, final String notes) {
		this.code=code;
		this.description = description;
		this.name = name;
		this.notes = notes;
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
	public Task getParent () {
		return this.parent;
	}
	
	/**
	 * Ritorna il progetto di appartenenza di questo nodo.
	 *
	 * @warning non valorizato, per retrocompatibilit&agrave; con JTTS v1 (BUG)
	 * @return il progetto di appartenenza.
	 */
//	public WorkSpace getWorkSpace () {
//		return this.project;
//	}
	
	/**
	 * Inserisce un nuovo elemento figlio alla posizione desiderata.
	 * L'inserimento del figlio viene notificata ai listener registrati su
	 * questo nodo e sul nuovo figlio.
	 *
	 * @param child il nuovo figlio.
	 * @param pos la posizione del figlio. Usare unvalore negativoper accodare.
	 */
	public int insert (Task child, int pos){
		int retValue;
		if (pos<0) {
			safeChildrenAccessor().add (child);
			retValue = safeChildrenAccessor().indexOf (child);
		} else {
			safeChildrenAccessor().add (pos, child);
			retValue = pos;
		}
		((ProgressItem)child).parent=this;
		((ProgressItem)child).project = this.project;
		
		this.setChanged ();
		((ProgressItem)child).setChanged ();
		this.notifyObservers ();
		((ProgressItem)child).notifyObservers ();
		
		return retValue;
	}
	
	/**
	 * Inserisce un nuovo elemento figlio in coda agli altri.
	 * L'inserimento del figlio viene notificata ai listener registrati su
	 * questo nodo e sul nuovo figlio.
	 *
	 * @param child il nuovo figlio.
	 * @return la posizione di inserimento del nuovo nodo.
	 */
	public int insert (Task child){
		int position;
		synchronized (safeChildrenAccessor()) {
			position = this.safeChildrenAccessor().size ();
			insert (child, position);
		}
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
		Task child = (Task)this.safeChildrenAccessor().remove (pos);
		((ProgressItem)child).parent=null;
		((ProgressItem)child).project = null;
		
		this.setChanged ();
		((ProgressItem)child).setChanged ();
		this.notifyObservers ();
		((ProgressItem)child).notifyObservers ();
	}
	
	/**
	 * Rimuove il figlio da questo nodo.
	 * La rimozione del figlio viene notificata ai listener registrati su
	 * questo nodo e sul figlio rimosso.
	 *
	 * @param child il figlio da rimuovere.
	 */
	public void remove (Task child) {
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
	public int childIndex (Task child){
		for (int i=0;i<this.safeChildrenAccessor().size ();i++){
			Task childAt = (Task)this.safeChildrenAccessor().get (i);
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
	public synchronized PieceOfWork startPeriod () {
		return startPeriod (new Date ());
	}
	
	/**
	 * Fa partire un avanzamento su questo nodo dal momento specificato.
	 *
	 * Questa azione viene notificata ai listener registrati su
	 * questo nodo.
	 */
	public synchronized PieceOfWork startPeriod (final Date startDate) {
		if (this.progressing){
			//non ferma avanzamento esistente, lo continua
			return null;
		}
		this.progressing = true;
		this.currentProgress = new Progress (startDate, null, this);
		safeProgressesAccessor ().add (this.currentProgress);
		
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
	public synchronized PieceOfWork stopPeriod () {
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
	 * @return <code>true</code> se c'� un avanzamento attivo;<code>false</code> altrimenti.
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
	public synchronized void addProgress (final PieceOfWork progress) {
		safeProgressesAccessor ().add (progress);
		
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
		for (Iterator it=this.safeChildrenAccessor().iterator ();it.hasNext ();){
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
	 * Ritorna la lista dei figli di questo nodo. Da usare solo in lettura.
	 * La lista non &egrave; modificabile.
	 *
	 * @return la lista dei figli.
	 */
	public List getChildren (){
		return Collections.unmodifiableList (this.safeChildrenAccessor());
	}
	
	/**
	 * Ritorna il figlio che occupa una determinata posizione tra i tutti i figli
	 * di questo nodo.
	 *
	 * @param pos la posizione del figlio da cercare.
	 * @return il figlio di questo nodo avente posizione <TT>pos</TT>.
	 */
	public Task childAt (int pos){
		return (Task)this.safeChildrenAccessor().get (pos);
	}
	
	/**
	 * Ritorna il numero di figli di questo nodo.
	 *
	 * @return il numero dei figli.
	 */
	public int childCount (){
		return this.safeChildrenAccessor().size ();
	}
	
	/**
	 * Ritorna la lista di avanzamenti appartnenti a queto nodo. Non dovrebbe
	 * essere usata per apportare modifiche agli avanzamenti.
	 *
	 * @return la lista di avanzamenti appartnenti a queto nodo.
	 */
	public List getPiecesOfWork (){
		return Collections.unmodifiableList (safeProgressesAccessor ());
	}
	
	private transient List _safeProgressesAccessor;
	/**
	 * Protegge l'accesso al campo interno da eventuali eccezioni dovutea corruzione dati. Siccome tali dati sonoconsideratiirrecuperabili
	 * allora tanto vale proseguire.
	 */
	protected List safeProgressesAccessor (){
		try {
			return this.progresses;
		} catch (Throwable e) {
			/*
			 * @workaround per mitigare problemi di corruzione dati
			 */
			if (_safeProgressesAccessor==null) {
				_safeProgressesAccessor = new ArrayList ();
				/*
				 * mostra messaggio solo la prima volta
				 */
				System.out.println ("WARNING: Potential corruption detected. Erasing unavailable object reference.");
				System.out.println ("Root cause:" + ExceptionUtils.getStackTrace (e));
			}
			return _safeProgressesAccessor;
		}
	}
	
	private transient List _safeChildrenAccessor;
	/**
	 * Protegge l'accesso al campo interno da eventuali eccezioni dovutea corruzione dati. Siccome tali dati sonoconsideratiirrecuperabili
	 * allora tanto vale proseguire.
	 */
	protected List safeChildrenAccessor (){
		try {
			return this.children;
		} catch (Throwable e) {
			/*
			 * @workaround per mitigare problmi di corruzione dati
			 */
			if (_safeChildrenAccessor==null) {
				_safeChildrenAccessor = new ArrayList ();
				/*
				 * mostra messaggio solo la prima volta
				 */
				System.out.println ("WARNING: Potential corruption detected. Erasing unavailable object reference.");
				System.out.println ("Root cause:" + ExceptionUtils.getStackTrace (e));
			}
			return _safeChildrenAccessor;
		}
	}
	/**
	 * Ritorna gli avanzamenti apparteneneti al sottoalbero avente questo nodo
	 * come radice.
	 * Una lista di {@link com.ost.timekeeper.model.PieceOfWork}.
	 *
	 * @return gli avanzamenti apparteneneti al sottoalbero.
	 */
	public List getSubtreeProgresses (){
		final List subProgresses = new ArrayList (safeProgressesAccessor ());
		for (final Iterator it = this.safeChildrenAccessor().iterator (); it.hasNext ();){
			subProgresses.addAll (((ProgressItem)it.next ()).getSubtreeProgresses ());
		}
		return subProgresses;
	}
	/**
	 * Ritorna gli elementi del sottoalbero.
	 * Una lista di {@link com.ost.timekeeper.model.Task}
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
	 * N.B.: questo metodo non modifica i riferimenti dei figli verso ilnuovo padre.
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
	 * @see com.ost.timekeeper.model.Task#insert .
	 *
	 * @param parent il nuovo padre.
	 *@throws ClassCastException se il parametro passato non &egrave; di tipo ProgressItem
	 */
	public void setParent (Task parent) {this.parent=(ProgressItem)parent;}
	
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
//	public void setProject (Project project) {
//		this.project=project;
//	}
	
	/**
	 * Ritorna l'avanzamento corrente.
	 *
	 * @return l'avanzamento corrente.
	 */
	public PieceOfWork getCurrentProgress (){
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
	 * Ritorna <TT>true</TT> se questo nodo � la radice della gerarchia dei 
	 * nodi di avanzamento del progetto.
	 *
	 * @return <TT>true</TT> se questo nodo � la radice della gerarchia dei 
	 * nodi di avanzamento del progetto.
	 */	
	public boolean isRoot (){
		return this.parent==null;
	}
	
	/**
	 * Rimuove il periodo di avanzamento specificato da questo nodo.
	 * @param progress l'avanzamento da rimuovere.
	 */
	public synchronized void removePieceOfWork (final PieceOfWork progress){
		final int size = safeProgressesAccessor ().size ();
		for (int i=0;i<size;i++){
			final PieceOfWork candidate = (PieceOfWork)safeProgressesAccessor ().get (i);
			if (candidate==progress){
				safeProgressesAccessor ().remove (i);
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
	public synchronized int insert (final PieceOfWork progress){
		progress.setTask (this);
		final int position = safeProgressesAccessor ().size ();
		safeProgressesAccessor ().add (progress);
		this.setChanged ();
		this.notifyObservers ();
		return position;
	}
	
	/**
	 * Aggiunge il periodo di avanzamento specificato a questo nodo.
	 * 
	 * @param position la posizionedelnuovoavanzamento. Usare unvalore negativoper accodare.
	 * @param progress l'avanzamento da aggiungere.
	 */
	public synchronized int insert (final PieceOfWork progress, int position){
		progress.setTask (this);
		int retValue;
		if (position<0) {
			progresses.add (progress);
			retValue = progresses.indexOf (progress);
		} else {
			progresses.add (position, progress);
			retValue = position;
		}
		this.setChanged ();
		this.notifyObservers ();
		
		return retValue;
	}

	public int pieceOfWorkIndex (PieceOfWork p) {
		return progresses.indexOf (p);
	}

	public PieceOfWork pieceOfWorkAt (int pos) {
		return (PieceOfWork)safeProgressesAccessor ().get (pos);
	}

	public int pieceOfWorkCount () {
		return safeProgressesAccessor ().size ();
	}

	public TaskBackup backup () {
		return new TaskBackupOmpl (this);
	}

	/**
	 * Implementa il backup. Classe ad esclusivo uso interno, da non rendere persistente.
	 *<P>
	 * La classe &egrave; statica per evitare l'accesso involontarioalle variabili della classe che la contiene. Deve avere l'accesso solamente per estensione!
	 */
	private static class TaskBackupOmpl extends ProgressItem implements TaskBackup {
		private final ProgressItem _source;
		public TaskBackupOmpl (final ProgressItem pi) {
			super (pi);
			_source = pi;
			
			/*
			 * Usa le liste interne di riferimenti per salvare i backup.
			 */
			children = new ArrayList ();
			for (final Iterator it = pi.safeChildrenAccessor().iterator (); it.hasNext ();) {
				final Task t = (Task)it.next ();
				safeChildrenAccessor().add (t.backup ());
			}
			progresses = new ArrayList ();
			for (final Iterator it = pi.progresses.iterator (); it.hasNext ();) {
				final PieceOfWork pow = (PieceOfWork)it.next ();
				progresses.add (pow.backup ());
			}
			
		}
		public Task getSource () {
			return _source;
		}
		
	public void restore () {
		
		_source.code = code;
		_source.description = description;
		_source.name = name;
		_source.notes = notes;

		_source.project = project;
		
		_source.children = new ArrayList ();
		for (final Iterator it = safeChildrenAccessor().iterator (); it.hasNext ();) {
			final TaskBackup tb = (TaskBackup)it.next ();
			tb.restore ();
			_source.safeChildrenAccessor().add (tb.getSource ());
		}
		_source.progresses = new ArrayList ();
		for (final Iterator it = progresses.iterator (); it.hasNext ();) {
			final PieceOfWorkBackup powb = (PieceOfWorkBackup)it.next ();
			powb.restore ();
			_source.progresses.add (powb.getSource ());
		}
	}
		
	}

}

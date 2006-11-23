/*
 * TaskTreeModel.java
 *
 * Created on November 9, 2006, 12:31 AM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.model.event.TaskTreeModelListener;

/**
 * Modello dell'albero degli avanzamenti.
 * <P>
 * Per "albero degli avanzamenti" si intende la gerarchia di <CODE>Task</CODE> 
 * appartenenti ad un progetto, con aggiunti tutti i <CODE>PieceOfWork</CODE> ad essi associati.
 *
 * @author Davide Cavestro
 */
public interface TaskTreeModel {
	
	/**
	 * Ritorna la radice dell'albero.
	 * 
	 * @return la radice dell'albero.
	 */
	Task getRoot ();
	
	/**
	 * Aggiunge un listenr per <OCDE>TaskTreeModelEvent</CODE> inviato a fronte delle modifiche all'albero.
	 * @param l il listener da aggiungere.
	 * @see removeTaskTreeModelListener
	 */
	void addTaskTreeModelListener (TaskTreeModelListener l);
	/**
	 * Rimuove un listener precedentemente aggiunto tramite <CODE>addTaskTreeModelListener</CODE>.
	 * 
	 * @param l  il listener da rimuovere.
	 * @see addTaskTreeModelListener
	 */
	void removeTaskTreeModelListener (TaskTreeModelListener l);
}

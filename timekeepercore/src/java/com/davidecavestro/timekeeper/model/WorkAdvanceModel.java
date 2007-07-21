/*
 * WorkAdvanceModel.java
 *
 * Created on November 25, 2006, 6:24 PM
 *
 */

package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.model.event.WorkAdvanceModelListener;
import java.util.Set;

/**
 * Modello di elenco degli avanzamenti in corso.
 * <P>
 * Esso rappresenta la lista dei lavori, o meglio, delle misurazioni di durata del lavoro, attualmente in corso
 * Gli avanzamenti in questione sono rappresentati dai <CODE>PieceOfWork</CODE> dell'albero degliavanzamenti (vedi <CODE>TaskTreeModel</CODE>
 * con data di fine nulla.
 *
 * @author Davide Cavestro
 */
public interface WorkAdvanceModel {
	
	/**
	 * Ritorna gli avanzamenti in fase di esecuzione.
	 *
	 * @return gli avanzamenti in fase di esecuzione.
	 */
	Set<PieceOfWork> getAdvancing ();
	
	/**
	 * Aggiunge un listenr per <OCDE>WorkAdvanceModelEvent</CODE> inviato a fronte delle modifiche al modello.
	 * @param l il listener da aggiungere.
	 * @see removeWorkAdvanceModelListener
	 */
	void addWorkAdvanceModelListener (WorkAdvanceModelListener l);
	
	/**
	 * Rimuove un listener precedentemente aggiunto tramite <CODE>addWorkAdvanceModelListener</CODE>.
	 *
	 * @param l  il listener da rimuovere.
	 * @see addWorkAdvanceModelListener
	 */
	void removeWorkAdvanceModelListener (WorkAdvanceModelListener l);
}

/*
 * Task.java
 *
 * Created on November 11, 2006, 12:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.davidecavestro.timekeeper.model;

import java.util.List;

/**
 * Interfaccia per un nodo dell'albero di tracciamento del lavoro.
 *
 * @author Davide Cavestro
 */
public interface Task {
	
	/**
	 * Ritorna il task superiore nella gerarchia.
	 * 
	 * @return il task superiore.
	 */
	Task getParent ();
	
	/**
	 * Inserisce un nuovo elemento figlio alla posizione desiderata.
	 *
	 * @param child il nuovo figlio.
	 * @param pos la posizione del figlio.
	 */
	void insert (Task child, int pos);
	
	/**
	 * Ritorna l'indice relativo alla posizione del figlio tra tutti i figli di
	 * questo nodo.
	 *
	 * @param child il figlio.
	 * @return l'indice relativo alla posizione del figlio.
	 */
	int childIndex (Task child);
	
	/**
	 * Rimuove da questo nodo il figlio alla posizione <TT>pos</TT>.
	 *
	 * @param pos la posizione del figlio da rimuovere.
	 */
	void remove (int pos);
	
	/**
	 * Ritorna il figlio che occupa una determinata posizione tra i tutti i figli
	 * di questo nodo.
	 *
	 * @param pos la posizione del figlio da cercare.
	 * @return il figlio di questo nodo avente posizione <TT>pos</TT>.
	 */
	Task childAt (int pos);
	
	/**
	 * Ritorna il progettod i appartenenza di questo nodo.
	 *
	 * @return il progetto di appartenenza.
	 */
	WorkSpace getWorkSpace ();
	
	/**
	 * Ritorna la lista di avanzamenti appartenenti a queto nodo. Non dovrebbe
	 * essere usata per apportare modifiche agli avanzamenti.
	 *
	 * @return la lista di avanzamenti appartnenti a queto nodo.
	 */
	List<PieceOfWork> getPiecesOfWork ();
	
	/**
	 * Ritorna il nome di questo nodo.
	 *
	 * @return il nome.
	 */
	String getName ();	
	
	/**
	 * Ritorna il numero di figli di questo nodo.
	 *
	 * @return il numero dei figli.
	 */
	int childCount ();	
	
	/**
	 * Rimuove il periodo di avanzamento specificato da questo nodo.
	 * @param p l'avanzamento da rimuovere.
	 */
	void removePieceOfWOrk (PieceOfWork p);
	
	/**
	 * Ritorna l'indice dell'avanzamento tra quelli appartenenti a questo task
	 * 
	 * @param p 
	 * @return 
	 */
	int pieceOfWorkIndex (PieceOfWork p);
}

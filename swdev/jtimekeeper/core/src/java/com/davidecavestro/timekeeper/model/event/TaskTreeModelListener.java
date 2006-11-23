/*
 * TaskTreeModelListener.java
 *
 * Created on November 9, 2006, 12:19 AM
 *
 */

package com.davidecavestro.timekeeper.model.event;

import java.util.EventListener;

/**
 * Definisce l'iNterfaccia per la notifica delle modifiche all'a'lbero degli avanzamenti (TaskTreeModel).
 *<P>
 *
 * @see javax.swing.event.TreeModelListener
 *
 * @author Davide Cavestro
 */
public interface TaskTreeModelListener extends EventListener {
	/**
	 * Invocato a fronte della modifica di alcuni nodi del sottoalbero, senza che gli stessisiano statispostati.
	 */
	void treeNodesChanged (TaskTreeModelEvent e);
	/**
	 * INvocatoa fronte dell'inserimentodi nuovi nodi nel sottoalbero.
	 */
	void treeNodesInserted (TaskTreeModelEvent e);
	/**
	 * Invocato a fronte della rimozione dei nodi dell'albero.
	 */
	void treeNodesRemoved (TaskTreeModelEvent e);
	/**
	 * Invocato a fronte di una drastica modifica del sottoalbero.
	 */
	void treeStructureChanged (TaskTreeModelEvent e);
}

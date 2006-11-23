/*
 * TaskTreePath.java
 *
 * Created on November 8, 2006, 11:07 PM
 *
 */

package com.davidecavestro.timekeeper.model;

/**
 * Percorso dell'albero degli avanzamenti.
 * <P>
 * I nodi dell'albero degli avanzamenti sono istanze di {@link Task}.
 *
 * @author Davide Cavestro
 */
public class TaskTreePath {
	
	/**
	 * Il percorso superiore.
	 */
	private final TaskTreePath _parentPath;
	
	/**
	 * L'ultimo elemento del percorso, ovvero quello pi&ugrave distante dalla radice dell'albero.
	 */
	private final Task _lastElement;
	
	/**
	 * Costruttore con elemento finale.
	 * <P>
	 * Fornisce il percorso completo fino alla radice dell'albero rintracciando i nodi superiori dell'elemento specificato.
	 * @param lastElement  l'ultimo elemento del percorso creato.
	 */
	public TaskTreePath (final Task lastElement) {
		_parentPath = 
			lastElement.getParent ()==null?
				null:
				new TaskTreePath (lastElement.getParent ());
		_lastElement = lastElement;
	}
	
//	/**
//	 * Costruttore composto.
//	 * @param parentPath il percorso padre.
//	 * @param lastElement l'ultimo elemento di questo percorso.
//	 */
//	public TaskTreePath (final TaskTreePath parentPath, final Task lastElement) {
//		_parentPath = parentPath;
//		_lastElement = lastElement;
//	}
	
//	public static TaskTreepath populatePath (final Task lastElement) {
//		return new 
//	}
	
	/**
	 * Ritorna il percorso che comprende tutti gli elementidi questo percorso, escluso l'ltimo.
	 * 
	 * @return il percorso superiore.
	 */
	public TaskTreePath getParentPath () {
		return _parentPath;
	}

	/**
	 * Ritorna l'ultimo elemento di questo percorso.
	 * 
	 * @return l'ultimo elemento di questo percorso.
	 */
	public Task getLastPathComponent () {
		return _lastElement;
	}

	/**
	 * Ritorna <TT>true</TT> se questo percorso &egraveM uguale a quello specificato.
	 * <P>
	 * Due percorsi sono uguali se contengono solamente nodi uguali nella medesima sequenza.
	 * 
	 * @param p il percorso
	 * @return <TT>true</TT> se questo percorso &egraveM uguale a quello specificato.
	 */
	public boolean equals (TaskTreePath p) {
		if (!_lastElement.equals (p.getLastPathComponent ())) {
			return false;
		}
		final TaskTreePath testingParent = p.getParentPath ();
		if (_parentPath!=null && testingParent!=null) {
			/* entrambi diversi da NULL */
			return _parentPath.equals (testingParent);
		} else {
			/* true se entrambi NULL */
			return _lastElement==testingParent;
		}
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo percorso contiene il percorso specificato.
	 * 
	 * @param p il candidato sotto-percorso da esaminare.
	 * @return <TT>true</TT> se questo percorso contiene il percorso specificato.
	 */
	public boolean contains (final TaskTreePath p) {
		if (_lastElement.equals (p.getLastPathComponent ())) {
			return true;
		} else {
			if (_parentPath !=null) {
				/*
				 * Prova con il percorso duperiore.
				 */
				return _parentPath.contains (p);
			}
		}
		
		return false;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo percorso contiene il task specificato.
	 * 
	 * @param t task da esaminare.
	 * @return <TT>true</TT> se questo percorso contiene il task specificato.
	 */
	public boolean contains (final Task t) {
		if (_lastElement.equals (t)) {
			return true;
		} else {
			if (_parentPath !=null) {
				/*
				 * Prova con il percorso duperiore.
				 */
				return _parentPath.contains (t);
			}
		}
		
		return false;
	}
}

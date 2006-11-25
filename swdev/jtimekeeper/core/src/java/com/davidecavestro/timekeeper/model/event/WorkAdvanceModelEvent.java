/*
 * WorkAdvanceModelEvent.java
 *
 * Created on November 25, 2006, 6:24 PM
 *
 */

package com.davidecavestro.timekeeper.model.event;

import com.davidecavestro.timekeeper.model.PieceOfWork;

/**
 * Incapsula le informazioni relative ad un evento di avanzamento di corso del lavoro.
 *
 * @author Davide Cavestro
 */
public class WorkAdvanceModelEvent extends java.util.EventObject {
	
	
	/**
	 * Identifica l'aggiunta di un avanzamentoin corso.
	 */
	public final static int INSERT = 1;
	/**
	 * Identifica la rimozione di un avanzamentoin corso.
	 */
	public final static int DELETE = -1;
	
	private final int _type;
	
	/**
	 * Identifica tutti gli avanzamenti in corso.
	 */
	public final static PieceOfWork[] ALL = new PieceOfWork[0];
	
	private final PieceOfWork[] _pow;
	
	/**
	 *
	 * Tutti gli avanzamenti in corso sono stati rimossi.
	 *
	 * @param source la sorgente di questo evento.
	 */
	public WorkAdvanceModelEvent (final Object source) {
		this (source, ALL, DELETE);
	}
	
	/**
	 * Gli avanzamenti specificati sono stati interessati da una azione di tipo <CODE>type</CODE>.
	 * <P>
	 * <CODE>type<CODE> pu&ograve; valere: {@link #INSERT} o {@link #DELETE}.
	 *
	 * @param pow gli avanzamenti interessati.
	 * @param source l'oggetto responsabile della generazione di questo evento.
	 * @param type il tipo di cambiamento.
	 */
	public WorkAdvanceModelEvent (final Object source, final PieceOfWork[] pow, final int type) {
		super (source);
		_pow = pow;
		_type = type;
	}
	
	/**
	 * Ritorna il tipo di evento. Vale {@link #INSERT} o {@link #DELETE}.
	 *
	 * @return il tipo di evento.
	 */
	public int getType (){
		return _type;
	}
	
	/**
	 * Ritorna gli avanzamenti interessati da questo evento.
	 *
	 * @return gli avanzamenti interessati da questo evento.
	 */
	public PieceOfWork[] getPiecesOfWork (){
		return _pow;
	}
	
	/**
	 * Ritorna una stringa che rappresenta questo evento.
	 *
	 * @return la rappresentazione in formato stringa per questo evento.
	 */
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		
		sb.append (getClass ().getName ())
		.append (" type ").append (_type);
		
		if (_pow!=null) {
			sb.append (" progresses [ ");
			if (_pow==ALL) {
				sb.append ("ALL ADVANCING PROGRESSES ");
			} else {
				for (final PieceOfWork p : _pow) {
					sb.append (p).append (" ");
				}
			}
			sb.append ("]");
		}
		
		return sb.toString ();
	}
	
}

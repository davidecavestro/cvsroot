/*
 * TaskTreeModelEvent.java
 *
 * Created on November 8, 2006, 11:28 PM
 *
 */

package com.davidecavestro.timekeeper.model.event;

import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.davidecavestro.timekeeper.model.Task;
import com.davidecavestro.timekeeper.model.TaskTreePath;

/**
 * Incapsula le informazioni relative ad un evento di modifica dell'albero degli avanzamenti.
 *<P>
 *
 * @see javax.swing.event.TreeModelEvent
 *
 * @author Davide Cavestro
 */
public class TaskTreeModelEvent extends java.util.EventObject {

	/**
	 * Evento su task.
	 */
	public final static int TASK = 0;
	
	/**
	 * Evento su avanzamenti.
	 */
	public final static int PROGRESS = 1;
	
	/**
	 * Tipo di evento: {@link #TASK} o {@link #PROGRESS}.
	 */
	private final int _type;
	
	/**
	 * Percorso che porta all'elemento superiore agli elementi interessati dall'evento.
	 */
	private final TaskTreePath _parentElementPath;
	
	/**
	 * Indici che indentificano la posizione in cui erano i figli.
	 */
	private final int[] _childIndices;
	
	/**
	 * Elementi rimossi.
	 */
	private final Task[] _children;
	
	/**
	 * Elementi rimossi.
	 */
	private final PieceOfWork[] _progresses;
	
	/**
	 * Array di task vuoto.
	 */
	private final static Task[] _voidTaskArray = new Task[0];

	/**
	 * Array di int vuoto.
	 */
	private final static int[] _voidIntArray = new int[0];
	
	/**
	 * Costruttore per un evento sui TASK.
	 *
	 * @param source l'oggetto responsabile della generazione di questo evento.
	 * @param parentElementPath identifica il percorso che porta all'elemento superiore a quelli interessati dall'evento
	 * @param childIndices an array of <code>int</code> that specifies the
     *               index values of the removed items. The indices must be
     *               in sorted order, from lowest to highest
	 * @param children an array of Object containing the inserted, removed, or
     *                 changed objects
	 */
	public TaskTreeModelEvent (final Object source, final TaskTreePath parentElementPath, final int[] childIndices, final Task[] children) {
		super (source);
		_parentElementPath = parentElementPath;
		_childIndices = childIndices;
		_children = children;
		_progresses = null;
		
		_type = TASK;
	}

	/**
	 * Costruttore per un evento sui PROGRESS.
	 *
	 * @param source l'oggetto responsabile della generazione di questo evento.
	 * @param parentElementPath identifica il percorso che porta all'elemento superiore a quelli interessati dall'evento
	 * @param childIndices an array of <code>int</code> that specifies the
     *               index values of the removed items. The indices must be
     *               in sorted order, from lowest to highest
	 * @param children an array of Object containing the inserted, removed, or
     *                 changed objects
	 */
	public TaskTreeModelEvent (final Object source, final TaskTreePath parentElementPath, final int[] childIndices, final PieceOfWork[] progresses) {
		super (source);
		_parentElementPath = parentElementPath;
		_childIndices = childIndices;
		_children = null;
		_progresses = progresses;
		
		_type = PROGRESS;
	}
	
	/**
	 * Costruttore usato in caso di variazioni della struttura del sottolabero.
	 * <P>
	 * Costituisce un evento di tipo TASK.
	 * 
	 * @param source 'oggetto responsabile della generazione di questo evento.
	 * @param parentElementPath il percorso che porta al sottoalbero che ha subito modifiche alla struttura,
	 */
	public TaskTreeModelEvent (final Object source, final TaskTreePath parentElementPath) {
		super (source);
		_parentElementPath = parentElementPath;
		_childIndices = _voidIntArray;
		_children = _voidTaskArray;
		_progresses = null;
		
		_type = TASK;
	}
	
	/**
	 * Ritorna il percorso che porta all'elemento superiore agli elementi interessati dall'evento.
	 * 
	 * @return il percorso che porta all'elemento superiore agli elementi interessati dall'evento.
	 */
	public TaskTreePath getPath (){
		return _parentElementPath;
	}
	
	/**
	 * Ritorna il tipo di questo evento.
	 * <P>
	 * Vale {@link #TASK} per unevento relativo a <TT>Task</TT>, oppure {@link #PROGRESS} per evento dedicato agli avanzamenti
	 *<BR>
	 * @return il tipo di questo evento.
	 */
	public int getType (){return _type;}
	
	/**
	 * Ritorna una stringa che rappresenta questo evento.
	 *
	 * @return la rappresentazione in formato stringa per questo evento.
	 */
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		
		sb.append (getClass ().getName ())
		.append (" path ").append (_parentElementPath)
		.append (" type ").append (_type)
		.append (" indices ").append (_childIndices);
		
		if (_children!=null) {
			sb.append (" children [ ");
			for (final Task task : _children) {
				sb.append (task).append (" ");
			}
			sb.append ("]");
		}
		
		if (_progresses!=null) {
			sb.append (" progresses [ ");
			for (final PieceOfWork progress : _progresses) {
				sb.append (progress).append (" ");
			}
			sb.append ("]");
		}
		
		return sb.toString ();
	}
	
	/**
	 * Consente la visita da parte dell'agente di sispezione.,
	 * 
	 * @param i l'agente di ispezione.
	 * @return l'esito della visita dell'agente.
	 */
	public Object allow (final Inspector i) {
		switch (_type) {
			case TASK:
				return i.inspectTASK (source, _type, _parentElementPath, _childIndices, _children);
			case PROGRESS:
				return i.inspectPROGRESS (source, _type, _parentElementPath, _childIndices, _progresses);
		}
		/*
		 * Caso non previsto
		 */
		return null;
	}
	
	/**
	 * Interfaccia per l'ispezione e l'elaborazione dell'evento in funzione del tipo.
	 * Si tratta in sostanza di un Visitor pattern.
	 */
	public interface Inspector {
		/**
		 * Ispeziona i dati di un evento di tipo <TT>TASK</TT>.
		 */
		Object inspectTASK (Object source, int type, TaskTreePath path, int[] childIndices, Task[] children);
		/**
		 * Ispeziona i dati di un evento di tipo <TT>PROGRESS</TT>.
		 */
		Object inspectPROGRESS (Object source, int type, TaskTreePath path, int[] childIndices, PieceOfWork[] progresses);
	}
}

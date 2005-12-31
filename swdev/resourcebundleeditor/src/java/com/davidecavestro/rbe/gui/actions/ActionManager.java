/*
 * ActionManager.java
 *
 * Created on 31 dicembre 2005, 11.11
 */

package com.davidecavestro.rbe.gui.actions;

import com.davidecavestro.rbe.ApplicationContext;

/**
 * Gestore delle Action.
 *
 * @author  davide
 */
public class ActionManager {
	
//	private final ApplicationContext _context;
	
	/** Costruttore. */
	public ActionManager () {
//		this._context = context;
	}
	
	private FindNextAction _findNextAction;
	public FindNextAction getFindNextAction (){
		if (null==this._findNextAction){
			this._findNextAction = new FindNextAction ();
		}
		return this._findNextAction;
	}
}

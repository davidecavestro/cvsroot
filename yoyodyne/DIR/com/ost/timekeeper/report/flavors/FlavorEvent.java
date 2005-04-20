/*
 * FlavorEvent.java
 *
 * Created on 4 aprile 2005, 22.53
 */

package com.ost.timekeeper.report.flavors;

/**
 * Evento relativo al modello di tipo di report.
 *
 * @author  davide
 */
public final class FlavorEvent extends java.util.EventObject {
	
	private final FlavorModel _flavorModel;
	
	/**
	 * Costruttore con modello.
	 *
	 * @param flavorModel il modello.
	 */
	public FlavorEvent (final FlavorModel flavorModel){
		super (flavorModel);
		this._flavorModel=flavorModel;
	}
	
	public String getFlavor (){
		return this._flavorModel.getFlavor ();
	}


}

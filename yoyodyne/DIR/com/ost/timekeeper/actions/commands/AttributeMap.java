/*
 * AttributeMap.java
 *
 * Created on 29 dicembre 2004, 12.09
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.actions.commands.attributes.*;
import com.ost.timekeeper.actions.commands.attributes.keys.*;
import java.util.*;

/**
 * Mappa di attrributi tipizzati.
 *
 * @author  davide
 */
public final class AttributeMap {
	
	/**
	 * La mappa degli attributi
	 */
	private final Map _hashMap = new HashMap ();
	
	/**
	 * Costruttore vuoto.
	 */
	public AttributeMap () {}
	
	/**
	 * Costruttore con attributi.
	 *
	 * @param attributes gli attributi con cui popolare questa mappa.
	 */
	public AttributeMap (final Attribute[] attributes) {
		putAttributes (attributes);
	}
	
	/**
	 * Inserisce gli attributi specificati in questa mappa. Ritorna l'array dei valori precedentemente impostati per le chiavi
	 * dei nuovi attributi impostati.
	 *
	 * @param attributes gli attributi da inserire.
	 * @return l'array dei valori precedentemente impostati per le chiavi
	 * dei nuovi attributi impostati.
	 */	
	public Attribute[] putAttributes (Attribute[] attributes){
		final Attribute[] retValue = new Attribute[attributes.length];
		
		for (int i = 0;i< attributes.length;i++){
			final Attribute attribute = (Attribute)attributes[i];
			retValue[i] = (Attribute)_hashMap.put (attribute.getKey (), attribute);
		}
		return retValue;
	}
	
	/**
	 * Rimuove gli attributi mappati dalle chiavi specificate da questa mappa. Ritorna gli attributi rimossi.
	 *
	 * @param attributes gli attributi.
	 * @return l'array degli attributi rimossi.
	 */	
	public Attribute[] removeAttributes (Key[] keys){
		final Attribute[] retValue = new Attribute[keys.length];
		
		for (int i = 0;i< keys.length;i++){
			final Key key = (Key)keys[i];
			retValue[i] = (Attribute)_hashMap.remove (key);
		}
		return retValue;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questa mappa contiene un entry per la chiave specificata.
	 *
	 * @param key la chiave.
	 * @return <TT>true</TT> se questa mappa contiene un entry per la chiave specificata.
	 */	
	public boolean containsKey (Key key){
		return _hashMap.containsKey (key);
	}
	
	/**
	 * Ritorna un attributo a partire dalla chiave specificata.
	 *
	 * @param key la chiave.
	 * @return l'attributo mappato tramite la chiave specificata.
	 */	
	public StringAttribute getAttribute (StringKey key){
		return (StringAttribute)this._hashMap.get (key);
	}

	/**
	 * Ritorna un attributo a partire dalla chiave specificata.
	 *
	 * @param key la chiave.
	 * @return l'attributo mappato tramite la chiave specificata.
	 */	
	public IntegerAttribute getAttribute (IntegerKey key){
		return (IntegerAttribute)this._hashMap.get (key);
	}
	
	/**
	 * Ritorna un attributo a partire dalla chiave specificata.
	 *
	 * @param key la chiave.
	 * @return l'attributo mappato tramite la chiave specificata.
	 */	
	public DoubleAttribute getAttribute (DoubleKey key){
		return (DoubleAttribute)this._hashMap.get (key);
	}
	
	/**
	 * Ritorna un attributo a partire dalla chiave specificata.
	 *
	 * @param key la chiave.
	 * @return l'attributo mappato tramite la chiave specificata.
	 */	
	public DateAttribute getAttribute (DateKey key){
		return (DateAttribute)this._hashMap.get (key);
	}
	
	/**
	 * Ritorna una rappresentazione in formato stringa di questa mappa.
	 *
	 * @return una rappresentazione in formato stringa di questa mappa.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (this._hashMap);
		return sb.toString ();
	}
}

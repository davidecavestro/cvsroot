/*
 * Filter.java
 *
 * Created on 30 gennaio 2005, 10.13
 */

package com.davidecavestro.timekeeper.report.filter;

/**
 * Filtro per la logica di estrazione dei dati.
 *
 * @author  davide
 */
public interface Filter {
	
	public boolean matches (final Object obj);
}

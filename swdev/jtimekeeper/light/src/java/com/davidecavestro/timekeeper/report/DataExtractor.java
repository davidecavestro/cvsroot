/*
 * DataEngine.java
 *
 * Created on 30 gennaio 2005, 10.02
 */

package com.davidecavestro.timekeeper.report;

import java.util.Collection;

/**
 * Estrae i dati per la generazione di un report. Implementa la logica 
 * specifica di estrazione dei dati di un determinato report.
 *
 * @author  davide
 */
public interface DataExtractor {
	
	public Collection extract ();
}

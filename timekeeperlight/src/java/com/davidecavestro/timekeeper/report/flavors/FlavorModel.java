/*
 * FlavorModel.java
 *
 * Created on 4 aprile 2005, 22.52
 */

package com.davidecavestro.timekeeper.report.flavors;

/**
 * Modello del tipo di report.
 *
 * @author  davide
 */
public interface FlavorModel {
	/**
	 * Tipo di report che mappa {@link com.ost.timekeeper.report.flavors.SimpleProgresses}.
	 */
	public final static String SIMPLE_PROGRESSES = "simple_progresses";
	
	/**
	 * Tipo di report che mappa {@link com.ost.timekeeper.report.flavors.CumulateProgresses}.
	 */
	public final static String CUMULATE_PROGRESSES = "cumulate_progresses";
	
	/**
	 * Tipo di report che mappa {@link com.ost.timekeeper.report.flavors.CumulateLocalProgresses}.
	 */
	public final static String CUMULATE_LOCAL_PROGRESSES = "cumulate_local_progresses";
	
	String getFlavor ();

}

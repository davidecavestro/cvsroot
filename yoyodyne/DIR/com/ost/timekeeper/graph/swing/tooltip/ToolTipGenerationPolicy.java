/*
 * ToolTipGenerationPolicy.java
 *
 * Created on 4 marzo 2005, 9.31
 */

package com.ost.timekeeper.graph.swing.tooltip;

import com.ost.timekeeper.graph.awt.SerieNode;

/**
 * Politica di generazione del messaggio di tooltip per un nodo della serie.
 *
 * @author  davide
 */
public interface ToolTipGenerationPolicy {
	String generate (final SerieNode node);
}

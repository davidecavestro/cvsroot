/*
 * SerieNodeTooltipSupplier.java
 *
 * Created on 4 marzo 2005, 9.26
 */

package com.ost.timekeeper.graph.swing.tooltip;

import com.ost.timekeeper.graph.awt.SerieNode;
import com.ost.timekeeper.util.Duration;
import java.text.DecimalFormat;

/**
 * Genera il messaggio di ToolTip per un nodo della serie
 *
 * @author  davide
 */
public final class SerieNodeToolTipSupplier {
	private final static DefaultPolicy _defaultPolicy = new DefaultPolicy ();
	/** Costruttore privato, evita istanziazione. */
	private SerieNodeToolTipSupplier () {
	}
	
	/**
	 * Ritorna il messaggio di ToolTip per il nodo specificato.
	 *
	 * @param node il nodo.
	 * @return il messaggio di ToolTip per il nodo specificato.
	 */	
	public static String getToolTip (final SerieNode node){
		return _defaultPolicy.generate (node);
	}
	
	/**
	 * Ritorna il messaggio di ToolTip per il nodo specificato.
	 *
	 * @return il messaggio di ToolTip per il nodo specificato.
	 * @param policy la politica di generazione.
	 * @param node il nodo.
	 */	
	public static String getToolTip (final SerieNode node, ToolTipGenerationPolicy policy){
		return policy.generate (node);
	}
	
	/**
	 * Implementazione interna di default. Fornisce una stringa del tipo:
	 * [NODE NAME] - local HH:MM:SS - total HH:MM:SS
	 */
	private final static class DefaultPolicy implements ToolTipGenerationPolicy {

		private DefaultPolicy (){}
		
		private final DurationNumberFormatter durationNumberFormatter = new DurationNumberFormatter ();

		private static class DurationNumberFormatter extends DecimalFormat {
			public DurationNumberFormatter (){
				this.setMinimumIntegerDigits (2);
			}
		}
	
		
		public String generate (final SerieNode node){
				
			final StringBuffer sb= new StringBuffer ();
			sb.append (node.getName ())
			.append (" - ")
			.append (" local: ").append (formatDuration (new Duration ((long)node.getValue ())))
			.append (" total: ").append (formatDuration (new Duration ((long)node.getTotalValue ())));
			;
			return sb.toString ();
		}
		
		/**
		 * Ritorna la stringa formattata che rappresenta la durata specificata nelformato previsto.
		 *
		 * @param duration la durata.
		 * @return la stringa formattata che rappresenta una durata nelformato previsto.
		 */		
		private String formatDuration (final Duration duration){
			final StringBuffer sb = new StringBuffer ();
			sb.append (durationNumberFormatter.format(duration.getHours()))
			.append (":")
			.append (durationNumberFormatter.format(duration.getMinutes()))
			.append (":")
			.append (durationNumberFormatter.format(duration.getSeconds()));
			return sb.toString ();
		}
	}
}

/*
 * SearchRenderer.java
 *
 * Created on 28 dicembre 2005, 23.10
 */

package com.davidecavestro.rbe.gui.search;

import java.awt.*;
import java.lang.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  davide
 */
public class SearchRenderer implements TableCellRenderer {
	
	final Matcher m;
	final TableCellRenderer delegate;
	final String highlightRGB = "ffff00";
	
	
	/** Costruttore. */
	public SearchRenderer (Matcher m, TableCellRenderer delegate) {
		this.m = m;
		this.delegate = delegate;
	}

	public java.awt.Component getTableCellRendererComponent (javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		final JLabel rubberStamp = (JLabel)delegate.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
		if (m.getHighlight ()){
			if (value instanceof String){
				String sValue = (String)value;
				if (m.match (sValue)){
					final String p = m.getPattern ();

					final StringBuffer sb = new StringBuffer ();
					sb.append ("<HTML>");
					int patternLength = p.length ();
					int fromIdx = 0;
					int matchIdx = fromIdx;
					do {
						matchIdx = sValue.indexOf (p, fromIdx);
						if (-1==matchIdx){
							sb.append (sValue.substring (fromIdx, sValue.length ()));
							break;
						}
						if (-1<matchIdx){
							if (matchIdx>0){
								sb.append (sValue.substring (fromIdx, matchIdx));
							}

							sb.append ("<SPAN STYLE=\"background: #")
							.append (highlightRGB)
							.append ("\">")
							.append (sValue.substring (matchIdx, matchIdx + patternLength))
							.append ("</SPAN>");

							fromIdx = matchIdx + patternLength;

						}
					} while (fromIdx > matchIdx);
					sb.append ("</HTML>");

					rubberStamp.setText (sb.toString ());
				}
			}
		}
		return rubberStamp;
	}
	
}

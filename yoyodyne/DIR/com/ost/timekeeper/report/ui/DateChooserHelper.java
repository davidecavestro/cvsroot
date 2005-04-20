/*
 * DateChooserHelper.java
 *
 * Created on 4 aprile 2005, 22.25
 */

package com.ost.timekeeper.report.ui;

import com.toedter.calendar.JDateChooser;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Clase di utilità per l'editazione delle date.
 *
 * @author  davide
 */
public final class DateChooserHelper implements PropertyChangeListener {
	public void propertyChange (PropertyChangeEvent evt) {
		final String propName = evt.getPropertyName ();
		if (propName!=null && propName.equals ("enabled")){
			final Boolean oldValue = (Boolean)evt.getOldValue ();
			final Boolean newValue = (Boolean)evt.getNewValue ();
			if(oldValue==null || newValue==null || oldValue.booleanValue ()!=newValue.booleanValue ()){
				final JDateChooser dateChooser = (JDateChooser)evt.getSource ();
				/* propaga variazione stato alle componeni */
				dateChooser.getSpinner ().setEnabled (newValue.booleanValue ());
			}
		}
	}
	
}

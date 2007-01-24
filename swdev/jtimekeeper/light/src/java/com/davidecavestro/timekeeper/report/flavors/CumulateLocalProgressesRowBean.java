/*
 * CumulateLocalProgressesRowBean.java
 *
 * Created on January 20, 2007, 7:44 PM
 */

package com.davidecavestro.timekeeper.report.flavors;

import java.beans.*;
import java.io.Serializable;

/**
 * Dati di riga per il report avanzamenti giornalieri con dettaglio.
 * @author Davide Cavestro
 */
public class CumulateLocalProgressesRowBean extends Object implements Serializable {
	
	public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
	
	private String sampleProperty;
	
	private PropertyChangeSupport propertySupport;
	
	public CumulateLocalProgressesRowBean () {
		propertySupport = new PropertyChangeSupport (this);
	}
	
	public String getSampleProperty () {
		return sampleProperty;
	}
	
	public void setSampleProperty (String value) {
		String oldValue = sampleProperty;
		sampleProperty = value;
		propertySupport.firePropertyChange (PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
	}
	
	
	public void addPropertyChangeListener (PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener (listener);
	}
	
	public void removePropertyChangeListener (PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener (listener);
	}

	/**
	 * Holds value of property periodID.
	 */
	private int periodID;

	/**
	 * Getter for property periodID.
	 * @return Value of property periodID.
	 */
	public int getPeriodID () {
		return this.periodID;
	}

	/**
	 * Setter for property periodID.
	 * @param periodID New value of property periodID.
	 */
	public void setPeriodID (int periodID) {
		this.periodID = periodID;
	}

	/**
	 * Holds value of property periodName.
	 */
	private String periodName;

	/**
	 * Getter for property periodName.
	 * @return Value of property periodName.
	 */
	public String getPeriodName () {
		return this.periodName;
	}

	/**
	 * Setter for property periodName.
	 * @param periodName New value of property periodName.
	 */
	public void setPeriodName (String periodName) {
		this.periodName = periodName;
	}

	/**
	 * Holds value of property taskID.
	 */
	private int taskID;

	/**
	 * Getter for property taskID.
	 * @return Value of property taskID.
	 */
	public int getTaskID () {
		return this.taskID;
	}

	/**
	 * Setter for property taskID.
	 * @param taskID New value of property taskID.
	 */
	public void setTaskID (int taskID) {
		this.taskID = taskID;
	}

	/**
	 * Holds value of property taskName.
	 */
	private String taskName;

	/**
	 * Getter for property taskName.
	 * @return Value of property taskName.
	 */
	public String getTaskName () {
		return this.taskName;
	}

	/**
	 * Setter for property taskName.
	 * @param taskName New value of property taskName.
	 */
	public void setTaskName (String taskName) {
		this.taskName = taskName;
	}

	/**
	 * Holds value of property taskHierarchy.
	 */
	private String taskHierarchy;

	/**
	 * Getter for property taskHierarchy.
	 * @return Value of property taskHierarchy.
	 */
	public String getTaskHierarchy () {
		return this.taskHierarchy;
	}

	/**
	 * Setter for property taskHierarchy.
	 * @param taskHierarchy New value of property taskHierarchy.
	 */
	public void setTaskHierarchy (String taskHierarchy) {
		this.taskHierarchy = taskHierarchy;
	}

	/**
	 * Holds value of property periodTotalEffort.
	 */
	private java.sql.Time periodTotalEffort;

	/**
	 * Getter for property periodTotalEffort.
	 * @return Value of property periodTotalEffort.
	 */
	public java.sql.Time getPeriodTotalEffort () {
		return this.periodTotalEffort;
	}

	/**
	 * Setter for property periodTotalEffort.
	 * @param periodTotalEffort New value of property periodTotalEffort.
	 */
	public void setPeriodTotalEffort (java.sql.Time periodTotalEffort) {
		this.periodTotalEffort = periodTotalEffort;
	}

	/**
	 * Holds value of property taskTotalEffort.
	 */
	private java.sql.Time taskTotalEffort;

	/**
	 * Getter for property taskTotalEffort.
	 * @return Value of property taskTotalEffort.
	 */
	public java.sql.Time getTaskTotalEffort () {
		return this.taskTotalEffort;
	}

	/**
	 * Setter for property taskTotalEffort.
	 * @param taskTotalEffort New value of property taskTotalEffort.
	 */
	public void setTaskTotalEffort (java.sql.Time taskTotalEffort) {
		this.taskTotalEffort = taskTotalEffort;
	}

	/**
	 * Holds value of property progressEffort.
	 */
	private java.sql.Time progressEffort;

	/**
	 * Getter for property progressEffort.
	 * @return Value of property progressEffort.
	 */
	public java.sql.Time getProgressEffort () {
		return this.progressEffort;
	}

	/**
	 * Setter for property progressEffort.
	 * @param progressEffort New value of property progressEffort.
	 */
	public void setProgressEffort (java.sql.Time progressEffort) {
		this.progressEffort = progressEffort;
	}

	/**
	 * Holds value of property progressNotes.
	 */
	private String progressNotes;

	/**
	 * Getter for property progressNotes.
	 * @return Value of property progressNotes.
	 */
	public String getProgressNotes () {
		return this.progressNotes;
	}

	/**
	 * Setter for property progressNotes.
	 * @param progressNotes New value of property progressNotes.
	 */
	public void setProgressNotes (String progressNotes) {
		this.progressNotes = progressNotes;
	}

	/**
	 * Holds value of property progressStart.
	 */
	private java.sql.Timestamp progressStart;

	/**
	 * Getter for property progressStart.
	 * @return Value of property progressStart.
	 */
	public java.sql.Timestamp getProgressStart () {
		return this.progressStart;
	}

	/**
	 * Setter for property progressStart.
	 * @param progressStart New value of property progressStart.
	 */
	public void setProgressStart (java.sql.Timestamp progressStart) {
		this.progressStart = progressStart;
	}

	/**
	 * Holds value of property progressEnd.
	 */
	private java.sql.Timestamp progressEnd;

	/**
	 * Getter for property progressEnd.
	 * @return Value of property progressEnd.
	 */
	public java.sql.Timestamp getProgressEnd () {
		return this.progressEnd;
	}

	/**
	 * Setter for property progressEnd.
	 * @param progressEnd New value of property progressEnd.
	 */
	public void setProgressEnd (java.sql.Timestamp progressEnd) {
		this.progressEnd = progressEnd;
	}

	/**
	 * Holds value of property periodStart.
	 */
	private java.sql.Timestamp periodStart;

	/**
	 * Getter for property periodStart.
	 * @return Value of property periodStart.
	 */
	public java.sql.Timestamp getPeriodStart () {
		return this.periodStart;
	}

	/**
	 * Setter for property periodStart.
	 * @param periodStart New value of property periodStart.
	 */
	public void setPeriodStart (java.sql.Timestamp periodStart) {
		this.periodStart = periodStart;
	}

	/**
	 * Holds value of property progressDescription.
	 */
	private String progressDescription;

	/**
	 * Getter for property progressDescription.
	 * @return Value of property progressDescription.
	 */
	public String getProgressDescription () {
		return this.progressDescription;
	}

	/**
	 * Setter for property progressDescription.
	 * @param progressDescription New value of property progressDescription.
	 */
	public void setProgressDescription (String progressDescription) {
		this.progressDescription = progressDescription;
	}


	
}

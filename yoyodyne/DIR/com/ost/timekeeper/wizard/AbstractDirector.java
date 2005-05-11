/*
 * AbstractDirector.java
 *
 * Created on 25 aprile 2005, 10.56
 */

package com.ost.timekeeper.wizard;

import java.util.Arrays;
import java.util.Iterator;
import javax.swing.event.*;

/**
 * Il coordinatore dei passi della procedura guidata.
 *
 * @author  davide
 */
public abstract class AbstractDirector extends StepChangeNotifierImpl implements Director, StepChangeListener  {
	
	private Step[] _steps;
	
	private int _currentIDX=0;
	
	/**
	 * Costruttore.
	 *
	 */
	public AbstractDirector (final Step[] steps){
		final int stepsCount = steps.length;
		this._steps = new Step[stepsCount];
		System.arraycopy (steps, 0, this._steps, 0, stepsCount);
	}
	
	/**
	 * Costruttore.
	 *
	 */
	protected AbstractDirector (){
		this._steps = new Step[0];
	}
	
	protected void init (final Step[] steps){
		if (this._steps!=null){
			for (int i=0;i<this._steps.length;i++){
				this._steps[i].removeStepChangeListener (this);
			}
		}
		final int stepsCount = steps.length;
		this._steps = new Step[stepsCount];
		System.arraycopy (steps, 0, this._steps, 0, stepsCount);
		for (int i=0;i<this._steps.length;i++){
			this._steps[i].addStepChangeListener (this);
		}
		if (this._steps.length>0){
			this._steps[0].setCurrent (true);
		}
	}
	
	/**
	 * Ritorna il passo corrente.
	 *
	 * @return il passo corrente.
	 */	
	public Step current (){
		return this._steps[this._currentIDX];
	}
	
	/**
	 * Ritorna il passo successivo.
	 *
	 * @return il passo successivo.
	 */	
	public Step next (){
		final Step previousStep = this._steps[this._currentIDX];
		previousStep.apply ();
		previousStep.setCurrent (false);
		final Step nextStep = this._steps[++this._currentIDX];
		nextStep.configure ();
		nextStep.setCurrent (true);
		fireStepChanged ();
		return nextStep;
	}
	
	/**
	 * Ritorna il passo precedente.
	 *
	 * @return il passo precedente.
	 */	
	public Step previous (){
		final Step currentStep = this._steps[this._currentIDX];
		currentStep.abort ();
		currentStep.setCurrent (false);
		this._currentIDX--;
		fireStepChanged ();
		final Step newCurrentStep = this._steps[this._currentIDX];
		newCurrentStep.setCurrent (true);
		return newCurrentStep;
	}
	
	/**
	 * Ritorna <TT>true<TT> ed applica le modifiche del passo corrente, se è valido.
	 * @return <TT>true</TT> se il passo corrente è valido.
	 */
	public boolean finish () {
		final Step currentStep = this.current ();
		if (currentStep.isValid ()){
			currentStep.apply ();
			currentStep.setCurrent (false);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Implementazione vuota.
	 */
	public void cancel () {
	}
	
	/**
	 * Ritorna <TT>true</TT> se e' disponibile un passo precedente.
	 *
	 * @return <TT>true</TT> se e' disponibile un passo precedente.
	 */	
	public boolean hasNext (){
		return this._steps.length > this._currentIDX+1;
	}
	/**
	 * Ritorna <TT>true</TT> se e' disponibile un passo successivo.
	 *
	 * @return <TT>true</TT> se e' disponibile un passo successivo.
	 */	
	public boolean hasPrevious (){
		return this._currentIDX > 0;
	}
	
	/**
	 * Viene notificato il cambiamento di passo. 
	 * @param sce l'evento 
	 */
	public void stepChanged (com.ost.timekeeper.wizard.StepChangeEvent stepChangeEvent) {
		this.fireStepChanged ();
	}	
	
	/**
	 * Ritorna l'iteratore sui passi.
	 *
	 * @return l'iteratore sui passi.
	 */	
	public Iterator iterateSteps (){
		return Arrays.asList (this._steps).iterator ();
	}
	
}

/*
 * Dialog.java
 *
 * Created on 25 aprile 2005, 10.27
 */

package com.ost.timekeeper.wizard;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.help.HelpManager;
import com.ost.timekeeper.help.HelpResource;
import com.ost.timekeeper.help.HelpResourcesResolver;
import com.ost.timekeeper.ui.support.GradientPanel;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.help.CSH;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

/**
 * Dialogo di interfaccia della procedura guidata.
 *
 * @author  davide
 */
public class Dialog extends JDialog{
	
	private final Director _director;
	
	private final JScrollPane _stepPanelContainer;
	
	/**
	 * Costruttore.
	 * @param title iltitolo della finestra.
	 * @param director il coordinator dei passi.
	 */
	public Dialog (final Director director, final String title) {
		super (Application.getInstance ().getMainForm (), title, true);
		this._director = director;
		this._stepPanelContainer = new JScrollPane ();
		initComponents ();
//		pack ();
		setSize (640, 480);
		setResizable (true);
		/* centra sullo schermo */
		setLocationRelativeTo (null);
	}
	
	private void initComponents (){
		final JPanel mainPanel = new JPanel (new BorderLayout ());
		final JPanel buttonPanel = new JPanel (new GridBagLayout ());
		
		final GridBagConstraints c = new GridBagConstraints ();
		c.insets = new Insets (3, 10, 3, 10);
		c.gridy = 0;
		
		c.gridx = 0;
		c.weightx = 1.0;
		buttonPanel.add (new JLabel (), c);
		
		c.weightx = 0.0;
		{
			final JButton previousButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "Previous"));
			previousButton.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					onPrevious (ae);
				}
			});
			previousButton.setEnabled (_director.hasPrevious ());
			this._director.addStepChangeListener (new StepChangeListener (){
				public void stepChanged (com.ost.timekeeper.wizard.StepChangeEvent stepChangeEvent) {
					previousButton.setEnabled (_director.hasPrevious ());
				}
			});

			c.gridx = 1;
			buttonPanel.add (previousButton, c);
		}
		
		{
			final JButton nextButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "Next"));
			nextButton.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					onNext (ae);
				}
			});
			nextButton.setEnabled (_director.hasNext ());
			this._director.addStepChangeListener (new StepChangeListener (){
				public void stepChanged (com.ost.timekeeper.wizard.StepChangeEvent stepChangeEvent) {
					nextButton.setEnabled (_director.hasNext () && _director.current ().isValid ());
				}
			});
			
			c.gridx = 2;
			buttonPanel.add (nextButton, c);
		}
		
		{
			final JButton finishButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "Finish"));
			finishButton.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					onFinish (ae);
				}
			});
			
			this._director.addStepChangeListener (new StepChangeListener (){
				public void stepChanged (com.ost.timekeeper.wizard.StepChangeEvent stepChangeEvent) {
					finishButton.setEnabled (_director.canFinish () && _director.current ().isValid ());
				}
			});
			
			c.gridx = 3;
			buttonPanel.add (finishButton, c);
		}
		
		{
			final JButton cancelButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "Cancel"));
			cancelButton.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					onCancel (ae);
				}
			});
			
			c.gridx = 4;
			buttonPanel.add (cancelButton, c);
		}
		
		{
			final JButton helpButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "Help"));
			javax.help.CSH.setHelpIDString (this, HelpResourcesResolver.getInstance ().resolveHelpID (HelpResource.DATASTORECONFIGURATIONWIZARD));
			helpButton.addActionListener (new CSH.DisplayHelpFromSource(HelpManager.getInstance ().getMainHelpBroker ()));
			
			helpButton.addActionListener (new ActionListener (){
				public void actionPerformed (ActionEvent ae){
					onHelp (ae);
				}
			});
			
			c.gridx = 5;
			buttonPanel.add (helpButton, c);
		}
		
		mainPanel.add (buttonPanel, BorderLayout.SOUTH);
		_stepPanelContainer.setBorder (new CompoundBorder (new BevelBorder (BevelBorder.LOWERED), new BevelBorder (BevelBorder.RAISED)));
		mainPanel.add (_stepPanelContainer, BorderLayout.CENTER);
		
		final Color gradientColor = UIManager.getColor("InternalFrame.activeTitleBackground");
		final JPanel iconPanel = new GradientPanel (gradientColor!=null?gradientColor:Color.LIGHT_GRAY, GradientPanel.OBLIQUE);
		iconPanel.setLayout (new BoxLayout (iconPanel, BoxLayout.Y_AXIS));
//		iconPanel.setBackground (gradientColor!=null?gradientColor:Color.LIGHT_GRAY);
		mainPanel.add (iconPanel, BorderLayout.WEST);
		iconPanel.add (new JLabel (ResourceSupplier.getImageIcon (ResourceClass.UI, "mandrake.png")));
		iconPanel.add (new JLabel (" "));
		for (final Iterator it = _director.iterateSteps (); it.hasNext ();){
			iconPanel.add (new JLabel (" "));
			final Step step = (Step)it.next ();
			final JLabel iconLabel = new JLabel (step.getIcon ());
			if (step.isCurrent ()){
				iconLabel.enable ();
			} else {
				iconLabel.disable ();
			}
			iconPanel.add (iconLabel);
			step.addStepChangeListener (new StepChangeAdapter (){
				public void stepChanged (com.ost.timekeeper.wizard.StepChangeEvent stepChangeEvent) {
					if (step.isCurrent ()){
						iconLabel.enable ();
					} else {
						iconLabel.disable ();
					}
					iconLabel.repaint ();
				}
			});
		}
		removeOldUI ();
		this._stepPanelContainer.getViewport ().add (this._director.current ().getUI ());
		this._stepPanelContainer.repaint ();

		this.getContentPane ().add (mainPanel);
	}
	
	/**
	 * Rimuove l'interfaccia grafica precedente.
	 */
	private void removeOldUI (){
		if (this._stepPanelContainer.getViewport ().getComponents ().length>0){
			this._stepPanelContainer.getViewport ().remove (0);
		}
	}
	
	/**
	 * Gestione evento pressione pulsante INDIETRO.
	 *
	 * @param ae l'evento
	 */	
	private void onPrevious (ActionEvent ae){
		removeOldUI ();
		this._stepPanelContainer.getViewport ().add (this._director.previous ().getUI ());
		this._stepPanelContainer.repaint ();
	}
	
	/**
	 * Gestione evento pressione pulsante SUCCESSIVO.
	 *
	 * @param ae l'evento
	 */	
	private void onNext (ActionEvent ae){
		removeOldUI ();
		this._stepPanelContainer.getViewport ().add (this._director.next ().getUI ());
		this._stepPanelContainer.repaint ();
	}
	
	/**
	 * Gestione evento pressione pulsante FINE.
	 *
	 * @param ae l'evento
	 */	
	private void onFinish (ActionEvent ae){
		this._director.finish ();
		this.hide ();
		this.dispose ();
	}
	
	/**
	 * Gestione evento pressione pulsante ANNULLA.
	 *
	 * @param ae l'evento
	 */	
	private void onCancel (ActionEvent ae){
		this._director.cancel ();
		this.hide ();
		this.dispose ();
	}
	
	/**
	 * Gestione evento pressione pulsante HELP.
	 *
	 * @param ae l'evento
	 */	
	private void onHelp (ActionEvent ae){
		
	}
}

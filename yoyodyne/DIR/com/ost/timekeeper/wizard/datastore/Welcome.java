/*
 * Welcome.java
 *
 * Created on 27 aprile 2005, 23.14
 */

package com.ost.timekeeper.wizard.datastore;

import com.ost.timekeeper.wizard.AbstractStep;
import com.ost.timekeeper.wizard.Director;
import com.ost.timekeeper.wizard.Step;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Passo di introduzione alla procedura.
 *
 * @author  davide
 */
public class Welcome extends AbstractStep {
	
	/** Costruttore. 
	 * @param director il coordinatore.
	 */
	public Welcome (final Director director) {
		super (director);
	}
	
	public void abort () {
	}
	
	public void apply () {
	}
	
	public void configure () {
	}
	
	public java.awt.Component getUI () {
		final JPanel controlsPanel = new JPanel (new BorderLayout ());
		final JLabel welcomeLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "Press.next.to.continue"));
		controlsPanel.add (welcomeLabel, BorderLayout.NORTH );
		
		final JEditorPane helpPane = new JEditorPane ("text/html", ResourceSupplier.getString (ResourceClass.UI, "controls", "datastore.config.wizard.welcome.HTML"));
		helpPane.setBackground (controlsPanel.getBackground ());
		helpPane.setEditable (false);
		helpPane.setFocusable (false);
		
		final JPanel mainPanel = new JPanel (new BorderLayout ());
		mainPanel.add (helpPane, BorderLayout.NORTH);
		mainPanel.add (controlsPanel, BorderLayout.CENTER);
		return mainPanel;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questo passo è valido.
	 * @return <TT>true</TT> se questo passo è valido.
	 */
	public boolean isValid (){
		return true;
	}
	
	/**
	 * Ritorna l'icona rappresentativa di questo passo.
	 *
	 * @return l'icona rappresentativa di questo passo.
	 */	
	public Icon getIcon (){
		return ResourceSupplier.getImageIcon (ResourceClass.UI, "welcome-huge.png");
	}
	
}

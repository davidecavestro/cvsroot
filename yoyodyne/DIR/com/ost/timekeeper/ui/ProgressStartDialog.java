/*
 * ProgressStartDialog.java
 *
 * Created on 17 aprile 2005, 23.38
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.actions.commands.AttributeMap;
import com.ost.timekeeper.actions.commands.attributes.DateAttribute;
import com.ost.timekeeper.actions.commands.attributes.StringAttribute;
import com.ost.timekeeper.actions.commands.attributes.keys.DateKey;
import com.ost.timekeeper.actions.commands.attributes.keys.StringKey;
import com.ost.timekeeper.util.Duration;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import com.toedter.components.JSpinField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * Finestra di inserimento dati per nuovo avanzamento.
 *
 * @author  davide
 */
public class ProgressStartDialog extends JDialog{
	
	public final static DateKey FROM = new DateKey ("from");
	public final static StringKey DESCRIPTION = new StringKey ("description");
	public final static StringKey NOTES = new StringKey ("notes");
	
	private boolean _confirmed = false;
	
	private static ProgressStartDialog _instance;
	
	private final JSpinField durationHourEditor = new JSpinField(0, 99);
	private final JSpinField durationMinEditor = new JSpinField(0,59);
	private final JSpinField durationSecsEditor = new JSpinField(0, 59);

	private final JTextArea descriptionEditor = new JTextArea ();
	private final JTextArea notesEditor = new JTextArea ();
	
	/** Costruttore. */
	private ProgressStartDialog () {
		super (Application.getInstance ().getMainForm (), ResourceSupplier.getString (ResourceClass.UI, "controls", "Progress.start.options"), true);
		initComponents ();
		pack ();
		/*
		 * Centra sullo schermo.
		 */
		this.setLocationRelativeTo (null);		
	}
	
	/**
	 * Inizializza i controlli.
	 */
	private void initComponents (){
		final JPanel mainPanel = new JPanel (new GridBagLayout ());
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
		
		durationHourEditor.adjustWidthToMaximumValue ();
		durationMinEditor.adjustWidthToMaximumValue ();
		durationSecsEditor.adjustWidthToMaximumValue ();
		
		final JPanel editPanel = new JPanel (new GridBagLayout ());
		final JPanel durationEditorPanel = new JPanel (new GridBagLayout ());
		{
			final GridBagConstraints c1 = new GridBagConstraints ();
			c1.fill = GridBagConstraints.BOTH;
			c1.anchor = GridBagConstraints.FIRST_LINE_START;
			c1.insets = new Insets (0, 0, 0, 10);


			c1.gridx = 0;
			c1.gridy = 0;
			durationEditorPanel.add (durationHourEditor, c1);

			c1.gridx = 1;
			c1.gridy = 0;
			durationEditorPanel.add (durationMinEditor, c1);
			
			c1.gridx = 2;
			c1.gridy = 0;
			durationEditorPanel.add (durationSecsEditor, c1);
			
			/* filler */
			c1.gridx = 3;
			c1.gridy = 0;
			c1.weightx=1.0;
			durationEditorPanel.add (new JLabel (), c1);
		}
		
		final JLabel durationLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "duration"));
		durationLabel.setLabelFor (durationEditorPanel);
			
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		editPanel.add (durationLabel, c);
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		editPanel.add (durationEditorPanel, c);
		
		final JLabel descriptionLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "description"));
		descriptionLabel.setLabelFor (descriptionEditor);
		descriptionEditor.setMinimumSize (new Dimension (120, 20));
		
		final JLabel notesLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "notes"));
		notesLabel.setLabelFor (notesEditor);
		notesEditor.setMinimumSize (new Dimension (120, 20));
		
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		editPanel.add (descriptionLabel, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 1;
		editPanel.add (new JScrollPane (descriptionEditor), c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		editPanel.add (notesLabel, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 2;
		editPanel.add (new JScrollPane (notesEditor), c);
		
		final JPanel buttonPanel = new JPanel ();
		
		final JButton confirmButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "confirm"));
		confirmButton.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				confirmButtonActionPerformed (evt);
			}
		});
		buttonPanel.add (confirmButton);
		
		final JButton cancelButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "cancel"));
		final Action cancelAction = new javax.swing.AbstractAction ("cancel"){
			public void actionPerformed(ActionEvent e) {
				onCancel ();
			}
		};
		cancelButton.setAction (cancelAction);
		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		cancelButton.getActionMap().put("cancel", cancelAction);
		
		buttonPanel.add (cancelButton);
		
		final DirectHelpButton dhb = new DirectHelpButton ();
		dhb.setRolloverEnabled (true);
		buttonPanel.add (dhb);
		
		getContentPane ().setLayout (new BorderLayout ());
		getContentPane ().add (editPanel, BorderLayout.CENTER);
		getContentPane ().add (buttonPanel, BorderLayout.SOUTH);
		
		getRootPane ().setDefaultButton (confirmButton);
	}
	
	/**
	 * Ritonra l'istanza della finestra.
	 *
	 * @return l'istanza della finestra.
	 */	
	public static ProgressStartDialog getInstance (){
		if (_instance==null){
			_instance = new ProgressStartDialog ();
		}
		return _instance;
	}
	
	/**
	 * Reimposta lo stato iniziale della finestra.
	 */
	private final void reset (){
		this._confirmed = false;
		durationHourEditor.setValue (0);
		durationMinEditor.setValue (0);
		durationSecsEditor.setValue (0);
		
		descriptionEditor.setText ("");
		notesEditor.setText ("");
	}
	
	/**
	 * Richiede e ritorna i dati impostati dall'utente.
	 *
	 * @return i dati impostati dall'utente.
	 */	
	public static AttributeMap getData (){
		
		final ProgressStartDialog instance = getInstance ();
		
		instance.reset ();
		instance.show ();
		if (!instance._confirmed){
			return null;
		} else {
			final AttributeMap attributes = new AttributeMap ();
			final Date startDate = new Date (new Date ().getTime () - new Duration (instance.durationHourEditor.getValue (), instance.durationMinEditor.getValue (), instance.durationSecsEditor.getValue (), 0).getTime ());
			attributes.putAttribute (new DateAttribute (FROM, startDate));
			attributes.putAttribute (new StringAttribute (DESCRIPTION, instance.descriptionEditor.getText ()));
			attributes.putAttribute (new StringAttribute (NOTES, instance.notesEditor.getText ()));
			
			return attributes;
		}
		
	}
	
	private void confirmButtonActionPerformed (java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		this._confirmed = true;
		this.hide ();
	}
	
	private void jButtonCancelActionPerformed (java.awt.event.ActionEvent evt) {
		onCancel ();
	}
	
	private void onCancel () {
		this.hide ();
	}
}

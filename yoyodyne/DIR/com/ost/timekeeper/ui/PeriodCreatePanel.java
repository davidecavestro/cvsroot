/*
 * PeriodCreatePanel.java
 *
 * Created on 16 aprile 2004, 11.30
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.actions.commands.CreateProgress;
import com.ost.timekeeper.actions.commands.attributes.Attribute;
import com.ost.timekeeper.actions.commands.attributes.DateAttribute;
import com.ost.timekeeper.actions.commands.attributes.StringAttribute;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.support.*;
import com.ost.timekeeper.util.*;
import com.toedter.calendar.JDateChooser;
import com.toedter.components.JSpinField;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Pannello di crezione periodo di avanzamento.
 *
 * @author  davide
 */
public final class PeriodCreatePanel extends javax.swing.JPanel implements Observer, KeyListener {
	
	/**********************************************
	 * INIZIO dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Pannello di editazione.
	 */
	private final javax.swing.JPanel editPanel = new javax.swing.JPanel ();
	
	/**
	 * Etichetta supporto editazione data/ora INIZIO.
	 */
	private final javax.swing.JLabel fromLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione data/ora FINE.
	 */
	private final javax.swing.JLabel toLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione DURATA.
	 */
	private final javax.swing.JLabel durationLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione DESCRIZIONE.
	 */
	private final javax.swing.JLabel descriptionLabel = new javax.swing.JLabel ();
	
	/**
	 * Etichetta supporto editazione NOTE.
	 */
	private final javax.swing.JLabel notesLabel = new javax.swing.JLabel ();
	
	private final String dateFormat = Application.getOptions ().getDateTimeFormat ();
	
	/**
	 * Componente editazione INIZIO.
	 */
//	private final javax.swing.JFormattedTextField  fromEditor = new javax.swing.JFormattedTextField  (DateFormat.getDateTimeInstance (DateFormat.DATE_FIELD, DateFormat.LONG));
	private final JDateChooser fromEditor = new JDateChooser (dateFormat, false);
	
	/**
	 * Componente editazione FINE.
	 */
//	private final javax.swing.JFormattedTextField toEditor = new javax.swing.JFormattedTextField  (DateFormat.getDateTimeInstance (DateFormat.DATE_FIELD, DateFormat.MEDIUM));
	private final JDateChooser toEditor = new JDateChooser (dateFormat, false);
	
	/**
	 * Componente editazione ORE durata.
	 */
	private final JSpinField durationHourEditor = new JSpinField(0, 99);
	
	/**
	 * Componente editazione MINUTI durata.
	 */
	private final JSpinField durationMinEditor = new JSpinField(0,59);
	
	/**
	 * Componente editazione SECONDI durata.
	 */
	private final JSpinField durationSecsEditor = new JSpinField(0, 59);
	
	/**
	 * COmponente editazione DESCRIZIONE.
	 */
	private final javax.swing.JTextArea descriptionEditor = new javax.swing.JTextArea (3, 20);
	
	/**
	 * Componente editazione NOTE.
	 */
	private final javax.swing.JTextArea notesEditor = new javax.swing.JTextArea (5, 20);
	
	/**
	 * Pulsantiera.
	 */
	private final javax.swing.JPanel buttonPanel = new javax.swing.JPanel ();
	/**
	 * Pulsante di conferma.
	 */
	private final javax.swing.JButton confirmButton = new javax.swing.JButton ();
	
	/**
	 * Pulsante di annullamento.
	 */
	private final javax.swing.JButton cancelButton = new javax.swing.JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "cancel"));
	/**********************************************
	 * FINE dichiarazione componenti UI interne.
	 **********************************************/
	
	/**
	 * Stato modifica ai dati trattati da questo pannello.
	 */
	private boolean _dataChanged = false;
	
	/**
	 * Costruttore.
	 *
	 */
	public PeriodCreatePanel () {
		/*
		 * Inizializzazione componenti grafiche.
		 */
		initComponents ();
	}
	
	
	/**
	 * Inizializzaizone componenti.
	 */
	private void initComponents () {
		
		this.setLayout (new java.awt.BorderLayout ());
		
		/*
		 * Configurazione pulsante CONFERMA.
		 */
		confirmButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent e){
				pushData ();
				/*@@@ riferiemnto al contenitore, che dovrebbe essere variabile
				 @todo risolvere la dipendenza*/
				NewProgressDialog.getInstance ().hide ();
			}
		});
		confirmButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "confirm"));
		
		cancelButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				/*@@@ riferiemnto al contenitore, che dovrebbe essere variabile
				 @todo risolvere la dipendenza*/
				NewProgressDialog.getInstance ().hide ();
			}
		});
		/*
		 * Configurazione pulsantiera.
		 */
		buttonPanel.setLayout (new java.awt.FlowLayout ());
		
		
		/*
		 * Inserimento pulsante CONFERMA.
		 */
		buttonPanel.add (confirmButton);
		
		/*
		 * Inserimento pulsante ANNULLA.
		 */
		buttonPanel.add (cancelButton);
		
		
		/*
		 * Configurazione pannello editazione.
		 */
		editPanel.setLayout (new java.awt.GridBagLayout ());
		
		
		/*
		 * Configurazione editazione campo INIZIO.
		 */
		fromLabel.setLabelFor (fromEditor);
		fromLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "from"));
		//		fromEditor.setMinimumSize (new Dimension (120, 20));
		fromEditor.addKeyListener (this);
		
		final PropertyChangeListener fromOrToChangeListener = new PropertyChangeListener (){
			public void propertyChange (PropertyChangeEvent evt){
				final Date fromDate = PeriodCreatePanel.this.fromEditor.getDate ();
				final Date toDate = PeriodCreatePanel.this.toEditor.getDate ();
				if (fromDate!=null && toDate!=null){
					PeriodCreatePanel.this.synchDurationControls (new Duration (fromDate,toDate));
				}
				PeriodCreatePanel.this.setDataChanged (true);
			}
		};
		fromEditor.addPropertyChangeListener ("date", fromOrToChangeListener);
		
		
		/*
		 * Configurazione editazione campo FINE.
		 */
		toLabel.setLabelFor (toEditor);
		toLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "to"));
		//		toEditor.setMinimumSize (new Dimension (120, 20));
		toEditor.addKeyListener (this);
		
		toEditor.addPropertyChangeListener ("date", fromOrToChangeListener);
		
		durationHourEditor.adjustWidthToMaximumValue ();
		durationMinEditor.adjustWidthToMaximumValue ();
		durationSecsEditor.adjustWidthToMaximumValue ();
		final JPanel durationEditorPanel = new JPanel (new GridBagLayout ());
		{
			final GridBagConstraints c1 = new GridBagConstraints ();
			c1.fill = GridBagConstraints.BOTH;
			c1.anchor = GridBagConstraints.FIRST_LINE_START;
			c1.insets = new Insets (0, 0, 0, 10);

			durationLabel.setLabelFor (durationEditorPanel);
			durationLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "duration"));

			c1.gridx = 0;
			c1.gridy = 0;
			durationEditorPanel.add (new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "hours"), JLabel.RIGHT), c1);

			c1.gridx = 1;
			c1.gridy = 0;
			durationEditorPanel.add (durationHourEditor, c1);

			c1.gridx = 2;
			c1.gridy = 0;
			durationEditorPanel.add (new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "mins"), JLabel.RIGHT), c1);

			c1.gridx = 3;
			c1.gridy = 0;
			durationEditorPanel.add (durationMinEditor, c1);
			
			c1.gridx = 4;
			c1.gridy = 0;
			durationEditorPanel.add (new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "secs"), JLabel.RIGHT), c1);

			c1.gridx = 5;
			c1.gridy = 0;
			durationEditorPanel.add (durationSecsEditor, c1);
			
			/* filler */
//			c1.gridx = 3;
//			c1.gridy = 0;
//			c1.weightx=1;
//			durationEditorPanel.add (new JLabel (), c1);
		}
		
		
			final PropertyChangeListener durationChangeListener = new PropertyChangeListener (){
				public void propertyChange (PropertyChangeEvent evt){
					if (synchronizingDurationControls){
						/* evita chiamata spuria */
						return;
					}
					PeriodCreatePanel.this.toEditor.setDate (
					new Date (
					PeriodCreatePanel.this.fromEditor.getDate ().getTime ()
					+new Duration (durationHourEditor.getValue (), durationMinEditor.getValue (), durationSecsEditor.getValue (), 0).getTime ()));
				}
			};	
			durationHourEditor.addPropertyChangeListener ("value", durationChangeListener);
			durationMinEditor.addPropertyChangeListener ("value", durationChangeListener);
			durationSecsEditor.addPropertyChangeListener ("value", durationChangeListener);
		
		/*
		 * Configurazione editazione DESCRIZIONE.
		 */
		descriptionLabel.setLabelFor (descriptionEditor);
		descriptionLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "description"));
		descriptionEditor.setMinimumSize (new Dimension (120, 20));
		descriptionEditor.addKeyListener (this);
		
		/*
		 * Configurazione editazione NOTE.
		 */
		notesLabel.setLabelFor (notesEditor);
		notesLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "notes"));
		notesEditor.setMinimumSize (new Dimension (120, 20));
		notesEditor.addKeyListener (this);
		
		final GridBagConstraints c = new GridBagConstraints ();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		editPanel.add (new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "controls", "main")), c);
		
		c.gridwidth = 1;
		/*
		 * Inserimento editazione INIZIO.
		 */
		c.gridx = 0;
		c.gridy = 1;
		editPanel.add (fromLabel, c);
		c.gridx = 1;
		c.gridy = 1;
		editPanel.add (fromEditor, c);
		
		/*
		 * Inserimento editazione FINE.
		 */
		c.gridx = 0;
		c.gridy = 2;
		editPanel.add (toLabel, c);
		c.gridx = 1;
		c.gridy = 2;
		editPanel.add (toEditor, c);

		/*
		 * Editazione DURATA
		 */
		c.gridx = 0;
		c.gridy = 3;
		editPanel.add (durationLabel, c);
		c.gridx = 1;
		c.gridy = 3;
		editPanel.add (durationEditorPanel, c);
//		c.gridx = 2;
//		c.gridy = 3;
//		c.weightx=1;
//		editPanel.add (new JLabel (), c);
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.weightx=1;
		editPanel.add (new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "controls", "secondary")), c);
		
		c.weightx=0;
		c.gridwidth = 1;
		
		/*
		 * Inserimento editazione DESCRIZIONE.
		 */
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 5;
		editPanel.add (descriptionLabel, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 5;
		editPanel.add (new JScrollPane (descriptionEditor,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), c);
		
		/*
		 * Inserimento editazione NOTE.
		 */
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 6;
		editPanel.add (notesLabel, c);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 6;
		editPanel.add (new JScrollPane (notesEditor,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), c);
		
		
		
		//		SpringUtilities.makeCompactGrid(editPanel,
		//                                4, 2, //rows, cols
		//                                6, 6,        //initX, initY
		//                                6, 6);       //xPad, yPad
		
		/*
		 * Inserimento pannello editazione.
		 */
		this.add (editPanel, java.awt.BorderLayout.CENTER);
		
		/*
		 * Inserimento pulsantiera.
		 */
		this.add (buttonPanel, java.awt.BorderLayout.SOUTH);
		
		/*
		 * Imposta dimensione minima.
		 */
		this.setMinimumSize (new Dimension (250, 150));
	}
	
	
	/**
	 * Specifica se la sincronizzazionedei controlli per l'editazionedella durata ? in corso.
	 */
	private boolean synchronizingDurationControls = false;
	/**
	 * Sincronizza gli editor didurata con la durata specificata.
	 */
	private void synchDurationControls (final Duration duration){
		synchronizingDurationControls = true;
		try {
			this.durationHourEditor.setValue ((int)duration.getHours ());
			this.durationMinEditor.setValue ((int)duration.getMinutes ());
			this.durationSecsEditor.setValue ((int)duration.getSeconds ());
		} finally {
			synchronizingDurationControls = false;
		}
	}
	
	/**
	 * Aggiorna il nodo di avanzamento selezionato con i dati delle componenti di editazione.
	 */
	private final void pushData (){
		final Application app = Application.getInstance ();
		
		final ProgressItem node = app.getSelectedItem ();
		if (node!=null){
			/*
			 * C'? un avanzamento selezionato.
			 * Valorizzazione avanzamento da campi editazione.
			 */
			new CreateProgress (node, 
				new Attribute[]{
					new DateAttribute (CreateProgress.FROM, this.fromEditor.getDate ()),
					new DateAttribute (CreateProgress.TO, this.toEditor.getDate ()),
					new StringAttribute (CreateProgress.DESCRIPTION, this.descriptionEditor.getText ()),
					new StringAttribute (CreateProgress.NOTES, this.notesEditor.getText ())
				}).execute ();

			
			this.setDataChanged (false);
		}
	}
	
	/**
	 * Aggiorna questo pannello a seguito di una notifica da parte di un oggetto sosservato.
	 * Nello specifico la notifica di interesse ? quella relativa al cambiamento del nodo
	 * di avanzamento correntemente selezionata.
	 * A seguito di tale notifica i campi di editaszione cvengono aggiornati.
	 *
	 * @param o la sorgente della notifica.
	 * @param arg argomento della notifica.
	 */
	public void update (Observable o, Object arg) {
	}
	
	/**
	 * Imposta il flag di "modifica avvenuta" per questo pannello.
	 */
	private void setDataChanged (boolean changed){
		this._dataChanged = changed;
		this.confirmButton.setEnabled (this._dataChanged && Application.getInstance ().getSelectedItem ()!=null);
	}
	
	/**
	 * Implementazione vuota.
	 */
	public void keyPressed (KeyEvent e) {
	}
	
	/**
	 * Implementazione vuota.
	 */
	public void keyReleased (KeyEvent e) {
	}
	
	/**
	 * Notifica modifica.
	 */
	public void keyTyped (KeyEvent e) {
		/*
		 * Notifica modifica dati.
		 */
		setDataChanged (true);
	}
	
	/**
	 * Reinizializza i controlli.
	 */
	public void reinit (){
		this.fromEditor.setDate (new Date ());
		this.toEditor.setDate (new Date ());
		this.descriptionEditor.setText ("");
		this.notesEditor.setText ("");
	}
}

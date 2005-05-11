/*
 * ProgressItemEditPanel.java
 *
 * Created on 06 dicembre 2004, 21.59
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.*;
import com.ost.timekeeper.actions.commands.UpdateProgress;
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
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.event.*;

/**
 * Pannello di modifica periodo di avanzamento.
 *
 * @author  davide
 * @todo fix abilitazione pulsante conferma (ora è abilitato di default, si riabilita solo alla pressione dei tasti sui campi).
 */
public final class PeriodEditPanel extends javax.swing.JPanel implements Observer, KeyListener {
	
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
	 * Pulsante di annullamento modifiche.
	 */
	private final javax.swing.JButton resetButton = new javax.swing.JButton ();
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
	public PeriodEditPanel () {
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
			}
		});
//		confirmButton.setAction (ActionPool.getInstance ().getProgressUpdateAction ());
		confirmButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "confirm"));
		
		/*
		 * Configurazione pulsante ANNULLA.
		 */
		resetButton.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "reset"));
		resetButton.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent e){
				resynch ();
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
		buttonPanel.add (resetButton);
		
		
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
				final Date fromDate = PeriodEditPanel.this.fromEditor.getDate ();
				final Date toDate = PeriodEditPanel.this.toEditor.getDate ();
				if (fromDate!=null && toDate!=null){
					PeriodEditPanel.this.synchDurationControls (new Duration (fromDate,toDate));
				}
				PeriodEditPanel.this.setDataChanged (true);
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
		
		
			final PropertyChangeListener durationChangeListener = new PropertyChangeListener (){
				public void propertyChange (PropertyChangeEvent evt){
					if (synchronizingDurationControls){
						/* evita chiamata spuria */
						return;
					}
					PeriodEditPanel.this.toEditor.setDate (
					new Date (
					PeriodEditPanel.this.fromEditor.getDate ().getTime ()
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
		
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		editPanel.add (new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "controls", "secondary")), c);
		
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
		
		
		{
			final JPanel controlsPanel = new JPanel (new BorderLayout ());
			controlsPanel .add (editPanel, BorderLayout.CENTER);
			controlsPanel.setBorder (new BevelBorder (BevelBorder.RAISED));

			/*
			 * Inserimento pannello editazione.
			 */
			this.add (controlsPanel, java.awt.BorderLayout.CENTER);
		}
		
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
	 * Risincronizza il pannello di editazione con i dati del modello.
	 */
	private final void resynch (){
		this.refreshingData = true;
		try {
			final Application app = Application.getInstance ();

			final Progress editSubject = app.getSelectedProgress ();
			final boolean validItem = editSubject!=null;
			final boolean editingEnabled = validItem;

			this.editPanel.setEnabled (validItem);

			Date from = null;
			Date to = null;
			String description = "";
			String notes = "";
			if (validItem){
				/*
				 * C'è un avanzamento selezionato.
				 * Inizializzazione campi editazione a valori avanzamento selezionato.
				 */
				from = editSubject.getFrom ();
				to = editSubject.getTo ();
				description = editSubject.getDescription ();
				notes = editSubject.getNotes ();
			}
	//		this.fromEditor.setEnabled (validItem);
	//		this.toEditor.setEnabled (validItem);
			if (validItem){
				this.fromEditor.setDate (from);
				if (to!=null){
					this.toEditor.setDate (to);
				}
				

				final Duration duration = editSubject.getDuration ();
				synchDurationControls (duration);
			}
			this.descriptionEditor.setText (description);
			this.notesEditor.setText (notes);
			
			final boolean periodFinished = editSubject!=null && !editSubject.isEndOpened ();
			this.toEditor.setEnabled (periodFinished);
			this.durationHourEditor.setEnabled (periodFinished);
			this.durationMinEditor.setEnabled (periodFinished);
			this.durationSecsEditor.setEnabled (periodFinished);
		} finally {
			refreshingData = false;
		}
		
		/*
		 * resetta flag modifica
		 */
		this.setDataChanged (false);
		
	}
	
	/**
	 * Specifica se la sincronizzazionedei controlli per l'editazionedella durata è in corso.
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
		
		final Progress editSubject = app.getSelectedProgress ();
		if (editSubject!=null){
			/*
			 * C'è un avanzamento selezionato.
			 * Valorizzazione avanzamento da campi editazione.
			 */
			
			final boolean periodFinished = editSubject!=null && !editSubject.isEndOpened ();
			
			new UpdateProgress (editSubject, 
				new Attribute[]{
					new DateAttribute (UpdateProgress.PROGRESSFROM, this.fromEditor.getDate ()),
					new DateAttribute (UpdateProgress.PROGRESSTO, periodFinished?this.toEditor.getDate ():null),
					new StringAttribute (UpdateProgress.PROGRESSDESCRIPTION, this.descriptionEditor.getText ()),
					new StringAttribute (UpdateProgress.PROGRESSNOTES, this.notesEditor.getText ())
				}).execute ();

//			final javax.jdo.PersistenceManager pm = app.getPersistenceManager ();
//			final javax.jdo.Transaction tx = pm.currentTransaction ();
//			tx.begin ();
//			try {
//				editSubject.setFrom ();
//				editSubject.setTo ((Date)this.toEditor.getValue ());
//				editSubject.setDescription (this.descriptionEditor.getText () );
//				editSubject.setNotes (this.notesEditor.getText () );
//				tx.commit ();
//			} catch (final Throwable t){
//				tx.rollback ();
//				throw new com.ost.timekeeper.util.NestedRuntimeException (t);
//			}
			
			this.setDataChanged (false);
		}
	}
	
	/**
	 * Aggiorna questo pannello a seguito di una notifica da parte di un oggetto sosservato.
	 * Nello specifico la notifica di interesse è quella relativa al cambiamento del nodo
	 * di avanzamento correntemente selezionata.
	 * A seguito di tale notifica i campi di editaszione cvengono aggiornati.
	 *
	 * @param o la sorgente della notifica.
	 * @param arg argomento della notifica.
	 */
	public void update (Observable o, Object arg) {
		if (o instanceof Application){
			if (arg!=null && arg.equals (ObserverCodes.SELECTEDPROGRESSCHANGE)){
				this.resynch ();
			}
		}
	}
	
	private boolean refreshingData = false;
	
	/**
	 * Imposta il flag di "modifica avvenuta" per questo pannello.
	 */
	private void setDataChanged (boolean changed){
		if (refreshingData){
			/* evita chiamate spurie */
			return;
		}
		this._dataChanged = changed;
		this.confirmButton.setEnabled (this._dataChanged/* && ActionPool.getInstance ().getProgressUpdateAction ().isEnabled ()*/);
		this.resetButton.setEnabled (this._dataChanged);
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
	
	
}

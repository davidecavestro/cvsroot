/*
 * ProgressListFrame.java
 *
 * Created on 4 dicembre 2004, 10.13
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Il frame per la gestione della lista avanzamenti.
 *
 * @author  davide
 */
public final class ProgressListFrame extends javax.swing.JInternalFrame {

	/**
	 * La tabella.
	 */
	private com.ost.timekeeper.ui.SubtreeProgressesTable progressTable;
	
	/**
	 * La componente per la selezione del tipo di lista.
	 */
	private JComboBox progressesListCombo;
	
	/**
	 * Istanza singleton.
	 */
	private static ProgressListFrame _instance;
	
	/** 
	 * Costruttore. 
	 */
	private ProgressListFrame () {
		super (ResourceSupplier.getString (ResourceClass.UI, "controls", "progress.list"),
			true, //resizable
			false, //closable
			true, //maximizable
			true);//iconifiable
		initComponents ();
		
				
	}
	
	/**
	 * Ritorna l'istanza di questo inspector.
	 *
	 * @return l'istanza di questo inspector.
	 */	
	public static ProgressListFrame getInstance (){
		if (_instance==null){
			_instance = new ProgressListFrame();
		}
		return _instance;
	}
	
	/** 
	 * Inizializzazione delle componenti di questo frame.
	 */
	private void initComponents () {
		final Application app = Application.getInstance ();
		
		this.progressTable = new com.ost.timekeeper.ui.SubtreeProgressesTable ();
		this.progressTable.getSelectionModel ().addListSelectionListener (
			new ListSelectionListener (){
				public void valueChanged  (ListSelectionEvent lse){
					//Ignore extra messages.
					if (lse.getValueIsAdjusting()){
						return;
					}

					Period selectedPeriod = null;
					final ListSelectionModel lsm = (ListSelectionModel)lse.getSource();
					if (lsm.isSelectionEmpty()) {
						//no rows are selected
					} else {
						int selectedRow = lsm.getMinSelectionIndex();
						//selectedRow is selected
						selectedPeriod = (Period)progressTable.getProgressTableModel ().getProgresses ().get (selectedRow);
					}
					Application.getInstance ().setSelectedProgress (selectedPeriod);
				}
			}
		);
		
		final JPanel progressesPane = new JPanel (new BorderLayout ());
		this.progressesListCombo = new JComboBox (
		/*
		 * valori possibili.
		 * @@@ ATTENZIONE: la posizione è direttamente mappata nel gesore azione.
		 */
		new Object[]{ProgressListType.LOCAL, ProgressListType.SUBTREE
//		ResourceSupplier.getString (ResourceClass.UI, "controls", "local"), 
//		ResourceSupplier.getString (ResourceClass.UI, "controls", "subtree")
		}
		);
		progressesListCombo.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				progressTable.getProgressTableModel ().setProgressListType ((ProgressListType)progressesListCombo.getSelectedItem ());
			}
		});
		final JPanel progressesTopPane = new JPanel (new BorderLayout ());
		progressesTopPane.add (progressesListCombo, BorderLayout.WEST);
		progressesPane.add (progressesTopPane, BorderLayout.NORTH);
		final JPanel progressesBottomPane = new JPanel (new BorderLayout ());
		progressesBottomPane.add (
		new JScrollPane (progressTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), 
		BorderLayout.CENTER);
		
		progressTable.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);
		
		progressesPane.add (progressesBottomPane, BorderLayout.CENTER);
		this.setContentPane (progressesPane);
		
		
		/*
		 * Imposta dimensione minima.
		 */
		this.setMinimumSize (new Dimension (250, 150));
		
		/* Imposta selezione lista avanzamenti. */
		setListType (Application.getOptions ().getProgressListType ());

		pack ();
	}

	/**
	 * Ritorna la tabella di questa lista.
	 * @return la tabella.
	 */	
	public 	com.ost.timekeeper.ui.SubtreeProgressesTable  getProgressTable (){
		return this.progressTable;
	}
	
	/**
	 * Imposta il tipo di lista avanzamenti di questa finestra.
	 *
	 * @param listType il tipo di lista avanzamenti.
	 */	
	public void setListType (ProgressListType listType){
		if (listType!=null){
			this.progressesListCombo.setSelectedItem (listType);
		}
	}
	
	/**
	 * Ritorna il tipo di lista avanzamenti di questa finestra.
	 *
	 * @return il tipo di lista avanzamenti.
	 */	
	public ProgressListType getListType (){
		return (ProgressListType) this.progressesListCombo.getSelectedItem ();
	}
}

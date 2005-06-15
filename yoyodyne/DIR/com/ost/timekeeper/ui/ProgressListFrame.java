/*
 * ProgressListFrame.java
 *
 * Created on 4 dicembre 2004, 10.13
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.*;
import com.ost.timekeeper.actions.ActionPool;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.ui.BaseInternalPanel;
import com.ost.timekeeper.ui.support.JToolButton;
import com.ost.timekeeper.util.*;
import com.ost.timekeeper.view.*;
//import com.tomtessier.scrollabledesktop.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Il frame per la gestione della lista avanzamenti.
 *
 * @author  davide
 */
public final class ProgressListFrame extends BaseInternalPanel {

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
		initComponents ();
		
				
	}
	
	/** Inizializza il frame con la posizione.
	 * 
	 */
	public final void init (final Desktop desktop, final int x, final int y){
		super.init (
			desktop,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "progress.list"), 
			ResourceSupplier.getImageIcon (ResourceClass.UI, "progress-list-frame.png"),
			false, 
			x, 
			y);

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

					Progress selectedPeriod = null;
					final ListSelectionModel lsm = (ListSelectionModel)lse.getSource();
					if (lsm.isSelectionEmpty()) {
						//no rows are selected
					} else {
						int selectedRow = lsm.getMinSelectionIndex();
						//selectedRow is selected
						selectedPeriod = (Progress)progressTable.getProgressTableModel ().getProgresses ().get (selectedRow);
					}
					Application.getInstance ().setSelectedProgress (selectedPeriod);
				}
			}
		);
		
		final JPanel progressesPane = new JPanel (new BorderLayout ());
		this.progressesListCombo = new JComboBox (
		/*
		 * valori possibili.
		 * @@@ ATTENZIONE: la posizione ? direttamente mappata nel gesore azione.
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
		progressesListCombo.setToolTipText (ResourceSupplier.getString (ResourceClass.UI, "controls", "progresslist.combo.local_subtree"));
		
		final javax.swing.JToolBar progressesToolBar = new javax.swing.JToolBar ();
		progressesToolBar.setLayout (new java.awt.FlowLayout ( java.awt.FlowLayout.LEFT));
		progressesToolBar.setBorder (new javax.swing.border.EtchedBorder ());
		progressesToolBar.setRollover (true);
		progressesToolBar.setAutoscrolls (true);
		
        progressesToolBar.putClientProperty("jgoodies.headerStyle", "Both");
		
		progressesToolBar.add (progressesListCombo);
		
		progressesToolBar.add (new javax.swing.JSeparator ());
		
		final javax.swing.JButton editButton = new JToolButton (ActionPool.getInstance ().getStartProgressEdit ());
		editButton.setText ("");
		progressesToolBar.add (editButton);
		
		final javax.swing.JButton deleteButton = new JToolButton (ActionPool.getInstance ().getProgressDeleteAction ());
		deleteButton.setText ("");
		progressesToolBar.add (deleteButton);
		
		
		progressesPane.add (progressesToolBar, BorderLayout.NORTH);
		final JPanel progressesBottomPane = new JPanel (new BorderLayout ());
		
		final JScrollPane tableScroller = new JScrollPane (progressTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScroller.getViewport ().setBackground (progressTable.getBackground ());
		
		/*
		 * mantiene il background allineato con la tabela
		 */
		app.addObserver (new Observer (){
			public void update(Observable o, Object arg){
				if (o instanceof Application){
					if (arg!=null && arg.equals (ObserverCodes.LOOKANDFEEL_CHANGING)){
						tableScroller.getViewport ().setBackground (progressTable.getBackground ());
					}
				}				
			}
		});
				
		progressesBottomPane.add (tableScroller, BorderLayout.CENTER);

//		progressTable.setIntercellSpacing (new Dimension (5, 3));
		progressTable.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);
		
		progressesPane.add (progressesBottomPane, BorderLayout.CENTER);
		
		this.setLayout (new GridBagLayout ());
		final GridBagConstraints c = new GridBagConstraints ();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 3, 3, 3);
		c.weightx=1.0;
		c.weighty=1.0;
		c.gridx=0;
		c.gridy=0;
		this.add (progressesPane, c);
		
		
		/*
		 * Imposta dimensione minima.
		 */
		this.setMinimumSize (new Dimension (250, 150));
		
		/* Imposta selezione lista avanzamenti. */
		setListType (Application.getOptions ().getProgressListType ());

//		this.setFrameIcon (ResourceSupplier.getImageIcon (ResourceClass.UI, "progress-list-frame.png"));
		
//		pack ();
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

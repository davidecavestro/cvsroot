/*
 * ProjectSelectDialog.java
 *
 * Created on 06 giugno 2004, 20.15
 */

package com.ost.timekeeper.ui;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;
import com.ost.timekeeper.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Finestra di selezione di un progetto.
 *
 * @author  davide
 */
public final class ProjectSelectDialog extends javax.swing.JDialog {
	
	private String titleText;
	private String labelText;
	private List projects;
	
	/**
	 * Costruttore con parametri.
	 *
	 * @param parent la finestra padre.
	 * @param title il titolo di questa finestra
	 * @param label l'etichetta contenete la richiesta di selezione.
	 * @param modal stato modale.
	 * @param projects l'insieme di progetti da presentare per la scelta.
	 */
	public ProjectSelectDialog(java.awt.Frame parent, String title, String label, boolean modal, List projects) {
		super(parent, modal);
		this.projects=projects;
		initComponents();
		this.titleText = title;
		this.labelText = label;
		postInitComponents();
	}
	
	/** 
	 * Inizializzazione componenti.
	 */
    private void initComponents() {
        jPanelMain = new javax.swing.JPanel();
        jLabelChoose = new javax.swing.JLabel();
        jListProjects = new javax.swing.JList();
        jPanelBottom = new javax.swing.JPanel();
        jButtonOk = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanelMain.setLayout(new java.awt.BorderLayout());

        jLabelChoose.setText("Choose");
        jPanelMain.add(jLabelChoose, java.awt.BorderLayout.NORTH);

        jListProjects.setModel(new ProjectListModel (this.projects));
		jListProjects.addMouseListener (new MouseAdapter (){
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount ()>1){
					jButtonConfirmActionPerformed ( null );
				}
			}
		});
        jPanelMain.add(new JScrollPane (jListProjects), java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelMain, java.awt.BorderLayout.CENTER);

        jButtonOk.setText(ResourceSupplier.getString (ResourceClass.UI, "global", "controls.button.confirm"));
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfirmActionPerformed(evt);
            }
        });

        jPanelBottom.add(jButtonOk);

        jButtonCancel.setText(ResourceSupplier.getString (ResourceClass.UI, "global", "controls.button.cancel"));
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanelBottom.add(jButtonCancel);

        getContentPane().add(jPanelBottom, java.awt.BorderLayout.SOUTH);

        getContentPane().add(new JPanel (), java.awt.BorderLayout.WEST);
        getContentPane().add(new JPanel (), java.awt.BorderLayout.EAST);
		
		getRootPane().setDefaultButton (jButtonOk);
        pack();
    }

	private void postInitComponents() {
		this.jLabelChoose.setText(this.labelText);
		this.setTitle(this.titleText);
	}

	private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		System.out.println (this.jListProjects.getSelectedIndex());
		this.jListProjects.setSelectedValue (null, true);
		this.hide ();
	}

	private void jButtonConfirmActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		this.hide();
	}
	
	/** 
	 * Chiude questa finestra. 
	 */
	private void closeDialog(java.awt.event.WindowEvent evt) {
		setVisible(false);
		dispose();
	}
	
    // Variables declaration 
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JLabel jLabelChoose;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelBottom;
    private javax.swing.JList jListProjects;
    // End of variables declaration
	
	/**
	 * Richiede all'utente di scegliere un progetto tra quelli disponibili, e lo ritorna.
	 *
	 * @param parent la finestra padre.
	 * @param title il titolo.
	 * @param label l'etichetta di scelta.
	 * @param modal stato modale.
	 * @return ritorna il progetto scelto dall'utente.
	 */	
	public static Project chooseProject (java.awt.Frame parent, String title, String label, boolean modal){
		ProjectSelectDialog dialog = new ProjectSelectDialog (parent, title, label, modal, Application.getInstance().getAvailableProjects ());
		dialog.show ();
		return (Project)dialog.jListProjects.getSelectedValue();
	}
	
	/**
	 * Modello dati per la lista di selezione.
	 */
	private final class ProjectListModel implements javax.swing.ListModel{
		
		private List projects;
		
		public ProjectListModel (List projects){
			this.projects = projects;
		}
		public void addListDataListener(javax.swing.event.ListDataListener l) {
		}
		
		public Object getElementAt(int index) {
			return this.projects.get(index);
		}
		
		public int getSize() {
			return this.projects.size ();
		}
		
		public void removeListDataListener(javax.swing.event.ListDataListener l) {
		}
		
	}
}

/*
 * ProjectSelectDialog.java
 *
 * Created on 06 giugno 2004, 20.15
 */

package com.ost.timekeeper.ui;

import java.util.*;

import com.ost.timekeeper.*;
import com.ost.timekeeper.model.*;

/**
 * Dialog per la selezione di un progetto.
 * @author  davide
 */
public class ProjectSelectDialog extends javax.swing.JDialog {
	
	private String titleText;
	private String labelText;
	private List projects;
	
	/** Creates new form ProjectSelectDialog */
	public ProjectSelectDialog(java.awt.Frame parent, String title, String label, boolean modal, List projects) {
		super(parent, modal);
		this.projects=projects;
		initComponents();
		this.titleText = title;
		this.labelText = label;
		postInitComponents();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 */
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jList1 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setText("jLabel1");
        jPanel1.add(jLabel1, java.awt.BorderLayout.NORTH);

        jList1.setModel(new ProjectListModel (this.projects));
        jPanel1.add(jList1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton1);

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton2);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }

	private void postInitComponents() {
		this.jLabel1.setText(this.labelText);
		this.setTitle(this.titleText);
	}

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		this.hide ();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
		this.hide();
	}
	
	/** Closes the dialog */
	private void closeDialog(java.awt.event.WindowEvent evt) {
		setVisible(false);
		dispose();
	}
	
    // Variables declaration 
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JList jList1;
    // End of variables declaration
	
	public static Project createDialog (java.awt.Frame parent, String title, String label, boolean modal){
		ProjectSelectDialog dialog = new ProjectSelectDialog (parent, title, label, modal, Application.getInstance().getAvailableProjects ());
		dialog.show ();
		return (Project)dialog.jList1.getSelectedValue();
	}
	
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

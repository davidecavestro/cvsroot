/*
 * OptionsDialog.java
 *
 * Created on March 7, 2006, 11:45 PM
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Dialog di impostazione delle opzioni.
 *
 * @author  davide
 */
public class OptionsDialog extends javax.swing.JDialog {

	private final ApplicationContext _context;
	
	/**
	 * Costruttore
	 */
	public OptionsDialog (java.awt.Frame parent, boolean modal, ApplicationContext context) {
		super (parent, modal);
		this._context = context;
		initComponents ();
		
		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		cancelButton.getActionMap().put("cancel", new javax.swing.AbstractAction ("cancel"){
			public void actionPerformed (ActionEvent ae){
				cancel ();
			}
		});
		
		this.getRootPane ().setDefaultButton (okButton);
		
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Options"));
        setModal(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setText("Choose look");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 3, 3, 3);
        jPanel1.add(jLabel1, gridBagConstraints);

        jList1.setFont(new java.awt.Font("Dialog", 0, 12));
        jList1.setModel(new AbstractListModel () {

            final LookAndFeelChoice[] lafs = LookAndFeelChoice.values ();

            public LookAndFeelChoice getElementAt (int index) {
                return lafs[index];
            }
            public int getSize () {
                return lafs.length;
            }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });

        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 11, 11);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("restart_application_after_changing_look"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(11, 11, 11, 11);
        jPanel1.add(jLabel2, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        okButton.setFont(new java.awt.Font("Dialog", 0, 12));
        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 10);
        jPanel2.add(okButton, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        cancelButton.setText("Cancel");
        cancelButton.setDefaultCapable(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 10);
        jPanel2.add(cancelButton, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-409)/2, (screenSize.height-235)/2, 409, 235);
    }// </editor-fold>//GEN-END:initComponents

	private void jList1ValueChanged (javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
		apply ();
	}//GEN-LAST:event_jList1ValueChanged

	private void formComponentShown (java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
		init (_context.getApplicationOptions ());		

	}//GEN-LAST:event_formComponentShown

	private void okButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		confirm ();
	}//GEN-LAST:event_okButtonActionPerformed

	private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		cancel ();
	}//GEN-LAST:event_cancelButtonActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
	
	private final void confirm (){
//		final UserSettings us = _context.getUserSettings ();
		
//		us.setBackupOnSave (Boolean.valueOf (createBackupFilesCheckBox.getModel ().isSelected ()));
//		us.setKeyEditing (Boolean.valueOf (enableKeyEditingCheckBox.getModel ().isSelected ()));
		
		_context.getUserSettings ().setLookAndFeel (getSelectedLAF ());		
		hide ();
	}
	
	private LookAndFeelChoice _initialChoice;
	
	private void init (ApplicationOptions ao) {
//		createBackupFilesCheckBox.getModel ().setSelected (ao.isBackupOnSaveEnabled ());
//		enableKeyEditingCheckBox.getModel ().setSelected (ao.isKeyEditingEnabled ());
		for (final LookAndFeelChoice c : LookAndFeelChoice.values ()) {
			if (c.getClassName ().equals (UIManager.getLookAndFeel ().getClass ().getName ())) {
				jList1.setSelectedValue (c, true);
				_initialChoice = c;
			}
		}
		if (_initialChoice==null) {
			_initialChoice = LookAndFeelChoice.CROSS_PLATFORM;
		}
	}
	
	private void cancel (){
		jList1.setSelectedValue (_initialChoice, false);
		apply ();
		hide ();
	}
	
	private enum LookAndFeelChoice {
		SYSTEM {
			public String getClassName () {
				return UIManager.getSystemLookAndFeelClassName ();
			}
			public String toString () {
				return "System";
			}
		},
		CROSS_PLATFORM {
			public String getClassName () {
				return UIManager.getCrossPlatformLookAndFeelClassName ();
			}
			public String toString () {
				return "Cross Platform";
			}
		},
		WINDOWS {
			public String getClassName () {
				return "com.jgoodies.looks.windows.WindowsLookAndFeel";
			}
			public String toString () {
				return "Windows";
			}
		},
		PLASTIC {
			public String getClassName () {
				return "com.jgoodies.looks.plastic.PlasticLookAndFeel";
			}
			public String toString () {
				return "Plastic";
			}
		},
		PLASTIC2D {
			public String getClassName () {
				return "com.jgoodies.looks.plastic.Plastic3DLookAndFeel";
			}
			public String toString () {
				return "Plastic 3D";
			}
		},
		PLASTICXP {
			public String getClassName () {
				return "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
			}
			public String toString () {
				return "Plastic XP";
			}
		},
		TINYLAF {
			public String getClassName () {
				return "de.muntjak.tinylookandfeel.TinyLookAndFeel";
			}
			public String toString () {
				return "TinyLAF";
			}
		};
		
		
		public abstract String getClassName ();
		public abstract String toString ();
		
	}
	
	private String getSelectedLAF () {
		final LookAndFeelChoice lafc = (LookAndFeelChoice)jList1.getSelectedValue ();
		if (lafc!=null) {
			return lafc.getClassName ();
		} else {
			return _initialChoice.getClassName ();
		}
	}
	
	private void apply () {
		_context.getWindowManager ().setLookAndFeel (getSelectedLAF ());
	}
}
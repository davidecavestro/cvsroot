/*
 * OpenWorkSpaceDialog.java
 *
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.gui.dialog.DialogEvent;
import com.davidecavestro.common.gui.dialog.DialogNotifier;
import com.davidecavestro.common.gui.dialog.DialogNotifierImpl;
import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.WorkSpace;
import com.davidecavestro.timekeeper.persistence.PersistenceNodeException;
import com.ost.timekeeper.model.Project;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;

/**
 * Dialog di selezione del progetto da aprire.
 *
 * @author  davide
 */
public class OpenWorkSpaceDialog extends javax.swing.JDialog implements PersistentComponent, DialogNotifier {
	
	
	private final DialogNotifierImpl _dialogNotifier;
	private final ApplicationContext _context;
	
	/**
	 * 
	 * @param parent 
	 * @param modal 
	 * @param workspace 
	 */
	public OpenWorkSpaceDialog (final ApplicationContext context, java.awt.Frame parent, boolean modal){
		super (parent, modal);
		_context = context;
		initComponents ();
		
		this._dialogNotifier = new DialogNotifierImpl ();
		
		
		this.getRootPane ().setDefaultButton (okButton);
		
		cancelButton.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW).put (KeyStroke.getKeyStroke ("ESCAPE"), "cancel");
		cancelButton.getActionMap().put("cancel", new javax.swing.AbstractAction ("cancel"){
			public void actionPerformed (ActionEvent ae){
				cancel ();
			}
		});
	
//		pack ();
		setLocationRelativeTo (null);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        projectsList = new javax.swing.JList();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Open workspace");
        setModal(true);
        okButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(okButton, "Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 8, 5);
        getContentPane().add(okButton, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(cancelButton, "Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 8, 5);
        getContentPane().add(cancelButton, gridBagConstraints);

        helpButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(helpButton, "Help");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 8, 5);
        getContentPane().add(helpButton, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, "Available projects");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 5);
        getContentPane().add(jLabel6, gridBagConstraints);

        projectsList.setFont(new java.awt.Font("Dialog", 0, 12));
        projectsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        projectsList.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                final JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                final WorkSpace ws = (WorkSpace)value;
                label.setText(ws.getName ());
                return label;
            }
        });
        projectsList.setVisibleRowCount(4);
        projectsList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                projectsListKeyTyped(evt);
            }
        });
        projectsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                projectsListValueChanged(evt);
            }
        });
        projectsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                projectsListMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(projectsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void projectsListKeyTyped (java.awt.event.KeyEvent evt) {//GEN-FIRST:event_projectsListKeyTyped
		if (evt.getKeyCode ()==KeyEvent.VK_ENTER){
			confirm ();
		}
	}//GEN-LAST:event_projectsListKeyTyped

	private void projectsListMouseClicked (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectsListMouseClicked
		if (evt.getClickCount ()>1){
			confirm ();
		}
	}//GEN-LAST:event_projectsListMouseClicked

	private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
		cancel ();
	}//GEN-LAST:event_cancelButtonActionPerformed

	private void okButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		confirm ();
	}//GEN-LAST:event_okButtonActionPerformed

	private void projectsListValueChanged (javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_projectsListValueChanged
		final WorkSpace wp = (WorkSpace)projectsList.getSelectedValue();
        projectsList.ensureIndexIsVisible(projectsList.getSelectedIndex());
		check ();
	}//GEN-LAST:event_projectsListValueChanged
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton helpButton;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JList projectsList;
    // End of variables declaration//GEN-END:variables

	/**
	 * Mostra la dialog con un progetto selezionato.
	 */
	public void showWithSelection (final WorkSpace ws) {
		reset ();
		projectsList.setModel (prepareProjects ());
		projectsList.setSelectedValue (ws, true);
		super.show ();
	}
	
	public void show () {
		reset ();
		projectsList.setModel (prepareProjects ());
		super.show ();
	}
	
	/**
	 * Reimposta lo stato iniziale.
	 */
	private void reset (){
		check ();
	}
	
	/**
	 * Controlli di abilitazione dei pulsanti
	 */
	private void check (){
		okButton.setEnabled (projectsList.getSelectedValue ()!=null);
	}
	
	public String getPersistenceKey () {
		return "open-project-dialog";
	}
	
	public void makePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);
	}
	
	public boolean restorePersistent (com.davidecavestro.common.gui.persistence.PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}
	

	/**
	 * COnsente l'ordinamento per nome dei progetti.
	 */
	class WorkSpaceComparator implements Comparator {
		
		public int compare (Object o1, Object o2) {
			return ((WorkSpace)o1).getName ().compareTo (((WorkSpace)o2).getName ());
		}
		
	}
	
	
	public void addDialogListener (com.davidecavestro.common.gui.dialog.DialogListener l) {
		this._dialogNotifier.addDialogListener (l);
	}	
	
	public void removeDialogListener (com.davidecavestro.common.gui.dialog.DialogListener l) {
		this._dialogNotifier.removeDialogListener (l);
	}
	
	public WorkSpace getSelectedWorkSpace (){
		return (WorkSpace)projectsList.getSelectedValue ();
	}
	
	
	/**
	 * Azione di CONFERMA sulla dialog
	 */
	private void confirm (){
		this._dialogNotifier.fireDialogPerformed (new DialogEvent (this, projectsList.getSelectedValue (), JOptionPane.OK_OPTION));
		hide ();		
	}
	
	/**
	 * Azione di ANNULLA sulla dialog
	 */
	private void cancel (){
		hide ();
	}
	
	/**
	 * Fornisce il modello aggiornato per la lista contenente i progetti
	 */
	private ListModel prepareProjects () {
		try {
			final List<Project> data = _context.getPersistenceNode ().getAvailableWorkSpaces ();
			
			Collections.sort (data, new WorkSpaceComparator ());

			return new AbstractListModel () {
				public Object getElementAt (int index) {
					return data.get (index);
				}
				public int getSize () {
					return data.size ();
				}
			};
		} catch (final PersistenceNodeException pne) {
			JOptionPane.showInternalMessageDialog (this, pne.getMessage ()+" Please see log console for more details.");
			return new DefaultListModel();
		}
		
	}
}

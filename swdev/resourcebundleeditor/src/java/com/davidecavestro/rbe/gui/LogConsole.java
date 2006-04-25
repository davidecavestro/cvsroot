/*
 * LogConsole.java
 *
 * Created on 25 febbraio 2006, 11.09
 */

package com.davidecavestro.rbe.gui;

import com.davidecavestro.rbe.ApplicationContext;
import javax.swing.text.Document;

/**
 * Console di log.
 *
 * @author  davide
 */
public class LogConsole extends javax.swing.JDialog {
	
	private final ApplicationContext _context;
	
	/** Costruttore. */
	public LogConsole (java.awt.Frame parent, boolean modal, ApplicationContext context) {
		super (parent, modal);
		this._context = context;
		
		initComponents ();
		
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

        jPanel1 = new javax.swing.JPanel();
        logPanel = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        logPanel.setBackground(new java.awt.Color(0, 0, 0));
        logPanel.setEditable(false);
        logPanel.setForeground(new java.awt.Color(255, 255, 255));
        logPanel.setCaretColor(new java.awt.Color(255, 255, 255));
        logPanel.setPreferredSize(new java.awt.Dimension(320, 240));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(logPanel, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
	
	/**
	 * Inizializza la finestra sul documento specificato.
	 *
	 * @param logDocument il documento contenente le informazioni di log.
	 */	
	public void init (final Document logDocument){
		logPanel.setDocument (logDocument);
		setTitle (prepareTitle ());
	}
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextPane logPanel;
    // End of variables declaration//GEN-END:variables
	
	private String prepareTitle (){
		final StringBuffer sb = new StringBuffer ();
		sb.append (_context.getApplicationData ().getApplicationExternalName ()).append (" - Log console");
		return sb.toString ();
	}
}
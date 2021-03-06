/*
 * LogConsole.java
 *
 * Created on April 18, 2006, 2:32 PM
 */

package com.davidecavestro.rbe.gui;

import com.davidecavestro.common.gui.persistence.PersistenceStorage;
import com.davidecavestro.common.gui.persistence.PersistenceUtils;
import com.davidecavestro.common.gui.persistence.PersistentComponent;
import com.davidecavestro.rbe.ApplicationContext;
import javax.swing.text.Document;

/**
 * Console di log.
 *
 * @author  davide
 */
public class LogConsole extends javax.swing.JFrame implements PersistentComponent {
	
	private final ApplicationContext _context;
	/** Costruttore. */
	public LogConsole (ApplicationContext context) {
		super ();
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

        logPanel = new javax.swing.JTextPane();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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
        getContentPane().add(logPanel, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
	
	/**
	 * Inizializza la finestra sul documento specificato.
	 *
	 * @param logDocument il documento contenente le informazioni di log.
	 */	
	public void init (final Document logDocument){
		logPanel.setDocument (logDocument);
		setTitle (prepareTitle (_context));
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane logPanel;
    // End of variables declaration//GEN-END:variables
	
	private String prepareTitle (ApplicationContext context){
		final StringBuffer sb = new StringBuffer ();
		sb.append (context.getApplicationData ().getApplicationExternalName ()).append (" - Log console");
		return sb.toString ();
	}

	public String getPersistenceKey () {
		return "logconsole";
	}

	public void makePersistent (PersistenceStorage props) {
		PersistenceUtils.makeBoundsPersistent (props, this.getPersistenceKey (), this);
	}

	public boolean restorePersistent (PersistenceStorage props) {
		return PersistenceUtils.restorePersistentBounds (props, this.getPersistenceKey (), this);
	}
}

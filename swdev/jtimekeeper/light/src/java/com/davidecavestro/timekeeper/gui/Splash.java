/*
 * Splash.java
 *
 * Created on 20 gennaio 2006, 23.58
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.timekeeper.ApplicationContext;
import java.awt.BorderLayout;

/**
 *
 * @author  davide
 */
public class Splash extends javax.swing.JFrame {
	
	private final ApplicationData _appData;
	
	private final PresentationPanel _presentationPanel;
	
		
	/** Costruttore.*/
	public Splash ( ApplicationData appData) {
		this._appData = appData;
		initComponents ();
		_presentationPanel = new PresentationPanel (appData);
		mainPanel.add (_presentationPanel, BorderLayout.CENTER);
		
//		pack ();
		/*
		 * Centra sullo schermo.
		 */
		setLocationRelativeTo (null);
		
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("splashFrame");
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        getAccessibleContext().setAccessibleName("splashFrame");
        getAccessibleContext().setAccessibleDescription("Application startup splash");
        mainPanel.setLayout(new java.awt.BorderLayout());

        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        mainPanel.setMaximumSize(null);
        mainPanel.setMinimumSize(new java.awt.Dimension(420, 300));
        mainPanel.setPreferredSize(new java.awt.Dimension(420, 300));
        progressBar.setValue(99);
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new java.awt.Dimension(148, 8));
        mainPanel.add(progressBar, java.awt.BorderLayout.SOUTH);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
	
	private void exitForm (java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
	}//GEN-LAST:event_exitForm
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables

	/**
	 * Mostra le informazioni specificate.
	 *
	 * @param info le informazioni.
	 */	
	public void showInfo (String info){
		this._presentationPanel.setInfoText (info);
	}
}
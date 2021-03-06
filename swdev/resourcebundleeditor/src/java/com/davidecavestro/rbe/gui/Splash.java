/*
 * Splash.java
 *
 * Created on 20 gennaio 2006, 23.58
 */

package com.davidecavestro.rbe.gui;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.rbe.ApplicationContext;
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
		
		pack ();
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
    private void initComponents() {//GEN-BEGIN:initComponents
        mainPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Loading_application..."));
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

        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2));
        progressBar.setValue(99);
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new java.awt.Dimension(148, 8));
        mainPanel.add(progressBar, java.awt.BorderLayout.SOUTH);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents
	
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

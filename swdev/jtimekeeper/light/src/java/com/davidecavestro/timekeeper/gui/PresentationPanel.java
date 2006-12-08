/*
 * PresentationPanel.java
 *
 * Created on 28 gennaio 2006, 11.26
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.common.application.ApplicationData;

/**
 *
 * @author  davide
 */
public class PresentationPanel extends javax.swing.JPanel {
	
	final ApplicationData _appData;
		
	/**
	 * Costruttore.
	 * @param appData i dati applicativi.
	 */
	public PresentationPanel (ApplicationData appData) {
		_appData = appData;
		initComponents ();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        appVersionLabel = new javax.swing.JLabel();
        appNameLabel = new javax.swing.JLabel();
        applicationImageLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        setPreferredSize(new java.awt.Dimension(420, 300));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(420, 300));
        appVersionLabel.setFont(new java.awt.Font("Impact", 1, 28));
        appVersionLabel.setForeground(new java.awt.Color(0, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(appVersionLabel, _appData.getVersionNumber ());
        appVersionLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        appVersionLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(appVersionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 34, 140, 90));

        appNameLabel.setFont(new java.awt.Font("Impact", 1, 50));
        appNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        appNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(appNameLabel, _appData.getApplicationExternalName ());
        appNameLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        appNameLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(appNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2, 260, 130));

        applicationImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        applicationImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/davidecavestro/timekeeper/gui/images/splash.png")));
        applicationImageLabel.setIconTextGap(-300);
        applicationImageLabel.setMaximumSize(null);
        jPanel1.add(applicationImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 290));

        add(jPanel1, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel appNameLabel;
    private javax.swing.JLabel appVersionLabel;
    private javax.swing.JLabel applicationImageLabel;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
	
	/**
	 * Imposta il testo dell'etichetta centrale per le informazioni.
	 *
	 * @param info il testo da mostrare.
	 */	
	public void setInfoText (final String info){
		applicationImageLabel.setText (info);
	}
}

/*
 * About.java
 *
 * Created on 25 gennaio 2006, 23.12
 */

package com.davidecavestro.rbe.gui;

import com.davidecavestro.common.application.ApplicationData;
import com.davidecavestro.rbe.ApplicationContext;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXTable;

/**
 * La finestra di About.
 *
 * @author  davide
 */
public class About extends javax.swing.JDialog {
	
		
	private final ApplicationContext _context;
	
	private final SystemEnvTableModel systemEnvTableModel;
	private final SystemPropertiesTableModel systemPropertiesTableModel;
	
	
	/**
	 * Costruttore.
	 * @param parent
	 * @param modal
	 * @param context il contesto applicativo.
	 */
	public About (java.awt.Frame parent, boolean modal, ApplicationContext context) {
		super (parent, modal);
		this._context = context;
		
		systemEnvTableModel = new SystemEnvTableModel ();
		systemPropertiesTableModel = new SystemPropertiesTableModel ();
		initComponents ();
		
		presentationPanel.add (new PresentationPanel (_context.getApplicationData ()), new java.awt.GridBagConstraints());
		
		setLocationRelativeTo (null);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        presentationPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        systemPropsTable = new JXTable (systemPropertiesTableModel);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setName("aboutDialog");
        jTabbedPane1.setMaximumSize(null);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(480, 360));
        presentationPanel.setLayout(new java.awt.GridBagLayout());

        jTabbedPane1.addTab(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("About"), presentationPanel);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(200, 200));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("ProductVersionLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, _context.getApplicationData ().getVersionNumber ());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("BuildLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, _context.getApplicationData ().getBuildNumber ()
            +" ["
            +com.davidecavestro.common.util.CalendarUtils.getTimestamp (
                _context.getApplicationData ().getBuildDate ().getTime (), "yyyyMMddHHmm")
            +"}");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("ReleaseDateLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, com.davidecavestro.common.util.CalendarUtils.getTimestamp (_context.getApplicationData ().getReleaseDate ().getTime (), "yyyy/MM/dd"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, _context.getApplicationData ().getApplicationExternalName ());
        jPanel5.add(jLabel7);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Product_information"));
        jPanel5.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jPanel5, gridBagConstraints);

        jTabbedPane1.addTab(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Detail"), jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jToggleButton1.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton1, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Java"));
        jToggleButton1.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Java_properties_button_tooltip"));
        buttonGroup1.add(jToggleButton1);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jToolBar1.add(jToggleButton1);

        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton2, java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Host"));
        jToggleButton2.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Host_env_button_tooltip"));
        buttonGroup1.add(jToggleButton2);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jToolBar1.add(jToggleButton2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jToolBar1, gridBagConstraints);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(javax.swing.UIManager.getDefaults().getColor("Table.background"));
        jScrollPane1.setMaximumSize(null);
        jScrollPane1.setMinimumSize(null);
        final Font keysFont = new Font("monospaced", Font.PLAIN, 12);
        systemPropsTable.setMaximumSize(null);
        systemPropsTable.setMinimumSize(null);
        systemPropsTable.setPreferredSize(null);
        systemPropsTable.setDefaultRenderer (String.class, new DefaultTableCellRenderer () {
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

                final JLabel label = (JLabel)super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
                if (0 != column) {
                    /* colonna chiavi */
                    label.setFont (keysFont);
                }
                return label;
            }

        });
        jScrollPane1.setViewportView(systemPropsTable);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel4, gridBagConstraints);

        jPanel3.add(jPanel2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("System"), jPanel3);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

	private void jToggleButton1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
		systemPropsTable.setModel (systemPropertiesTableModel);
	}//GEN-LAST:event_jToggleButton1ActionPerformed

	private void jToggleButton2ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
		systemPropsTable.setModel (systemEnvTableModel);
	}//GEN-LAST:event_jToggleButton2ActionPerformed
	

	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel presentationPanel;
    private javax.swing.JTable systemPropsTable;
    // End of variables declaration//GEN-END:variables

	private final class SystemPropertiesTableModel implements TableModel {

		private final Object[] keys;
		private final Object[] values;
		
		public SystemPropertiesTableModel (){
            this.keys = System.getProperties ().keySet ().toArray (new Object[0]);
            this.values = System.getProperties ().values ().toArray (new Object[0]);
		}
		
		public void addTableModelListener (javax.swing.event.TableModelListener l) {}
		
		public int getColumnCount () {
			return 2;
		}
		
		public Class getColumnClass (int columnIndex) {
			return String.class;
		}
		
		public String getColumnName (int columnIndex) {
			switch (columnIndex) {
				case 0:
					return java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Property");
				case 1:
					return java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Value");
				default:
					return null;
			}
		}
		
		public int getRowCount () {
			return keys.length;
		}
		
		public Object getValueAt (int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case 0:
					return keys[rowIndex];
				case 1:
					return values[rowIndex];
				default:
					return null;
			}
		}
		
		public boolean isCellEditable (int rowIndex, int columnIndex) {
			return false;
		}
		
		public void removeTableModelListener (javax.swing.event.TableModelListener l) {}
		
		public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
			throw new UnsupportedOperationException ();
		}
		
	}

	private final class SystemEnvTableModel implements TableModel {

		private final Map env;
		private final Object[] keys;
		
		public SystemEnvTableModel (){
			this.env = System.getenv ();
            this.keys = env.keySet ().toArray (new Object[0]);
		}
		
		public void addTableModelListener (javax.swing.event.TableModelListener l) {}
		
		public int getColumnCount () {
			return 2;
		}
		
		public Class getColumnClass (int columnIndex) {
			return String.class;
		}
		
		public String getColumnName (int columnIndex) {
			switch (columnIndex) {
				case 0:
					return java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Property");
				case 1:
					return java.util.ResourceBundle.getBundle("com.davidecavestro.rbe.gui.res").getString("Value");
				default:
					return null;
			}
		}
		
		public int getRowCount () {
			return keys.length;
		}
		
		public Object getValueAt (int rowIndex, int columnIndex) {
			switch (columnIndex) {
				case 0:
					return keys[rowIndex];
				case 1:
					return env.get (keys[rowIndex]);
				default:
					return null;
			}
		}
		
		public boolean isCellEditable (int rowIndex, int columnIndex) {
			return false;
		}
		
		public void removeTableModelListener (javax.swing.event.TableModelListener l) {}
		
		public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
			throw new UnsupportedOperationException ();
		}
		
	}
	
}

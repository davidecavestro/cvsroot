/*
 * About.java
 *
 * Created on 25 gennaio 2006, 23.12
 */

package com.davidecavestro.timekeeper.gui;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.conf.ApplicationOptions;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.html.HTMLEditorKit;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.SortController;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.decorator.Sorter;

/**
 * La finestra di About.
 *
 * @author  davide
 */
public class About extends javax.swing.JDialog {
	
	private final static Object[] _voidObjectArray = new Object[0];
		
	private final ApplicationContext _context;
	
	private final ProxyTableModel proxyTableModel;
	
	private final SystemEnvTableModel systemEnvTableModel;
	private final SystemPropertiesTableModel systemPropertiesTableModel;
	private final ApplicationOptionsTableModel applicationOptionsTableModel;
	
	
	/**
	 * Costruttore.
	 * @param parent
	 * @param modal
	 * @param context il contesto applicativo.
	 */
	public About (java.awt.Frame parent, boolean modal, ApplicationContext context) {
		super (parent, modal);
		this._context = context;
		
		proxyTableModel = new ProxyTableModel ();
		
		systemEnvTableModel = new SystemEnvTableModel ();
		systemPropertiesTableModel = new SystemPropertiesTableModel ();
		applicationOptionsTableModel = new ApplicationOptionsTableModel ();
		
		proxyTableModel.setModel (systemEnvTableModel);
		
		initComponents ();
		
		presentationPanel.add (new PresentationPanel (_context.getApplicationData ()), new java.awt.GridBagConstraints());
		
		
		/*
		 * toggle sort
		 * 1 asc, 2 desc, 3 null
		 */
		((JXTable)systemPropsTable).setFilters (new FilterPipeline () {
			protected SortController createDefaultSortController () {
				return new SorterBasedSortController () {
					
					public void toggleSortOrder (int column, Comparator comparator) {
						Sorter currentSorter = getSorter ();
						if ((currentSorter != null)
						&& (currentSorter.getColumnIndex () == column)
						&& !currentSorter.isAscending ()) {
							setSorter (null);
						} else {
							super.toggleSortOrder (column, comparator);
						}
					}
					
				};
			}
		});		
		
		((JXTable)systemPropsTable).setColumnControlVisible (true);
		((JXTable)systemPropsTable).setSortOrder (0, SortOrder.ASCENDING);
		

		javaToggleButton.doClick ();
			
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        presentationPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        licenseEditorPane = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton3 = new javax.swing.JToggleButton();
        javaToggleButton = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        systemPropsTable = new JXTable (proxyTableModel);

        setTitle("About");
        setName("aboutDialog");
        jTabbedPane1.setFont(new java.awt.Font("Dialog", 0, 12));
        jTabbedPane1.setMaximumSize(null);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(420, 300));
        presentationPanel.setLayout(new java.awt.GridBagLayout());

        presentationPanel.setMinimumSize(new java.awt.Dimension(420, 300));
        presentationPanel.setPreferredSize(new java.awt.Dimension(420, 300));
        jTabbedPane1.addTab(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("About"), presentationPanel);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(200, 200));
        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ProductVersionLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("BuildLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("ReleaseDateLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, _context.getApplicationData ().getApplicationExternalName () + " - ");
        jPanel5.add(jLabel7);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Product_information"));
        jPanel5.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        jPanel1.add(jPanel5, gridBagConstraints);

        jScrollPane3.setBorder(null);
        jTextPane1.setBorder(null);
        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextPane1.setText(_context.getApplicationData ().getBuildNotes ());
        jTextPane1.setOpaque(false);
        jScrollPane3.setViewportView(jTextPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane3, gridBagConstraints);

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextField1.setText(_context.getApplicationData ().getBuildNumber ()
            +"-"
            +com.davidecavestro.common.util.CalendarUtils.getTimestamp (
                _context.getApplicationData ().getBuildDate ().getTime (), "yyyyMMddHHmm")
        );
        jTextField1.setBorder(null);
        jTextField1.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField1, gridBagConstraints);

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextField2.setText(_context.getApplicationData ().getVersionNumber ());
        jTextField2.setBorder(null);
        jTextField2.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField2, gridBagConstraints);

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextField3.setText(com.davidecavestro.common.util.CalendarUtils.getTimestamp (_context.getApplicationData ().getReleaseDate ().getTime (), "yyyy/MM/dd"));
        jTextField3.setBorder(null);
        jTextField3.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField3, gridBagConstraints);

        jTabbedPane1.addTab(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Detail"), jPanel1);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMaximumSize(null);
        jScrollPane2.setMinimumSize(null);
        licenseEditorPane.setEditorKit(new HTMLEditorKit ());
        licenseEditorPane.setMaximumSize(null);
        licenseEditorPane.setPreferredSize(null);
        final java.net.URL licenseURL = getClass ().getResource ("/license/license.html");

        if (licenseURL != null) {
            try {
                licenseEditorPane.setPage (licenseURL);
            } catch (final IOException ioe) {
                System.err.println ("Attempted to read a bad URL: " + licenseURL + ioe);
            }
        } else {
            System.err.println ("Couldn't find file: license.html");
        }
        jScrollPane2.setViewportView(licenseEditorPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jScrollPane2, gridBagConstraints);

        jTabbedPane1.addTab(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("License"), jPanel6);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton3, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Application"));
        jToggleButton3.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("About/System/HostButton/tooltip"));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jToolBar1.add(jToggleButton3);

        buttonGroup1.add(javaToggleButton);
        javaToggleButton.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(javaToggleButton, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Java"));
        javaToggleButton.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Java_properties_button_tooltip"));
        javaToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaToggleButtonActionPerformed(evt);
            }
        });

        jToolBar1.add(javaToggleButton);

        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setFont(new java.awt.Font("Dialog", 0, 12));
        org.openide.awt.Mnemonics.setLocalizedText(jToggleButton2, java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Host"));
        jToggleButton2.setToolTipText(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Host_env_button_tooltip"));
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
        final Font valuesFont = new Font("monospaced", Font.PLAIN, 12);
        systemPropsTable.setCellSelectionEnabled(true);
        systemPropsTable.setDefaultRenderer (String.class, new DefaultTableCellRenderer () {
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

                final JLabel label = (JLabel)super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
                if (0 != column) {
                    /* colonna valori */
                    label.setFont (valuesFont);
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

        jTabbedPane1.addTab(java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("System"), jPanel3);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jToggleButton3ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
		systemPropsTable.setModel (applicationOptionsTableModel);
//		((JXTable)systemPropsTable).packColumn (1,0);		
	}//GEN-LAST:event_jToggleButton3ActionPerformed

	private void javaToggleButtonActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaToggleButtonActionPerformed
		systemPropsTable.setModel (systemPropertiesTableModel);
//		((JXTable)systemPropsTable).packColumn (1,0);		
	}//GEN-LAST:event_javaToggleButtonActionPerformed

	private void jToggleButton2ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
		systemPropsTable.setModel (systemEnvTableModel);
//		((JXTable)systemPropsTable).packColumn (1,0);	
	}//GEN-LAST:event_jToggleButton2ActionPerformed
	

	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToggleButton javaToggleButton;
    private javax.swing.JTextPane licenseEditorPane;
    private javax.swing.JPanel presentationPanel;
    private javax.swing.JTable systemPropsTable;
    // End of variables declaration//GEN-END:variables

	
	/**
	 * Incapsula un tablemodel in modo da poterne variare l'istanza senza indurre una variazione di modello.
	 * Alla variazione del modello incapusalto scatena una rivalidazione dei dati.
	 */
	private class ProxyTableModel extends AbstractTableModel {
		private TableModel _back;
		
		
		public void setModel (final TableModel m) {
			_back = m;
			fireTableStructureChanged ();
		}
		
		public int getRowCount () {
			return _back.getRowCount ();
		}

		public int getColumnCount () {
			return _back.getColumnCount ();
		}

		public String getColumnName (int columnIndex) {
			return _back.getColumnName (columnIndex);
		}

		public Class<?> getColumnClass (int columnIndex) {
			return _back.getColumnClass (columnIndex);
		}

		public boolean isCellEditable (int rowIndex, int columnIndex) {
			return _back.isCellEditable (rowIndex, columnIndex);
		}

		public Object getValueAt (int rowIndex, int columnIndex) {
			return _back.getValueAt (rowIndex, columnIndex);
		}

		public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
			_back.setValueAt (aValue, rowIndex, columnIndex);
		}

		public void addTableModelListener (TableModelListener l) {
			_back.addTableModelListener (l);
		}

		public void removeTableModelListener (TableModelListener l) {
			_back.removeTableModelListener (l);
		}
	}
	
	private final class SystemPropertiesTableModel implements TableModel {

		private final Object[] keys;
		private final Object[] values;
		
		public SystemPropertiesTableModel (){
            this.keys = System.getProperties ().keySet ().toArray (_voidObjectArray);
            this.values = System.getProperties ().values ().toArray (_voidObjectArray);
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
					return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Property");
				case 1:
					return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Value");
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
            this.keys = env.keySet ().toArray (_voidObjectArray);
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
					return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Property");
				case 1:
					return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Value");
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
	
	private final class ApplicationOptionsTableModel implements TableModel {

		private final Map env;
		private final Method[] keys;
		
		public ApplicationOptionsTableModel (){
			this.env = System.getenv ();
            this.keys = ApplicationOptions.class.getDeclaredMethods ();
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
					return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Property");
				case 1:
					return java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Value");
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
					return keys[rowIndex].getName ();
				case 1:

					try {
						return keys[rowIndex].invoke (_context.getApplicationOptions (), _voidObjectArray);
					} catch (IllegalArgumentException ex) {
						ex.printStackTrace();
					} catch (IllegalAccessException ex) {
						ex.printStackTrace();
					} catch (InvocationTargetException ex) {
						ex.printStackTrace();
					}
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

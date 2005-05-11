/*
 * DatastorePath.java
 *
 * Created on 27 aprile 2005, 23.14
 */

package com.ost.timekeeper.wizard.datastore;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.wizard.AbstractStep;
import com.ost.timekeeper.wizard.Director;
import com.ost.timekeeper.wizard.Step;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Passo di configurazione del percorso del datastore.
 *
 * @author  davide
 */
public class DatastorePath extends AbstractStep {
	
	final DataStoreDirector _director;
	
	/**
	 * Costruttore.
	 * @param director il coordinatore.
	 */
	public DatastorePath (final DataStoreDirector director) {
		super (director);
		this._director=director;
	}
	
	public void abort () {
	}
	
	public void apply () {
		_director.setDatastorePath (this._dirPath);
	}
	
	public void configure () {
		this._dirPath = Application.getInstance ().getOptions ().getJDOStorageDirPath ();
	}
	
	private JTextField jdoStorageDirPathEditor;
	public java.awt.Component getUI () {
		final JPanel controlsPanel = new JPanel (new GridBagLayout ());

		this.jdoStorageDirPathEditor = new JTextField (this._dirPath);
		
		jdoStorageDirPathEditor.getDocument ().addDocumentListener (new DocumentListener(){
		    public void insertUpdate(DocumentEvent e){somethingChanged (e);}
		    public void removeUpdate(DocumentEvent e){somethingChanged (e);}
			public void changedUpdate(DocumentEvent e){somethingChanged (e);}
	
			private void somethingChanged (DocumentEvent e){
				DatastorePath.this._dirPath = jdoStorageDirPathEditor.getText ();
				fireStepChanged ();
			}
		});

		boolean validPath = false;
		try {
			new File (this._dirPath);
			validPath = true;
		} catch (Exception e){
			Application.getLogger ().error ("Invalid JDO datastore directory path", e);
		}
		final JFileChooser jdoStorageDirPathChooser = validPath?new JFileChooser(this._dirPath):new JFileChooser ();
		
		final JButton jdoStorageDirPathChoice = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "choose"));
		
		final JLabel jdoStorageDirPathLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "jdostoragedir.path"));
		jdoStorageDirPathLabel.setLabelFor (jdoStorageDirPathEditor);
		jdoStorageDirPathChooser.setFileSelectionMode (jdoStorageDirPathChooser.DIRECTORIES_ONLY);
		jdoStorageDirPathChooser.setFileHidingEnabled (false);
		//		desktopColorChooser.setBorder (new EtchedBorder (EtchedBorder.RAISED));
		jdoStorageDirPathChoice.addActionListener (new ActionListener (){
			public void actionPerformed (ActionEvent ae){
				final int returnVal = jdoStorageDirPathChooser.showOpenDialog (
					Application.getInstance ().getMainForm());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					jdoStorageDirPathEditor.setText (jdoStorageDirPathChooser.getSelectedFile().getPath ());
//					setDataChanged (true);
				}
			}});
		
		final GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
		
		/*
		 * Inserimento componenti editazione percorso directory storage notifica sonora.
		 */
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		controlsPanel.add (jdoStorageDirPathLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		controlsPanel.add (jdoStorageDirPathEditor, c);
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.0;
		controlsPanel.add (jdoStorageDirPathChoice, c);
		
		/* filler */
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weightx = 1.0;
		c.weighty = 1.0;
		controlsPanel.add (new JLabel (), c);
		
		
		final JEditorPane helpPane = new JEditorPane ("text/html", ResourceSupplier.getString (ResourceClass.UI, "controls", "datastore.config.wizard.dir.path.explain.HTML"));
		helpPane.setBackground (controlsPanel.getBackground ());
		final JPanel mainPanel = new JPanel (new BorderLayout ());
		helpPane.setEditable (false);
		helpPane.setFocusable (false);

		mainPanel.add (helpPane, BorderLayout.NORTH);
		mainPanel.add (controlsPanel, BorderLayout.CENTER);
		return mainPanel;
	}
	
	private String _dirPath;
	
	/**
	 * Ritorna <TT>true</TT> se questo passo è valido.
	 * @return <TT>true</TT> se questo passo è valido.
	 */
	public boolean isValid (){
		return _dirPath!=null && _dirPath.trim ().length ()>0;
	}
	
	/**
	 * Ritorna l'icona rappresentativa di questo passo.
	 *
	 * @return l'icona rappresentativa di questo passo.
	 */	
	public Icon getIcon (){
		return ResourceSupplier.getImageIcon (ResourceClass.UI, "datastorepath-huge.png");
	}
	
	
}

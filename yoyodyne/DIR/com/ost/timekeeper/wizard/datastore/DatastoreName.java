/*
 * DatastoreName.java
 *
 * Created on 29 aprile 2005, 00.46
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
 * Passo di configurazione del nome del datastore.
 *
 * @author  davide
 */
public class DatastoreName extends AbstractStep {
	
	final DataStoreDirector _director;
	/**
	 * Costruttore.
	 * @param director il coordinatore.
	 */
	public DatastoreName (final DataStoreDirector director) {
		super (director);
		this._director = director;
	}
	
	public void abort () {
	}
	
	public void apply () {
		_director.setDatastoreName (this._datastoreName);
	}
	
	public void configure () {
		this._datastoreName = Application.getInstance ().getOptions ().getJDOStorageName ();
	}
	
	private JTextField jdoStorageNameEditor;
	public java.awt.Component getUI () {
		final JPanel controlsPanel = new JPanel (new GridBagLayout ());

		final JLabel jdoStorageNameLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "jdostorage.name"));
		this.jdoStorageNameEditor = new JTextField (this._datastoreName);
		
		jdoStorageNameLabel.setLabelFor (jdoStorageNameEditor);
		jdoStorageNameLabel.setText (ResourceSupplier.getString (ResourceClass.UI, "controls", "jdostorage.name"));
		jdoStorageNameEditor.getDocument ().addDocumentListener (new DocumentListener(){
		    public void insertUpdate(DocumentEvent e){somethingChanged (e);}
		    public void removeUpdate(DocumentEvent e){somethingChanged (e);}
			public void changedUpdate(DocumentEvent e){somethingChanged (e);}
	
			private void somethingChanged (DocumentEvent e){
				_datastoreName = jdoStorageNameEditor.getText ();
				fireStepChanged ();
			}
		});
		
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
		controlsPanel.add (jdoStorageNameLabel, c);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		controlsPanel.add (jdoStorageNameEditor, c);
		
		/* filler */
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		controlsPanel.add (new JLabel (), c);
		
		final JEditorPane helpPane = new JEditorPane ("text/html", ResourceSupplier.getString (ResourceClass.UI, "controls", "datastore.config.wizard.datastore.name.explain.HTML"));
		helpPane.setBackground (controlsPanel.getBackground ());
		helpPane.setEditable (false);
		helpPane.setFocusable (false);
		
		final JPanel mainPanel = new JPanel (new BorderLayout ());
		mainPanel.add (helpPane, BorderLayout.NORTH);
		mainPanel.add (controlsPanel, BorderLayout.CENTER);
		return mainPanel;
	}
	
	private String _datastoreName;
	/**
	 * Ritorna <TT>true</TT> se questo passo è valido.
	 * @return <TT>true</TT> se questo passo è valido.
	 */
	public boolean isValid (){
		return _datastoreName!=null && _datastoreName.trim ().length ()>0;
	}
	
	/**
	 * Ritorna l'icona rappresentativa di questo passo.
	 *
	 * @return l'icona rappresentativa di questo passo.
	 */	
	public Icon getIcon (){
		return ResourceSupplier.getImageIcon (ResourceClass.UI, "datastorename-huge.png");
	}
	
	
}

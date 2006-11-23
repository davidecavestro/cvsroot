/*
 * NewProjectAction.java
 *
 * Created on 6 dicembre 2005, 23.14
 */

package com.davidecavestro.timekeeper.actions;

import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.TaskTreeModelImpl;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * Crea e imposta nell'applicazione un nuovo progetto.
 *
 * @author  davide
 */
public class NewProjectAction extends AbstractAction {
	
	private final ApplicationContext _context;
	
	/**
	 * Costruttore.
	 * @param context il contesto applicativo.
	 */
	public NewProjectAction (ApplicationContext context) {
		this._context = context;
		this.putValue (ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke (java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
	}
	
	public void actionPerformed (java.awt.event.ActionEvent e) {
		final TaskTreeModelImpl model = _context.getModel ();
		
		final String projectName = (String)JOptionPane.showInputDialog (this._context.getWindowManager ().getMainWindow (), 
			java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Insert_project_name"),
			java.util.ResourceBundle.getBundle("com.davidecavestro.timekeeper.gui.res").getString("Insert_project_name"),
			JOptionPane.PLAIN_MESSAGE,
			null, null, "blank");
		
		model.setWorkSpace (new Project (projectName, new ProgressItem (projectName))); 
	}
	
	
}

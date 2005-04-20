/*
 * NewProgressDialog.java
 *
 * Created on 17 aprile 2005, 11.33
 */

package com.ost.timekeeper.ui;

import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import java.awt.Component;
import javax.swing.JDialog;

/**
 * Finestra per la crezione di un nuovo avanzamento.
 *
 * @author  davide
 */
public final class NewProgressDialog extends JDialog{
	
	private static NewProgressDialog _instance;
	
	/** Costruttore vuoto. */
	public NewProgressDialog () {
		initComponents ();
	}
	
	public static NewProgressDialog getInstance (){
		if (_instance==null){
			_instance = new NewProgressDialog ();
		}
		return _instance;
	}
	
	private final void initComponents (){
		this.setTitle (ResourceSupplier.getString (ResourceClass.UI, "controls", "New.progress"));
		this._panel = createControlPane ();
		this.getContentPane ().add (_panel);
		pack ();
		/*
		 * Centra sullo schermo.
		 */
		setLocationRelativeTo (null);
	}
	
	private PeriodCreatePanel _panel;
	private PeriodCreatePanel createControlPane (){
		return new PeriodCreatePanel ();
	}
	
	public void show () {
		this._panel.reinit ();
		super.show ();
	}
	
}

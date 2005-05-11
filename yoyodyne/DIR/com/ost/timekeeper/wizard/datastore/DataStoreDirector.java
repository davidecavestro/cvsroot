/*
 * DataStoreDirector.java
 *
 * Created on 25 aprile 2005, 19.39
 */

package com.ost.timekeeper.wizard.datastore;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.conf.UserSettings;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import com.ost.timekeeper.wizard.AbstractDirector;
import com.ost.timekeeper.wizard.Step;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

/**
 * Coordinatore della procedura guidata per la configurazione del datastore.
 *
 * @author  davide
 */
public class DataStoreDirector extends AbstractDirector{
	
	private String _datastoreName;
	private String _datastorePath;
//	private boolean _datastoretested;
	
	/** Costruttore. */
	public DataStoreDirector () {
		super ();
		init (new Step[]{
			new Welcome (this),
			new DatastorePath (this),
			new DatastoreName (this)
		});
		this._datastorePath = Application.getInstance ().getOptions ().getJDOStorageDirPath ();
		this._datastoreName = Application.getInstance ().getOptions ().getJDOStorageName ();
//		this._datastoretested = false;
	}
	
	/**
	 * Ritorna il nome del datastore.
	 *
	 * @return il nome del datastore.
	 */	
	String getDatastoreName (){
		return this._datastoreName;
	}
	
	/**
	 * Imposta il nome del datastore.
	 */	
	void setDatastoreName (final String datastoreName){
		this._datastoreName = datastoreName;
	}
	
	/**
	 * Ritorna il percorso del datastore.
	 *
	 * @return il percorso del datastore.
	 */	
	String getDatastorePath (){
		return this._datastorePath;
	}
	
	/**
	 * Imposta il percorso del datastore.
	 */	
	void setDatastorePath (final String datastorePath){
		this._datastorePath = datastorePath;
	}
	
	/**
	 * Ritorna <TT>true</TT> se e' possibile terminare la procedura con successo.
	 *
	 * @return <TT>true</TT> se e' possibile terminare la procedura con successo.
	 */	
	public boolean canFinish () {
		return this._datastorePath!=null && this._datastorePath.trim ().length ()>0 &&
		this._datastoreName!=null && this._datastoreName.trim ().length ()>0/* &&
		this._datastoretested*/;
	}
	
//	private void datastoreConfChanged (){
//		this._datastoreDetected = false;
//	}
//	
//	void  checkDatastoreConfiguration (){
//		this._datastoreDetected = true;
//	}
	
	private boolean checkForDatastoreExistance (){
		if (this._datastorePath!=null && this._datastoreName!=null){
			try {
				final String[] fileNames = new File  (this._datastorePath).list ();
				for (int i=0;i<fileNames.length;i++){
					final String fileName = fileNames[i];
					if (fileName.startsWith (this._datastoreName+".")){
						return true;
					}
				}
			} catch (final Exception e){
				/* eccezione silenziabile */
				Application.getLogger ().debug ("eeor testing datastore existance", e);
			}
		}
		return false;
	}
	
	
	/**
	 * Applica le modifiche.
	 */
	public boolean finish () {
		if (!super.finish ()){
			return false;
		}
		
		final UserSettings userSettings = UserSettings.getInstance ();
		userSettings.setJDOStorageDirPath (this.getDatastorePath ());
		userSettings.setJDOStorageName (this.getDatastoreName ());
		
		final boolean existingDatastore = checkForDatastoreExistance ();
		
		boolean canCreateDatastore = !existingDatastore;
//		if (existingDatastore){
//			final int response = JOptionPane.showConfirmDialog (
//			Application.getInstance ().getMainForm (), ResourceSupplier.getString (ResourceClass.UI, "controls", "datastorage.init.confirm"));
//			
//			if (response==JOptionPane.CANCEL_OPTION){
//				/* Terminazione non desiderata */
//				return false;
//			} else if (response==JOptionPane.OK_OPTION){
//				canCreateDatastore = true;
//			} 			
//		}
		
		if (canCreateDatastore){
			Application.getInstance ().createDataStore ();
			return true;
		} 
		
		/* imposta solamente la configurazione */
		Application.getInstance ().initPersistence ();
		
		return true;
		
	}
	
}

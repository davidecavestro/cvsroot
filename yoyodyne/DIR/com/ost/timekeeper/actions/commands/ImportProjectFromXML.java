/*
 * ImportProjectFromXML.java
 *
 * Created on 12 marzo 2005, 00.51
 */

package com.ost.timekeeper.actions.commands;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.ApplicationData;
import com.ost.timekeeper.actions.commands.xml.Elements;
import com.ost.timekeeper.help.HelpResource;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import com.ost.timekeeper.ui.StringInputDialog;
import com.ost.timekeeper.util.CalendarUtils;
import com.ost.timekeeper.util.NestedRuntimeException;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Importa il progetto in formato XML.
 *
 * @author  davide
 */
public class ImportProjectFromXML implements Command, Elements {
	
	private Project _project;
	private Project _toDelete;
	private Document _document;
	/**
	 * Costruttore.
	 * @param document il documento da usare per l'importazione.
	 */
	public ImportProjectFromXML (final Document document) {
		this._document=document;
	}
	
	public void execute () {
		final ProjectElement projectElement = new ProjectElement (this._document.getRootElement ());
		this._project = projectElement.getProject ();
		this._toDelete = null;
		if (!checkForConflicts ()){
			return;
		}
		
		final javax.jdo.PersistenceManager pm = Application.getInstance ().getPersistenceManager();
		final javax.jdo.Transaction tx  = pm.currentTransaction();
		tx.begin();
		try {

			if (this._toDelete!=null){
				pm.deletePersistent (this._toDelete);
			}
			pm.makePersistent(this._project);

			tx.commit();
		} catch (final Throwable t){
			tx.rollback ();
			throw new com.ost.timekeeper.util.NestedRuntimeException (t);
		}
	}
	
	/**
	 * Ritorna il progetto importato.
	 *
	 * @return il progetto importato.
	 */	
	public Project getProject (){
		return this._project;
	}
	
	private final class ProgressItemElement extends Element {
		private ProgressItem _progressItem;
		public ProgressItemElement (final Element element, final Project project){
			this._progressItem = new ProgressItem ();
			
			this._progressItem.setCode (element.getChild (CODE_PROPERTY).getValue ());
			this._progressItem.setName (element.getChild (NAME_PROPERTY).getValue ());
			this._progressItem.setDescription (element.getChild (DESCRIPTION_PROPERTY).getValue ());
			this._progressItem.setNotes (element.getChild (NOTES_PROPERTY).getValue ());
			
			final List children = new ArrayList ();
			for (final Iterator it = element.getChildren (PROGRESSITEM_ELEMENT).iterator ();it.hasNext ();){
				final ProgressItem child = new ProgressItemElement ((Element)it.next (), project).getProgressItem ();
				child.setParent (this._progressItem);
				children.add (child);
			}
			this._progressItem.setChildren (children);
			final List progresses = new ArrayList ();
			for (final Iterator it = element.getChildren (PROGRESS_ELEMENT).iterator ();it.hasNext ();){
				final Progress progress = new ProgressElement ((Element)it.next (), this._progressItem).getProgress ();
				progresses.add (progress);
			}
			this._progressItem.setProgresses (progresses);
		}
		
		public ProgressItem getProgressItem (){
			return this._progressItem;
		}
		
	}
	private final class ProjectElement  {
		private Project _project;
		public ProjectElement (final Element element){
			this._project = new Project ();
			this._project.setName (element.getChild (NAME_PROPERTY).getValue ());
			this._project.setDescription (element.getChild (DESCRIPTION_PROPERTY).getValue ());
			this._project.setNotes (element.getChild (NOTES_PROPERTY).getValue ());

			
			this._project.setRoot (new ProgressItemElement (element.getChild (PROGRESSITEM_ELEMENT), this._project).getProgressItem ());
		}
		
		public Project getProject (){
			return this._project;
		}
	}
	private final class ProgressElement extends Element {
		private Progress _progress;
		public ProgressElement (final Element element, final ProgressItem progressItem){
			try {
			this._progress = new Progress (
				CalendarUtils.timestamp2Date (
					element.getChild (FROM_PROPERTY).getValue ()), 
				CalendarUtils.timestamp2Date (
					element.getChild (TO_PROPERTY).getValue ()), 
					progressItem);
			} catch (ParseException pe){
				throw new NestedRuntimeException (pe);
			}
			this._progress.setDescription (element.getChild (DESCRIPTION_PROPERTY).getValue ());
			this._progress.setNotes (element.getChild (NOTES_PROPERTY).getValue ());
			
		}
		public Progress getProgress (){
			return this._progress;
		}
	}
	private final class NullableSingleValueElement extends Element {
		public NullableSingleValueElement (final String type, final String value){
			super (type);
			if (value!=null){
				setText (value);
			}
		}
	}
	
	private boolean checkForConflicts (){
		boolean conflicts = false;
		Project toRemove = null;
		for (final Iterator it = Application.getInstance ().getAvailableProjects ().iterator ();it.hasNext ();){
			final Project existing = (Project)it.next ();
			if (existing.getName ()!=null && existing.getName ().equals (this._project.getName ())){
				conflicts = true;
				toRemove = existing;
				break;
			}
		}
		if (conflicts){
			final String overwrite = ResourceSupplier.getString (ResourceClass.UI, "controls", "overwrite");
			final String rename = ResourceSupplier.getString (ResourceClass.UI, "controls", "rename");
			
			final Object[] possibilities = {overwrite, rename};
			final String choice = (String)JOptionPane.showInputDialog(
                    Application.getInstance ().getMainForm (),
                    ResourceSupplier.getString (ResourceClass.UI, "controls", "project.import.conflict.resolution"),
                    ResourceSupplier.getString (ResourceClass.UI, "controls", "project.import.conflict"),
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    overwrite);
			
			if (choice!=null){
				//si continua
				if (choice.equals (rename)){
					//rinomina
//					JOptionPane.showMessageDialog (
//                    Application.getInstance ().getMainForm (), "rinomina");
					
					this._project.setName (StringInputDialog.supplyString (Application.getInstance ().getMainForm (),
					ResourceSupplier.getString (ResourceClass.UI, "controls", "project.import.newname"),
					ResourceSupplier.getString (ResourceClass.UI, "controls", "project.import.enter_name"),
					true,
					HelpResource.PROJECTIMPORTCONFLICT_NAMEINPUT_DIALOG));
		
					return checkForConflicts ();

				} else if (choice.equals (overwrite)){
					//sovrascrive
					if (
					JOptionPane.showConfirmDialog (
					Application.getInstance ().getMainForm (), ResourceSupplier.getString (ResourceClass.UI, "controls", "project.import.conflict.resolution.overwrite.confirm"))!=JOptionPane.OK_OPTION){
						return false;
					}
					this._toDelete = toRemove;
					return true;
				}
			}
		}
		return true;
	}
	
}

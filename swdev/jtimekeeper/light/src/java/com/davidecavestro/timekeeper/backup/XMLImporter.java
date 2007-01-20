/*
 * XMLImporter.java
 *
 * Created on January 2, 2007, 3:22 PM
 *
 */

package com.davidecavestro.timekeeper.backup;

import com.davidecavestro.common.util.CalendarUtils;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.davidecavestro.timekeeper.model.WorkSpace;
import com.davidecavestro.timekeeper.model.WorkSpaceModelImpl;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.model.Project;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Effettua il restore da file dei dati relativi ad un progetto.
 *
 * @author Davide Cavestro
 */
public class XMLImporter {
	
	final ApplicationContext _context;
	
	/**
	 * Costruttore.
	 */
	public XMLImporter (final ApplicationContext context) {
		_context = context;
	}
	
	/**
	 * Effettua il restore da file dei dati relativi ad un progetto.
	 * Il file usato &egrave; in formato XML.
	 *
	 * @param project il progetto da salvare.
	 * @param document il documento XML da usare.
	 *
	 * @return <CODE>true</CODE> se il ripristino /egrave; andato a buon fine.
	 */
	public boolean restore (final Document document, final ImportConflictsResolver icr) {
		final ProjectElement projectElement = new ProjectElement (document.getRootElement ());
		final WorkSpace prj = projectElement.getProject ();
		return _restore (prj, icr);
	}

	private void makePersistent (final WorkSpace prj, final WorkSpace toRemove) {
		final WorkSpaceModelImpl wm = _context.getWorkSpaceModel ();
			if (toRemove!=null){
				wm.setElementAt (prj, wm.indexOf (toRemove));
			} else {
				wm.addElement (prj);
			}
	}
	
	
	private WorkSpace findConflicts (final WorkSpace prj) {
		for (final Iterator<WorkSpace> it = Arrays.asList (_context.getWorkSpaceModel ().toArray ()).iterator ();it.hasNext ();){
			final WorkSpace existing = it.next ();
			if (existing.getName ()!=null && existing.getName ().equals (prj.getName ())){
				/*
				 * trovato conflitto
				 */
				return existing;
			}
		}
		return null;
	}
	
	private boolean _restore (final WorkSpace prj, final ImportConflictsResolver icr){
		WorkSpace conflicting = null;
		ConflictResolution choice = null;
		do {
			conflicting = findConflicts (prj);

			choice = icr.chooseResolution ();

			if (choice==ConflictResolution.RENAME) {
				/*
				 * rinomina
				 */
				icr.rename (prj);
			}

		} while (choice==ConflictResolution.RENAME);
			
			
		if (choice==ConflictResolution.CANCEL) {
			/*
			 * azione abbandonata
			 */
			return false;
		}
		
		return save (prj, icr, conflicting);
	}
	
	private boolean save (final WorkSpace prj, final ImportConflictsResolver icr, final WorkSpace conflicting) {
		final boolean overwritingCurrentWS = conflicting == _context.getModel ().getWorkSpace ();
		if (overwritingCurrentWS) {
			if (!icr.continueOverwritingCurrentWorkspace ()) {
				return false;
			}
		}
		makePersistent (prj, conflicting);
		if (overwritingCurrentWS) {
			/*
			 * sovrascrittura del progetto corrente.
			 */
			_context.getModel ().setWorkSpace (prj);
		}
		return true;
	}
	
	
	public enum ConflictResolution {
		OVERWRITE {
			public String toString () {
				return "Overwrite";
			}
		},
		RENAME {
			public String toString () {
				return "Rename";
			}
		},
		CANCEL {
			public String toString () {
				return "Cancel";
			}
		}
		
	}
		
	public static abstract class ImportConflictsResolver {
		
		public abstract ConflictResolution chooseResolution ();
		
		public void rename (final WorkSpace prj) {
			prj.setName (getNewName ());
			prj.getRoot ().setName (prj.getName ());
		}
		
		public abstract String getNewName ();

		public abstract boolean continueOverwritingCurrentWorkspace ();
		
	}
	
	
	
	private final class ProgressItemElement extends Element {
		private ProgressItem _progressItem;
		public ProgressItemElement (final Element element, final Project project){
			this._progressItem = new ProgressItem ();
			
			this._progressItem.setCode (element.getChild (XMLResources.CODE_PROPERTY).getValue ());
			this._progressItem.setName (element.getChild (XMLResources.NAME_PROPERTY).getValue ());
			this._progressItem.setDescription (element.getChild (XMLResources.DESCRIPTION_PROPERTY).getValue ());
			this._progressItem.setNotes (element.getChild (XMLResources.NOTES_PROPERTY).getValue ());
			
			final List children = new ArrayList ();
			for (final Iterator it = element.getChildren (XMLResources.PROGRESSITEM_ELEMENT).iterator ();it.hasNext ();){
				try {
					final ProgressItem child = new ProgressItemElement ((Element)it.next (), project).getProgressItem ();
					child.setParent (this._progressItem);
					children.add (child);
				} catch (Exception e){
					_context.getLogger ().error ("error importing node ", e);
				}
			}
			this._progressItem.setChildren (children);
			final List progresses = new ArrayList ();
			for (final Iterator it = element.getChildren (XMLResources.PROGRESS_ELEMENT).iterator ();it.hasNext ();){
				try {
					final Progress progress = new ProgressElement ((Element)it.next (), this._progressItem).getProgress ();
					progresses.add (progress);
				} catch (Exception e){
					_context.getLogger ().error ("error importing progress ", e);
				}
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
			this._project.setName (element.getChild (XMLResources.NAME_PROPERTY).getValue ());
			this._project.setDescription (element.getChild (XMLResources.DESCRIPTION_PROPERTY).getValue ());
			this._project.setNotes (element.getChild (XMLResources.NOTES_PROPERTY).getValue ());
			
			
			this._project.setRoot (new ProgressItemElement (element.getChild (XMLResources.PROGRESSITEM_ELEMENT), this._project).getProgressItem ());
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
					element.getChild (XMLResources.FROM_PROPERTY).getValue ()),
					CalendarUtils.timestamp2Date (
					element.getChild (XMLResources.TO_PROPERTY).getValue ()),
					progressItem);
			} catch (ParseException pe){
				throw new RuntimeException (pe);
			}
			this._progress.setDescription (element.getChild (XMLResources.DESCRIPTION_PROPERTY).getValue ());
			this._progress.setNotes (element.getChild (XMLResources.NOTES_PROPERTY).getValue ());
			
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
}

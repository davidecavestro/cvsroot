/*
 * ReportDataGenerator.java
 *
 * Created on 30 gennaio 2005, 9.56
 */

package com.ost.timekeeper.report;

import com.ost.timekeeper.*;
import com.ost.timekeeper.report.*;
import com.ost.timekeeper.report.ui.*;
import com.ost.timekeeper.util.*;
//import it.businesslogic.ireport.connection.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.*;
import org.jdom.*;
import org.jdom.output.*;

/**
 * Generatore di report.
 *
 * @author  davide
 */
public final class ReportDataGenerator {
	/** Costruttore. */
	public ReportDataGenerator () {
	}
	
	/**
	 * Genera il report.
	 *
	 * @param extractor l'estrattore dei dati.
	 * @param prefs le preferenze di generazione del report.
	 * @param jasperBindings le impostazioni...
	 * @return il report.
	 */
	public void generate ( final DataExtractor extractor, final ReportPreferences prefs, final JRBindings jasperBindings){
		final Document data = extractor.extract ();
		try {
			final File dataFile = File.createTempFile ("data", ".xml");
			
			final XMLOutputter xo = new XMLOutputter ();
			xo.output (data, new FileOutputStream (dataFile));
			
			//			final File output = prefs.getOutput ();
			//@todo completare generazione report
			
			final String reportFileName = jasperBindings.getReportFileName ();
			//			final String outFileName = output.getPath ();
			final String xmlFileName = dataFile.getPath ();
			final String recordPath = jasperBindings.getRecordPath ();
			final JRXmlDataSource jrxmlds = new JRXmlDataSource (xmlFileName,recordPath);
			
			final HashMap hm = new HashMap ();
			
			final JasperPrint print = JasperFillManager.fillReport (
			reportFileName,
			hm,
			jrxmlds);
			
			//			final JRExporter exporter = new net.sf.jasperreports.engine.export.JRPdfExporter ();
			//
			//			exporter.setParameter (JRExporterParameter.OUTPUT_FILE_NAME,outFileName);
			//			exporter.setParameter (JRExporterParameter.JASPER_PRINT,print);
			//
			//			exporter.exportReport ();
			//
			//			System.out.println ("Created file: " + outFileName);
			//
			//			ReportViewer.viewReport (print);
			
			final String taskName = jasperBindings.getTaskType ();
			dispatchAction (taskName, print, null);
		} catch (final Exception e){
			throw new NestedRuntimeException (e);
		}
	}
	
	private void dispatchAction (final String taskName, final JasperPrint print, String fileName) throws JRException {
		
		final JFrame parent = Application.getInstance ().getMainForm ();
		
		if (JRBindings.TASK_PRINT.equals (taskName)) {
			JasperPrintManager.printReport (print, true);
		} else if (JRBindings.TASK_VIEW.equals (taskName)) {
			ReportViewer.viewReport (print);
		} else if (JRBindings.TASK_PDF.equals (taskName)) {
			if (fileName==null){
				fileName = chooseFileName (
				new String []{FileUtils.pdf},
				new String []{ResourceSupplier.getString (ResourceClass.UI, "controls", "file.type.pdf")
				});
			}
			JasperExportManager.exportReportToPdfFile (print, fileName);
			
			JOptionPane.showMessageDialog (parent,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated.at")+":\n "+fileName,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated"),
			JOptionPane.INFORMATION_MESSAGE);

		} else if (JRBindings.TASK_XML.equals (taskName)) {
			if (fileName==null){
				fileName = chooseFileName (
				new String []{FileUtils.xml},
				new String []{ResourceSupplier.getString (ResourceClass.UI, "controls", "file.type.xml")
				});
			}
			JasperExportManager.exportReportToXmlFile (print, fileName, false);
			
			JOptionPane.showMessageDialog (parent,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated.at")+":\n "+fileName,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated"),
			JOptionPane.INFORMATION_MESSAGE);

		} else if (JRBindings.TASK_XML_EMBED.equals (taskName)) {
			if (fileName==null){
				fileName = chooseFileName (
				new String []{FileUtils.xml},
				new String []{ResourceSupplier.getString (ResourceClass.UI, "controls", "file.type.xml.embed")
				});
			}
			JasperExportManager.exportReportToXmlFile (print, fileName, true);
			
			JOptionPane.showMessageDialog (parent,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated.at")+":\n "+fileName,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated"),
			JOptionPane.INFORMATION_MESSAGE);

		} else if (JRBindings.TASK_HTML.equals (taskName)) {
			if (fileName==null){
				fileName = chooseFileName (
				new String []{FileUtils.html},
				new String []{ResourceSupplier.getString (ResourceClass.UI, "controls", "file.type.html")
				});
			}
			JasperExportManager.exportReportToHtmlFile (print, fileName);
			
			JOptionPane.showMessageDialog (parent,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated.at")+":\n "+fileName,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated"),
			JOptionPane.INFORMATION_MESSAGE);

		} else if (JRBindings.TASK_XLS.equals (taskName)) {
			if (fileName==null){
				fileName = chooseFileName (
				new String []{FileUtils.xls},
				new String []{ResourceSupplier.getString (ResourceClass.UI, "controls", "file.type.xls")
				});
			}
			JRXlsExporter exporter = new JRXlsExporter ();
			
			exporter.setParameter (JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter (JRExporterParameter.OUTPUT_FILE_NAME, fileName);
			exporter.setParameter (JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			
			exporter.exportReport ();
			
			JOptionPane.showMessageDialog (parent,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated.at")+":\n "+fileName,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated"),
			JOptionPane.INFORMATION_MESSAGE);

		} else if (JRBindings.TASK_CSV.equals (taskName)) {
			if (fileName==null){
				fileName = chooseFileName (
				new String []{FileUtils.csv},
				new String []{ResourceSupplier.getString (ResourceClass.UI, "controls", "file.type.csv")
				});
			}
			JRCsvExporter exporter = new JRCsvExporter ();
			
			exporter.setParameter (JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter (JRExporterParameter.OUTPUT_FILE_NAME, fileName);
			
			exporter.exportReport ();

			JOptionPane.showMessageDialog (parent,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated.at")+":\n "+fileName,
			ResourceSupplier.getString (ResourceClass.UI, "controls", "report.successfully.generated"),
			JOptionPane.INFORMATION_MESSAGE);

		}
	}
	
	final JFileChooser _chooser = new JFileChooser ();
	
	/**
	 * Permette all'utente di scegliere il percorso del file da generare.
	 *
	 * @param extension l'estensione filtro.
	 * @return
	 */
	private String chooseFileName (final String[] extensions, final String[] descriptions){
		final com.ost.timekeeper.Application app = com.ost.timekeeper.Application.getInstance ();
		
		_chooser.resetChoosableFileFilters ();
		
		if (extensions!=null && extensions.length>0){
			final CustomFileFilter filter = new CustomFileFilter (extensions, descriptions);
			_chooser.addChoosableFileFilter (filter);
		}
		final int returnVal = _chooser.showSaveDialog (app.getMainForm ());
		if (returnVal != JFileChooser.APPROVE_OPTION){
			return null;
		}
		final java.io.File output = _chooser.getSelectedFile ();
		return output.getPath ();
	}
	
	/**
	 * Filtro personalizzato per la scelta dei file.
	 */
	private final class CustomFileFilter extends  javax.swing.filechooser.FileFilter {
		/**
		 * Le estensioni accettate.
		 */
		private String[] _acceptedExtensions;
		/**
		 * Descrizioni delle estensioni.
		 */
		private String[] _descriptions;
		
		/**
		 * Costruttore con estensioni accettatee relative descrizioni.
		 *
		 * @param acceptedExtensions le estensioni accettate.
		 * @param descriptions le descrizioni.
		 */
		public CustomFileFilter (final String[] acceptedExtensions, final String[] descriptions){
			this._acceptedExtensions = acceptedExtensions;
			this._descriptions = descriptions;
		}
		
		/**
		 * Accetta tutte le directory e le estensioni specificate.
		 */
		public boolean accept (File f) {
			if (f.isDirectory ()) {
				return true;
			}
			
			final String extension = FileUtils.getExtension (f, false);
			if (extension != null) {
				for (int i=0;i<_acceptedExtensions.length;i++){
					final String testingExtension = _acceptedExtensions[i];
					if (extension.equalsIgnoreCase (testingExtension)){
						return true;
					}
				}
			}
			
			return false;
		}
		
		//The description of this filter
		public String getDescription () {
			final StringBuffer sb = new StringBuffer ();
			for (int i=0;i<this._descriptions.length;i++){
				if (i>0){
					sb.append (" / ");
				}
				sb.append (this._descriptions[i]);
			}
			return sb.toString ();
		}
	}
	
}

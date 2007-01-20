/*
 * ReportDataGenerator.java
 *
 * Created on 30 gennaio 2005, 9.56
 */

package com.davidecavestro.timekeeper.report;


import com.davidecavestro.common.util.file.CustomFileFilter;
import com.davidecavestro.common.util.file.FileUtils;
import com.davidecavestro.timekeeper.ApplicationContext;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.jdom.Document;

import org.jdom.output.*;

/**
 * Generatore di report.
 *
 * @author  davide
 */
public final class ReportDataGenerator {
	
	private final ApplicationContext _context;
	/** Costruttore. */
	public ReportDataGenerator (final ApplicationContext context) {
		_context = context;
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
		_context.getLogger ().debug ("Starting report generation process");
		
		final StringBuffer logBuffer = new StringBuffer ();
		logBuffer .append ("Generating report data.\n");
		logBuffer .append ("extractor: ").append (extractor).append ("\n");
		logBuffer .append ("preferences: ").append (prefs).append ("\n");
		logBuffer .append ("bindings: ").append (jasperBindings);
		_context.getLogger ().debug (logBuffer.toString ());
				
		final Document data = extractor.extract ();
		try {
			final File dataFile = File.createTempFile ("data", ".xml");
			
			final XMLOutputter xo = new XMLOutputter ();
			xo.output (data, new FileOutputStream (dataFile));
			
			//			final File output = prefs.getOutput ();
			//@todo completare generazione report
			
			final InputStream reportDescriptor = jasperBindings.getReportDescriptor ();
			//			final String outFileName = output.getPath ();
			final String xmlFileName = dataFile.getPath ();
			final String recordPath = jasperBindings.getRecordPath ();
			final JRXmlDataSource jrxmlds = new JRXmlDataSource (xmlFileName,recordPath);
			
			final HashMap hm = new HashMap ();
			
			final JasperPrint print = JasperFillManager.fillReport (
			reportDescriptor,
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
			throw new RuntimeException (e);
		}
		_context.getLogger ().debug ("Report generation process successfully completed");
	}
	
	private void dispatchAction (final String taskName, final JasperPrint print, String fileName) throws JRException {
		
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
			
			JOptionPane.showMessageDialog (_context.getWindowManager ().getMainWindow (),
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
			
			JOptionPane.showMessageDialog (_context.getWindowManager ().getMainWindow (),
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
			
			JOptionPane.showMessageDialog (_context.getWindowManager ().getMainWindow (),
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
			
			JOptionPane.showMessageDialog (_context.getWindowManager ().getMainWindow (),
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
			
			JOptionPane.showMessageDialog (_context.getWindowManager ().getMainWindow (),
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

			JOptionPane.showMessageDialog (_context.getWindowManager ().getMainWindow (),
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
		_chooser.resetChoosableFileFilters ();
		
		if (extensions!=null && extensions.length>0){
			final CustomFileFilter filter = new CustomFileFilter (extensions, descriptions);
			_chooser.addChoosableFileFilter (filter);
		}
		final int returnVal = _chooser.showSaveDialog (_context.getWindowManager ().getMainWindow ());
		if (returnVal != JFileChooser.APPROVE_OPTION){
			return null;
		}
		final java.io.File output = _chooser.getSelectedFile ();
		return output.getPath ();
	}
	
	
}

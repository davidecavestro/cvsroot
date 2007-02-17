/*
 * ReportDataGenerator.java
 *
 * Created on 30 gennaio 2005, 9.56
 */

package com.davidecavestro.timekeeper.report;


import com.davidecavestro.timekeeper.ApplicationContext;
import java.io.*;
import java.util.*;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
		
		final Collection data = extractor.extract ();
		try {
			//			final File output = prefs.getOutput ();
			//@todo completare generazione report
			
			final InputStream reportDescriptor = jasperBindings.getReportDescriptor ();
			//			final String outFileName = output.getPath ();
			final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource (data);
			
			final HashMap hm = new HashMap ();
			
			final JasperPrint print = JasperFillManager.fillReport (
				reportDescriptor,
				hm,
				dataSource);
			
			ReportViewer.viewReport (print);
		} catch (final Exception e){
			throw new RuntimeException (e);
		}
		_context.getLogger ().debug ("Report generation process successfully completed");
	}
	
	
}

/*
 * FileUtils.java
 *
 * Created on 12 dicembre 2004, 11.44
 */

package com.ost.timekeeper.util;

import java.io.*;

/**
 * Utilit� per la gestione dei file.
 *
 * @author  davide
 */
public class FileUtils {
	
	/** Costruttore vuoto e privato (evita istanziazione).*/
	private FileUtils () {
	}
	
	/**
	 * Garantisce l'esistenza del percorso che porta al file specificato.
	 *
	 * @param file il file.
	 */	
	public static void makeFilePath (File file){
		if (file.exists ()){
			/*
			 * File gi� esistente.
			 */
			return;
		} else {
			final File parent = file.getParentFile ();
			if (parent!=null && !parent.exists ()){
				/*
				 * Ha una directory padre, non esistente.
				 * Crea il percorso.
				 */
				parent.mkdirs ();
			}
		}
	}
	
}

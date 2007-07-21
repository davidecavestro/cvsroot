/*
 * DefaultSettings.java
 *
 * Created on 13 settembre 2004, 22.50
 */

package com.davidecavestro.timekeeper.conf;

import java.io.File;

/**
 * Impostazioni di predefinite.
 *
 * @author  davide
 */
public final class DefaultSettings implements ApplicationSettings {
	
	/**
	 * Header file impostazioni.
	 */
	public final static String PROPERTIES_HEADER = "DEFAULT SETTINGS";

	/**
	 * Dimensione predefinita per il buffer del log di testo semplice
	 */
	public final static Integer DEFAULT_PLAINTEXTLOG_BUFFERSIZE = new Integer (8192);
	
	private static DefaultSettings _instance;
	
	private final ApplicationEnvironment _env;
	
	private final UserResources _user;
	
	/** Costruttore. */
	public DefaultSettings (final ApplicationEnvironment environment, final UserResources userResources){
		_env = environment;
		_user = userResources;
	}
	
	/**
	 * Ritorna il percorso della directory contenente i file di log.
	 *
	 * @return il percorso della directory contenente i file di log.
	 */
	public String getLogDirPath () {
		final StringBuffer sb = new StringBuffer ();
		sb.append (_user.getUserApplicationDirPath ());
		sb.append ("/logs");
		return sb.toString ();
	}
	
	/**
	 * Ritorna il lookAndFeel predefinito (JGoodies Plastic3D).
	 * @return il lookAndFeel.
	 */
	public String getLookAndFeel () {
		return com.jgoodies.looks.windows.WindowsLookAndFeel.class.getName ();
//		return UIManager.getSystemLookAndFeelClassName ();
	}
	
	/**
	 * Ritorna la dimensione del buffer per il logger di testo semplice.
	 *
	 * @return la dimensione del buffer per il logger di testo semplice.
	 */
	public Integer getPlainTextLogBufferSize (){
		return DEFAULT_PLAINTEXTLOG_BUFFERSIZE;
	}
	
	public String[] getRecentPaths () {
		return new String[0];
	}
	
	public String getLastPath () {
		return null;
	}
	
	/**
	 * Ritorna sempre Boolean.TRUE.
	 */	
	public Boolean getBackupOnSave () {
		return Boolean.valueOf (backupOnSave ());
	}
	
	/**
	 * Ritorna il valore di default per l'impostazione di salvataggio copie di backup.
	 * Ritorna sempre <TT>true</TT>.
	 */	
	public static boolean backupOnSave () {
		return true;
	}
	
	/**
	 * Ritorna sempre Boolean.FALSE.
	 */	
	public Boolean getKeyEditing () {
		return Boolean.valueOf (keyEditing ());
	}
	
	/**
	 * Ritorna il valore di default per l'impostazione di editazione chiavi.
	 * Ritorna sempre <TT>false</TT>.
	 */	
	public static boolean keyEditing () {
		return false;
	}
	
	/**
	 * Ritorna il valore di default per l'impostazione di scarto dei locale con codifiche non valide.
	 * Ritorna sempre <TT>false</TT>.
	 *
	 * @return il valore di default per l'impostazione di scarto dei locale con codifiche non valide.
	 *
	 * @see #discardMalformedEncoding()
	 */	
	public Boolean getDiscardMalformedEncoding (){
		return Boolean.valueOf (discardMalformedEncoding ());
	}
	
	/**
	 * Ritorna il valore di default per l'impostazione di scarto dei locale con codifiche non valide.
	 * Ritorna sempre <TT>false</TT>.
	 *
	 */	
	public static boolean discardMalformedEncoding (){
		return false;
	}

	public String getJDOStorageName () {
		return "database";
	}

	public String getJDOUserName () {
		return System.getProperty ("user.name");
	}

	public String getJDOStorageDirPath () {
		/*
		 * @workaround
		 * @todo codice da rimuovere, mantenuto solamente per retrocompatibilita' con le prime versioni
		 * di jttslite 2.0 che creavano il database in una posizione centralizzata senza salvarne il percorsonelle preferenze
		 * Cio' comporta che le modifiche apportate al fine di far creare il databse (per le nuove installazioni/nuovi utenti) 
		 * inuna directorydiversa fanno creare un nuovodatabase ignorando quello esistente.
		 * Questo codice consente di lascire in uso il database precedente, ma almeno salvandone il percorso nelle preferenze (comunque pericoloso, inquantosolitamente creato nella directory
		 * di installazione, che potrebbe essere rimossa)
		 * Si potra'pertantorimuovere questo codice, in linea teorica, non appena non ci saranno piu'installazioni di jttslite che NONscrivono il percorso del database nelle preferenze (versioni < 2.0.3).
		 */
		final String s = getCentralizedDataDir ();
		if (new File (s).exists ()) {
			return s;
		}
		return _user.getUserApplicationDataDirPath ();
	}

	public String getLastProjectName () {
		return null;
	}

	/**
	 * Ritorna lo stato predefinito di abilitazione del ghost per il grafico ad anellosulla tabella degli avanzamenti, ovvero sempre <CODE>true</CODE>
	 *
	 * @return lo stato di abilitazione predefinito del ghost per il grafico ad anellosulla tabella degli avanzamenti.
	 */
	public static boolean isChartGhostEnabled () {
		return true;
	}

	/**
	 * @see #isChartGhostEnabled()
	 */
	public Boolean getChartGhostEnabled () {
		return Boolean.valueOf (isChartGhostEnabled ());
	}

	/**
	 * Valore didefault di sistema per la profondit&agrave; del grafico ad anello.
	 */
	public static int defaultChartDepth () {
		return 5;
	}

	public Integer getChartDepth () {
		return defaultChartDepth ();
	}
	
	
	/**
	 * Ritorna ilpercorso della directorycentralizzata per il database.
	 *Viene mantenuto solamente a scopodi retrocompatibilita'con le prime installazioni
	 *di JTTSlite 2.0, che hanno creato il database in tale directory
	 */
	private String getCentralizedDataDir () {
		final StringBuffer sb = new StringBuffer ();
		final String applicationDirPath = _env.getApplicationDirPath ();
		if (applicationDirPath!=null){
			sb.append (applicationDirPath);
		} else {
			sb.append (System.getProperty (ResourceNames.USER_WORKINGDIR_PATH));
		}
		sb.append ("/data");
		return sb.toString ();		
	}
}

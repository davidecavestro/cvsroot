/*
 * HelpResource.java
 *
 * Created on 27 dicembre 2004, 13.45
 */

package com.ost.timekeeper.help;

/**
 * Una risorsa di help. E' costituita da un valore, utilizzabile direttamente 
 * per il recupero di risorse dall'help, oppure come chiave per una mappatura.
 *
 * @author  davide
 */
public final class HelpResource {
	/**
	 * La risorsa.
	 */
	private String _resource;
	
	/** Albero dei nodi di avanzamento. */
	public final static HelpResource PROGRESSITEMTREE = new HelpResource ("html.progressitemtree");
	
	/** Lista degli avanzamenti. */
	public final static HelpResource PROGRESSLIST = new HelpResource ("html.progresslist");
	
	/** Lista degli avanzamenti. */
	public final static HelpResource PROGRESSITEMINSPECTORFRAME = new HelpResource ("html.progressiteminspectorframe");
	
	/** Lista degli avanzamenti. */
	public final static HelpResource PERIODINSPECTORFRAME = new HelpResource ("html.periodinspectorframe");
	
	/** Finestra di seleczione progetto. */
	public final static HelpResource PROJECTSELECTDIALOG = new HelpResource ("html.projectselectdialog");
	
	/** La pulsantiera principale. */
	public final static HelpResource MAINTOOLBAR = new HelpResource ("html.maintoolbar");
	
	/** La finestra di inserimento del nuovo nodo. */
	public final static HelpResource NEWNODEDIALOG = new HelpResource ("html.newnodedialog");
	
	/** La finestra di inserimento del nuovo progetto. */
	public final static HelpResource NEWPROJECTDIALOG = new HelpResource ("html.newprojectdialog");
	
	/** La finestra di modifica preferenze utente. */
	public final static HelpResource USERSETTINGSDIALOG = new HelpResource ("html.usersettingsdialog");
	
	/** La finestra di generazione dei report. */
	public final static HelpResource REPORTGENERATIONDIALOG = new HelpResource ("html.reportgenerationdialog");
	
	/** La finestra di selezione della radice del grafico ad anello. */
	public final static HelpResource RINGCHART_ROOTSELECTION_DIALOG = new HelpResource ("html.ringchartrootselectiondialog");
	
	/** La finestra di gestione grafici. */
	public final static HelpResource CHARTFRAME = new HelpResource ("html.chartframe");
	
	/**
	 * Costruttore con risorsa specificata.
	 *
	 * @param resource il valore della risorsa.
	 */	
	private HelpResource (final String value){
		this._resource = value;
	}
	
	/**
	 * Ritorna il valore di questa risorsa.
	 *
	 * @return il valore di questa risorsa.
	 */
	public String getValue (){
		return this._resource;
	}
	
	/**
	 * Ritorna <TT>true</TT> se questa risorsa è uguale all'oggetto specificato.
	 *
	 * @param obj l'oggetto da confrontare.
	 * @return <TT>true</TT> se questa risorsa è uguale all'oggetto specificato.
	 */	
	public boolean equals (Object obj){
		if (this==obj){
			/* identità*/
			return true;
		} else if (obj==null){
			/* test su NULL*/
			return false;
		} else {
			if (obj instanceof HelpResource){
				/* stesso tipo */
				final HelpResource test = (HelpResource)obj;
				/* testa valore */
				return this._resource==test._resource ||
				(this._resource!=null && this._resource.equals (test._resource));
			} else {
				/* tipo incompatibile*/
				return false;
			}
		}
		
	}
	
	/**
	 * Ritorna la rappresentazione in formato stringa di questa risorsa.
	 *
	 * @return una stringa che rappresenta questa risorsa.
	 */	
	public String toString (){
		final StringBuffer sb = new StringBuffer ();
		sb.append ("resource: ").append (this._resource);
		return sb.toString ();
	}
}

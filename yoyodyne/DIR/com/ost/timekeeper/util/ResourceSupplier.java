/*
 * ResourceSupplier.java
 *
 * Created on 2 maggio 2004, 8.47
 */

package com.ost.timekeeper.util;

import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 * Fornisce risorse quali stringhe localizzate, immagini.
 *
 * @author  davide
 */
public final class ResourceSupplier {
	/**
	 * Il percorso delle risorse localizzate.
	 */
	private final static String BUNDLE_PATH		= "com/ost/timekeeper/ui/bundle/";
	/**
	 * Il percorso delle risorse grafiche.
	 */
	private final static String IMAGE_PATH		= "/com/ost/timekeeper/ui/images/";
	
	/** 
	 * Costruttore vuoto. 
	 */
	private ResourceSupplier() {
	}
	
	/**
	 * Ritorna una risorsa di tipo stringa.
	 *
	 * @param resClass il tipo di risorsa.
	 * @param bundle l'identificatore del bundle di risorse.
	 * @param key la chiave della risorsa localizzata,
	 *
	 * @return la stringa di tipo <TT>resClass</TT> appartenente al bundle <TT>bundle</TT> ed identificata dalla chiave <TT>key</TT>.
	 */	
	public final static String getString (ResourceClass resClass, String bundle, String key){
		String bundlePath = "";
		if (resClass==ResourceClass.UI){
			bundlePath = BUNDLE_PATH;
		}
		try {
			return java.util.ResourceBundle.getBundle(bundlePath+bundle).getString(key);
		} catch (MissingResourceException mre){
			return bundle+"_"+key;
		}
	} 
	
	/**
	 * Ritorna una risorsa di tipo icona.
	 *
	 * @param resClass il tipo di risorsa.
	 * @param name il nome della risorsa.
	 * @return l'icona di tipo <TT>resClass</TT> identificata da <TT>name</TT>.
	 */	
	public final static ImageIcon getImageIcon (ResourceClass resClass, String name) {
		return getImageIcon (resClass, name, true);
	}
	/**
	 * Ritorna una risorsa di tipo icona.
	 *
	 * @return l'icona di tipo <TT>resClass</TT> identificata da <TT>name</TT>.
	 * @param defaultIfMissing specifica se ritornare l'immagine di default in caso di immagine mancante.
	 * @param resClass il tipo di risorsa.
	 * @param name il nome della risorsa.
	 * @throws MissingReourceException
	 */	
	public final static ImageIcon getImageIcon (ResourceClass resClass, String name, boolean defaultIfMissing) throws MissingResourceException{
		String iamgePath = "";
		if (resClass==ResourceClass.UI){
			iamgePath = IMAGE_PATH;
		}
		try {
			return loadIcon (ResourceSupplier.class.getResource(iamgePath+name));
		} catch (MissingResourceException mre) {
			if (defaultIfMissing){
				return getMissingImageIcon ();
			} else {
				throw mre;
			}
		}
	}
	
	/**
	 * Ritorna una icona a partire da una URL.
	 *
	 * @return una icona caricata da <TT>URL</TT>.
	 * @param url l'URL della risorsa.
	 * @throws MissingResourceException nel caso in cui l'icona non sia reperibile.
	 */	
	private final static ImageIcon loadIcon (URL url) throws MissingResourceException {
		try {
			return new javax.swing.ImageIcon(url);
		} catch (Exception e){
			throw new MissingResourceException ("Missing icon", url!=null?url.toString ():"null", "");
		}
		
	}
	
	/**
	 * L'immagine di default, da usare in caso di risorsa mancante.
	 */
	private static ImageIcon missingImageIcon;
	
	public static ImageIcon getMissingImageIcon () {
		if (missingImageIcon==null){
			missingImageIcon = new ImageIcon (
			new byte[] {
				/*
				 * byte immagine default per immagini mancanti.
				 */
				(byte)255, (byte)216, (byte)255, (byte)224, (byte)0, (byte)16, (byte)74, (byte)70, (byte)73, (byte)70, (byte)0, (byte)1, (byte)1, (byte)1, (byte)0, (byte)72, (byte)0, (byte)72, (byte)0, (byte)0, (byte)255, (byte)254, (byte)0, (byte)23, (byte)67, (byte)114, (byte)101, (byte)97, (byte)116, (byte)101, (byte)100, (byte)32, (byte)119, (byte)105, (byte)116, (byte)104, (byte)32, (byte)84, (byte)104, (byte)101, (byte)32, (byte)71, (byte)73, (byte)77, (byte)80, (byte)255, (byte)219, (byte)0, (byte)67, (byte)0, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)255, (byte)219, (byte)0, (byte)67, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)255, (byte)192, (byte)0, (byte)17, (byte)8, (byte)0, (byte)16, (byte)0, (byte)16, (byte)3, (byte)1, (byte)17, (byte)0, (byte)2, (byte)17, (byte)1, (byte)3, (byte)17, (byte)1, (byte)255, (byte)196, (byte)0, (byte)23, (byte)0, (byte)0, (byte)3, (byte)1, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)5, (byte)7, (byte)10, (byte)255, (byte)196, (byte)0, (byte)28, (byte)16, (byte)0, (byte)3, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)5, (byte)6, (byte)7, (byte)4, (byte)3, (byte)8, (byte)2, (byte)1, (byte)22, (byte)255, (byte)196, (byte)0, (byte)24, (byte)1, (byte)0, (byte)3, (byte)1, (byte)1, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)5, (byte)6, (byte)3, (byte)8, (byte)255, (byte)196, (byte)0, (byte)31, (byte)17, (byte)0, (byte)2, (byte)3, (byte)1, (byte)1, (byte)0, (byte)3, (byte)1, (byte)1, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)4, (byte)5, (byte)2, (byte)3, (byte)6, (byte)7, (byte)1, (byte)8, (byte)19, (byte)20, (byte)22, (byte)17, (byte)255, (byte)218, (byte)0, (byte)12, (byte)3, (byte)1, (byte)0, (byte)2, (byte)17, (byte)3, (byte)17, (byte)0, (byte)63, (byte)0, (byte)217, (byte)125, (byte)222, (byte)151, (byte)112, (byte)131, (byte)214, (byte)19, (byte)234, (byte)95, (byte)121, (byte)4, (byte)189, (byte)249, (byte)0, (byte)216, (byte)149, (byte)244, (byte)59, (byte)72, (byte)140, (byte)220, (byte)4, (byte)129, (byte)118, (byte)243, (byte)97, (byte)223, (byte)166, (byte)67, (byte)31, (byte)121, (byte)189, (byte)67, (byte)209, (byte)136, (byte)158, (byte)252, (byte)35, (byte)218, (byte)225, (byte)255, (byte)0, (byte)35, (byte)207, (byte)142, (byte)9, (byte)232, (byte)65, (byte)5, (byte)244, (byte)224, (byte)217, (byte)31, (byte)81, (byte)80, (byte)21, (byte)102, (byte)93, (byte)239, (byte)185, (byte)120, (byte)109, (byte)68, (byte)119, (byte)72, (byte)247, (byte)172, (byte)157, (byte)162, (byte)106, (byte)27, (byte)79, (byte)97, (byte)81, (byte)217, (byte)27, (byte)234, (byte)28, (byte)7, (byte)52, (byte)198, (byte)53, (byte)80, (byte)110, (byte)114, (byte)255, (byte)0, (byte)73, (byte)187, (byte)216, (byte)233, (byte)253, (byte)34, (byte)219, (byte)33, (byte)89, (byte)73, (byte)62, (byte)178, (byte)43, (byte)163, (byte)65, (byte)85, (byte)210, (byte)174, (byte)106, (byte)4, (byte)18, (byte)167, (byte)35, (byte)202, (byte)99, (byte)214, (byte)206, (byte)191, (byte)122, (byte)63, (byte)149, (byte)226, (byte)185, (byte)135, (byte)85, (byte)192, (byte)232, (byte)176, (byte)145, (byte)33, (byte)134, (byte)87, (byte)228, (byte)66, (byte)198, (byte)13, (byte)245, (byte)124, (byte)209, (byte)133, (byte)246, (byte)176, (byte)107, (byte)153, (byte)237, (byte)10, (byte)162, (byte)149, (byte)116, (byte)110, (byte)225, (byte)176, (byte)78, (byte)16, (byte)133, (byte)22, (byte)131, (byte)167, (byte)250, (byte)90, (byte)147, (byte)25, (byte)242, (byte)22, (byte)11, (byte)232, (byte)44, (byte)110, (byte)137, (byte)161, (byte)208, (byte)159, (byte)205, (byte)220, (byte)84, (byte)43, (byte)115, (byte)48, (byte)198, (byte)65, (byte)68, (byte)247, (byte)214, (byte)196, (byte)125, (byte)19, (byte)100, (byte)82, (byte)1, (byte)230, (byte)53, (byte)31, (byte)238, (byte)252, (byte)230, (byte)191, (byte)253, (byte)158, (byte)251, (byte)95, (byte)168, (byte)88, (byte)134, (byte)52, (byte)175, (byte)204, (byte)204, (byte)247, (byte)15, (byte)221, (byte)221, (byte)13, (byte)118, (byte)107, (byte)230, (byte)66, (byte)251, (byte)6, (byte)96, (byte)197, (byte)113, (byte)109, (byte)235, (byte)76, (byte)93, (byte)252, (byte)52, (byte)237, (byte)70, (byte)88, (byte)236, (byte)98, (byte)54, (byte)167, (byte)61, (byte)95, (byte)214, (byte)55, (byte)27, (byte)59, (byte)3, (byte)147, (byte)218, (byte)231, (byte)0, (byte)88, (byte)175, (byte)214, (byte)89, (byte)161, (byte)112, (byte)32, (byte)249, (byte)145, (byte)63, (byte)118, (byte)116, (byte)127, (byte)217, (byte)99, (byte)173, (byte)57, (byte)21, (byte)20, (byte)58, (byte)219, (byte)165, (byte)76, (byte)141, (byte)0, (byte)117, (byte)185, (byte)171, (byte)103, (byte)85, (byte)112, (byte)118, (byte)95, (byte)172, (byte)135, (byte)251, (byte)141, (byte)98, (byte)44, (byte)174, (byte)78, (byte)34, (byte)241, (byte)231, (byte)92, (byte)9, (byte)32, (byte)195, (byte)135, (byte)141, (byte)12, (byte)53, (byte)255, (byte)0, (byte)31, (byte)131, (byte)227, (byte)220, (byte)227, (byte)64, (byte)219, (byte)183, (byte)232, (byte)63, (byte)149, (byte)236, (byte)109, (byte)255, (byte)0, (byte)155, (byte)19, (byte)153, (byte)240, (byte)212, (byte)231, (byte)34, (byte)111, (byte)182, (byte)91, (byte)83, (byte)26, (byte)179, (byte)26, (byte)183, (byte)27, (byte)78, (byte)220, (byte)188, (byte)99, (byte)75, (byte)39, (byte)151, (byte)231, (byte)225, (byte)137, (byte)113, (byte)234, (byte)204, (byte)206, (byte)53, (byte)229, (byte)107, (byte)186, (byte)59, (byte)253, (byte)123, (byte)113, (byte)205, (byte)33, (byte)26, (byte)140, (byte)222, (byte)85, (byte)205, (byte)173, (byte)40, (byte)110, (byte)115, (byte)74, (byte)197, (byte)122, (byte)142, (byte)204, (byte)173, (byte)76, (byte)215, (byte)60, (byte)195, (byte)228, (byte)156, (byte)194, (byte)85, (byte)255, (byte)0, (byte)71, (byte)161, (byte)45, (byte)119, (byte)100, (byte)43, (byte)71, (byte)185, (byte)29, (byte)250, (byte)228, (byte)83, (byte)179, (byte)104, (byte)59, (byte)129, (byte)18, (byte)88, (byte)3, (byte)172, (byte)173, (byte)67, (byte)249, (byte)119, (byte)238, (byte)39, (byte)14, (byte)169, (byte)2, (byte)150, (byte)118, (byte)189, (byte)150, (byte)30, (byte)66, (byte)190, (byte)178, (byte)211, (byte)40, (byte)57, (byte)165, (byte)7, (byte)222, (byte)161, (byte)46, (byte)172, (byte)12, (byte)90, (byte)213, (byte)187, (byte)18, (byte)69, (byte)101, (byte)53, (byte)240, (byte)201, (byte)198, (byte)161, (byte)126, (byte)176, (byte)6, (byte)145, (byte)54, (byte)177, (byte)119, (byte)127, (byte)249, (byte)108, (byte)139, (byte)161, (byte)221, (byte)150, (byte)87, (byte)72, (byte)195, (byte)36, (byte)242, (byte)82, (byte)170, (byte)18, (byte)80, (byte)36, (byte)74, (byte)155, (byte)127, (byte)42, (byte)246, (byte)44, (byte)152, (byte)69, (byte)85, (byte)231, (byte)35, (byte)54, (byte)63, (byte)55, (byte)181, (byte)192, (byte)243, (byte)204, (byte)106, (byte)71, (byte)184, (byte)145, (byte)246, (byte)5, (byte)124, (byte)129, (byte)185, (byte)131, (byte)207, (byte)215, (byte)172, (byte)117, (byte)82, (byte)80, (byte)49, (byte)188, (byte)185, (byte)87, (byte)150, (byte)3, (byte)94, (byte)125, (byte)167, (byte)47, (byte)12, (byte)34, (byte)216, (byte)187, (byte)117, (byte)211, (byte)236, (byte)170, (byte)166, (byte)5, (byte)81, (byte)209, (byte)52, (byte)23, (byte)32, (byte)27, (byte)157, (byte)88, (byte)124, (byte)111, (byte)196, (byte)228, (byte)110, (byte)223, (byte)41, (byte)203, (byte)117, (byte)76, (byte)201, (byte)15, (byte)154, (byte)86, (byte)34, (byte)135, (byte)126, (byte)164, (byte)216, (byte)245, (byte)207, (byte)13, (byte)249, (byte)53, (byte)58, (byte)120, (byte)180, (byte)18, (byte)23, (byte)211, (byte)239, (byte)187, (byte)32, (byte)235, (byte)132, (byte)231, (byte)168, (byte)29, (byte)154, (byte)134, (byte)112, (byte)148, (byte)181, (byte)241, (byte)250, (byte)192, (byte)73, (byte)62, (byte)151, (byte)60, (byte)94, (byte)79, (byte)252, (byte)8, (byte)50, (byte)127, (byte)72, (byte)250, (byte)48, (byte)161, (byte)71, (byte)24, (byte)37, (byte)119, (byte)18, (byte)245, (byte)32, (byte)61, (byte)109, (byte)179, (byte)113, (byte)155, (byte)17, (byte)17, (byte)34, (byte)214, (byte)169, (byte)111, (byte)245, (byte)84, (byte)38, (byte)190, (byte)252, (byte)160, (byte)107, (byte)198, (byte)161, (byte)31, (byte)190, (byte)200, (byte)154, (byte)221, (byte)174, (byte)246, (byte)137, (byte)202, (byte)168, (byte)170, (byte)43, (byte)207, (byte)107, (byte)176, (byte)70, (byte)75, (byte)199, (byte)19, (byte)232, (byte)168, (byte)6, (byte)63, (byte)112, (byte)140, (byte)106, (byte)168, (byte)120, (byte)14, (byte)198, (byte)150, (byte)197, (byte)206, (byte)231, (byte)22, (byte)29, (byte)67, (byte)107, (byte)129, (byte)233, (byte)138, (byte)163, (byte)191, (byte)36, (byte)125, (byte)130, (byte)206, (byte)253, (byte)163, (byte)216, (byte)58, (byte)105, (byte)212, (byte)161, (byte)26, (byte)146, (byte)153, (byte)204, (byte)54, (byte)85, (byte)181, (byte)26, (byte)131, (byte)173, (byte)222, (byte)161, (byte)178, (byte)37, (byte)133, (byte)162, (byte)197, (byte)108, (byte)28, (byte)104, (byte)189, (byte)102, (byte)118, (byte)183, (byte)25, (byte)226, (byte)237, (byte)14, (byte)52, (byte)230, (byte)14, (byte)9, (byte)113, (byte)140, (byte)99, (byte)207, (byte)208, (byte)10, (byte)183, (byte)156, (byte)135, (byte)255, (byte)217
				}
			);
		}
		return missingImageIcon;
	}
}

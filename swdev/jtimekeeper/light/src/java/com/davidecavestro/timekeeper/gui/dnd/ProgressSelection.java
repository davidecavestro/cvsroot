/*
 * ProgressSelection.java
 *
 * Created on 09 marzo 2005, 00.05
 */

package com.davidecavestro.timekeeper.gui.dnd;

import com.davidecavestro.timekeeper.model.PieceOfWork;
import com.ost.timekeeper.model.*;
import java.awt.datatransfer.*;

/**
 * La selezione contenente di un avanzamento.
 *
 * @author  davide
 */
public class ProgressSelection implements Transferable{
	
	/** I tipi di dato supportati. */
	private static final DataFlavor[] flavors = {
		DataFlavors.progressFlavor
	};
	
    private PieceOfWork[] data;
						   
	/**
	 * Costruttore con avanzamenti.
	 * @param data gli avanzamenti.
	 */
	public ProgressSelection (PieceOfWork[] data) {
        this.data = data;
    }
	
	/**
	 * Ritorna il dato trasportato, in base al tipo di dato specificato.
	 *
	 * @param flavor il tipo di dato desiderato.
	 * @throws UnsupportedFlavorException
	 * @throws IOException
	 * @return il dato trasportato, in base al tipo di dato specificato.
	 */	
	public Object getTransferData (DataFlavor flavor) throws UnsupportedFlavorException, java.io.IOException {
		if (flavor.equals (DataFlavors.progressFlavor)) {
			return (PieceOfWork[])data;
		} else {
			throw new UnsupportedFlavorException (flavor);
		}
	}
	
	/**
	 * Ritorna la lista dei tipi con cui il dato trasportato pu� essere restituito.
	 * I tipi supportati sono:
	 *	<UL>
	 *		<LI>{@link DataFlavors#progressFlavor}.
	 *	</UL>
	 *
	 * @return la lista dei tipi con cui il dato trasportato pu� essere restituito.
	 */	
	public DataFlavor[] getTransferDataFlavors () {
		return (DataFlavor[])flavors.clone ();
	}
	
	/**
	 * Ritorna <TT>true</TT> se il tipo di dato � supportato per questa selezione.
	 *
	 * @param flavor il tipo di dato. 
	 * @return <TT>true</TT> se il tipo di dato � supportato per questa selezione.
	 */	
	public boolean isDataFlavorSupported (DataFlavor flavor) {
		for (int i = 0; i < flavors.length; i++) {
			if (flavor.equals (flavors[i])) {
				return true;
			}
		}
		return false;
	}
}

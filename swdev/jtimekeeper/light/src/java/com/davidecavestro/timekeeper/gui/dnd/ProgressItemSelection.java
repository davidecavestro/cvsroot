/*
 * ProgressItemSelection.java
 *
 * Created on 28 dicembre 2004, 18.44
 */

package com.davidecavestro.timekeeper.gui.dnd;

import com.davidecavestro.timekeeper.model.Task;
import java.awt.datatransfer.*;
import java.io.IOException;
import javax.swing.TransferHandler;

/**
 * La selezione contenente di un nodo di avanzamento.
 *
 * @author  davide
 */
public class ProgressItemSelection implements MemoTransferable {
	
	/** I tipi di dato supportati. */
	private static final DataFlavor[] flavors = {
		DataFlavors.progressItemFlavor
	};
	
    private Task[] data;
	private int _action = TransferHandler.NONE;
						   
	/**
	 * Costruttore con nodo di avanzamento.
	 * @param data il nodo di avanzamento.
	 */
	public ProgressItemSelection (Task[] data) {
        this.data = data;
    }
	
	public void setAction (int action) {
		_action = action;
	}
	
	/**
	 * Ritorna il dato trasportato, in base al tipo di dato specificato.
	 *
	 * @param flavor il tipo di dato desiderato.
	 * @throws UnsupportedFlavorException
	 * @throws IOException
	 * @return il dato trasportato, in base al tipo di dato specificato.
	 */	
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (flavor.equals (DataFlavors.progressItemFlavor)) {
			return new TransferData<Task> (data, _action);
		} else {
			throw new UnsupportedFlavorException (flavor);
		}
	}
	
	/**
	 * Ritorna la lista dei tipi con cui il dato trasportato pu� essere restituito.
	 * I tipi supportati sono:
	 *	<UL>
	 *		<LI>{@link DataFlavors#progressItemFlavor}.
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

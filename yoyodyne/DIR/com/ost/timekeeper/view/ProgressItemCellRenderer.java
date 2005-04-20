/*
 * PRogressItemCellRenderer.java
 *
 * Created on 25 aprile 2004, 11.16
 */

package com.ost.timekeeper.view;

import com.ost.timekeeper.Application;
import com.ost.timekeeper.model.Progress;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JTree;

/**
 * Implementa comportamenti custom per la visualizzazione delle celle dell'albero dei nodi di avanzamento.
 *
 * @author  davide
 */
public class ProgressItemCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
	
	/** 
	 * Costruttore vuoto. 
	 */
	public ProgressItemCellRenderer() {
		
	}
	final ImageIcon progressingIcon = ResourceSupplier.getImageIcon (ResourceClass.UI, "gear_small.gif");
	final Image progressingImage = progressingIcon.getImage (); 
//    if (progressingIcon != null) {
//        tree.setCellRenderer(new MyRenderer(tutorialIcon));
//    }
	private Object value;
    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {
						this.value=value;
        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
//        if (isProgressing(value)) {
//            setToolTipText(ResourceSupplier.getString (ResourceClass.UI, "controls", "progress.in.subtree"));
//        } else {
//            setToolTipText(null); //no tool tip
//        } 

        return this;
    }

    public void paint(Graphics g) {
		super.paint (g);
        if (isProgressing(value)) {
			g.drawImage (progressingImage, 0, 0, null);
//            setIcon(progressingIcon);
		}
	}
    private boolean isProgressing(Object value) {
		final ProgressItem node = (ProgressItem)value;
		if (node.isProgressing ()){
			return true;
		}
		for (final Iterator it = node.getSubtreeProgresses ().iterator ();it.hasNext ();){
			final Progress progress = (Progress)it.next ();
			try{
			if (progress.isEndOpened ()){
				return true;
			}
			} catch (javax.jdo.JDOObjectNotFoundException e){Application.getLogger ().error (com.ost.timekeeper.util.ExceptionUtils.getStackTrace (e).toString ());}
		}
		return false;
    }
	
}

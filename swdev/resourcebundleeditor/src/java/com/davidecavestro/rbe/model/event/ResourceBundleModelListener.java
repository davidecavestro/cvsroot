/*
 * ResourceBundleModelListener.java
 *
 * Created on 2 dicembre 2005, 0.32
 */

package com.davidecavestro.rbe.model.event;

/**
 *
 * @author  davide
 */
public interface ResourceBundleModelListener extends java.util.EventListener {
    /**
     * Questa notifica informa i listener che sono cambiati i locale o le entry del ResourceBundleModel..
     */
    public void resourceBundleChanged (ResourceBundleModelEvent e);
}

/*
 * ProgressListener.java
 *
 * Created on 18 aprile 2004, 10.42
 */

package com.ost.timekeeper.model;

import java.util.*;

/**
 * Interfaccia per la notifica degli eventi di avanzamento.
 * @author  davide
 */
public interface ProgressListener extends EventListener{
	public void progressStarted (ProgressEvent e);
	public void progressStopped (ProgressEvent e);
	public void progressAdded (ProgressEvent e);
	public void progressRemoved (ProgressEvent e);
	public void nodeAdded (ProgressEvent e);
	public void nodeRemoved (ProgressEvent e);
}

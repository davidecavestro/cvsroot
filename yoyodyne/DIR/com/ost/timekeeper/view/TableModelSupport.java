
package com.ost.timekeeper.view;

import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;

public class TableModelSupport {
   private Vector vector = new Vector();

   public void addTableModelListener( TableModelListener listener ) {
      if ( listener != null && !vector.contains( listener ) ) {
         vector.addElement( listener );
      }
   }

   public void removeTableModelListener( TableModelListener listener ) {
      if ( listener != null ) {
         vector.removeElement( listener );
      }
   }

   public void fireTableChanged( TableModelEvent e ) {
      Enumeration listeners = vector.elements();
      while ( listeners.hasMoreElements() ) {
         TableModelListener listener = (TableModelListener)listeners.nextElement();
         listener.tableChanged( e );
      }
   }

}

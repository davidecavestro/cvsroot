package com.davidecavestro.timekeeper.gui;
/*
  Copyright (c) 2006, Ulrich Hilger, Light Development, http://www.lightdev.com
  All rights reserved.

  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:

    - Redistributions of source code must retain the above copyright notice, this 
       list of conditions and the following disclaimer.
       
    - Redistributions in binary form must reproduce the above copyright notice, 
       this list of conditions and the following disclaimer in the documentation 
       and/or other materials provided with the distribution.
       
    - Neither the name of Light Development nor the names of its contributors may be 
       used to endorse or promote products derived from this software without specific 
       prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
  SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT 
  OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;



/**
 * <p>A drop target for class <code>JXTreeTable</code> that implements autoscrolling, automatic 
 * tree node expansion and a custom drag image during the drag part of a drag and drop 
 * operation.</p> 
 * 
 * <p>This class actually is a copy of TreeDropTarget slightly 
 * adapted for use with JXTreeTable.</p>
 * 
 * @author Ulrich Hilger
 * @author Light Development
 * @author <a href="http://www.lightdev.com">http://www.lightdev.com</a>
 * @author <a href="mailto:info@lightdev.com">info@lightdev.com</a>
 * @author published under the terms and conditions of the BSD License,
 *      for details see file license.txt in the distribution package of this software
 *
 * @version 1, 30.03.2006
 */
public class TreeTableDropTarget /*extends DropTarget */ implements DropTargetListener {

	/**
	 * constructor
	 * @param the transfer handler that provides the drag image for the currently dragged node
	 */
  public TreeTableDropTarget(/*TreeTableNodeMover h*/) {
    super();
//    this.handler = h;
  }

  /* -------------- DropTargetListener start ----------------- */

  /**
   * use method dragOver to constantly update the drag mark and darg image as 
   * well as to support automatic scrolling durng a drag operation
   */
  public void dragOver(DropTargetDragEvent dtde) {
    Point loc = dtde.getLocation();
    JXTreeTable tree = (JXTreeTable) dtde.getDropTargetContext().getComponent();
    updateDragMark(tree, loc);
    paintImage(tree, loc);
    autoscroll(tree, loc);
//    super.dragOver(dtde);
  }

  /**
   * clear the drawings on exit
   */
  public void dragExit(DropTargetDragEvent dtde) {
    clearImage((JXTreeTable) dtde.getDropTargetContext().getComponent());
//    super.dragExit(dtde);
  }

  /**
   * clear the drawings on drop
   */
  public void drop(DropTargetDropEvent dtde) {
    clearImage((JXTreeTable) dtde.getDropTargetContext().getComponent());
//	System.out.println ("dropping...");
//    super.drop(dtde);
  }

  /* ----------------- DropTartgetListener end ------------------ */

  /* ----------------- drag image painting start ------------------ */

  /**
   * paint the dragged node
   */
  private final void paintImage(JXTreeTable tree, Point pt) {
//  	BufferedImage image = handler.getDragImage(tree);
//  	if(image != null) {
//  		tree.paintImmediately(rect2D.getBounds());
//  		rect2D.setRect((int) pt.getX()-15,(int) pt.getY()-15,image.getWidth(),image.getHeight());
//  		tree.getGraphics().drawImage(image,(int) pt.getX()-15,(int) pt.getY()-15,tree);
//  	}
  }
  
  /**
   * clear drawings
   */
  private final void clearImage(JXTreeTable tree) {
    tree.paintImmediately(rect2D.getBounds());
  }

  /* ----------------- drag image painting end ------------------ */

  /* ----------------- autoscroll implementation start ------------------ */

  private Insets getAutoscrollInsets() {
    return autoscrollInsets;
  }

  /**
   * scroll visible tree parts when user drags outside an 'inner part' of 
   * the visible region
   */
  private void autoscroll(JXTreeTable tree, Point cursorLocation) {
    Insets insets = getAutoscrollInsets();
    Rectangle outer = tree.getVisibleRect();
    Rectangle inner = new Rectangle(
        outer.x+insets.left,
        outer.y+insets.top,
        outer.width-(insets.left+insets.right),
        outer.height-(insets.top+insets.bottom));
    if (!inner.contains(cursorLocation))  {
      Rectangle scrollRect = new Rectangle(
          cursorLocation.x-insets.left,
          cursorLocation.y-insets.top,
          insets.left+insets.right,
          insets.top+insets.bottom);
      tree.scrollRectToVisible(scrollRect);
    }
  }

  /* ----------------- autoscroll implementation end ------------------ */

  /* ----------------- insertion mark painting start ------------------ */

  /**
   * manage display of a drag mark either highlighting a node or drawing an
   * insertion mark
   */
  public void updateDragMark(JXTreeTable tree, Point location) {
  	mostRecentLocation = location;
    int row = tree.getRowForPath(tree.getPathForLocation(location.x, location.y));
    TreePath path = tree.getPathForRow(row);
    if(path != null) {
      Rectangle rowBounds = tree.getCellRect(row, 0, false); // tree.getPathBounds(path);
      /*
       * find out if we have to mark a tree node or if we 
       * have to draw an insertion marker
       */ 
      int rby = rowBounds.y;
      int topBottomDist = insertAreaHeight / 2;
      // x = top, y = bottom of insert area
      Point topBottom = new Point(rby - topBottomDist, rby + topBottomDist);
      if(topBottom.x <= location.y && topBottom.y >= location.y) {
        // we are inside an insertArea
        paintInsertMarker(tree, location);
      }
      else {
        // we are inside a node
        markNode(tree, location);
      }
    }
  }
  
  /**
   * get the most recent mouse location, i.e. the drop location when called upon drop
   * @return the mouse location recorded most recently during a drag operation
   */
  public Point getMostRecentDragLocation() {
  	return mostRecentLocation;
  }
  
  /**
   * mark the node that is closest to the current mouse location 
   */
  private void markNode(JXTreeTable tree, Point location) {
    TreePath path = tree.getPathForLocation(location.x, location.y);
    int row = tree.getRowForPath(path);
    if(path != null) {
      if(lastRowBounds != null) {
        Graphics g = tree.getGraphics();
        g.setColor(Color.white);
        g.drawLine(lastRowBounds.x, lastRowBounds.y, 
        		lastRowBounds.x + lastRowBounds.width, lastRowBounds.y);
      }
//      tree.setRowSelectionInterval(row, row);
      //tree.setSelectionPath(path);
//      tree.expandPath(path);
    }
  }

  /**
   * paint an insert marker between the nodes closest to the current mouse location
   */
  private void paintInsertMarker(JXTreeTable tree, Point location) {
    Graphics g = tree.getGraphics();
    tree.clearSelection();
    int row = tree.getRowForPath(tree.getPathForLocation(location.x, location.y));
    TreePath path = tree.getPathForRow(row);
    if(path != null) {
      Rectangle rowBounds = tree.getCellRect(row, 0, false); //tree.getPathBounds(path);
	  ((Graphics2D)g).setStroke (new BasicStroke(5.5f));
      if(lastRowBounds != null) {
        g.setColor(Color.white);
        g.drawLine(lastRowBounds.x, lastRowBounds.y, 
        		lastRowBounds.x + lastRowBounds.width, lastRowBounds.y);
      }
      if(rowBounds != null) {
        g.setColor(Color.red);
        g.drawLine(rowBounds.x, rowBounds.y, rowBounds.x + rowBounds.width, rowBounds.y);
      }
      lastRowBounds = rowBounds;
	  
//	  System.out.println (""+System.currentTimeMillis ()+" painting insert marker");
    }
  }

  /* ----------------- insertion mark painting end ------------------ */

  /* ----------------- class fields ------------------ */

  /** bounding rectangle of the last row a dragOver was recorded for */
  private Rectangle lastRowBounds;

  /** height of the gap between any two node rows to treat as an area for inserts */
  private int insertAreaHeight = 8;
  
  /** insets for autoscroll */
  private Insets autoscrollInsets = new Insets(20, 20, 20, 20);
  
  /** rectangle to clear (where the last image was drawn) */
  private Rectangle rect2D = new Rectangle();
  
  /** the transfer handler that provides the image for the currently dragged node */
//  private TreeTableNodeMover handler;
  
  private Point mostRecentLocation;

  
  
  
  
  
  
  
  
  
  
  

    public void dragEnter(DropTargetDragEvent dtde) {}

//    public void dragOver(DropTargetDragEvent dtde) {}

    public void dropActionChanged(DropTargetDragEvent dtde) {}

    public void dragExit(DropTargetEvent dte) {}

//    public void drop(DropTargetDropEvent dtde) {}
  
}

package com.tomtessier.scrollabledesktop;

import com.jgoodies.plaf.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.border.*;

/**
 * This class provides a custom internal frame. Each internal frame 
 * is assigned an associated toggle button and an optional radio button 
 * menu item. These buttons reside in the 
 * {@link com.tomtessier.scrollabledesktop.DesktopResizableToolBar 
 * DesktopResizableToolBar} and
 * {@link com.tomtessier.scrollabledesktop.DesktopMenu DesktopMenu}.
 * classes respectively.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @author <a href="mailto:francesco.furfari@guest.cnuce.cnr.it">Francesco Furfari</a>
 * @version 1.0  9-Aug-2001
 */

public class BaseInternalFrame extends JInternalFrame {

      private JToggleButton associatedButton;
      private JRadioButtonMenuItem associatedMenuButton;

      private boolean isClosable;
      private int initialWidth;
      private int initialHeight;

     /** 
       *  creates the BaseInternalFrame
       *
       * @param title the string displayed in the title bar of the internal frame
       * @param icon the ImageIcon displayed in the title bar of the internal frame
       * @param frameContents the contents of the internal frame
       * @param isClosable determines whether the frame is closable
       */
      public BaseInternalFrame(String title, 
                              ImageIcon icon, JPanel frameContents, 
                              boolean isClosable) {

            super(title, // title
                  true, //resizable
                  isClosable, //closable
                  true, //maximizable
                  true);//iconifiable

            this.isClosable = isClosable;

            setBackground(Color.white);
            setForeground(Color.blue);

            if (icon != null) {
                  setFrameIcon(icon);
            }

            // add the window contents
            getContentPane().add(frameContents);
            pack();

            saveSize();

            setVisible(true); // turn the frame on
      }

      private void saveSize() {
            initialWidth = getWidth();
            initialHeight = getHeight();
      }

      /**
        * constructor provided for compatibility with JInternalFrame
        */
      public BaseInternalFrame() {
            super();
            saveSize();
      }
      /**
        * constructor provided for compatibility with JInternalFrame
        */
      public BaseInternalFrame(String title) {
            super(title);
            saveSize();
      }
      /**
        * constructor provided for compatibility with JInternalFrame
        */
      public BaseInternalFrame(String title, boolean resizable) {
            super(title, resizable);
            saveSize();
      }
      /**
        * constructor provided for compatibility with JInternalFrame
        */
      public BaseInternalFrame(String title, boolean resizable, boolean closable) {
            super(title, resizable, closable);
            this.isClosable = isClosable;
            saveSize();
      }
      /**
        * constructor provided for compatibility with JInternalFrame
        */
      public BaseInternalFrame(String title, boolean resizable, boolean closable,
                              boolean maximizable) {
            super(title, resizable, closable, maximizable);
            this.isClosable = isClosable;
            saveSize();
      }
      /**
        * constructor provided for compatibility with JInternalFrame
        */
      public BaseInternalFrame(String title, boolean resizable, boolean closable,
                              boolean maximizable, boolean iconifiable) {
            super(title, resizable, closable, maximizable, iconifiable);
            this.isClosable = isClosable;
            saveSize();
      }


     /** 
       *  sets the associated menu button
       *
       * @param associatedMenuButton the menu button to associate with 
       * the internal frame
       */
      public void setAssociatedMenuButton(JRadioButtonMenuItem associatedMenuButton) {
            this.associatedMenuButton = associatedMenuButton;
      }
     /** 
       *  returns the associated menu button
       *
       * @return the JRadioButtonMenuItem object associated with this internal frame
       */
      public JRadioButtonMenuItem getAssociatedMenuButton() {
            return associatedMenuButton;
      }

     /** 
       *  sets the associated toggle button
       *
       * @param associatedButton the toggle button to associate with 
       * the internal frame
       */
      public void setAssociatedButton(JToggleButton associatedButton) {
            this.associatedButton = associatedButton;
      }
     /** 
       *  returns the associated toggle button
       *
       * @return the JToggleButton object associated with this internal frame
       */
      public JToggleButton getAssociatedButton() {
            return associatedButton;
      }

     /** 
       *  returns the initial dimensions of this internal frame. Necessary so that
       * internal frames can be restored to their default sizes when the cascade
       * frame positioning mode is chosen in 
       * {@link com.tomtessier.scrollabledesktop.FramePositioning FramePositioning}.
       *
       * @return the Dimension object representing the initial dimensions of
       * this internal frame
       */
      public Dimension getInitialDimensions() {
            return new Dimension(initialWidth, initialHeight);
      }

     /** 
       *  returns the toString() representation of this object. Useful for 
       * debugging purposes.
       *
       * @return the toString() representation of this object
       */
      public String toString() {
            return "BaseInternalFrame: " + getTitle();
      }


     /** 
       *  selects the current frame, along with any toggle and menu 
       * buttons that may be associated with it
       */ 
      public void selectFrameAndAssociatedButtons() {

            // select associated toolbar button
            if (associatedButton != null) {
                  associatedButton.setSelected(true);
                  ((BaseToggleButton)associatedButton).
                        flagContentsChanged(false);
            }

            // select menu button
            if (associatedMenuButton != null) {
                  associatedMenuButton.setSelected(true);
            }

            try {
                  setSelected(true);
                  setIcon(false);  // select and de-iconify the frame
            } catch (java.beans.PropertyVetoException pve) {
                  System.out.println(pve.getMessage());
            }

            setVisible(true); // and make sure the frame is turned on

      }


     /** 
       *  saves the size of the current internal frame for those frames whose
       * initial width and heights have not been set. Called when the internal
       * frame is added to the JDesktopPane. 
       * <BR><BR>
       * Manually-built internal frames won't display properly without this.
       * <BR><BR>
       * Fix by <a href="mailto:francesco.furfari@guest.cnuce.cnr.it">Francesco Furfari</a>
       *
       */ 
      public void addNotify() {
            super.addNotify();
            if (initialWidth == 0 && initialHeight == 0) {
                  saveSize();
            }
      }
 
//    /**
//     * Updates the header.
//     */
//    private void updateHeader() {
////        gradientPanel.setBackground(getHeaderBackground());
////        gradientPanel.setOpaque(isSelected());
////        titleLabel.setForeground(getTextForeground(isSelected()));
////        headerPanel.repaint();
//    }
//    
//
//    /**
//     * Updates the UI. In addition to the superclass behavior, we need
//     * to update the header component.
//     */
//    public void updateUI() {
//        super.updateUI();
////        if (titleLabel != null) {
//            updateHeader();
////        }
//    }
//
//    /**
//     * Determines and answers the header's text foreground color.
//     * Tries to lookup a special color from the L&amp;F.
//     * In case it is absent, it uses the standard internal frame forground.
//     * 
//     * @param selected   true to lookup the active color, false for the inactive
//     * @return the color of the foreground text
//     */
//    protected Color getTextForeground(boolean selected) {
//        Color c =
//            UIManager.getColor(
//                selected
//                    ? "SimpleInternalFrame.activeTitleForeground"
//                    : "SimpleInternalFrame.inactiveTitleForeground");
//        if (c != null) {
//            return c;
//        }
//        return UIManager.getColor(
//            selected 
//                ? "InternalFrame.activeTitleForeground" 
//                : "Label.foreground");
//
//    }
//
//    /**
//     * Determines and answers the header's background color.
//     * Tries to lookup a special color from the L&amp;F.
//     * In case it is absent, it uses the standard internal frame background.
//     * 
//     * @return the color of the header's background
//     */
//    protected Color getHeaderBackground() {
//        Color c =
//            UIManager.getColor("SimpleInternalFrame.activeTitleBackground");
//        if (c != null)
//            return c;
//        if (LookUtils.IS_LAF_WINDOWS_XP_ENABLED)
//            c = UIManager.getColor("InternalFrame.activeTitleGradient");
//        return c != null
//            ? c
//            : UIManager.getColor("InternalFrame.activeTitleBackground");
//    }
//
//    // Helper Classes *******************************************************
//
//    // A custom border for the raised header pseudo 3D effect.
//    private static class RaisedHeaderBorder extends AbstractBorder {
//
//        private static final Insets INSETS = new Insets(1, 1, 1, 0);
//
//        public Insets getBorderInsets(Component c) { return INSETS; }
//
//        public void paintBorder(Component c, Graphics g,
//            int x, int y, int w, int h) {
//                
//            g.translate(x, y);
//            g.setColor(UIManager.getColor("controlLtHighlight"));
//            g.fillRect(0, 0,   w, 1);
//            g.fillRect(0, 1,   1, h-1);
//            g.setColor(UIManager.getColor("controlShadow"));
//            g.fillRect(0, h-1, w, 1);
//            g.translate(-x, -y);
//        }
//    }
//
//    // A custom border that has a shadow on the right and lower sides.
//    private static class ShadowBorder extends AbstractBorder {
//
//        private static final Insets INSETS = new Insets(1, 1, 3, 3);
//
//        public Insets getBorderInsets(Component c) { return INSETS; }
//
//        public void paintBorder(Component c, Graphics g,
//            int x, int y, int w, int h) {
//                
//            Color shadow        = UIManager.getColor("controlShadow");
//            if (shadow == null) {
//                shadow = Color.GRAY;
//            }
//            Color lightShadow   = new Color(shadow.getRed(), 
//                                            shadow.getGreen(), 
//                                            shadow.getBlue(), 
//                                            170);
//            Color lighterShadow = new Color(shadow.getRed(),
//                                            shadow.getGreen(),
//                                            shadow.getBlue(),
//                                            70);
//            g.translate(x, y);
//            
//            g.setColor(shadow);
//            g.fillRect(0, 0, w - 3, 1);
//            g.fillRect(0, 0, 1, h - 3);
//            g.fillRect(w - 3, 1, 1, h - 3);
//            g.fillRect(1, h - 3, w - 3, 1);
//            // Shadow line 1
//            g.setColor(lightShadow);
//            g.fillRect(w - 3, 0, 1, 1);
//            g.fillRect(0, h - 3, 1, 1);
//            g.fillRect(w - 2, 1, 1, h - 3);
//            g.fillRect(1, h - 2, w - 3, 1);
//            // Shadow line2
//            g.setColor(lighterShadow);
//            g.fillRect(w - 2, 0, 1, 1);
//            g.fillRect(0, h - 2, 1, 1);
//            g.fillRect(w-2, h-2, 1, 1);
//            g.fillRect(w - 1, 1, 1, h - 2);
//            g.fillRect(1, h - 1, w - 2, 1);
//            g.translate(-x, -y);
//        }
//    }
//    // A panel with a horizontal gradient background.
//    private static class GradientPanel extends JPanel {
//        
//        private GradientPanel(LayoutManager lm, Color background) {
//            super(lm);
//            setBackground(background);
//        }
//
//        public void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            if (!isOpaque()) {
//                return;
//            }
//            Color control = UIManager.getColor("control");
//            int width  = getWidth();
//            int height = getHeight();
//
//            Graphics2D g2 = (Graphics2D) g;
//            Paint storedPaint = g2.getPaint();
//            g2.setPaint(
//                new GradientPaint(0, 0, getBackground(), width, 0, control));
//            g2.fillRect(0, 0, width, height);
//            g2.setPaint(storedPaint);
//        }
//    }
}
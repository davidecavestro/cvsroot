/*
 * JRingChart.java
 *
 * Created on 28 febbraio 2005, 21.32
 */

package com.ost.timekeeper.graph.swing;

import java.awt.*;
import JSci.awt.*;
import com.ost.timekeeper.graph.awt.*;
import com.ost.timekeeper.graph.swing.tooltip.SerieNodeToolTipSupplier;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.*;
import javax.swing.JLabel;

/**
 * A pie chart Swing component.
 *
 * @author  davide
 */
public class JRingChart  extends JTreeGraph2D {
	
	private int _depth=4;
	/**
	 * Total value of pie.
	 */
	private double pieTotal;
	/**
	 * Slice colors.
	 */
	protected Color sliceColor[]={Color.blue,Color.green,Color.red,Color.yellow,Color.cyan,Color.lightGray,Color.magenta,Color.orange,Color.pink};
	
	private JLabel _xAxisLabel;
	
	/**
	 * Costruttore.
	 *
	 * @param tgm il modello.
	 * @param depth il numero di livelli visibili.
	 */
	public JRingChart (TreeGraph2DModel tgm, int depth) {
		this (tgm, depth, null);
	}
	
	/**
	 * Costruttore.
	 *
	 * @param xAxisLabel l'etichetta chefa da descrittore dell'asse X.
	 * @param tgm il modello.
	 * @param depth il numero di livelli visibili.
	 */
	public JRingChart (final TreeGraph2DModel tgm, int depth, final JLabel xAxisLabel) {
		super (tgm);
		setDepth (depth);
		this._xAxisLabel = xAxisLabel;
        //Register with the shared instance of TooltipManager
        javax.swing.ToolTipManager.sharedInstance().registerComponent(this);
		
		dataChanged (new GraphDataEvent (model));
		addMouseListener (new JRingChart.MouseAdapter ());
	}
	
	public void setDepth (int depth){
		if(depth<1){
			throw new Error ("Invalid depth value: "+depth);
		}
		this._depth=depth;
        redraw();
	}
	
	public int getDepth (){
		return this._depth;
	}
	
	/**
	 * Implementation of GraphDataListener.
	 * Application code will not use this method explicitly, it is used internally.
	 */
	public void dataChanged (GraphDataEvent e) {
//		model.firstSeries ();
		SerieNode root = model.getRoot ();
		final int len=root.childrenLength ();
		pieTotal = root.getTotalValue ();
//		pieTotal=0.0f;
//		for(int i=0;i<len;i++){
//			pieTotal+=model.getValue (i);
//		}
		if(len>sliceColor.length) {
			Color tmp[]=sliceColor;
			sliceColor=new Color[len];
			System.arraycopy (tmp,0,sliceColor,0,tmp.length);
			for(int i=tmp.length;i<sliceColor.length;i++){
				sliceColor[i]=sliceColor[i-tmp.length];
			}
		}
		rescale ();
	}
	/**
	 * Sets the slice color of the nth slice.
	 * @param n the index of the slice.
	 * @param c the slice color.
	 */
	public final void setColor (int n,Color c) {
		sliceColor[n]=c;
	}
	/**
	 * Gets the slice color of the nth slice.
	 * @param n the index of the slice.
	 */
	public final Color getColor (int n) {
		return sliceColor[n];
	}
	/**
	 * Reshapes the JPieChart to the specified bounding box.
	 */
	public final void setBounds (int x,int y,int width,int height) {
		super.setBounds (x,y,width,height);
		rescale ();
	}
	/**
	 * Rescales the JPieChart.
	 */
	protected final void rescale () {
		// Swing optimised
		origin.x=getWidth ()/2;
		origin.y=getHeight ()/2;
		redraw ();
	}
	
//	private final java.util.Map _shapesToNodes = new java.util.HashMap ();
	private final java.util.Collection _nodeGraphData = new java.util.ArrayList ();
	
	double _xLevel=(origin.x/*-axisPad*/)/_depth;
	double _yLevel=(origin.y/*-axisPad*/)/_depth;
	double _xWidth=2*_xLevel;
	double _yWidth=2*_yLevel;
	
	final Font legendaFont = new Font("Default",Font.BOLD,12);
	
	/**
	 * Paint the graph.
	 */
	protected void offscreenPaint (Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
        Stroke s = new BasicStroke(2f);
//        g2.setColor(Color.WHITE);
//        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(s);
		
		_nodeGraphData.clear ();
		
		_xLevel=(origin.x/*-axisPad*/)/_depth;
		_yLevel=(origin.y/*-axisPad*/)/_depth;
		_xWidth=2*_xLevel;
		_yWidth=2*_yLevel;
	
 BufferedImage bi = new BufferedImage(5, 5,
                                BufferedImage.TYPE_INT_RGB);
         Graphics2D big = bi.createGraphics();
         big.setColor(Color.lightGray);
         big.fillRect(0, 0, 5, 5);
         big.setColor(Color.white);
         big.fillOval(0, 0, 5, 5);
         Rectangle r = new Rectangle(0,0,5,5);
		 
		computeShapes (new JRingChart.NodeGraphData (model.getRoot (), 
		new TexturePaint(bi, r)),
		null,
		1, 0.0d, 360.0d);
		for (final java.util.Iterator it=_nodeGraphData.iterator ();it.hasNext ();){
			JRingChart.NodeGraphData data = (JRingChart.NodeGraphData )it.next ();
			
//		String message = data.getNode ().getName ();
//				
//		FontMetrics metrics = g2.getFontMetrics();
//		int width = metrics.stringWidth( message );
//		int height = metrics.getHeight();
//		g2.setColor (Color.RED);
//        g2.setStroke(new BasicStroke (1f));
//		g2.drawString( message, (float)data.getArea ().getBounds ().getX (), (float)data.getArea ().getBounds ().getY () );		
//		java.awt.geom.Area dataArea = data.getArea ();
//		g2.drawRect (dataArea.getBounds ().x, dataArea.getBounds ().y, dataArea.getBounds ().width, dataArea.getBounds ().height );		
			
			
			g2.setColor (Color.BLACK);
			g2.draw (data.getArea ());
			g2.setPaint (data.getPaint ());
			g2.fill (data.getArea ());
		}
		
		/*
		 * testo radice
		 */
		SerieNode root = model.getRoot ();
		StringBuffer sb= new StringBuffer ();
		sb.append ("Root: ")
		.append (SerieNodeToolTipSupplier.getToolTip (root));
		final String message = sb.toString ();
		
		if (this._xAxisLabel!=null){
			/*
			 * usa eticheta asse X
			 */
			this._xAxisLabel.setText (message);
		} else {
			/*
			 * scrive direttamente sul fondo del componente
			 */
			g2.setFont (legendaFont);
			final FontMetrics metrics = g2.getFontMetrics();
			int width = metrics.stringWidth( message );
			int height = metrics.getHeight();
			g2.setColor (Color.BLACK);
			g2.drawString( message, 0, this.getHeight () );
		}
	}
	
	/*
	 * Arco su cui lavorare.
	 **/
	private java.awt.geom.Arc2D.Double _theArc = new java.awt.geom.Arc2D.Double (java.awt.geom.Arc2D.PIE);
	
	protected void computeShapes (NodeGraphData nodeData, java.awt.geom.Area[] subtractors, int level, double startAngle, double extensionAngle) {
		SerieNode node = nodeData.getNode ();
		java.awt.Paint paint = nodeData.getPaint ();
		
		final double x=_xLevel*(_depth-level);
		final double y=_yLevel*(_depth-level);
		final double width=_xWidth*level;
		final double height=_yWidth*level;
		
		double arcAngle,angle=0;
		double childStart = startAngle;
		int childLevel = level+1;
		
		_theArc.setAngleStart (startAngle);
		_theArc.setAngleExtent (extensionAngle);
		_theArc.setFrame (x,y,width,height);
		java.awt.geom.Area currentArea = new java.awt.geom.Area (_theArc);
		
		final int subtractorsLength;
		if (subtractors!=null){
			subtractorsLength = subtractors.length;
			for (int i=0;i<subtractors.length;i++){
				currentArea.subtract (subtractors[i]);
			}
		} else {
			subtractorsLength = 0;
		}
		final int childSubtractorsLength = subtractorsLength+1;
		java.awt.geom.Area[] childSubtractors = new java.awt.geom.Area[childSubtractorsLength];
		if (subtractors!=null){
			System.arraycopy (subtractors, 0, childSubtractors, 0, subtractorsLength);
		}
		
		nodeData.setArea (currentArea);
		
		_nodeGraphData.add (nodeData);
		
		if (level>=this._depth){
			/*
			 * inibisce tracciamento livelli fuori dall'area visibile.
			 */
			return;
		}
		for (int ix=0;ix<node.childrenLength ();ix++){
			SerieNode child = node.childAt (ix);
			double childExtension = child.getTotalValue ()*extensionAngle/node.getTotalValue ();
			childSubtractors[childSubtractorsLength-1] = currentArea;
			Color color = null;
			if (2==childLevel){
				color = sliceColor[ix];
			} else {
				Color oldCOlor = (Color)paint;
				color = new Color (/*Math.abs (*/oldCOlor.getRed ()/*-colorSeed)*/, /*Math.abs (*/oldCOlor.getGreen ()/*-colorSeed)*/, /*Math.abs (*/oldCOlor.getBlue ()/*-colorSeed)*/, (int)(oldCOlor.getAlpha ()/1.3));
			}
			
//			final int colorSeed = 20*level*ix;
			computeShapes (
				new JRingChart.NodeGraphData (child, color), 
				childSubtractors, 
				childLevel, 
				childStart, 
				childExtension);
			childStart += childExtension;
		}
	}
	
	private static class NodeGraphData {
		private SerieNode _node;
		private java.awt.Paint _paint;
		private java.awt.geom.Area _area;
		
		public NodeGraphData (SerieNode node, java.awt.Paint paint){
			this._node=node;
			this._paint=paint;
		}
		
		public NodeGraphData (SerieNode node, java.awt.Paint paint, java.awt.geom.Area area){
			this._node=node;
			this._paint=paint;
			this._area=area;
		}
		
		public SerieNode getNode (){ return this._node;}
		public java.awt.Paint getPaint (){ return this._paint;}
		public java.awt.geom.Area getArea (){ return this._area;}
		public void setArea (java.awt.geom.Area area){ this._area = area;}
		
		public String toString (){
			final StringBuffer sb = new StringBuffer ();
			sb.append ("node: ").append (this._node);
			sb.append (" paint: ").append (this._paint);
			sb.append (" area: ").append (this._area);
			return sb.toString ();
		}
	}
	
	/*
	 * Ritorna il tooltip per l'area interesata.
	 * @return il tooltip per l'area interesata.
	 */
    public String getToolTipText(java.awt.event.MouseEvent e) {
        for (final java.util.Iterator it = _nodeGraphData.iterator ();it.hasNext ();) {
			JRingChart.NodeGraphData data = (JRingChart.NodeGraphData )it.next ();			
            if (data.getArea ().contains(e.getPoint())) {
				return SerieNodeToolTipSupplier.getToolTip (data.getNode ());
            }
        }
        return super.getToolTipText(e);
    }
	
	private final class MouseAdapter extends java.awt.event.MouseAdapter {
		public void mouseClicked(MouseEvent me){
			if (me.getClickCount ()>0){
				boolean hit = false;
				JRingChart.NodeGraphData data=null;
//				System.out.println ("click detected");
				for (final java.util.Iterator it = _nodeGraphData.iterator ();it.hasNext ();) {
					data = (JRingChart.NodeGraphData )it.next ();			
					if (data.getArea ().contains(me.getPoint())) {
//						System.out.println ("changing root node: "+data.getNode ());
						hit=true;
						break;
					}
				}
				if (hit){
					JRingChart.this.getModel ().setRoot (data.getNode ());				
				}
			}
		}
	}
	
}


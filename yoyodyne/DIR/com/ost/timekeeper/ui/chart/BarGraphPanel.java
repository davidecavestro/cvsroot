/*
 * BarGraphPanel.java
 *
 * Created on 19 marzo 2005, 13.26
 */

package com.ost.timekeeper.ui.chart;

import JSci.awt.CategoryGraph2DModel;
import JSci.awt.DefaultCategoryGraph2DModel;
import JSci.awt.DefaultGraph2DModel;
import JSci.awt.Graph2D;
import JSci.awt.Graph2DModel;
import JSci.awt.GraphLayout;
import JSci.swing.JBarGraph;
import JSci.swing.JGraphLayout;
import JSci.swing.JLineGraph;
import com.ost.timekeeper.Application;
import com.ost.timekeeper.graph.awt.TreeGraph2DModel;
import com.ost.timekeeper.help.HelpResource;
import com.ost.timekeeper.model.ProgressItem;
import com.ost.timekeeper.report.filter.TargetedFilterContainer;
import com.ost.timekeeper.report.flavors.CumulateProgresses;
import com.ost.timekeeper.ui.ProgressItemTreeSelector;
import com.ost.timekeeper.ui.TopBorderPane;
import com.ost.timekeeper.util.DurationFormat;
import com.ost.timekeeper.util.NestedRuntimeException;
import com.ost.timekeeper.util.ResourceClass;
import com.ost.timekeeper.util.ResourceSupplier;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Pannello contenente i controli ed il grafico a barre.
 *
 * @author  davide
 */
public class BarGraphPanel extends JPanel{
	
	private CategoryGraph2DModel _dataModel;
	private ProgressItem _root;
	private JBarGraph _graph;
	
	private String _graphTitle;
	private JLabel _graphTitleLabel/* = new JLabel ()*/;
	
	private int _periodLength = 1;
	private int _periodCount = 7;
	
	private JDateChooser _dateEditor;
	private Date _date = new Date ();
	
	/**
	 * Costruttore.
	 * @param chartTitle il titolo del grafico.
	 */
	public BarGraphPanel (final String chartTitle) {
		super ();
		this._graphTitle = chartTitle;
		initComponents ();
	}
	
	private void initComponents (){
		this.setLayout (new BorderLayout ());
		
		final JPanel chartPanel = new JPanel (new GraphLayout ());
		
		final Font titleFont = new Font ("Default",Font.BOLD,14);
		
		this._dataModel = createData (this._root, this._date, this._periodLength, this._periodCount);
		
		this._graph = new JBarGraph (this._dataModel);
		
		final Color localSerieColor = Color.GREEN;
		final Color subtreeSerieColor = Color.YELLOW;
		final Color totalSerieColor = Color.BLUE;
		
		this._graph.setColor (0, localSerieColor);
		this._graph.setColor (1, subtreeSerieColor);
		this._graph.setColor (2, totalSerieColor);
		
		this._graph.setYNumberFormat (new DurationFormat ());
		
		//		this._graph.setGridLines (true);
		//		this._graph.setMarker (new Graph2D.DataMarker.Circle (5));
		final JPanel lineGraphPanel=new JPanel (new JGraphLayout ());
		this._graphTitleLabel=new JLabel (this._graphTitle, JLabel.CENTER);
		this._graphTitleLabel.setFont (titleFont);
		
		chartPanel.add (this._graphTitleLabel,"Title");
		chartPanel.add (_graph,"Graph");
		chartPanel.add (new JLabel (
		ResourceSupplier.getString (ResourceClass.UI, "controls", "duration"),
		JLabel.RIGHT),
		"Y-axis");
		chartPanel.add (new JLabel (
		ResourceSupplier.getString (ResourceClass.UI, "controls", "period"),
		JLabel.CENTER)
		,"X-axis");
		
		
		
		
		
		final JPanel chartContainer = new JPanel (new BorderLayout ());
		chartContainer.add (chartPanel, BorderLayout.NORTH);
		
		final JPanel legendaPanel = new JPanel (new FlowLayout ());
		legendaPanel.setBorder (new TitledBorder ( new BevelBorder (BevelBorder.LOWERED), ResourceSupplier.getString (ResourceClass.UI, "controls", "legenda")));
		legendaPanel.add (new Legenda (ResourceSupplier.getString (ResourceClass.UI, "controls", "local"), localSerieColor));
		legendaPanel.add (new Legenda (ResourceSupplier.getString (ResourceClass.UI, "controls", "subtree"), subtreeSerieColor));
		legendaPanel.add (new Legenda (ResourceSupplier.getString (ResourceClass.UI, "controls", "total"), totalSerieColor));
		
		chartContainer.add (legendaPanel, BorderLayout.CENTER);
		
		add (new JScrollPane (chartContainer), BorderLayout.CENTER);
		
		
		final JPanel controlsContainer = new JPanel (new GridBagLayout ());
		
		final GridBagConstraints c = new GridBagConstraints ();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets (3, 10, 3, 10);
		
		c.weightx = 0.5;
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		controlsContainer.add (new TopBorderPane (ResourceSupplier.getString (ResourceClass.UI, "controls", "control.panel")), c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		controlsContainer.add (createControlPanel (), c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		controlsContainer.add (createButtonPanel (), c);
		
		/* etichetta vuota per riemire spazio */
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		controlsContainer.add (new JLabel (), c);
		
		add (controlsContainer, BorderLayout.SOUTH);
	}
	
	private static DefaultCategoryGraph2DModel createData (final ProgressItem root, final Date date, final int periodLength, final int periodCount) {
		
		final Element rootElement;
		if (root!=null){
			final Document data = new CumulateProgresses (root, new TargetedFilterContainer []{},
			date,
			periodLength,
			periodCount
			).extract ();
			rootElement = data.getRootElement ();
		} else {
			rootElement = new Element ("void");
		}
		final List periods = rootElement.getChildren (CumulateProgresses.PERIOD_ELEMENT);
		final int periodsCount = periods.size ();
		
		final String labels[] = new String [periodsCount];
		float localValues[] = new float [periodsCount];
		float subtreeValues[] = new float [periodsCount];
		float totalValues[] = new float [periodsCount];
		
		try {
			int ix = 0;
			for (final Iterator it = periods.iterator ();it.hasNext ();){
				final Element period = (Element)it.next ();
				final Element localValueElement = period.getChild (CumulateProgresses.MILLISECLOCALDURATION_ELEMENT);
				final float localValue = Float.parseFloat (localValueElement.getValue ());
				localValues[ix] = localValue;
				final Element subtreeValueElement = period.getChild (CumulateProgresses.MILLISECSUBTREEDURATION_ELEMENT);
				final float subtreeValue = Float.parseFloat (subtreeValueElement.getValue ());
				subtreeValues[ix] = subtreeValue;
				totalValues[ix] = localValue+subtreeValue;
				
				final Element periodNameElement = period.getChild (CumulateProgresses.PERIODNAME_ELEMENT);
				labels[ix] = periodNameElement.getValue ();
				ix++;
			}
		} catch (NumberFormatException nfe){
			throw new NestedRuntimeException (nfe);
		}
		//		final File dataFile = File.createTempFile ("data", ".xml");
		//
		//		final XMLOutputter xo = new XMLOutputter ();
		//		xo.output (data, new FileOutputStream (dataFile));
		
		
		DefaultCategoryGraph2DModel model=new DefaultCategoryGraph2DModel ();
		model.setCategories (labels);
		//		model.setXAxis (0.0f,5.0f,values1.length);
		model.addSeries (localValues);
		model.addSeries (subtreeValues);
		model.addSeries (totalValues);
		//		model.setSeriesVisible (1,true);
		//		model.setSeriesVisible (2,true);
		//		model.setSeriesVisible (3,true);
		return model;
	}
	
	//	private static SerieNode createSerieNode (final ProgressItem progressItem) {
	//		if (null==progressItem){
	//			return new DefaultSerieNode (null + " - " + null, 0, new SerieNode [0]);
	//		}
	//		/*
	//		 * calcola durata complessiva sottoalbero.
	//		 */
	//		double totalDuration = 0;
	//		for (final Iterator it = progressItem.getProgresses ().iterator ();it.hasNext ();){
	//			final Progress period = (Progress)it.next ();
	//			totalDuration += period.getDuration ().getTime ();
	//		}
	//
	//		/*
	//		 * Popola array serie nodi figli
	//		 */
	//		SerieNode[] childSeries = new SerieNode[progressItem.getChildren ().size ()];
	//		int i=0;
	//		for (final Iterator it = progressItem.getChildren ().iterator ();it.hasNext ();){
	//			childSeries[i] = createSerieNode ((ProgressItem)it.next ());
	//			i++;
	//		}
	//
	//		return new DefaultSerieNode (progressItem.getCode () + " - " + progressItem.getName (), totalDuration, childSeries);
	//	}
	
	/**
	 * Reimposta la radice del grafico, e lo aggiorna.
	 */
	public final void reloadChart (final ProgressItem root) {
		this._root = root;
		this._graph.setModel (this.createData (root, this._date, this._periodLength, this._periodCount));
	}
	
	private JPanel createButtonPanel (){
		final JPanel thePanel = new JPanel ();
		
		final JButton rootSelectionButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.root"));
		rootSelectionButton.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				final ProgressItemTreeSelector.UserChoice choice = ProgressItemTreeSelector.supplyProgressItem (
				ResourceSupplier.getString (ResourceClass.UI, "controls", "progressitem.choice"),
				ResourceSupplier.getString (ResourceClass.UI, "controls", "choose.linegraph.root"), true, HelpResource.LINECHART_ROOTSELECTION_DIALOG);
				if (choice.isConfirmed ()){
					BarGraphPanel.this.reloadChart (choice.getChoice ());
				}
			}
		});
		thePanel.add (rootSelectionButton);
		
		final JButton updateButton = new JButton (ResourceSupplier.getString (ResourceClass.UI, "controls", "update"));
		updateButton.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				BarGraphPanel.this._graph.setModel (BarGraphPanel.this.createData (BarGraphPanel.this._root, BarGraphPanel.this._date, BarGraphPanel.this._periodLength, BarGraphPanel.this._periodCount));
			}
		});
		thePanel.add (updateButton);
		
		return thePanel;
	}
	
	private final String dateFormat = Application.getOptions ().getDateFormat ();
	
	private JPanel createControlPanel (){
		final JPanel thePanel = new JPanel (new GridBagLayout ());
		/**
		 * Componente editazione .
		 */
		
	
		/**
		 * Componente editazione DATA.
		 */
		_dateEditor = new JDateChooser (dateFormat, false);
		final JLabel dateLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "date"));
		dateLabel.setLabelFor (_dateEditor);
		
		final PropertyChangeListener dateChangeListener = new PropertyChangeListener (){
			public void propertyChange (PropertyChangeEvent evt){
				final Date date = BarGraphPanel.this._dateEditor.getDate ();
				if (date!=null){
					BarGraphPanel.this._date = date;
				}
			}
		};
		_dateEditor.addPropertyChangeListener ("date", dateChangeListener);
		
		final javax.swing.JSpinner periodLengthEditor = new javax.swing.JSpinner ();
		
		final JLabel periodLengthEditorLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "period.length.days"));
		periodLengthEditorLabel.setLabelFor (periodLengthEditor);
		
		final JComponent lengthEditorComponent = periodLengthEditor.getEditor ();
		if (lengthEditorComponent instanceof JSpinner.DefaultEditor) {
			/* impoxsta il numero di colonne dell'editor */
			((JSpinner.DefaultEditor)lengthEditorComponent).getTextField ().setColumns (2);
		}
		periodLengthEditor.setModel (
		new SpinnerNumberModel (this._periodLength, //initial value
		1, //min
		31, //max
		1)); //step
		
		periodLengthEditor.addChangeListener (new ChangeListener (){
			public void stateChanged (ChangeEvent e){
				final Object value = periodLengthEditor.getValue ();
				if (value!=null){
					BarGraphPanel.this._periodLength = ((Integer)value).intValue ();
				}
			}});
			
		/**
		 * Componente editazione numero livelli visibili.
		 */
		final javax.swing.JSpinner periodCountEditor = new javax.swing.JSpinner ();
		
		final JLabel periodCountEditorLabel = new JLabel (ResourceSupplier.getString (ResourceClass.UI, "controls", "period.count"));
		periodCountEditorLabel.setLabelFor (periodCountEditor);
		
		final JComponent countEditorCOmponent = periodCountEditor.getEditor ();
		if (countEditorCOmponent instanceof JSpinner.DefaultEditor) {
			/* impoxsta il numero di colonne dell'editor */
			((JSpinner.DefaultEditor)countEditorCOmponent).getTextField ().setColumns (2);
		}
		periodCountEditor.setModel (
		new SpinnerNumberModel (this._periodCount, //initial value
		1, //min
		100, //max
		1)); //step
		
		periodCountEditor.addChangeListener (new ChangeListener (){
			public void stateChanged (ChangeEvent e){
				final Object value = periodCountEditor.getValue ();
				if (value!=null){
					BarGraphPanel.this._periodCount = ((Integer)value).intValue ();
				}
			}});
			
			final GridBagConstraints c = new GridBagConstraints ();
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.insets = new Insets (3, 10, 3, 10);
			
		/*
		 * Inserimento componenti editazione.
		 */
			c.gridx = 0;
			c.gridy = 0;
			thePanel.add (dateLabel, c);
			c.gridx = 1;
			c.gridy = 0;
			thePanel.add (_dateEditor, c);
			
			c.gridx = 2;
			c.gridy = 0;
			thePanel.add (periodLengthEditorLabel, c);
			c.gridx = 3;
			c.gridy = 0;
			thePanel.add (periodLengthEditor, c);
			
			c.gridx = 4;
			c.gridy = 0;
			thePanel.add (periodCountEditorLabel, c);
			c.gridx = 5;
			c.gridy = 0;
			thePanel.add (periodCountEditor, c);
			
			
			return thePanel;
	}
	
	private final static class Legenda extends JPanel {
		public Legenda (final String text, final Color color){
			super (new FlowLayout ());
			final JLabel label = new JLabel (text);
			final JLabel legenda = new JLabel ("XXX");
			legenda.setMinimumSize (new Dimension (20, 15));
			legenda.setBackground (color);
			legenda.setOpaque (true);
			legenda.setForeground (color);
			//			legenda.setBorder (new CompoundBorder (new BevelBorder (BevelBorder.LOWERED), new BevelBorder (BevelBorder.RAISED)));
			add (legenda);
			add (label);
		}
		
	}
}

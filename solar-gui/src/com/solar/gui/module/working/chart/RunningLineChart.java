package com.solar.gui.module.working.chart;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

public class RunningLineChart extends ApplicationFrame {
	public RunningLineChart(String paramString) {
		super(paramString);
		JPanel localJPanel = createDemoPanel();
		localJPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(localJPanel);
	}

	private static CategoryDataset createDataset() {
		DefaultCategoryDataset localDefaultCategoryDataset = new DefaultCategoryDataset();
		localDefaultCategoryDataset.addValue(21.0D, "Series 1", "Category 1");
		localDefaultCategoryDataset.addValue(50.0D, "Series 1", "Category 2");
		localDefaultCategoryDataset.addValue(152.0D, "Series 1", "Category 3");
		localDefaultCategoryDataset.addValue(184.0D, "Series 1", "Category 4");
		localDefaultCategoryDataset.addValue(299.0D, "Series 1", "Category 5");
		localDefaultCategoryDataset.addValue(275.0D, "Series 2", "Category 1");
		localDefaultCategoryDataset.addValue(121.0D, "Series 2", "Category 2");
		localDefaultCategoryDataset.addValue(98.0D, "Series 2", "Category 3");
		localDefaultCategoryDataset.addValue(103.0D, "Series 2", "Category 4");
		localDefaultCategoryDataset.addValue(210.0D, "Series 2", "Category 5");
		localDefaultCategoryDataset.addValue(198.0D, "Series 3", "Category 1");
		localDefaultCategoryDataset.addValue(165.0D, "Series 3", "Category 2");
		localDefaultCategoryDataset.addValue(55.0D, "Series 3", "Category 3");
		localDefaultCategoryDataset.addValue(34.0D, "Series 3", "Category 4");
		localDefaultCategoryDataset.addValue(77.0D, "Series 3", "Category 5");
		return localDefaultCategoryDataset;
	}

	private static JFreeChart createChart(CategoryDataset paramCategoryDataset) {
		JFreeChart localJFreeChart = ChartFactory.createLineChart("Line Chart Demo 7", "Category", "Count",
				paramCategoryDataset, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot localCategoryPlot = (CategoryPlot) localJFreeChart.getPlot();
		NumberAxis localNumberAxis = (NumberAxis) localCategoryPlot.getRangeAxis();
		localNumberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		LineAndShapeRenderer localLineAndShapeRenderer = (LineAndShapeRenderer) localCategoryPlot.getRenderer();
		localLineAndShapeRenderer.setSeriesShapesVisible(0, true);
		localLineAndShapeRenderer.setSeriesShapesVisible(1, false);
		localLineAndShapeRenderer.setSeriesShapesVisible(2, true);
		localLineAndShapeRenderer.setSeriesLinesVisible(2, false);
		localLineAndShapeRenderer.setSeriesShape(2, ShapeUtilities.createDiamond(4.0F));
		localLineAndShapeRenderer.setDrawOutlines(true);
		localLineAndShapeRenderer.setUseFillPaint(true);
		localLineAndShapeRenderer.setBaseFillPaint(Color.white);
		return localJFreeChart;
	}

	public static JPanel createDemoPanel() {
		JFreeChart localJFreeChart = createChart(createDataset());
		return new ChartPanel(localJFreeChart);
	}

	public static void main(String[] paramArrayOfString) {
		RunningLineChart localLineChartDemo7 = new RunningLineChart("JFreeChart: LineChartDemo7.java");
		localLineChartDemo7.pack();
		RefineryUtilities.centerFrameOnScreen(localLineChartDemo7);
		localLineChartDemo7.setVisible(true);
	}
}

package com.solar.gui.module.working.chart;

import java.util.Date;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class RealTimeChart extends ChartPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private static TimeSeries timeSeries;

	public RealTimeChart(JFreeChart chart) {
		super(chart);
	}

	public RealTimeChart(String chartContent, String title, String yaxisName) {
		super(createChart(chartContent, title, yaxisName));
	}

	private static JFreeChart createChart(String chartContent, String title, String yaxisName) {
		// 创建时序图对象
		timeSeries = new TimeSeries(chartContent);
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeSeries);

		for (int i = 0; i < 30; i++) {
			long currentTimeMillis = System.currentTimeMillis();
			currentTimeMillis = currentTimeMillis - (30 - i) * 1000;
			timeSeries.add(new Millisecond(new Date(currentTimeMillis)), (long) (Math.random() * 20 + 80));
		}

		JFreeChart jfreechart = MFat.createTimeSeriesChart(title, "时间(分)", yaxisName, timeseriescollection, true, true,
				false);
		XYPlot xyplot = jfreechart.getXYPlot();
		// 纵坐标设定
		ValueAxis valueaxis = xyplot.getDomainAxis();
		// 自动设置数据轴数据范围
		valueaxis.setAutoRange(true);
		// 数据轴固定数据范围 30s
		valueaxis.setFixedAutoRange(30000D);

		valueaxis = xyplot.getRangeAxis();
		// valueaxis.setRange(0.0D,200D);
		return jfreechart;
	}

	private long randomNum() {
		return (long) (Math.random() * 20 + 80);
	}

	@Override
	public void run() {
		while (true) {
			try {
				timeSeries.add(new Millisecond(), randomNum());
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

}
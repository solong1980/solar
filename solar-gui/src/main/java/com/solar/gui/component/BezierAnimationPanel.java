package com.solar.gui.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BezierAnimationPanel extends JPanel implements Runnable {

	Color backgroundColor = new Color(0, 0, 153);
	Color outerColor = new Color(255, 255, 255);
	Color gradientColorA = new Color(255, 0, 101);
	Color gradientColorB = new Color(255, 255, 0);

	GradientPaint gradient = null;

	BufferedImage img;

	Rectangle bounds = null;

	Thread anim;

	private final Object lock = new Object();

	/**
	 * BezierAnimationPanel Constructor
	 */
	public BezierAnimationPanel() {
		addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				if (isShowing()) {
					start();
				} else {
					stop();
				}
			}
		});
	}

	public boolean isOpaque() {
		return true;
	}

	public Color getGradientColorA() {
		return gradientColorA;
	}

	public void setGradientColorA(Color c) {
		if (c != null) {
			gradientColorA = c;
		}
	}

	public Color getGradientColorB() {
		return gradientColorB;
	}

	public void setGradientColorB(Color c) {
		if (c != null) {
			gradientColorB = c;
		}
	}

	public Color getOuterColor() {
		return outerColor;
	}

	public void setOuterColor(Color c) {
		if (c != null) {
			outerColor = c;
		}
	}

	public void start() {
		Dimension size = getSize();
		anim = new Thread(this);
		anim.setPriority(Thread.MIN_PRIORITY);
		anim.start();
	}

	public synchronized void stop() {
		anim = null;
		notify();
	}

	public void animate(float[] pts, float[] deltas, int index, int limit) {
		float newpt = pts[index] + deltas[index];
		if (newpt <= 0) {
			newpt = -newpt;
			deltas[index] = (float) (Math.random() * 3.0 + 2.0);
		} else if (newpt >= (float) limit) {
			newpt = 2.0f * limit - newpt;
			deltas[index] = -(float) (Math.random() * 3.0 + 2.0);
		}
		pts[index] = newpt;
	}

	public void run() {
		Thread me = Thread.currentThread();
		while (getSize().width <= 0) {
			try {
				anim.sleep(500);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public void paint(Graphics g) {
		synchronized (lock) {
			Graphics2D g2d = (Graphics2D) g;
			BasicStroke solid = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 3.0f);
			g2d.setStroke(solid);

			int top_x = 10;
			int top_y = 10;
			int x_width = 120;
			int y_height = 400;
			int grap = 100;

			g2d.drawRect(top_x, top_y, x_width, y_height);

			Font font1 = new Font("宋体", Font.BOLD, 14);
			g2d.setFont(font1);
			g2d.drawString("太阳能电池板", top_x + 5, top_y + 15);
			g2d.drawString("电池板电压数据", top_x + 5, top_y + 40);
			g2d.drawString("充电电流数据", top_x + 5, top_y + 70);
			g2d.drawString("总发电量数据", top_x + 5, top_y + 100);

			g2d.drawLine(top_x + x_width, 100, top_x + x_width + grap - 5, 100);

			Polygon p = new Polygon(
					new int[] { top_x + x_width + grap - 5, top_x + x_width + grap - 5, top_x + x_width + grap },
					new int[] { 95, 105, 100 }, 3);
			g2d.drawPolygon(p);

			g2d.drawRect(top_x + x_width + grap, top_y, x_width, 150);
			g2d.drawString("太阳能污水", top_x + x_width + grap + 5, top_y + 15);
			g2d.drawString("处理控制器", top_x + x_width + grap + 5, top_y + 40);

			g2d.drawLine(top_x + x_width + grap + 30, top_y + 150, top_x + x_width + grap + 30, top_y + 195);
			p = new Polygon(new int[] { top_x + x_width + grap + 30 - 5, top_x + x_width + grap + 30 + 5,
					top_x + x_width + grap + 30 }, new int[] { top_y + 195, top_y + 195, top_y + 200 }, 3);
			g2d.drawPolygon(p);

			g2d.drawLine(top_x + x_width + grap + x_width - 30, top_y + 150 + 5, top_x + x_width + grap + x_width - 30,
					top_y + 200);
			p = new Polygon(
					new int[] { top_x + x_width + grap + x_width - 30 - 5, top_x + x_width + grap + x_width - 30 + 5,
							top_x + x_width + grap + x_width - 30 },
					new int[] { top_y + 150 + 5, top_y + 150 + 5, top_y + 150 }, 3);
			g2d.drawPolygon(p);

			g2d.drawRect(top_x + x_width + grap, top_y + 200, x_width, 150);

			g2d.drawString("铅酸电池组", top_x + x_width + grap + 5, top_y + 210);
			g2d.drawString("电池组电压", top_x + x_width + grap + 5, top_y + 230);
			g2d.drawString("电池剩余容量", top_x + x_width + grap + 5, top_y + 250);

			// 运行数据

			g2d.drawLine(top_x + x_width + grap + x_width, top_y + 30, top_x + x_width + grap + x_width + grap,
					top_y + 30);
			g2d.drawLine(top_x + x_width + grap + x_width, top_y + 40, top_x + x_width + grap + x_width + grap,
					top_y + 40);
			g2d.drawLine(top_x + x_width + grap + x_width, top_y + 100, top_x + x_width + grap + x_width + grap,
					top_y + 100);
			g2d.drawLine(top_x + x_width + grap + x_width, top_y + 110, top_x + x_width + grap + x_width + grap,
					top_y + 110);

			g2d.drawLine(top_x + x_width + grap + x_width + grap, top_y + 30,
					top_x + x_width + grap + x_width + 2 * grap - 5, 75);
			g2d.drawLine(top_x + x_width + grap + x_width + grap, top_y + 40,
					top_x + x_width + grap + x_width + 2 * grap - 5, 145);
			g2d.drawLine(top_x + x_width + grap + x_width + grap, top_y + 100,
					top_x + x_width + grap + x_width + 2 * grap - 5, 215);
			g2d.drawLine(top_x + x_width + grap + x_width + grap, top_y + 110,
					top_x + x_width + grap + x_width + 2 * grap - 5, 285);

			p = new Polygon(new int[] { top_x + x_width + grap + x_width + 2 * grap - 5,
					top_x + x_width + grap + x_width + 2 * grap - 5, top_x + x_width + grap + x_width + 2 * grap },
					new int[] { 70, 80, 75 }, 3);
			g2d.drawPolygon(p);

			p = new Polygon(new int[] { top_x + x_width + grap + x_width + 2 * grap - 5,
					top_x + x_width + grap + x_width + 2 * grap - 5, top_x + x_width + grap + x_width + 2 * grap },
					new int[] { 210, 220, 215 }, 3);
			g2d.drawPolygon(p);

			p = new Polygon(new int[] { top_x + x_width + grap + x_width + 2 * grap - 5,
					top_x + x_width + grap + x_width + 2 * grap - 5, top_x + x_width + grap + x_width + 2 * grap },
					new int[] { 280, 290, 285 }, 3);
			g2d.drawPolygon(p);

			p = new Polygon(new int[] { top_x + x_width + grap + x_width + 2 * grap - 5,
					top_x + x_width + grap + x_width + 2 * grap - 5, top_x + x_width + grap + x_width + 2 * grap },
					new int[] { 140, 150, 145 }, 3);
			g2d.drawPolygon(p);

			g2d.drawRect(top_x + x_width + grap + x_width + 2 * grap, 50, 250, 50);
			g2d.drawRect(top_x + x_width + grap + x_width + 2 * grap, 120, 250, 50);
			g2d.drawRect(top_x + x_width + grap + x_width + 2 * grap, 190, 250, 50);
			g2d.drawRect(top_x + x_width + grap + x_width + 2 * grap, 260, 250, 50);

			g2d.drawString("风机 工作电流 是否正常工作", top_x + x_width + grap + x_width + 2 * grap + 15, top_y + 75);
			g2d.drawString("备用风机 工作电流 是否正常工作", top_x + x_width + grap + x_width + 2 * grap + 15, top_y + 215);
			g2d.drawString("水泵 工作电流 是否正常工作", top_x + x_width + grap + x_width + 2 * grap + 15, top_y + 285);
			g2d.drawString("备用水泵 工作电流 是否正常工作", top_x + x_width + grap + x_width + 2 * grap + 15, top_y + 145);

			if (img != null) {
				BufferedImage bi = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2dd = bi.createGraphics();
				paint(g2dd);
				try {
					ImageIO.write(bi, "PNG", new File("frame.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				g2d.clearRect(top_x + x_width + grap + x_width + grap - 1, 260 - 1, 253, 53);
			}
		}
	}
}

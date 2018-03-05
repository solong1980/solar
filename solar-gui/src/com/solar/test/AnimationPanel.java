package com.solar.test;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AnimationPanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	Color backgroundColor = new Color(0, 0, 153);
	Color outerColor = new Color(255, 255, 255);
	Color gradientColorA = new Color(255, 0, 101);
	Color gradientColorB = new Color(255, 255, 0);

	boolean bgChanged = false;

	GradientPaint gradient = null;

	public final int NUMPTS = 6;

	/**
	 * 定义6个点 偶数x,奇数y
	 */
	float animpts[] = new float[NUMPTS * 2];
	/**
	 * 定义6个点的移动增量 偶数x,奇数y
	 */
	float deltas[] = new float[NUMPTS * 2];

	float staticpts[] = { 50.0f, 0.0f, 150.0f, 0.0f, 200.0f, 75.0f, 150.0f, 150.0f, 50.0f, 150.0f, 0.0f, 75.0f, };

	float movepts[] = new float[staticpts.length];

	/** 定义一个图形对象每次将图像画如该对象中,每次渲染的时候使用改对象 */
	BufferedImage img;

	Rectangle bounds = null;

	// 更新界面线程
	Thread anim;

	// 双缓冲切换锁
	private final Object lock = new Object();

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLayout(new BorderLayout());
		frame.add(new AnimationPanel(), BorderLayout.CENTER);
		frame.setVisible(true);
	}

	/**
	 * AnimationPanel Constructor
	 */
	public AnimationPanel() {
		addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				if (isShowing()) {
					start();
				} else {
					stop();
				}
			}
		});
		setBackground(getBackgroundColor());
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

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color c) {
		if (c != null) {
			backgroundColor = c;
			setBackground(c);
			bgChanged = true;
		}
	}

	public void start() {
		Dimension size = getSize();
		for (int i = 0; i < animpts.length; i += 2) {
			animpts[i + 0] = (float) (Math.random() * size.width);
			animpts[i + 1] = (float) (Math.random() * size.height);
			deltas[i + 0] = (float) (Math.random() * 4.0 + 2.0);
			deltas[i + 1] = (float) (Math.random() * 4.0 + 2.0);
			if (animpts[i + 0] > size.width / 6.0f) {
				deltas[i + 0] = -deltas[i + 0];
			}
			if (animpts[i + 1] > size.height / 6.0f) {
				deltas[i + 1] = -deltas[i + 1];
			}
		}

		for (int i = 0; i < animpts.length; i++) {
			System.out.println("animpts[" + i + "]=" + animpts[i]);
		}
		for (int i = 0; i < deltas.length; i++) {
			System.out.println("deltas[" + i + "]=" + deltas[i]);
		}

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
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		}

		Graphics2D g2d = null;// 图像对象graphic绘制工具
		Graphics2D BufferG2D = null;// 缓冲
		Graphics2D ScreenG2D = null;// 界面绘制

		// 图像线条基本特征
		BasicStroke solid = new BasicStroke(9.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 9.0f);
		// GeneralPath只是将前后两点连接起来形成的路径,
		GeneralPath gp = new GeneralPath(GeneralPath.WIND_NON_ZERO);

		int rule = AlphaComposite.SRC_OVER;

		AlphaComposite opaque = AlphaComposite.SrcOver;
		AlphaComposite blend = AlphaComposite.getInstance(rule, 0.9f);
		AlphaComposite set = AlphaComposite.Src;

		Dimension oldSize = getSize();
		Shape clippath = null;
		while (anim == me) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}
			Dimension size = getSize();

			// 窗口大小变化，释放现有的gd,重新创建图像对象
			if (size.width != oldSize.width || size.height != oldSize.height) {
				img = null;
				clippath = null;
				if (BufferG2D != null) {
					BufferG2D.dispose();
					BufferG2D = null;
				}
				if (ScreenG2D != null) {
					ScreenG2D.dispose();
					ScreenG2D = null;
				}
			}
			oldSize = size;

			if (img == null) {
				// 创建一个图形对象
				img = (BufferedImage) createImage(size.width, size.height);
			}

			if (BufferG2D == null) {

				BufferG2D = img.createGraphics();
				BufferG2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
				BufferG2D.setClip(clippath);

			}
			g2d = BufferG2D;

			float[] ctrlpts;
			for (int i = 0; i < animpts.length; i += 2) {
				animate(animpts, deltas, i + 0, size.width);
				animate(animpts, deltas, i + 1, size.height);
			}
			ctrlpts = animpts;
			int len = ctrlpts.length;
			gp.reset();
			int dir = 0;

			// 从最后一个点
			float prevx = ctrlpts[len - 2];
			float prevy = ctrlpts[len - 1];
			// 到第一个点
			float curx = ctrlpts[0];
			float cury = ctrlpts[1];
			// --闭合

			// 中点
			float midx = (curx + prevx) / 2.0f;
			float midy = (cury + prevy) / 2.0f;

			// 路径从最后一个点和第一个点的中点开始
			gp.moveTo(midx, midy);

			for (int i = 2; i <= ctrlpts.length; i += 2) {
				// 取中点到第一个点的中点
				float x1 = (midx + curx) / 2.0f;
				float y1 = (midy + cury) / 2.0f;
				prevx = curx;
				prevy = cury;

				if (i < ctrlpts.length) {
					curx = ctrlpts[i + 0];
					cury = ctrlpts[i + 1];
				} else {
					curx = ctrlpts[0];
					cury = ctrlpts[1];
				}
				midx = (curx + prevx) / 2.0f;
				midy = (cury + prevy) / 2.0f;
				float x2 = (prevx + midx) / 2.0f;
				float y2 = (prevy + midy) / 2.0f;
				// --重复过程

				gp.curveTo(x1, y1, x2, y2, midx, midy);
			}
			gp.closePath();

			synchronized (lock) {
				g2d.setComposite(set);
				// g2d.setBackground(backgroundColor);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

				if (bgChanged || bounds == null) {
					bounds = new Rectangle(0, 0, getWidth(), getHeight());
					bgChanged = false;
				}

				// g2d.clearRect(bounds.x-5, bounds.y-5, bounds.x + bounds.width + 5, bounds.y +
				// bounds.height + 5);
				g2d.clearRect(0, 0, getWidth(), getHeight());

				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(outerColor);
				g2d.setComposite(opaque);
				g2d.setStroke(solid);
				g2d.draw(gp);
				g2d.setPaint(gradient);
				g2d.drawString("我市一个大傻瓜", 30, 30);

				if (!bgChanged) {
					bounds = gp.getBounds();
				} else {
					bounds = new Rectangle(0, 0, getWidth(), getHeight());
					bgChanged = false;
				}
				gradient = new GradientPaint(bounds.x, bounds.y, gradientColorA, bounds.x + bounds.width,
						bounds.y + bounds.height, gradientColorB, true);
				g2d.setComposite(blend);
				g2d.fill(gp);
			}
			if (g2d == BufferG2D) {
				repaint();
			}
			Thread.yield();
		}
		if (g2d != null) {
			g2d.dispose();
		}
	}

	public void paint(Graphics g) {
		synchronized (lock) {
			Graphics2D g2d = (Graphics2D) g;
			if (img != null) {
				int imgw = img.getWidth();
				int imgh = img.getHeight();
				g2d.setComposite(AlphaComposite.Src);
				g2d.drawImage(img, null, 0, 0);
			}
		}
	}
}

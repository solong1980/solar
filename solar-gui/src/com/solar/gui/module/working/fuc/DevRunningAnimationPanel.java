package com.solar.gui.module.working.fuc;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import com.alibaba.fastjson.TypeReference;
import com.solar.client.SoRet;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.entity.SoRunningData;
import com.solar.gui.component.formate.ButtonUI;

public class DevRunningAnimationPanel extends JPanel implements Observer, Runnable {
	private static final long serialVersionUID = 1L;

	Color backgroundColor = new Color(255, 255, 255);

	Color fontColor = new Color(0, 0, 0);
	Color fontBackColor = new Color(0, 200, 101);

	// 图片大小
	int IMG_WIDTH = 60;
	int IMG_HEIGHT = 60;

	BufferedImage img;

	Thread anim;

	private final Object lock = new Object();

	/**
	 * BezierAnimationPanel Constructor
	 */
	public DevRunningAnimationPanel() {
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

	public void start() {
		anim = new Thread(this);
		anim.setPriority(Thread.MIN_PRIORITY);
		anim.start();
	}

	public synchronized void stop() {
		anim = null;
		notify();
	}

	public void paint(Graphics g) {
		synchronized (lock) {
			Graphics2D g2d = (Graphics2D) g;
			if (img != null) {
				g2d.setComposite(AlphaComposite.Src);
				g2d.drawImage(img, null, 0, 0);
			}
		}
	}

	Font font = new Font("宋体", Font.BOLD, 14);

	public void run() {

		BufferedImage solar_panel = ButtonUI.createImageBuff("solar/solar_panel_good.gif");

		BufferedImage arrow1 = ButtonUI.createImageBuff("solar/arrowright.png");

		BufferedImage sewage_controller = ButtonUI.createImageBuff("solar/solar_sewage_controller.gif");
		BufferedImage battery_pack = ButtonUI.createImageBuff("solar/battery_pack.gif");

		BufferedImage arrow2 = ButtonUI.createImageBuff("solar/arrowright.png");

		BufferedImage fan = ButtonUI.createImageBuff("solar/fan_good.gif");
		Image fans = ButtonUI.createGifImage("solar/fan_good.gif");

		BufferedImage bakfan = ButtonUI.createImageBuff("solar/bakfan_good.gif");
		BufferedImage waterpump = ButtonUI.createImageBuff("solar/waterpump_good.gif");
		BufferedImage bakwaterpump = ButtonUI.createImageBuff("solar/bakwaterpump_good.gif");

		Graphics2D g2d = null;
		Graphics2D bufferG2D = null;// 缓冲
		Thread me = Thread.currentThread();
		Dimension oldSize = getSize();
		while (anim == me) {
			if (this.lastGetData == null) {
				synchronized (lock) {
					try {
						lock.wait(1000);
					} catch (InterruptedException e) {
					}
				}
				continue;
			}
			// 获得组件大小
			Dimension size = getSize();

			// 窗口大小变化，释放现有的gd,重新创建图像对象
			if (size.width != oldSize.width || size.height != oldSize.height) {
				img = null;
				// 清除重画
				if (bufferG2D != null)
					bufferG2D.dispose();
				bufferG2D = null;
			}
			oldSize = size;
			// 创建一个图形对象
			if (img == null)
				img = (BufferedImage) createImage(size.width, size.height);
			if (bufferG2D == null) {
				bufferG2D = img.createGraphics();
				bufferG2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
				bufferG2D.setClip(null);
			}
			g2d = bufferG2D;

			synchronized (lock) {
				g2d.setBackground(backgroundColor);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

				g2d.clearRect(0, 0, getWidth(), getHeight());
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.drawImage(solar_panel.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 40 + 100,
						50 + 150, null);

				Color backColor = g2d.getColor();
				writeData(g2d, "电池板电压:", 15 + 100, 120 + 150, 120);
				writeData(g2d, "充电电流:", 15 + 100, 150 + 150, 120);
				writeData(g2d, "总发电量:", 15 + 100, 180 + 150, 120);
				g2d.setColor(backColor);

				g2d.drawImage(arrow1.getScaledInstance(IMG_WIDTH / 2, IMG_HEIGHT / 2, Image.SCALE_SMOOTH), 120 + 100,
						65 + 150, null);

				g2d.drawImage(sewage_controller.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 200 + 100,
						50 + 100, null);
				writeData(g2d, "太阳能污水处理控制器", 160 + 100, 50 + 100 + 70);
				g2d.setColor(backColor);

				g2d.drawImage(battery_pack.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 200 + 100,
						150 + 100, null);
				writeData(g2d, "电池剩余容量:", 170 + 100, 150 + 100 + 70, 120);
				g2d.setColor(backColor);

				g2d.drawImage(arrow2.getScaledInstance(IMG_WIDTH / 2, IMG_HEIGHT / 2, Image.SCALE_SMOOTH), 280 + 100,
						165, null);

				g2d.drawRoundRect(340 + 100, 45, 80, 370, 20, 20);

				// g2d.drawImage(fan.getScaledInstance(IMG_WIDTH, IMG_HEIGHT,
				// Image.SCALE_SMOOTH), 350+100, 50, null);
				g2d.drawImage(fans, 350 + 100, 50, null);

				g2d.drawImage(bakfan.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 100, 150,
						null);
				g2d.drawImage(waterpump.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 100, 250,
						null);
				g2d.drawImage(bakwaterpump.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 100, 350,
						null);
			}
			repaint();
			Thread.yield();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}
		}
		if (g2d != null) {
			g2d.dispose();
		}
	}

	private void writeData(Graphics2D g2d, String content, int x, int y, int width) {
		g2d.setColor(fontBackColor);
		@SuppressWarnings("restriction")
		FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
		Rectangle2D stringBounds = fm.getStringBounds(content, g2d);

		g2d.fillRect(x, y, 120, 20);
		g2d.setColor(fontColor);
		g2d.setFont(font);

		g2d.drawString(content, x + (int) stringBounds.getX(), y + (int) stringBounds.getHeight());
	}

	private void writeData(Graphics2D g2d, String content, int x, int y) {
		g2d.setColor(fontBackColor);
		@SuppressWarnings("restriction")
		FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);
		Rectangle2D stringBounds = fm.getStringBounds(content, g2d);

		g2d.fillRect(x, y, (int) stringBounds.getWidth(), 20);
		g2d.setColor(fontColor);
		g2d.setFont(font);

		g2d.drawString(content, x + (int) stringBounds.getX(), y + (int) stringBounds.getHeight());
	}

	private SoRunningData lastGetData = null;

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof SoRet) {
			SoRet ret = (SoRet) arg;
			int code = ret.getCode();
			int status = ret.getStatus();
			if (status == 0) {
				switch (code) {
				case ConnectAPI.DEVICES_RUNNINGDATA_RESPONSE:
					List<SoRunningData> runningDatas = JsonUtilTool.fromJson(ret.getRet(),
							new TypeReference<List<SoRunningData>>() {
							});
					if (runningDatas.size() > 0) {
						lastGetData = runningDatas.get(0);
						synchronized (lock) {
							lock.notify();
						}
					}
					break;
				default:
					break;
				}
			}
		}
	}

}

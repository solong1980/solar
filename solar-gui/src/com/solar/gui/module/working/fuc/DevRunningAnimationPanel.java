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

	public void drawWithGif() {
		Image solar_panel = ButtonUI.createGifImage("solar/solar_panel_good.gif");

		BufferedImage arrow1 = ButtonUI.createImageBuff("solar/arrowright.png");

		Image sewage_controller = ButtonUI.createGifImage("solar/solar_sewage_controller.gif");
		Image battery_pack = ButtonUI.createGifImage("solar/battery_pack.gif");

		BufferedImage arrow2 = ButtonUI.createImageBuff("solar/arrowright.png");

		Image fan = ButtonUI.createGifImage("solar/fan_good.gif");
		Image fanbad = ButtonUI.createGifImage("solar/fan_bad.gif");

		Image bakfan = ButtonUI.createGifImage("solar/bakfan_good.gif");
		Image bakfanbad = ButtonUI.createGifImage("solar/bakfan_bad.gif");

		Image waterpump = ButtonUI.createGifImage("solar/waterpump_good.gif");
		Image waterpumpbad = ButtonUI.createGifImage("solar/waterpump_bad.gif");

		Image bakwaterpump = ButtonUI.createGifImage("solar/bakwaterpump_good.gif");
		Image bakwaterpumpbad = ButtonUI.createGifImage("solar/bakwaterpump_bad.gif");

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
			// 太阳能板电压
			String vssun = this.lastGetData.getVssun();
			// 电池充电电流
			String ichg = this.lastGetData.getIchg();
			// 充电累积度数
			String pchg = this.lastGetData.getPchg();
			// 电池剩余容量
			String level = this.lastGetData.getLevel();

			String stat = this.lastGetData.getStat();

			int st = Integer.parseInt(stat);

			// boolean s0bad = ((st & 8) == 8) ? true : false;

			boolean s1bad = ((st & 32768) == 32768) ? true : false;
			boolean s2bad = ((st & 16384) == 16384) ? true : false;
			boolean s3bad = ((st & 8192) == 8192) ? true : false;
			boolean s4bad = ((st & 4096) == 4096) ? true : false;

			this.lastGetData.getVssun();
			synchronized (lock) {
				g2d.setBackground(backgroundColor);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

				g2d.clearRect(0, 0, getWidth(), getHeight());
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.drawImage(solar_panel, 40 + 100, 50 + 150, null);

				Color backColor = g2d.getColor();
				writeData(g2d, "电池板电压:" + vssun + "V", 15 + 100, 120 + 150, 120);
				writeData(g2d, "充电电流:" + ichg + "A", 15 + 100, 150 + 150, 120);
				try {
					writeData(g2d, "总发电量:" + String.format("%.3f ", Long.parseLong(pchg) / 1000.0f) + "度", 15 + 100,
							180 + 150);
				} catch (Exception e) {
				}
				g2d.setColor(backColor);

				// 第一个箭头
				g2d.drawImage(arrow1.getScaledInstance(IMG_WIDTH / 2, IMG_HEIGHT / 2, Image.SCALE_SMOOTH), 120 + 150,
						65 + 150, null);

				g2d.drawImage(sewage_controller, 200 + 200, 50 + 100, null);
				writeData(g2d, "太阳能污水处理控制器", 160 + 200, 50 + 100 + 70);
				g2d.setColor(backColor);

				g2d.drawImage(battery_pack, 200 + 200, 150 + 100, null);
				writeData(g2d, "电池剩余容量:" + level + "%", 170 + 200, 150 + 100 + 70);
				g2d.setColor(backColor);

				// 第二个箭头
				g2d.drawImage(arrow2.getScaledInstance(IMG_WIDTH / 2, IMG_HEIGHT / 2, Image.SCALE_SMOOTH), 280 + 250,
						165, null);

				g2d.drawRoundRect(340 + 300, 45, 80, 370, 20, 20);

				if (s1bad)
					g2d.drawImage(fanbad, 350 + 300, 50, null);
				else
					// g2d.drawImage(fan.getScaledInstance(IMG_WIDTH, IMG_HEIGHT,
					// Image.SCALE_SMOOTH), 350 + 100, 50,
					// null);
					g2d.drawImage(fan, 350 + 300, 50, null);

				if (s2bad)
					g2d.drawImage(bakfanbad, 350 + 300, 150, null);
				else
					g2d.drawImage(bakfan, 350 + 300, 150, null);

				if (s3bad)
					g2d.drawImage(waterpumpbad, 350 + 300, 250, null);
				else
					g2d.drawImage(waterpump, 350 + 300, 250, null);

				if (s4bad)
					g2d.drawImage(bakwaterpumpbad, 350 + 300, 350, null);
				else
					g2d.drawImage(bakwaterpump, 350 + 300, 350, null);
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

	public void run() {

		BufferedImage solar_panel = ButtonUI.createImageBuff("solar/solar_panel_good.gif");

		BufferedImage arrow1 = ButtonUI.createImageBuff("solar/arrowright.png");

		BufferedImage sewage_controller = ButtonUI.createImageBuff("solar/solar_sewage_controller.gif");
		BufferedImage battery_pack = ButtonUI.createImageBuff("solar/battery_pack.gif");

		BufferedImage arrow2 = ButtonUI.createImageBuff("solar/arrowright.png");

		BufferedImage fan = ButtonUI.createImageBuff("solar/fan_good.gif");
		BufferedImage fanbad = ButtonUI.createImageBuff("solar/fan_bad.gif");

		Image fans = ButtonUI.createGifImage("solar/fan_good.gif");

		BufferedImage bakfan = ButtonUI.createImageBuff("solar/bakfan_good.gif");
		BufferedImage bakfanbad = ButtonUI.createImageBuff("solar/bakfan_bad.gif");

		BufferedImage waterpump = ButtonUI.createImageBuff("solar/waterpump_good.gif");
		BufferedImage waterpumpbad = ButtonUI.createImageBuff("solar/waterpump_bad.gif");

		BufferedImage bakwaterpump = ButtonUI.createImageBuff("solar/bakwaterpump_good.gif");
		BufferedImage bakwaterpumpbad = ButtonUI.createImageBuff("solar/bakwaterpump_bad.gif");

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
			// 太阳能板电压
			String vssun = this.lastGetData.getVssun();
			// 电池充电电流
			String ichg = this.lastGetData.getIchg();
			// 充电累积度数
			String pchg = this.lastGetData.getPchg();
			// 电池剩余容量
			String level = this.lastGetData.getLevel();

			String stat = this.lastGetData.getStat();

			int st = Integer.parseInt(stat);

			// boolean s0bad = ((st & 8) == 8) ? true : false;

			boolean s1bad = ((st & 32768) == 32768) ? true : false;
			boolean s2bad = ((st & 16384) == 16384) ? true : false;
			boolean s3bad = ((st & 8192) == 8192) ? true : false;
			boolean s4bad = ((st & 4096) == 4096) ? true : false;

			this.lastGetData.getVssun();
			synchronized (lock) {
				g2d.setBackground(backgroundColor);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

				g2d.clearRect(0, 0, getWidth(), getHeight());
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2d.drawImage(solar_panel.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 40 + 100,
						50 + 150, null);

				Color backColor = g2d.getColor();
				writeData(g2d, "电池板电压:" + vssun + "V", 15 + 100, 120 + 150, 120);
				writeData(g2d, "充电电流:" + ichg + "A", 15 + 100, 150 + 150, 120);
				try {
					writeData(g2d, "总发电量:" + String.format("%.3f ", Long.parseLong(pchg) / 1000.0f) + "度", 15 + 100,
							180 + 150);
				} catch (Exception e) {
				}
				g2d.setColor(backColor);

				// 第一个箭头
				g2d.drawImage(arrow1.getScaledInstance(IMG_WIDTH / 2, IMG_HEIGHT / 2, Image.SCALE_SMOOTH), 120 + 150,
						65 + 150, null);

				g2d.drawImage(sewage_controller.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 200 + 200,
						50 + 100, null);
				writeData(g2d, "太阳能污水处理控制器", 160 + 200, 50 + 100 + 70);
				g2d.setColor(backColor);

				g2d.drawImage(battery_pack.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 200 + 200,
						150 + 100, null);
				writeData(g2d, "电池剩余容量:" + level + "%", 170 + 200, 150 + 100 + 70);
				g2d.setColor(backColor);

				// 第二个箭头
				g2d.drawImage(arrow2.getScaledInstance(IMG_WIDTH / 2, IMG_HEIGHT / 2, Image.SCALE_SMOOTH), 280 + 250,
						165, null);

				g2d.drawRoundRect(340 + 300, 45, 80, 370, 20, 20);

				if (s1bad)
					g2d.drawImage(fanbad.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 300, 50,
							null);
				else
					// g2d.drawImage(fan.getScaledInstance(IMG_WIDTH, IMG_HEIGHT,
					// Image.SCALE_SMOOTH), 350 + 100, 50,
					// null);
					g2d.drawImage(fans, 350 + 300, 50, null);

				if (s2bad)
					g2d.drawImage(bakfanbad.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 300,
							150, null);
				else
					g2d.drawImage(bakfan.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 300, 150,
							null);
				if (s3bad)
					g2d.drawImage(waterpumpbad.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 300,
							250, null);
				else
					g2d.drawImage(waterpump.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 300,
							250, null);

				if (s4bad)
					g2d.drawImage(bakwaterpumpbad.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH),
							350 + 300, 350, null);
				else
					g2d.drawImage(bakwaterpump.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH), 350 + 300,
							350, null);
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

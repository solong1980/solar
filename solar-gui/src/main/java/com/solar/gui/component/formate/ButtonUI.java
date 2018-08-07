package com.solar.gui.component.formate;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ButtonUI {
	public static ImageIcon createImageIcon(String filename, String description) {
		String path = "/resources/images/" + filename;
		return new ImageIcon(ButtonUI.class.getClass().getResource(path), description);
	}

	public static BufferedImage createImageBuff(String filename) {
		String path = "/resources/images/" + filename;
		BufferedImage buf = null;
		try {
			buf = ImageIO.read(ButtonUI.class.getClass().getResource(path));
		} catch (IOException e) {

		}
		return buf;
	}

	public static Image createGifImage(String filename) {
		String path = "/resources/images/" + filename;
		Image image = null;
		try {
			image = Toolkit.getDefaultToolkit().createImage(ButtonUI.class.getResource(path));
			MediaTracker mt = new MediaTracker(new JPanel());// new JPanel可以替换成当前的视图类
			mt.addImage(image, 0);
			mt.waitForAll();
			//image = image.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static BufferedImage createGifImageBuff(String filename) {
		String path = "/resources/images/" + filename;
		BufferedImage buf = null;
		try {
			Image image = Toolkit.getDefaultToolkit().createImage(ButtonUI.class.getResource(path));

			MediaTracker mt = new MediaTracker(new JPanel());// new JPanel可以替换成当前的视图类
			mt.addImage(image, 0);
			mt.waitForAll();

			buf = createImageBuff(image);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return buf;
	}

	public static BufferedImage createImageBuff(Image img) {
		BufferedImage buf = null;

		buf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = buf.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		return buf;
	}

	public static JRadioButton makeRadioBtn(String text, String description) {
		JRadioButton radio = new JRadioButton(text, createImageIcon("buttons/rb.gif", description));
		radio.setPressedIcon(createImageIcon("buttons/rbp.gif", description));
		radio.setRolloverIcon(createImageIcon("buttons/rbr.gif", description));
		radio.setRolloverSelectedIcon(createImageIcon("buttons/rbrs.gif", description));
		radio.setSelectedIcon(createImageIcon("buttons/rbs.gif", description));
		radio.setMargin(new Insets(0, 0, 0, 0));
		return radio;
	}

	public static JPanel pagePanel() {
		JPanel pagionationPanel = new JPanel();
		pagionationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton comp = new JButton("上一页");
		JButton comp2 = new JButton("下一页");

		JLabel gongLabel = new JLabel("共");
		JTextField totalField = new JTextField(3);
		totalField.setEnabled(false);
		JLabel tiaoLabel = new JLabel("条");
		pagionationPanel.add(comp);
		pagionationPanel.add(comp2);
		pagionationPanel.add(Box.createHorizontalGlue());
		pagionationPanel.add(gongLabel);
		pagionationPanel.add(totalField);
		pagionationPanel.add(tiaoLabel);
		return pagionationPanel;
	}

	public static JButton makeImgBtn(String filename) {
		JButton button = new JButton(createImageIcon(filename, ""));
		return button;
	}
}

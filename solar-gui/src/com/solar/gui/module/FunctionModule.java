package com.solar.gui.module;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class FunctionModule extends JPanel {
	private static final long serialVersionUID = 1L;

	protected int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	protected int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	protected GraphicsConfiguration defaultConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDefaultConfiguration();
	// 获取屏幕边界
	protected Insets SCREEN_INSETS = Toolkit.getDefaultToolkit().getScreenInsets(defaultConfiguration);
	// 取得底部边界高度，即任务栏高度
	protected int TASKCOL_HEIGHT = SCREEN_INSETS.bottom;

	// The preferred size of the demo
	protected int PREFERRED_WIDTH = SCREEN_WIDTH;
	protected int PREFERRED_HEIGHT = SCREEN_HEIGHT - TASKCOL_HEIGHT;

	protected int HEADER_HEIGHT = 0;

	Border loweredBorder = new CompoundBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED),
			new EmptyBorder(5, 5, 5, 5));

	// Premade convenience dimensions, for use wherever you need 'em.
	public static Dimension HGAP2 = new Dimension(2, 1);
	public static Dimension VGAP2 = new Dimension(1, 2);

	public static Dimension HGAP5 = new Dimension(5, 1);
	public static Dimension VGAP5 = new Dimension(1, 5);

	public static Dimension HGAP10 = new Dimension(10, 1);
	public static Dimension VGAP10 = new Dimension(1, 10);

	public static Dimension HGAP15 = new Dimension(15, 1);
	public static Dimension VGAP15 = new Dimension(1, 15);

	public static Dimension HGAP20 = new Dimension(20, 1);
	public static Dimension VGAP20 = new Dimension(1, 20);

	public static Dimension HGAP25 = new Dimension(25, 1);
	public static Dimension VGAP25 = new Dimension(1, 25);

	public static Dimension HGAP30 = new Dimension(30, 1);
	public static Dimension VGAP30 = new Dimension(1, 30);

	private JPanel swingset = null;
	private JPanel panel = null;
	private String resourceName = null;

	// Resource bundle for internationalized and accessible text
	private ResourceBundle bundle = null;

	public FunctionModule(JPanel swingset) {
		this(swingset, null, null);
	}

	public FunctionModule(JPanel swingset, String resourceName, String iconPath) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		this.resourceName = resourceName;
		this.swingset = swingset;
		if (swingset == null) {
			HEADER_HEIGHT = 0;
		} else {
			HEADER_HEIGHT = 150;
		}
	}

	public String getResourceName() {
		return resourceName;
	}

	public JPanel getModulePanel() {
		return panel;
	}

	public JPanel getJPanel() {
		return swingset;
	}

	public String getString(String key) {
		String value = key;
		try {
			if (bundle == null) {
				bundle = ResourceBundle.getBundle("resources.swingset");
			}
			value = bundle.getString(key);
		} catch (MissingResourceException e) {
			System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
		}
		return value;
	}

	public static final String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
	public static final String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
	public static final String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	public static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	public static final String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";

	public void mainImpl() {
		JFrame frame = new JFrame(getName());
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(getModulePanel(), BorderLayout.CENTER);
		getModulePanel().setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT - 40));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(metal);
			SwingUtilities.updateComponentTreeUI(frame);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// MetalLookAndFeel.setCurrentTheme(new AquaTheme());
		// MetalLookAndFeel.setCurrentTheme(new CharcoalTheme());
		// MetalLookAndFeel.setCurrentTheme(new ContrastTheme());
		// MetalLookAndFeel.setCurrentTheme(new EmeraldTheme());
		// MetalLookAndFeel.setCurrentTheme(new RubyTheme());
		// MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
		MetalLookAndFeel.setCurrentTheme(new OceanTheme());

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		FunctionModule demo = new FunctionModule(null);
		demo.mainImpl();
	}

	public void init() {
		setLayout(new BorderLayout());
		add(getModulePanel(), BorderLayout.CENTER);
	}

	public void updateDragEnabled(boolean dragEnabled) {
	}
}

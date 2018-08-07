package com.solar.gui.module.working.chart;

import java.awt.Font;
import java.util.Locale;

import org.jfree.chart.StandardChartTheme;

public class ZhTheam extends StandardChartTheme {
	private static final long serialVersionUID = -923879426981638151L;

	public static ZhTheam createZhTheme() {
		return new ZhTheam();
	}

	public ZhTheam() {
		super("JFree");
		// The default font used by JFreeChart unable to render Chinese properly.
		// We need to provide font which is able to support Chinese rendering.
		if (Locale.getDefault().getLanguage().equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
			final Font oldExtraLargeFont = this.getExtraLargeFont();
			final Font oldLargeFont = this.getLargeFont();
			final Font oldRegularFont = this.getRegularFont();
			final Font oldSmallFont = this.getSmallFont();

			final Font extraLargeFont = new Font("Sans-serif", oldExtraLargeFont.getStyle(),
					oldExtraLargeFont.getSize());
			final Font largeFont = new Font("Sans-serif", oldLargeFont.getStyle(), oldLargeFont.getSize());
			final Font regularFont = new Font("Sans-serif", oldRegularFont.getStyle(), oldRegularFont.getSize());
			final Font smallFont = new Font("Sans-serif", oldSmallFont.getStyle(), oldSmallFont.getSize());

			this.setExtraLargeFont(extraLargeFont);
			this.setLargeFont(largeFont);
			this.setRegularFont(regularFont);
			this.setSmallFont(smallFont);
		}
	}

}

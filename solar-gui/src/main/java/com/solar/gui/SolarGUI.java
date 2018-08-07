package com.solar.gui;

import java.awt.EventQueue;

import com.solar.gui.module.InternalFrameModule;

public class SolarGUI {
	public SolarGUI() {
		super();
		InternalFrameModule demo = new InternalFrameModule(null);
		demo.mainImpl();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new SolarGUI();
			}
		});
	}

}

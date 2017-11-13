package com.solar.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.solar.server.mina.bootstrap.SolarServer;

public class SolarServerStartupAndStop implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//SolarServer solarServer = (SolarServer) arg0.getServletContext().getAttribute("gameserver");
		SolarServer.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		SolarServer solarServer = SolarServer.getInstance();
		arg0.getServletContext().setAttribute("solarserver", solarServer);
		SolarServer.startUp();
	}

}

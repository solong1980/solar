package com.solar.cli;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ConfigLoader {
	public static Properties loadConf(String config) {
		try {
			URL resource = Thread.currentThread().getContextClassLoader().getResource(config);
			Properties conf = new Properties();
			conf.load(resource.openStream());
			return conf;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}

package com.solar.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.solar.entity.SoAccount;

public class Endpoint {
	public static <T> T getObject(String name) {
		try {
			Class<?> forName = Class.forName(name);
			Object newInstance = forName.newInstance();
			return (T) newInstance;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws MalformedURLException {
		// TODO 维护10000个终端长连接
		scan();
	}

	private static void scan() throws MalformedURLException {
		URL resource = Endpoint.class.getClassLoader().getResource(".");
		String substring = resource.getFile().toString().substring(1).replace("/", "\\");

		URL s = new URL(resource, "com/solar/entity");
		String file = s.getFile();
		File[] listFiles = new File(file.substring(1).replace("/", "\\")).listFiles();

		for (File klassFile : listFiles) {
			String klassName = klassFile.getPath().replace(substring, "");
			klassName = klassName.replace("\\", ".");
			klassName = klassName.replace(".class", "");
			// create wapper class
			SoAccount a = getObject(klassName);
		}
	}

}

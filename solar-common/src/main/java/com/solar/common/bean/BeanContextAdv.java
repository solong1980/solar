package com.solar.common.bean;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.solar.common.context.ConnectAPI;

public class BeanContextAdv {
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
		Field[] fields = ConnectAPI.class.getFields();
		for (Field field : fields) {
			System.out.println(field.getName());
		}

		// scan();
	}

	public <T> List<T> scan(String pkg) throws MalformedURLException {
		String path = pkg.replace(".", File.separator);
		URL resource = BeanContextAdv.class.getClassLoader().getResource(".");
		String substring = resource.getFile().toString().substring(1).replace("/", File.separator);

		URL s = new URL(resource, path);
		String file = s.getFile();
		File[] listFiles = new File(file.substring(1).replace("/", File.separator)).listFiles();

		List<T> ts = new ArrayList<>();
		for (File klassFile : listFiles) {
			String klassName = klassFile.getPath().replace(substring, "");
			klassName = klassName.replace("\\", ".");
			klassName = klassName.replace(".class", "");
			// create wapper class
			T a = getObject(klassName);
			if (a != null)
				ts.add(a);
		}

		return ts;
	}
}

package com.solar.common.bean;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
		if (listFiles == null)
			try {
				Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader()
						.getResources(pkg.replace(".", "/"));
				while (urlEnumeration.hasMoreElements()) {
					URL url = urlEnumeration.nextElement();// 得到的结果大概是：jar:file:/C:/Users/ibm/.m2/repository/junit/junit/4.12/junit-4.12.jar!/org/junit
					String protocol = url.getProtocol();// 大概是jar
					if ("jar".equalsIgnoreCase(protocol)) {
						// 转换为JarURLConnection
						JarURLConnection connection = (JarURLConnection) url.openConnection();
						if (connection != null) {
							JarFile jarFile = connection.getJarFile();
							if (jarFile != null) {
								// 得到该jar文件下面的类实体
								Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
								while (jarEntryEnumeration.hasMoreElements()) {
									/*
									 * entry的结果大概是这样： org/ org/junit/
									 * org/junit/rules/ org/junit/runners/
									 */
									JarEntry entry = jarEntryEnumeration.nextElement();
									String jarEntryName = entry.getName();
									// 这里我们需要过滤不是class文件和不在basePack包名下的类
									if (jarEntryName.contains(".class")
											&& jarEntryName.replaceAll("/", ".").startsWith(pkg)) {
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf("."))
												.replace("/", ".");
										Class cls = Class.forName(className);
										System.out.println(cls);
									}
								}
							}
						}
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}

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

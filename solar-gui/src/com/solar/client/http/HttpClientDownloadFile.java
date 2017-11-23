package com.solar.client.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientDownloadFile {
	public static void main(String[] args) {
	}

	public void upfile(Long id) {
		String url = "http://localhost:8080/solar-web/file/download/" + id;
		String savepath = ".";
		HttpClientDownloadFile clientDownloadFile = new HttpClientDownloadFile();
		clientDownloadFile.download(url, savepath);
	}

	public String download(String url, String savepath) {
		CloseableHttpClient client = HttpClients.createDefault();
		RequestConfig config = null;
		config = RequestConfig.custom().build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(config);
		try {
			HttpResponse respone = client.execute(httpGet);
			if (respone.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}
			HttpEntity entity = respone.getEntity();
			Header[] allHeaders = respone.getAllHeaders();

			String filename = UUID.randomUUID().toString();
			for (Header header : allHeaders) {
				// 获取文件名
				String name = header.getName();
				if (name.equals("Content-disposition")) {
					String value = header.getValue();
					value = value.replace("attachment; filename=", "");
					byte[] bytes = value.getBytes("iso-8859-1");
					filename = new String(bytes, "utf-8");
				}
			}
			if (entity != null) {
				InputStream is = entity.getContent();
				File file = new File(savepath + File.separator + filename);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[4096];
				int len = -1;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}

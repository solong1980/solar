package com.solar.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.solar.db.services.SoAppVersionService;
import com.solar.entity.SoAppVersion;

@Controller
public class SoDownloadController {
	private static final Logger logger = LoggerFactory.getLogger(SoDownloadController.class);

	private SoAppVersionService appVersionService;

	public SoDownloadController() {
		appVersionService = SoAppVersionService.getInstance();
	}

	/**
	 * 删除
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/file/download/{id}")
	public void download(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		SoAppVersion appVersion = appVersionService.selectById(id);
		if (appVersion != null) {
			String fileName = appVersion.getFileName();
			String savedPath = appVersion.getPath();
			File f = new File(savedPath);
			String characterEncoding = request.getCharacterEncoding();
			if (f.exists()) {
				// 读取文件
				OutputStream os = new BufferedOutputStream(response.getOutputStream());
				try {
					response.setContentType("application/octet-stream");
					if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) { // IE浏览器
						fileName = URLEncoder.encode(fileName, "UTF-8");
					} else {
						fileName = URLDecoder.decode(fileName,characterEncoding);// 其他浏览器
					}
					response.setContentLengthLong(f.length());
					response.setHeader("Content-disposition",
							"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1")); // 指定下载的文件名
					os.write(FileUtils.readFileToByteArray(f));
					os.flush();
				} catch (IOException e) {
					logger.error("download file id=" + id + " fail", e);
					e.printStackTrace();
				} finally {
					if (os != null) {
						os.close();
					}
				}
			} else {
				logger.error("download file id=" + id + " fail,file not exists");
				throw new RuntimeException("File not exists");
			}
		}

	}
}

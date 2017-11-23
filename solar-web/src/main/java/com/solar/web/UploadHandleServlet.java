package com.solar.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.db.services.SoAppVersionService;
import com.solar.entity.SoAppVersion;

@SuppressWarnings("serial")
public class UploadHandleServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(UploadHandleServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
		String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
		File file = new File(savePath);
		// 判断上传文件的保存目录是否存在
		if (!file.exists() && !file.isDirectory()) {
			logger.info(savePath + " 目录不存在，需要创建");
			// 创建目录
			file.mkdir();
		}
		// 消息提示
		String message = "";
		int success = 0;// 0 没问题，1 有问题
		SoAppVersion appVersion = new SoAppVersion();

		try {
			// 使用Apache文件上传组件处理文件上传步骤：
			// 1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 2、创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 3、判断提交上来的数据是否是上传表单的数据
			if (!ServletFileUpload.isMultipartContent(request)) {
				// 按照传统方式获取数据
				logger.error("表单提交设置错误");
				return;
			}
			// 4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			List<FileItem> list = upload.parseRequest(request);

			for (FileItem item : list) {
				InputStream in = null;
				FileOutputStream out = null;
				// 如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					String name = item.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = item.getString("UTF-8");

					if (value == null || value.isEmpty()) {
						if (name.equals("info"))
							message = "版本信息 不能为空!";
						if (name.equals("type"))
							message = "包类型 不能为空!";
						success = 1;
						break;
					}
					logger.info(name + "=" + value);

					if (name.equals("info"))
						appVersion.setInfo(value);
					if (name.equals("type"))
						appVersion.setType(Integer.parseInt(value));

				} else {
					try {
						String filename = item.getName();

						logger.info("upload filename = " + filename);
						if (filename == null || filename.trim().equals("")) {
							continue;
						}
						// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
						// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
						// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
						filename = filename.substring(filename.lastIndexOf("\\") + 1);
						String filePath = savePath + "\\" + filename;

						appVersion.setFileName(filename);
						appVersion.setPath(filePath);

						// 获取item中的上传文件的输入流
						in = item.getInputStream();
						// 创建一个文件输出流
						out = new FileOutputStream(filePath);

						// 创建一个缓冲区
						byte buffer[] = new byte[1024];
						// 判断输入流中的数据是否已经读完的标识
						int len = 0;
						// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
						while ((len = in.read(buffer)) > 0) {
							// 使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath +
							// "\\"
							// + filename)当中
							out.write(buffer, 0, len);
						}

						// 删除处理文件上传时生成的临时文件
						item.delete();
						message = "文件上传成功！";
					} finally {
						// 关闭输入流
						if (in != null)
							in.close();
						if (out != null)
							// 关闭输出流
							out.close();
					}
				}
			}
		} catch (Exception e) {
			success = 1;
			message = "文件上传失败！";
			e.printStackTrace();
		}
		if (message.isEmpty()) {
			success = 1;
			message = "请选择文件!";
		}

		if (success == 0) {
			// 保存新的版本
			SoAppVersionService.getInstance().addNewVersion(appVersion);
		}

		request.setAttribute("success", success);
		request.setAttribute("message", message);
		request.getRequestDispatcher("../message.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
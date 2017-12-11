package com.solar.mns.email;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class MailServer {
	public static final Logger logger = LoggerFactory.getLogger(MailServer.class);

	public static MailServer mailServer = new MailServer();

	public static MailServer getInstance() {
		return mailServer;
	}

	private Properties props = new Properties();
	private Authenticator authenticator;

	private MailServer() {
		logger.debug("init mail server");
		try {
			URL resource = Thread.currentThread().getContextClassLoader().getResource("mail");
			String file = resource.getPath();
			Properties properties = new Properties();
			properties.load(Files.asCharSource(new File(file), Charsets.UTF_8).openStream());

			String host = properties.getProperty("host");
			String server = properties.getProperty("server");
			String from = properties.getProperty("from");
			String pa = properties.getProperty("passs");
			// 开启debug调试
			props.setProperty("mail.debug", "false");
			// 发送服务器需要身份验证
			props.setProperty("mail.smtp.auth", "true");
			// 设置邮件服务器主机名
			props.setProperty("mail.host", host);
			props.setProperty("mail.server", server);
			// 发送邮件协议名称
			props.setProperty("mail.transport.protocol", "smtp");

			// 发件人的账号
			props.put("mail.user", from);
			// 访问SMTP服务时需要提供的密码
			props.put("mail.password", pa);
			authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 用户名、密码
					String userName = props.getProperty("mail.user");
					String password = props.getProperty("mail.password");
					return new PasswordAuthentication(userName, password);
				}
			};
		} catch (IOException e) {
			logger.error("init mail server error", e);
		}

	}

	public void send(String receiver, String subject, String content) throws MessagingException {
		logger.info("send mail to " + receiver);
		// 设置环境信息
		Session session = Session.getInstance(props, authenticator);
		// 创建邮件对象
		Message msg = new MimeMessage(session);
		msg.setSubject(subject);
		// 设置邮件内容
		msg.setText(content);
		// 设置发件人
		msg.setFrom(new InternetAddress(props.getProperty("mail.user")));

		Transport transport = session.getTransport();
		// 连接邮件服务器
		transport.connect(props.getProperty("mail.server"), "javamail");
		// 发送邮件
		transport.sendMessage(msg, new Address[] { new InternetAddress(receiver) });
		// 关闭连接
		transport.close();
	}
	
	public static void main(String[] args) {
		try {
			MailServer.getInstance().send("solong1980@qq.com", "测试", "你的密码aaaaa,新手机号码11111");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}

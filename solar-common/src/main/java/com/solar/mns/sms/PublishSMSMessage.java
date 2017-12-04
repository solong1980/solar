package com.solar.mns.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class PublishSMSMessage {

	private static final String REGEIN_ID = "cn-af";
	private static final String SMS_115635041 = "sadfas";
	private static final String SignName = "阿里云短信测试专用";
	// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	private static final String AccessKeyId = "adfasdf";
	private static final String AccessKeySecret = "sadffafsd";

	// 产品名称:云通信短信API产品,开发者无需替换
	static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	static final String domain = "dysmsapi.aliyuncs.com";

	public static SendSmsResponse sendSms(String outId, String phone, String jsonParam) throws ClientException {
		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile(REGEIN_ID, AccessKeyId, AccessKeySecret);
		DefaultProfile.addEndpoint(REGEIN_ID, REGEIN_ID, product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(phone);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName(SignName);
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(SMS_115635041);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		request.setTemplateParam(jsonParam);
		// 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId(outId);

		// 可自助调整超时时间
		// System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		// System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		return sendSmsResponse;
	}

	public static void main(String[] args) throws ClientException {
		SendSmsResponse sendSms = sendSms("1000", "15827164571", "{\"customer\": \"longlianghua\"}");
		System.out.println(sendSms);
	}

}
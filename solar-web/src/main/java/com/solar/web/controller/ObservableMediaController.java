package com.solar.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solar.common.context.Consts.AuditResult;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoAppVersion;
import com.solar.entity.SoDevices;
import com.solar.entity.SoPage;
import com.solar.entity.SoProject;
import com.solar.entity.SoProjectWorkingMode;
import com.solar.entity.SoRunningData;
import com.solar.entity.SoVCode;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class ObservableMediaController {

	@ApiOperation(value = "登录", httpMethod = "POST", notes = "通过帐号,密码登陆")
	@RequestMapping("/solar/login")
	public String login(@ApiParam(required = true, name = "account", value = "帐户对象") SoAccount account) {
		String jsonString = "";
		return jsonString;
	}

	@ApiOperation(value = "查询最新版本", httpMethod = "POST", notes = "启动时查询版本对象")
	@RequestMapping("/solar/appVersion")
	public String queryAppVersion(
			@ApiParam(required = true, name = "appVersion", value = "客户端类型:APK(10), IPA(50), RPM(100);") SoAppVersion appVersion) {
		String jsonString = "";
		return jsonString;
	}

	@ApiOperation(value = "添加项目信息", httpMethod = "POST", notes = "保存项目")
	@RequestMapping("/solar/addProject")
	public String saveProject(@ApiParam(required = true, name = "project", value = "项目信息") SoProject project) {
		String jsonString = "";
		return jsonString;
	}

	@ApiOperation(value = "用户注册", httpMethod = "POST", notes = "用户提交注册信息")
	@RequestMapping("/solar/regiest")
	public String regiest(@ApiParam(required = true, name = "account", value = "注册信息") SoAccount account) {
		String jsonString = "";
		return jsonString;
	}

	public void getVCode(SoVCode soVCode) {
	}

	public void findBack(SoAccountFind accountFind) {
	}

	public void accountFindQuery(SoAccountFind soAccountFind) {
	}

	public void accountFindAgree(Long id) {
		SoAccountFind accountFind = new SoAccountFind();
		accountFind.setId(id);
		accountFind.setStatus(AuditResult.AGREE.getStatus());
	}

	public void accountFindReject(Long id) {
		SoAccountFind accountFind = new SoAccountFind();
		accountFind.setId(id);
		accountFind.setStatus(AuditResult.REJECT.getStatus());
	}

	public void accountQuery(SoPage<SoAccount, List<SoAccount>> page) {
	}

	public void accountSelect(Long id) {
		SoAccount account = new SoAccount();
		account.setId(id);
	}

	public void accountUpdate(SoAccount account) {
	}

	public void regiestAgree(SoAccount account) {
		account.setStatus(AuditResult.AGREE.getStatus());
	}

	public void regiestReject(Long id) {
		SoAccount account = new SoAccount();
		account.setId(id);
		account.setStatus(AuditResult.REJECT.getStatus());
	}

	public void checkAccountProject(Long id) {
		SoAccount account = new SoAccount();
		account.setId(id);
	}

	public void queryProjects(SoPage<SoProject, List<SoProject>> soPage) {
		SoProject c = soPage.getC();
		if (c == null)
			soPage.setC(new SoProject());
	}

	public void selectProject(Long id) {
		SoProject project = new SoProject();
		project.setId(id);
	}

	public void deleteProject(Long id) {
		SoProject project = new SoProject();
		project.setId(id);
	}

	public void getRunningData(String devNo) {
		SoRunningData runningData = new SoRunningData();
		runningData.setUuid(devNo);
	}

	public void getProjectWorkingMode(Long projectId) {
		SoProjectWorkingMode projectWorkingMode = new SoProjectWorkingMode();
		projectWorkingMode.setProjectId(projectId);
	}

	public void updateProjectWorkingMode(SoProjectWorkingMode mode) {
	}

	public void addDevice(SoDevices device) {
	}

	public void updateDevice(SoDevices device) {
	}

}

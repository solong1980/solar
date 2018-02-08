package com.solar.cli.netty.command.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.solar.cli.netty.controller.MsgProcessor;
import com.solar.cli.netty.session.ISession;
import com.solar.command.message.request.ClientRequest;
import com.solar.command.message.response.app.LoginResponse;
import com.solar.common.annotation.ProcessCMD;
import com.solar.common.context.ConnectAPI;
import com.solar.common.context.ErrorCode;
import com.solar.common.context.RoleType;
import com.solar.common.util.JsonUtilTool;
import com.solar.controller.common.INotAuthProcessor;
import com.solar.db.services.SoAccountService;
import com.solar.db.services.SoProjectService;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountLocation;
import com.solar.entity.SoProject;

/**
 * 
 * @author long lianghua
 *
 */
@ProcessCMD(API_CODE = ConnectAPI.LOGIN_COMMAND)
public class LoginCmdProcessor extends MsgProcessor implements INotAuthProcessor {
	private static final String ADMIN_LOCATION_ID = "000000";
	private static final Logger logger = LoggerFactory.getLogger(LoginCmdProcessor.class);
	private SoAccountService accountService;
	private SoProjectService projectService;

	public LoginCmdProcessor() {
		accountService = SoAccountService.getInstance();
		projectService = SoProjectService.getInstance();
	}

	@Override
	public void process(ISession<?> session, ClientRequest request) throws Exception {
		String json = request.getString();
		if (logger.isDebugEnabled()) {
			logger.debug("login : {}", json);
		}
		SoAccount account = JsonUtilTool.fromJson(json, SoAccount.class);

		// String acc = account.getAccount();
		String passwd = account.getPassword();
		List<SoAccount> dbAccounts = accountService.selectByAccount(account);

		@SuppressWarnings("deprecation")
		String md = Hashing.md5().newHasher().putString(passwd, Charsets.UTF_8).hash().toString();

		if (null == dbAccounts || dbAccounts.isEmpty()) {
			account.setMsg(ErrorCode.Error_000003);
			account.setRetCode(SoAccount.FAILURE);
			session.sendMsg(new LoginResponse(JsonUtilTool.toJson(account)));
		} else if (dbAccounts.size() > 1) {
			account.setMsg(ErrorCode.Error_000012);
			account.setRetCode(SoAccount.FAILURE);
			session.sendMsg(new LoginResponse(JsonUtilTool.toJson(account)));
		} else {
			SoAccount dbAccount = dbAccounts.get(0);
			if (dbAccount.getPassword().equals(md)) {
				dbAccount.setMsg("登陆成功");
				dbAccount.setPassword(null);
				session.setEnti(dbAccount);
				session.setLogin(true);
				int type = dbAccount.getType();
				dbAccount.setRole(type);
				RoleType roleType = RoleType.roleType(type);
				// 查询管辖地,管理员查询所有工程地址,维护员/局方查询自己注册地址,再根据注册地址查项目
				List<SoAccountLocation> accountLocations = new ArrayList<>();
				switch (roleType) {
				case ADMIN:
					List<SoProject> projects = projectService.queryProjectByLocationId(ADMIN_LOCATION_ID);
					dbAccount.setProjects(projects);
					SoAccountLocation accountLocation = new SoAccountLocation();
					accountLocation.setLocationId(ADMIN_LOCATION_ID);
					accountLocations.add(accountLocation);
					break;
				case OPERATOR:
					//需要查询权限表
					
					break;
				case USER:
					accountLocations = accountService.queryGovernmentLocation(dbAccount.getId());
					List<String> locations = new ArrayList<>();
					for (SoAccountLocation location : accountLocations) {
						locations.add(location.getLocationId());
					}
					// 查项目
					projects = projectService.queryProjectByLocationIds(locations);
					dbAccount.setProjects(projects);
					break;
				default:
					break;
				}

				dbAccount.setLocations(accountLocations);
				session.sendMsg(new LoginResponse(JsonUtilTool.toJson(dbAccount)));
			} else {
				account.setMsg(ErrorCode.Error_000004);
				account.setRetCode(SoAccount.FAILURE);
				session.sendMsg(new LoginResponse(JsonUtilTool.toJson(account)));
			}
		}
	}
}

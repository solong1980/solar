package com.solar.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import com.solar.client.net.NetConf;
import com.solar.common.context.Consts.AuditResult;
import com.solar.common.context.Consts.ProjectType;
import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoAppVersion;
import com.solar.entity.SoDataServerInfo;
import com.solar.entity.SoDevices;
import com.solar.entity.SoPage;
import com.solar.entity.SoProject;
import com.solar.entity.SoVCode;

public class ObservableMedia extends Observable {
	HostClient hostClient = null;

	private SoAccount sessionAccount;

	private Set<String> sunPowerLocationFilterSet = new HashSet<>();
	private Set<String> smartLocationFilterSet = new HashSet<>();
	private Map<String, List<SoProject>> sunPowerLocationProjectsMap = new HashMap<String, List<SoProject>>();
	private Map<String, List<SoProject>> smartLocationProjectsMap = new HashMap<String, List<SoProject>>();

	public void setSessionAccount(SoAccount sessionAccount) {
		this.sessionAccount = sessionAccount;
		if (sessionAccount != null) {
			projectLocationFilterSet();
		}
	}

	public SoAccount getSessionAccount() {
		return sessionAccount;
	}

	public Set<String> getSunPowerLocationFilterSet() {
		return sunPowerLocationFilterSet;
	}

	public Set<String> getSmartLocationFilterSet() {
		return smartLocationFilterSet;
	}

	public List<SoProject> getSunPowerProjects(String locationId) {
		return sunPowerLocationProjectsMap.get(locationId);
	}

	public List<SoProject> getSmartProjects(String locationId) {
		return smartLocationProjectsMap.get(locationId);
	}

	private void projectLocationFilterSet() {
		if (sessionAccount == null)
			return;
		List<SoProject> projects = sessionAccount.getProjects();
		if (projects == null)
			return;
		for (SoProject soProject : projects) {

			String locationId = soProject.getLocationId();
			String provinceId = locationId.substring(0, 2) + "0000";
			String cityId = locationId.substring(0, 4) + "00";

			int type = soProject.getType();
			ProjectType pt = ProjectType.type(type);
			switch (pt) {
			case SUN_POWER:
				sunPowerLocationFilterSet.add(locationId);
				sunPowerLocationFilterSet.add(provinceId);
				sunPowerLocationFilterSet.add(cityId);

				List<SoProject> sunPowList = sunPowerLocationProjectsMap.get(locationId);
				if (sunPowList == null) {
					sunPowList = new ArrayList<>();
					sunPowerLocationProjectsMap.put(locationId, sunPowList);
				}
				sunPowList.add(soProject);

				break;
			case SMART:
				smartLocationFilterSet.add(locationId);
				smartLocationFilterSet.add(provinceId);
				smartLocationFilterSet.add(cityId);

				List<SoProject> smartList = smartLocationProjectsMap.get(locationId);
				if (smartList == null) {
					smartList = new ArrayList<>();
					smartLocationProjectsMap.put(locationId, smartList);
				}
				smartList.add(soProject);
				break;
			default:
				break;
			}
		}
	}

	public static void main(String[] args) {
		long t = System.currentTimeMillis();
		// // host connect
		// HostClient hostClient = new HostClient(NetConf.buildHostConf());
		//
		// if (hostClient.getDataServerNetConf() == null) {
		// SoDataServerInfo dataServerInfo = new SoDataServerInfo();
		// dataServerInfo.setDevId("000000");
		// hostClient.send(ConnectAPI.DATA_SERVER_QUERY_COMMAND,
		// JsonUtilTool.toJson(dataServerInfo));
		// hostClient.recive();
		// }
		// // data connect
		// DataClient dataClient = new
		// DataClient(hostClient.getDataServerNetConf());
		// // send & receive & close
		// dataClient.dataUpload();
		// hostClient.start();
		// hostClient.close();

		ObservableMedia media = new ObservableMedia();
		media.sendData();

		System.out.println(System.currentTimeMillis() - t);
	}

	private static class Inner {
		public static ObservableMedia observableMedia = new ObservableMedia();
	}

	public static ObservableMedia getInstance() {
		return Inner.observableMedia;
	}

	public void connect() {
		// host connect
		hostClient = new HostClient(this, NetConf.buildHostConf());
		// Get data server info
		if (hostClient.getDataServerNetConf() == null) {

			SoDataServerInfo dataServerInfo = new SoDataServerInfo();
			dataServerInfo.setDevId("000000");

			hostClient.getDataServerInfo(dataServerInfo);
			try {
				hostClient.recive();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		hostClient.start();
	}

	private ObservableMedia() {
		super();
	}

	public void close() {
		hostClient.stop();
	}

	public void sendData() {
		// data connect
		DataClient dataClient = new DataClient(hostClient.getDataServerNetConf());
		// send & receive & close
		// dataClient.dataUpload();
	}

	public void deviceAccess(SoDevices devices) {
		hostClient.deviceAccess(devices);
		setChanged();
	}

	public void login(SoAccount account) {
		hostClient.login(account);
		setChanged();
	}

	public void getRunningMode() {
		hostClient.runningMode();
		setChanged();
	}

	public void sendUpdateConfig() {
		hostClient.workingModeUpdate();
		setChanged();
	}

	public void getUpdate() {
		hostClient.send(12, "");
		setChanged();
	}

	public void getConfig() {
		hostClient.send(12, "");
		setChanged();
	}

	public void queryAppVersion(int type) {
		SoAppVersion appVersion = new SoAppVersion();
		appVersion.setType(type);
		hostClient.queryAppVersion(appVersion);
		setChanged();
	}

	public void saveProject(SoProject soProject) {
		hostClient.saveProject(soProject);
		setChanged();
	}

	public void regiest(SoAccount account) {
		hostClient.regiest(account);
		setChanged();
	}

	public void getVCode(SoVCode soVCode) {
		hostClient.getVCode(soVCode);
		setChanged();
	}

	public void findBack(SoAccountFind accountFind) {
		hostClient.findBack(accountFind);
		setChanged();
	}

	public void accountFindQuery(SoAccountFind soAccountFind) {
		hostClient.accountFindQuery(soAccountFind);
		setChanged();
	}

	public void accountFindAgree(Long id) {
		SoAccountFind accountFind = new SoAccountFind();
		accountFind.setId(id);
		accountFind.setStatus(AuditResult.AGREE.getStatus());
		hostClient.accountFindAudit(accountFind);
		setChanged();
	}

	public void accountFindReject(Long id) {
		SoAccountFind accountFind = new SoAccountFind();
		accountFind.setId(id);
		accountFind.setStatus(AuditResult.REJECT.getStatus());
		hostClient.accountFindAudit(accountFind);
		setChanged();
	}

	public void accountQuery(SoPage<SoAccount, List<SoAccount>> page) {
		hostClient.accountQuery(page);
		setChanged();
	}

	public void regiestAgree(SoAccount account) {
		account.setStatus(AuditResult.AGREE.getStatus());
		hostClient.regiestAudit(account);
		setChanged();
	}

	public void regiestReject(Long id) {
		SoAccount account = new SoAccount();
		account.setId(id);
		account.setStatus(AuditResult.REJECT.getStatus());
		hostClient.regiestAudit(account);
		setChanged();
	}

	public void checkAccountProject(Long id) {
		SoAccount account = new SoAccount();
		account.setId(id);
		hostClient.checkAccountProject(account);
		setChanged();
	}

	public void queryProjects(SoPage<SoProject, List<SoProject>> soPage) {
		SoProject c = soPage.getC();
		if (c == null)
			soPage.setC(new SoProject());
		hostClient.queryProjects(soPage);
		setChanged();
	}

	public void selectProject(Long id) {
		SoProject project = new SoProject();
		project.setId(id);
		hostClient.selectProject(project);
		setChanged();
	}
}

package com.solar.server.commons.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.entity.SoDevices;

public class AppSessionManager {
	private static final Logger logger = LoggerFactory.getLogger(AppSessionManager.class);

	public Map<String, AppSession> sessionMap = new ConcurrentHashMap<String, AppSession>();
	public Map<String, AppSession> devSessionMap = new ConcurrentHashMap<String, AppSession>();

	public Map<String, AppSession> devNoSessionMap = new ConcurrentHashMap<String, AppSession>();

	public static int topOnlineAccountCount = 0;

	private static class Inner {
		private static AppSessionManager appSessionManager = new AppSessionManager();

		public static AppSessionManager getAppSessionManager() {
			return appSessionManager;
		}
	}

	private AppSessionManager() {
	}

	/**
	 *
	 * @return
	 */
	public static AppSessionManager getInstance() {
		return Inner.getAppSessionManager();
	}

	/**
	 * 存放APPSESSION
	 * 
	 * @param appSession
	 * @return
	 */
	public boolean putAppSessionToHashMap(AppSession appSession) {
		String sessionID = appSession.getSessionID();
		boolean result = checkSessionIsHava(sessionID);
		if (!result) {
			sessionMap.put(sessionID, appSession);
			if (sessionMap.size() > topOnlineAccountCount) {
				topOnlineAccountCount = sessionMap.size();
			}
		}
		return !result;
	}

	public void putDevSessionToHashMap(AppSession appSession) {
		String sessionID = appSession.getSessionID();
		logger.info("add device session sessionId=" + sessionID);
		AppSession t = devSessionMap.get(sessionID);
		if (t == null) {
			devSessionMap.put(sessionID, appSession);
			if (devSessionMap.size() > topOnlineAccountCount) {
				topOnlineAccountCount = topOnlineAccountCount + devSessionMap.size();
			}
		}
		SoDevices enti = appSession.getEnti(SoDevices.class);
		if (enti == null) {
			logger.error("device session has no device info object");
		} else {
			String devNo = enti.getDevNo();
			devNoSessionMap.put(devNo, appSession);
		}
	}

	public void rmDevSession(AppSession appSession) {
		logger.error("remove device session sessionId=" + appSession.getSessionID());
		if (appSession.isLogin()) {
			SoDevices enti = appSession.getEnti(SoDevices.class);
			if (enti != null) {
				String devNo = enti.getDevNo();
				logger.error("remove device session devNo=" + devNo);
				devNoSessionMap.remove(devNo);
				devSessionMap.remove(appSession.getSessionID());
			}
		}
	}

	public void rmAppSession(AppSession appSession) {
		sessionMap.remove(appSession.getSessionID());
	}

	public int getVauleSize() {
		return sessionMap.size();
	}

	/**
	 *
	 * @param
	 * @return
	 */
	public AppSession getAvatarByUuid(String uuid) {
		return sessionMap.get(uuid);
	}

	public List<AppSession> getAllSession() {
		List<AppSession> result = null;
		if (getVauleSize() > 0) {
			result = new ArrayList<AppSession>(getVauleSize());
			Collection<AppSession> connection = sessionMap.values();
			Iterator<AppSession> iterator = connection.iterator();
			while (iterator.hasNext()) {
				result.add(iterator.next());
			}
		}
		return result;
	}

	public AppSession getDevsSession(String devNo) throws ExecutionException {
		return devNoSessionMap.get(devNo);
	}

	/**
	 * 检测用户session是否存在
	 * 
	 * @param uuid
	 * @return
	 */
	private boolean checkSessionIsHava(String sessionId) {
		// 可以用来判断是否在线****等功能
		AppSession appSession = sessionMap.get(sessionId);
		if (appSession != null) {
			return true;
		}
		return false;
	}

}

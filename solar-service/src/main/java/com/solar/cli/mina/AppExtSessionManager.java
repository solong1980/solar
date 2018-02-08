package com.solar.cli.mina;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cache.SolarCache;
import com.solar.cli.ServerContext;
import com.solar.cli.redis.SolarJedis;
import com.solar.entity.SoDevices;
import com.solar.server.commons.session.AppSession;

public class AppExtSessionManager {
	private static final Logger logger = LoggerFactory.getLogger(AppExtSessionManager.class);

	public Map<String, AppSession> sessionMap = new ConcurrentHashMap<String, AppSession>();
	public Map<String, AppSession> devSessionMap = new ConcurrentHashMap<String, AppSession>();

	public Map<String, AppSession> devNoSessionMap = new ConcurrentHashMap<String, AppSession>();

	public static int topOnlineAccountCount = 0;

	private static class Inner {
		private static AppExtSessionManager NettySessionManager = new AppExtSessionManager();

		public static AppExtSessionManager getNettySessionManager() {
			return NettySessionManager;
		}
	}

	private ServerContext context;

	public void setContext(ServerContext context) {
		this.context = context;
	}

	// public ServerContext getContext() {
	// return context;
	// }

	private AppExtSessionManager() {

	}

	/**
	 *
	 * @return
	 */
	public static AppExtSessionManager getInstance() {
		return Inner.getNettySessionManager();
	}

	/**
	 * 存放NettySession
	 * 
	 * @param session
	 * @return
	 */
	public boolean putNettySessionToHashMap(AppSession session) {
		String sessionID = session.getSessionID();
		boolean result = checkSessionIsHava(sessionID);
		if (!result) {
			sessionMap.put(sessionID, session);
			if (sessionMap.size() > topOnlineAccountCount) {
				topOnlineAccountCount = sessionMap.size();
			}
		}
		return !result;
	}

	public void putDevSessionToHashMap(AppSession session) {
		String sessionID = session.getSessionID();
		logger.info("add device session sessionId=" + sessionID);
		AppSession t = devSessionMap.get(sessionID);
		if (t == null) {
			devSessionMap.put(sessionID, session);
			if (devSessionMap.size() > topOnlineAccountCount) {
				topOnlineAccountCount = topOnlineAccountCount + devSessionMap.size();
			}
		}
		SoDevices enti = session.getEnti(SoDevices.class);
		if (enti == null) {
			logger.error("device session has no device info object");
		} else {
			String devNo = enti.getDevNo();
			synchronized (devNoSessionMap) {
				devNoSessionMap.put(devNo, session);
				SolarJedis.getInstance().set(devNo, context.getIp() + ":" + context.getDevPort());
			}
		}
	}

	public void rmDevSession(AppSession session) {
		logger.error("remove device session sessionId=" + session.getSessionID());
		devSessionMap.remove(session.getSessionID());
		if (session.isLogin()) {
			SoDevices enti = session.getEnti(SoDevices.class);
			if (enti != null) {
				String devNo = enti.getDevNo();
				synchronized (devNoSessionMap) {
					AppSession devNoSession = devNoSessionMap.get(devNo);
					if (devNoSession == session) {
						logger.error("remove device session devNo=" + devNo);
						devNoSessionMap.remove(devNo);
						SolarJedis.getInstance().del(devNo);
					} else {
						logger.error("device devNo=" + devNo + "'s session has been replaced,no need to be removed");
					}
				}
			}
		}
	}

	public void rmNettySession(AppSession session) {
		sessionMap.remove(session.getSessionID());
		SolarCache.getInstance().removeSessionContext(session.getSessionID());
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
		AppSession NettySession = sessionMap.get(sessionId);
		if (NettySession != null) {
			return true;
		}
		return false;
	}

}

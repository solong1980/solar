package com.solar.cli.netty.session;

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

public class NettyExtSessionManager {
	private static final Logger logger = LoggerFactory.getLogger(NettyExtSessionManager.class);

	public Map<String, NettySession> sessionMap = new ConcurrentHashMap<String, NettySession>();
	public Map<String, NettySession> devSessionMap = new ConcurrentHashMap<String, NettySession>();

	public Map<String, NettySession> devNoSessionMap = new ConcurrentHashMap<String, NettySession>();

	public static int topOnlineAccountCount = 0;

	private static class Inner {
		private static NettyExtSessionManager nettySessionManager = new NettyExtSessionManager();

		public static NettyExtSessionManager getNettySessionManager() {
			return nettySessionManager;
		}
	}

	private ServerContext context;

	public void setContext(ServerContext context) {
		this.context = context;
	}

	// public ServerContext getContext() {
	// return context;
	// }

	private NettyExtSessionManager() {

	}

	/**
	 *
	 * @return
	 */
	public static NettyExtSessionManager getInstance() {
		return Inner.getNettySessionManager();
	}

	/**
	 * 存放NettySession
	 * 
	 * @param NettySession
	 * @return
	 */
	public boolean putAppSessionToHashMap(NettySession NettySession) {
		String sessionID = NettySession.getSessionID();
		boolean result = checkSessionIsHava(sessionID);
		if (!result) {
			sessionMap.put(sessionID, NettySession);
			if (sessionMap.size() > topOnlineAccountCount) {
				topOnlineAccountCount = sessionMap.size();
			}
		}
		return !result;
	}

	public void putDevSessionToHashMap(NettySession session) {
		String sessionID = session.getSessionID();
		logger.info("add device session sessionId=" + sessionID);
		NettySession t = devSessionMap.get(sessionID);
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

	public void rmDevSession(NettySession session) {
		logger.error("remove device session sessionId=" + session.getSessionID());
		devSessionMap.remove(session.getSessionID());
		if (session.isLogin()) {
			SoDevices enti = session.getEnti(SoDevices.class);
			if (enti != null) {
				String devNo = enti.getDevNo();
				synchronized (devNoSessionMap) {
					NettySession devNoSession = devNoSessionMap.get(devNo);
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

	public void rmNettySession(NettySession nettySession) {
		if (nettySession == null)
			return;
		sessionMap.remove(nettySession.getSessionID());
		SolarCache.getInstance().removeSessionContext(nettySession.getSessionID());
	}

	public int getVauleSize() {
		return sessionMap.size();
	}

	/**
	 *
	 * @param
	 * @return
	 */
	public NettySession getAvatarByUuid(String uuid) {
		return sessionMap.get(uuid);
	}

	public List<NettySession> getAllSession() {
		List<NettySession> result = null;
		if (getVauleSize() > 0) {
			result = new ArrayList<NettySession>(getVauleSize());
			Collection<NettySession> connection = sessionMap.values();
			Iterator<NettySession> iterator = connection.iterator();
			while (iterator.hasNext()) {
				result.add(iterator.next());
			}
		}
		return result;
	}

	public NettySession getDevsSession(String devNo) throws ExecutionException {
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
		NettySession NettySession = sessionMap.get(sessionId);
		if (NettySession != null) {
			return true;
		}
		return false;
	}

}

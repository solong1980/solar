package com.solar.server.commons.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.solar.command.message.response.ErrorResponse;
import com.solar.common.context.ErrorCode;
import com.solar.entity.SoAccount;

public class AppSessionManager {

	public Map<String, AppSession> sessionMap = new ConcurrentHashMap<String, AppSession>();
	private Cache<Long, List<AppSession>> custDevSessionCache;

	public static int topOnlineAccountCount = 0;

	private static class Inner {
		private static AppSessionManager appSessionManager = new AppSessionManager();

		public static AppSessionManager getAppSessionManager() {
			return appSessionManager;
		}
	}

	private AppSessionManager() {
		custDevSessionCache = CacheBuilder.newBuilder().initialCapacity(1000).build();
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
	public boolean putGameSessionInHashMap(AppSession appSession, String useId) {

		boolean result = checkSessionIsHava(useId);
		if (result) {
			// System.out.println("这个用户已登录了,更新session");
			try {
				sessionMap.get("UUID_" + useId).sendMsg(new ErrorResponse(ErrorCode.Error_000022));
				Thread.sleep(1000);
				sessionMap.get("UUID_" + useId).close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		sessionMap.put("UUID_" + useId, appSession);
		if (sessionMap.size() > topOnlineAccountCount) {
			topOnlineAccountCount = sessionMap.size();
		}

		return !result;
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

	/**
	 * 从缓存获取客户设备session
	 * 
	 * @param custId
	 * @return
	 * @throws ExecutionException
	 */
	public List<AppSession> getCustDevsSession(Long custId) throws ExecutionException {
		List<AppSession> custDevSessions = custDevSessionCache.get(custId, new Callable<List<AppSession>>() {
			@Override
			public List<AppSession> call() throws Exception {
				List<AppSession> result = null;
				if (getVauleSize() > 0) {
					result = new ArrayList<AppSession>(50);
					Collection<AppSession> connection = sessionMap.values();
					Iterator<AppSession> iterator = connection.iterator();
					while (iterator.hasNext()) {
						AppSession next = iterator.next();
						SoAccount enti = next.getEnti(SoAccount.class);
						if (custId.equals(enti.getCustId()))
							result.add(next);
					}
				}
				return result;
			}
		});
		return custDevSessions;
	}

	/**
	 * 检测用户session是否存在
	 * 
	 * @param uuid
	 * @return
	 */
	private boolean checkSessionIsHava(String uuid) {
		// 可以用来判断是否在线****等功能
		AppSession appSession = sessionMap.get("UUID_" + uuid);
		if (appSession != null) {
			return true;
		}
		return false;
	}

}

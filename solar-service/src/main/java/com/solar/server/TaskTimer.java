package com.solar.server;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 每天固定时间执行任务的定时器
 */
public class TaskTimer {
	static int count = 0;
	AsyncTaskQueue asyncTaskQueue = new AsyncTaskQueue();

	public static void showTimer() {
		/**
		 * 定时迁移上报数据，每周清理一次
		 */
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				++count;
				System.out.println("时间=" + new Date() + " 执行了" + count + "次"); // 1次
			}
		};

		// 设置执行时间
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);// 每天
		// 定制每天的21:09:00执行，
		calendar.set(year, month, day, 1, 0, 0);
		Date date = calendar.getTime();
		Timer timer = new Timer();
		System.out.println(date);
		timer.schedule(task, date, 24 * 60 * 60 * 1000);
	}

	/**
	 * 心跳检测程序，一分钟检测一次
	 */
	public static void headBag() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				
			}
		};
		// 设置执行时间
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);// 每天
		// 定制每天的21:09:00执行，
		calendar.set(year, month, day, 0, 0, 1);
		Date date = calendar.getTime();
		Timer timer = new Timer();
		System.out.println(date);
		// 20秒一次心跳包
		timer.schedule(task, date, 20000);
	}
}

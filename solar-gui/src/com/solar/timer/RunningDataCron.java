package com.solar.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.client.ObservableMedia;

public class RunningDataCron {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunningDataCron.class);

	private String devNo;
	private boolean running = false;

	public RunningDataCron() {
		super();
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getDevNo() {
		return devNo;
	}

	public void setDevNo(String devNo) {
		this.devNo = devNo;
	}

	public void startFetchRunningData() {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r, "RunningDataThread");
				return thread;
			}
		});

		executorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					LOGGER.debug("running data state: run=" + running);
					if (devNo == null)
						return;
					if (running)
						ObservableMedia.getInstance().getRunningData(devNo);
				} catch (Exception e) {
					LOGGER.error("schedule running data error", e);
				}
			}
		}, 1, 60, TimeUnit.SECONDS);
	}
}

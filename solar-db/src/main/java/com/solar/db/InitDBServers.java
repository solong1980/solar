package com.solar.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.db.services.SoAccountService;
import com.solar.db.services.SoCustomerService;
import com.solar.db.services.SoDevicesService;
import com.solar.db.services.SoRunningDataService;
import com.solar.db.services.SoWorkingModeService;

public class InitDBServers {
	private static final Logger logger = LoggerFactory.getLogger(InitDBServers.class);

	public void initServersFun() throws IOException {
		Reader reader = Resources.getResourceAsReader("myBatisConfig.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		
		SoAccountService.getInstance().initSetSession(sqlSessionFactory);
		SoCustomerService.getInstance().initSetSession(sqlSessionFactory);
		SoDevicesService.getInstance().initSetSession(sqlSessionFactory);
		
		SoRunningDataService.getInstance().initSetSession(sqlSessionFactory);
		SoWorkingModeService.getInstance().initSetSession(sqlSessionFactory);
		
		
		
		// 做个查询,使数据库连接池初始化
		Long maxId = SoRunningDataService.getInstance().getMaxId();
		logger.info("current max data id :" + maxId);
	}

	private static InitDBServers initDBServers = new InitDBServers();

	public static InitDBServers getInstance() {
		return initDBServers;
	}

	public static void main(String[] args) {
		try {
			InitDBServers.getInstance().initServersFun();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SoRunningDataService instance = SoRunningDataService.getInstance();
		Long maxId = instance.getMaxId();
		System.out.println(maxId);
	}
}

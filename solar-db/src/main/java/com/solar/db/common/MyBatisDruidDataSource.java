package com.solar.db.common;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;

import com.alibaba.druid.pool.DruidDataSource;

public class MyBatisDruidDataSource implements DataSourceFactory {

	private Properties configure;

	@Override
	public void setProperties(Properties props) {
		this.configure = props;
	}

	@Override
	public DataSource getDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(configure.getProperty("driver"));
		dataSource.setUrl(configure.getProperty("url"));
		dataSource.setUsername(configure.getProperty("username"));
		dataSource.setPassword(configure.getProperty("password"));
		dataSource.setInitialSize(Integer.parseInt(configure.getProperty("initialSize", "5")));
		dataSource.setMinIdle(Integer.parseInt(configure.getProperty("minIdle", "1")));
		dataSource.setMaxActive(Integer.parseInt(configure.getProperty("maxActive", "10")));
		try {
			dataSource.setFilters("stat");// 启用监控统计功能
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// for mysql
		// dataSource.setPoolPreparedStatements(false);
		return dataSource;
	}

}

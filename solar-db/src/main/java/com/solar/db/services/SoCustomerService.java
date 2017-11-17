package com.solar.db.services;

import org.apache.ibatis.session.SqlSessionFactory;

import com.solar.db.dao.SoCustomerMapper;
import com.solar.db.dao.impl.SoCustomerDao;
import com.solar.entity.SoCustomer;

/**
 * @author long liang hua
 */
public class SoCustomerService {
	private static SoCustomerService customerService = new SoCustomerService();
	private SoCustomerMapper customerDao;

	public SoCustomerService() {
		super();
	}

	public static SoCustomerService getInstance() {
		return customerService;
	}

	public void initSetSession(SqlSessionFactory sqlSessionFactory) {
		customerDao = new SoCustomerDao(sqlSessionFactory);
	}

	public SoCustomer selectById(Long id) {
		return customerDao.selectById(id);
	}

}

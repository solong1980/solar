package com.solar.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.solar.entity.SoAccount;
import com.solar.entity.SoPage;
import com.solar.entity.SoPrivilege;

public interface SoAccountMapper {

	SoAccount selectById(Long id);

	SoAccount selectByAccount(String acc);

	void addAccount(SoAccount soAccount);

	SoAccount selectByPhone(@Param("phone") String matchPhone);

	void updateAccount(SoAccount account);

	List<SoAccount> selectBySoAccount(SoAccount account);

	List<SoAccount> queryAccount(@Param("page") SoPage<SoAccount,List<SoAccount>> accountPage);

	void agreeOperatorAccount(SoAccount account, List<SoPrivilege> privileges);

	void updateStatus(SoAccount account);
}
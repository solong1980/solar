package com.solar.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;
import com.solar.entity.SoPage;

public interface SoAccountFindMapper {

	void addAccountFind(SoAccountFind accountFind);

	List<SoAccountFind> queryAccountFind(@Param("page") SoPage<SoAccountFind, List<SoAccountFind>> accountFindPage);

	Integer queryAccountFindCount(@Param("page") SoPage<SoAccountFind, List<SoAccountFind>> accountFindPage);

	SoAccountFind getAccountFindById(Long id);

	void doAuditAgree(SoAccountFind dbAccountFind, SoAccount account);

	void updateStatus(SoAccountFind accountFind);

	void doAuditReject(SoAccountFind accountFind);
}
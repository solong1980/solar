package com.solar.db.dao;

import java.util.List;

import com.solar.entity.SoAccount;
import com.solar.entity.SoAccountFind;

public interface SoAccountFindMapper {

	void addAccountFind(SoAccountFind accountFind);

	List<SoAccountFind> queryAccountFind(SoAccountFind accountFind);

	SoAccountFind getAccountFindById(Long id);

	void doAuditAgree(SoAccountFind dbAccountFind, SoAccount account);

	void updateStatus(SoAccountFind accountFind);

	void doAuditReject(SoAccountFind accountFind);
}
package com.solar.db.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.solar.entity.SoAccount;

public interface SoAccountMapper {

	SoAccount selectById(Long id);

	SoAccount selectByAccount(String acc);

	void addAccount(SoAccount soAccount);

	SoAccount selectByPhone(@Param("phone") String matchPhone);

	void updateAccount(SoAccount account);

	List<SoAccount> selectBySoAccount(SoAccount account);

}
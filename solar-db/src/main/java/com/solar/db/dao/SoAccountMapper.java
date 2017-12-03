package com.solar.db.dao;

import com.solar.entity.SoAccount;

public interface SoAccountMapper {

	SoAccount selectById(Long id);

	SoAccount selectByAccount(String acc);

	void addAccount(SoAccount soAccount);
}
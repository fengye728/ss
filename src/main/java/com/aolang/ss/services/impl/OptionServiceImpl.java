package com.aolang.ss.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.aolang.ss.mappers.OptionModelMapper;
import com.aolang.ss.models.OptionModel;
import com.aolang.ss.services.OptionService;
import com.aolang.ss.utils.CommonUtil;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class OptionServiceImpl implements OptionService {
	
	// private static final int MAX_INSERT_RECORDS_NUM = 1000;
	
	private static String tablePrefix = "option_";
	
	@Autowired
	private OptionModelMapper optionMapper;
	
	/**
	 * Insert a list of oi of one day records into database. Create table if it not exists
	 */
	
	@Override
	public int insert(OptionModel record) {
		if (record == null) {
			return 0;
		}
		String tableName = tablePrefix + CommonUtil.getQuarterByDay(record.getEventDay());
		return optionMapper.insertWithoutConflict(tableName, record);
	}

	@Override
	public void createTable(Integer eventDay) {
		optionMapper.createTable(tablePrefix + CommonUtil.getQuarterByDay(eventDay));
	}
}

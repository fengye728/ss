package com.aolang.ss.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.aolang.ss.models.OptionModel;

public interface OptionModelMapper {

	OptionModel selectByPrimaryKey(@Param("tableName") String tableName, @Param("id") Long id);
	
	int deleteByEventDay(@Param("tableName") String tableName, @Param("eventDay") Integer eventDay);
	
	int insertWithoutConflict(@Param("tableName") String tableName, @Param("item") OptionModel item);
	
	int insertList(@Param("tableName") String tableName, @Param("recordList") List<OptionModel> recordList);
	
	void createTable(@Param("tableName") String tableName);
}
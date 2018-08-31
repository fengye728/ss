package com.aolang.ss.services;

import com.aolang.ss.models.OptionModel;

public interface OptionService {
	
	int insert(OptionModel record);
	
	void createTable(Integer eventDay);
}

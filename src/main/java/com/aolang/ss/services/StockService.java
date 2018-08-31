package com.aolang.ss.services;

import java.util.List;

import com.aolang.ss.models.StockModel;

public interface StockService {
	
	int addStockQuote(StockModel record);
	
	void createTable();
	
	List<Integer> getAllQuoteDate(); 
}

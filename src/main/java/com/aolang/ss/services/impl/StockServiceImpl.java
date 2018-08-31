package com.aolang.ss.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.aolang.ss.mappers.StockModelMapper;
import com.aolang.ss.models.StockModel;
import com.aolang.ss.services.StockService;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class StockServiceImpl implements StockService {

	@Autowired
	private StockModelMapper stockQuoteModelMapper;

	@Override
	public int addStockQuote(StockModel record) {
		if(null == record) {
			return 0;
		}
		return stockQuoteModelMapper.insertWithoutConflict(record);
	}

	@Override
	public void createTable() {
		stockQuoteModelMapper.createTable();
	}

	@Override
	public List<Integer> getAllQuoteDate() {
		return stockQuoteModelMapper.selectQuoteDate();
	}
}

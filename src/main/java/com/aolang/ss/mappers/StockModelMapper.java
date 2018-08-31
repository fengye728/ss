package com.aolang.ss.mappers;

import java.util.List;

import com.aolang.ss.models.StockModel;

public interface StockModelMapper {

    int insertWithoutConflict(StockModel record);
    
    void createTable();
    
    List<Integer> selectQuoteDate();
}
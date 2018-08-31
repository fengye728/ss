/**
 * 
 */
/**
 * @author Maple.S
 *
 */
package com.aolang.ss.constants;

public class CommonConstants {
	public static int FILE_BUFFER_SIZE = 131072;			//	128KB
	
	public static final Character OPTION_TRADE_OPTION_TYPE_CALL = 'C';
	public static final Character OPTION_TRADE_OPTION_TYPE_PUT = 'P';
	
	public static final String DAY_DATE_FORMAT = "yyMMdd";
	
	public static final String VALID_STOCK_SYMBOL_REG = "[a-zA-Z]+";
	
	// -------------------- Stricknet data constants ----------------------
	public static final String STRICKNET_REAL_TIME_FILE_NAME = "data.zip";
	
	public static final String STRICKNET_HISTORY_FILE_NAME_REG = "data_(\\d+).zip";	// date format: yyyyMMdd
	
}
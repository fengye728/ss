package com.aolang.ss.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.aolang.ss.constants.CommonConstants;

public class CommonUtil {
	
	/**
	 * Get stock symbol, option expiration date, option type and option strike price from input symbol and set these into result.
	 * 
	 * @param symbol
	 * @return [stock_symbol, call_put, expiration, strike]
	 * @throws Exception
	 */
	public static String[] parseOptionSymbol(String symbol) throws Exception{
		
		int stockSymbolBeginIndex = 0;
		int stockSymbolEndIndex = 0;
		int symbolLength = symbol.length();
		int index = 0;
		
		String[] result = new String[4]; // [stock_symbol, call_put, expiration, strike]
		
		// get stock symbol
		while((++index) < symbolLength && !Character.isDigit(symbol.charAt(index)));
		stockSymbolEndIndex = index;
		
		result[0] = symbol.substring(stockSymbolBeginIndex, stockSymbolEndIndex);
		
		// get option type and its index
		while((++index) < symbolLength) {
			char ch = symbol.charAt(index);
			if(ch == CommonConstants.OPTION_TRADE_OPTION_TYPE_CALL
					|| ch == CommonConstants.OPTION_TRADE_OPTION_TYPE_PUT) {
				
				result[1] = "" + ch;
				break;
			}
		}
		
		// get option expiration date
		result[2] = symbol.substring(stockSymbolEndIndex, index);
		
		// get option strike price
		result[3] = String.valueOf(Integer.valueOf(symbol.substring(index + 1, symbolLength)) / 1000.0);
		
		return result;
	}
	
	public static Integer stockDate2OptionDate(Integer date) {
		return date % 1000000;
	}
	/**
	 * Convert int date to date date.
	 * @param nDate
	 * @return
	 */
	public static Date nDate2dDate(Integer nDate) {
		SimpleDateFormat DAY_DATE_FORMATTER = new SimpleDateFormat(CommonConstants.DAY_DATE_FORMAT);
		try {
			return DAY_DATE_FORMATTER.parse("" + nDate);
		} catch (ParseException e) {
			return null;
		}
		
	}
	
	/**
	 * Convert date date to int date
	 * @param dDate
	 * @return
	 */
	public static Integer dDate2nDate(Date dDate) {
		SimpleDateFormat DAY_DATE_FORMATTER = new SimpleDateFormat(CommonConstants.DAY_DATE_FORMAT);
		return Integer.valueOf(DAY_DATE_FORMATTER.format(dDate));
	}
	
	/**
	 * Change date with offset.
	 * 
	 * @param nDate integer date (yyMMdd)
	 * @param offset
	 * @return resulted integer date (yyMMdd)
	 */
	public static Integer changeNDate(Integer nDate, int offset) {
		Date eventDay = CommonUtil.nDate2dDate(nDate);
		
		Calendar date = Calendar.getInstance();
		date.setTime(eventDay);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + offset);
		
		return CommonUtil.dDate2nDate(date.getTime());
	}

	/**
	 * Change date with offset.
	 * 
	 * @param dDate date whose type is date
	 * @param offset
	 * @return resulted date whose type is date
	 */
	public static Date changeDDate(Date dDate, int offset) {
		
		Calendar date = Calendar.getInstance();
		date.setTime(dDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + offset);
		
		return date.getTime();
	}
	
	/**
	 * Get the quarter(yyn) this record belong to.
	 * 
	 * @return
	 */
	public static String getQuarterByDay(Integer day) {
		int year = day / 10000;
		int quarterInYear = ((day % 10000) / 100 - 1) / 3 + 1;
		
		return String.valueOf(year) + String.valueOf(quarterInYear);
	}
	
	/**
	 * Guarantee the nDate is the format of yyyyMMdd.
	 * @param nDate
	 * @return
	 */
	public static Integer safeNDate(Integer nDate) {
		return nDate % 1000000;
	}
	
	public static int getDigit(char ch){
		return ch - '0';
	}
	
}

package com.aolang.ss.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aolang.ss.constants.CommonConstants;
import com.aolang.ss.models.OptionModel;
import com.aolang.ss.models.StockModel;
import com.aolang.ss.services.OptionService;
import com.aolang.ss.services.StockService;

@Component
public class StricknetUtil {
	
	private static Logger logger = Logger.getLogger(StricknetUtil.class);
	
	private static final String FILENAME_OPTION_PREFIX = "Options";
	private static final String FILENAME_STOCK_PREFIX = "Equities";
	
	private static final String DATE_INFILE_REG = "m(\\d+)d(\\d+)y(\\d+)";
	
	private static final double STOCK_PRICE_LOW_THRESHOLD = 0.01;
	
	@Autowired
	OptionService optionService;
	
	@Autowired
	StockService stockService;
	
	public void zipDataProcess(String zipFilename) {
		ZipFile zf = null;
		try {
			zf = new ZipFile(zipFilename);

			// get option entry
			ZipEntry optionEntry = getZipEntry(zf, FILENAME_OPTION_PREFIX);
			Integer eventDay = parseFileDate(optionEntry.getName());
			if(optionEntry != null) {
				BufferedReader optionBr = new BufferedReader(new InputStreamReader(zf.getInputStream(optionEntry)));
				processOption(optionBr, eventDay);
			}
			
			// get stock entry
			ZipEntry stockEntry = getZipEntry(zf, FILENAME_STOCK_PREFIX);
			eventDay = parseFileDate(stockEntry.getName());
			if(optionEntry != null) {
				BufferedReader stockBr = new BufferedReader(new InputStreamReader(zf.getInputStream(stockEntry)));
				processStock(stockBr, eventDay);
			}
						
			
		} catch (IOException e) {

		} finally {
			try {
				if(zf != null)
					zf.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}			
		}
		
	}
	
	/**
	 * Insert all option records to database from data of BufferedReader.
	 * @param br
	 * @param eventDay
	 * @return true if success, otherwise false.
	 */
	private boolean processOption(BufferedReader br, Integer eventDay) {
		try {
			// create table for event day
			optionService.createTable(eventDay);

			// insert option records
			String line = null;
			int count = 0;
			while( (line = br.readLine()) != null) {
				OptionModel model = parseOptionRecord(line, eventDay);
				count += optionService.insert(model);
				
			}
			logger.info("Process Option records of " + eventDay + ":" + count);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Insert all option records to database from data of BufferedReader.
	 * @param br
	 * @param eventDay
	 * @return true if success, otherwise false.
	 */
	private boolean processStock(BufferedReader br, Integer eventDay) {
		try {
			// create table for event day
			stockService.createTable();

			// insert option records
			String line = null;
			int count = 0;
			while( (line = br.readLine()) != null) {
				StockModel model = parseStockRecord(line, eventDay);
				count += stockService.addStockQuote(model);
				
			}
			logger.info("Process Stock records of " + eventDay + ":" + count);			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Parse a stock record from a line text of Sticknet equities file.
	 * @param record
	 * @param eventDay
	 * @return
	 */
	private StockModel parseStockRecord(String record, Integer eventDay) {
		try {
			String[] fields = record.split(",");
			StockModel result = new StockModel();
			
			result.setQuoteDate(eventDay);
			result.setSymbol(fields[0]);
			result.setCompanyName(fields[1]);
			result.setOpen(Double.valueOf(fields[3]));
			result.setHigh(Double.valueOf(fields[4]));
			result.setLow(Double.valueOf(fields[5]));
			result.setClose(Double.valueOf(fields[6]));
			result.setVolume(Long.valueOf(fields[7]));
			
			// filter quote
			if(!result.getSymbol().matches(CommonConstants.VALID_STOCK_SYMBOL_REG)) {
				// invalid symbol
				return null;
			} else if (result.getVolume() == 0 || result.getClose() <= STOCK_PRICE_LOW_THRESHOLD || result.getOpen() <= STOCK_PRICE_LOW_THRESHOLD) {
				// symbol not exist or stop trade
				return null;
			}
			
			return result;
		} catch(Exception e) {
			logger.error("Parse stock record failed:" + record);
			return null;
		}
	}
	/**
	 * Parse a option record from a line text of Sticknet options file.
	 * @param record
	 * @param eventDay
	 * @return A option record if success, otherwise null.
	 */
	private OptionModel parseOptionRecord(String record, Integer eventDay) {
		
		try {
			String[] fields = record.split(",");
			
			OptionModel result = new OptionModel();
			
			String ask = fields[fields.length - 3];
			String bid = fields[fields.length - 4];
			String lastSale = fields[fields.length - 7];
			Double price = null;
			if(lastSale.equals("")) {
				if(!ask.equals("")) {
					price = Double.valueOf(ask);
				}
				if(!bid.equals("")) {
					if(price == null) {
						price = Double.valueOf(bid);
					} else {
						price = (Double.valueOf(bid) + price) / 2;
					}
				}
			} else {
				price = Double.valueOf(lastSale);
			}
			
			String[] optionFields = CommonUtil.parseOptionSymbol(fields[0].replaceAll("\\s", ""));
			result.setEventDay(eventDay);
			result.setStockSymbol(optionFields[0]);
			result.setCallPut(optionFields[1].charAt(0));
			result.setExpiration(Integer.valueOf(optionFields[2]));
			result.setStrike(Double.valueOf(optionFields[3]));
			
			result.setPrice(price);
			result.setVolume(Integer.valueOf(fields[fields.length - 6]));
			result.setOpenInterest(Integer.valueOf(fields[fields.length - 2]));
			
			return result;
		} catch (Exception e) {
			logger.error("Parse option record failed:" + record);
			return null;
		}
		
	}
	
	/**
	 * Get zip entry with specified prefix from zip file.
	 * @param zf
	 * @param entryPrefix
	 * @return
	 */
	private ZipEntry getZipEntry(ZipFile zf, String entryPrefix) {
		Enumeration<? extends ZipEntry> files = zf.entries();
		
		while(files.hasMoreElements()) {
			ZipEntry file = files.nextElement();
			if(file.getName().startsWith(entryPrefix, 0)) {
				
				return file;
			}
		}
		return null;
	}
	
	/**
	 * Parse int date from filename.
	 * @param filename
	 * @return yyMMdd
	 */
	private Integer parseFileDate(String filename) {
		try {
			Pattern pat = Pattern.compile(DATE_INFILE_REG);
			Matcher mat = pat.matcher(filename);
			if(mat.find() && mat.groupCount() == 3) {
				int mm = Integer.parseInt(mat.group(1));
				int dd = Integer.parseInt(mat.group(2));
				int yy = Integer.parseInt(mat.group(3));
				return yy * 10000 + mm * 100 + dd;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.error("Parse file date fail!");
		return null;
	}
}


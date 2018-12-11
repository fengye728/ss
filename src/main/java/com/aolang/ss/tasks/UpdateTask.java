/**
 * 
 */
/**
 * @author Maple.S
 *
 */
package com.aolang.ss.tasks;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aolang.ss.config.ConfigProperties;
import com.aolang.ss.constants.CommonConstants;
import com.aolang.ss.services.StockService;
import com.aolang.ss.utils.CommonUtil;
import com.aolang.ss.utils.FTPUtil;
import com.aolang.ss.utils.StricknetUtil;

@Component
public class UpdateTask {
	
	private static Logger logger = Logger.getLogger(UpdateTask.class);
	
	@Autowired
	ConfigProperties configProperties;
	
	@Autowired
	StricknetUtil stricknetUtil;
	
	@Autowired
	StockService stockService;
	
	@Scheduled(cron = "${schedule.cron.today}", zone = "${schedule.timezone}")
	public void updateDaily() {
		updateToday();
		updateHis();
	}
	
	/**
	 * Update today date.
	 */
	public void updateToday() {
		FTPUtil ftpUtil = new FTPUtil(configProperties);
		// download real time file
		String localFilename = configProperties.getDataPath() + File.separator + CommonConstants.STRICKNET_REAL_TIME_FILE_NAME;
		if(ftpUtil.download(CommonConstants.STRICKNET_REAL_TIME_FILE_NAME, localFilename)) {
			// download success, then process
			stricknetUtil.zipDataProcess(localFilename);
		} else {
			logger.error("Download real time date fail, process fail!");
		}
		
	}
	
	/**
	 * Update history data that not existed in system. 
	 * Hypothesis: option and stock data update together.
	 */
	public void updateHis() {
		FTPUtil ftpUtil = new FTPUtil(configProperties);
		
		// update history data
		List<Integer> quoteDateList = stockService.getAllQuoteDate();
		List<String> ftpFilenames = ftpUtil.listFiles();
		for(String filename : ftpFilenames) {
			Integer date = parseDateFromFilename(filename);
			if(date == null) {
				continue;
			}
			
			if(!quoteDateList.contains(CommonUtil.stockDate2OptionDate(date))) {
				// download success, then process
				String localFilename = configProperties.getDataPath() + File.separator + filename;
				if(ftpUtil.download(filename, localFilename)) {
					// download success, then process
					stricknetUtil.zipDataProcess(localFilename);
				} else {
					logger.error("Download file " + filename + " fail, process fail!");
				}				
			}
		}
		
	}
	
	private Integer parseDateFromFilename(String filename) {
		Pattern pat = Pattern.compile(CommonConstants.STRICKNET_HISTORY_FILE_NAME_REG);
		Matcher mat = pat.matcher(filename);
		if(mat.find() && mat.groupCount() == 1) {
			return Integer.valueOf(mat.group(1));
		} else {
			return null;
		}
	}
}
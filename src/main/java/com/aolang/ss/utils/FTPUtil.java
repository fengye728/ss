/**
 * 
 */
/**
 * @author Maple.S
 *
 */
package com.aolang.ss.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.aolang.ss.config.ConfigProperties;
import com.aolang.ss.constants.CommonConstants;


public class FTPUtil {
	
	private static Logger logger = Logger.getLogger(FTPUtil.class);
	
	private FTPClient ftp = new FTPClient();
	
	private ConfigProperties config;
	
	public FTPUtil(ConfigProperties config) {
		this.config = config;
	}
	
	public boolean download(String ftpFilename, String localFilename) {
		if(!this.connect()) {
			return false;
		}
		FileOutputStream localFos = null;
		try {
			localFos = new FileOutputStream(localFilename);
			ftp.retrieveFile(ftpFilename, localFos);
			logger.info("Download " + ftpFilename + " completed in " + localFilename);
			return true;
		} catch (Exception e) {
			logger.error("Download " + ftpFilename + " failed:" + e.getMessage());
			return false;
		} finally {
			if(localFos != null) {
				try {
					localFos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			this.close();
		}
	}
	
	public List<String> listFiles() {
		if(!this.connect()) {
			return null;
		}
		try {
			FTPFile[] files = ftp.listFiles();
			List<String> result = new ArrayList<>();
			for(int i = 0; i < files.length; i++) {
				result.add(files[i].getName());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			this.close();
		}
	}
	
	/**
	 * Connect FTP server. 
	 * @return true if success, otherwise false.
	 */
	private boolean connect() {
		try {
			// login 
			ftp.connect(config.getFtpAddress(), Integer.parseInt(config.getFtpPort()));
			if(!ftp.login(config.getFtpUsername(), config.getFtpPassword())) {
				logger.error("FTP login fail!");
				return false;
			}
			
			int replyCode = ftp.getReplyCode();
			if( !FTPReply.isPositiveCompletion(replyCode) ) {
				logger.error("FTP connect fail:" + replyCode);
				return false;
			}
			// ftp setting
			ftp.setBufferSize(CommonConstants.FILE_BUFFER_SIZE);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			ftp.setControlKeepAliveTimeout(30 * 60);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Close ftp client.
	 */
	private void close() {
		if(ftp != null && ftp.isConnected()) {
			try {
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
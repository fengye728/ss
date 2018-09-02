/**
 * 
 */
/**
 * @author Maple.S
 *
 */
package com.aolang.ss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ss", ignoreInvalidFields = true)
public class ConfigProperties {
	
	// ------------------ Ftp Config -----------------------------
	private String ftpAddress = "eodstricknet.deltaneutral.com";
	
	private String ftpPort = "21";
	
	private String ftpUsername = "jerry.liu";
	
	private String ftpPassword = "newark";
	
	private String dataPath = "data";
	
	// -----------------------------------------------------------

	public String getFtpAddress() {
		return ftpAddress;
	}

	public void setFtpAddress(String ftpAddress) {
		this.ftpAddress = ftpAddress;
	}

	public String getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}
}
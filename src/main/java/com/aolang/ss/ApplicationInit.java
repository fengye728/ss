package com.aolang.ss;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.aolang.ss.config.ConfigProperties;
import com.aolang.ss.tasks.UpdateTask;

@Component
public class ApplicationInit implements ApplicationRunner{
	
	private static Logger logger = Logger.getLogger(ApplicationInit.class);

	@Autowired
	ConfigProperties configProperties;
	
	@Autowired
	UpdateTask updateTask;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// create directory
		File file = new File(configProperties.getDataPath());
		if(!file.exists()) {
			file.mkdirs();
		}
		
		// update his and update new data
		logger.info("Update real time data...");
		updateTask.updateToday();

		logger.info("Update history data...");
		updateTask.updateHis();
		
		logger.info("Startup scheduled tasks...");
	}

}

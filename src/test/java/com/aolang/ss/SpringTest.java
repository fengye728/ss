/**
 * 
 */
/**
 * @author Maple.S
 *
 */
package com.aolang.ss;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.aolang.ss.tasks.UpdateTask;
import com.aolang.ss.utils.FTPUtil;
import com.aolang.ss.utils.StricknetUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTest {

	@Autowired
	StricknetUtil stricknetUtil;
	
	@Autowired
	UpdateTask task;
	
	@Test
	public void test() {
		task.updateHis();
	}
}
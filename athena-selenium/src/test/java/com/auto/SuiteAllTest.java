package com.auto;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.auto.testcases.PPToMDToMD.PPtoMDtoMD;
import com.auto.testcases.PPtoRMtoRD.PPtoRMtoRD;
import com.auto.testcases.R1toR1.R1toR1;

/**
 * @author yq
 */

@RunWith(Suite.class)//创建一个测试包
//增加想要的测试类
@SuiteClasses({PPtoRMtoRD.class }) 

public class SuiteAllTest {
}

   


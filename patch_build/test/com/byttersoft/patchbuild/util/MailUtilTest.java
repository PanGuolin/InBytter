package com.byttersoft.patchbuild.util;

import junit.framework.TestCase;

import com.byttersoft.patchbuild.utils.MailInfo;
import com.byttersoft.patchbuild.utils.MailUtil;

/**
 * Test for MailUtil
 * @author pangl
 *
 */
public class MailUtilTest extends TestCase{
	
	public void test_sendMail() throws Exception {
		MailInfo info = new MailInfo();
		info.setContent("<html><body><h1>test</h1></body></html>");
		info.setToAddrs("pangl@bytter.com");
		info.setSubject("测试邮件3");
		MailUtil.sendMail(info);
		assertTrue(true);
	}

}

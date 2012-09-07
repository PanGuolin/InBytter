package com.byttersoft.patchbuild;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

import com.byttersoft.patchbuild.beans.BuildConfig;

import junit.framework.TestCase;

/**
 * Test for PatchConfig
 * @see BuildConfig
 * @author pangl
 *
 */
public class PatchConfigTest extends TestCase{
	
	public void test_readFromFile() throws Exception {
		InputStream in = PatchConfigTest.class.getResourceAsStream("test_patch1.xml");
		BuildConfig config = BuildConfig.readFromStream(in, "test_patch1");
		Properties prop1 = config.toProperties("_");
		
		in = PatchConfigTest.class.getResourceAsStream("test_patch2.xml");
		config = BuildConfig.readFromStream(in, "test_patch2");
		Properties prop2 = config.toProperties("_");
		
		assertEquals(prop1.get("_allFiles"), prop2.get("_allFiles"));
	
		System.out.println(prop1.toString());
		String expected = "{_developers=pangl@bytter.com;developer@bytter.com," +
				" _allFiles=admin/src/main/java/com/byttersoft/admin/action/BisAccTypeAction.java;" +
				"admin-api/src/main/java/com/byttersoft/approve/dao/ApproveDao.java;" +
				"admin/src/main/java/com/byttersoft/admin/form/AccountAuthorForm.java;" +
				"admin/src/main/webapp/admin/accountAuth.jsp;" +
				"admin/src/main/webapp/admin/addBank.jsp;" +
				"admin/src/main/webapp/WEB-INF/classes/spring/spring_adm.xml;" +
				"admin/src/main/resources/config/quartz.properties," +
				" _modules=admin-api;admin," +
				" _webFiles=admin/src/main/webapp/admin/accountAuth.jsp;" +
				"admin/src/main/webapp/admin/addBank.jsp;" +
				"admin/src/main/webapp/WEB-INF/classes/spring/spring_adm.xml," +
				" _version=sp1," +
				" _resourceFiles=admin/src/main/resources/config/quartz.properties," +
				" _customer=all," +
				" _csFiles=," +
				" _id=test_patch1," +
				" _testers=pangl@bytter.com;tester@bytter.com," +
				" _vps=VP0003," +
				" _javaFiles=admin/src/main/java/com/byttersoft/admin/action/BisAccTypeAction.java;" +
				"admin-api/src/main/java/com/byttersoft/approve/dao/ApproveDao.java;" +
				"admin/src/main/java/com/byttersoft/admin/form/AccountAuthorForm.java}";
		
		assertEquals(expected, prop1.toString());
	}
	
	public void test_readFromFile2() throws Exception {
		
		InputStream in = PatchConfigTest.class.getResourceAsStream("test_old.xml");
		BuildConfig config = BuildConfig.readFromStream(in, "test_patch1");
		Properties prop1 = config.toProperties("_");
		assertTrue(true);
	}
	
	public void test_toXML() throws Exception {
		InputStream in = PatchConfigTest.class.getResourceAsStream("test_old.xml");
		BuildConfig config = BuildConfig.readFromStream(in, "test_patch1");
		String xml = config.toXML();
		ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());
		BuildConfig config2 = BuildConfig.readFromStream(input, "test_patch1");
		assertEquals(config.toString(), config2.toString());
	}
		
}

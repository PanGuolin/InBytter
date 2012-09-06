package com.byttersoft.jdbc.translater.replace;

import junit.framework.TestCase;
/**
 * 测试各种替换效果
 * @author pangl
 *
 */
public class ReplaceTest extends TestCase {

	/**
	 * 测试  {@link SKeywordsRep}
	 * @throws Exception
	 */
	public void test_SKeywordsRep() throws Exception {
		SKeywordsRep rep = new SKeywordsRep(new String[]{"from", "dual"}, "");
		
		StringBuilder sql = new StringBuilder("select 1 from dual");
		rep.replace(sql);
		assertEquals("select 1 ", sql.toString());
		
		sql = new StringBuilder("select 1 from dual_table");
		rep.replace(sql);
		assertEquals("select 1 from dual_table", sql.toString());
	}
	
	/**
	 * test {@link OperatorRep}
	 * @throws Exceptin
	 */
	public void test_OperatorRep() throws Exception {
		OperatorRep rep = new OperatorRep("||", "+");
		StringBuilder sql = new StringBuilder("select 'it is a test ' || 'for || of oracle' from dual");
		rep.replace(sql);
		
		String expected = "select 'it is a test ' + 'for || of oracle' from dual";
		assertEquals(expected, sql.toString());
	}
	
	/**
	 * test {@link KeywordRep}
	 * @throws Exception
	 */
	public void test_KeywordRep() throws Exception {
		KeywordRep rep = new KeywordRep("sysdate", "getDate()");
		StringBuilder sql = new StringBuilder("select sysdate sysdate1 from dual");
		rep.replace(sql);
		
		String expected = "select GETDATE() sysdate1 from dual";
		assertEquals(expected, sql.toString());
	}
	
	/**
	 * test {@link FunctionRep}
	 * @throws Exception
	 */
	public void test_FunctionRep() throws Exception {
		FunctionRep rep = new FunctionRep("TO_CHAR", 2, new String[]{"2='dd mon yyyy'"},
				"CONVERT(char, ?1, 106)");
		StringBuilder sql = new StringBuilder("select TO_CHAR(sysdate, 'dd mon yyyy'), " +
				"TO_CHAR(sysdate, 'mm/dd/yyyy') from dual");
		rep.replace(sql);
		String expected = "select CONVERT(char, sysdate, 106), TO_CHAR(sysdate, 'mm/dd/yyyy') from dual";
		assertEquals(expected, sql.toString());
		
	}
}

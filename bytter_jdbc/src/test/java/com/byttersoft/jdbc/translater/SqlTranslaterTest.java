package com.byttersoft.jdbc.translater;

import junit.framework.TestCase;

import com.byttersoft.jdbc.translater.replace.FunctionRep;
import com.byttersoft.jdbc.translater.replace.KeywordRep;
import com.byttersoft.jdbc.translater.replace.OperatorRep;
import com.byttersoft.jdbc.translater.replace.SKeywordsRep;

/**
 * test for {@link SqlTranslater}
 * @author pangl
 *
 */
public class SqlTranslaterTest extends TestCase {
	
	/**
	 * test for {@link SqlTranslater#createFunctionRep(String)}
	 */
	public void test_createFunctionRep() {
		String exprStr = "TO_CHAR(?1, 'dd mon yyyy') => CONVERT(char, ?1, 106)";
		FunctionRep expected = new FunctionRep("TO_CHAR", 2, 
				new String[]{"2='dd mon yyyy'"}, "CONVERT(char, ?1, 106)");
		FunctionRep actual = SqlTranslater.createFunctionRep(exprStr);
		assertEquals(expected, actual);
	}
	
	/**
	 * test for {@link SqlTranslater#createOperatorRep(String)}
	 */
	public void test_createOperatorRep() {
		String exprStr = "|| => +";
		OperatorRep expected = new OperatorRep("||", "+");
		OperatorRep actual = SqlTranslater.createOperatorRep(exprStr);
		assertEquals(expected, actual);
	}
	
	/**
	 * test for {@link SqlTranslater#createKeywordRep(String)}
	 */
	public void test_createKeywordRep() {
		String exprStr = "sysdate => getdate()";
		KeywordRep expected = new KeywordRep("sysdate", "getdate()");
		KeywordRep actual = SqlTranslater.createKeywordRep(exprStr);
		assertEquals(expected, actual);
	}
	
	/**
	 * test for {@link SqlTranslater#createSKeywordsRep(String)}
	 */
	public void test_createSKeywordsRep() {
		String exprStr = "from dual =>";
		SKeywordsRep expected = new SKeywordsRep(new String[]{"from", "dual"}, "");
		SKeywordsRep actual = SqlTranslater.createSKeywordsRep(exprStr);
		assertEquals(expected, actual);
		
		exprStr = "from    dual =>  ";
		actual = SqlTranslater.createSKeywordsRep(exprStr);
		assertEquals(expected, actual);
	}
	
}

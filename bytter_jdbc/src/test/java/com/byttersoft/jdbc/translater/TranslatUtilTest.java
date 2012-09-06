package com.byttersoft.jdbc.translater;

import com.byttersoft.jdbc.translater.expr.FunctionExpr;

import junit.framework.TestCase;

/**
 * Test for {@link SqlUtil}
 * @author pangl
 *
 */
public class TranslatUtilTest extends TestCase{

	public void test_isInString() {
		String sql = "select 'michael pan' from t_users where user_id = '0029'";
		int index = sql.indexOf("pan");
		assertTrue(SqlUtil.isInString(sql, index));
		
		index = sql.indexOf("user_id");
		assertFalse(SqlUtil.isInString(sql, index));
	}
	
	public void test_findKeyword() {
		String sql = "select NVL(a.paysysbankcode,0) from tale1 a";
		int index = SqlUtil.findKeyword(sql, "NVL", 0);
		assertEquals(7, index);
		
		index = SqlUtil.findKeyword(sql, "NvL", 0);
		assertEquals(7, index);
		
		index = SqlUtil.findKeyword(sql, "pays", 0);
		assertEquals(-1, index);
	}
	
	
	public void test_findFunction() {
		
		String sql = "select NvL(a.paysysbankcode,0) from tale1 a";
		FunctionExpr expr = SqlUtil.findFunction(sql, "NVL", 1, 0);
		assertEquals(null, expr);
		
		expr = SqlUtil.findFunction(sql, "NVL", 2, 0);
		assertTrue(expr != null);
		assertEquals("NVL", expr.getFuntionName());
		assertEquals(2, expr.getParameterSize());
	}
	
}

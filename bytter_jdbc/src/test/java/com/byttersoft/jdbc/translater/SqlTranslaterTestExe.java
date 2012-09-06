package com.byttersoft.jdbc.translater;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import com.byttersoft.jdbc.BytterDriver;
import com.byttersoft.jdbc.DBType;

/**
 * Test case for {@link SqlTranslater}
 * @author MickeyMic
 *
 */
public class SqlTranslaterTestExe extends TestCase{
	
	/**
	 * ≤‚ ‘¡¨Ω”∑˚∫≈
	 */
	public void test_operator_concat() throws Exception{
		
		String sql = "select 'this is a test for ' || '|| in database' from dual";
		String expected_ms = "select 'this is a test for ' + '|| in database'";
		assertEquals(expected_ms, SqlTranslater.translate(sql, DBType.sqlserver));
		
		testObjEquals(sql);
	}
	
	/**
	 * 
	 * ≤‚ ‘ NVL º∞ SUBSTR∫Ø ˝
	 */
	public void test_NVL_SUBSTR() {
		String sql = "select NvL(substr(a.paysysbankcode,4,4),0) from tale1 a";
		String expected = "select ISNULL(SUBSTRING(a.paysysbankcode, 4, 4), 0) from tale1 a";
		
		String actual = SqlTranslater.translate(sql, DBType.sqlserver);
		assertEquals(expected, actual);
	}
	
	/**
	 * ≤‚ ‘ sysdate
	 */
	public void test_sysdate() throws Exception{
		String sql = "select sysdate from dual";
		String expected = "select GETDATE()";
		
		String actual = SqlTranslater.translate(sql, DBType.sqlserver);
		assertEquals(expected, actual);
		
		testDateEquals(sql);
	}
	
	/**
	 * ≤‚ ‘to_number◊™ªª
	 */
	public void test_to_number() throws Exception{
		String sql = "select TO_NUMBER('100') from dual";
		String expected_ms = "select CONVERT(numeric, '100')";
		
		String actual = SqlTranslater.translate(sql, DBType.sqlserver);
		assertEquals(expected_ms, actual);
		
		testObjEquals(sql);
	}
	
	/**
	 * ≤‚ ‘TO_DATE(?1, 'yyyy.MM.dd')
	 * @throws Exception
	 */
	public void test_TO_DATE() throws Exception {
		String sql = "select to_date('2012-08-14', 'yyyy.MM.dd') from dual";
		String expected_ms = "select CONVERT(datetime, '2012-08-14', 102)";
		
		assertEquals(expected_ms, SqlTranslater.translate(sql, DBType.sqlserver));
		testDateEquals(sql);
	}
	
	
	private Object getMSSqlResult(String sql) throws Exception{
		
		Class.forName(BytterDriver.class.getName());
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
				"jdbc:byttersoft:sqlserver:jdbc:sqlserver://192.168.0.36:1433;databaseName=bt_jszx", 
				"sa", "sasa");
		} catch (Exception ex) {
			if (ex.getMessage().indexOf("connect timed out") != -1)
				return null;
			throw ex;
		}
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		try {
			while(rs.next()) {
				return rs.getObject(1);
			}
		} finally {
			conn.close();
		}
		throw new Exception("no result");
	}
	
	private Object getOraSqlResult(String sql) throws Exception{
		
		Class.forName(BytterDriver.class.getName());
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:byttersoft:oracle:jdbc:oracle:thin:@192.168.0.36:1522:orcl11gr2", 
					"btv10_cp", "btv10_cp");
		} catch (Exception ex) {
			if (ex.getMessage().indexOf("The Network Adapter could not establish the connection") != -1)
				return null;
			throw ex;
		}
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		try {
			while(rs.next()) {
				return rs.getObject(1);
			}
		} finally {
			conn.close();
		}
		throw new Exception("no result");
	}
	
	private void testDateEquals(String sql) throws Exception{
		Timestamp msResult = (Timestamp)getMSSqlResult(sql);
		if (msResult == null)
			return;
		Date oraResult = (Date)getOraSqlResult(sql);
		if (oraResult == null)
			return;
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		String ora = formater.format(oraResult);
		String mss = formater.format(msResult);
		assertEquals(ora, mss);
	}
	
	private void testObjEquals(String sql) throws Exception{
		Object msResult = getMSSqlResult(sql);
		if (msResult == null)
			return;
		Object oraResult = getOraSqlResult(sql);
		if (oraResult == null)
			return;
		assertEquals(msResult, oraResult);
	}
}

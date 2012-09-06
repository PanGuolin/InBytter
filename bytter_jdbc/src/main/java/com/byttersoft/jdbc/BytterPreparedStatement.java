package com.byttersoft.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Bytter JDBC PreparedStatment й╣ож
 * @author pangl
 *
 */
public class BytterPreparedStatement extends BytterStatement implements PreparedStatement {
	private HashMap<Integer, Object> paramValues = new HashMap<Integer, Object>();
	private Map<Integer, String> paramTypes = new HashMap<Integer, String>();
	
	BytterPreparedStatement(BytterConnection conn, PreparedStatement stat, DBType type) {
		super(conn, stat, type);
	}

	public ResultSet executeQuery() throws SQLException {
		return ((PreparedStatement)stat).executeQuery();
	}

	public int executeUpdate() throws SQLException {
		return ((PreparedStatement)stat).executeUpdate();
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		((PreparedStatement)stat).setNull(parameterIndex, sqlType);
		paramValues.put(parameterIndex, null);
		paramTypes.put(parameterIndex, "sqlType:" + sqlType);
		
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		((PreparedStatement)stat).setBoolean(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "boolean");
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		((PreparedStatement)stat).setByte(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "byte");
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		((PreparedStatement)stat).setShort(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "short");
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		((PreparedStatement)stat).setInt(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "int");
		
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		((PreparedStatement)stat).setLong(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "long");
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		((PreparedStatement)stat).setFloat(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "float");
		
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		((PreparedStatement)stat).setDouble(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "double");
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		((PreparedStatement)stat).setBigDecimal(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "bigDecimal");
		
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		((PreparedStatement)stat).setString(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "String");
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		((PreparedStatement)stat).setBytes(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "byte[]");
		
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		((PreparedStatement)stat).setDate(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "date");
		
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		((PreparedStatement)stat).setTime(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "time");
		
	}

	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		((PreparedStatement)stat).setTimestamp(parameterIndex, x);
		paramValues.put(parameterIndex, x);
		paramTypes.put(parameterIndex, "timestamp");
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		((PreparedStatement)stat).setAsciiStream(parameterIndex, x, length);
	}

	/**
	 * @deprecated
	 */
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		((PreparedStatement)stat).setUnicodeStream(parameterIndex, x, length);
		
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		((PreparedStatement)stat).setBinaryStream(parameterIndex, x, length);
		
	}

	public void clearParameters() throws SQLException {
		((PreparedStatement)stat).clearParameters();
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scale) throws SQLException {
		((PreparedStatement)stat).setObject(parameterIndex, x, targetSqlType, scale);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		((PreparedStatement)stat).setObject(parameterIndex, x, targetSqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		((PreparedStatement)stat).setObject(parameterIndex, x);
	}

	public boolean execute() throws SQLException {
		return ((PreparedStatement)stat).execute();
	}

	public void addBatch() throws SQLException {
		((PreparedStatement)stat).addBatch();
		
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		((PreparedStatement)stat).setCharacterStream(parameterIndex, reader, length);
		
	}

	public void setRef(int i, Ref x) throws SQLException {
		((PreparedStatement)stat).setRef(i, x);
		
	}

	public void setBlob(int i, Blob x) throws SQLException {
		((PreparedStatement)stat).setBlob(i, x);
		
	}

	public void setClob(int i, Clob x) throws SQLException {
		((PreparedStatement)stat).setClob(i, x);
		
	}

	public void setArray(int i, Array x) throws SQLException {
		((PreparedStatement)stat).setArray(i, x);
		
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return ((PreparedStatement)stat).getMetaData();
	}

	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		((PreparedStatement)stat).setDate(parameterIndex, x, cal);
		
	}

	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		((PreparedStatement)stat).setTime(parameterIndex, x, cal);
		
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		((PreparedStatement)stat).setTimestamp(parameterIndex, x, cal);
	}

	public void setNull(int paramIndex, int sqlType, String typeName)
			throws SQLException {
		((PreparedStatement)stat).setNull(paramIndex, sqlType, typeName);
		
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		((PreparedStatement)stat).setURL(parameterIndex, x);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return ((PreparedStatement)stat).getParameterMetaData();
	}

}

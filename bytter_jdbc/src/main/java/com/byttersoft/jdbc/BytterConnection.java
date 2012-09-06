package com.byttersoft.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

import com.byttersoft.jdbc.translater.SqlTranslater;

/**
 * Bytter 的数据库连接实现
 * @author pangl
 *
 */
public class BytterConnection implements Connection {

	/**
	 * 原始SQL连接
	 */
	private final Connection conn;
	
	/**
	 * 数据库类型
	 */
	final DBType type;
	
	BytterConnection(Connection conn, DBType dbType) {
		this.conn = conn;
		this.type = dbType;
	}
	
	public Statement createStatement() throws SQLException {
		return new BytterStatement(this, conn.createStatement(), type);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return new BytterPreparedStatement(this, conn.prepareStatement(sql), type);
		//return conn.prepareStatement(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return conn.prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return conn.nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		conn.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}

	public void commit() throws SQLException {
		conn.commit();
	}

	public void rollback() throws SQLException {
		conn.rollback();
	}

	public void close() throws SQLException {
		conn.close();
	}

	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		conn.setReadOnly(readOnly);
	}

	public boolean isReadOnly() throws SQLException {
		return conn.isReadOnly();
	}

	public void setCatalog(String catalog) throws SQLException {
		conn.setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return conn.getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		conn.setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return conn.getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		return conn.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		conn.clearWarnings();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return conn.createStatement(resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return new BytterPreparedStatement(this, conn.prepareStatement(sql, resultSetType, resultSetConcurrency), type);
//		return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return conn.getTypeMap();
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		conn.setTypeMap(map);

	}

	public void setHoldability(int holdability) throws SQLException {
		conn.setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return conn.getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return conn.setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		conn.rollback(savepoint);

	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		conn.releaseSavepoint(savepoint);

	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Statement stat = conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		return new BytterStatement(this, stat, type);
	} 

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return new BytterPreparedStatement(this, conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability), type);
//		return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return new BytterPreparedStatement(this, conn.prepareStatement(sql, autoGeneratedKeys), type);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return new BytterPreparedStatement(this, conn.prepareStatement(sql, columnIndexes), type);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		sql = SqlTranslater.translate(sql, type);
		return new BytterPreparedStatement(this, conn.prepareStatement(sql, columnNames), type);
	}
	
	public Connection getOriginalConnection() {
		return conn;
	}


}

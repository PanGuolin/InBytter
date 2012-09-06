package com.byttersoft.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import com.byttersoft.jdbc.translater.SqlTranslater;

/**
 * ���ص�JDBC Statment ʵ��
 * @author pangl
 *
 */
public class BytterStatement implements Statement {
	
	protected final BytterConnection conn;
	protected final Statement stat;
	protected final DBType type;
	
	BytterStatement(BytterConnection conn, Statement stat, DBType type) {
		this.conn = conn;
		this.stat = stat;
		this.type = type;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		return stat.executeQuery(SqlTranslater.translate(sql, type));
	}

	public int executeUpdate(String sql) throws SQLException {
		return stat.executeUpdate(SqlTranslater.translate(sql, type));
	}

	public void close() throws SQLException {
		stat.close();
	}

	public int getMaxFieldSize() throws SQLException {
		return stat.getMaxFieldSize();
	}

	public void setMaxFieldSize(int max) throws SQLException {
		stat.setMaxFieldSize(max);
	}

	public int getMaxRows() throws SQLException {
		return stat.getMaxRows();
	}

	public void setMaxRows(int max) throws SQLException {
		stat.setMaxRows(max);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		stat.setEscapeProcessing(enable);
	}

	public int getQueryTimeout() throws SQLException {
		return stat.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		stat.setQueryTimeout(seconds);
	}

	public void cancel() throws SQLException {
		stat.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return stat.getWarnings();
	}

	public void clearWarnings() throws SQLException {
		stat.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		stat.setCursorName(name);
	}

	public boolean execute(String sql) throws SQLException {
		return stat.execute(SqlTranslater.translate(sql, type));
	}

	public ResultSet getResultSet() throws SQLException {
		return stat.getResultSet();
	}

	public int getUpdateCount() throws SQLException {
		return stat.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return stat.getMoreResults();
	}

	public void setFetchDirection(int direction) throws SQLException {
		stat.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return stat.getFetchDirection();
	}

	public void setFetchSize(int rows) throws SQLException {
		stat.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return stat.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return stat.getResultSetConcurrency();
	}

	public int getResultSetType() throws SQLException {
		return stat.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		stat.addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		stat.clearBatch();
	}

	public int[] executeBatch() throws SQLException {
		return stat.executeBatch();
	}

	public Connection getConnection() throws SQLException {
		return conn;
	}

	public boolean getMoreResults(int current) throws SQLException {
		return stat.getMoreResults(current);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return stat.getGeneratedKeys();
	}

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		return stat.executeUpdate(SqlTranslater.translate(sql, type), autoGeneratedKeys);
	}

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		return stat.executeUpdate(SqlTranslater.translate(sql, type), columnIndexes);
	}

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return stat.executeUpdate(SqlTranslater.translate(sql, type), columnNames);
	}

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		return stat.execute(SqlTranslater.translate(sql, type), autoGeneratedKeys);
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return stat.execute(SqlTranslater.translate(sql, type), columnIndexes);
	}

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		return stat.execute(SqlTranslater.translate(sql, type), columnNames);
	}

	public int getResultSetHoldability() throws SQLException {
		return stat.getResultSetHoldability();
	}

}
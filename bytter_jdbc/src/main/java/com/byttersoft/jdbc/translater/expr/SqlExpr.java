package com.byttersoft.jdbc.translater.expr;

/**
 * SQL表达式
 * @author pangl
 *
 */
public class SqlExpr {
	
	protected final int start;
	
	protected final int end;

	protected final String content;
	
	/**
	 * 创建一个表达式对象
	 * @param sql SQL片断
	 * @param start 表达式在整个SQL中的起始位置
	 * @param end 表达式在整个SQL中的结束位置
	 */
	public SqlExpr(String sql, int start, int end) {
		this.start = start;
		this.end = end;
		this.content = sql;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getContent() {
		return content;
	}
	
	public String toString() {
		return content.trim();
	}
}

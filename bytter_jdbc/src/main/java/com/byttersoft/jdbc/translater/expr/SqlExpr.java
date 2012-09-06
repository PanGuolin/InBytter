package com.byttersoft.jdbc.translater.expr;

/**
 * SQL���ʽ
 * @author pangl
 *
 */
public class SqlExpr {
	
	protected final int start;
	
	protected final int end;

	protected final String content;
	
	/**
	 * ����һ�����ʽ����
	 * @param sql SQLƬ��
	 * @param start ���ʽ������SQL�е���ʼλ��
	 * @param end ���ʽ������SQL�еĽ���λ��
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

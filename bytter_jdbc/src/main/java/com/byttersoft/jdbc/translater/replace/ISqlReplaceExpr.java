package com.byttersoft.jdbc.translater.replace;

/**
 * SQL替换表达式
 * @author pangl
 *
 */
public interface ISqlReplaceExpr {
	
	/**
	 * 对SQL进行替换操作
	 * @param sql 待查找替换的原始SQL语句
	 * @return 执行替换之后的SQL语句
	 */
	void replace(StringBuilder sql);

}

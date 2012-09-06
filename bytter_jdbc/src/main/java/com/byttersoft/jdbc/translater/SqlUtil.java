package com.byttersoft.jdbc.translater;

import com.byttersoft.jdbc.translater.expr.FunctionExpr;


/**
 * SQL 转换工具类
 * @author pangl
 *
 */
public abstract class SqlUtil {
	

	/**
	 * 判断sql中指定位置是否被包含在字符串当中
	 * @param sql
	 * @param start
	 * @return
	 */
	public static boolean isInString(CharSequence sql, int start) {
		if (sql.length() == 0 || sql.length() <= start)
			return false;
		int pos = -1;
		
		boolean stringFlag = false;
		while(pos <= start) {
			pos ++;
			char ch = sql.charAt(pos);
			if (ch == '\'') {
				stringFlag = !stringFlag;
			}
		}
		return stringFlag;
	}

	
	
	/**
	 * 从指定位置开始在SQL语句中查找关键字<br/>
	 * 关键字不区分大小写
	 * @param sql 待查找的SQL语句
	 * @param keyword 关键字
	 * @param fromIndex 起始位置，查找时包含该索引
	 * @return
	 */
	public static int findKeyword(CharSequence sql, String keyword, int fromIndex) {
		String lowSql = sql.toString().toLowerCase();
		String lowKey = keyword.toLowerCase();
		int sIndex  = lowSql.indexOf(lowKey, fromIndex);
		while(sIndex != -1) {
			if (isInString(sql, sIndex) || 
					!isSeparator(sql, sIndex -1) ||
					!isSeparator(sql, sIndex + keyword.length())) {
				sIndex = lowSql.indexOf(keyword, sIndex + 1);
				continue;
			}
			break;
		}
		return sIndex;
	}
	
	/**
	 * 在SQL语句中查找 指定函数的起始方法
	 * @param sql SQL语句 
	 * @param functionName 函数名称
	 * @param paraLen 函数参数长度
	 * @param fromIndex 查找的起始位置，查找时包含该位置
	 * @return
	 */
	public static FunctionExpr findFunction(String sql, String functionName, int paraLen, int fromIndex) {
		int index = findKeyword(sql, functionName, fromIndex);
		while(index != -1) {
			int pos = index + functionName.length()-1;
			while(true) {
				pos ++;
				char ch = sql.charAt(pos);
				if (Character.isWhitespace(ch)) {
					continue;
				}
				//下一个字符不是(说明该关键字不是函数名称
				if (ch != '(') {
					index = findKeyword(sql, functionName, pos);
					break;
				}
				//下一个字符是(则判断参数个数是否匹配
				int ePos = findEndBracket(sql, pos + 1);
				if (ePos == -1)
					return null;
				FunctionExpr expr = new FunctionExpr(sql.substring(index, ePos+1), index, ePos+1);
				if (expr.getParameterSize() == paraLen) {
					return expr;
				}
				index = findKeyword(sql, functionName, ePos + 1);
			}
		}
		return null;
	}
	

	/**
	 * 从指定位置查找结束的右括号
	 * @param sql
	 * @param fromIndex
	 * @return
	 */
	public static int findEndBracket(String sql, int fromIndex) {
		int pos = fromIndex;
		while(pos < sql.length()) {
			char ch = sql.charAt(pos);
			if (Character.isWhitespace(ch)) {
				pos ++;
				continue;
			}
			if (ch == '\'') {
				int p = findEndString(sql, pos + 1);
				if (p == -1)
					return -1;
				pos = p + 1;
				continue;
			}
			if (ch == '(') {
				int p = findEndBracket(sql, pos + 1);
				if (p == -1)
					return -1;
				pos = p + 1;
				continue;
			}
			if (ch == ')') {
				return pos;
			}
			pos ++;
		}
		return -1;
	}
	
	
	/**
	 * 查找另一个结束的字符串标识
	 * @param sql
	 * @param fromIndex
	 * @return
	 */
	public static int findEndString(String sql, int fromIndex) {
		int index = fromIndex;
		int len = sql.length();
		while(index < len) {
			char ch = sql.charAt(index);
			if (ch == '\'') {
				if (index >= len -1) return index;
				if (sql.charAt(fromIndex + 1) != '\'') return index;
			}
			index ++;
		}
		return -1;
	}
	
	/**
	 * 判断SQL中指定位置的字符是不是分隔符
	 * @param sql SQL语句
	 * @param index 检索索引
	 * @return 如果检索索引超出范围或该位置上分隔符则返回true
	 */
	public static boolean isSeparator(CharSequence sql, int index) {
		if (index == -1 || index >= sql.length() -1)
			return true;
		return isSeparator(sql.charAt(index));
	}
	
	public static boolean isSeparator(char ch) {
		if (Character.isWhitespace(ch))
			return true;
		if (isOperatorChar(ch))
			return true;
		switch (ch) {
			case ')':
			case '(':
			case ',':
				return true;
			default:
				return false;
		}	
	}
	
	/**
	 * 判断SQL中指定位置的字符是不是操作符(字符)
	 * @param sql
	 * @param index
	 * @return 
	 */
	static boolean isOperatorChar(CharSequence sql, int index) {
		if (index == -1 || index >= sql.length() -1)
			return false;
		return isOperatorChar(sql.charAt(index));	
	}
	
	static boolean isOperatorChar(char ch) {
		switch (ch) {
			case '*':
			case '+':
			case '-':
			case '/':
			case '>':
			case '<':
			case '=':
			case '!':
			case '^':
			case '|':
				return true;
			default:
				return false;
		}		
	}
	
	/**
	 * 判断两个数组对象是否相等
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static boolean arrayEquals(Object[] a1, Object[] a2) {
		if (a1 == null) {
			return a2 == null;
		}
		if (a1.length != a2.length)
			return false;
		for (int i=0; i<a1.length; i++) {
			if (!a1[i].equals(a2[i]))
				return false;
		}
		return true;
	}
}

package com.byttersoft.jdbc.translater;

import com.byttersoft.jdbc.translater.expr.FunctionExpr;


/**
 * SQL ת��������
 * @author pangl
 *
 */
public abstract class SqlUtil {
	

	/**
	 * �ж�sql��ָ��λ���Ƿ񱻰������ַ�������
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
	 * ��ָ��λ�ÿ�ʼ��SQL����в��ҹؼ���<br/>
	 * �ؼ��ֲ����ִ�Сд
	 * @param sql �����ҵ�SQL���
	 * @param keyword �ؼ���
	 * @param fromIndex ��ʼλ�ã�����ʱ����������
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
	 * ��SQL����в��� ָ����������ʼ����
	 * @param sql SQL��� 
	 * @param functionName ��������
	 * @param paraLen ������������
	 * @param fromIndex ���ҵ���ʼλ�ã�����ʱ������λ��
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
				//��һ���ַ�����(˵���ùؼ��ֲ��Ǻ�������
				if (ch != '(') {
					index = findKeyword(sql, functionName, pos);
					break;
				}
				//��һ���ַ���(���жϲ��������Ƿ�ƥ��
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
	 * ��ָ��λ�ò��ҽ�����������
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
	 * ������һ���������ַ�����ʶ
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
	 * �ж�SQL��ָ��λ�õ��ַ��ǲ��Ƿָ���
	 * @param sql SQL���
	 * @param index ��������
	 * @return �����������������Χ���λ���Ϸָ����򷵻�true
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
	 * �ж�SQL��ָ��λ�õ��ַ��ǲ��ǲ�����(�ַ�)
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
	 * �ж�������������Ƿ����
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

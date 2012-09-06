package com.byttersoft.jdbc.translater.replace;

import com.byttersoft.jdbc.translater.SqlUtil;

/**
 * 操作符替换表达式
 * @author pangl
 *
 */
public class OperatorRep implements ISqlReplaceExpr{

	public final String orgOper;
	
	public final String targetOper;

	/**
	 * 
	 * @param orgOper 原始符号
	 * @param targetOper 目标符号
	 */
	public OperatorRep(String orgOper, String targetOper) {
		super();
		this.orgOper = orgOper;
		this.targetOper = targetOper;
	}

	public void replace(StringBuilder sql) {
		int index = findOperator(sql, orgOper, 0);
		int len = orgOper.length();
		int repLen = targetOper.length();
		while(index != -1) {
			sql.replace(index, index + len, targetOper);
			index = index - len + repLen;
			index = findOperator(sql, orgOper, index);
		}
	}
	
	
	/**
	 * 查找操作符
	 * @param sql
	 * @param operator
	 * @param startIndex
	 * @return
	 */
	static int findOperator(StringBuilder sql, String operator, int fromIndex) {
		int sIndex = sql.indexOf(operator, fromIndex);
		while(sIndex != -1) {
			if (SqlUtil.isInString(sql.toString(), sIndex)) {
				sIndex = sql.indexOf(operator, sIndex + operator.length());
			} else {
				return sIndex;
			}
		}
		return sIndex;
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof OperatorRep))
			return false;
		OperatorRep rep = (OperatorRep)o;
		return orgOper.equals(rep.orgOper) &&
				targetOper.equals(rep.targetOper);
	}
}

package com.byttersoft.jdbc.translater.replace;

import com.byttersoft.jdbc.translater.SqlUtil;
import com.byttersoft.jdbc.translater.expr.FunctionExpr;


/**
 * 函数替换表达式
 * @author pangl
 *
 */
public class FunctionRep implements ISqlReplaceExpr {
	
	/**
	 * 原始的函数名称
	 */
	private final String orgFunctName;
	
	/**
	 * 原始函数的参数个数
	 */
	private final int orgParamSize;
	
	/**
	 * 目标函数的表达式
	 */
	private final String targetExpr;
	
	
	/**
	 * 源函数的参数特征，即条件，如 to_date(x,'yyyy-mm-dd')则用 2='yyyy-mm-dd'表示
	 */
	private final String[] conditions;

	public FunctionRep(String orgName, int paramSize, String[] conditions, String targetExpr) {
		this.orgFunctName = orgName.toUpperCase();
		this.orgParamSize = paramSize;
		//this.targetFunctName = targetName.toUpperCase();
		//this.targetParamDesc = params;
		this.conditions = conditions;
		this.targetExpr = targetExpr;
	}
	
	public FunctionRep(String orgName, int paramSize, String targetExpr) {
		this(orgName, paramSize, null, targetExpr);
	}

	public void replace(StringBuilder sql) {
		FunctionExpr funct = findFunction(sql, 0);
		while(funct != null) {
			StringBuilder sb = new StringBuilder(targetExpr);
			//如果某个函数的参数超过9位，则会产生错误的转换结果
			for (int i=0; i<orgParamSize; i++) {
				String find = "?" + (i+1);
				int sPos = sb.indexOf(find);
				while(sPos != -1) {
					sb.replace(sPos, sPos + 2, funct.getParameter(i+1).toString());
					sPos = sb.indexOf(find);
				}
			}
			sql.replace(funct.getStart(), funct.getEnd(), sb.toString());
			funct = findFunction(sql, 0);
		}
	}
	
	/**
	 * 在SQL语句中查找 指定函数的起始方法
	 * @param sql SQL语句 
	 * @param functionName 函数名称
	 * @param paraLen 函数参数长度
	 * @param fromIndex 查找的起始位置，查找时包含该位置
	 * @return
	 */
	private FunctionExpr findFunction(StringBuilder sql, int fromIndex) {
		int index = SqlUtil.findKeyword(sql.toString(), this.orgFunctName, fromIndex);
		while(index != -1) {
			int pos = index + orgFunctName.length()-1;
			while(true) {
				pos ++;
				char ch = sql.charAt(pos);
				if (Character.isWhitespace(ch)) {
					continue;
				}
				//下一个字符不是(说明该关键字不是函数名称
				if (ch != '(') {
					index = SqlUtil.findKeyword(sql.toString(), orgFunctName, pos);
					break;
				}
				//下一个字符是(则判断参数个数是否匹配
				int ePos = SqlUtil.findEndBracket(sql.toString(), pos + 1);
				if (ePos == -1)
					return null;
				FunctionExpr expr = new FunctionExpr(sql.substring(index, ePos+1), index, ePos+1);
				boolean suitable = expr.getParameterSize() == this.orgParamSize;
				if (suitable) {
					if (this.conditions != null) {
						for (String cond : conditions) {
							String[] ps = cond.split("=");
							int pI = Integer.parseInt(ps[0]);
							if (!ps[1].trim().equalsIgnoreCase(expr.getParameter(pI).toString())) {
								suitable = false;
								break;
							}
						}
					}
				}
				if (suitable)
					return expr;
				index = SqlUtil.findKeyword(sql.toString(), this.orgFunctName, ePos + 1);
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof FunctionRep))
			return false;
		FunctionRep rep = (FunctionRep)o;
		if (! (orgFunctName.equals(rep.orgFunctName) &&
				orgParamSize == rep.orgParamSize &&
				targetExpr.equalsIgnoreCase(rep.targetExpr) ))
				return false;
		if (!SqlUtil.arrayEquals(conditions, rep.conditions))
			return false;
		return true;
	}
}

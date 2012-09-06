package com.byttersoft.jdbc.translater.replace;

import com.byttersoft.jdbc.translater.SqlUtil;

/**
 * 关键字替换表达式
 * @author pangl
 *
 */
public class KeywordRep implements ISqlReplaceExpr{
	private final String orgKeyword;
	
	private final String targetKeyword;

	/**
	 * 
	 * @param orgKeyword 原始关键字
	 * @param targetKeyword 目标关键字
	 */
	public KeywordRep(String orgKeyword, String targetKeyword) {
		super();
		this.orgKeyword = orgKeyword.toUpperCase();
		this.targetKeyword = targetKeyword.toUpperCase();
	}

	public void replace(StringBuilder sql) {
		int index = SqlUtil.findKeyword(sql.toString(), orgKeyword, 0);
		int len = orgKeyword.length();
		int repLen = targetKeyword.length();
		while(index != -1) {
			sql.replace(index, index + len, targetKeyword);
			index = index - len + repLen;
			index =  SqlUtil.findKeyword(sql.toString(), orgKeyword, index);
		}
	}
	
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof KeywordRep))
			return false;
		KeywordRep rep = (KeywordRep)o;
		return orgKeyword.equals(rep.orgKeyword) &&
				targetKeyword.equals(rep.targetKeyword);
	}
}

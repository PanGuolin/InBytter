package com.byttersoft.jdbc.translater.replace;

import com.byttersoft.jdbc.translater.SqlUtil;

/**
 * 顺序关键字替换
 * @author pangl
 *
 */
public class SKeywordsRep implements ISqlReplaceExpr {
	
	private final String[] keywords;
	
	private final String target;
	
	
	/**
	 * @param keywords 有顺序的关键字
	 * @param target 替换的目标语句
	 */
	public SKeywordsRep(String[] keywords, String target) {
		super();
		this.keywords = keywords;
		for (int i=0; i<keywords.length; i++) {
			keywords[i] = keywords[i].trim();
		}
		this.target = target.trim();
	}



	public void replace(StringBuilder sql) {
		int sPos = SqlUtil.findKeyword(sql, keywords[0], 0);
		while(sPos != -1) {
			boolean suit = true;
			int ePos = sPos + keywords[0].length();
			for (int i=1; i<keywords.length; i++) {
				int pos = nextKeywordIndex(sql, keywords[i], ePos);
				if (pos == -1) {
					suit = false;
					break;
				}
				ePos = pos + keywords[i].length();
			}
			
			if (!suit) {
				sPos = SqlUtil.findKeyword(sql, keywords[0], ePos);
			} else {
				sql.replace(sPos, ePos, target);
				sPos = SqlUtil.findKeyword(sql, keywords[0], 0);
			}
		}
	}
	
	/**
	 * 判断下一个标记是否为指定的关键字并返回其起始索引
	 * @param sql
	 * @param keyword
	 * @param fromIndex
	 * @return 如果下一个关键字不是指定的关键字，则返回-1
	 */
	private int nextKeywordIndex(StringBuilder sql, String keyword, int fromIndex) {
		int pos = fromIndex;
		char ch = sql.charAt(pos);
		int maxLen = sql.length() - 1;
		int keyLen = keyword.length();
		while(Character.isWhitespace(ch)) {
			if (pos >= maxLen) {
				return -1;
			}
			pos ++;
			ch = sql.charAt(pos);
		}
		
		if (pos + keyword.length()-1 > maxLen)
			return -1;
		if (!sql.substring(pos, pos + keyLen).equalsIgnoreCase(keyword))
			return -1;
		if (pos + keyword.length() == maxLen)
			return pos;
		if (SqlUtil.isSeparator(sql, pos + keyword.length() +1))
			return pos;
		return -1;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SKeywordsRep))
			return false;
		SKeywordsRep rep = (SKeywordsRep)obj;
		if (!target.equals(rep.target))
			return false;
		return SqlUtil.arrayEquals(keywords, rep.keywords);
	}
}

package com.byttersoft.jdbc.translater.expr;

import java.util.ArrayList;
import java.util.List;

import com.byttersoft.jdbc.translater.SqlUtil;


/**
 * SQL 函数表达式
 * @author pangl
 *
 */
public class FunctionExpr extends SqlExpr{
	
	/**
	 * 函数名称
	 */
	private String funtionName;
	
	/**
	 * 函数表达式
	 */
	private List<SqlExpr> parameters = new ArrayList<SqlExpr>();

	public FunctionExpr(String sql, int start, int end) {
		super(sql, start, end);
		
		int index = content.indexOf('(');
		this.funtionName = content.substring(0, index).trim().toUpperCase();
		
		int sPos = index + 1;
		int cPos = sPos;//当前位置
//		StringBuilder sb = new StringBuilder(content);
		while(cPos < content.length() - 1) {
			char ch = content.charAt(cPos);
			if (ch == '\'') {
				cPos = SqlUtil.findEndString(content, cPos + 1) + 1;
				continue;
			}
			//如果有一个括号，说明一下个参数是函数或包含在括号中的表达式
			if (ch == '(') {
				cPos = SqlUtil.findEndBracket(content, cPos + 1);
				continue;
			}
			if (ch == ',') {
				createParam(sPos, cPos);
				cPos ++;
				sPos = cPos;
				continue;
			}
			cPos ++;
		}
		if (sPos < content.length() - 1) {
			String str = content.substring(sPos, content.length() - 1);
			if (str.trim().length() > 0) {
				createParam(sPos, cPos);
			}
		}
	}
	
	private void createParam(int sPos, int ePos) {
		SqlExpr expr = new SqlExpr(content.substring(sPos, ePos), sPos + start, ePos + start);
		parameters.add(expr);
		
	}

	public String getFuntionName() {
		return funtionName;
	}

	public int getParameterSize() {
		return parameters.size();
	}
	
	public SqlExpr getParameter(int index) {
		return parameters.get(index-1);
	}
}

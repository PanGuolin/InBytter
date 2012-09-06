package com.byttersoft.jdbc.translater.replace;

import com.byttersoft.jdbc.translater.SqlUtil;
import com.byttersoft.jdbc.translater.expr.FunctionExpr;


/**
 * �����滻���ʽ
 * @author pangl
 *
 */
public class FunctionRep implements ISqlReplaceExpr {
	
	/**
	 * ԭʼ�ĺ�������
	 */
	private final String orgFunctName;
	
	/**
	 * ԭʼ�����Ĳ�������
	 */
	private final int orgParamSize;
	
	/**
	 * Ŀ�꺯���ı��ʽ
	 */
	private final String targetExpr;
	
	
	/**
	 * Դ�����Ĳ������������������� to_date(x,'yyyy-mm-dd')���� 2='yyyy-mm-dd'��ʾ
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
			//���ĳ�������Ĳ�������9λ�������������ת�����
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
	 * ��SQL����в��� ָ����������ʼ����
	 * @param sql SQL��� 
	 * @param functionName ��������
	 * @param paraLen ������������
	 * @param fromIndex ���ҵ���ʼλ�ã�����ʱ������λ��
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
				//��һ���ַ�����(˵���ùؼ��ֲ��Ǻ�������
				if (ch != '(') {
					index = SqlUtil.findKeyword(sql.toString(), orgFunctName, pos);
					break;
				}
				//��һ���ַ���(���жϲ��������Ƿ�ƥ��
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

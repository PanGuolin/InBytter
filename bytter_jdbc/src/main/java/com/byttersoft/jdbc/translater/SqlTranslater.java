package com.byttersoft.jdbc.translater;

import java.util.ArrayList;
import java.util.List;

import com.byttersoft.jdbc.DBType;
import com.byttersoft.jdbc.translater.replace.FunctionRep;
import com.byttersoft.jdbc.translater.replace.ISqlReplaceExpr;
import com.byttersoft.jdbc.translater.replace.KeywordRep;
import com.byttersoft.jdbc.translater.replace.OperatorRep;
import com.byttersoft.jdbc.translater.replace.SKeywordsRep;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 * SqlTranslater ��ʾһ�����ݿ�����ת����<br/>
 * ����һ�������࣬����ת�������ɸ����ݿ��Ӧ��ʵ��������ʵ��
 * @author pangl
 *
 */
public abstract class SqlTranslater {
	
	private static Hashtable instances = new Hashtable();
	
	
	/**
	 * ��SQLת��ָ�����ݿ��ϼ��ݵ�SQL���
	 * @param sql ��ת����SQL���
	 * @param type ָ��Ŀ�����ݿ�����
	 * @return ���������ָ�����͵�ת������ֱ�ӷ���
	 */
	public static String translate(String sql, DBType type) {
		SqlTranslater translater = getInstance(type);
		if (translater != null) {
			return translater.translateSql(sql);
		}
		return sql;
	}
	
	/**
	 * �������ݿ����ͷ��ض���ת����
	 * @param type
	 * @return Ĭ�Ϸ���oracle�Ķ���ת����
	 */
	private final static SqlTranslater getInstance(DBType type) {
		SqlTranslater instance = (SqlTranslater)instances.get(type);
		if (instance == null) {
			synchronized (instances) {
				instance = (SqlTranslater)instances.get(type);
				if (instance == null) {
					switch (type) {
					case sqlserver:
						instance = new SqlServerTranslater();
						break;
					case db2:
						instance = new DB2Translater();
						break;
					case oracle:
					case oracle10g:
						instance = new OracleTranslater();
						break;
					default:
						throw new RuntimeException("unsupported db type:" + type);
					}
					instance.init();
					instances.put(type, instance);
				}
			}
		}
		return instance;
	}
	
	/**
	 * ��ʼ��ʵ������
	 * @exception RuntimeException ������õı��ʽ���ϲ��򷵻��������쳣
	 */
	private void init() {
		String[] exprs = getSqlReplaceExprs();
		if (exprs == null)
			return;
		List<ISqlReplaceExpr> reps = new ArrayList<ISqlReplaceExpr>();
		for (String expr : exprs) {
			expr = expr.trim();
			ISqlReplaceExpr rep = null;
			if (expr.startsWith("F:")) {
				rep = createFunctionRep(expr.substring(2));
			} else if (expr.startsWith("K:")) {
				rep = createKeywordRep(expr.substring(2));
			} else if (expr.startsWith("O:")) {
				rep = createOperatorRep(expr.substring(2));
			}  else if (expr.startsWith("S:")) {
				rep = createSKeywordsRep(expr.substring(2));
			}
			if (rep != null) {
				reps.add(rep);
			} else {
				throw new RuntimeException("unrecognized repalce expression :" + expr);
			}
		}
		this.replacements = (ISqlReplaceExpr[])reps.toArray(new ISqlReplaceExpr[reps.size()]);
	}
	
	/**
	 * ���ݱ��ʽ����һ�������滻����<br/>
	 * ���ʽ���������ڣ�NVL(?1, ?2) =&gt; ISNULL(?1, ?2)<br/>
	 * ע�⺯���Ĳ����в��ܰ�������,
	 * @param expr
	 * @return
	 */
	static FunctionRep createFunctionRep(String expr) {
		String[] p = expr.split("=>");
		String oExpr = p[0].trim();
		String tExpr = p[1].trim();
		
		String oFName = oExpr.substring(0, oExpr.indexOf('('));
		
		
		oExpr = oExpr.substring(oExpr.indexOf('(')+1, oExpr.lastIndexOf(')'));
		
		
		String[] oParams = oExpr.split(",");
		List<String> oConditions = new ArrayList<String>();
		for (int i=0; i<oParams.length; i++) {
			String oParam = oParams[i].trim();
			if (oParam.charAt(0) != '?') {
				oConditions.add((i+1) + "=" + oParam);
			}
		}
		return new FunctionRep(oFName, oParams.length, 
				(String[])oConditions.toArray(new String[oConditions.size()]), tExpr);
	}
	
	/**
	 * ���ݱ��ʽ����һ���ؼ����滻����<br/>
	 * ���ʽ��ʽ���£�sysdate =&gt; getdate()<br/>
	 * @param expr
	 * @return
	 */
	static KeywordRep createKeywordRep(String expr) {
		String[] p = expr.split("=>");
		if (p.length > 1)
			return new KeywordRep(p[0].trim(), p[1].trim());
		else 
			return new KeywordRep(p[0].trim(), "");
	}
	
	/**
	 * ���ݱ��ʽ����һ���ؼ����б��滻����<br/>
	 * ���ʽ���£�keyword1 keyword2 keyword3 => replace_string
	 * @param expr
	 * @return
	 */
	static SKeywordsRep createSKeywordsRep(String expr) {
		String[] p = expr.split("=>");
		String[] kwds = p[0].split(" ");
		List<String> kwdList = new ArrayList<String>();
		for (String k : kwds) {
			if (k.trim().length() == 0)
				continue;
			kwdList.add(k.trim());
		}
		if (p.length > 1)
			return new SKeywordsRep((String[])kwdList.toArray(new String[kwdList.size()]), p[1].trim());
		else
			return new SKeywordsRep((String[])kwdList.toArray(new String[kwdList.size()]), "");
	}
	
	/**
	 * ���ݱ��ʽ����һ���������滻����
	 * ���ʽ��ʽ���£�|| => +
	 * @param expr
	 * @return
	 */
	static OperatorRep createOperatorRep(String expr) {
		String[] p = expr.split("=>");
		return new OperatorRep(p[0].trim(), p[1].trim());
	}
	/**
	 * ����滻���ʽ
	 */
	private ISqlReplaceExpr[] replacements;
	
	/**
	 * ת��SQL���
	 * @param sql
	 * @return
	 */
	public final String translateSql(String sql) {
		StringBuilder sb = new StringBuilder(sql);
		if (replacements != null) {
			for (ISqlReplaceExpr expr : replacements) {
				expr.replace(sb);
			}
		}
		doTranslate(sb);
		return sb.toString().trim();
	}
	
	protected abstract String[] getSqlReplaceExprs();
	
	/**
	 * �����滻֮���SQLת������
	 * @param sql
	 */
	protected abstract void doTranslate(StringBuilder sql);
}

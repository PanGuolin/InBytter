package com.byttersoft.jdbc.translater.replace;

/**
 * SQL�滻���ʽ
 * @author pangl
 *
 */
public interface ISqlReplaceExpr {
	
	/**
	 * ��SQL�����滻����
	 * @param sql �������滻��ԭʼSQL���
	 * @return ִ���滻֮���SQL���
	 */
	void replace(StringBuilder sql);

}

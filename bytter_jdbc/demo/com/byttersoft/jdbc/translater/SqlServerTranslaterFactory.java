package com.byttersoft.jdbc.translater;

/**
 * ���� ����ģʽ֮���幤�� ��SqlServerת�������� 
 * @author pangl
 *
 */
public class SqlServerTranslaterFactory implements ITranslaterFactory {

	/**
	 * ʵ�ֳ��󹤳� �෽��
	 */
	public SqlTranslater create() {
		//���ӵĴ����߼�
		return new SqlServerTranslater();
	}

}

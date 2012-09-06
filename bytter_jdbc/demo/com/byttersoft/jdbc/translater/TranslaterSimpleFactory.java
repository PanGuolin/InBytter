package com.byttersoft.jdbc.translater;

import com.byttersoft.jdbc.DBType;

/**
 * ������ļ򵥹���
 * @author pangl
 *
 */
public class TranslaterSimpleFactory {

	/**
	 * �������ݿ����ͷ�����Ӧ��SQL������
	 * @param type ��������
	 * @return �������Ϊnull�򲻴����򷵻�null
	 */
	public static SqlTranslater create(DBType type) {
		switch(type) {
		case db2:
			return new DB2Translater();
		case oracle:
		case oracle10g:
			return new OracleTranslater();
		case sqlserver:
			return new SqlServerTranslater();
		}
		return null;
	}
}

class SimpleFacotryClient {
	public static void main(String[] args) {
		SqlTranslater translater = TranslaterSimpleFactory.create(DBType.db2);
		String sql = translater.translateSql("select 1 from dual");
		System.out.println(sql);
	}
}

package com.byttersoft.jdbc.translater;

import com.byttersoft.jdbc.DBType;

/**
 * 翻译类的简单工厂
 * @author pangl
 *
 */
public class TranslaterSimpleFactory {

	/**
	 * 根据数据库类型返回相应的SQL翻译器
	 * @param type 数据类型
	 * @return 如果类型为null或不存在则返回null
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

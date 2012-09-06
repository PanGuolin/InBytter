package com.byttersoft.jdbc.translater;

/**
 *��������ģʽ �ͻ���
 * @author pangl
 *
 */
public class MethodFactoryClient {

	public static void main(String[] args) {
		ITranslaterFactory factory = new DB2TranslaterFactory();
		SqlTranslater translater = factory.create();
		System.out.println(translater.translateSql("select 1 from dual"));
		
		factory = new SqlServerTranslaterFactory();
		translater = factory.create();
		System.out.println(translater.translateSql("select 1 from dual"));
	}
}

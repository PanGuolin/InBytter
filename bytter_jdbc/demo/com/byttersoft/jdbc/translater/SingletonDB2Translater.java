package com.byttersoft.jdbc.translater;

/**
 * ����ģʽ��DB2ת���������ö���ʽ����
 * @author pangl
 *
 */
public class SingletonDB2Translater extends SqlTranslater{
	
	private static final SingletonDB2Translater instance = new SingletonDB2Translater();
	
	public static SingletonDB2Translater getInstance() {
		return instance;
	}
	
	private SingletonDB2Translater() {}

	@Override
	protected String[] getSqlReplaceExprs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doTranslate(StringBuilder sql) {
		// TODO Auto-generated method stub
		
	}

}

package com.byttersoft.jdbc.translater;

/**
 * 懒汉式单例模式
 * @author pangl
 *
 */
public class SingletonSqlServerTranslater extends SqlTranslater{
	
	private static volatile SingletonSqlServerTranslater instance = null;
	
	public static SingletonSqlServerTranslater getInstance() {
		if (instance == null) {
			synchronized (SingletonSqlServerTranslater.class) {
				if (instance == null) {
					instance = new SingletonSqlServerTranslater();
				}
			}
		}
		return instance;
	}

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

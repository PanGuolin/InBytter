package com.byttersoft.jdbc.translater;

/**
 * 工厂 方法模式之具体工厂 ：SqlServer转换器工厂 
 * @author pangl
 *
 */
public class SqlServerTranslaterFactory implements ITranslaterFactory {

	/**
	 * 实现抽象工厂 类方法
	 */
	public SqlTranslater create() {
		//复杂的创建逻辑
		return new SqlServerTranslater();
	}

}

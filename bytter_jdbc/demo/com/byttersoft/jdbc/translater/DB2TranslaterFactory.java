package com.byttersoft.jdbc.translater;

/**
 * 具体工厂之DB2数据库转换器工厂类
 * @author pangl
 *
 */
public class DB2TranslaterFactory implements ITranslaterFactory {

	/**
	 * 实现抽象工厂方法
	 */
	public SqlTranslater create() {
		//复杂的创建逻辑
		return new DB2Translater();
	}

}

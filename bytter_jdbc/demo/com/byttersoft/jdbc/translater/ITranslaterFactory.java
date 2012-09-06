package com.byttersoft.jdbc.translater;

/**
 * 工厂方法模式的抽象工厂类（接口）
 * @author pangl
 *
 */
public interface ITranslaterFactory {
	
	/**
	 * 创建转换类接口
	 * @return
	 */
	public SqlTranslater create();

}

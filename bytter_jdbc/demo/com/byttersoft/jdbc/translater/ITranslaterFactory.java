package com.byttersoft.jdbc.translater;

/**
 * ��������ģʽ�ĳ��󹤳��ࣨ�ӿڣ�
 * @author pangl
 *
 */
public interface ITranslaterFactory {
	
	/**
	 * ����ת����ӿ�
	 * @return
	 */
	public SqlTranslater create();

}

package com.byttersoft.jdbc.translater;

/**
 * ���幤��֮DB2���ݿ�ת����������
 * @author pangl
 *
 */
public class DB2TranslaterFactory implements ITranslaterFactory {

	/**
	 * ʵ�ֳ��󹤳�����
	 */
	public SqlTranslater create() {
		//���ӵĴ����߼�
		return new DB2Translater();
	}

}

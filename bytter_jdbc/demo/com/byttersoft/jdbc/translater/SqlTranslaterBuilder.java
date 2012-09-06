package com.byttersoft.jdbc.translater;

import com.byttersoft.jdbc.translater.replace.IFunctionReplacer;
import com.byttersoft.jdbc.translater.replace.IKeywordReplacer;

/**
 * builderģʽ�е�Sqlת�����ӿ�
 * @author pangl
 *
 */
public interface SqlTranslaterBuilder {

	public IFunctionReplacer[] createFunctionReplacers();
	
	public IKeywordReplacer[] createKeywordReplacers();
}

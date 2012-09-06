package com.byttersoft.jdbc.translater;

import com.byttersoft.jdbc.translater.replace.IFunctionReplacer;
import com.byttersoft.jdbc.translater.replace.IKeywordReplacer;

/**
 * builder模式中的Sql转换器接口
 * @author pangl
 *
 */
public interface SqlTranslaterBuilder {

	public IFunctionReplacer[] createFunctionReplacers();
	
	public IKeywordReplacer[] createKeywordReplacers();
}

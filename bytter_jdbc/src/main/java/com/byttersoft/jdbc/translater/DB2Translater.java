package com.byttersoft.jdbc.translater;

/**
 * DB2���ݿ��е�SQL���ת����
 * @author pangl
 *
 */
public class DB2Translater extends SqlTranslater {

	@Override
	protected String[] getSqlReplaceExprs() {
		return new String[] {
				"S:from dual => SYSIBM.SYSDUMMY1"
		};
	}

	@Override
	protected void doTranslate(StringBuilder sql) {
		// TODO Auto-generated method stub
	}

}

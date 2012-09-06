package com.byttersoft.jdbc.translater;

/**
 * SqlServer 数据库的对象转换器
 * @author pangl
 *
 */
public class SqlServerTranslater extends SqlTranslater{
	
	private static final String[] replace_exps = new String[] {
		"F:CONCAT(?1, ?2) => ?1 + ?2",
		"F:INITCAP(?1) => SUBSTRING(UPPER('?1'),1,1) + SUBSTRING(LOWER('?1'),2, LEN('?1'))",
		"F:SUBSTR(?1, ?2) => SUBSTRING(?1, ?2, 0)",
		"F:SUBSTR(?1, ?2, ?3) => SUBSTRING(?1, ?2, ?3)",
		"F:INSRT(?1, ?2) => CHARINDEX(?2, ?1)",
		"F:INSRT(?1, ?2, ?3) => CHARINDEX(?2, ?1, ?3)",
		"F:TRIM(?1) => LTRIM(RTRIM(?1))",
		"F:LPAD(?1, ?2) => SPACE(?2-LEN(?1)) + ?1",
		"F:RPAD(?1, ?2) => ?1 + SPACE(?2-LEN(?1))",
		"F:LENGTH(?1) => LEN(?1)",
		"F:NVL(?1, ?2) => (CASE WHEN (ISNULL(?1, '') = '') THEN ?2 ELSE ?1 END)",

		"F:TO_NUMBER(?1) => CONVERT(numeric, ?1)",
		//"F:TO_DATE(?1) => CONVERT(datetime, ?1)",
		"F:TO_DATE(?1, 'Month dd, yyyy') => CONVERT(datetime, ?1)",
		"F:TO_DATE(?1, 'yyyy.MM.dd') => CONVERT(datetime, ?1, 102)",
		"F:TO_DATE(?1, 'YYYY-MM-DD HH24:MI:SS') => CONVERT(datetime, ?1, 20)",
		"F:TO_DATE(?1, 'YYYY.MM.DD HH24:MI:SS') => CAST(?1 as datetime)",
		
		"F:TO_CHAR(?1) => CONVERT(char, ?1)",
		"F:TO_CHAR(?1, 'dd mon yyyy') => CONVERT(char, ?1, 106)",
		"F:TO_CHAR(?1, 'mm/dd/yyyy') => CONVERT(char, ?1, 101)",
		"F:TO_CHAR(?1, 'yyyy-mm-dd') => CONVERT(char, ?1, 120)",
		"F:TO_CHAR(?1, 'YYYY-MM-DD HH24:MI:SS') => CONVERT(CHAR, ?1, 20)",
		"F:TO_CHAR(?1, 'YYYY-MM-DD') => CONVERT(CHAR, ?1, 23)",
		"F:TO_CHAR(?1, 'YYYY.MM.DD HH24:MI:SS') => CONVERT(CHAR, ?1, 20)",
		"F:TO_CHAR(?1, 'YYYY.MM.DD') => CONVERT(CHAR, ?1, 23)",
		
		"F:ROUND(?1) => ROUND(?1, 0)",
		"F:HEXTORAW(?1) => CONVERT(binary, ?1)",
		"F:RAWTOHEX(?1) => CONVERT(char, ?1)",
		
		"F:MONTHS_BETWEEN(?1, ?2) => DATEDIFF(M, ?2, ?1)",
		"F:ADD_MONTHS(?1, ?2) => DATEADD(M, ?2, ?1)",
		"F:TRUNC(?1) => CAST(CONVERT(VARCHAR, ?1, 23))",
		"O:|| => +",
		"K:sysdate => getdate()",
		"K:LAST_DAY => DATEADD(MM, DATEDIFF(MM,0,GETDATE())+1, 0)+ CAST(CONVERT(VARCHAR, GETDATE(), 108) AS DATETIME)-1",
		"S:from dual =>"
	};

	SqlServerTranslater(){}

	@Override
	protected void doTranslate(StringBuilder sql) {
	}

	@Override
	protected String[] getSqlReplaceExprs() {
		return replace_exps;
	}	
}

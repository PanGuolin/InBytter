#oracle 至 db2的转换引擎
dual=sysibm.sysdummy1
sysdate=current timestamp
to_number(#0)=cast(#0 as integer)
nvl(#0,#1)=coalesce(#0,#1,#0)
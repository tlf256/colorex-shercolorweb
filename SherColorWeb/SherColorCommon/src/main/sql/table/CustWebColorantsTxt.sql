CREATE TABLE SHERCOLOR.CustWebColorantsTxt (	
	CustomerId         varchar2(20)         NOT NULL ENABLE,
	ClrntSysId         varchar2(5)          NOT NULL ENABLE,
	TinterModel        varchar2(30)         NOT NULL ENABLE,
	TinterSerialNbr    varchar2(15)         NOT NULL ENABLE,
	ClrntCode          varchar2(10)         NOT NULL ENABLE,
	Position           number(2,0)          NOT NULL ENABLE,
	MaxCanisterFill    number(9,4)                         ,
	FillAlarmLevel     number(9,4)                         ,
	FillStopLevel      number(9,4)                         ,
	CurrentClrntAmount number(9,4)                         ,
CONSTRAINT CustWebColorantsTxt_pk PRIMARY KEY (CustomerId, ClrntSysId, TinterModel, TinterSerialNbr, ClrntCode, Position));

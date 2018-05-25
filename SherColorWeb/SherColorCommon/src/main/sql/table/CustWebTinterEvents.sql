CREATE TABLE SHERCOLOR.CustWebTinterEvents (
    Guid                varchar2(40)         NOT NULL ENABLE, 
	CustomerId          varchar2(20)         NOT NULL ENABLE,
	ClrntSysId          varchar2(5)          NOT NULL ENABLE,
	TinterModel         varchar2(30)         NOT NULL ENABLE,
	TinterSerialNbr     varchar2(15)         NOT NULL ENABLE,
	DateTime            timestamp(6) with time zone         ,
	Function            varchar2(26)                        ,
	AppVersion          varchar2(7)                         ,
	TinterDriverVersion varchar2(10)                        ,
	EventDetails        varchar2(256)                       ,
	ErrorStatus         varchar2(10)                        ,
	ErrorSeverity       varchar2(10)                        ,
	ErrorNumber         varchar2(10)                        ,
	ErrorMessage        varchar2(242)                       ,
constraint CustWebTinterEvents_pk primary key (Guid));
	
create index CustWebTinterEvents_alt1 on SHERCOLOR.CustWebTinterEvents(CustomerId, ClrntSysId, TinterModel, TinterSerialNbr, Function);

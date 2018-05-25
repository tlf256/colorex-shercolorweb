CREATE TABLE SHERCOLOR.CustWebTinterEventsDetail (
	Guid               varchar2(40)                        ,
	Type               varchar2(25)                        ,
	Name               varchar2(25)                        ,
	Qty                float                               ,
CONSTRAINT CustWebTinterEventsDetail_pk PRIMARY KEY (Guid, Type, Name));


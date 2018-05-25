CREATE TABLE SHERCOLOR.CustWebJobFields (	
	CustomerId        varchar2(20)         NOT NULL ENABLE, 
	SeqNbr            number(3,0)          NOT NULL ENABLE, 
	ScreenLabel       varchar2(15)                        ,
	FieldDefault      varchar2(30)                        ,
	EntryRequired     number(1,0) check (EntryRequired between 0 and 1),
	Active            number(1,0) check (Active between 0 and 1),
CONSTRAINT CustWebJobFields_pk PRIMARY KEY (CustomerId, SeqNbr));

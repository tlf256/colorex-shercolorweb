alter table SHERCOLOR.CustWebTran add QuantityDispensed number(4,0) default 0;

alter table SHERCOLOR.CustWebTran modify ColorName   varchar2(30);

alter table SHERCOLOR.CustWebTran add OrigColorType  varchar2(20);
alter table SHERCOLOR.CustWebTran add OrigColorComp  varchar2(20);
alter table SHERCOLOR.CustWebTran add OrigColorId    varchar2(10);
alter table SHERCOLOR.CustWebTran add OrigFormSource varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigFormMethod varchar2(50);
alter table SHERCOLOR.CustWebTran add OrigClrnt1     varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigClrntAmt1  number(7,0) default 0;
alter table SHERCOLOR.CustWebTran add OrigClrnt2     varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigClrntAmt2  number(7,0) default 0;
alter table SHERCOLOR.CustWebTran add OrigClrnt3     varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigClrntAmt3  number(7,0) default 0;
alter table SHERCOLOR.CustWebTran add OrigClrnt4     varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigClrntAmt4  number(7,0) default 0;
alter table SHERCOLOR.CustWebTran add OrigClrnt5     varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigClrntAmt5  number(7,0) default 0;
alter table SHERCOLOR.CustWebTran add OrigClrnt6     varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigClrntAmt6  number(7,0) default 0;
alter table SHERCOLOR.CustWebTran add OrigClrnt7     varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigClrntAmt7  number(7,0) default 0;
alter table SHERCOLOR.CustWebTran add OrigClrnt8     varchar2(5);
alter table SHERCOLOR.CustWebTran add OrigClrntAmt8  number(7,0) default 0;


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

CREATE TABLE SHERCOLOR.CustWebTinterEventsDetail (
	Guid               varchar2(40)                        ,
	Type               varchar2(25)                        ,
	Name               varchar2(25)                        ,
	Qty                float                               ,
CONSTRAINT CustWebTinterEventsDetail_pk PRIMARY KEY (Guid, Type, Name));

CREATE TABLE SHERCOLOR.CustWebTranCorr (	
	CustomerId        varchar2(20)         NOT NULL ENABLE, 
	ControlNbr        number(7,0)          NOT NULL ENABLE, 
	LineNbr           number(3,0)          NOT NULL ENABLE,
	Cycle             number(3,0)          NOT NULL ENABLE,
	UnitNbr           number(3,0)          NOT NULL ENABLE,
	Step              number(3,0)          NOT NULL ENABLE,
	Reason            varchar2(100)                       , -- Too Red, Too Green, Too Light, Too Dark, etc.
	Status            varchar2(20)                        , -- Open, Accepted, Discarded, Keep Original, etc.
	UserId            varchar2(20)                        ,
	DateTime          timestamp(6) with time zone         ,
	CorrMethod        varchar2(20)                        , -- Manual Adjustment, Percent Adjustment, Color Eye Adjustment
	MergedWithOrig    number(1,0) default 0 check (MergedWithOrig between 0 and 1),
	ClrntSysId        varchar2(5)                         ,
	ShotSize          number(4,0)                         ,
	Clrnt1            varchar2(5)                         ,
	ClrntAmt1         number(7,0)                         ,
	Clrnt2            varchar2(5)                         ,
	ClrntAmt2         number(7,0)                         ,
	Clrnt3            varchar2(5)                         ,
	ClrntAmt3         number(7,0)                         ,
	Clrnt4            varchar2(5)                         ,
	ClrntAmt4         number(7,0)                         ,
	Clrnt5            varchar2(5)                         ,
	ClrntAmt5         number(7,0)                         ,
	Clrnt6            varchar2(5)                         ,
	ClrntAmt6         number(7,0)                         ,
	Clrnt7            varchar2(5)                         ,
	ClrntAmt7         number(7,0)                         ,
	Clrnt8            varchar2(5)                         ,
	ClrntAmt8         number(7,0)                         ,
CONSTRAINT CustWebTranCorr_pk PRIMARY KEY (CustomerId, ControlNbr, LineNbr, Cycle, UnitNbr, Step));

--Added by BKP 20171127
  CREATE TABLE "SHERCOLOR"."CUSTWEBDEVICES" 
   (	"CUSTOMERID" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"SERIALNBR" VARCHAR2(30 BYTE) NOT NULL ENABLE, 
	"DEVICETYPE" VARCHAR2(20 BYTE), 
	"DEVICEMODEL" VARCHAR2(20 BYTE),
	"SEQNBR" NUMBER(38,0) DEFAULT 1
   );

--DJM 20171127
CREATE TABLE "SHERCOLOR"."CUSTWEBECAL" ( 
   	"CUSTOMERID" VARCHAR2(20 BYTE), 
	"COLORANTID" VARCHAR2(5 BYTE), 
	"TINTERMODEL" VARCHAR2(25 BYTE), 
	"TINTERSERIAL" VARCHAR2(15 BYTE), 
	"UPLOADDATE" VARCHAR2(10 BYTE), 
	"UPLOADTIME" VARCHAR2(4 BYTE), 
	"FILENAME" VARCHAR2(50 BYTE), 
	"DATA" BLOB, 
	  PRIMARY KEY ("FILENAME"));

  CREATE INDEX "SHERCOLOR"."CUSTECAL_IDX1" ON "SHERCOLOR"."CUSTWEBECAL" ("CUSTOMERID", "COLORANTID", "TINTERMODEL", "TINTERSERIAL", "UPLOADDATE", "UPLOADTIME");

--LEG files place by BKP 20171127
-- Table CustWebActiveProds

CREATE TABLE "SHERCOLOR"."CUSTWEBACTIVEPRODS" 
   (	"SALESNBR" VARCHAR2(9 BYTE) NOT NULL ENABLE, 
	"PRODNBR" VARCHAR2(9 BYTE) NOT NULL ENABLE, 
	"SIZECD" VARCHAR2(2 BYTE), 
	"GALLONS" NUMBER(9,2), 
	"UPC" VARCHAR2(12 BYTE), 
	"COMMENTS" VARCHAR2(100 BYTE), 
	  PRIMARY KEY ("SALESNBR"));

  CREATE INDEX "SHERCOLOR"."PRODNBR" ON "SHERCOLOR"."CUSTWEBACTIVEPRODS" ("PRODNBR");

  CREATE INDEX "SHERCOLOR"."PRODSIZE" ON "SHERCOLOR"."CUSTWEBACTIVEPRODS" ("PRODNBR", "SIZECD");

-- Table CustWebDealer
CREATE TABLE "SHERCOLOR"."CUSTWEBDEALER" 
   (	"CUSTOMERID" VARCHAR2(9 BYTE) NOT NULL ENABLE, 
	"DEALERNAME" VARCHAR2(30 BYTE), 
	"DLRPHONENBR" VARCHAR2(20 BYTE), 
	"DLRHOMESTORE" NUMBER(*,0), 
	"COMMENTS" VARCHAR2(100 BYTE), 
	  PRIMARY KEY ("CUSTOMERID"));

  CREATE INDEX "SHERCOLOR"."DEALERNAME" ON "SHERCOLOR"."CUSTWEBDEALER" ("DEALERNAME");

  CREATE INDEX "SHERCOLOR"."DLRPHONENBR" ON "SHERCOLOR"."CUSTWEBDEALER" ("DLRPHONENBR");

-- Table CustWebDealerCust
 CREATE TABLE "SHERCOLOR"."CUSTWEBDEALERCUST" 
   (	"CUSTOMERID" VARCHAR2(9 BYTE) NOT NULL ENABLE, 
	"DLRCUSTID" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"DLRCUSTNAME" VARCHAR2(30 BYTE), 
	"DLRCUSTADDRESS" VARCHAR2(30 BYTE), 
	"DLRCUSTCITY" VARCHAR2(20 BYTE), 
	"DLRCUSTSTATE" VARCHAR2(2 BYTE), 
	"DLRCUSTZIP" VARCHAR2(6 BYTE), 
	"DLRCUSTCOUNTRY" VARCHAR2(20 BYTE), 
	"DLRCUSTPHONENBR" VARCHAR2(20 BYTE), 
	"DLRCUSTCONTACT" VARCHAR2(30 BYTE), 
	"DATEADDED" DATE, 
	"DATEUPDATED" DATE, 
	"UPDATEUSER" VARCHAR2(6 BYTE), 
	"COMMENTS" VARCHAR2(100 BYTE), 
	  PRIMARY KEY ("CUSTOMERID", "DLRCUSTID"));

  CREATE INDEX "SHERCOLOR"."CUSTCITY" ON "SHERCOLOR"."CUSTWEBDEALERCUST" ("CUSTOMERID", "DLRCUSTCITY");

  CREATE INDEX "SHERCOLOR"."CUSTCONTACT" ON "SHERCOLOR"."CUSTWEBDEALERCUST" ("CUSTOMERID", "DLRCUSTCONTACT");

  CREATE INDEX "SHERCOLOR"."CUSTNAME" ON "SHERCOLOR"."CUSTWEBDEALERCUST" ("CUSTOMERID", "DLRCUSTNAME");

  CREATE INDEX "SHERCOLOR"."CUSTPHONENBR" ON "SHERCOLOR"."CUSTWEBDEALERCUST" ("CUSTOMERID", "DLRCUSTPHONENBR");

  CREATE INDEX "SHERCOLOR"."CUSTSTATE" ON "SHERCOLOR"."CUSTWEBDEALERCUST" ("CUSTOMERID", "DLRCUSTSTATE");

-- Table CustWebDealerCustOrd
 CREATE TABLE "SHERCOLOR"."CUSTWEBDEALERCUSTORD" 
   (	"CUSTOMERID" VARCHAR2(9 BYTE) NOT NULL ENABLE, 
	"DLRCUSTID" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"CONTROLNBR" NUMBER(*,0), 
	"CUSTORDERNBR" VARCHAR2(20 BYTE), 
	"DATEADDED" DATE, 
	"DATEUPDATED" DATE, 
	"UPDATEUSER" VARCHAR2(6 BYTE), 
	"COMMENTS" VARCHAR2(100 BYTE), 
	  PRIMARY KEY ("CUSTOMERID", "DLRCUSTID", "CONTROLNBR"));

  CREATE INDEX "SHERCOLOR"."CUSTORDERNBR" ON "SHERCOLOR"."CUSTWEBDEALERCUSTORD" ("CUSTOMERID", "DLRCUSTID", "CUSTORDERNBR");




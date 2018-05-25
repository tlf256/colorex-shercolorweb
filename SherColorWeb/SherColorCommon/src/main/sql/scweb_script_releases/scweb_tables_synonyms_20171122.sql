--------------------------------------------------------
--  Create synonyms and set permissions for CustWebColorantsTxt
--------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CustWebColorantsTxt FOR SHERCOLOR.CustWebColorantsTxt;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CustWebColorantsTxt FOR SHERCOLOR.CustWebColorantsTxt;
GRANT SELECT ON SHERCOLOR.CustWebColorantsTxt TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CustWebColorantsTxt TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CustWebTinterEvents
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CustWebTinterEvents FOR SHERCOLOR.CustWebTinterEvents;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CustWebTinterEvents FOR SHERCOLOR.CustWebTinterEvents;
GRANT SELECT ON SHERCOLOR.CustWebTinterEvents TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CustWebTinterEvents TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CustWebTinterEventsDetail
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CustWebTinterEventsDetail FOR SHERCOLOR.CustWebTinterEventsDetail;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CustWebTinterEventsDetail FOR SHERCOLOR.CustWebTinterEventsDetail;
GRANT SELECT ON SHERCOLOR.CustWebTinterEventsDetail TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CustWebTinterEventsDetail TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CustWebTranCorr
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CustWebTranCorr FOR SHERCOLOR.CustWebTranCorr;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CustWebTranCorr FOR SHERCOLOR.CustWebTranCorr;
GRANT SELECT ON SHERCOLOR.CustWebTranCorr TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CustWebTranCorr TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CUSTWEBDEVICES
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CUSTWEBDEVICES FOR SHERCOLOR.CUSTWEBDEVICES;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CUSTWEBDEVICES FOR SHERCOLOR.CUSTWEBDEVICES;
GRANT SELECT ON SHERCOLOR.CUSTWEBDEVICES TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CUSTWEBDEVICES TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CUSTWEBECAL
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CUSTWEBECAL FOR SHERCOLOR.CUSTWEBECAL;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CUSTWEBECAL FOR SHERCOLOR.CUSTWEBECAL;
GRANT SELECT ON SHERCOLOR.CUSTWEBECAL TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CUSTWEBECAL TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CUSTWEBACTIVEPRODS
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CUSTWEBACTIVEPRODS FOR SHERCOLOR.CUSTWEBACTIVEPRODS;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CUSTWEBACTIVEPRODS FOR SHERCOLOR.CUSTWEBACTIVEPRODS;
GRANT SELECT ON SHERCOLOR.CUSTWEBACTIVEPRODS TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CUSTWEBACTIVEPRODS TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CUSTWEBDEALER
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CUSTWEBDEALER FOR SHERCOLOR.CUSTWEBDEALER;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CUSTWEBDEALER FOR SHERCOLOR.CUSTWEBDEALER;
GRANT SELECT ON SHERCOLOR.CUSTWEBDEALER TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CUSTWEBDEALER TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CUSTWEBDEALERCUST
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CUSTWEBDEALERCUST FOR SHERCOLOR.CUSTWEBDEALERCUST;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CUSTWEBDEALERCUST FOR SHERCOLOR.CUSTWEBDEALERCUST;
GRANT SELECT ON SHERCOLOR.CUSTWEBDEALERCUST TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CUSTWEBDEALERCUST TO SHERCOLOR_USER;

----------------------------------------------------------------------
--  Create synonyms and set permissions for CUSTWEBDEALERCUSTORD
----------------------------------------------------------------------
CREATE OR REPLACE SYNONYM SHERCOLOR_RO.CUSTWEBDEALERCUSTORD FOR SHERCOLOR.CUSTWEBDEALERCUSTORD;
CREATE OR REPLACE SYNONYM SHERCOLOR_USER.CUSTWEBDEALERCUSTORD FOR SHERCOLOR.CUSTWEBDEALERCUSTORD;
GRANT SELECT ON SHERCOLOR.CUSTWEBDEALERCUSTORD TO SHERCOLOR_RO;
GRANT DELETE, INSERT, SELECT, UPDATE ON SHERCOLOR.CUSTWEBDEALERCUSTORD TO SHERCOLOR_USER;



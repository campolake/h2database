-- Copyright 2004-2018 H2 Group. Multiple-Licensed under the MPL 2.0,
-- and the EPL 1.0 (http://h2database.com/html/license.html).
-- Initial Developer: H2 Group
--

SELECT CAST(CURRENT_TIMESTAMP AS TIMESTAMP(9)) = LOCALTIMESTAMP;
>> TRUE

SELECT CAST(CURRENT_TIMESTAMP(0) AS TIMESTAMP(9)) = LOCALTIMESTAMP(0);
>> TRUE

SELECT CAST(CURRENT_TIMESTAMP(9) AS TIMESTAMP(9)) = LOCALTIMESTAMP(9);
>> TRUE

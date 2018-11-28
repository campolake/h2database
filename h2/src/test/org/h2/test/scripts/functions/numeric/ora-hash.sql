-- Copyright 2004-2018 H2 Group. Multiple-Licensed under the MPL 2.0,
-- and the EPL 1.0 (http://h2database.com/html/license.html).
-- Initial Developer: H2 Group
--

SELECT ORA_HASH(NULL);
>> null

SELECT ORA_HASH(NULL, 0);
>> null

SELECT ORA_HASH(NULL, 0, 0);
>> null

SELECT ORA_HASH(1);
>> 3509391659

SELECT ORA_HASH(1, -1);
> exception INVALID_VALUE_2

SELECT ORA_HASH(1, 0);
>> 0

SELECT ORA_HASH(1, 4294967295);
>> 3509391659

SELECT ORA_HASH(1, 4294967296);
> exception INVALID_VALUE_2

SELECT ORA_HASH(1, 4294967295, -1);
> exception INVALID_VALUE_2

SELECT ORA_HASH(1, 4294967295, 0);
>> 3509391659

SELECT ORA_HASH(1, 4294967295, 10);
>> 2441322222

SELECT ORA_HASH(1, 4294967295, 4294967295);
>> 3501171530

SELECT ORA_HASH(1, 4294967295, 4294967296);
> exception INVALID_VALUE_2

CREATE TABLE TEST(I BINARY, B BLOB, S VARCHAR, C CLOB);
> ok

INSERT INTO TEST VALUES ('010203', '010203', 'abc', 'abc');
> update count: 1

SELECT ORA_HASH(I) FROM TEST;
>> 2562861693

SELECT ORA_HASH(B) FROM TEST;
>> 2562861693

SELECT ORA_HASH(S) FROM TEST;
>> 1191608682

SELECT ORA_HASH(C) FROM TEST;
>> 1191608682

DROP TABLE TEST;
> ok

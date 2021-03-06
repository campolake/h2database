/*
 * Copyright 2004-2018 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.util;

public class ParserUtil {

    /**
     * A keyword.
     */
    public static final int KEYWORD = 1;

    /**
     * An identifier (table name, column name,...).
     */
    public static final int IDENTIFIER = 2;

    /**
     * The token "ALL".
     */
    public static final int ALL = IDENTIFIER + 1;

    /**
     * The token "CHECK".
     */
    public static final int CHECK = ALL + 1;

    /**
     * The token "CONSTRAINT".
     */
    public static final int CONSTRAINT = CHECK + 1;

    /**
     * The token "CROSS".
     */
    public static final int CROSS = CONSTRAINT + 1;

    /**
     * The token "CURRENT_DATE".
     */
    public static final int CURRENT_DATE = CROSS + 1;

    /**
     * The token "CURRENT_TIME".
     */
    public static final int CURRENT_TIME = CURRENT_DATE + 1;

    /**
     * The token "CURRENT_TIMESTAMP".
     */
    public static final int CURRENT_TIMESTAMP = CURRENT_TIME + 1;

    /**
     * The token "DISTINCT".
     */
    public static final int DISTINCT = CURRENT_TIMESTAMP + 1;

    /**
     * The token "EXCEPT".
     */
    public static final int EXCEPT = DISTINCT + 1;

    /**
     * The token "EXISTS".
     */
    public static final int EXISTS = EXCEPT + 1;

    /**
     * The token "FALSE".
     */
    public static final int FALSE = EXISTS + 1;

    /**
     * The token "FETCH".
     */
    public static final int FETCH = FALSE + 1;

    /**
     * The token "FOR".
     */
    public static final int FOR = FETCH + 1;

    /**
     * The token "FOREIGN".
     */
    public static final int FOREIGN = FOR + 1;

    /**
     * The token "FROM".
     */
    public static final int FROM = FOREIGN + 1;

    /**
     * The token "FULL".
     */
    public static final int FULL = FROM + 1;

    /**
     * The token "GROUP".
     */
    public static final int GROUP = FULL + 1;

    /**
     * The token "HAVING".
     */
    public static final int HAVING = GROUP + 1;

    /**
     * The token "INNER".
     */
    public static final int INNER = HAVING + 1;

    /**
     * The token "INTERSECT".
     */
    public static final int INTERSECT = INNER + 1;

    /**
     * The token "IS".
     */
    public static final int IS = INTERSECT + 1;

    /**
     * The token "JOIN".
     */
    public static final int JOIN = IS + 1;

    /**
     * The token "LIKE".
     */
    public static final int LIKE = JOIN + 1;

    /**
     * The token "LIMIT".
     */
    public static final int LIMIT = LIKE + 1;

    /**
     * The token "MINUS".
     */
    public static final int MINUS = LIMIT + 1;

    /**
     * The token "NATURAL".
     */
    public static final int NATURAL = MINUS + 1;

    /**
     * The token "NOT".
     */
    public static final int NOT = NATURAL + 1;

    /**
     * The token "NULL".
     */
    public static final int NULL = NOT + 1;

    /**
     * The token "OFFSET".
     */
    public static final int OFFSET = NULL + 1;

    /**
     * The token "ON".
     */
    public static final int ON = OFFSET + 1;

    /**
     * The token "ORDER".
     */
    public static final int ORDER = ON + 1;

    /**
     * The token "PRIMARY".
     */
    public static final int PRIMARY = ORDER + 1;

    /**
     * The token "ROWNUM".
     */
    public static final int ROWNUM = PRIMARY + 1;

    /**
     * The token "SELECT".
     */
    public static final int SELECT = ROWNUM + 1;

    /**
     * The token "TRUE".
     */
    public static final int TRUE = SELECT + 1;

    /**
     * The token "UNION".
     */
    public static final int UNION = TRUE + 1;

    /**
     * The token "UNIQUE".
     */
    public static final int UNIQUE = UNION + 1;

    /**
     * The token "WHERE".
     */
    public static final int WHERE = UNIQUE + 1;

    /**
     * The token "WITH".
     */
    public static final int WITH = WHERE + 1;

    private static final int UPPER_OR_OTHER_LETTER =
            1 << Character.UPPERCASE_LETTER
            | 1 << Character.TITLECASE_LETTER
            | 1 << Character.MODIFIER_LETTER
            | 1 << Character.OTHER_LETTER;

    private static final int UPPER_OR_OTHER_LETTER_OR_DIGIT =
            UPPER_OR_OTHER_LETTER
            | 1 << Character.DECIMAL_DIGIT_NUMBER;

    private ParserUtil() {
        // utility class
    }

    /**
     * Checks if this string is a SQL keyword.
     *
     * @param s the token to check
     * @return true if it is a keyword
     */
    public static boolean isKeyword(String s) {
        if (s.length() == 0) {
            return false;
        }
        return getSaveTokenType(s, false) != IDENTIFIER;
    }

    /**
     * Is this a simple identifier (in the JDBC specification sense).
     *
     * @param s identifier to check
     * @return is specified identifier may be used without quotes
     * @throws NullPointerException if s is {@code null}
     */
    public static boolean isSimpleIdentifier(String s) {
        if (s.length() == 0) {
            return false;
        }
        char c = s.charAt(0);
        // lowercase a-z is quoted as well
        if ((UPPER_OR_OTHER_LETTER >>> Character.getType(c) & 1) == 0 && c != '_') {
            return false;
        }
        for (int i = 1, length = s.length(); i < length; i++) {
            c = s.charAt(i);
            if ((UPPER_OR_OTHER_LETTER_OR_DIGIT >>> Character.getType(c) & 1) == 0 && c != '_') {
                return false;
            }
        }
        return getSaveTokenType(s, true) == IDENTIFIER;
    }

    /**
     * Get the token type.
     *
     * @param s the token
     * @param additionalKeywords whether TOP, INTERSECTS, and "current data /
     *                           time" functions are keywords
     * @return the token type
     */
    public static int getSaveTokenType(String s, boolean additionalKeywords) {
        /*
         * JdbcDatabaseMetaData.getSQLKeywords() and tests should be updated when new
         * non-SQL:2003 keywords are introduced here.
         */
        switch (s.charAt(0)) {
        case 'A':
            if ("ALL".equals(s)) {
                return ALL;
            }
            return IDENTIFIER;
        case 'C':
            if ("CHECK".equals(s)) {
                return CHECK;
            } else if ("CONSTRAINT".equals(s)) {
                return CONSTRAINT;
            } else if ("CROSS".equals(s)) {
                return CROSS;
            }
            if (additionalKeywords) {
                if ("CURRENT_DATE".equals(s) || "CURRENT_TIME".equals(s) || "CURRENT_TIMESTAMP".equals(s)) {
                    return KEYWORD;
                }
            }
            return IDENTIFIER;
        case 'D':
            if ("DISTINCT".equals(s)) {
                return DISTINCT;
            }
            return IDENTIFIER;
        case 'E':
            if ("EXCEPT".equals(s)) {
                return EXCEPT;
            } else if ("EXISTS".equals(s)) {
                return EXISTS;
            }
            return IDENTIFIER;
        case 'F':
            if ("FETCH".equals(s)) {
                return FETCH;
            } else if ("FROM".equals(s)) {
                return FROM;
            } else if ("FOR".equals(s)) {
                return FOR;
            } else if ("FOREIGN".equals(s)) {
                return FOREIGN;
            } else if ("FULL".equals(s)) {
                return FULL;
            } else if ("FALSE".equals(s)) {
                return FALSE;
            }
            return IDENTIFIER;
        case 'G':
            if ("GROUP".equals(s)) {
                return GROUP;
            }
            return IDENTIFIER;
        case 'H':
            if ("HAVING".equals(s)) {
                return HAVING;
            }
            return IDENTIFIER;
        case 'I':
            if ("INNER".equals(s)) {
                return INNER;
            } else if ("INTERSECT".equals(s)) {
                return INTERSECT;
            } else if ("IS".equals(s)) {
                return IS;
            }
            if (additionalKeywords) {
                if ("INTERSECTS".equals(s)) {
                    return KEYWORD;
                }
            }
            return IDENTIFIER;
        case 'J':
            if ("JOIN".equals(s)) {
                return JOIN;
            }
            return IDENTIFIER;
        case 'L':
            if ("LIMIT".equals(s)) {
                return LIMIT;
            } else if ("LIKE".equals(s)) {
                return LIKE;
            }
            if (additionalKeywords) {
                if ("LOCALTIME".equals(s) || "LOCALTIMESTAMP".equals(s)) {
                    return KEYWORD;
                }
            }
            return IDENTIFIER;
        case 'M':
            if ("MINUS".equals(s)) {
                return MINUS;
            }
            return IDENTIFIER;
        case 'N':
            if ("NOT".equals(s)) {
                return NOT;
            } else if ("NATURAL".equals(s)) {
                return NATURAL;
            } else if ("NULL".equals(s)) {
                return NULL;
            }
            return IDENTIFIER;
        case 'O':
            if ("OFFSET".equals(s)) {
                return OFFSET;
            } else if ("ON".equals(s)) {
                return ON;
            } else if ("ORDER".equals(s)) {
                return ORDER;
            }
            return IDENTIFIER;
        case 'P':
            if ("PRIMARY".equals(s)) {
                return PRIMARY;
            }
            return IDENTIFIER;
        case 'R':
            if ("ROWNUM".equals(s)) {
                return ROWNUM;
            }
            return IDENTIFIER;
        case 'S':
            if ("SELECT".equals(s)) {
                return SELECT;
            }
            if (additionalKeywords) {
                if ("SYSDATE".equals(s) || "SYSTIME".equals(s) || "SYSTIMESTAMP".equals(s)) {
                    return KEYWORD;
                }
            }
            return IDENTIFIER;
        case 'T':
            if ("TRUE".equals(s)) {
                return TRUE;
            }
            if (additionalKeywords) {
                if ("TODAY".equals(s) || "TOP".equals(s)) {
                    return KEYWORD;
                }
            }
            return IDENTIFIER;
        case 'U':
            if ("UNIQUE".equals(s)) {
                return UNIQUE;
            } else if ("UNION".equals(s)) {
                return UNION;
            }
            return IDENTIFIER;
        case 'W':
            if ("WITH".equals(s)) {
                return WITH;
            } else if ("WHERE".equals(s)) {
                return WHERE;
            }
            return IDENTIFIER;
        default:
            return IDENTIFIER;
        }
    }

}

package datamodel;

/**
 * Used to specify the parameter type for the query param.
 * <pre>
 * If there is a mismatch while running the query.
 * Cross the type passed in config with the type from
 * Types
 * Represents the data types from{@link  java.sql.Types}
 * </pre>
 */
public enum Type {

  ARRAY(2003),
  BIGINT(-5),
  BINARY(-2),
  BIT(-7),
  BLOB(2004),
  BOOLEAN(16),
  CHAR(1),
  CLOB(2005),
  DATE(91),
  DECIMAL(3),
  DISTINCT(2001),
  DOUBLE(8),
  FLOAT(6),
  INTEGER(4),
  JAVA_OBJECT(2000),
  LONGNVARCHAR(-16),
  LONGVARBINARY(-4),
  LONGVARCHAR(-1),
  NCHAR(-15),
  NCLOB(2011),
  NULL(0),
  NUMERIC(2),
  NVARCHAR(-9),
  OTHER(1111),
  REAL(7),
  REF(2006),
  REF_CURSOR(2012),
  ROWID(-8),
  SMALLINT(5),
  SQLXML(2009),
  STRUCT(2002),
  TIME(92),
  TIME_WITH_TIMEZONE(2013),
  TIMESTAMP(93),
  TIMESTAMP_WITH_TIMEZONE(2014),
  TINYINT(-6),
  VARBINARY(-3),
  VARCHAR(12);

  private int value;


  Type(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }


}

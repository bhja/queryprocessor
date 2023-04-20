package datamodel;

import org.immutables.value.Value.Immutable;

/**
 * name - Name of the column/parameter in case of stored procedure. Required with respect to the
 * resolved Query value - Parameter value to set position - Position to substitute the parameter
 * type - Sql data type of the parameter to run the query. Refer {@link Type} for mapping
 * <pre>
 * Eg 1: A typical prepared statement request would look like this
 * sql: select * from employee where id = ? and name = ?
 * queryParams: [{
 *   name: id,
 *   position :1,
 *   value : 13453,
 *   type : Type.INTEGER
 * },
 * {
 *   name:name,
 *   position: 2,
 *   value : Joe,
 *   type : Type.VARCHAR
 * }]
 *
 * </pre>
 */
@Immutable
public interface QueryParam {

  String name();

  Object value();

  Integer position();

  Type type();


}

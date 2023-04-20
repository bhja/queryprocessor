package datamodel;

import java.util.List;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

/**
 * Class represents the sybase query definition . See {@link QueryParam} for query mapping example
 */
@Immutable
public interface SybaseQuery extends BaseQuery {

  String sql();

  List<QueryParam> queryParams();

  @Default
  default boolean isCallableStmt() {
    return false;
  }


}

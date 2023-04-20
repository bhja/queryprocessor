package datamodel;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
public interface Query {

  @Nullable
  MongoQuery mongoQuery();

  @Nullable
  SybaseQuery sybaseQuery();

  String name();

  String value();

  List<String> label();

  QueryResultType resultType();

  @Value.Check
  default void check() {
    Preconditions.checkState(!(mongoQuery() == null && sybaseQuery() == null),
        "One of them is required to continue with querying");
  }

}

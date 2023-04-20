package datamodel;

import com.google.gson.JsonObject;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public interface QueryResult {

  Query query();

  List<JsonObject> result();

  String executedQuery();
}

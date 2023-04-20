package org.bh;

import com.google.gson.JsonObject;
import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
public interface SybaseQueryResults {

  String resolvedQuery();

  List<JsonObject> results();

}

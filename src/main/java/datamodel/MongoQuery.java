package datamodel;

import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
public interface MongoQuery extends BaseQuery {

  String collection();

  String queryString();


  List<String> relativeDates();

  QueryType type();


}

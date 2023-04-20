package org.bh;

import datamodel.QueryParam;
import io.reactivex.Single;
import java.util.List;

public class Processor {

  StandardSybaseClient client;

  public Processor() {
    client = new StandardSybaseClient();
  }

  public SybaseQueryResults runQuery(String query, List<QueryParam> params, boolean isCallableStmt)
      throws Exception {
    Single<SybaseQueryResults> resultsSingle = Single.fromFuture(
        client.execSybaseQuery(query, params, isCallableStmt)).to(v -> v);
    return resultsSingle.blockingGet();
  }


}

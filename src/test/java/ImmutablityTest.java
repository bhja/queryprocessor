import datamodel.ImmutableMongoQuery;
import datamodel.ImmutableQuery;
import datamodel.ImmutableQueryParam;
import datamodel.ImmutableSybaseQuery;
import datamodel.MongoQuery;
import datamodel.Query;
import datamodel.QueryParam;
import datamodel.QueryResultType;
import datamodel.QueryType;
import datamodel.SybaseQuery;
import datamodel.Type;
import java.util.ArrayList;
import java.util.Collections;
import org.bh.Processor;
import org.bh.SybaseQueryResults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ImmutablityTest {

  @Test
  public void testQueryBuilder() throws IllegalStateException {
    QueryResultType r = QueryResultType.metrics;
    Assertions.assertThrows(IllegalStateException.class,
        () -> ImmutableQuery.builder().name("test").resultType(r).addLabel("test").value("v")
            .build());
  }

  @Test
  public void testMongoQuery() {

    MongoQuery mongoQuery = ImmutableMongoQuery.builder().queryString("test")
        .type(QueryType.aggregate).collection("name").build();
    QueryResultType r = QueryResultType.metrics;
    Query q = ImmutableQuery.builder().name("test").mongoQuery(mongoQuery).resultType(r)
        .addLabel("test").value("v").build();

  }

  @Test
  public void testPlain() throws Exception {
    SybaseQuery query = ImmutableSybaseQuery.builder()
        .sql("SELECT * from model.dbo.TEST").queryParams(new ArrayList<>())
        .build();

    //  Vertx vertx = Vertx.vertx();
    //  Processor processor = new Processor(vertx);
    Processor processor = new Processor();
    SybaseQueryResults queryResults = processor.runQuery(query.sql(), query.queryParams(), false);
    Assertions.assertNotNull(queryResults.results());
    System.out.println(queryResults);

  }

  @Test
  public void testPrepareStmt0() throws Exception {

    QueryParam param = ImmutableQueryParam.builder().name("id").value(1).type(Type.INTEGER)
        .position(1).build();
    SybaseQuery query = ImmutableSybaseQuery.builder()
        .sql("SELECT * from model.dbo.TEST where id in(?) ")
        .queryParams(Collections.singleton(param))
        .build();

    Processor processor = new Processor();
    SybaseQueryResults results = processor.runQuery(query.sql(), query.queryParams(), false);
    Assertions.assertNotNull(results);
  }


  @Test
  public void testSPWithParam() throws Exception {
    QueryParam param = ImmutableQueryParam.builder().name("id").value(1).type(Type.INTEGER)
        .position(1).build();

    SybaseQuery query = ImmutableSybaseQuery.builder().sql("{ call model.dbo.sp_test(?) }")
        .isCallableStmt(true).queryParams(Collections.singleton(param)).build();
    Processor processor = new Processor();
    SybaseQueryResults results = processor.runQuery(query.sql(), query.queryParams(),
        query.isCallableStmt());
    System.out.println(results);
  }

  @Test
  public void testSPWithoutParam() throws Exception {
    SybaseQuery query = ImmutableSybaseQuery.builder()
        .sql("{ call model.dbo.sp__cpu_busy_thread_test() }").isCallableStmt(true)
        .queryParams(new ArrayList<>()).build();
    Processor processor = new Processor();
    SybaseQueryResults results = processor.runQuery(query.sql(), query.queryParams(),
        query.isCallableStmt());
    System.out.println(results);
  }
}



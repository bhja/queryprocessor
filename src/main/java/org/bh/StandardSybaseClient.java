package org.bh;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import datamodel.QueryParam;
import datamodel.Type;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardSybaseClient {

  private Logger log = LoggerFactory.getLogger(StandardSybaseClient.class);

  private DataSource dataSource;


  public StandardSybaseClient() {

  }

  public DataSource getDataSource() throws Exception {
    if (dataSource == null) {
      Properties properties = loadProperties();
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(properties.getProperty("datasource.jdbcUrl"));
      config.setUsername(properties.getProperty("datasource.userName"));
      config.setPassword(properties.getProperty("datasource.password"));
      config.setPoolName(properties.getProperty("datasource.poolName", "sybase-metrics-pool"));
      config.setMaximumPoolSize(
          Integer.valueOf(properties.getProperty("datasource.maxPoolSize", "10")));
      config.setMaximumPoolSize(
          Integer.valueOf(properties.getProperty("datasource.minPoolSize", "5")));
      config.setMinimumIdle(Integer.valueOf(properties.getProperty("datasource.minIdle", "1")));

      dataSource = new HikariDataSource(config);
    }
    return dataSource;
  }


  public JsonObject getRow(ResultSetMetaData metaData, ResultSet resultSet) {

    JsonObject object = new JsonObject();
    try {
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        String name = metaData.getColumnName(i);
        int type = metaData.getColumnType(i);
        Object value = resultSet.getObject(name);
        if (value != null) {
          setResultByType(name, object, type, value);
        } else {
          object.add(name, JsonNull.INSTANCE);
        }
      }

    } catch (Exception e) {
      log.error("Could not fetch the column" + e.getMessage());
    }

    return object;
  }

  /**
   * Returns {@link SybaseQueryResults}
   *
   * @param query
   * @param params {@link QueryParam}
   * @return
   * @throws Exception
   */

  public CompletableFuture<SybaseQueryResults> execSybaseQuery(String query,
      List<QueryParam> params,
      boolean isCallable)
  {

    return CompletableFuture.supplyAsync(()-> {
      Connection connection = null;

      ImmutableSybaseQueryResults.Builder queryResults = ImmutableSybaseQueryResults.builder();
      List<JsonObject> results = new ArrayList<>();
      Statement statement = null;
      try {
        connection = getDataSource().getConnection();
        ResultSet resultSet;
        if (isCallable) {
          statement = connection.prepareCall(query);
          setParameters(connection, statement, params);
          resultSet = ((CallableStatement) statement).executeQuery();
        } else {
          if (params == null || params.isEmpty()) {
            queryResults.resolvedQuery(query);
            resultSet = connection.createStatement().executeQuery(query);
          } else {
            statement = connection.prepareStatement(query);
            setParameters(connection, statement, params);
            //TODO - Executed query cannot be obtained. We can only print it out.
            resultSet = ((PreparedStatement) statement).executeQuery();
          }
        }
        queryResults.resolvedQuery(buildQueryDetails(query, params));
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
          results.add(getRow(metaData, resultSet));
        }
      } catch (Exception ex) {
        throw new RuntimeException(
            "Could not execute the query due to [ " + ex.getMessage() + " ] ");
      } finally {

        try{ if (statement != null) {
          statement.close();
        }
        if (connection != null) {
            connection.close();
          }} catch (SQLException e) {
            throw new RuntimeException(e);
        }
      }
      queryResults.results(results);
        return queryResults.build();
    });
  }


  /**
   * ADD all the datatypes that have to be handled.
   *
   * @param type
   * @return
   */
  public int typeOf(Type type) {
    return Type.valueOf(type.name()).getValue();
  }

  protected String buildQueryDetails(String query, List<QueryParam> params) {
    StringBuilder builder = new StringBuilder();
    builder.append("{QUERY=").append(query).append("}").append("\n");
    if (!params.isEmpty()) {
      builder.append("PARAMETERS=")
          .append(params).append("}");

    }
    return builder.toString();
  }

  protected void setParameters(Connection connection, Statement statement,
      List<QueryParam> params) {
    if (statement instanceof CallableStatement) {
      params.forEach(param -> {
        try {
          ((CallableStatement) statement).setObject(param.position(), param.value(),
              typeOf(param.type()));
        } catch (Exception e) {
          throw new RuntimeException(
              "Could not set the parameter for the query due to issue " + e.getMessage());
        }
      });
    } else if (statement instanceof PreparedStatement) {
      params.forEach(param -> {
        try {

          ((PreparedStatement) statement).setObject(param.position(), param.value(),
              typeOf(param.type()));

        } catch (Exception e) {
          throw new RuntimeException(
              "Could not set the parameter for the query due to issue " + e.getMessage());
        }
      });
    }
  }

  protected void setResultByType(String name, JsonObject jsonObject, int type, Object value) {
    switch (type) {
      case 4:
        jsonObject.addProperty(name, (int) (value));
        break;
      default:
        jsonObject.addProperty(name, String.valueOf(value));

    }
  }

  private Properties loadProperties() throws Exception {
    Properties prop = new Properties();
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("datasource.properties");
    prop.load(inputStream);
    return prop;
  }


}






package org.bh;


import datamodel.ImmutableQueryParam;
import datamodel.QueryParam;
import datamodel.Type;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainClass {

  public static void main(String... a) throws Exception {
    Logger logger  = LoggerFactory.getLogger(MainClass.class);
    Processor processor = new Processor();

    QueryParam param = ImmutableQueryParam.builder().name("id").position(1).value(1).type(
        Type.INTEGER).build();

    SybaseQueryResults result = processor.runQuery("select * from model.dbo.TEST where id=?",
        Collections.singletonList(param), false);
    logger.info("%s",result);
  }


}

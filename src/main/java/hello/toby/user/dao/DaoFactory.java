package hello.toby.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {

  @Bean
  public UserDaoJdbc userDao() throws ClassNotFoundException {
    UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
    userDaoJdbc.setDataSource(dataSource());
    return userDaoJdbc;
  }

 /* @Bean
  public JdbcContext jdbcContext() throws ClassNotFoundException {
    JdbcContext jdbcContext = new JdbcContext();
    jdbcContext.setDataSource(dataSource());
    return jdbcContext;
  }*/

  @Bean
  public ConnectionMaker connectionMaker() {
    return new DConnectionMaker();
  }

  @Bean
  public DataSource dataSource() throws ClassNotFoundException {

    Class driverClass = Class.forName("com.mysql.jdbc.Driver");

    SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
    simpleDriverDataSource.setDriverClass(driverClass);
    simpleDriverDataSource.setUrl("jdbc:mysql://localhost/springbook");
    simpleDriverDataSource.setUsername("root");
    simpleDriverDataSource.setPassword("1234");
    return simpleDriverDataSource;
  }
}

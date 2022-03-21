package hello.toby.user.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() throws ClassNotFoundException {
    UserDao userDao = new UserDao();
    return userDao;
  }

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

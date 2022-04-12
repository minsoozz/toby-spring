package hello.toby.user.dao;

import hello.toby.mail.DummyMailSender;
import hello.toby.user.service.UserService;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;

@Configuration
public class DaoFactory {

  @Bean
  public UserDaoJdbc userDao() throws ClassNotFoundException {
    UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
    userDaoJdbc.setDataSource(dataSource());
    userDaoJdbc.setJdbcContext(jdbcContext());
    return userDaoJdbc;
  }

  @Bean
  public UserService userService() throws ClassNotFoundException {
    UserService userService = new UserService();
    userService.setUserDao(userDao());
    userService.setDataSource(dataSource());
    return userService;
  }

  @Bean
  public JdbcContext jdbcContext() throws ClassNotFoundException {
    JdbcContext jdbcContext = new JdbcContext();
    jdbcContext.setDataSource(dataSource());
    return jdbcContext;
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

  @Bean
  public DataSourceTransactionManager transactionManager() throws ClassNotFoundException {
    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
    dataSourceTransactionManager.setDataSource(dataSource());
    return dataSourceTransactionManager;
  }

  @Bean
  public MailSender mailSender() {
    return new DummyMailSender();
  }
}

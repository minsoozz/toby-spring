package hello.toby.config;

import hello.toby.mail.DummyMailSender;
import hello.toby.message.MessageFactoryBean;
import hello.toby.user.dao.ConnectionMaker;
import hello.toby.user.dao.DConnectionMaker;
import hello.toby.user.dao.JdbcContext;
import hello.toby.user.dao.UserDaoJdbc;
import hello.toby.user.service.TxProxyFactoryBean;
import hello.toby.user.service.UserService;
import hello.toby.user.service.UserServiceImpl;
import hello.toby.user.service.UserServiceTx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;

import javax.sql.DataSource;

@Configuration
public class BeanConfig {

    @Bean
    public UserDaoJdbc userDao() throws ClassNotFoundException {
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        userDaoJdbc.setDataSource(dataSource());
        userDaoJdbc.setJdbcContext(jdbcContext());
        return userDaoJdbc;
    }

    @Bean
    public TxProxyFactoryBean userService() throws ClassNotFoundException {
        TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
        txProxyFactoryBean.setTarget(userServiceImpl());
        txProxyFactoryBean.setTransactionManager(transactionManager());
        txProxyFactoryBean.setPattern("upgradeLevels");
        txProxyFactoryBean.setServiceInterface(Class.forName("hello.toby.user.service.UserService"));
        return txProxyFactoryBean;
    }

    @Bean
    public UserServiceImpl userServiceImpl() throws ClassNotFoundException {
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(userDao());
        userServiceImpl.setMailSender(mailSender());
        return userServiceImpl;
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

    @Bean
    public MessageFactoryBean message() {
        MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
        messageFactoryBean.setText("Factory Bean");
        return messageFactoryBean;
    }
}

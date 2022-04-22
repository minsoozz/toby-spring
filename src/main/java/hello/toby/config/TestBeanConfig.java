package hello.toby.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class TestBeanConfig {

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager();
    }
}

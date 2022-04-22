package hello.toby.transaction;

import static hello.toby.user.domain.Level.BASIC;
import static hello.toby.user.domain.Level.GOLD;
import static hello.toby.user.domain.Level.SILVER;
import static hello.toby.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static hello.toby.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;

import hello.toby.config.BeanConfig;
import hello.toby.config.TestBeanConfig;
import hello.toby.user.domain.User;
import hello.toby.user.service.UserService;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestBeanConfig.class, BeanConfig.class})
public class TransactionalTest {

    @Autowired
    UserService userService;

    @Autowired
    PlatformTransactionManager transactionManager;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("minsoo1", "일민수", "1", BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("minsoo2", "이민수", "2", BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("minsoo3", "삼민수", "3", SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("minsoo4", "삼민수", "3", SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("minsoo5", "삼민수", "3", GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    void transactionSync() throws SQLException {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    @Test
    @Transactional(readOnly = true)
    void transactionDelete() throws SQLException {
        userService.deleteAll();
    }
}

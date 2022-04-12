package hello.toby.user.service;

import static hello.toby.user.domain.Level.BASIC;
import static hello.toby.user.domain.Level.GOLD;
import static hello.toby.user.domain.Level.SILVER;
import static hello.toby.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static hello.toby.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import hello.toby.user.dao.DaoFactory;
import hello.toby.user.dao.UserDao;
import hello.toby.user.domain.User;
import hello.toby.user.service.UserService.TestUserService;
import hello.toby.user.service.UserService.TestUserServiceException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserServiceTest {

  @Autowired
  UserService userService;

  @Autowired
  UserDao userDao;

  @Autowired
  DataSource dataSource;

  @Autowired
  PlatformTransactionManager transactionManager;

  @Autowired
  MailSender mailSender;

  List<User> users;

  @Test
  void bean() {
    assertThat(this.userService).isNotNull();
  }

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
  @DirtiesContext
  void upgradeLevels() throws Exception {
    userDao.deleteAll();
    for (User user : users) {
      try {
        userDao.add(user);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    MockMailSender mockMailSender = new MockMailSender();
    userService.setMailSender(mockMailSender);

    userService.upgradeLevels();

    checkLevelUpgraded(users.get(0), false);
    checkLevelUpgraded(users.get(1), true);
    checkLevelUpgraded(users.get(2), false);
    checkLevelUpgraded(users.get(3), true);
    checkLevelUpgraded(users.get(4), false);

    List<String> request = mockMailSender.getRequests();
    assertThat(request.size()).isEqualTo(2);
    assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
    assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());
  }

  private void checkLevelUpgraded(User user, boolean upgraded) throws SQLException, ClassNotFoundException {
    User userUpdate = userDao.get(user.getId());
    if (upgraded) {
      assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
    } else {
      assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
    }
  }

  @Test
  void add() throws SQLException, ClassNotFoundException {
    userDao.deleteAll();

    User userWithLevel = users.get(4);
    User userWithoutLevel = users.get(0);
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    User userWithLevelRead = userDao.get(userWithLevel.getId());
    User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
    assertThat(userWithoutLevelRead.getLevel()).isEqualTo(BASIC);
  }

  @Test
  void upgradeAllOrNothing() throws Exception {
    UserService testUserService = new TestUserService(users.get(3).getId());
    testUserService.setUserDao(this.userDao); // userDao를 수동 DI 해준다
    testUserService.setTransactionManager(transactionManager);
    testUserService.setMailSender(mailSender);
    userDao.deleteAll();
    for (User user : users) {
      userDao.add(user);
    }

    try {
      testUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {

    }
    checkLevelUpgraded(users.get(1), false);
  }

  static class MockMailSender implements MailSender {

    private List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
      return requests;
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
      requests.add(simpleMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }
  }
}

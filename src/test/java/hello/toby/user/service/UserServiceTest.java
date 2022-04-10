package hello.toby.user.service;

import static hello.toby.user.domain.Level.BASIC;
import static hello.toby.user.domain.Level.GOLD;
import static hello.toby.user.domain.Level.SILVER;
import static org.assertj.core.api.Assertions.assertThat;

import hello.toby.user.dao.DaoFactory;
import hello.toby.user.dao.UserDao;
import hello.toby.user.domain.Level;
import hello.toby.user.domain.User;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserServiceTest {

  @Autowired
  UserService userService;

  @Autowired
  UserDao userDao;

  List<User> users;

  @Test
  void bean() {
    assertThat(this.userService).isNotNull();
  }

  @BeforeEach
  void setUp() {
    users = Arrays.asList(
        new User("minsoo1", "일민수", "1", BASIC, 49, 0),
        new User("minsoo2", "이민수", "2", BASIC, 50, 10),
        new User("minsoo3", "삼민수", "3", Level.SILVER, 60, 29),
        new User("minsoo4", "삼민수", "3", Level.SILVER, 60, 30),
        new User("minsoo5", "삼민수", "3", Level.GOLD, 100, 100)
    );
  }

  @Test
  void upgradeLevels() throws SQLException, ClassNotFoundException {
    userDao.deleteAll();
    for (User user : users) {
      try {
        userDao.add(user);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    userService.upgradeLevels();

    checkLevel(users.get(0), BASIC);
    checkLevel(users.get(1), SILVER);
    checkLevel(users.get(2), SILVER);
    checkLevel(users.get(3), GOLD);
    checkLevel(users.get(4), GOLD);
  }

  private void checkLevel(User user, Level expectedLevel) throws SQLException, ClassNotFoundException {
    User userUpdate = userDao.get(user.getId());
    Assertions.assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
  }
}

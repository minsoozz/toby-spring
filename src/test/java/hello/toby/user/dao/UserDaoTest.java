package hello.toby.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import hello.toby.user.domain.User;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

class UserDaoTest {

  private UserDao dao;

  private User user1;
  private User user2;
  private User user3;

  @BeforeEach
  void setUp() {

    dao = new UserDao();

    DataSource dataSource = new SingleConnectionDataSource(
        "jdbc:mysql://localhost/springbook", "root", "1234", true);

    dao.setDataSource(dataSource);

    this.user1 = new User("minsoo1", "일민수", "1");
    this.user2 = new User("minsoo2", "이민수", "2");
    this.user3 = new User("minsoo3", "삼민수", "3");
  }

  @Test
  void addAndGet() throws SQLException, ClassNotFoundException {

    dao.deleteAll();
    assertThat(dao.getCount()).isEqualTo(0);

    dao.add(user1);
    dao.add(user2);
    assertThat(dao.getCount()).isEqualTo(2);

    User userget1 = dao.get(user1.getId());
    assertThat(userget1.getName()).isEqualTo(user1.getName());
    assertThat(userget1.getPassword()).isEqualTo(user1.getPassword());

    User userget2 = dao.get(user2.getId());
    assertThat(userget2.getName()).isEqualTo(user2.getName());
    assertThat(userget2.getPassword()).isEqualTo(user2.getPassword());
  }

  @Test
  void count() throws SQLException, ClassNotFoundException {

    dao.deleteAll();
    assertThat(dao.getCount()).isEqualTo(0);

    dao.add(user1);
    assertThat(dao.getCount()).isEqualTo(1);

    dao.add(user2);
    assertThat(dao.getCount()).isEqualTo(2);

    dao.add(user3);
    assertThat(dao.getCount()).isEqualTo(3);
  }

  @Test
  void getUserFailure() throws SQLException {

    dao.deleteAll();
    assertThat(dao.getCount()).isEqualTo(0);

    Assertions.assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));

  }
}
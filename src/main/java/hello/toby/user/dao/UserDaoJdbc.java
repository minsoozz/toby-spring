package hello.toby.user.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import hello.toby.config.BeanConfig;
import hello.toby.exception.DuplicateUserIdException;
import hello.toby.user.domain.Level;
import hello.toby.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {

  private JdbcTemplate jdbcTemplate;

  private JdbcContext jdbcContext;

  private DataSource dataSource;

  public void setJdbcContext(JdbcContext jdbcContext) {
    this.jdbcContext = jdbcContext;
  }

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.dataSource = dataSource;
  }


  public void add() throws DuplicateUserIdException {
    try {
      // JDBC를 이용해 user 정보를 DB에 추가하는 코드 또는
      // 그런 기능 있는 다른 SQLException을 던지는 메소드를 호출하는 코드
      add(null);
    } catch (SQLException e) {
      if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
        throw new DuplicateUserIdException(e);
      } else {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void add(final User user) throws SQLException {
    jdbcContext.workWithStatementStrategy(new StatementStrategy() {

                                            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                                              PreparedStatement ps = c.prepareStatement(
                                                  "insert into users(id, name, password,level,login,recommend,email) values(?,?,?,?,?,?,?)");
                                              ps.setString(1, user.getId());
                                              ps.setString(2, user.getName());
                                              ps.setString(3, user.getPassword());
                                              ps.setInt(4, user.getLevel().intValue());
                                              ps.setInt(5, user.getLogin());
                                              ps.setInt(6, user.getRecommend());
                                              ps.setString(7, user.getEmail());

                                              return ps;
                                            }
                                          }
    );
  }

  @Override
  public User get(String id) throws ClassNotFoundException, SQLException {
    return this.jdbcTemplate.queryForObject("select * from users where id = ?",
        new Object[]{id},
        new RowMapper<User>() {
          @Override
          public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            System.out.println("user = " + user.getId());
            return user;
          }
        }
    );
  }

  @Override
  public void deleteAll() throws SQLException {
    this.jdbcTemplate.update("delete from users");
  }


  @Override
  public int getCount() throws SQLException {
    return this.jdbcTemplate.query(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        return con.prepareStatement("select count(*) from users");
      }
    }, new ResultSetExtractor<Integer>() {
      @Override
      public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
        rs.next();
        return rs.getInt(1);
      }
    });
  }

  @Override
  public void update(User user) {
    this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, " +
            "recommend = ?, email = ? where id = ?", user.getName(), user.getPassword(), user.getLevel().intValue(),
        user.getLogin(),
        user.getRecommend(), user.getId(), user.getEmail());
  }

  @Override
  public List<User> getAll() {
    return this.jdbcTemplate.query("select * from users order by id",
        new RowMapper<User>() {
          @Override
          public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level"))); // 추가
            user.setLogin(rs.getInt("login")); // 추가
            user.setRecommend(rs.getInt("recommend")); // 추가
            return user;
          }
        });
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    BeanConfig factory = new BeanConfig();
    UserDaoJdbc dao1 = factory.userDao();
    UserDaoJdbc dao2 = factory.userDao();

    System.out.println("dao1 = " + dao1);
    System.out.println("dao2 = " + dao2);

    ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfig.class);
    UserDaoJdbc dao3 = context.getBean("userDao", UserDaoJdbc.class);
    UserDaoJdbc dao4 = context.getBean("userDao", UserDaoJdbc.class);

    System.out.println("dao3 = " + dao3);
    System.out.println("dao4 = " + dao4);
  }
}

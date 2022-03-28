package hello.toby.user.dao;

import hello.toby.user.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class UserDao {

  private JdbcTemplate jdbcTemplate;

  private JdbcContext jdbcContext;

  private DataSource dataSource;


  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.dataSource = dataSource;
  }

  public void add(final User user) throws SQLException {
    jdbcContext.workWithStatementStrategy(new StatementStrategy() {

                                            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                                              PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
                                              ps.setString(1, user.getId());
                                              ps.setString(2, user.getName());
                                              ps.setString(3, user.getPassword());

                                              return ps;
                                            }
                                          }
    );
  }

  public User get(String id) throws ClassNotFoundException, SQLException {
    return this.jdbcTemplate.queryForObject("select * from user where id = ?",
        new Object[]{id},
        new RowMapper<User>() {
          @Override
          public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
          }
        }
    );
  }

  public void deleteAll() throws SQLException {
    this.jdbcTemplate.update("delete from users");
  }


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

  public List<User> getAll() {
    return this.jdbcTemplate.query("select * from users order by id",
        new RowMapper<User>() {
          @Override
          public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
          }
        });
  }

  public static void main(String[] args) throws SQLException, ClassNotFoundException {

    DaoFactory factory = new DaoFactory();
    UserDao dao1 = factory.userDao();
    UserDao dao2 = factory.userDao();

    System.out.println("dao1 = " + dao1);
    System.out.println("dao2 = " + dao2);

    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    UserDao dao3 = context.getBean("userDao", UserDao.class);
    UserDao dao4 = context.getBean("userDao", UserDao.class);

    System.out.println("dao3 = " + dao3);
    System.out.println("dao4 = " + dao4);
  }
}

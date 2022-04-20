package hello.toby.user.service;

import hello.toby.user.domain.User;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

public class UserServiceTx implements UserService {

  UserService userService;
  PlatformTransactionManager transactionManager;

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void add(User user) throws SQLException {
    userService.add(user);
  }

  @Override
  public User get(String id) throws SQLException, ClassNotFoundException {
    return userService.get(id);
  }

  @Override
  public List<User> getAll() {
    return userService.getAll();
  }

  @Override
  public void deleteAll() throws SQLException {
    userService.deleteAll();
  }

  @Override
  public void update(User user) {
    userService.update(user);
  }

  @Override
  public void upgradeLevels() {
    TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

    try {
      userService.upgradeLevels();
      this.transactionManager.commit(status);
    } catch (RuntimeException e) {
      this.transactionManager.rollback(status);
      throw e;
    }
  }
}

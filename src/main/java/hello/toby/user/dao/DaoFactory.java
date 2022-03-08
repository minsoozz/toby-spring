package hello.toby.user.dao;

public class DaoFactory {

  public UserDao userDao() {
    ConnectionMaker connectionMaker = new DConnectionMaker();
    return new UserDao(connectionMaker);
  }

  public AccountDao accountDao() {
    return new AccountDao(connectionMaker());
  }

  public MessageDao messageDao() {
    return new MessageDao(connectionMaker());
  }

  private ConnectionMaker connectionMaker() {
    return new DConnectionMaker();
  }
}

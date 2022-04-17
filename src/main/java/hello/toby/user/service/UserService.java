package hello.toby.user.service;

import hello.toby.user.domain.User;

import java.sql.SQLException;

public interface UserService {

  void add(User user) throws SQLException;
  void upgradeLevels();
}

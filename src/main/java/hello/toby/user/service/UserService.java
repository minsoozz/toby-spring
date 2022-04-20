package hello.toby.user.service;

import hello.toby.user.domain.User;
import java.sql.SQLException;
import java.util.List;

public interface UserService {

    void add(User user) throws SQLException;

    User get(String id) throws SQLException, ClassNotFoundException;

    List<User> getAll();

    void deleteAll() throws SQLException;

    void update(User user);

    void upgradeLevels();
}

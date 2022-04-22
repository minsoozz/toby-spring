package hello.toby.user.service;

import hello.toby.user.domain.User;
import java.sql.SQLException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserService {

    void add(User user) throws SQLException;

    @Transactional(readOnly = true)
    User get(String id) throws SQLException, ClassNotFoundException;

    @Transactional(readOnly = true)
    List<User> getAll();

    void deleteAll() throws SQLException;

    void update(User user);

    void upgradeLevels();
}

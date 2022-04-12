package hello.toby.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import hello.toby.user.domain.Level;
import hello.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

  User user;

  @BeforeEach
  void setUp() {
    user = new User();
  }

  @Test
  void upgradeLevel() {
    Level[] levels = Level.values();
    for (Level level : levels) {
      if (level.nextLevel() == null) {
        continue;
      }
      user.setLevel(level);
      user.upgradeLevel();
      assertThat(user.getLevel()).isEqualTo(level.nextLevel());
    }
  }

  @Test
  void cannotUpgradeLevel() {
    Level[] levels = Level.values();
    for (Level level : levels) {
      if (level.nextLevel() != null) {
        continue;
      }
      user.setLevel(level);
      assertThrows(IllegalArgumentException.class, () ->
          user.upgradeLevel());
    }
  }
}

package hello.toby.user.service;

import hello.toby.user.domain.User;

public interface UserLevelUpgradePolicy {

  boolean canUpgradeLevel(User user);

  void upgradeLevel(User user);
}

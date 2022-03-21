package hello.toby.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class JUnitTest {

  @Autowired
  ApplicationContext applicationContext;
  JUnitTest jUnitTest = this;
  static Set<ApplicationContext> applicationContexts = new HashSet<>();
  static Set<JUnitTest> junitTests = new HashSet<>();

  @AfterAll
  public static void afterAll() {
    assertEquals(applicationContexts.size(), 1);
    assertEquals(junitTests.size(), 3);
  }

  @Test
  public void test1() {
    applicationContexts.add(applicationContext);
    junitTests.add(jUnitTest);
  }

  @Test
  public void test2() {
    applicationContexts.add(applicationContext);
    junitTests.add(jUnitTest);
  }

  @Test
  public void test3() {
    applicationContexts.add(applicationContext);
    junitTests.add(jUnitTest);
  }
}
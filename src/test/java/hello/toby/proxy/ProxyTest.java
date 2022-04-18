package hello.toby.proxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class ProxyTest {

    @Test
    void simpleProxy1() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }

    @Test
    void simpleProxy2() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(hello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    @Test
    void proxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
    }
}

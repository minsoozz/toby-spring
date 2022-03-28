package hello.toby.template;

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalcSumTest {

  Calculator calculator;
  String numFilepath;

  @BeforeEach
  void setUp() {
    this.calculator = new Calculator();
    this.numFilepath = getClass().getResource("/numbers.txt").getPath();
  }

  @Test
  void sumOfNumbers() throws IOException {
    int sum = calculator.calcSum(numFilepath);
    Assertions.assertThat(sum).isEqualTo(10);
  }

  @Test
  void multiplyOfNumbers() throws IOException {
    Assertions.assertThat(calculator.calMultiply(this.numFilepath)).isEqualTo(24);
  }
}

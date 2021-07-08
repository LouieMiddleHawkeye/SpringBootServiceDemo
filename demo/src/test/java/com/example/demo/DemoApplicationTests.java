package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DemoApplicationTests {

	Calculator calculator = new Calculator();

	@Test
	void itShouldAddTwoNumbers() {
		// given
		int numberOne = 10;
		int numberTwo = 20;

		// when
		int result = calculator.add(numberOne, numberTwo);

		// then
		assertThat(result).isEqualTo(30);
	}

	class Calculator {
		int add(int a, int b) { return a + b; }
	}

}

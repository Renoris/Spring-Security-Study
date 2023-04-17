package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}


	public void test() {
		long a = 223147483647L;
		System.out.println(Long.toBinaryString(a));
		System.out.println(a);

		int b = (int) a;
		System.out.println(Integer.toBinaryString(b));
		System.out.println(b);
	}
}

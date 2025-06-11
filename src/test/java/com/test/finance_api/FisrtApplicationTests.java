package com.test.finance_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class FinanceApiTests {

	@Test
	void contextLoads() {
		ArrayList<String> bands = new ArrayList<>();
		bands.add("Lenine");
		bands.add("Doja Cat");
		bands.add("Emicida");

		for(String band : bands) {
			System.out.println(band);
		}
	}

}

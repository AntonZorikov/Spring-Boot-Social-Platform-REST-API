package com.example.jeddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(JedditApplication.class, args);
	}
	/*TODO:
		Add null annotations

		Add unique annotations

		Commentary entity:
			- id
			- text
			- date
			- user ManyToOne
			- post ManyToOne

		api - ?

	 */
}

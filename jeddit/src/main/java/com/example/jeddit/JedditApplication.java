package com.example.jeddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(JedditApplication.class, args);
	}
	/*TODO:
		Posts entity
			- id
			- title
			- text
			- date
			- communityId ManyToOne
			- userId ManyToOne
			- rating
			- commentaries OneToMany

		Post controller
			api:
				- post: post/
					- JWTTokenRequest
				- get: post/{id}
				- put: post/{id}
					- UpdatePostRequest model
						- token
						- addedText
				- delete: post/{id}
					- JWTTokenRequest
				- post: post/{id}/upvote
					- JWTTokenRequest
				- post: post/{id}/downvote
					- JWTTokenRequest
				- delete: post/{id}/vote
					- JWTTokenRequest
				- get: post/{id}/vote
					- JWTTokenRequest

		UserService create carma calculate function

		Add null annotations

		Add unique annotations

	 */
}

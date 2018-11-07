package com.Profile;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.Profile.model.Block;
import com.Profile.model.Person;
import com.Profile.repository.PersonRepository;

@SpringBootApplication
public class ProfileApplication {

	//public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty = 1;
	
	public static void main(String[] args) {
		SpringApplication.run(ProfileApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demo(PersonRepository repository) {
		return (args) -> {
			// save a couple of customers
			
			
			// fetch all customers
			//for (Block block  : repository.findAll()) {
				//System.out.println(block.toString());
			//}
		};
	}

}

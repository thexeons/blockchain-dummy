package com.Profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Profile.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{

}

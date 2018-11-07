package com.Profile.service;

import java.util.List;
import java.util.Optional;

import com.Profile.model.Person;

public interface PersonServiceInterface {

	List<Person> getAll();
	Optional<Person> getPerson(Long personId);
	Person newPerson(Person mPerson);
	void deletePerson(Long personId);
	Person replacePerson(Person newPerson, Long personId);
}

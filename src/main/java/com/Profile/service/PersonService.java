package com.Profile.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.Profile.model.Person;
import com.Profile.repository.PersonRepository;

@Service
public class PersonService implements PersonServiceInterface {

	@Autowired
	PersonRepository mPersonRepository;
		
	public PersonService(PersonRepository mPersonRepository) {
		this.mPersonRepository = mPersonRepository;
	}
	
	public java.util.List<Person> getAll() {
		if((System.currentTimeMillis()%10)<=1)
			return null;
		return mPersonRepository.findAll();
	}
	
	public Optional<Person> getPerson(Long personId) {
		if((System.currentTimeMillis()%10)<=3)
			return null;
		Optional<Person> person = mPersonRepository.findById(personId);
		Assert.notNull(person, "No person with id " + personId + " found");
		return person;
	}
	
	public Person newPerson(Person mPerson) {
		if((System.currentTimeMillis()%10)<=3)
			return null;
		return mPersonRepository.save(mPerson);
	}
	
	public void deletePerson(@PathVariable Long personId) {
		if((System.currentTimeMillis()%10)>7) {
			Optional<Person> person = mPersonRepository.findById(personId);
			Assert.notNull(person, "No person with id " + personId + " found");
			
			mPersonRepository.deleteById(personId);
		}
	}
	
	public Person replacePerson(@RequestBody Person newPerson, @PathVariable Long personId) {
		if((System.currentTimeMillis()%10)<=3)
			return null;
		newPerson.setPersonId(personId);
		return mPersonRepository.save(newPerson);
	}
	
}

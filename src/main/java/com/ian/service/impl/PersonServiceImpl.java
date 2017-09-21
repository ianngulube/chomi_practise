/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.Message;
import com.ian.entity.Person;
import com.ian.entity.Request;
import com.ian.service.PersonService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private MongoTemplate personMongoTemplate;

    @Override
    public void savePerson(Person person) {
        if (!personMongoTemplate.collectionExists(Person.class)) {
            personMongoTemplate.createCollection(Person.class);
        }
        personMongoTemplate.insert(person, "Person");
    }

    @Override
    public List<Person> listPersons() {
        return personMongoTemplate.findAll(Person.class, "Person");
    }

    @Override
    public Person findPerson(String id) {
        return personMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Person.class, "Person");
    }

    @Override
    public void updatePerson(Person newPerson) {
        Person person = personMongoTemplate.findOne(Query.query(Criteria.where("id").is(newPerson.getId())), Person.class, "Person");
        person.setAge(newPerson.getAge());
        person.setBlockedPersonsIds(newPerson.getBlockedPersonsIds());
        person.setCountryId(newPerson.getCountryId());
        person.setDob(newPerson.getDob());
        person.setFirstName(newPerson.getFirstName());
        person.setFriendsIds(newPerson.getFriendsIds());
        person.setGender(newPerson.getGender());
        person.setLanguage(newPerson.getLanguage());
        person.setLastName(newPerson.getLastName());
        person.setMsisdn(newPerson.getMsisdn());
        person.setNetworkProvider(newPerson.getNetworkProvider());
        person.setPassword(newPerson.getPassword());
        person.setProvinceId(newPerson.getProvinceId());
        person.setUserName(newPerson.getUserName());
        personMongoTemplate.save(person, "Person");
    }

    @Override
    public void deletePerson(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Person findPersonByMsisdn(String msisdn) {
        return personMongoTemplate.findOne(Query.query(Criteria.where("msisdn").is(msisdn)), Person.class, "Person");
    }

    @Override
    public List<Person> findPersonByUserName(String userName) {
        return personMongoTemplate.find(Query.query(Criteria.where("userName").is(userName)), Person.class, "Person");
    }

    @Override
    public List<Person> findPersonByProvinceAndGender(String province, String gender) {
        return personMongoTemplate.find(Query.query(Criteria.where("provinceId").is(province)).addCriteria(Criteria.where("gender").is(gender)), Person.class, "Person");
    }

    @Override
    public List<Person> findPeopleInTheFriendRequestsList(List<Request> requestList) {
        List<Person> persons = new ArrayList();
        int x = 0;
        for (Request list : requestList) {
            persons.add(personMongoTemplate.findOne(Query.query(Criteria.where("id").is(list.getOrigin())), Person.class, "Person"));
            x++;
        }
        return persons;
    }

    @Override
    public List<Person> findAllMyChomis(String msisdn) {
        List<String> friendIds = personMongoTemplate.findOne(Query.query(Criteria.where("msisdn").is(msisdn)), Person.class, "Person").getFriendsIds();
        List<Person> persons = new ArrayList<>();
        if (friendIds != null) {
            for (String id : friendIds) {
                persons.add(this.findPerson(id));
            }
        }
        return persons;
    }

    @Override
    public List<Person> findPeopleInTheMessagesList(List<Message> messages) {
        List<String> personIds = new ArrayList();
        for (Message message : messages) {
            if (!personIds.contains(message.getFrom())) {
                personIds.add(message.getFrom());
            }
        }
        List<Person> persons = new ArrayList();
        for (String str : personIds) {
            persons.add(personMongoTemplate.findOne(Query.query(Criteria.where("id").is(str)), Person.class, "Person"));
        }
        return persons;
    }

    @Override
    public List<Person> findPeopleGivenChomiIds(List<String> chomiIds) {
        List<Person> persons = new ArrayList();
        for (String id : chomiIds) {
            persons.add(personMongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Person.class, "Person"));
        }
        return persons;
    }

    @Override
    public boolean personExistsAsMyChomi(String msisdn, String id) {
        List<Person> mychomis = this.findAllMyChomis(msisdn);
        for (Person p : mychomis) {
            if (p.getId().equalsIgnoreCase(id)) {
                System.out.println("=====================================" + p.getMsisdn() + "============================================");
                return true;
            }
        }
        return false;
    }

}

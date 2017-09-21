/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Message;
import com.ian.entity.Person;
import com.ian.entity.Request;
import java.util.List;

public interface PersonService {

    void savePerson(Person person);

    List<Person> listPersons();

    Person findPerson(String id);

    List<Person> findPersonByUserName(String userName);

    List<Person> findPersonByProvinceAndGender(String province, String gender);

    List<Person> findPeopleInTheFriendRequestsList(List<Request> requestList);

    Person findPersonByMsisdn(String msisdn);

    void updatePerson(Person newPerson);

    void deletePerson(String id);

    List<Person> findAllMyChomis(String msisdn);

    List<Person> findPeopleInTheMessagesList(List<Message> messages);

    List<Person> findPeopleGivenChomiIds(List<String> chomiIds);

    boolean personExistsAsMyChomi(String msisdn, String id);
}

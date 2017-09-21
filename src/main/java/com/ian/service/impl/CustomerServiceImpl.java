/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service.impl;

import com.ian.entity.Customer;
import com.ian.service.CustomerService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private MongoTemplate customermongoTemplate;

    @Override
    public void saveCustomer(Customer customer) {
        if (!customermongoTemplate.collectionExists(Customer.class)) {
            customermongoTemplate.createCollection(Customer.class);
        }
        customer.setLastModified(new Date());
        customermongoTemplate.insert(customer, "customerTable");
    }

    @Override
    public List<Customer> listCustomers() {
        return customermongoTemplate.findAll(Customer.class, "customerTable");
    }

    @Override
    public Customer findCustomer(String id) {
        return customermongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Customer.class, "customerTable");
    }

    @Override
    public void updateCustomer(Customer newCustomer) {
        Customer customer = customermongoTemplate.findOne(Query.query(Criteria.where("id").is(newCustomer.getId())), Customer.class, "customerTable");
        customer.setName(newCustomer.getName());
        customer.setAddress(newCustomer.getAddress());
        customer.setLastModified(new Date());
        customermongoTemplate.save(customer, "customerTable");
    }

    @Override
    public void deleteCustomer(String id) {
        customermongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Customer.class, "customerTable");
    }
}

/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.service;

import com.ian.entity.Customer;
import java.util.List;

public interface CustomerService {

    void saveCustomer(Customer customer);

    List<Customer> listCustomers();

    Customer findCustomer(String id);

    void updateCustomer(Customer newCustomer);

    void deleteCustomer(String id);
}

package iota.com.service;

import iota.com.core.EntityManager;
import iota.com.model.Customer;

import java.util.List;

public class CustomerManager {
    private final EntityManager entityManager;

    public CustomerManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addCustomer(Customer customer) throws Exception {
        entityManager.persist(customer);
    }

    public Customer findCustomerById(Long id) throws Exception {
        return entityManager.find(Customer.class, id);
    }

    public List<Customer> findAllCustomers() throws Exception {
        String query = "SELECT * FROM customer";
        return entityManager.query(Customer.class, query, List.of());
    }

    public void updateCustomer(Customer customer) throws Exception {
        entityManager.update(customer);
    }

    public void deleteCustomer(Long id) throws Exception {
        entityManager.delete(Customer.class, id);
    }
}


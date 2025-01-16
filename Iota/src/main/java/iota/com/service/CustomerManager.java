package iota.com.service;

import iota.com.core.EntityManager;
import iota.com.model.Customer;
import iota.com.model.Booking;

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

    /**
     * Delete customer and their related bookings as a single transaction.
     */
    public void deleteCustomerWithBookings(Long customerId) throws Exception {
        entityManager.beginTransaction();
        try {
            Customer customer = findCustomerById(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("No customer found with ID: " + customerId);
            }

            String bookingQuery = "SELECT * FROM booking WHERE customer_id = ?";
            List<Booking> bookings = entityManager.query(Booking.class, bookingQuery, List.of(customerId));

            for (Booking booking : bookings) {
                entityManager.delete(Booking.class, booking.getId());
            }

            entityManager.delete(Customer.class, customerId);
            entityManager.commitTransaction();
        } catch (Exception e) {
            entityManager.rollbackTransaction();
            throw e; // Propagate the exception
        }
    }
}

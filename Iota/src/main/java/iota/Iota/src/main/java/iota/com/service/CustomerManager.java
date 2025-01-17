package iota.com.service;

import iota.com.core.EntityManager;
import iota.com.model.Customer;
import iota.com.model.Booking;

import java.util.List;

/**
 * This class manages customer-related operations using the provided EntityManager.
 */
public class CustomerManager {
    private final EntityManager entityManager;

    /**
     * Constructs a new CustomerManager instance with the given EntityManager.
     *
     * @param entityManager The EntityManager to be used for database operations.
     */
    public CustomerManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Adds a new customer to the database.
     *
     * @param customer The customer to be added.
     * @throws Exception If an error occurs during the database operation.
     */
    public void addCustomer(Customer customer) throws Exception {
        entityManager.persist(customer);
    }

    /**
     * Finds a customer by their ID.
     *
     * @param id The ID of the customer to be found.
     * @return The customer with the given ID, or null if not found.
     * @throws Exception If an error occurs during the database operation.
     */
    public Customer findCustomerById(Long id) throws Exception {
        return entityManager.find(Customer.class, id);
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return A list of all customers.
     * @throws Exception If an error occurs during the database operation.
     */
    public List<Customer> findAllCustomers() throws Exception {
        String query = "SELECT * FROM customer";
        return entityManager.query(Customer.class, query, List.of());
    }

    /**
     * Updates an existing customer in the database.
     *
     * @param customer The customer to be updated.
     * @throws Exception If an error occurs during the database operation.
     */
    public void updateCustomer(Customer customer) throws Exception {
        entityManager.update(customer);
    }

    /**
     * Deletes a customer from the database by their ID.
     *
     * @param id The ID of the customer to be deleted.
     * @throws Exception If an error occurs during the database operation.
     */
    public void deleteCustomer(Long id) throws Exception {
        entityManager.delete(Customer.class, id);
    }

    /**
     * Deletes a customer and their related bookings as a single transaction.
     *
     * @param customerId The ID of the customer to be deleted.
     * @throws Exception If an error occurs during the database operation or if the customer is not found.
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

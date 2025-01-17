package iota.com.managers;

import iota.com.core.EntityManager;
import iota.com.model.Service;

import java.util.List;

public class ServiceManager {
    private final EntityManager entityManager;

    public ServiceManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Add a new service.
     */
    public void addService(Service service) throws Exception {
        entityManager.persist(service);
    }

    /**
     * Retrieve a service by ID.
     */
    public Service getServiceById(Long serviceId) throws Exception {
        return entityManager.find(Service.class, serviceId);
    }

    /**
     * Update a service's details.
     */
    public void updateService(Service service) throws Exception {
        entityManager.update(service);
    }

    /**
     * Retrieve all services.
     */
    public List<Service> getAllServices() throws Exception {
        String query = "SELECT * FROM service";
        return entityManager.query(Service.class, query, null);
    }

    /**
     * Delete a service.
     */
    public void deleteService(Long serviceId) throws Exception {
        entityManager.delete(Service.class, serviceId);
    }
}

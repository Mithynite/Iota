package iota.com.utils;

import iota.com.core.EntityManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * Utility for importing CSV data into tables using EntityManager.
 */
public class CsvManager {
    private final EntityManager entityManager;

    public CsvManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Imports CSV data into a specific entity class.
     *
     * @param clazz       The class of the entity (e.g., Room.class, Customer.class).
     * @param csvFilePath The path to the CSV file.
     * @param columnMapping The ordered list of field names (matching the CSV order) to map data to the entity.
     * @throws Exception If an error occurs during the process.
     */
    public <T> void importCsv(Class<T> clazz, String csvFilePath, List<String> columnMapping) throws Exception {
        // Read the CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean headerSkipped = false;

            entityManager.beginTransaction();
            try {
                while ((line = reader.readLine()) != null) {
                    if (!headerSkipped) {
                        headerSkipped = true; // Skip header row
                        continue;
                    }

                    // Parse the CSV row into entity
                    String[] data = line.split(",");
                    if (data.length != columnMapping.size()) {
                        throw new IllegalArgumentException("Mismatch between CSV columns and field mapping.");
                    }

                    T entity = ReflectionUtils.mapCsvRowToEntity(clazz, columnMapping, data);
                    // Persist the entity
                    entityManager.persist(entity);
                }

                entityManager.commitTransaction();
                System.out.println("CSV data imported successfully into " + clazz.getSimpleName() + ".");
            } catch (Exception e) {
                entityManager.rollbackTransaction();
                throw new Exception("Error during CSV import: " + e.getMessage(), e);
            }
        }
    }
}

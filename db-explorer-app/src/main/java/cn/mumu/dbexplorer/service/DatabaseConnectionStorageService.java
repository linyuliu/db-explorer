package cn.mumu.dbexplorer.service;

import cn.mumu.dbexplorer.entity.DatabaseConnection;
import cn.mumu.dbexplorer.repository.DatabaseConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing stored database connections
 */
@Service
public class DatabaseConnectionStorageService {
    
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionStorageService.class);
    
    @Autowired
    private DatabaseConnectionRepository repository;
    
    /**
     * Save a new database connection configuration
     */
    public DatabaseConnection saveConnection(DatabaseConnection connection) {
        log.info("Saving database connection: {}", connection.getName());
        return repository.save(connection);
    }
    
    /**
     * Find all stored connections
     */
    public List<DatabaseConnection> getAllConnections() {
        return repository.findAll();
    }
    
    /**
     * Find connection by ID
     */
    public Optional<DatabaseConnection> findById(Long id) {
        return repository.findById(id);
    }
    
    /**
     * Find connection by name
     */
    public Optional<DatabaseConnection> findByName(String name) {
        return repository.findByName(name);
    }
    
    /**
     * Find connections by database type
     */
    public List<DatabaseConnection> findByDatabaseType(String databaseType) {
        return repository.findByDatabaseType(databaseType);
    }
    
    /**
     * Update an existing connection
     */
    public DatabaseConnection updateConnection(DatabaseConnection connection) {
        log.info("Updating database connection: {}", connection.getName());
        return repository.save(connection);
    }
    
    /**
     * Delete a connection by ID
     */
    public void deleteConnection(Long id) {
        log.info("Deleting database connection with ID: {}", id);
        repository.deleteById(id);
    }
    
    /**
     * Check if connection name already exists
     */
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
    
    /**
     * Initialize with some default connections for testing
     */
    public void initializeDefaultConnections() {
        if (repository.count() == 0) {
            log.info("Initializing default H2 embedded connection");
            
            DatabaseConnection h2Connection = new DatabaseConnection();
            h2Connection.setName("H2 Embedded");
            h2Connection.setDatabaseType("h2");
            h2Connection.setDatabase("dbexplorer");
            h2Connection.setUsername("sa");
            h2Connection.setPassword("");
            h2Connection.setDescription("Default H2 embedded database for testing");
            
            saveConnection(h2Connection);
        }
    }
}